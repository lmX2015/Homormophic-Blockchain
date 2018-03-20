package peerTable;

public class Blockchain {
	private int id;
	private String hash;
	private int size;
	public synchronized int getId() {
		return id;
	}
	public synchronized void setId(int id) {
		this.id = id;
	}
	public synchronized String getHash() {
		return hash;
	}
	public synchronized void setHash(String hash) {
		this.hash = hash;
	}
	public synchronized int getSize() {
		return size;
	}
	public synchronized void setSize(int size) {
		this.size = size;
	}
	public Blockchain(int id, String hash, int size) {
		super();
		this.id = id;
		this.hash = hash;
		this.size = size;
	}
	public Blockchain(String hash, int size) {
		super();
		this.hash = hash;
		this.size = size;
	}
	public Blockchain() {
		
	};
	
	public String toString() {
		return size+";"+hash;
	}
	
}
