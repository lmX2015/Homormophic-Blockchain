
package peerTable;
import network.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import messages.HelloMessage;
import network.MuxDemux;
import self.Self;

import static java.lang.Math.toIntExact;

import java.net.InetAddress;


public class PeerTable {
	
	/*constant semantic */
	public final int HEARD = 1;
	public final  int SYNCHRONIZED =3;
	public final int INCONSISTENT = 0;
	public final int DELAYED=2;
	//public final int DYING=4;
	
	/*private fields*/
	private  int helloInt;
	private String ID= "";
	private MuxDemux m =null;
	private HashMap<String,Integer[]> table=new HashMap<String,Integer[]>();
	private HashMap <String,Blockchain> datatable = new HashMap<String,Blockchain>();
	private HashMap <String,InetAddress> peeraddress = new HashMap<String,InetAddress>();
	
	
	
	
	/*getters*/
	public synchronized String getID() {return ID;}
	
	
	/*setters*/
	public synchronized void setHelloInt(int h) {
		helloInt = h;
	}
	public void setMux( MuxDemux m) {this.m =m;}
	
	
	//public DownloadService dw =null;
	
	
	/* check the timeouts */
	class Checker implements Runnable{
		String id =null;
		@Override
		public void run() {

			while(table.get(id)!=null) {
				Integer[] peer =table.get(id);
				if (peer[1]==0) {
					kill(id);
					peer[2]=INCONSISTENT;
					table.put(id, peer);

				}
				else {
					peer[1]=1;
					table.put(id,peer);
				}
				try {
					Thread.sleep(helloInt*1000); // convention to take in second
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}
	
	private ArrayList<Thread> checkerList =  new ArrayList<Thread>() ;
	
	/* constructors */
	public PeerTable(int helloInt,int sequence) {
		this.helloInt= helloInt;
	
	}
	public PeerTable(int HelloInt) {
		this.ID=Self.getId();
	}
	public PeerTable(String ID,int helloInt,int sequence) {
		this.ID =ID;
		this.helloInt= helloInt;
	}
	
	public synchronized void kill(String id) {
		table.remove(id);	
	}
	private void addPeer(String id) {
		System.out.println("Adding peeer :"+id);
		Integer[] val= new Integer[4];
		val[0] =0; // block number 
		val[1]= 1; // is checked;
		val[2] = HEARD;
		val[3] = checkerList.size();
		table.put(id, val);
		Checker c = new Checker();
		c.id =id;
		Thread t = new Thread(c);
		t.start();
		checkerList.add(t);
		//HelloSender.send(id);
		System.out.println("New peeer :"+id);
		return;
	}
	public synchronized Integer[] getPeerState(String id) {
		return table.get(id);
	}
	public synchronized void printTable() {
		Iterator it = table.entrySet().iterator();
		while (it.hasNext()) {
			HashMap.Entry pair = (HashMap.Entry)it.next();
			System.out.println(pair.getKey() + " = " + pair.getValue());
			it.remove(); // avoids a ConcurrentModificationException
		}
	}
	public synchronized String toString() {
		Iterator it = datatable.entrySet().iterator();
		String res ="";
		while (it.hasNext()) {
			HashMap.Entry pair = (HashMap.Entry)it.next();
			res = res + "\n"+pair.getKey() + " = " + pair.getValue().toString() + " --- IP =" +peeraddress.get(pair.getKey());
			//it.remove(); // avoids a ConcurrentModificationException
			if (!pair.getKey().equals(ID))res = res + " --- State :"+table.get(pair.getKey())[2];
		}
		return res;
		//return table.toString();
	}
	
	public synchronized void refresh(String peerS) {
		Integer[] peer= table.get(peerS);
		if (peer ==null) {
			addPeer(peerS);
			Blockchain b =new Blockchain();
			datatable.put(peerS, b);
			// Send Hello message TO DO add blockchainNum;
			m.send(new HelloMessage(ID,peerS, 0, "hash").getHelloMessageAsEncodedString());
		}
		else {
			peer[1]= 1;		
			table.put(peerS, peer);
		}
	}
	public synchronized void update(String peer, Blockchain b) {
		datatable.put(peer, b);
	}
	public synchronized Blockchain getB(String peer) {
		return datatable.get(peer);
	}
	public synchronized Integer getState(String peer) {
		Integer[] res = table.get(peer);
		if (res==null) return null;
		return res[0];
	}
	public synchronized void registerIP(InetAddress a, String m) {
		String [] parser = m.split(";");
		if (parser.length<2)return;
		peeraddress.put(parser[1],a);
	}
	public synchronized InetAddress getIP(String peerID) {
		return peeraddress.get(peerID);
	}


}
