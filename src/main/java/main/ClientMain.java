package main;

import classes.Publisher;
import classes.Subscriber;
import classes.Topic;
import interfaces.IPublication;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientMain {

	public static void main(String[] args) {
	
		int clientPort 	= 5050;
		int brokerPort	= 8022;
		String host 	= "127.0.0.1";
		long clientID 	= 1;
		
		try {
			
			Socket s 		= new Socket(host,brokerPort);
			//Subscriber sub 	= new Subscriber(clientID,clientPort,brokerPort);
			Publisher pub 	= new Publisher(clientID,clientPort,brokerPort);
			pub.connect(s);
			createAd(pub);
			s.close();

			s = new Socket(host,brokerPort);
			publish(pub);
			s.close();


			s = new Socket(host,brokerPort);
			pub.connect(s);
			removeAd(pub);

//			sub.connect(s);
//			sub.listentoBroker();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}


	private static void createAd(Publisher p) {
		Topic t = new Topic("Weather");
		IPublication.Format f = IPublication.Format.JSON;
		p.advertise(t, f);
	}

	private static void publish(Publisher p) {
		Topic t = new Topic("Weather");
		IPublication.Format f = IPublication.Format.JSON;
		String content = " { \"hello\":  \"world\"}";
		p.publish(t.getName(), content);
	}

	private static void removeAd(Publisher p) {
		Topic t = new Topic("Weather");
		IPublication.Format f = IPublication.Format.JSON;
		p.unadvertise(t, f);
	}

}
