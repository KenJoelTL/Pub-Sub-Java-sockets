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

			Socket subscriberSocket 		= new Socket(host,brokerPort);
			Subscriber subscriber 	= new Subscriber(clientID,clientPort,brokerPort);
			subscriber.connect(subscriberSocket);

			subscribeTopicJson(subscriber);
			subscribeTopicXml(subscriber);

			subscriber.listentoBroker(); // start the loop

			// fire event from the publisher
			Socket pubSocket = new Socket(host,brokerPort);
			pub.connect(pubSocket);
			publishJson(pub);
			pubSocket.close();

			pubSocket = new Socket(host,brokerPort);
			pub.connect(pubSocket);
			publishXml(pub);
			pubSocket.close();


			pubSocket = new Socket(host,brokerPort);
			pub.connect(pubSocket);
			removeAd(pub);
			pubSocket.close();

			// finalise by unsubscribing, the last publish should not be working
			unsubscribeTopicJson(subscriber);
			unsubscribeTopicXml(subscriber);
			subscriber.killListener();

			pubSocket = new Socket(host,brokerPort);
			pub.connect(pubSocket);
			publishJson(pub);
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

	private static void publishJson(Publisher p) {
		p.publish("Weather", " { \"hello\":  \"world\"}", IPublication.Format.JSON);
	}

	private static void publishXml(Publisher p) {
		p.publish("Weather", "<note>\n" +
						"<to>Tove</to>\n" +
						"<from>Jani</from>\n" +
						"<heading>Reminder</heading>\n" +
						"<body>Don't forget me this weekend!</body>\n" +
						"</note>", IPublication.Format.XML);
	}

	private static void removeAd(Publisher p) {
		Topic t = new Topic("Weather");
		IPublication.Format f = IPublication.Format.JSON;
		p.unadvertise(t, f);
	}

	private static void subscribeTopicJson(Subscriber s) {
		Topic t = new Topic("Weather");
		IPublication.Format f = IPublication.Format.JSON;
		s.subscribe(t, f);
	}

	private static void subscribeTopicXml(Subscriber s) {
		Topic t = new Topic("Weather");
		IPublication.Format f = IPublication.Format.XML;
		s.subscribe(t, f);
	}

	private static void unsubscribeTopicJson(Subscriber s) {
		Topic t = new Topic("Weather");
		IPublication.Format f = IPublication.Format.JSON;
		s.unsubscribe(t, f);
	}

	private static void unsubscribeTopicXml(Subscriber s) {
		Topic t = new Topic("Weather");
		IPublication.Format f = IPublication.Format.XML;
		s.unsubscribe(t, f);
	}

}
