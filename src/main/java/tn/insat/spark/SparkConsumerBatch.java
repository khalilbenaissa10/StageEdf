package tn.insat.spark;

import tn.insat.kafka.KafkaHelper;
import tn.insat.models.Consommation;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.sql.Row;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.*;

import kafka.serializer.StringDecoder;
import scala.Tuple2;

import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.hive.HiveContext;

import java.util.HashMap;
import java.util.HashSet;
public class SparkConsumerBatch {
	
	 private static String brokerList = "localhost:9092" ;
	 
	 public static void main(String[] args){
	
		 //create spark context
		  SparkConf sparkConf = new SparkConf().setAppName("jdf-dt-rtoc-withSQL").set("spark.serializer", "org.apache.spark.serializer.KryoSerializer").setMaster("local[*]");
	      JavaSparkContext jsc = new JavaSparkContext(sparkConf);
		 
		
		 //Create spark streaming context
		 JavaStreamingContext jssc = new JavaStreamingContext(jsc, Durations.seconds(30));
		 
		 //Create sql context      
	      final SQLContext hivecontext = new HiveContext(jsc); 
	     
		 
		 //Read data from kafka topic to direct stream
		 HashSet<String> topicSet = new HashSet<String>();
		 topicSet.add("topic_edf");
		 HashMap<String,String> kafkaParams = new HashMap<String,String>();
		 kafkaParams.put("metadata.broker.list", brokerList);
		 JavaPairInputDStream<String, String> kafkaStream = KafkaUtils.createDirectStream(
				 jssc,
				 String.class,
				 String.class,
				 StringDecoder.class,
				 StringDecoder.class,
				 kafkaParams,
				 topicSet);
		 
		  kafkaStream.foreachRDD(new VoidFunction<JavaPairRDD<String, String>>() {
	            public void call(JavaPairRDD<String, String> stringStringJavaPairRDD) throws Exception {

	                //Creation de RDD d'objets Consommation
	                JavaRDD<Consommation> consommationJavaRDD = stringStringJavaPairRDD.map(new Function<Tuple2<String, String>, Consommation>() {
	                    public Consommation call(Tuple2<String, String> stringStringTuple2) throws Exception {
	                        return new Consommation(stringStringTuple2._2());
	                    }
	                });
	           //     System.out.println("-------------");
	              //  System.out.println(consommationJavaRDD.collect().toString());
	                
	                
	             // Apply a schema to an RDD of JavaBeans and register it as a table.
	                DataFrame frame = hivecontext.createDataFrame(consommationJavaRDD,Consommation.class);
	                
	                //putting dataframe in hdfs via hive
//	                frame.registerTempTable("temptable") ;                
//	                hivecontext.sql("CREATE TABLE IF NOT EXISTS consommations as select * from temptable ") ;
	                
	                frame.write().mode(SaveMode.Append).saveAsTable("consommations_edf");
	            }	
		  
		  });
		  
		  //System.out.println("start computation");
		    // Start the computation
		    jssc.start();
		    jssc.awaitTermination();

	 
	
		 
	 }

}
