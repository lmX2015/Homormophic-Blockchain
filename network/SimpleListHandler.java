package network;

import config.config;
import peerTable.Database;

public class SimpleListHandler  {
	GlobalListHandler g;
	String senderID;
	String peerID;
	int sequence;
	String[] parts;
	int totalParts;
	boolean[] received;
	int processed;
	public SimpleListHandler(String senderID,String peerID,int sequence,Database data) {
		this.senderID= senderID;
		this.peerID = peerID;
		this.sequence = sequence;
		//this.data = data;
		this.totalParts=totalParts;
		received = new boolean[totalParts];
		processed=0;
		this.parts = data.getAll();
	}
	
		
	public SimpleListHandler(String s) {
		String[] parser = s.split(";");
		if (!parser[0].equals("LIST"))return;
		if (parser.length<7) {
			System.err.println("Malformated Packet");
			return;
		}
		this.peerID=parser[1];
		this.senderID=parser[2];
		this.sequence = Integer.parseInt(parser[3]);
		this.totalParts =Integer.parseInt(parser[4]);
		parts =new String[totalParts];
		received = new boolean[totalParts];
		for (int i =0; i<totalParts;++i) {
			received[i]=false;
		}
		processed =0;		
			
		
	}
	public synchronized void handleMessage(String s) {
		if (config.psimlist)System.out.println("[LISTHANDLER]"+this.peerID+"handling : "+ s);
		parse(s);
	}
	private void parse(String s) {
		String[] parser = s.split(";");
		if (!parser[0].equals("LIST"))return;
		if (parser.length<7) {
			System.err.println("Malformated Packet");
			return;
		};
		/*if (!parser[1].equals(this.peerID)) {
			System.err.println("wrong peerID");
			
			return;
		}*/
		//if (!parser[2].equals(this.senderID)) return;
		if (!parser[3].equals(""+this.sequence )) { 
			if (config.psimlist)System.out.println("[LISTHANDLER]["+this.peerID+"] wrong sequence#" );
			return;
		}
		if (!parser[4].equals(""+this.totalParts )) {
			System.err.println("wrong part number");
			return;
		}
		int index = Integer.parseInt(parser[5]);
		//System.out.println("[LISTHANDLER] Received part : "+index );
		
		String d = parser[6]; 
		if (index>totalParts) {
			System.err.println("out of bond index part");
		}
		if (!received[index]) {
				received[index]= true;
				
				processed ++;
				parts[index]=d;
				if (config.psimlist)System.out.println("[LISTHANDLER] Received : "+d+" Missing "+(totalParts-processed) );
				if (processed>=totalParts)g.finish(this);
		}
		
		
	}
	

}
