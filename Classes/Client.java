/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import Interfaces.IClient;
import Interfaces.IPublication;
import Interfaces.ITopic;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;

/**
 *
 * @author AP57630
 */
public class Client implements IClient{

    private long id;
    private int port, brokerPort;
    private Socket socket;

    @Override
    public void connect(Socket socket) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

    }






}
