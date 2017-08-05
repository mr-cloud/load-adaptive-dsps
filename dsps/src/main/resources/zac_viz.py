import json
import os
# Parse ISO date
import dateutil.parser as dapa
import pandas as pd
from matplotlib import pyplot as plt
import numpy as np


strategy_indices = {
    'FIXED': 0,
    'OPTIMIZED': 1,
    'FINED': 2
}

strategy_labels = {
    0: 'FIX({} ,{})',
    1: 'OPT({}, {})',
    2: 'FIN({}, {})'
}

legend_labels = ['COST', 'SLA']
ylabel = 'COST/SLA: Based on FIX'
xlabel = 'strategies'

plan_colors = {
    0: ['#ff1a1a', '#1aff1a'],
    1: ['#1a1aff', '#ff1aff'],
    2: ['#1affff', '#0d0d0d']
}

plan_keys = {
    0: 'WIGGLE',
    1: 'OVERLOADED',
    2: 'UNDERLOADED'
}


def perform(perform_map, output, x_tick_labels):
    if len(perform_map) == 1:
        ax = [None]
        fig, ax[0] = plt.subplots(1, 1, sharey=True)
    else:
        fig, ax = plt.subplots(1, len(perform_map), sharey=True)
    handles = []
    for idx, plan in enumerate(perform_map):
        df = perform_map[plan]
        df = df.sort_index()
        x = list(df.index)
        expand_coe = 1.0
        x_axis = list(expand_coe * np.asarray(range(1, len(x) + 1)))
        cols = list(df.columns)

        # Warning: hard coding for convenience drawing.
        sla_scale = df.loc[1, 'sla'] / df.loc[0, 'sla']
        y_limit = max(sla_scale, 1.5)
        # ax[idx].axhline(y=1, ls='--', lw=2, c='black')
        for i in range(len(x_tick_labels)):
            x_tick_labels[i] = x_tick_labels[i].format(np.round(df.loc[i, 'cost'], 2), np.round(df.loc[i, 'sla'], 2))

        for idx2, col in enumerate(cols):
            handle = None
            if idx2 == 0:
                handle = ax[idx].bar(x_axis, np.array(df.loc[:, col].values) / df.loc[:, col].values[0], width=0.618,
                                     color=plan_colors[plan][idx2], align='center', edgecolor='none')
            elif idx2 == 1:
                handle, = ax[idx].plot(x_axis, np.array(df.loc[:, col].values) / df.loc[:, col].values[0],
                                      color=plan_colors[plan][idx2],
                                      linestyle='-', marker='^', markeredgecolor=plan_colors[plan][idx2],
                                      label=None)
            else:
                pass
            if handle is not None:
                handles.append(handle)
        ax[idx].set(xlabel=xlabel)
        ax[idx].set_xlim([0, expand_coe*(len(x) + 1)])
        ax[idx].set_ylim([0, y_limit])
        ax[idx].set_xticks(x_axis)
        ax[idx].set_xticklabels(x_tick_labels)
        ax[idx].text(0.04, 9, plan_keys[plan], size='large')
    fig.legend(handles, legend_labels, 'upper center', ncol=len(legend_labels), frameon=False)
    fig.text(0.04, 0.5, ylabel, va='center', rotation='vertical', size='large')
    # plt.show()
    if not os.path.exists(os.path.abspath(os.path.join(output, os.pardir))):
        os.mkdir(os.path.abspath(os.path.join(output, os.pardir)))
    plt.savefig(output) # Competition with show().
    plt.close()



def draw_stats(metrics_dir, output):
    perform_map = {}
    strategy_list = []
    for metrics_file in os.listdir(metrics_dir):
        metrics = json.loads(open(metrics_dir + '/' + metrics_file, 'r').read())
        if len(metrics) <= 1:
            continue
        plan = int(metrics[0]['plan'])
        strategy = strategy_indices[metrics[0]['strategy']]
        if not strategy_list.__contains__(strategy):
            strategy_list.append(strategy)
        cost = 0.0
        sla = 0.0
        total_t = 0.0
        # drop out the first trace.
        for idx in range(2, len(metrics)):
            interval = (dapa.parse(metrics[idx]['date']['$date'])
                        - dapa.parse(metrics[idx - 1]['date']['$date'])).total_seconds()
            total_t += interval
            cost += metrics[idx - 1]['machinesRunning'] * interval
            input_rate = (int(metrics[idx]['messagesTotal']['$numberLong'])
                          - int(metrics[idx - 1]['messagesTotal']['$numberLong']))
            throughput = (int(metrics[idx]['messagesConsumed']['$numberLong'])
                          - int(metrics[idx - 1]['messagesConsumed']['$numberLong']))
            if input_rate > throughput:
                sla += 1
        cost /= total_t
        sla /= len(metrics) - 1
        if not perform_map.__contains__(plan):
            df = pd.DataFrame({'cost': cost, 'sla': sla}, index=[strategy])
            perform_map[plan] = df
        else:
            df = perform_map[plan]
            df.loc[strategy, 'cost'] = cost
            df.loc[strategy, 'sla'] = sla
    x_ticks_labels = []
    strategy_list.sort()
    for stra in strategy_list:
        x_ticks_labels.append(strategy_labels[stra])
    perform(perform_map, output, x_ticks_labels)


def fast_and_furious(metrics_dir, output):
    for metrics_file in os.listdir(metrics_dir):
        metrics = json.loads(open(metrics_dir + '/' + metrics_file, 'r').read())
        if len(metrics) <= 1:
            continue
        driver = str.rsplit(metrics_file, '.')[0]
        leader = list()
        follower = list()
        for idx in range(2, len(metrics)):
            interval = (dapa.parse(metrics[idx]['date']['$date'])
                        - dapa.parse(metrics[idx - 1]['date']['$date'])).total_seconds()
            leader.append(
                ((int(metrics[idx]['messagesTotal']['$numberLong'])
                 - int(metrics[idx - 1]['messagesTotal']['$numberLong']))
                 / interval)
            )
            follower.append(
                metrics[idx - 1]['machinesRunning']
            )
        handles = list()
        labels = list()
        avg_input_rate = int(np.round(np.mean(leader)))
        labels.append('average input rate: {} msg/s'.format(avg_input_rate))
        avg_mac_num = np.round(np.mean(follower), 2)
        labels.append('average cost: {} machines'.format(avg_mac_num))
        handles.append(plt.plot(list(range(len(leader))), list(np.array(leader) / avg_input_rate), ls='--', lw=2, c='red')[0])
        handles.append(plt.plot(list(range(len(leader))), list(np.array(follower) / avg_mac_num), ls='-', lw=2, c='green')[0])

        plt.figlegend(handles, labels, 'upper center', ncol=2, frameon=False)
        plt.figtext(0.04, 0.5, 'Auto-scaling Zac', va='center', rotation='vertical', size='large')
        plt.xlabel('time (interval 10 seconds)')
        # plt.show()
        plt.savefig(output + driver + '.pdf')
        plt.close()


def main():
    draw_stats(metrics_dir='zac-metrics/effectiveness', output='zac-viz/effectiveness.pdf')
    # draw_stats(metrics_dir='zac-metrics/robustness', output='zac-viz/robustness.pdf')
    # fast_and_furious(metrics_dir='zac-metrics/effectiveness', output='zac-viz/fast&furious_')

if __name__ == '__main__':
    main()