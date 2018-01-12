package network;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import peerTable.PeerTable;
import config.config;

public class HelloHandler implements SimpleMessageHandler, Runnable{
	 public PeerTable table;
	 public String ID;
	 private BlockingQueue<String> incoming = new LinkedBlockingQueue<String>(100);
	 private MuxDemux myMuxDemux = null;

	    public void setMuxDemux(MuxDemux md){
	        myMuxDemux = md;
	        
	    }

	    public void handleMessage(String m){
	    	String[] parser = m.split(";");
	    	if (parser.length>0&& parser[0].equals("HELLO")) {
	    		if (config.phello)System.out.println("Hello detected");
	    		try {
	    			incoming.add(m);
	    			if (config.phello)System.out.println("Hello added to queue");
	    		}
	    		catch (IllegalStateException e) {
	    			if (config.phello)System.out.println("Hello handler queue full");
	    		}
	    	}
	    }
		
	    public void run(){
	    	System.out.println("Hello Handler Started");
	        while (true){
	            String msg;
	            
				try {
					msg = incoming.take();
					if (config.phello)System.out.println("Treating Hello :"+msg);
						try {
							HelloMessage m = new HelloMessage(msg);
							//System.out.println(m);
							String[] parser= msg.split(";");
							if  (parser[1].equals(ID)){
								if (config.phello)System.out.println("Hello Discarded");
							}
							
							else {
								if (config.phello)System.out.println("got hello : "+m.toString());
								
								table.refresh(m.getSenderID(),config.helloInt,m.getSequence());
							}
										}
						catch (IllegalArgumentException e) {
							
						}
					
				
			        //myMuxDemux.send("This is the message to send");			         
				} catch (InterruptedException e) {
					e.printStackTrace();
			   
				}
	        }
	    }

}