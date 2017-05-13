package org.mlgb.dsps.monitor;
import org.mlgb.dsps.util.ClusterSummaryVO;
import org.mlgb.dsps.util.Consts;
import org.mlgb.dsps.util.MachinesStatsVO;
import org.mlgb.dsps.util.MessagesStatsVO;
import org.mlgb.dsps.util.TopologiesSummaryVO;
import org.mlgb.dsps.util.TopologyProfileVO;

public interface NightsWatcher {
    /*
     * from Storm UI
     */
    default ClusterSummaryVO getClusterSummary(){
        return getClusterSummary(Consts.CLUSTER_SUMMARY);
    };
    
    ClusterSummaryVO getClusterSummary(String url);
    
    TopologiesSummaryVO getTopologiesSummary(String url);
    default TopologiesSummaryVO getTopologiesSummary(){
        return getTopologiesSummary(Consts.TOPOLOGY_SUMMARY);
    }
    
    TopologyProfileVO getTopologyProfile(String prefix, String id);
    default TopologyProfileVO getTopologyProfile(String id){
        return getTopologyProfile(Consts.TOPOLOGY_PROFILE_PREFIX, id);
    }
    
    /*
     * from Zac
     */
    MachinesStatsVO getMachinesStats();
    
    /*
     * from Kafka and Zookeeper
     */
    MessagesStatsVO getMessagesStats();
}
