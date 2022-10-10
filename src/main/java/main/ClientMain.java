package main;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import classes.Client;
import classes.Subscriber;

public class ClientMain {

	public static void main(String[] args) {
	
		int clientPort 	= 5050;
		int brokerPort	= 8022;
		String host 	= "127.0.0.1";
		long clientID 	= 1;
		
		try {
			
			Socket s 		= new Socket(host,brokerPort);
			Subscriber sub 	= new Subscriber(clientID,clientPort,brokerPort);
			sub.connect(s);
			sub.listentoBroker();
			
			
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
