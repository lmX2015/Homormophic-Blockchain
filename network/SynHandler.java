package network;
import config.config;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import peerTable.PeerTable;

public class SynHandler implements SimpleMessageHandler{
	MuxDemux m =null;
	public ListSender lst= null; 
	public PeerTable table;
	public String ID;
	private BlockingQueue<String> incoming = new LinkedBlockingQueue<String>(100);
	private MuxDemux myMuxDemux = null;

	public void setMuxDemux(MuxDemux md){
		myMuxDemux = md;

	}

	public void handleMessage(String m){
		String[] parser = m.split(";");
		if (parser.length>0&& parser[0].equals("SYN")) {
			//System.out.println("Syn detected");
			try {
				incoming.add(m);
				//System.out.println("Syn added to queue");
			}
			catch (IllegalStateException e) {
				System.out.println("Syn handler queue full");
			}
		}
	}

	public void run(){
		System.out.println("Syn Handler Started");
		while (true){
			String msg;
			try {
				msg = incoming.take();
				//System.out.println("Treating SYN");
				String[] parser = msg.split(";");
				if (parser[2].equals(ID)) {
					if (config.psyn)System.out.println("SYN matched ");
					lst.send(parser[1]);
				}
				else {
					if (config.psyn)System.out.println("Syn Discarded :"+parser[2]);
				}
				
				}


			   
			 catch (InterruptedException e) {
				e.printStackTrace();

			}
		}
	}


}
