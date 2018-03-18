package security;

public class Block {
	private int id;
	private String hash;
	private Payload payload;
	public Block() {}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public Payload getPayload() {
		return payload;
	}
	public void setPayload(Payload payload) {
		this.payload = payload;
	}
	public synchronized  String toString() {
		return "id:"+id+"\n"+"hash:"+hash+"\n"+"data:"+payload;
	}
	

}
