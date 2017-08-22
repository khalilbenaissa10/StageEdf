package tn.insat.datainjection;

import v13.*;
import v13.agents.Agent;

import java.io.PrintStream;
import java.util.Collection;
import java.util.Iterator;

public class FileDataInjector implements AtomDataInjector {
	
	private static final java.util.logging.Logger LOGGER = java.util.logging.Logger
			.getLogger(FileDataInjector.class.getName());
	
    private PrintStream pw = null;

    public FileDataInjector(){

    }


	public void createOutput()  {

	}


	public void sendAgent(long ts, Agent a, Order o, PriceRecord pr)
			throws HadoopTutorialException {

            System.out.println(a.name + ";" + a.cash + ";" + o.obName + ";" + a.getInvest(o.obName) + ";" + (pr != null?Long.valueOf(pr.price):"none")+pr.timestamp);

	}




	public void sendPriceRecord(long ts, PriceRecord pr, long bestAskPrice,
                                long bestBidPrice) throws HadoopTutorialException {

            //System.out.println("Price;" + pr + ";" + bestAskPrice + ";" + bestBidPrice);

	}



	public void sendOrder(long ts, Order o) throws HadoopTutorialException {
		if(this.pw != null) {
            //System.out.println(o.toString()/*+displayTimestamp()*/);
        }
	}


	public void sendTick(long ts, Day day, Collection<OrderBook> orderbooks)
			throws HadoopTutorialException {


            Iterator<OrderBook> i$ = orderbooks.iterator();

            while(i$.hasNext()) {
                OrderBook ob = (OrderBook)i$.next();
                StringBuilder sb = new StringBuilder();
                sb.append("Tick;").append(day.currentPeriod().currentTick()).append(";");
                sb.append(ob.obName).append(";" + (ob.ask.size() > 0?Long.valueOf(((LimitOrder)ob.ask.first()).price):"0"));
                sb.append(";").append(ob.bid.size() > 0?Long.valueOf(((LimitOrder)ob.bid.first()).price):"0");
                sb.append(";").append(ob.lastFixedPrice != null?Long.valueOf(ob.lastFixedPrice.price):"0").append(";");
                //System.out.println(sb.toString());
            }

	}


	public void sendDay(long ts, int nbDays, Collection<OrderBook> orderbooks)
			throws HadoopTutorialException {

            Iterator<OrderBook> i$ = orderbooks.iterator();

            while(i$.hasNext()) {
                OrderBook ob = (OrderBook)i$.next();
                StringBuilder sb = new StringBuilder();
                sb.append(ob.obName).append(";").append(ob.firstPriceOfDay);
                sb.append(";").append(ob.lowestPriceOfDay).append(";");
                sb.append(ob.highestPriceOfDay).append(";").append(ob.lastPriceOfDay);
                sb.append(";").append(ob.numberOfPricesFixed).append(";");
                //System.out.println("Day;" + nbDays + ";" + sb.toString());
            }

	}


	public void sendExec(long ts, Order o) throws HadoopTutorialException {

            //System.out.println("Exec;" + o.sender.name + "-" + o.extId);

	}


	public void closeOutput() throws HadoopTutorialException {

	}

}