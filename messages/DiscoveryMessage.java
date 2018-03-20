package messages;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DiscoveryMessage {
	
	private String Id;
	private int sequence;
	private String hash;
	
	public synchronized String getId() {
		return Id;
	}
	public synchronized void setId(String id) {
		Id = id;
	}
	public synchronized int getSequence() {
		return sequence;
	}
	public synchronized void setSequence(int sequence) {
		this.sequence = sequence;
	}
	public synchronized String getHash() {
		return hash;
	}
	public synchronized void setHash(String hash) {
		this.hash = hash;
	}
	
	public DiscoveryMessage(String id, int sequence, String hash) {
		super();
		Id = id;
		this.sequence = sequence;
		this.hash = hash;
	}
	public DiscoveryMessage(String s) {
		String regex = "HELLO;([0-9]{1,10});((?:[a-z]|[0-9]){1,128})";
		Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(s);
		if (matcher.find()) {
			  if(matcher.groupCount()!=3) throw new IllegalArgumentException("Mal formated packet hello");
			  Id = matcher.group(1);
		      sequence = Integer.parseInt(matcher.group(2));
		      hash = matcher.group(3);
		}    
		
	}
	public String toString() {
		return "DISC;"+Id+";"+sequence+";"+hash;
	}	
	

}
