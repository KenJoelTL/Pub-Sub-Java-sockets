/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import interfaces.IPublication;
import interfaces.IPublisher;
import interfaces.ITopic;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author AP57630
 */
public class Publisher extends Client implements IPublisher{

    public Publisher(long id, int port, int brokerPort) {
        super(id, port, brokerPort);
    }

    public Publisher(long id) {
        super(id);
    }

    @Override
    public void advertise(ITopic t, IPublication.Format format) {

        try {
            Request req = new Request(this.getId(), "ADVERTISE", format.name(), t.getName() );
            ObjectOutputStream output = new ObjectOutputStream(this.getSocket().getOutputStream());
            output.writeObject(req);

            ObjectInputStream input = new ObjectInputStream(this.getSocket().getInputStream());
            String res = (String) input.readObject();
            System.out.println("Réponse du serveur: "+res);
            output.close();
            input.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void publish(ITopic t, IPublication p) {
        this.publish(t.getName(), p.fromCanonicaltoJSON().toString());
    }

    public void publish(String topicName, String content) { //format ?
        try {
            Request req = new Request(this.getId(), "PUBLISH", "JSON", topicName, content );
            ObjectOutputStream output = new ObjectOutputStream(this.getSocket().getOutputStream());
            output.writeObject(req);

            // Réponse du serveur
            ObjectInputStream input = new ObjectInputStream(this.getSocket().getInputStream());
            String res = (String) input.readObject();
            System.out.println("Réponse du serveur: "+res);
            output.close();
            input.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void unadvertise(ITopic t, IPublication.Format format){
        try {
            Request req = new Request(this.getId(), "UNADVERTISE", format.name(), t.getName(), format.name() );
            ObjectOutputStream output = new ObjectOutputStream(this.getSocket().getOutputStream());
            output.writeObject(req);

            //
            ObjectInputStream input = new ObjectInputStream(this.getSocket().getInputStream());
            String res = (String) input.readObject();
            System.out.println("Réponse du serveur: "+res);
            output.close();
            input.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(obj != null && obj instanceof Publisher) {
            Publisher p = (Publisher) obj;
            return this.getId() == p.getId();
        }
        return false;

    }
}
