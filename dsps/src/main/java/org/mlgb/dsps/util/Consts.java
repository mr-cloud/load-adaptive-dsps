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
    
    // mongodb://[username:password@]host1[:port1][,host2[:port2],...[,hostN[:portN]]][/[database][?options]]
    public static final String MONGO_HOST = "localhost";
    public static final String MONGO_PORT = "27017";
    public static final String METRICS_COLLECTION = "metrics";
    public static final String MONGO_DB_STORM = "storm";
    public static final String MONGO_USERNAME = "";
    public static final String MONGO_PASSWORD = "";
    public static final String MONGO_SPEC_DB = "";
    public static final String MONGO_CLIENT_URI = 
            "mongodb://" 
            + (MONGO_USERNAME.equals("")?"":(MONGO_USERNAME + ":")) 
            + (MONGO_PASSWORD.equals("")?"":(MONGO_PASSWORD + "@"))
            + MONGO_HOST 
            + (MONGO_PORT.equals("")?"":(":" + MONGO_PORT))
            + (MONGO_SPEC_DB.equals("")?"":("/" + MONGO_SPEC_DB));
    public static final String COLLECTION_METRIC_MACHINES_TOTAL = "machinesTotal";
    public static final String COLLECTION_METRIC_MACHINES_RUNNING = "machinesRunning";
    public static final String COLLECTION_METRIC_MESSAGES_TOTAL = "messagesTotal";
    public static final String COLLECTION_METRIC_MESSAGES_RUNNING = "messagesRunnning";
    public static final long METRICS_HEARTBEAT_MILLIS = 10000;
    
    
}
