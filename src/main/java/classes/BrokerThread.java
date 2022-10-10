package classes;

import main.App;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
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
		
		try {
			OutputStream oos = socket.getOutputStream();			
			PrintWriter writer = new PrintWriter(oos, true);
			writer.println("This is a message sent to the client");

			
		} catch (IOException e) {
			e.printStackTrace();
		}
				
				
			
			
			
			
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
		return SUCCESS;
	}

	private int unadvertise (Topic topic, Publisher publisher) {
		if (this.topics.contains(topic.getName())) {
			return ERROR;
		}

		int isRemoved = topic.removePub(publisher) ? SUCCESS: ERROR;
		if(topic.getPub().isEmpty() && topic.getSub().isEmpty()) {
			this.topics.remove(topic);
		}

		return isRemoved;
	}

    private void notifySubscribers() {
    	//TODO: Chercher les bons topics dans la list et envoyer aux bon subscriber avec le bon FORMAT
    	//boucle sur les sub
    	/*
    	 * 
    	 * 
    	 * 
    	 * 
    	 * 
    	 * */
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
