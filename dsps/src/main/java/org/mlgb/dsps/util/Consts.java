package org.mlgb.dsps.util;
/**
 * Some constants.
 * @author Leo
 *
 */
public class Consts {
    public static final String STORM_UI_HOST = "localhost";
    public static final int STORM_UI_PORT = 8080;
    public static final String STORM_UI_PROTOCAL = "http";
    public static final String CLUSTER_SUMMARY = "/api/v1/cluster/summary";
    public static final String TOPOLOGY_SUMMARY = "/api/v1/topology/summary";
    public static final String TOPOLOGY_PROFILE_PREFIX = "/api/v1/topology/";
    
    public static final String ZAC_PROTOCAL = "http";
    public static final String ZAC_HOST = "192.168.56.1";
    public static final int ZAC_PORT = 5000;
    public static final String ZAC_PATH_MACHINES_STATS = "/machines-stats";

    public static final String ZOOKEEPER_HOST_PORT = "localhost:2181";
    public static final int ZOOKEEPER_TIMEOUT = 5000;

    public static final String KAFKA_BROKERS = "localhost:9092";    
    public static final String CONSUMER_GROUP_ID = "south_group";
    public static final String TOPIC = "thrones";
    public static final String ZK_ROOT = "/consumers";
    // command consumer znode.
    /*    public static final String OFFSET_ZNODE = ZK_ROOT + "/"
            + CONSUMER_GROUP_ID + "/offsets/"
            + TOPIC + "/0";
*/    
    // Kafka spout znode.
    public static final String OFFSET_ZNODE = ZK_ROOT + "/"
            + CONSUMER_GROUP_ID + "/partition_0";
    
    
}
