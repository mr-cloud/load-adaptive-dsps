package org.mlgb.dsps.monitor;
/**
 * Monitor and gather the running parameters of the cluster from
 * Storm UI, Kafka and Zac (Resource Managment Layer).
 * @author Leo
 *
 */
import org.json.simple.JSONObject;
import org.mlgb.dsps.utils.Consts;

public interface NightsWatcher {
    /*
     * from Storm UI
     */
    default JSONObject getClusterSummary(){
        return getClusterSummary(Consts.CLUSTER_SUMMARY);
    };
    
    JSONObject getClusterSummary(String url);
    
    JSONObject getTopologiesSummary(String url);
    default JSONObject getTopologiesSummary(){
        return getTopologiesSummary(Consts.TOPOLOGY_SUMMARY);
    }
    
    JSONObject getTopologyProfile(String prefix, String id);
    default JSONObject getTopologyProfile(String id){
        return getTopologyProfile(Consts.TOPOLOGY_PROFILE_PREFIX, id);
    }
    
    /*
     * from Zac
     */
    JSONObject getMachinesNumber();
    
    /*
     * from Kafka
     */
    long getMessagesNumberInQue();
}
