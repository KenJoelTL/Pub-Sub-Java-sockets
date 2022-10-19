package main;

import classes.Publisher;
import classes.Subscriber;
import classes.Topic;
import interfaces.IPublication;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientMain {

    public static Subscriber s1;
    public static Subscriber s2;
    public static Subscriber s3;
    public static Subscriber s4;
    public static Subscriber s5;

    public static Publisher p1;
    public static Publisher p2;
    public static Publisher p3;
    public static Publisher p4;
    public static Publisher p5;

    public static long ID_CLIENT = 0;
    public static void main(String[] args) {

        // integrated tests

        int clientPort = 5050;
        int brokerPort = 8022;
        String host = "127.0.0.1";
        long clientID = 1;

        try {
            createSubs(host, clientPort, brokerPort);

            createPubs(host, clientPort+5, brokerPort);

            Thread.sleep(300);

            connectPubs();

            createAds();

            Thread.sleep(300);

            connectSubs();

            subscribeToTopics();

            Thread.sleep(300);

            connectPubs();

            Thread.sleep(300);

            publishToTopics();
//
//            Thread.sleep(300);
//
//            unsubscribeToTopics();
//
//            Thread.sleep(300);

            disconnectPubs();
            disconnectSubs();


        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


//        Publisher pub = new Publisher(clientID, clientPort, brokerPort);
//        tryPublisher(pub, host, brokerPort);
//        trySubscriber(pub, host, brokerPort);
    }

//    private static void tryPublisher(Publisher pub, String host, int brokerPort) {
//        try {
//            String topicName = "Weather";
//
//            Socket s = new Socket(host, brokerPort);
//            pub.connect(s);
//            createAd(pub, topicName);
//            s.close();
//
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static void trySubscriber(Publisher pub, String host, int brokerPort) {
//        try {
//
//            int clientPort = 5051;
//            long clientID = 1;
//            String topicName = "Weather";
//
//            Socket subscriberSocket = new Socket(host, brokerPort);
//            Subscriber subscriber = new Subscriber(clientID, clientPort, brokerPort);
//            subscriber.connect(subscriberSocket);
//
//            subscribeTopicJson(subscriber, topicName);
//            subscribeTopicXml(subscriber, topicName);
//
//            subscriber.listenToBroker(); // start the loop
//
//            // fire event from the publisher
//            Socket pubSocket = new Socket(host, brokerPort);
//            pub.connect(pubSocket);
//            publishJson(pub, topicName);
//            pubSocket.close();
//
//            pubSocket = new Socket(host, brokerPort);
//            pub.connect(pubSocket);
//            publishXml(pub, topicName);
//            pubSocket.close();
//
//
//            pubSocket = new Socket(host, brokerPort);
//            pub.connect(pubSocket);
//            removeAd(pub, topicName);
//            pubSocket.close();
//
//            // finalise by unsubscribing, the last publish should not be working
//            unsubscribeTopicJson(subscriber, topicName);
//            unsubscribeTopicXml(subscriber, topicName);
//            subscriber.killListener();
//
//            pubSocket = new Socket(host, brokerPort);
//            pub.connect(pubSocket);
//            publishJson(pub, topicName);
//            pubSocket.close();
//
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    private static void createAd(Publisher p, String topicName) {
        Topic t = new Topic(topicName);
        IPublication.Format f = IPublication.Format.JSON;
        p.advertise(t, f);
    }

    private static void createAd(Publisher p, String topicName, String format) {
        Topic t = new Topic(topicName);
        IPublication.Format f = format.equals("XML") ? IPublication.Format.XML : IPublication.Format.JSON;
        p.advertise(t, f);
    }

    private static void publishJson(Publisher p, String topicName) {
        p.publish(topicName, " { \"hello\":  \"world\"}", IPublication.Format.JSON);
    }

    private static void publishXml(Publisher p, String topicName) {
        p.publish(topicName, "<note>\n" +
                "<to>Tove</to>\n" +
                "<from>Jani</from>\n" +
                "<heading>Reminder</heading>\n" +
                "<body>Don't forget me this weekend!</body>\n" +
                "</note>", IPublication.Format.XML);
    }

    private static void removeAd(Publisher p, String topicName) {
        Topic t = new Topic(topicName);
        IPublication.Format f = IPublication.Format.JSON;
        p.unadvertise(t, f);
    }

    private static void subscribeTopicJson(Subscriber s, String topicName) {
        Topic t = new Topic(topicName);
        IPublication.Format f = IPublication.Format.JSON;
        s.subscribe(t, f);
    }

    private static void subscribeTopicXml(Subscriber s, String topicName) {
        Topic t = new Topic(topicName);
        IPublication.Format f = IPublication.Format.XML;
        s.subscribe(t, f);
    }

    private static void unsubscribeTopicJson(Subscriber s, String topicName) {
        Topic t = new Topic(topicName);
        IPublication.Format f = IPublication.Format.JSON;
        s.unsubscribe(t, f);
    }

    private static void unsubscribeTopicXml(Subscriber s, String topicName) {
        Topic t = new Topic(topicName);
        IPublication.Format f = IPublication.Format.XML;
        s.unsubscribe(t, f);
    }



    public static void createSubs(String host, int clientPort, int brokerPort) throws IOException {

        s1 = new Subscriber(++ID_CLIENT, ++clientPort, brokerPort, host);
        s2 = new Subscriber(++ID_CLIENT, ++clientPort, brokerPort, host);
        s3 = new Subscriber(++ID_CLIENT, ++clientPort, brokerPort, host);
        s4 = new Subscriber(++ID_CLIENT, ++clientPort, brokerPort, host);
        s5 = new Subscriber(++ID_CLIENT, ++clientPort, brokerPort, host);

    }


    public static void createPubs(String host, int clientPort, int brokerPort) throws IOException {

        p1 = new Publisher(++ID_CLIENT, ++clientPort, brokerPort, host);
        p2 = new Publisher(++ID_CLIENT, ++clientPort, brokerPort, host);
        p3 = new Publisher(++ID_CLIENT, ++clientPort, brokerPort, host);
        p4 = new Publisher(++ID_CLIENT, ++clientPort, brokerPort, host);
        p5 = new Publisher(++ID_CLIENT, ++clientPort, brokerPort, host);

    }

    public static void connectSubs() throws IOException {
        s1.connect();
        s2.connect();
        s3.connect();
        s4.connect();
        s5.connect();
    }

    public static void connectPubs() throws IOException {
        p1.connect();
        p2.connect();
        p3.connect();
        p4.connect();
        p5.connect();
    }


    public static void createAds() {
        createAd(p1, "weather", "XML");
        createAd(p2, "weather/montreal");
        createAd(p3,"weather/montreal/humidity", "XML");
        createAd(p4,"sport/montreal","XML");
        createAd(p5,"sport");
    }


    public static void subscribeToTopics() {
        subscribeTopicJson(s1, "weather/*");
        subscribeTopicXml(s2, "weather/montreal");
        subscribeTopicJson(s3,"weather/montreal/humidity");
        subscribeTopicJson(s4,"sport/#");
        subscribeTopicXml(s5,"sport");
    }

    public static void unsubscribeToTopics() {
        unsubscribeTopicJson(s1, "weather/*");
        unsubscribeTopicXml(s2, "weather/montreal");
        unsubscribeTopicJson(s3,"weather/montreal/humidity");
        unsubscribeTopicJson(s4,"sport/#");
        unsubscribeTopicXml(s5,"sport");
    }

    public static void publishToTopics() {
        publishXml(p1, "weather/laval");
        publishJson(p2, "weather/montreal");
        publishXml(p3,"weather/montreal/humidity");
        publishXml(p4,"sport/volley");
        publishJson(p5,"sport");
    }

    public static void disconnectSubs() throws IOException {
        s1.disconnect();
        s2.disconnect();
        s3.disconnect();
        s4.disconnect();
        s5.disconnect();
    }

    public static void disconnectPubs() throws IOException {
        p1.disconnect();
        p2.disconnect();
        p3.disconnect();
        p4.disconnect();
        p5.disconnect();
    }
}
