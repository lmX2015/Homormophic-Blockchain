package network;
import java.io.BufferedReader;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.SynchronousQueue;

import peerTable.PeerTable;
import config.config;

public class DownloadService implements Runnable{
	private SynchronousQueue <fileRequest> q;
	private boolean isRunning =true;
	private String path ;
	public PeerTable table =null;
	public DownloadService () {
		q = new SynchronousQueue<fileRequest>();
		path = config.pathPeerFile;

	}
	public void handle(fileRequest f ) {
		q.add(f);
	}
	@Override
	public void run() {
		System.out.println("DowloadServie Started");
		BufferedReader in;
		PrintWriter out;
		while (isRunning) {
			try {
				fileRequest r=q.take();
				// check the state of the peer
				Integer state =table.getState(r.getId());
				if (!(state==null ||state==table.DYING)) {
					String pathfile = path+'/'+r.getId();
					PrintWriter output = new PrintWriter(pathfile);
					//out.write(data);
					Socket socket = new Socket(r.getAd(),4242);	
					in = new BufferedReader (new InputStreamReader (socket.getInputStream()));
					out = new PrintWriter(socket.getOutputStream());
					out.println("get "+r.getName());
					out.flush();

					String message_distant = in.readLine();
					if (!message_distant.equals(r.getName())) {
						socket.close();
						output.close();
						in.close();
						out.close();
					}

					else {
						in.readLine();// discard size, we are using a buffer
						message_distant =in.readLine();
						while(message_distant!=null) {
							output.println(message_distant);
							message_distant = in.readLine();
						}

						output.close();
						out.close();
						in.close();
						socket.close();
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public synchronized void stop() {
		this.isRunning=false;
	}

}
