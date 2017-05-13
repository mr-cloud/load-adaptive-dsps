package org.mlgb.dsps.monitor;

//import util.properties packages
import java.util.Properties;
import java.util.Random;

//import simple producer packages
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.Callback;
//import KafkaProducer packages
import org.apache.kafka.clients.producer.KafkaProducer;

//import ProducerRecord packages
import org.apache.kafka.clients.producer.ProducerRecord;
import org.mlgb.dsps.util.Consts;

import uni.akilis.helper.LoggerX;
/**
 * Kafka producer: produce messages to Kafka and record the total number of messages.
 * @author Leo
 *
 */
public class WhiteWalkerProducer {
    private Producer<String, String> producer;
    private String topicName;
    private Random rand;
    
    public  WhiteWalkerProducer(String topicName) {
        // Check arguments length value
        if("".equals(topicName)){
            LoggerX.println("Enter topic name");
            System.exit(1);
        }
        
        this.topicName = topicName;
        
        // create instance for properties to access producer configs   
        Properties props = new Properties();

        //Assign localhost id
        props.put("bootstrap.servers", Consts.KAFKA_BROKERS);

        //Set acknowledgements for producer requests.      
        props.put("acks", "all");

        //If the request fails, the producer can automatically retry,
        props.put("retries", 0);

        //Specify buffer size in config
        props.put("batch.size", 16384);

        //Reduce the no of requests less than 0   
        props.put("linger.ms", 1);

        //The buffer.memory controls the total amount of memory available to the producer for buffering.   
        props.put("buffer.memory", 33554432);

        props.put("key.serializer", 
                "org.apache.kafka.common.serializa-tion.StringSerializer");

        props.put("value.serializer", 
                "org.apache.kafka.common.serializa-tion.StringSerializer");

        producer = new KafkaProducer
                <String, String>(props);
        
        rand = new Random();
    }
    
    public void produceWhiteWaker(Callback callback){
        String val = String.valueOf(rand.nextInt());
        this.producer.send(new ProducerRecord<String, String>(topicName, 
                val, val), callback);
    }
    
    public void closeProducer(){
        producer.close();        
    }
}
