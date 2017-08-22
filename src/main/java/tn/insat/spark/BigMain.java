package tn.insat.spark;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SQLContext;




import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.hive.HiveContext;

public class BigMain {
	


	static String[] argss = new String[]{"-kafka"};


	
	public static void main(String[] args) throws InterruptedException {
		

		
//		Consommation cons1 = new Consommation("client44","PRS",5200,124877755);
//		Consommation cons2 = new Consommation("client74","BRD",1200,122277724);
//		Consommation cons3 = new Consommation("client1","MRS",25411,744814745);
//		Consommation cons4 = new Consommation("client23","PRS",1245,548524511);
		
		String cons1 = new String("client5;PRS;5200;124877755");
		String cons2 = new String("client6;BRD;1200;122277724");
		String cons3 = new String("client7;MRS;25411;744814745");	
		
		
		
		List<String> liste = new ArrayList<String>();
		
		liste.add(cons1);
		liste.add(cons2);
		liste.add(cons3);
			
		 
		 //create spark context
		  SparkConf conf = new SparkConf().setAppName("jdf-dt-rtoc-withSQL").set("spark.serializer", "org.apache.spark.serializer.KryoSerializer").setMaster("local[*]");
	      JavaSparkContext sc = new JavaSparkContext(conf);
	      
	      SQLContext hivecontext = new HiveContext(sc); 
	      
//	     hivecontext.setConf("hive.metastore.warehouse.dir", "hdfs://quickstart.cloudera:8020/user/hive/warehouse")  ;
//	      hivecontext.setConf("hive.metastore.uris", "thrift://quickstart.cloudera:9083")  ;
	    
	      
		  	
		    DataFrame df = hivecontext.createDataset(liste, Encoders.STRING()).toDF();
		    df.printSchema();	
		    df.show();
	      
	 
//		    df.registerTempTable("temptable") ;                
//		    hivecontext.sql("CREATE TABLE IF NOT EXISTS tests as select * from temptable") ;

		
		    
		    df.write().mode(SaveMode.Overwrite).saveAsTable("tests");
		    
		    
		    
	}

}
