package network;
import java.net.InetAddress;

public class fileRequest {
	private InetAddress add;
	private String filename;
	private String id;
	public fileRequest(InetAddress a,String f,String id) {
		add=a;
		filename =f;
		this.id =id;
	}
	public InetAddress getAd() {return add;}
	public String getName() {return filename;}
	public String getId() {return id;}
}
