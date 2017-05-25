package org.mlgb.dsps.analysis_plan;

import org.mlgb.dsps.executor.Scaler;
import org.mlgb.dsps.executor.ScalingExecutor;
import org.mlgb.dsps.monitor.Jones;
import org.mlgb.dsps.monitor.JonesFactory;

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
    
    /**
     * Zac startup entry.
     * Override this method when inherit this base class. 
     */
    public static void main(String[] args){
        new BaseZacBrain();
        // TODO: Some additional configuration.
    }
    public BaseZacBrain(){
        this.jones = JonesFactory.createJones();
        this.profiler = new OnlineProfiler(jones);
        this.scaler = new Scaler();
        this.brainStorming();
    }
    
    /**
     * Scheduling
     */
    public void brainStorming(){
    }
}
