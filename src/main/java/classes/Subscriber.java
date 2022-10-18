/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import interfaces.IPublication;
import interfaces.ISubscriber;
import interfaces.ITopic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Subscriber extends Client implements ISubscriber{

    private boolean isListening = false;

    public Subscriber(long id, int port, int brokerPort) {
        super(id,port,brokerPort);
    }

    public Subscriber(long id) {
        super(id);
    }

    @Override
    public void subscribe(ITopic t, IPublication.Format format) {
        try {
            Request req = new Request(this.getId(), "SUBSCRIBE", format.name(), t.getName());
            ObjectOutputStream output = new ObjectOutputStream(this.getSocket().getOutputStream());
            output.writeObject(req);
            //output.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void unsubscribe(ITopic t, IPublication.Format format) {
        try {
            Request req = new Request(this.getId(), "UNSUBSCRIBE", format.name(), t.getName());
            ObjectOutputStream output = new ObjectOutputStream(this.getSocket().getOutputStream());
            output.writeObject(req);
            //output.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void listentoBroker() {
        Thread listenerThread = new Thread() {
            public void run() {
                isListening = true;
                System.out.println("Started listening for incoming stream");
                while(isListening) {
                    ObjectInputStream  input = null;
                    try {
                        input 	= new ObjectInputStream((getSocket().getInputStream())) ;
                        String message = (String)input.readObject();
                        System.out.println(" ==================================== ");
                        System.out.println(" ");
                        System.out.println(message);
                        System.out.println(" ");
                        System.out.println(" ==================================== ");
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    getSocket().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        listenerThread.start();
    }

    public void stopListentoBroker() {
        this.isListening = false;
    }

    public void killListener() {
        isListening = false;
    }

    public void setSubscriberSocket(Socket s) {
        try {
            connect(s);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client)) return false;
        Client that = (Client) o;
        return this.getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return (int) (getId() ^ (getId() >>> 32));
    }
}
