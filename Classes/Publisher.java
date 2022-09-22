/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import Interfaces.IPublication;
import Interfaces.IPublisher;
import Interfaces.ITopic;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author AP57630
 */
public class Publisher extends Client implements IPublisher{
    
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

    

    
}
