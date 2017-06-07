package org.mlgb.dsps.app.sale_recommend;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;

/**
 * create a topology from builder, configure the topology and submit it to Storm cluster.
 * @author Leo
 *
 */
public class SaleTopologyRunner {
    public static void main(String[] args) throws Exception {
        String topologyName = "";
        String configFile = "";
        if(args.length < 0 || args.length > 3 || (args.length != 0 && StringUtils.isBlank(args[0]))){
            System.out.println("Error deploying topology.");
            System.out.println("Usage: [<topology name> [<config name>] [debug]]");
            System.out.println("Please provide correct command-line arguments and try again.");
            System.exit(1);
        }
        else if(args.length == 0){
            System.out.println("Use default name and config for topology deployment!");
            topologyName = Keys.TOPOLOGY_NAME;
            configFile = Keys.DEFAULT_CONFIG;
        }
        else{
            topologyName = args[0];
            if((args.length == 2 && !"debug".equalsIgnoreCase(args[1]))
                    || args.length == 3){
                configFile = args[1];
            }
            else{
                System.out.println("Missing input : config file location, using default one.");
                configFile = Keys.DEFAULT_CONFIG;		
            }
        }
        Properties configs = loadConfigProperties(configFile);
        Config config = createConfig(shouldRunInDebugMode(args), configs);
        StormSubmitter.submitTopology(topologyName, config, SaleTopologyBuilder.build(configs));
    }

    private static Properties loadConfigProperties(String configFile) {
        Properties configs = new Properties();
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            configs.load(classLoader.getResourceAsStream(configFile));
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        return configs;
    }

    private static boolean shouldRunInDebugMode(String[] args) {
        return args.length > 1 && "debug".equalsIgnoreCase(args[args.length-1]);
    }

    private static Config createConfig(Boolean debug, Properties configs) {
        Config config = new Config();
        config.setDebug(debug);
        config.setMessageTimeoutSecs(Integer.parseInt(configs.getProperty(Keys.TIMEOUT)));
        config.setNumWorkers(Integer.parseInt(configs.getProperty(Keys.NUM_WORKERS)));
        config.setNumAckers(Integer.parseInt(configs.getProperty(Keys.NUM_ACKERS)));
        config.setNumEventLoggers(Integer.parseInt(configs.getProperty(Keys.NUM_EVENTLOGGERS)));
        config.put(Config.TOPOLOGY_STATS_SAMPLE_RATE, Double.parseDouble(configs.getProperty(Keys.TOPOLOGY_STATS_SAMPLE_RATE)));
        config.put(Config.TOPOLOGY_BACKPRESSURE_ENABLE, Boolean.parseBoolean(configs.getProperty(Keys.TOPOLOGY_BACKPRESSURE_ENABLE)));
        return config;
    }
}
