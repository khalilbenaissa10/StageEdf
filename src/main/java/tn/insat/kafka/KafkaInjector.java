package tn.insat.kafka;

import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tn.insat.datainjection.AtomConfiguration;
import tn.insat.datainjection.AtomDataInjector;
import tn.insat.datainjection.HadoopTutorialException;
import v13.Day;
import v13.LimitOrder;
import v13.Order;
import v13.OrderBook;
import v13.PriceRecord;
import v13.agents.Agent;

public class KafkaInjector implements AtomDataInjector {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(KafkaInjector.class);
    private Producer<String, String> producer;
    private String topic;
    private int count = 0 ;
    
    public KafkaInjector(AtomConfiguration atomConfiguration) {
        topic = atomConfiguration.getKafkaTopic();
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, atomConfiguration.getKafkaBoot());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put("request.timeout.ms", 100);
        producer = new KafkaProducer<String, String>(props);
    }

	public void closeOutput() throws HadoopTutorialException {
		  LOGGER.info(count+" data inserted");
	        producer.close();
		
	}

	public void createOutput() throws HadoopTutorialException {
		// TODO Auto-generated method stub
		
	}

	public void sendAgent(long ts, Agent a, Order o, PriceRecord pr)
			throws HadoopTutorialException {
		 StringBuilder sb = new StringBuilder();
	        sb.append(a.name).append(";");
	        sb.append(a.cash).append(";");
	        sb.append(o.obName).append(";");
	        sb.append(a.getInvest(o.obName)).append(";");
	        sb.append((pr != null ? Long.valueOf(pr.price) : "none")).append(";");
	        sb.append(ts);
	        count++ ;
	        producer.send(new ProducerRecord<String, String>(topic,Integer.toString(count),sb.toString()));
		
	}

	public void sendPriceRecord(long ts, PriceRecord pr, long bestAskPrice,
			long bestBidPrice) throws HadoopTutorialException {
		 StringBuilder sb = new StringBuilder();
	        sb.append("Price").append(";");
	        sb.append(pr).append(";");
	        sb.append(bestAskPrice).append(";");
	        sb.append(bestBidPrice).append(";");
	        sb.append(ts);
		
	}

	public void sendOrder(long ts, Order o) throws HadoopTutorialException {
		StringBuilder sb = new StringBuilder();
        sb.append(o.toString()).append(";");
        sb.append(ts);
		
	}

	public void sendTick(long ts, Day day, Collection<OrderBook> orderbooks)
			throws HadoopTutorialException {
		 Iterator<OrderBook> i$ = orderbooks.iterator();

	        while (i$.hasNext()) {
	            OrderBook ob = i$.next();
	            StringBuilder sb = new StringBuilder();
	            sb.append("Tick").append(";");
	            sb.append(day.currentPeriod().currentTick()).append(";");
	            sb.append(ob.obName).append(";");
	            sb.append(ob.ask.size() > 0 ? Long.valueOf(((LimitOrder) ob.ask.first()).price) : "0").append(";");
	            sb.append(ob.bid.size() > 0 ? Long.valueOf(((LimitOrder) ob.bid.first()).price) : "0").append(";");
	            sb.append(ob.lastFixedPrice != null ? Long.valueOf(ob.lastFixedPrice.price) : "0").append(";");
	            sb.append(ts);
	        }
		
	}

	public void sendDay(long ts, int nbDays, Collection<OrderBook> orderbooks)
			throws HadoopTutorialException {
		Iterator<OrderBook> i$ = orderbooks.iterator();

        while (i$.hasNext()) {
            OrderBook ob = i$.next();
            StringBuilder sb = new StringBuilder();
            sb.append("Day").append(";");
            sb.append(nbDays).append(";");
            sb.append(ob.obName).append(";");
            sb.append(ob.firstPriceOfDay).append(";");
            sb.append(ob.lowestPriceOfDay).append(";");
            sb.append(ob.highestPriceOfDay).append(";");
            sb.append(ob.lastPriceOfDay).append(";");
            sb.append(ob.numberOfPricesFixed).append(";");
            sb.append(ts);
            
        }
		
	}

	public void sendExec(long ts, Order o) throws HadoopTutorialException {
		  StringBuilder sb = new StringBuilder();
	        sb.append("Exec").append(";");
	        sb.append(o.sender.name).append("-").append(o.extId).append(";");
	        sb.append(ts);
		
	}
	
	

}
