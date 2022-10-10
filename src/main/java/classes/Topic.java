package classes;


import interfaces.IPublisher;
import interfaces.ISubscriber;
import interfaces.ITopic;
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
    XML, JSON;
}
public class Topic implements ITopic{
    private String name =""; // le topic souhaite
    private List pub, sub;

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
    public List getPub() {
        return pub;
    }

    @Override
    public List getSub() {
        return sub;
    }

    public void addPub(Publisher p) {
        this.pub.add(p);
    }

    public void addSub(Subscriber s) {
        this.sub.add(s);
    }

    public boolean removePub(Publisher p) {
        return this.pub.remove(p);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj != null && obj instanceof Topic) {
            Topic t = (Topic) obj;
            return this.name.equals(t.getName());
        }
        return false;

    }
}
