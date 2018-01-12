package network;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class HelloMessage {
	
	/* fields */
	private String senderID; 
	private int sequence;
	private String hash;
	private int numPeers;	
	private HashMap<String, Integer> peers; 

	/* getters */
	public String getSenderID(){return this.senderID;}
	public String[] getPeers(){return this.peers.keySet().toArray(new String[0]);}
	public int getSequence(){return this.sequence;}
	public int getPeerNumber() {return this.numPeers;}
	public String getHash() {return this.hash;}
	
	/*setters*/
	public void setSequence(int sequence) {this.sequence =sequence;}
	
	
	/* format 
	 * HELLO;SenderId;sequence;hash;numpeers;peer1;etc; 
	 * */
	public HelloMessage(String s) {
		peers = new HashMap<String, Integer>();
		String regex = "HELLO;((?:[a-z]|[0-9]){1,16});([0-9]{1,10});((?:[a-z]|[0-9]){1,16});([0-9]{1,3})((?:;(?:(?:[a-z]|[0-9]){1,16}))+)";
		Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(s);
		if (matcher.find()) {
			  if(matcher.groupCount()!=5) throw new IllegalArgumentException("Mal formated packet hello");
			  senderID = matcher.group(1);
		      sequence = Integer.parseInt(matcher.group(2));
		      
		      hash = matcher.group(3);
		      numPeers = Integer.parseInt(matcher.group(4));
		      
		      if (numPeers>255) throw new IllegalArgumentException("Too many peers");
		      
		      String[] peersList = matcher.group(5).split(";");
		      if (peersList.length!=numPeers&&peersList.length!=numPeers+1) throw new IllegalArgumentException("Mal formated packet hello : wrong Number of peer");
		      for (int i=0;i<numPeers;++i){
		    	  peers.put(peersList[i+1],1);
		      }
		   }    
		
	}
	public HelloMessage(String senderID, int sequenceNo, String hash){
		peers = new HashMap<String, Integer>();
		this.senderID = senderID;
		sequence =sequenceNo;
		this.hash = hash;
	}
	public String getHelloMessageAsEncodedString(){
		String res="HELLO;";
		res+=senderID;
		res+=";";
		res+=sequence;
		res+=";";
		res+=hash;
		res+=";";
		String[] ps=getPeers();
		res+=ps.length;
		//res+=";";
		for (String p:ps) {
			res+=";";
			res+=p;
		}
		return res;
	}
	public void addPeer(String peerID){
		peers.put(peerID,1);
	}
	public void updatePeer(String peerID,int status){
		peers.put(peerID,status);
	}
	public void removePeer(String peerID){
		peers.remove(peerID);
	}
	public String toString(){
		String res = "HELLO;"+senderID+";"+sequence+";"+hash+";"+numPeers+";";

		String[] Peers = getPeers();
		for (String s: Peers){
			res = res +s+";";
		}
		return res;
	}
}
