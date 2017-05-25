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
     */
    void scaleOut();
    /**
     * Shutdown machine
     */
    void scaleIn();
    /**
     * 1. Scale in process, or thread level.
     * 2. After scaling out in machine level,
     * we need to reblance the cluster to take effective.
     *    
     */
    void reblanceCluster(Properties props);
}
