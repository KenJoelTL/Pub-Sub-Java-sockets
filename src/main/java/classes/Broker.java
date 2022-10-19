package classes;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import interfaces.*;
import main.App;

import java.io.IOException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author AP57630
 */
public class Broker implements IBroker, Runnable {


    private final int PORT = 8022;

    private List<Topic> topics;
    private long id;
    private ServerSocket serverSocket;
    private boolean isRunning;
    private Thread thread;
    private App app;

    public Broker(App app) {

        this.app = app;
        this.isRunning = false;

        this.topics = new ArrayList<Topic>();

        try {

            this.serverSocket = new ServerSocket(PORT);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void listenToNetwork() throws IOException {
        Socket clientSocket = this.serverSocket.accept();
        BrokerThread bt = new BrokerThread(clientSocket, this.topics, this.app);
        bt.start();
    }

    public void run() {

        this.app.updateLog("Server is listening on port " + PORT);

        while (this.isRunning) {

            try {

                this.listenToNetwork();

            } catch (IOException e) {

                e.printStackTrace();
            }
        }

        try {

            if (!this.serverSocket.isClosed()) {
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

        this.app.updateLog("Server stopped");
    }

}
