package tn.insat.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import org.apache.kafka.common.TopicPartition;


import java.io.IOException;
import java.util.Properties;
import java.util.Map;

public class KafkaHelper {
	


    private Producer<String, String> producer;
    private Consumer<String, String> consumer;
    private int count = 0; //id message
    private String brokerList = "localhost:9092" ;
    private String zookeeper =  "localhost:2181" ;
    
    

    public KafkaHelper() {


    }
    
    public void createProducer(){
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put("request.timeout.ms", 100);
        producer = new KafkaProducer<String, String>(props);
    }
    
    public void writeMessage(String message, String topicName) {
        count++;
        producer.send(new ProducerRecord<String, String>(topicName, Integer.toString(count), message));
    }
    
    
    public void closeProducer() {

        producer.close();
    }
    
    public Consumer<String, String> createConsumer(String topicName) throws IOException {

    	  Properties props = new Properties();
          props.put("zk.connect",zookeeper);
          props.put("zookeeper.connect",zookeeper);
          props.put("bootstrap.servers",brokerList);
          props.put("group.id", "test-consumer-group");
          props.put("zk.sessiontimeout.ms", "6000");
          props.put("zk.synctime.ms", "200");
          props.put("autocommit.interval.ms", "1000");
          props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
          props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
          props.put("partition.assignment.strategy", "range");
          consumer = new KafkaConsumer<String, String>(props);
          consumer.subscribe(topicName);
          return consumer;

    }

    public Map<String, ConsumerRecords<String, String>> getMessages(Consumer<String, String> cons) {
        Map<String, ConsumerRecords<String, String>> ret = null;
        //while (ret == null) {
            ret = cons.poll(Long.MAX_VALUE);
        //}
        return ret;
    }

    public void closeConsumer(Consumer<String, String> cons) {

        cons.close();
    }

}
