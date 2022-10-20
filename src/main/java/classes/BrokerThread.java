package classes;

import interfaces.IPublication;
import main.App;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Thread gérant l'interaction entre un Client et un Broker
 */
public class BrokerThread implements Runnable {

    private final int ERROR = -1;
    private final int SUCCESS = 1;

    private Socket socket;
    private boolean isRunning;
    private Thread thread;
    private TopicRepository topicRepo;
    private App app;

    public BrokerThread(Socket clientSocket, TopicRepository topicRepository, App app) {

        this.isRunning = false;
        this.socket = clientSocket;
        this.topicRepo = topicRepository;
        this.app = app;
        this.app.updateLog("New client connected");

    }

    /**
     * Crée un abonnement du Client aux Topics spécifiés
     *
     * @param topicName    Identifiant du Topic
     * @param subscription Abonnement d'un Subscriber à un Topic
     * @return 1 s'il n'y a pas de problème, -1 dans le cas d'une anomalie
     */
    private int onSubscribe(String topicName, Subscription subscription) {
        Topic topic;
        List<Topic> topicList = this.topicRepo.findByName(topicName);
        if (topicList.isEmpty()) {
            topic = new Topic(topicName);
            this.topicRepo.add(topic);
        } else {
            topic = topicList.get(0);
        }
        ((Subscriber) subscription.getSubscriber()).setSocket(this.socket);
        topic.addSubscription(subscription);
        return SUCCESS;
    }

    /**
     * Annule l'abonnement du Client
     *
     * @param topicName    Identifiant du Topic
     * @param subscription Abonnement d'un Subscriber à un Topic
     * @return 1 s'il n'y a pas de problème, -1 dans le cas d'une anomalie
     */
    private int onUnsubscribe(String topicName, Subscription subscription) {
        List<Topic> topicList = this.topicRepo.findByName(topicName);
        if (topicList.isEmpty())
            return ERROR;

        Topic topic = topicList.get(0);
        topic.removeSubscription(subscription);
        return SUCCESS;
    }

    /**
     * Crée annonce de publication pour un Topic pour un format de message source
     * spécifié
     *
     * @param topicName Identifiant du Topic
     * @param ad        Annone de publication d'un publisher pour Topic
     * @return 1 s'il n'y a pas de problème, -1 dans le cas d'une anomalie
     */
    private int onAdvertise(String topicName, Advertisement ad) {
        Topic topic;
        List<Topic> topicList = this.topicRepo.findByName(topicName);
        if (topicList.isEmpty()) {
            topic = new Topic(topicName);
            this.topicRepo.add(topic);
        } else {
            topic = topicList.get(0);
        }
        topic.addAdvertisement(ad);
        return SUCCESS;
    }

    /**
     * Supprimer l'annonce de publication associée au Topic pour un format spécifié
     *
     * @param topicName Identifiant du Topic
     * @param ad        Annone de publication d'un publisher pour Topic
     * @return 1 s'il n'y a pas de problème, -1 dans le cas d'une anomalie
     */
    private int onUnadvertise(String topicName, Advertisement ad) {
        List<Topic> topicList = this.topicRepo.findByName(topicName);
        if (topicList.isEmpty())
            return ERROR;

        Topic topic = topicList.get(0);
        int isRemoved = topic.removeAdvertisement(ad) ? SUCCESS : ERROR;
        if (topic.getPub().isEmpty() && topic.getSub().isEmpty()) {
            this.topicRepo.remove(topic);
        }

        return isRemoved;
    }

    /**
     * Diffuse une publication donnée au Subscribers concernés
     *
     * @param topicName Identifiant du Topic
     * @param content   Contenu du message
     * @param format    Format du message
     */
    private void notifySubscribers(String topicName, String content, String format) {
        ArrayList<Topic> topicList = (ArrayList<Topic>) this.topicRepo.findByCorrespondingName(topicName); // List of
                                                                                                           // topics

        for (Topic topic : topicList) {
            for (Subscription subtion : topic.getSubscriptions()) {
                Publication p = new Publication(topic, format, content);
                String message = "";
                try {
                    if (subtion.getFormat() == IPublication.Format.JSON) {
                        message = p.fromCanonicalToJSON();
                    } else if (subtion.getFormat() == IPublication.Format.XML) {
                        message = p.fromCanonicalToXML();
                    }

                    Socket socketClient = ((Subscriber) subtion.getSubscriber()).getSocket();
                    ObjectOutputStream output = new ObjectOutputStream(socketClient.getOutputStream());
                    output.writeObject(message);

                    this.app.updateLog("Subscriber #" + ((Subscriber) subtion.getSubscriber()).getId()
                            + " got NOTIFIED about: " + topicName + " in " + format);
                    this.app.updateLog("***********");
                    this.app.updateLog("\tSubscriber #" + ((Subscriber) subtion.getSubscriber()).getId() + " RECEIVED\n"
                            + message);
                    this.app.updateLog("***********");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Écoute les requêtes des clients
     */
    private void listenToRequests() {
        Boolean isListening = true;
        ObjectInputStream input;
        Request req;
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

    /**
     * Achemine les requêtes envoyées par les clients en fonction de leur action
     *
     * @param req Requête d'un client
     * @return 1 s'il n'y a pas de problème, -1 dans le cas d'une anomalie
     * @throws IOException
     */
    private int handleRequest(Request req) throws IOException {

        int response = 0;
        String action = req.getAction();

        if ("ADVERTISE".equals(action)) {

            String topicName = req.getTopic();
            String format = req.getFormat();
            Publisher p = new Publisher(req.getSenderClientId());

            Advertisement ad = new Advertisement(p, IPublication.Format.valueOf(format));
            response = this.onAdvertise(topicName, ad);

            this.app.updateLog("Publisher #" + p.getId() + " has ADVERTISED: " + topicName + " | " + format);
            this.stop();

        } else if ("PUBLISH".equals(action)) {

            String topicName = req.getTopic();
            String format = req.getFormat();
            String content = req.getContent();
            Publisher p = new Publisher(req.getSenderClientId());
            this.app.updateLog("Publisher #" + p.getId() + " has PUBLISHED to: " + topicName + " | " + format);
            this.notifySubscribers(topicName, content, format);
            this.stop();
        } else if ("UNADVERTISE".equals(action)) {

            String topicName = req.getTopic();
            String format = req.getFormat();
            Publisher p = new Publisher(req.getSenderClientId());
            Advertisement ad = new Advertisement(p, IPublication.Format.valueOf(format));
            response = this.onUnadvertise(topicName, ad);

            this.app.updateLog("Publisher #" + p.getId() + " has UNADVERTISED: " + topicName + " | ");
            this.stop();

        } else if ("SUBSCRIBE".equals(action)) {

            String topicName = req.getTopic();
            String format = req.getFormat();
            Subscriber subscriber = new Subscriber(req.getSenderClientId());

            Subscription subscription = new Subscription(subscriber, IPublication.Format.valueOf(format));
            response = this.onSubscribe(topicName, subscription);
            this.app.updateLog(
                    "Subscriber #" + subscriber.getId() + " has SUBSCRIBED to: " + topicName + " | " + format);

        } else if ("UNSUBSCRIBE".equals(action)) {

            String topicName = req.getTopic();
            String format = req.getFormat();
            Subscriber subscriber = new Subscriber(req.getSenderClientId());

            Subscription subscription = new Subscription(subscriber, IPublication.Format.valueOf(format));
            response = this.onUnsubscribe(topicName, subscription);
            this.app.updateLog(
                    "Subscriber #" + subscriber.getId() + " has UNSUBSCRIBED to: " + topicName + " | " + format);

        }

        return response;
    }

    /**
     * Lance l'écoute de requêtes - implémentation de la méthode run
     */
    public void run() {
        this.listenToRequests();
    }

    // -- synchronized to work directly with the thread
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
            this.socket.close();
            this.thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
