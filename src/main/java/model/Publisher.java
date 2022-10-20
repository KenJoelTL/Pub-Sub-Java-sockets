package model;

import interfaces.IPublication;
import interfaces.IPublisher;
import interfaces.ITopic;

import java.io.*;

/**
 * Client qui publie des messages sur un broker
 *
 * @author AP57630
 */
public class Publisher extends Client implements IPublisher {

    public Publisher(long id, int port, int brokerPort, String host) {
        super(id, port, brokerPort, host);
    }

    public Publisher(long id, int port, int brokerPort) {
        super(id, port, brokerPort);
    }

    public Publisher(long id) {
        super(id);
    }

    @Override
    public void advertise(ITopic t, IPublication.Format format) {
        try {
            Request req = new Request(this.getId(), "ADVERTISE", format.name(), t.getName());
            ObjectOutputStream output = new ObjectOutputStream(this.getSocket().getOutputStream());
            output.writeObject(req);
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void publish(String topicName, String content, IPublication.Format format) {
        try {
            System.out.println("PUBLISHING\n"+content);
            Request req = new Request(this.getId(), "PUBLISH", format.name(), topicName, content);
            ObjectOutputStream output = new ObjectOutputStream(this.getSocket().getOutputStream());
            output.writeObject(req);
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void unadvertise(ITopic t, IPublication.Format format) {
        try {
            Request req = new Request(this.getId(), "UNADVERTISE", format.name(), t.getName());
            ObjectOutputStream output = new ObjectOutputStream(this.getSocket().getOutputStream());
            output.writeObject(req);
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Publisher) {
            Publisher p = (Publisher) obj;
            return this.getId() == p.getId();
        }
        return false;
    }
}
