package network;

public class ListMessage {
	String senderID;
	String peerID;
	int sequence;
	int nparts;
	int part;
	String data;
	public ListMessage(String senderID,String peerID,int sequence, int nparts,int part,String data){
		this.senderID=senderID;
		this.peerID=peerID;
		this.sequence =sequence;
		this.nparts=nparts;
		this.part =part;
		this.data = data;
	}
	public ListMessage(String s) {
		String[] parser = s.split(";");
		if (!parser[0].equals("LIST")) throw new IllegalArgumentException();
		if (parser.length!=7) {
			System.err.println("Malformated Packet");
			throw new IllegalArgumentException();
		}
		senderID=parser[1];
		peerID=parser[2];
		sequence = Integer.parseInt(parser[3]);
		nparts = Integer.parseInt(parser[4]);
		part =Integer.parseInt(parser[5]);
		data =parser[6];
	}
	public String getMessage() {
		return "LIST;"+senderID+";"+peerID+";"+sequence+";"+nparts+";"+part+";"+data+";";
	}

		
}
