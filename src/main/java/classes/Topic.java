package classes;


import interfaces.IPublication;
import interfaces.IPublisher;
import interfaces.ISubscriber;
import interfaces.ITopic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author AP57630
 */
enum Format {
    XML, JSON;
}

public class Topic implements ITopic, Serializable {
    private String name = ""; // le topic souhaite
    private List<IPublisher> pub;
    private List<ISubscriber> sub;

    private List<Subscription> subscriptions;

    public Topic(String name) {
        this.name = name;
        this.pub = new ArrayList<IPublisher>();
        this.sub = new ArrayList<ISubscriber>();
        this.subscriptions = new ArrayList<Subscription>();
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
        var subscriberList = new ArrayList<ISubscriber>();
        for (Subscription s : this.subscriptions) {
            subscriberList.add(s.getSubscriber());
        }
        return subscriberList;
//        return this.subscriptions.stream().map(s -> s.getSubscriber()).toList();
    }

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void addPub(Publisher p) {
        this.pub.add(p);
    }

    public void addSub(Subscriber s) {
        this.sub.add(s);
    }

    public void addSubscription(Subscription s) {
        this.subscriptions.add(s);
    }

    public void addSubscription(ISubscriber s, IPublication.Format format) {
        Subscription newSubscription = new Subscription(s, format);
        this.subscriptions.add(newSubscription);
    }

    public void removeSubscription(Subscription s) {
        this.subscriptions.remove(s);
    }

    public void removeSubscription(ISubscriber s, IPublication.Format format) {
        Subscription newSubscription = new Subscription(s, format);
        this.subscriptions.remove(newSubscription);
    }

    public boolean removePub(IPublisher p) {
        return this.pub.remove(p);
    }

    public boolean removeSub(ISubscriber subscriber) {
        return this.sub.remove(subscriber);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Topic) {
            Topic t = (Topic) obj;
            return this.name.equals(t.getName());
        }
        return false;

    }


}
