package network;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import peerTable.PeerTable;


//import java.util.concurrent.SynchronousQueue;

/* listen for udp packet; store ip addresses; 
 * dispatch to all handlers received packet; 
 * maintain a pool of message to be sent (transmitted by senders)
 * */

public class MuxDemux implements Runnable{
    
	/* private objects */
	private DatagramSocket myS = null;
    private BufferedReader in;
    private SimpleMessageHandler[] myMessageHandlers;
    private BlockingQueue<String> outgoing = new LinkedBlockingQueue<String>(100);
    
    
    /*public fields */
    public String ID;
    public PeerTable table;
    public MuxDemux (SimpleMessageHandler[] h, DatagramSocket mySocket){
        myS = mySocket;
        myMessageHandlers = h;
    }
    
    public String getNext() throws InterruptedException {
    	return outgoing.take();  	
    }
    
    
    public void run(){
        for (int i=0; i<myMessageHandlers.length; i++){
            myMessageHandlers[i].setMuxDemux(this);
        }
        try{
        	while (true) {
        		byte[] buf = new byte[1000];
        		DatagramPacket packet = new DatagramPacket(buf, buf.length);
        		myS.receive(packet);
        		in = new BufferedReader(new InputStreamReader( new DataInputStream(new ByteArrayInputStream(packet.getData(), packet.getOffset(), packet.getLength()))));
        		String message;

        		while ((message = in.readLine()) != null) { 
        			for (int i=0; i<myMessageHandlers.length; i++){
        				myMessageHandlers[i].handleMessage(message);
        				
        			}
        			table.registerIP(packet.getAddress(),message );
            		
        		}
        		
        	}
        }catch (IOException e){ }		
        try{
            in.close(); myS.close();
        }catch(IOException e){ }
    }

    public void receivedFakeMessage(String message) {
    	for (int i=0; i<myMessageHandlers.length; i++){
			myMessageHandlers[i].handleMessage(message);
			
		}
		
    }
    public void send(String s){
    	//System.out.println("Message added to outgoing queue :"+s);
        outgoing.add(s);	
    }

}

