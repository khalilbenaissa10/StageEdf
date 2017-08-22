package tn.insat.kafka;

import java.io.IOException;
import java.util.Map;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;

public class MainTest {

	public static void main(String[] args) throws IOException {
		 KafkaHelper helper = new KafkaHelper();
		 helper.createProducer();
		 helper.writeMessage("PRS;154.00", "resultKafka");
		 helper.closeProducer();
		 
		 System.out.println("helper is over!!!");
		 Consumer<String, String> cons = helper.createConsumer("test");
		 System.out.println(cons);
		 Map<String, ConsumerRecords<String, String>> result= helper.getMessages(cons);
		 helper.closeConsumer(cons);
		 System.out.println(result);
	}

}
