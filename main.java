import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

import config.config;
import network.*;
import peerTable.*;
import services.*;
import self.Self;

public class main {

	
	public static void main(String[] args) {
		Self.init();
		String ID = Self.getId();
		DatagramSocket mySocket= null;
		try {
			mySocket = new DatagramSocket(4242);
			//mySocket.setBroadcast(true);
		} catch (SocketException e1) {
			System.out.println("Can not bind socket");
			e1.printStackTrace();
		}

		SimpleMessageHandler[] handlers = new SimpleMessageHandler[1];
		handlers[0]= new HelloHandler();
		//handlers[1]= new HelloHandler();
		//handlers[2]= new DebugHandler();
		//handlers[1]= new SynHandler();
		//handlers[2]= new GlobalListHandler();

		PeerTable table = new PeerTable(config.helloInt);

		MuxDemux dm = new MuxDemux(handlers, mySocket);
		dm.ID=ID;

		table.setMux(dm);

		HelloSender helloSender =new HelloSender(dm);
		helloSender.table =table;

		//ListSender listSender =new ListSender();
		//listSender.table =table;

		/* to add
		FileHandler fh = new FileHandler();
		fh.table=table;
		ServingService ss = new ServingService();
		ss.f=fh;
		*/

		//DownloadService dw = new DownloadService();
		//dw.table=table;
		//table.dw=dw;

		//Thread dwt = new Thread(dw);
		//dwt.start();

		/*
		 * Thread sst =new Thread(ss);
		 
		Thread fht =new Thread(fh);
		sst.start();
		fht.start();
		*/

		//((SynHandler)handlers[1]).lst= listSender;
		//((SynHandler)handlers[1]).ID= ID;

		((HelloHandler)handlers[0]).ID= ID;
		
		

		((HelloHandler)handlers[0]).table = table;

		dm.table=table;


		new Thread(handlers[0]).start();
		//new Thread(handlers[1]).start();
		//new Thread(handlers[2]).start();
		//new Thread(handlers[3]).start();
		//new Thread(handlers[4]).start();

		new Thread(dm).start();
		HTTPServer http = new HTTPServer();
		http.table = table;
		http.start();


		while (true) {
			String payload;
			try {
				payload = dm.getNext();

				byte[] byteArray = payload.getBytes();
				try {
					DatagramPacket dp = new DatagramPacket(byteArray, byteArray.length, InetAddress.getByName("255.255.255.255"), 4242);
					try {
						mySocket.send(dp);
						if (config.pmessageOut)System.out.println("Message send : "+payload);
						//System.out.println("Message send : ");

					} catch (IOException e) {
						e.printStackTrace();
					}
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

	}

}
