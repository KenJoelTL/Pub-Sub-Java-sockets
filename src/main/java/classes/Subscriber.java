/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import Interfaces.IPublication;
import Interfaces.ISubscriber;
import Interfaces.ITopic;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author AP57630
 */
public class Subscriber extends Client implements ISubscriber {
    
    
       @Override
    public void subscribe(ITopic t, IPublication.Format format) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void unsubscribe(ITopic t, IPublication.Format format) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void listentoBroker() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    


}