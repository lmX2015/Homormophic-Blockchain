package services;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import peerTable.PeerTable;


public class HTTPServer extends Thread {
	  
	  public PeerTable table;
	  
	  @Override 
	  public void run()  {
	    HttpServer server;
		try {
			server = HttpServer.create(new InetSocketAddress(4243), 0);
			server.createContext("/getDB", new MyHandler());
			server.setExecutor(null); // creates a default executor
			server.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   
	  }

	  class MyHandler implements HttpHandler {
	    public void handle(HttpExchange t) throws IOException {
	      byte [] response = table.toString().getBytes();
	      t.sendResponseHeaders(200, response.length);
	      OutputStream os = t.getResponseBody();
	      os.write(response);
	      os.close();
	    }
	  }
	}