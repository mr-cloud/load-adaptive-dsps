package org.mlgb.dsps.app.sale_recommend;

/**
 * This is an utility class. It contains the keys that should be present in the input config-file
 * @author Leo
 */
public class Keys {

    //kafka spout
    public static final String KAFKA_SPOUT_ID = "kafka-spout";
    public static final String KAFKA_SPOUT_COUNT          = "kafka-spout.count";

    //bolt
    public static final String DUMMY_BOLT_ID = "dummy-bolt";
    public static final String DUMMY_BOLT_COUNT = "dummy-bolt.count";
    public static final String DUMMY_BOLT_NUM_TASKS = "dummy-bolt.numTasks";
    
    //bolt latency
    public static final String BOLT_LATENCY_IN_MILLIS = "BOLT_LATENCY_IN_MILLIS";

    //storm workers number
    public static final String NUM_WORKERS = "NUM_WORKERS";

    //message timeout
    public static final String TIMEOUT = "TIMEOUT";

    //eventloggers number
    public static final String NUM_EVENTLOGGERS = "NUM_EVENTLOGGERS";
    
    //default configuration file name
    public static final String DEFAULT_CONFIG = "default_config.properties";

    //topology name
    public static final String TOPOLOGY_NAME = "SALE_ON_ZAC";
    //acker number
    public static final String NUM_ACKERS = "NUM_ACKERS";
    //stats sample rate
    public static final String TOPOLOGY_STATS_SAMPLE_RATE = "TOPOLOGY_STATS_SAMPLE_RATE";
    //backp-ressure
    public static final String TOPOLOGY_BACKPRESSURE_ENABLE = "TOPOLOGY_BACKPRESSURE_ENABLE";
    public static final String FIND_RECOMMENDED_SALES_BOLT_COUNT = "FIND_RECOMMENDED_SALES_BOLT_COUNT";
    public static final String FIND_RECOMMENDED_SALES_BOLT_ID = "FIND_RECOMMENDED_SALES_BOLT_ID";
    public static final String FIND_RECOMMENDED_SALES_BOLT_NUM_TASKS = "FIND_RECOMMENDED_SALES_BOLT_NUM_TASKS";
    public static final String FIND_RECOMMENDED_SALES_BOLT_LATENCY_IN_MILLIS = "FIND_RECOMMENDED_SALES_BOLT_LATENCY_IN_MILLIS";

    public static final String LOOKUP_SALES_DETAILS_BOLT_COUNT = "LOOKUP_SALES_DETAILS_BOLT_COUNT";
    public static final String LOOKUP_SALES_DETAILS_BOLT_ID = "LOOKUP_SALES_DETAILS_BOLT_ID";
    public static final String LOOKUP_SALES_DETAILS_BOLT_LATENCY_IN_MILLIS = "LOOKUP_SALES_DETAILS_BOLT_LATENCY_IN_MILLIS";
    public static final String LOOKUP_SALES_DETAILS_BOLT_NUM_TASKS = "LOOKUP_SALES_DETAILS_BOLT_NUM_TASKS";

    public static final String SAVE_RECOMMENDED_SALES_BOLT_COUNT = "SAVE_RECOMMENDED_SALES_BOLT_COUNT";
    public static final String SAVE_RECOMMENDED_SALES_BOLT_ID = "SAVE_RECOMMENDED_SALES_BOLT_ID";
    public static final String SAVE_RECOMMENDED_SALES_BOLT_LATENCY_IN_MILLIS = "SAVE_RECOMMENDED_SALES_BOLT_LATENCY_IN_MILLIS";
    public static final String SAVE_RECOMMENDED_SALES_BOLT_NUM_TASKS = "SAVE_RECOMMENDED_SALES_BOLT_NUM_TASKS";

}
