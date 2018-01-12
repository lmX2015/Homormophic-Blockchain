package network;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import peerTable.PeerTable;

public class ListSender implements Runnable {
	public PeerTable table;
	public int version = 0;
	private BlockingQueue<String> incoming = new LinkedBlockingQueue<String>(100);
	private MuxDemux myMuxDemux = null;
	
	public void setMux(MuxDemux m) {
		myMuxDemux = m;
	}
	public void send(String peerID) {
		try {
			incoming.add(peerID);
			
		}
		catch (Exception e) {
			
		}
		
	}
	@Override
	public void run() {
		while (true) {
			try {
				String peer = incoming.take();
				String[] data= table.getMyData();
				int count =0;
				for(String s :data) {
					ListMessage mess = new ListMessage(table.getID(),peer,version, data.length,count,s);
					myMuxDemux.send(mess.getMessage());
					count ++;
				}
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
