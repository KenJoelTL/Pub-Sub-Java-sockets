/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

/**
 *
 * @author AP57630
 */
public interface ISubscriber{
    public void subscribe(ITopic t, IPublication.Format format);
    public void unsubscribe(ITopic t, IPublication.Format format);
    public void listenToBroker();
}
