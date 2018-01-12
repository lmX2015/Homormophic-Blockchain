package network;

public interface SimpleMessageHandler extends Runnable  {
	public  void setMuxDemux(MuxDemux m);
	public  void handleMessage(String m); 
	
}
	