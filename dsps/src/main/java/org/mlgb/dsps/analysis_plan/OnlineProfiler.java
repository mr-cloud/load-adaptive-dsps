package org.mlgb.dsps.analysis_plan;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import org.mlgb.dsps.monitor.Jones;

/**
 * Collect running information for analysis and planning.
 * @author Leo
 *
 */
public class OnlineProfiler {
    /**
     * History steps.
     */
    public int BATCH_SIZE = 10;  
    
    /**
     * Capacities of workers (mainly component for task processing).
     */
    public Queue<ArrayList<Float>> capacities = new LinkedList<ArrayList<Float>>();
    
    /**
     * To compute SLA violation rate belongs to [0, 1].
     * Maybe helpful to Reinforcement learning optimization.  
     */
    public Queue<float[]> sla = new LinkedList<float[]>(); 
    
    /**
     * Thresholds for scaling when beyond the workers' acceptable capacity.
     */
    public float cHigh = 0.8f;
    public float cLow = 0.5f;
    
    /**
     * Thresholds of consecutive violation times.
     */
    public int vHigh = 3;
    public int vLow = 3;
    
    /**
     * Consecutive violation times.
     */
    public int violationH = 0;
    public int violationL = 0;
    
    /**
     * Safety time for hot-startup in machine level in seconds. 
     */
    public int freezeT = 30;

    private Jones jones;
    
    /**
     * Fixed thresholds when analyzing and planning.  
     * @param jones
     */
    public OnlineProfiler(Jones jones){
        this.jones = jones; 
        // Start a thread to keep the profile updated.
        new Thread(new Runnable(){

            @Override
            public void run() {
                // TODO: update capacities and  <input rate, throughput>.
            }
            
        }).start();
    }
    
    // TODO
    public OnlineProfiler(Jones jones, Optimizer opt){
        
    }
}
