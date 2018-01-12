package security;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Payload {
	private ArrayList<Transaction> trades;
	
	public synchronized ArrayList<Transaction> getTransactions() {
		return trades;
	}
	public Payload() {
		trades = new ArrayList<Transaction>();
	}
	public Payload(String payload) {
		trades = new ArrayList<Transaction>();
		String regex = "TR:([0-9]{1,2})((?:,(?:(?:[a-z]|[0-9]){1,16});(?:(?:[a-z]|[0-9]){1,16});(?:[0-9]{1,10});(?:[0-9]{1,10});(?:(?:[a-z]|[0-9]){1,16});(?:(?:[0-9]{1,10})|(?:null));(?:(?:[0-9]{1,10})|(?:null)))*)";
		Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(payload);
		if (matcher.find()) {
			if (matcher.groupCount()!=2) throw new IllegalArgumentException("Malformated payload");
			int tradeNum = Integer.parseInt(matcher.group(1));
			String [] trans = matcher.group(2).split(",");
			if (trans.length!=tradeNum+1) throw new IllegalArgumentException("Malformated payload");
			for (int i=1;i<trans.length;i++) {
				trades.add(new Transaction(trans[i]));
			}
		}
	}
	public synchronized String toString() {
		String res="TR:";
		res+=trades.size();
		for (int i =0;i<trades.size();i++) {
			res+=",";
			res+=trades.get(i);
		}
		return res;
	}
	/// TO DO : hello message style ....
	
}
