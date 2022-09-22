package Classes;


import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import Interfaces.*;
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
public class Broker implements IBroker{
    private long id;
    private int port;
    private List<Topic> topics;
    private ServerSocket socket;



    @Override
    public void listenToNetwork() throws IOException{

    }


}
