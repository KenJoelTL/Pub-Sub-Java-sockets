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
    String host;

    private Socket socket;

    public Client(long id, int port, int brokerPort) {
        this.id = id;
        this.port = port;
        this.brokerPort = brokerPort;
        this.host = "127.0.0.1";
    }

    public Client(long id, int clientPort, int brokerPort, String host) {
        this.id = id;
        this.port = clientPort;
        this.brokerPort = brokerPort;
        this.host = host;
    }

    public Client(long id) {
        this.id = id;
    }

    public void connect() throws IOException {
        this.socket = new Socket(host, brokerPort);
    }

    @Override
    public void disconnect() throws IOException {
        this.socket.close();
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

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
