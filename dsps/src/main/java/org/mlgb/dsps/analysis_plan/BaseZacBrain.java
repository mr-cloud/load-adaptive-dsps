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
 * @author Leo
 *
 */
public class BaseZacBrain {
    private Jones jones;
    private OnlineProfiler profiler;
    private ScalingExecutor scaler;
    private String strategy = Consts.STRATEGY_FIXED_THRESHOLD;
    private Planning plan = PlanningFactory.createPlanning();
    private boolean FINED_SCALING_TOGGLE = false;
    private boolean tuning = false;

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

        while (true) {  // Run forever except an error occurs.
            if (!this.tuning) {  // if tuning toggle is open, no strategy will be applied.
                if (this.profiler.curCap == null 
                        || this.profiler.curCap.size() == 0) {
                    // Do nothing.
                }
                else {
                    // Check the capacity of the most urgent component.
                    BoltVO maxBolt = this.profiler.curCap.get(0);
                    double maxCap = Double.parseDouble(maxBolt.getCapacity());
                    if (maxCap < this.profiler.cLow) {
                        this.profiler.violationL += 1;
                        this.profiler.violationH = 0;
                    }
                    else if (maxCap > this.profiler.cHigh){
                        this.profiler.violationH += 1;
                        this.profiler.violationL = 0;
                    }
                    else
                        ;          
                    Properties prop = new Properties();
                    // Scale in or out
                    if (this.profiler.violationH > this.profiler.vHigh) {
                        int executors = maxBolt.getExecutors();
                        int tasks = maxBolt.getTasks();
                        if (FINED_SCALING_TOGGLE && executors < tasks) {
                            // Scale up in thread level.
                            prop.put(Consts.REBALANCE_PARAMETER_id, this.profiler.topologyId);
                            prop.put(Consts.REBALANCE_PARAMETER_wait_time, Consts.REBALANCE_DEFAUT_WAIT_TIME_SECONDS);
                            prop.put(Consts.REBALANCE_PARAMETER_bolt_id, maxBolt.getBoltId());
                            prop.put(Consts.REBALANCE_PARAMETER_numExecutors, executors + 1);
                            this.scaler.reblanceCluster(prop);
                        }
                        else if (FINED_SCALING_TOGGLE && this.profiler.slotsFree > 0) {
                            // Scale up in process level.
                            prop.put(Consts.REBALANCE_PARAMETER_id, this.profiler.topologyId);
                            prop.put(Consts.REBALANCE_PARAMETER_wait_time, Consts.REBALANCE_DEFAUT_WAIT_TIME_SECONDS);
                            prop.put(Consts.REBALANCE_PARAMETER_numWorkers, this.profiler.workersTotal + 1);
                            this.scaler.reblanceCluster(prop);
                        }
                        else if (this.profiler.curMac.getMachinesRunning() < this.profiler.curMac.getMachinesTotal()) {
                            // Scale out in machine level.
                            this.scaler.scaleOut();
                            this.scaler.reblanceCluster(null);
                            try {
                                Thread.sleep(this.profiler.freezeT * 1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        else{
                            LoggerX.println("No more resorce to scale out!");
                        }
                    }
                    else if (this.profiler.violationL < this.profiler.vLow) {
                        // Scale in.
                        this.scaler.scaleIn();
                        try {
                            Thread.sleep(this.profiler.freezeT * 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                        ;
                }   
            }
            try {
                Thread.sleep(Consts.REBALANCE_DEFAUT_WAIT_TIME_SECONDS * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
