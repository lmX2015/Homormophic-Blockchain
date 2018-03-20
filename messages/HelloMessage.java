package messages;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class HelloMessage {

	/* fields */
	private String senderId;
	private String receiverId;
	private int sequence;
	private String hash;


	public synchronized String getSenderId() {
		return senderId;
	}
	public synchronized void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	public synchronized String getReceiverId() {
		return receiverId;
	}
	public synchronized void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
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
	/* format 
	 * HELLO;SenderId;ReceiverId;sequence;hash;
	 * */
	public HelloMessage(String s) {
		String regex = "HELLO;((?:[a-z]|[0-9]){1,16});((?:[a-z]|[0-9]){1,16});([0-9]{1,10});((?:[a-z]|[0-9]){1,128})";
		Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(s);
		if (matcher.find()) {
			if(matcher.groupCount()!=4) throw new IllegalArgumentException("Mal formated packet hello");
			senderId = matcher.group(1);
			receiverId = matcher.group(2);

			sequence = Integer.parseInt(matcher.group(3));

			hash = matcher.group(4);
		}    

	}
	public HelloMessage(String senderID, String r, int sequenceNo, String hash){
		this.senderId = senderID;
		this.receiverId= r;
		sequence =sequenceNo;
		this.hash = hash;
	}
	public String getHelloMessageAsEncodedString(){
		String res="HELLO;";
		res+=senderId;
		res+=";";
		res+=receiverId;
		res+=";";

		res+=sequence;
		res+=";";
		res+=hash;
		return res;
	}
	public String toString(){
		String res="HELLO;";
		res+=senderId;
		res+=";";
		res+=receiverId;
		res+=";";

		res+=sequence;
		res+=";";
		res+=hash;
		return res;

	}
}
