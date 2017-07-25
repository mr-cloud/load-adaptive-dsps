package org.mlgb.dsps.analysis_plan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.mlgb.dsps.monitor.Jones;
import org.mlgb.dsps.util.vo.BoltVO;
import org.mlgb.dsps.util.vo.ClusterSummaryVO;
import org.mlgb.dsps.util.vo.MachinesStatsVO;
import org.mlgb.dsps.util.vo.MessagesStatsVO;
import org.mlgb.dsps.util.vo.TopologiesSummaryVO;
import org.mlgb.dsps.util.vo.TopologyProfileVO;

import uni.akilis.helper.LoggerX;

/**
 * Collect running information for analysis and planning.
 * @author Leo
 *
 */
public class OnlineProfiler {
    public static final String TAG = OnlineProfiler.class.getName();

    //-----------------Optimization Parameters
    /**
     * History steps. 10 adopted scaling actions at least.
     */
    public int BATCH_SIZE = 100;  

    /**
     * Thresholds for scaling when beyond the workers' acceptable capacity.
     */
    public double cHigh = 0.8;
    public double cLow = 0.5;

    /**
     * Thresholds of consecutive violation times.
     */
    public int vHigh = 3;
    public int vLow = 3;

    /**
     * Safety time for hot-startup in machine level in seconds. 
     */
    public int freezeT = 20;
    //----------------------------------------------------------

    /**
     * Capacities of workers (mainly component for task processing).
     */
    public Queue<ArrayList<BoltVO>> capacities = new LinkedList<ArrayList<BoltVO>>();

    /**
     * The latest capacity reference for fast indexing.
     */
    public List<BoltVO> curCap;

    /**
     * To compute SLA violation rate belongs to [0, 1].
     * Maybe helpful to reinforcement learning optimization.  
     */
    public Queue<MessagesStatsVO> sla = new LinkedList<>(); 

    /**
     * The latest SLA reference for fast indexing.
     */
    public MessagesStatsVO curMsg;

    /**
     * To calculate the cost of a policy with new thresholds Pi.
     * Maybe helpful to threshold-based optimization. 
     */
    public Queue<MachinesStatsVO> machines = new LinkedList<>();

    /**
     * The latest machines reference for fast indexing.
     */
    public MachinesStatsVO curMac;

    /**
     * Consecutive violation times.
     */
    public int violationH = 0;
    public int violationL = 0;

    /**
     * Used number of slots by this topology.
     */
    public int workersTotal;

    /**
     * Total slots in cluster.
     */
    public int slotsTotal;

    /**
     * Free slots in cluster.
     */
    public int slotsFree;

    /**
     * Topology ID
     */
    public String topologyId;

    /**
     * After each scaling, we need some break 
     * to sample the data with the new cluster.
     */
    public boolean isUpdatable = true;

    private Jones jones;
    private Thread profiling;

    /**
     * Fixed thresholds when analyzing and planning.  
     * @param jones
     */
    public OnlineProfiler(Jones jones){
        this.jones = jones; 
    }

    public void profilerOnline() {
        // Start a thread to keep the profile updated.
        this.profiling = new Thread(new Runnable(){

            @Override
            public void run() {
                while(!Thread.interrupted()){
                    // update capacities and  <input rate, throughput>.
                    if (isUpdatable) {
                        ClusterSummaryVO cluster = jones.getClusterSummary();
                        slotsTotal = cluster.getSlotsTotal();
                        slotsFree = cluster.getSlotsFree();
                        TopologiesSummaryVO topoSum = jones.getTopologiesSummary();
                        if (topoSum.getTopologies().size() == 0) {
                            LoggerX.error(TAG, "There is no running topogy.");
                        }
                        else{
                            TopologyProfileVO topoProf = jones.getTopologyProfile(topoSum.getTopologies().get(0).getId());
                            workersTotal = topoProf.getWorkersTotal();
                            topologyId = topoProf.getId();
                            List<BoltVO> newCap = topoProf.getBolts();
                            Collections.sort(newCap);
                            curCap = newCap;
                            capacities.offer((ArrayList<BoltVO>) newCap);
                            if (capacities.size() > BATCH_SIZE) {
                                capacities.poll();
                            }
                            MessagesStatsVO newMsg = jones.getMessagesStats();
                            sla.offer(newMsg);
                            curMsg = newMsg;
                            if (sla.size() > BATCH_SIZE) {
                                sla.poll();
                            }
                            MachinesStatsVO newMac = jones.getMachinesStats(); 
                            machines.offer(newMac);
                            curMac = newMac;
                            if (machines.size() > BATCH_SIZE) {
                                machines.poll();
                            }
                        }   
                    }
                    try {
                        Thread.sleep(freezeT * 1000);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
                LoggerX.println(TAG, "Exit OnlineProfiler.");
            }

        });
        this.profiling.start();
    }

    // TODO
    public OnlineProfiler(Jones jones, Optimizer opt){

    }

    public void snapshot() {
        if (this.profiling != null) {
            this.profiling.interrupt();     
        }
    }
}
