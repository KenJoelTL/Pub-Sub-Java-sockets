/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import interfaces.IClient;

import java.io.IOException;
import java.net.Socket;

/**
 * @author AP57630
 */
public class Client implements IClient {

    private long id;
    private int port, brokerPort;
    private Socket socket;

    public Client(long id, int port, int brokerPort) {
        this.id = id;
        this.port = port;
        this.brokerPort = brokerPort;
    }

    public Client(long id) {
        this.id = id;
    }

    @Override
    public void connect(Socket socket) throws IOException {
        this.socket = socket;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPort() {
        return port;
    }

    public int getBrokerPort() {
        return brokerPort;
    }

    public Socket getSocket() {
        return socket;
    }


}
