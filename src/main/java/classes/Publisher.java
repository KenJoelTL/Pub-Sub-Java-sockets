/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import interfaces.IPublication;
import interfaces.IPublisher;
import interfaces.ITopic;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author AP57630
 */
public class Publisher extends Client implements IPublisher{

    public Publisher(long id, int port, int brokerPort) {
        super(id, port, brokerPort);
    }

    @Override
    public void advertise(ITopic t, IPublication.Format format) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void publish(ITopic t, IPublication p) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void unadvertise(ITopic t, IPublication.Format format){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean equals(Object obj) {
        if(obj != null && obj instanceof Topic) {
            Publisher p = (Publisher) obj;
            return this.getId() == p.getId();
        }
        return false;

    }
}
