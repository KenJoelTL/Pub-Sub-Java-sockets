package classes;

import interfaces.IPublication;
import interfaces.ISubscriber;
import main.App;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.*;
import java.net.Socket;
import java.util.List;


public class BrokerThread implements Runnable {

	private final int 	ERROR	= -1;
	private final int 	SUCCESS = 1;
	
	
    private Socket 	socket;
    private boolean isRunning;
    private Thread 	thread;
    private List<Topic> topics;
    private App app;
    
    
		public BrokerThread(Socket s, List<Topic> t, App p) {

			this.isRunning = false;
			this.socket = s;
			this.topics = t;
			this.app = p;
			this.app.updateLog("New client connected");

		}
	
	
	
		public void run() {
			this.listenToRequests();
		}
	
	
    private int onSubscribe(String topicName, Subscriber subscriber) {
			Topic topic = this.topics.stream().filter(t -> t.getName().equals(topicName)).findFirst().orElse(null);
			if (topic == null) { return ERROR; }
			subscriber.setSubscriberSocket(this.socket);
			topic.addSub(subscriber);
			return SUCCESS;
    } 

    private int unsubscribe(String topicName, Subscriber subscriber) {
			Topic topic = this.topics.stream().filter(t -> t.getName().equals(topicName)).findFirst().orElse(null);
			if (topic == null) { return ERROR; }
			topic.removeSub(subscriber);
			return SUCCESS;
    }

		private int advertise (String topicName, Publisher publisher) {
			Topic topic = this.topics.stream().filter(t -> t.getName().equals(topicName)).findFirst().orElse(new Topic(topicName));
			topic.addPub(publisher);
			this.topics.add(topic);
			System.out.println("advertise!");
			return SUCCESS;
		}

		private int unadvertise (String topicName, Publisher publisher) {
			Topic topic = this.topics.stream().filter(t -> t.getName().equals(topicName)).findFirst().orElse(null);
			if (topic == null) { return ERROR; }

			int isRemoved = topic.removePub(publisher) ? SUCCESS: ERROR;
			if(topic.getPub().isEmpty() && topic.getSub().isEmpty()) {
				this.topics.remove(topic);
			}

			return isRemoved;
		}

    private void notifySubscribers(String topicName, String content, String format) throws IOException {

			Topic topic = this.topics.stream().filter(t -> t.getName().equals(topicName)).findFirst().orElse(null);
			if (topic == null) { return; }
				for (ISubscriber sub: topic.getSub()) {
					Publication p = new Publication(topic, format, content);
					String message = "";
					try {
						if(sub.getFormat().equals(IPublication.Format.JSON)) {
							message = p.fromCanonicalToJSON();
						}
						else if(sub.getFormat().equals(IPublication.Format.XML)) {
							message = p.fromCanonicalToXML();
						}

						Socket socketClient = ((Subscriber) sub).getSocket();
						ObjectOutputStream output = new ObjectOutputStream(socketClient.getOutputStream());
						output.writeObject(message);

					} catch(Exception e) {
						e.printStackTrace();
					}
				}

    }

	private static Document convertStringToXMLDocument(String xmlString)
	{
		//Parser that produces DOM object trees from XML content
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		//API to obtain DOM Document instance
		DocumentBuilder builder = null;
		try
		{
			//Create DocumentBuilder with default configuration
			builder = factory.newDocumentBuilder();

			//Parse the content to Document object
			Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
			return doc;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

		private void listenToRequests() {
			Boolean isListening = true;
			ObjectInputStream  input = null;
			Request req = null;
			try {
				input 	= new ObjectInputStream((this.socket.getInputStream())) ;
				req = (Request)input.readObject();
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

				String topicName = req.getContent();
				Publisher p = new Publisher(req.getSenderId());
				response = this.advertise(topicName,p);

				this.socket.close();
				this.app.updateLog("Publisher #" + p.getId()  + " has advertised: "+ topicName);

			} else if ("PUBLISH".equals(action)) {

				String topicName = req.getTopic();
				String format = req.getFormat();
				Publisher p = new Publisher(req.getSenderId());
				this.notifySubscribers(topicName, req.getContent(), format);
				this.socket.close();
				this.app.updateLog("Publisher #" + p.getId()  + " has published: "+ topicName + " | " + format);

			} else if ("UNADVERTISE".equals(action)) {

				String topicName = req.getContent();
				Publisher p = new Publisher(req.getSenderId());
				response = this.unadvertise(topicName, p);
				this.app.updateLog("Publisher #" + p.getId()  + " has unadvertised: "+ topicName + " | ");
				this.socket.close();

			} else if("SUBSCRIBE".equals(action)) {

				String topicName = req.getContent();
				String format = req.getFormat();

				Subscriber subscriber = new Subscriber(req.getSenderId(), IPublication.Format.valueOf(format));
				response = this.onSubscribe(topicName, subscriber);

			} else if("UNSUBSCRIBE".equals(action)) {

				String topicName = req.getContent();
				String format = req.getFormat();
				Subscriber subscriber = new Subscriber(req.getSenderId(), IPublication.Format.valueOf(format));
				response = this.unsubscribe(topicName, subscriber);

			}

			return response;
		}

		//-- synchronized to work directly with the thread
		public synchronized void start(){

			if(this.isRunning){
				return;
			}

			this.isRunning = true;

			this.thread	= new Thread(this);
			this.thread.start();
		}

		public synchronized void stop(){

			if(!this.isRunning){
				return;
			}

			this.isRunning = false;

			try {
				this.thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
}
