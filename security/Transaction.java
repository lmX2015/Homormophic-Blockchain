package security;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Transaction {
	
	private String senderId;
	private String receiverId;
	private int amount;
	private int nonce;
	private String signature;
	private Integer blockNumber;
	private Integer blockIndex;
	
	public synchronized String getSenderId() {return senderId;}
	public synchronized String getReceiverId() {return receiverId;}
	public synchronized int getAmount() {return amount;}
	public synchronized String getSignature() {return signature;}
	public synchronized int getNonce(){return nonce;}
	public synchronized int getBlockNumber(){return blockNumber;}
	public synchronized int getBlockIndex(){return blockIndex;}
	
	
	public synchronized void setBlockNumber(int newb) {blockNumber=newb;}
	public synchronized void setBlockIndex(int newi) {blockIndex=newi;}
	
	public Transaction(String senderId,String receiverId,int amount,int nonce,String signature) {
		this.senderId=senderId;
		this.receiverId=receiverId;
		this.amount=amount;
		this.nonce = nonce;
		this.signature = signature;
		blockNumber= null;
		blockIndex= null;
		
	}
	public Transaction(String trade) {
		// TO DO regex parsing !!!!
		//format : SenderId;ReceiverId;amount;nonce;signature;blockNumber;blockIndex;
		String regex = "((?:[a-z]|[0-9]){1,16});((?:[a-z]|[0-9]){1,16});([0-9]{1,10});([0-9]{1,10});((?:[a-z]|[0-9]){1,16});((?:[0-9]{1,10})|(?:null));((?:[0-9]{1,10})|(?:null))";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(trade);
		if (!matcher.find()) throw new IllegalArgumentException("Mal formated transaction");
		if(matcher.groupCount()!=7) throw new IllegalArgumentException("Mal formated transaction");
	      
		senderId = matcher.group(1);
	    receiverId= matcher.group(2);
	    amount =Integer.parseInt(matcher.group(3));
	    nonce = Integer.parseInt(matcher.group(4));
	    signature=matcher.group(5);
	    if (matcher.group(6).equals("null"))blockNumber=null;
	    else {
	    	blockNumber = Integer.parseInt(matcher.group(6));
		    
	    }
	    if (matcher.group(7).equals("null"))blockIndex=null;
	    else {
	    	blockIndex = Integer.parseInt(matcher.group(7));
		    
	    }
	    
	    
	    //blockNumber = matcher.group(6);
	    
		
		
	}
	
	public String toString() {
		String res = "";
		res+=senderId;
		res+=";";
		res+=receiverId;
		res+=";";
		res+=amount;
		res+=";";
		res+=nonce;
		res+=";";
		res+=signature;
		res+=";";
		res+=blockNumber;
		res+=";";
		res+=blockIndex;
		res+=";";
		return res;
	}
}
