package org.mlgb.dsps.app;

import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.kafka.BrokerHosts;
import org.apache.storm.kafka.KafkaSpout;
import org.apache.storm.kafka.SpoutConfig;
import org.apache.storm.kafka.StringScheme;
import org.apache.storm.kafka.ZkHosts;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.topology.TopologyBuilder;
import org.mlgb.dsps.util.Consts;

public class ToyApp {
    public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException, AuthorizationException {
        Config config = new Config();
        config.setDebug(true);
        config.setNumWorkers(2);
        config.setNumAckers(1);
        config.setNumEventLoggers(1);        
        String topologyName;
        if (args.length > 0) {
            topologyName = args[0];
        }
        else{
            topologyName = "toy";
        }
        // build Kafka spout.
        BrokerHosts hosts = new ZkHosts(Consts.ZOOKEEPER_HOST_PORT);
        SpoutConfig spoutConfig = new SpoutConfig(hosts, Consts.TOPIC, Consts.ZK_ROOT, Consts.CONSUMER_GROUP_ID);
        spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
        spoutConfig.ignoreZkOffsets = true;// read from head for each deployment or restart.
        KafkaSpout kafkaSpout = new KafkaSpout(spoutConfig);
        
        // build and submit topology
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("toy-spout", kafkaSpout, 1);
        StormSubmitter.submitTopology(topologyName, config, builder.createTopology());
    }
}
