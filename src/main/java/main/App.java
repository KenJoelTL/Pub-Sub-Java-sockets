package main;

import model.Broker;
import vue.Frame;
import vue.MainPanel;


public class App implements Runnable{

	private final int WIDTH = 500;
	private final int HEIGHT = 400;
  
    private boolean 	isRunning;
    private Thread 		thread;
	private Frame		appFrame;
	private MainPanel	mainPanel;
	private Broker broker;
	private String log;
	
    public App() {

    	this.log = "";
    	this.appFrame 	= new Frame("LOG721-Broker",WIDTH,HEIGHT);
    	this.mainPanel 	= new MainPanel(WIDTH,HEIGHT,this);
    	this.appFrame.addToFrame(mainPanel);
    	this.isRunning 	= false;
    }
   
	public void run() {
		
		while(this.isRunning){}
		
		this.stop();	
	}
	
	
	public void updateLog(String s) {
		
		this.log += "  "+ s + "\n"; 
		this.updateView();
	}
	
	public void clearLog() {
		this.log =""; 
		this.updateView();
	}
	
	public void startServer() {
		this.broker = new Broker(this);
		this.broker.start();
		//this.log ="";
		
	}
	
	public void stopServer() {
		this.broker.closeServerSocket();
		this.broker.stop();
		this.broker = null;
	}
	
	public void updateView() {
		this.mainPanel.updateView(this.log);
	}
	
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
