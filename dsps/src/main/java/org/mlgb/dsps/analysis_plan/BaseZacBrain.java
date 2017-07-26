package org.mlgb.dsps.analysis_plan;

import java.util.Properties;

import org.mlgb.dsps.executor.ScalerFactory;
import org.mlgb.dsps.executor.ScalingExecutor;
import org.mlgb.dsps.monitor.Jones;
import org.mlgb.dsps.monitor.JonesFactory;
import org.mlgb.dsps.monitor.Planning;
import org.mlgb.dsps.monitor.PlanningFactory;
import org.mlgb.dsps.util.Consts;
import org.mlgb.dsps.util.vo.BoltVO;

import uni.akilis.helper.LoggerX;

/**
 * Analyze the metrics supported by OnlineProfiler,
 * and make a plan for scaling out or scaling in. You should
 * inherit this base class and implement your own schedule 
 * for auto-scaling.
 * 
 * @author Leo
 *
 */
public class BaseZacBrain{
    public Jones jones;
    public OnlineProfiler profiler;
    public ScalingExecutor scaler;
    public String strategy = Consts.STRATEGY_FIXED_THRESHOLD;
    public Planning plan = PlanningFactory.createPlanning();
    public boolean FINED_SCALING_TOGGLE = false;
    public boolean tuning = false;
    public Thread brain;

    public static final String TAG = BaseZacBrain.class.getName();

    public boolean isTuning() {
        return tuning;
    }
    public void setTuning(boolean tuning) {
        this.tuning = tuning;
    }
    public boolean isFINED_SCALING_TOGGLE() {
        return FINED_SCALING_TOGGLE;
    }
    public void setFINED_SCALING_TOGGLE(boolean fINED_SCALING_TOGGLE) {
        FINED_SCALING_TOGGLE = fINED_SCALING_TOGGLE;
    }
    public String getStrategy() {
        return strategy;
    }
    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }
    public Planning getPlan() {
        return plan;
    }
    public void setPlan(Planning plan) {
        this.plan = plan;
    }

    public BaseZacBrain(){
        this.jones = JonesFactory.createJones();
        this.profiler = new OnlineProfiler(jones);
        this.scaler = ScalerFactory.createScaler();
    }

    /**
     * Scaling Strategy
     * 
     * Note: Override this method when inheriting this base class. 
     */
    public void brainStorming(){
        if (this.strategy == null || this.strategy.isEmpty() || this.plan == null) {
            LoggerX.error("Neither strategy nor plan can be empty!");
            System.exit(1);
        }
        this.jones.uprising(Consts.TOPIC, this.plan, this.strategy);
        try {
            Thread.sleep(Consts.BRAIN_WAKE_UP_TIME * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.profiler.profilerOnline();
        this.brain = new Thread(new Brain());
        this.brain.start();
    }

    /**
     * Exit for performance evaluation. 
     * Usually set a timer to call this method.
     */
    public void exit(){
        try {
            this.brain.interrupt();
            Thread.sleep(1000);
            this.profiler.snapshot();
            Thread.sleep(1000);
            this.jones.hibernate();
            Thread.sleep(1000);
            this.scaler.shutdown();
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    class Brain implements Runnable {
        @Override
        public void run() {
            while (!Thread.interrupted()){
                if (!tuning) {  // if tuning toggle is open, no strategy will be applied.
                    if (profiler.curCap == null 
                            || profiler.curCap.size() == 0) {
                        // Do nothing.
                    }
                    else {
                        // Check the capacity of the most urgent component.
                        LoggerX.debug(TAG, "\nCapacities:");
                        for (BoltVO ele: profiler.curCap) {
                            LoggerX.debug(ele.getCapacity());
                        }
                        BoltVO maxBolt = profiler.curCap.get(0);
                        double maxCap = Double.parseDouble(maxBolt.getCapacity());
                        // Filter invalid samples.
                        /*                        if (Math.abs(maxCap) < Consts.ERROR_EPSILON) {
                            // Ignore this sample.
                            continue;
                        }*/
                        if (maxCap < profiler.cLow) {
                            profiler.violationL += 1;
                            profiler.violationH = 0;
                        }
                        else if (maxCap > profiler.cHigh){
                            profiler.violationH += 1;
                            profiler.violationL = 0;
                        }
                        else
                            ;          
                        Properties prop = new Properties();
                        prop.put(Consts.REBALANCE_PARAMETER_id, profiler.topologyId);
                        prop.put(Consts.REBALANCE_PARAMETER_wait_time, Consts.REBALANCE_DEFAUT_WAIT_TIME_SECONDS);

                        // Scale in or out
                        if (profiler.violationH > profiler.vHigh) {
                            int executors = maxBolt.getExecutors();
                            int tasks = maxBolt.getTasks();
                            if (FINED_SCALING_TOGGLE && executors < tasks) {
                                // Scale up in thread level.
                                prop.put(Consts.REBALANCE_PARAMETER_bolt_id, maxBolt.getBoltId());
                                prop.put(Consts.REBALANCE_PARAMETER_numExecutors, executors + 1);
                                scaler.rebalanceCluster(prop);

                            }
                            else if (FINED_SCALING_TOGGLE && profiler.slotsFree > 0) {
                                // Scale up in process level.
                                prop.put(Consts.REBALANCE_PARAMETER_numWorkers, profiler.workersTotal + 1);
                                scaler.rebalanceCluster(prop);
                            }
                            else if (profiler.curMac.getMachinesRunning() < profiler.curMac.getMachinesTotal()) {
                                // Scale out in machine level.
                                prop.put(Consts.REBALANCE_PARAMETER_numWorkers, profiler.workersTotal + 1);
                                scaler.scaleOut(prop);
                            }
                            else{
                                LoggerX.println("No more resource to scale out!");
                            }
                            try {
                                profiler.isUpdatable = false;
                                Thread.sleep(Consts.BRAIN_WAKE_UP_TIME * 1000);
                                profiler.isUpdatable = true;
                            } catch (InterruptedException e) {
                                break;
                            }
                            profiler.violationH = 0;
                        }
                        else if (profiler.violationL > profiler.vLow) {
                            // Scale in.
                            prop.put(Consts.REBALANCE_PARAMETER_numWorkers, profiler.workersTotal - 1);
                            scaler.scaleIn(prop);
                            try {
                                profiler.isUpdatable = false;
                                Thread.sleep(Consts.BRAIN_WAKE_UP_TIME * 1000);
                                profiler.isUpdatable = true;
                            } catch (InterruptedException e) {
                                break;
                            }
                            profiler.violationL = 0;
                        }
                        else
                            ;
                    }   
                }
                try {
                    Thread.sleep(profiler.freezeT * 1000);
                } catch (InterruptedException e) {
                    break;
                }
            }
            LoggerX.println(TAG, "Exit BaseZacBrain.");
        }
    }
}
