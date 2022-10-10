/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import java.io.IOException;
import java.net.Socket;
import Interfaces.IPublication.Format;

/**
 *
 * @author AP57630
 */
public interface IClient {    
    public void connect(Socket socket) throws IOException;    
}
