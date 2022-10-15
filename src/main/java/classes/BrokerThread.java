package classes;

import interfaces.ISubscriber;
import main.App;
import org.json.JSONObject;
import org.json.JSONTokener;

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

		//le client est ici

		this.listenToPublisher();
			/*

		try {
			OutputStream oos = socket.getOutputStream();
			PrintWriter writer = new PrintWriter(oos, true);
			writer.println("This is a message sent to the client");


			InputStream input = null;
			BufferedReader reader 	= null;
			String message 			="";



			
		} catch (IOException e) {
			e.printStackTrace();
		}
				*/
				
			

			
			
    	//this.subscribe(null, null);
    	//this.unsubscribe(null, null);
    	//this.notifySubscribers();
			
			
			
			
	}
	
	
    private int onSubscribe(String t, Subscriber s) {
    	
    	//-- on check si le topic existe ou pas
    	if(!this.topics.contains(t)) return ERROR;
		//TODO: Chercher les bons topics dans la list et ajouter le subscriber aux topics trouvés
		return SUCCESS;

    	//Topic topic = this.topics.g
    	
    } 
    
    
    private void unsubscribe (Topic topic, Subscriber subscriber) {
		//TODO: Chercher les bons topics dans la list et enlever le subscriber aux topics trouvés
    }

	private int advertise (Topic topic, Publisher publisher) {
		if (this.topics.contains(topic)) {
			return ERROR;
		}
		topic.addPub(publisher);
		this.topics.add(topic);
		System.out.println("advertise!");
		return SUCCESS;
	}

	private int unadvertise (Topic topicToFind, Publisher publisher) {
		Topic topic = this.topics.stream().filter(t -> t.equals(topicToFind)).findFirst().orElse(null);
		if (topic == null) { return ERROR; }

		int isRemoved = topic.removePub(publisher) ? SUCCESS: ERROR;
		if(topic.getPub().isEmpty() && topic.getSub().isEmpty()) {
			this.topics.remove(topic);
		}

		return isRemoved;
	}

    private void notifySubscribers(Topic topicToFind, String content, String format) throws IOException {
    	//TODO: Chercher les bons topics dans la list et envoyer aux bon subscriber avec le bon FORMAT
    	//boucle sur les sub


		Topic topic = this.topics.stream().filter(t -> t.equals(topicToFind)).findFirst().orElse(null);
		if (topic == null) { return; }

		JSONObject jsonMessage =  (JSONObject) new JSONTokener(content).nextValue();
		Publication p = new Publication(topic, jsonMessage);


		for (ISubscriber sub: topic.getSub()) {
			String message = p.fromCanonicaltoJSON().toString();

			Socket socketClient = ((Subscriber) sub).getSocket();
			ObjectOutputStream output = new ObjectOutputStream(socketClient.getOutputStream());
			output.writeObject(message);

		}
    	/*
    	 * 
    	 * 
    	 * 
    	 * 
    	 * 
    	 * */
    }

	
	
	
	
	private void listenToPublisher() {
		Boolean isListening = true;
		ObjectInputStream  input = null;
		Request req = null;
		try {
			input 	= new ObjectInputStream((this.socket.getInputStream())) ;
			req = (Request)input.readObject();
			int response = this.handleRequest(req);
			ObjectOutputStream output = new ObjectOutputStream(this.socket.getOutputStream());
			input.close();
			output.close();
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
			String format = req.getFormat();
			Topic t = new Topic(topicName);
			Publisher p = new Publisher(req.getSenderId());
			response= this.advertise(t,p);
			this.app.updateLog("Publisher #" + p.getId()  + " has advertised: "+ topicName + " | " + format);

		} else if ("PUBLISH".equals(action)) {
			//String topicName = reader.readLine();

			String topicName = req.getContent();
			String format = req.getFormat();
			Topic t = new Topic(topicName);
			Publisher p = new Publisher(req.getSenderId());
			this.notifySubscribers(t, req.getContent(), format);

			this.app.updateLog("Publisher #" + p.getId()  + " has published: "+ topicName + " | " + format);
		} else if ("UNADVERTISE".equals(action)) {
			String topicName = req.getContent();
			String format = req.getFormat();
			Topic t = new Topic(topicName);
			Publisher p = new Publisher(req.getSenderId());

			response = this.unadvertise(t, p);
			this.app.updateLog("Publisher #" + p.getId()  + " has unadvertised: "+ topicName + " | " + format);

		} else {
			action = "";
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
