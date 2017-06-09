package org.mlgb.dsps.executor;

import java.util.Properties;

/**
 * Executor for scaling the cluster.
 * @author Leo
 *
 */
public interface ScalingExecutor {
    /**
     * Add machine.
     * @return 
     */
    boolean scaleOut();
    /**
     * Rebalance with corresponding number of workers after scaling out.
     * @param prop Rebalance parameters.
     * @return
     */
    boolean scaleOut(Properties prop);
    /**
     * Shutdown machine
     * @return 
     */
    boolean scaleIn();
    /**
     * Rebalance with corresponding number of workers after scaling in.
     * @param prop Rebalance parameters.
     * @return
     */
    boolean scaleIn(Properties prop);
    /**
     * 1. Scale in process, or thread level.
     * 2. After scaling out in machine level,
     * we need to reblance the cluster to take effective.
     * @return 
     *    
     */
    boolean rebalanceCluster(Properties prop);
    
    /**
     * Shut down executor.
     */
    void shutdown();
}
