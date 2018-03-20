package network;

import peerTable.PeerTable;
import self.Self;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import config.config;
import messages.HelloMessage;
public class HelloSender {
	private MuxDemux mux =null;
	private int seq;
	private String hash;
	public PeerTable table;
	private BlockingQueue<String> incoming = new LinkedBlockingQueue<String>(100);
	public void send(String m) {
		incoming.add(m);
	}
	public synchronized void setSeq(int s) {
		seq =s;
	}
	private String id;
	public void setMuxDemux(MuxDemux m) {
		mux =m ;
	}
	public HelloMessage hey;
	public HelloSender(MuxDemux m) {
		//hey = new HelloMessage(id,num,helloInt);
		if (!config.blockchainEnabled)hash =config.defaultHash;
		setMuxDemux(m);
		s= new Sender();
		main= new Thread(s);
		main.start();
	}
	class Sender implements Runnable{
		public void run() {
			while (true) {
				String dest=incoming.peek();
				hey = new HelloMessage(Self.getId(), dest, Self.getCurrentBlock().getId()-1, Self.getCurrentBlock().getHashPrec());
				mux.send(hey.getHelloMessageAsEncodedString());
				
				try {
					Thread.sleep(config.helloInt*10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	Sender s; 
	Thread main; 
	
}
