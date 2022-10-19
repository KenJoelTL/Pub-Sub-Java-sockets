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
public interface IPublisher{
    public void advertise(ITopic t, IPublication.Format format);
    public void publish(String topicName, String content,IPublication.Format format);
    public void unadvertise(ITopic t, IPublication.Format format);
}
