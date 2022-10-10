package classes;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import interfaces.*;
import main.App;

import java.io.IOException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author AP57630
 */
public class Broker implements IBroker,Runnable{


    private final int PORT = 8022;

    private List<Topic> topics;
    private long id;
    private ServerSocket serverSocket;
    private boolean isRunning;
    private Thread thread;
    private App app;
    // private Map<Topic, HashMap<String,String>> subscribersTopicMap = new HashMap<Topic, HashMap<Subscriber>>();
    private Map<Long,Socket> mapClient;

    private long idTracker;

    public Broker(App app) {

        this.app 		= app;
        this.isRunning 	= false;

        this.idTracker = 0;
        this.mapClient = new HashMap<Long,Socket>();

        try {

            this.serverSocket = new ServerSocket(PORT);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void listenToNetwork() throws IOException{

        Socket clientSocket = this.serverSocket.accept();
        this.incrementID();
        this.mapClient.put(this.idTracker, clientSocket);
        BrokerThread bt = new BrokerThread(clientSocket,this.topics,this.app);
        bt.start();


    }

    public void incrementID() {
        this.idTracker++;
    }








    public void run() {

        this.app.updateLog("Server is listening on port " + PORT);

        while(this.isRunning){

            try {

                this.listenToNetwork();

            } catch (IOException e) {

                e.printStackTrace();
            }
        }

        try {

            if(!this.serverSocket.isClosed()) {
                this.serverSocket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        this.stop();
    }

    public void closeServerSocket() {

        try {
            this.serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        this.app.updateLog("Server stopped");
    }

}
