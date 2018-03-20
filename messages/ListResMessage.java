package messages;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ListResMessage {
	private String senderId;
	private String receiverId;
	private int numBlock;
	private String[] blocks;
	
	
	
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



	public synchronized int getNumBlock() {
		return numBlock;
	}



	public synchronized void setNumBlock(int numBlock) {
		this.numBlock = numBlock;
	}



	public synchronized String[] getBlocks() {
		return blocks;
	}



	public synchronized void setBlocks(String[] blocks) {
		this.blocks = blocks;
	}



	public ListResMessage(String senderId, String reiceiverId, int numBlock, String[] blocks) {
		super();
		this.senderId = senderId;
		this.receiverId = reiceiverId;
		this.numBlock = numBlock;
		this.blocks = blocks;
	}
	
	public ListResMessage(String s) {
		String regex = "LISTRES;((?:[a-z]|[0-9]){1,16});((?:[a-z]|[0-9]){1,16});([0-9]{1,100});((?:(?:[a-z]|[0-9]){1,128};)+)";
		Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(s);
		if (matcher.find()) {
			if(matcher.groupCount()!=4) throw new IllegalArgumentException("Mal formated packet ListRes");
			senderId = matcher.group(1);
			receiverId = matcher.group(2);

			numBlock = Integer.parseInt(matcher.group(3));

			blocks = matcher.group(4).split(";");
			if (blocks.length!=numBlock)throw new IllegalArgumentException("Mal formated packet ListRes");
		}    

	}
	public String toString() {
		String res = "LISTRES;"+senderId+";"+receiverId+";"+numBlock+";";
		for (String b: blocks) {
			res= res+ b+";";
		}
		return res;
	}
	
	
}
