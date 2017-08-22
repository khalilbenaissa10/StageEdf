package tn.insat.datainjection;

import v13.Day;
import v13.Order;
import v13.OrderBook;
import v13.PriceRecord;
import v13.agents.Agent;

import java.util.Collection;

/**
 *
 */
public interface AtomDataInjector {

	public void closeOutput() throws HadoopTutorialException;

	public void createOutput() throws HadoopTutorialException;

	public void sendAgent(long ts, Agent a, Order o, PriceRecord pr)
			throws HadoopTutorialException;

	public void sendPriceRecord(long ts, PriceRecord pr, long bestAskPrice,
								long bestBidPrice) throws HadoopTutorialException;


	public void sendOrder(long ts, Order o) throws HadoopTutorialException;

	public void sendTick(long ts, Day day, Collection<OrderBook> orderbooks)
			throws HadoopTutorialException;

	public void sendDay(long ts, int nbDays, Collection<OrderBook> orderbooks)
			throws HadoopTutorialException;

	public void sendExec(long ts, Order o) throws HadoopTutorialException;

}