package peerTable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Database {
	
	private ArrayList<String> data = new ArrayList();
	
	private int version =0;
	public synchronized int getVersion() {
		return version;
	}
	public synchronized void setVersion(int v) {
		version =v;
	}
	public synchronized String getPart(int part) {
		return data.get(part);
	}
	public synchronized void setPart(int part,String content) {
		if (content.length()>255) {
			System.err.println("Part too long");
			return;
		}
		data.set(part,content);
		version ++;
	}
	public synchronized String toString() {
		return data.toString();
	}
	
	public synchronized String[] getAll() {
		String[] res = new String [data.size()];
		return data.toArray(res);
	}
	public synchronized void addAll(String[] c) {
		for (String s : c) {
			data.add(s);
		}
	}
	public synchronized void addMessage(String content) {
		for (int i=0;i<content.length();i+=254) {
			data.add(content.substring(i*255, Math.min((i+1)*255,content.length())));
		}
		version ++;
		
	}
	public synchronized void remove(int part) {
		data.remove(part);
		
	}
	public synchronized void clean() {
		data = new  ArrayList<String>();
		
	}
	
	
	
}
