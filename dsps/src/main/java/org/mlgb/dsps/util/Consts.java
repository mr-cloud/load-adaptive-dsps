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
    public static final String ZAC_PATH_SCALE_OUT = "/scale-out";
    public static final String ZAC_PATH_SCALE_IN = "/scale-in";

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
    public static final String COLLECTION_METRIC_MESSAGES_RUNNING = "messagesConsumed";
    public static final long METRICS_HEARTBEAT_MILLIS = 10000;
    public static final String COLLECTION_DATE = "date";
    public static final String COLLECTION_STRATEGY = "strategy";
    public static final String COLLECTION_PLANNING = "plan";

    
    // Rebalance options.
    public static final String REBALANCE_PARAMETER_id = "id";
    public static final String REBALANCE_PARAMETER_wait_time = "wait-time";
    public static final String REBALANCE_PARAMETER_options = "rebalanceOptions";
    public static final String REBALANCE_PARAMETER_numWorkers = "numWorkers";
    public static final String REBALANCE_PARAMETER_executors = "executors";
    public static final int REBALANCE_DEFAUT_WAIT_TIME_SECONDS = 3 * 60;
    public static final String REBALANCE_PARAMETER_bolt_id = "boltId";
    public static final String REBALANCE_PARAMETER_numExecutors = "numExecutors";
    
    // Strategies
    public static final String STRATEGY_FIXED_THRESHOLD = "FIXED";
    public static final String STRATEGY_THRESHOLD_BASED_OPT = "OPTIMIZED";
    public static final String STRATEGY_REINFORCEMENT_LEARNING = "RL";
    public static final int MINIMUM_NUM_WORKERS = 2;
    
    // Experiments set up.
    /**
     * Experiment running time.
     */
    public static final String RUNNING_TIME_KEY = "RUNNING_TIME";
    public static final long TEST_RUNNING_TIME = 30 * 60 * 1000;
    public static final long DEPLOYMENT_RUNNING_TIME = 3 * 60 * 60 * 1000;
    /**
     * Shift the input rate at interval.
     */
    public static final long GEAR_SHIFT_INTERVAL = 10 * 60 * 1000;  // per 5 minutes.
    /**
     * Zac configuration file.
     */
    public static final String ZAC_CONFIG = "zac.properties";
    /**
     * LoggerX config.
     */
    public static final String LOGGERX_LEVEL_KEY = "LOGGERX_LEVEL";

    
    
}
