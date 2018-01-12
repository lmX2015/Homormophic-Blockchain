package network;

import peerTable.PeerTable;
import config.config;
public class HelloSender {
	private MuxDemux mux =null;
	private int seq;
	private String hash;
	private int helloInt;
	public PeerTable table;
	public synchronized void setSeq(int s) {
		seq =s;
	}
	private String id;
	public void setMuxDemux(MuxDemux m) {
		mux =m ;
	}
	public HelloMessage hey;
	public HelloSender(String id,int helloInt,int num, MuxDemux m) {
		//hey = new HelloMessage(id,num,helloInt);
		this.id =id;
		this.seq =num;
		this.helloInt=helloInt;
		if (!config.blockchainEnabled)hash =config.defaultHash;
		setMuxDemux(m);
		s= new Sender();
		main= new Thread(s);
		main.start();
	}
	class Sender implements Runnable{
		public void run() {
			while (true) {
				hey = new HelloMessage(id,seq,hash);
				String peers[]=table.toStringArr();
				for(String s : peers) {
					hey.addPeer(s);
					hey.addPeer(id);
				}
				hey.setSequence(table.getMyDbVersion());
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
