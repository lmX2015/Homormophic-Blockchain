
package peerTable;
import network.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import network.MuxDemux;

import static java.lang.Math.toIntExact;

import java.net.InetAddress;


public class PeerTable {
	
	/*constant semantic */
	public final int HEARD = 1;
	public final  int SYNCHRONIZED =3;
	public final int DYING = 2;
	public final int INCONSISTENT = 0;
	
	/*private fields*/
	private  int helloInt;
	private  int numAlive;
	private String ID= "";
	private MuxDemux m =null;
	private HashMap<String,Integer[]> table=new HashMap<String,Integer[]>();
	private HashMap <String,Database> datatable = new HashMap<String,Database>();
	private HashMap <String,InetAddress> peeraddress = new HashMap<String,InetAddress>();
	
	
	
	
	/*getters*/
	public synchronized String getID() {return ID;}
	
	
	/*setters*/
	public synchronized void setHelloInt(int h) {
		helloInt = h;
	}
	public void setMux( MuxDemux m) {this.m =m;}
	
	
	public DownloadService dw =null;
	
	
	/* check the timeouts */
	class Checker implements Runnable{
		String id =null;
		int timeout =1;
		@Override
		public void run() {

			while(table.get(id)!=null && (table.get(id)[2]!=DYING)) {
				Integer[] peer =table.get(id);
				if (peer[1]==0) {
					//kill(id);
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
		this.numAlive =0;

	}

	public PeerTable(String content,String ID,int helloInt,int sequence) {
		Database db = new Database();
		db.addMessage(content);
		db.addMessage(content);
		db.setVersion(sequence);
		this.ID =ID;
		datatable.put(ID,db);
		this.helloInt= helloInt;
		this.numAlive =0;
	}
	public synchronized String[] getMyData() {
		Database db = datatable.get(ID);
		return db.getAll();
	}
	
	public synchronized void kill(String id) {
		Integer[] p =table.get(id);
		if( p==null) return;
		p[2]= DYING;
		table.put(id,p);
		
		numAlive --;
	}
	private void addPeer(String id,int timeout) {
		System.out.println("Adding peeer :"+id);
		Integer[] val= new Integer[4];
		val[0] =0; // numero
		val[1]= 1;
		val[2] = HEARD;
		val[3] = checkerList.size();
		table.put(id, val);
		Checker c = new Checker();
		c.id =id;
		c.timeout=timeout;
		Thread t = new Thread(c);
		t.start();
		checkerList.add(t);
		numAlive ++;
		m.send(new SynMessage(ID,0,id).getSynMessageAsEncodedString());
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
	
	public synchronized String[] toStringArr() {
		String[] res = new String[numAlive];
		int count =0;
		for (Entry<String,Integer[]> pair : table.entrySet()){
			//iterate over the pairs
			//System.out.println(pair.getKey()+" "+pair.getValue());
			if (pair.getValue()[2]!=DYING) {
				res[count]=pair.getKey();
				++count;
			}
		}
		return res;
	}
	public synchronized String[] getData(String peerID) {
		Integer[] peer=table.get(peerID);
		if (peer==null) return null;
		if (peer[2]==DYING)return null;
		Database db =datatable.get(peerID);
		if (db==null) return null;
		return db.getAll();
	}
	public synchronized void refresh(String peerS,int timeout,int number) {



		Integer[] peer= table.get(peerS);
		if (peer ==null) {
			addPeer(peerS,timeout);
			Database db =new Database();
			datatable.put(peerS, db);
			db.setVersion(0);
			// syn request;
			m.send(new SynMessage(ID,number,peerS).getSynMessageAsEncodedString());
		}
		else {
			peer[1]= 1;
		
			//Database db = datatable.get(peerS);
			//db.setVersion(db.getVersion()+1);
			//db.clean();
			if (peer[0]<number) {
				Database db = datatable.get(peerS);
				db.setVersion(number);
				//db.clean();
				m.send(new SynMessage(ID,number,peerS).getSynMessageAsEncodedString());
			}
			
			
		}
	}
	public synchronized void updateDb(String peerID,int num,String[]content) {
		Database db = datatable.get(peerID);
		Integer[] peer= table.get(peerID);
		peer[2]=SYNCHRONIZED;
		if (db.getVersion()==num);
		db.clean();
		db.addAll(content);
		InetAddress ip = peeraddress.get(peerID);
		if (ip==null) return;
		for (String s : content) {
			dw.handle(new fileRequest(ip, s, peerID));
		}
		
	}
	public synchronized void registerIP(InetAddress a, String m) {
		String [] parser = m.split(";");
		if (parser.length<2)return;
		peeraddress.put(parser[1],a);
	}
	public synchronized InetAddress getIP(String peerID) {
		return peeraddress.get(peerID);
	}
	public synchronized void changeMyDb(String[] content) {
		Database db=datatable.get(ID);
		int newversion = db.getVersion()+1;
		Database newdb =new Database();
		newdb.setVersion(newversion);
		newdb.addAll(content);
		datatable.put(ID,newdb);
	};
	public synchronized int getMyDbVersion() {
		return datatable.get(ID).getVersion();
	}
	public Integer getState(String peerID) {
		Integer[] state = table.get(peerID);
		if (state==null)return null;
		return state[2];
	}


}
