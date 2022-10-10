package Classes;


import Interfaces.IPublisher;
import Interfaces.ISubscriber;
import Interfaces.ITopic;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author AP57630
 */
enum Format{
    XML, JSON
}
public class Topic implements ITopic{
    private String name;
    private List<IPublisher> pub;
    private List<ISubscriber>sub;

    public Topic(String name) {
        this.name = name;
    }

    public Topic(String name, List<IPublisher> pub, List<ISubscriber> sub) {
        this.name = name;
        this.pub = pub;
        this.sub = sub;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<IPublisher> getPub() {
        return pub;
    }

    @Override
    public List<ISubscriber> getSub() {
        return sub;
    }
    
    
    
}
