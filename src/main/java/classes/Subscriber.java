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
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author AP57630
 */
public class Subscriber extends Client implements ISubscriber{

    private boolean isListening = false;

    public Subscriber(long id, int port, int brokerPort) {
        super(id,port,brokerPort);
    }
    @Override
    public void subscribe(ITopic t, IPublication.Format format) {

        //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void unsubscribe(ITopic t, IPublication.Format format) {

        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void listentoBroker() {

        this.isListening 		= true;
        InputStream input = null;
        BufferedReader reader 	= null;
        String message 			="";

        try {
            input 	= this.getSocket().getInputStream() ;
            reader 	= new BufferedReader(new InputStreamReader(input));
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        while(this.isListening) {

            try {

                message = reader.readLine();
                System.out.println(message);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            this.getSocket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopListentoBroker() {
        this.isListening = false;
    }

}
