package network;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SynMessage {
	/* fields */
	private String senderID; 
	private int sequence;  
	private String peerID; 
	
	/* getters */
	public String getSenderID(){return this.senderID;}
	public int getSequence(){return this.sequence;}
	public String getPeerID(){return this.peerID;}
	
	public SynMessage(String s) {
		String regex = "HELLO;((?:[a-z]|[0-9]){1,16});((?:[a-z]|[0-9]){1,16});([0-9]{1,10});";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(s);
		    if (matcher.find()) {
		    	 if(matcher.groupCount()!=4) throw new IllegalArgumentException("Mal formated packet syn");
			     
		      senderID = matcher.group(1);
		      sequence = Integer.parseInt(matcher.group(3));
		      
		      peerID = matcher.group(3);
		      String[] peersList = matcher.group(5).split(";");
		     
		   }    
		
	}
	public SynMessage(String senderID, int sequenceNo, String peerId){
		//peers = new HashMap<String, Integer>();
		this.senderID = senderID;
		sequence =sequenceNo;
		this.peerID = peerId;
	}
	public String getSynMessageAsEncodedString(){
		String res="SYN;";
		res+=senderID;
		res+=";";
		res+=peerID;
		res+=";";
		res+=sequence;
		res+=";";
		
		return res;
	}
	public String toString(){
		
		return getSynMessageAsEncodedString();
	}

}
