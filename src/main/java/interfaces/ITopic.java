/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import classes.Subscriber;

import java.util.List;

/**
 * @author AP57630
 */


public interface ITopic {

    public String getName();

    public List<IPublisher> getPub();

    public List<ISubscriber> getSub();

}
