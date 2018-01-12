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


public class main {

	/**
	 * @param args
	 */

	public static void main(String[] args) {
		//team.printTeam();
		String ID=config.ID;

		DatagramSocket mySocket= null;
		try {
			mySocket = new DatagramSocket(4242);
			//mySocket.setBroadcast(true);
		} catch (SocketException e1) {
			System.out.println("Can not bind socket");
			e1.printStackTrace();
		}

		SimpleMessageHandler[] handlers = new SimpleMessageHandler[3];
		handlers[0]= new HelloHandler();
		//handlers[1]= new HelloHandler();
		//handlers[2]= new DebugHandler();
		handlers[1]= new SynHandler();
		handlers[2]= new GlobalListHandler();

		PeerTable table = new PeerTable("lorem ipsum bla bla bla",ID,config.helloInt,config.firstSeq);

		MuxDemux dm = new MuxDemux(handlers, mySocket);
		dm.ID=ID;

		table.setMux(dm);

		HelloSender helloSender =new HelloSender(ID, config.helloInt, config.firstSeq,dm);
		helloSender.table =table;

		ListSender listSender =new ListSender();
		listSender.table =table;

		/* to add
		FileHandler fh = new FileHandler();
		fh.table=table;
		ServingService ss = new ServingService();
		ss.f=fh;
		*/

		DownloadService dw = new DownloadService();
		dw.table=table;
		table.dw=dw;

		Thread dwt = new Thread(dw);
		dwt.start();

		/*
		 * Thread sst =new Thread(ss);
		 
		Thread fht =new Thread(fh);
		sst.start();
		fht.start();
		*/

		((SynHandler)handlers[1]).lst= listSender;
		((SynHandler)handlers[1]).ID= ID;

		((HelloHandler)handlers[0]).ID= ID;
		
		((GlobalListHandler)handlers[2]).db= table;

		listSender.setMux(dm);
		new Thread(listSender).start();


		((HelloHandler)handlers[0]).table = table;

		dm.table=table;


		new Thread(handlers[0]).start();
		new Thread(handlers[1]).start();
		new Thread(handlers[2]).start();
		//new Thread(handlers[3]).start();
		//new Thread(handlers[4]).start();

		new Thread(dm).start();
		HTTPServer http = new HTTPServer();
		http.table = table;
		http.start();

		if(config.cmlineon) {
			Thread UserInput = new Thread() {
				public void run() {
					Scanner sc = new Scanner(System.in);
					while (true) {
						String line = sc.next();
						if(!line.equals("Stop"))System.out.println("Did not understand your command");
						else break;

					}
					//DyingMessage IDONTWANADIE= new DyingMessage();
					//dm.send(IDONTWANADIE.getMessage());
					System.out.println("Server interrupted by user ");
					System.out.println("Your peers have been notified by a dying Message");
					System.out.println("GoodBye");
					Runtime.getRuntime().exit(0);


				}
			};
			UserInput.start();
		}
		if (config.interrupthandler) {
			Runtime.getRuntime().addShutdownHook(new Thread() {

				public void run() { 
					//DyingMessage IDONTWANADIE= new DyingMessage();
					//dm.send(IDONTWANADIE.getMessage());
					System.out.println("Server interrupted by user ");
					System.out.println("Your peers have been notified by a dying Message");
					System.out.println("GoodBye");

				}

			});
		}
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
