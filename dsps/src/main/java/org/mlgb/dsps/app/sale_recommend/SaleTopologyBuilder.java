package org.mlgb.dsps.app.sale_recommend;

import java.util.Properties;

import org.apache.storm.generated.StormTopology;
import org.apache.storm.kafka.BrokerHosts;
import org.apache.storm.kafka.KafkaSpout;
import org.apache.storm.kafka.SpoutConfig;
import org.apache.storm.kafka.StringScheme;
import org.apache.storm.kafka.ZkHosts;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;
import org.mlgb.dsps.util.Consts;
import org.mlgb.dsps.util.LatencySimulator;

/**
 * build topology's nodes and edges.
 * @author Leo
 *
 */
public class SaleTopologyBuilder {

	public static StormTopology build(Properties configs) {
		TopologyBuilder builder = new TopologyBuilder();

		// build Kafka spout.
        BrokerHosts hosts = new ZkHosts(Consts.ZOOKEEPER_HOST_PORT);
        SpoutConfig spoutConfig = new SpoutConfig(hosts, Consts.TOPIC, Consts.ZK_ROOT, Consts.CONSUMER_GROUP_ID);
        spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
        spoutConfig.ignoreZkOffsets = true;// read from head for each deployment or restart.
        KafkaSpout kafkaSpout = new KafkaSpout(spoutConfig);
		
        //set the kafkaSpout to topology
		//parallelism-hint for kafkaSpout - defines number of executors/threads to be spawn per container
		int kafkaSpoutCount = Integer.parseInt(configs.getProperty(Keys.KAFKA_SPOUT_COUNT));
		builder.setSpout(configs.getProperty(Keys.KAFKA_SPOUT_ID), kafkaSpout, kafkaSpoutCount);
		
		//set the bolt to topology
		int dummyBoltCount = Integer.parseInt(configs.getProperty(Keys.DUMMY_BOLT_COUNT));
		builder.setBolt(configs.getProperty(Keys.DUMMY_BOLT_ID), new DummyBolt(Integer.parseInt(configs.getProperty(Keys.BOLT_LATENCY_IN_MILLIS))), dummyBoltCount)
		    .setNumTasks(Integer.parseInt(configs.getProperty(Keys.DUMMY_BOLT_NUM_TASKS)))
			.shuffleGrouping(configs.getProperty(Keys.KAFKA_SPOUT_ID));
			
		return builder.createTopology();
	}

	static class DummyBolt extends BaseBasicBolt{
	    /**
         * 
         */
        private static final long serialVersionUID = 1L;
        private int delay;
	    
        public DummyBolt(int delay) {
            super();
            this.delay = delay;
        }

        @Override
        public void execute(Tuple input, BasicOutputCollector collector) {
            LatencySimulator.simulate(delay);
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {           
        }
	    
	}
}
