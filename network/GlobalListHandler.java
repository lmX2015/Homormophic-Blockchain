package network;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.SynchronousQueue;

import peerTable.PeerTable;
import config.config;

public class GlobalListHandler  implements SimpleMessageHandler{
	MuxDemux m;
	private SynchronousQueue<String> queue = new  SynchronousQueue<String>();
	public PeerTable db;
	private HashMap<String,SimpleListHandler> handlers= new HashMap<String,SimpleListHandler>();
	
	@Override
	public void run() {
		if (config.pgl)System.out.println("Global List handler Started");
		while(true) {
			String message;
			try {
				
				message =queue.take();
				SimpleListHandler a = new SimpleListHandler(message);
				if (config.pgl)System.out.println("Sychronizing with : "+message);
				synchronized (handlers) {
					String peerID=a.senderID;
					int sequence= a.sequence;
					String ID =peerID+";"+sequence;
					if (handlers.get(ID)==null) {
						a.g=this;
						a.handleMessage(message);
						handlers.put(ID,a);
						if (config.pgl)System.out.println("new ListHandler Lauched");
						
					}
					else handlers.get(ID).handleMessage(message);

				}
				
					
				
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}
	public void finish(SimpleListHandler l) {
		if (config.pgl)System.out.println("FINISHED !");
		
		String peerID=l.peerID;
		int sequence= l.sequence;			
		String ID =peerID+";"+sequence;
		db.updateDb(peerID,sequence,l.parts); 
		synchronized(handlers) {handlers.remove(ID);}
			
		
		
	}
	@Override
	public void setMuxDemux(MuxDemux m) {
		this.m =m;
	}

	@Override
	public void handleMessage(String m) {
		if (config.pgl)System.out.println("[GLOBALLISTHANDER]received : "+ m);
		try {
			String[] parser= m.split(";");
			if (parser[0].equals("LIST")){
			
				queue.add(m);
			}
		}
		catch(Exception e){
			
		}
		
	}

}
