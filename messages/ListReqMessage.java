package messages;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ListReqMessage {

	private String senderId;
	private String receiverId;

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

	public ListReqMessage(String s) {
		String regex = "LISTREQ;((?:[a-z]|[0-9]){1,16});((?:[a-z]|[0-9]){1,16})";
		Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(s);
		if (matcher.find()) {
			if(matcher.groupCount()!=2) throw new IllegalArgumentException("Mal formated packet hello");
			senderId = matcher.group(1);
			receiverId = matcher.group(2);

		}    


	}
	public ListReqMessage(String senderId, String receiverId) {
		super();
		this.senderId = senderId;
		this.receiverId = receiverId;
	}
	public String toString() {
		return senderId+";"+receiverId;
	}





}
