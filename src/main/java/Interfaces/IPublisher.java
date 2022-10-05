/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

/**
 *
 * @author AP57630
 */
public interface IPublisher{
    public void advertise(ITopic t, IPublication.Format format);
    public void publish(ITopic t, IPublication p);
    public void unadvertise(ITopic t, IPublication.Format format);
}
