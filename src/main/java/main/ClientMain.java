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

		// integrated tests

		int clientPort 	= 5050;
		int brokerPort	= 8022;
		String host 	= "127.0.0.1";
		long clientID 	= 1;

		Publisher pub = new Publisher(clientID,clientPort,brokerPort);
		tryPublisher(pub, host, brokerPort);
		trySubscriber(pub, host, brokerPort);
	}

	private static void tryPublisher(Publisher pub, String host, int brokerPort){
		try {

			Socket s = new Socket(host,brokerPort);
			pub.connect(s);
			createAd(pub);
			s.close();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void trySubscriber(Publisher pub, String host, int brokerPort) {
		try {

			int clientPort 	= 5051;
			long clientID 	= 1;

			Socket s 		= new Socket(host,brokerPort);
			Subscriber sub 	= new Subscriber(clientID,clientPort,brokerPort);
			sub.connect(s);

			subscribeTopic(sub);
			sub.listentoBroker(); // start the loop

			// fire event from the publisher
			Socket pubSocket = new Socket(host,brokerPort);
			pub.connect(pubSocket);
			publish(pub);
			pubSocket.close();

			pubSocket = new Socket(host,brokerPort);
			pub.connect(pubSocket);
			removeAd(pub);
			pubSocket.close();

			// finalise by unsubscribing
			unsubscribeTopic(sub);

			//sub.killListener();

			pubSocket = new Socket(host,brokerPort);
			pub.connect(pubSocket);
			publish(pub);
			pubSocket.close();

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
		p.publish("Weather", " { \"hello\":  \"world\"}");
	}

	private static void removeAd(Publisher p) {
		Topic t = new Topic("Weather");
		IPublication.Format f = IPublication.Format.JSON;
		p.unadvertise(t, f);
	}

	private static void subscribeTopic(Subscriber s) {
		Topic t = new Topic("Weather");
		IPublication.Format f = IPublication.Format.JSON;
		s.subscribe(t, f);
	}

	private static void unsubscribeTopic(Subscriber s) {
		Topic t = new Topic("Weather");
		IPublication.Format f = IPublication.Format.JSON;
		s.unsubscribe(t, f);
	}


}
