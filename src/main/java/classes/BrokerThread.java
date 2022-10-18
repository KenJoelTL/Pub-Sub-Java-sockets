package classes;

import interfaces.IPublication;
import main.App;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.*;
import java.net.Socket;
import java.util.List;


public class BrokerThread implements Runnable {

    private final int ERROR = -1;
    private final int SUCCESS = 1;


    private Socket socket;
    private boolean isRunning;
    private Thread thread;
    private List<Topic> topics;
    private App app;


    public BrokerThread(Socket clientSocket, List<Topic> topicList, App app) {

        this.isRunning = false;
        this.socket = clientSocket;
        this.topics = topicList;
        this.app = app;
        this.app.updateLog("New client connected");

    }


    public void run() {
        this.listenToRequests();
    }


    private int onSubscribe(String topicName, Subscription subscription) {
        Topic topic = this.topics.stream().filter(t -> t.getName().equals(topicName)).findFirst().orElse(null);
        if (topic == null) return ERROR;

        ((Subscriber)subscription.getSubscriber()).setSubscriberSocket(this.socket);
        topic.addSubscription(subscription);
        return SUCCESS;
    }

    private int onUnsubscribe(String topicName, Subscription subscription) {
        Topic topic = this.topics.stream().filter(t -> t.getName().equals(topicName)).findFirst().orElse(null); //TopicManager.find(topicName) tree<Topic>
        if (topic == null) return ERROR;

        topic.removeSubscription(subscription);
        return SUCCESS;
    }

    private int onAdvertise(String topicName, Advertisement ad) {
        Topic topic = this.topics.stream().filter(t -> t.getName().equals(topicName)).findFirst().orElse(null);
        if (topic == null) {
            topic = new Topic(topicName);
            this.topics.add(topic);
        }
        topic.addAdvertisement(ad);
        return SUCCESS;
    }

    private int onUnadvertise(String topicName, Advertisement ad) {
        Topic topic = this.topics.stream().filter(t -> t.getName().equals(topicName)).findFirst().orElse(null);
        if (topic == null) return ERROR;

        int isRemoved = topic.removeAdvertisement(ad) ? SUCCESS : ERROR;
        if (topic.getPub().isEmpty() && topic.getSub().isEmpty()) {
            this.topics.remove(topic);
        }

        return isRemoved;
    }

    private void notifySubscribers(String topicName, String content, String format) throws IOException {
        var topicList = this.topics.stream().filter(t -> t.getName().equals(topicName)).toList(); //List of topics

        for (Topic topic : topicList) {
            for (Subscription subtion : topic.getSubscriptions()) {
                Publication p = new Publication(topic, format, content);
                String message = "";
                try {
                    if (subtion.getFormat().equals(IPublication.Format.JSON)) {
                        message = p.fromCanonicalToJSON();
                    } else if (subtion.getFormat().equals(IPublication.Format.XML)) {
                        message = p.fromCanonicalToXML();
                    }

                    Socket socketClient = ((Subscriber) subtion.getSubscriber()).getSocket();
                    ObjectOutputStream output = new ObjectOutputStream(socketClient.getOutputStream());
                    output.writeObject(message);

                    this.app.updateLog("Subscriber #" + ((Subscriber)subtion.getSubscriber()).getId() + " got NOTIFIED about: " + topicName + " in " + format);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static Document convertStringToXMLDocument(String xmlString) {
        //Parser that produces DOM object trees from XML content
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        //API to obtain DOM Document instance
        DocumentBuilder builder = null;
        try {
            //Create DocumentBuilder with default configuration
            builder = factory.newDocumentBuilder();

            //Parse the content to Document object
            Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void listenToRequests() {
        Boolean isListening = true;
        ObjectInputStream input = null;
        Request req = null;
        try {
            input = new ObjectInputStream((this.socket.getInputStream()));
            req = (Request) input.readObject();
            this.handleRequest(req);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private int handleRequest(Request req) throws IOException {

        int response = 0;
        String action = req.getAction();

        if ("ADVERTISE".equals(action)) {

            String topicName = req.getTopic();
            String format = req.getFormat();
            Publisher p = new Publisher(req.getSenderClientId());

            Advertisement ad = new Advertisement(p, IPublication.Format.valueOf(format));
            response = this.onAdvertise(topicName, ad);

            this.socket.close();
            this.app.updateLog("Publisher #" + p.getId() + " has ADVERTISED: " + topicName + " | " + format);

        } else if ("PUBLISH".equals(action)) {

            String topicName = req.getTopic();
            String format = req.getFormat();
            String content = req.getContent();
            Publisher p = new Publisher(req.getSenderClientId());
            this.notifySubscribers(topicName, content, format);
            this.socket.close();
            this.app.updateLog("Publisher #" + p.getId() + " has PUBLISHED to: " + topicName + " | " + format);

        } else if ("UNADVERTISE".equals(action)) {

            String topicName = req.getTopic();
            String format = req.getFormat();
            Publisher p = new Publisher(req.getSenderClientId());
            Advertisement ad = new Advertisement(p, IPublication.Format.valueOf(format));
            response = this.onUnadvertise(topicName, ad);
            this.app.updateLog("Publisher #" + p.getId() + " has UNADVERTISED: " + topicName + " | ");
            this.socket.close();

        } else if ("SUBSCRIBE".equals(action)) {

            String topicName = req.getTopic();
            String format = req.getFormat();
            Subscriber subscriber = new Subscriber(req.getSenderClientId());

            Subscription subscription = new Subscription(subscriber, IPublication.Format.valueOf(format));
            response = this.onSubscribe(topicName, subscription);
            this.app.updateLog("Subscriber #" + subscriber.getId() + " has SUBSCRIBED to: " + topicName + " | " + format);

        } else if ("UNSUBSCRIBE".equals(action)) {

            String topicName = req.getTopic();
            String format = req.getFormat();
            Subscriber subscriber = new Subscriber(req.getSenderClientId());

            Subscription subscription = new Subscription(subscriber, IPublication.Format.valueOf(format));
            response = this.onUnsubscribe(topicName, subscription);
            this.app.updateLog("Subscriber #" + subscriber.getId() + " has UNSUBSCRIBED to: " + topicName + " | " + format);

        }

        return response;
    }

    //-- synchronized to work directly with the thread
    public synchronized void start() {

        if (this.isRunning) {
            return;
        }

        this.isRunning = true;

        this.thread = new Thread(this);
        this.thread.start();
    }

    public synchronized void stop() {

        if (!this.isRunning) {
            return;
        }

        this.isRunning = false;
        this.app.updateLog("Client disconnected");

        try {
            this.thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
