package classes;


import interfaces.IPublication;
import interfaces.IPublisher;
import interfaces.ISubscriber;
import interfaces.ITopic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private String name; // le topic souhaite
    private List<Advertisement> ads;
    private List<Subscription> subscriptions;

    public Topic(String name) {
        this.name = name;
        this.ads = new ArrayList<Advertisement>();
        this.subscriptions = new ArrayList<Subscription>();
    }

    @Override
    public String getName() {
        return name;
    }


    @Override
    public List<IPublisher> getPub() {
        var publisherList = new ArrayList<IPublisher>();
        for (Advertisement ad : this.ads) {
            publisherList.add(ad.getPublisher());
        }
        return publisherList;
//        return this.ads.stream().map(s -> s.getPublisher()).toList();
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

    public void addAdvertisement(Advertisement ad) {
        this.ads.add(ad);
    }

    public void addAdvertisement(IPublisher p, IPublication.Format format) {
        Advertisement newAd = new Advertisement(p, format);
        this.ads.add(newAd);
    }


    public boolean removeAdvertisement(Advertisement ad) {
        return this.ads.remove(ad);
    }

    public void addSubscription(Subscription s) {
        this.subscriptions.add(s);
    }

    public void addSubscription(ISubscriber s, IPublication.Format format) {
        Subscription newSubscription = new Subscription(s, format);
        this.subscriptions.add(newSubscription);
    }

    public boolean removeSubscription(Subscription s) {
        return this.subscriptions.remove(s);
    }

    public boolean removeSubscription(ISubscriber s, IPublication.Format format) {
        Subscription newSubscription = new Subscription(s, format);
        return this.subscriptions.remove(newSubscription);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Topic)) return false;
        Topic topic = (Topic) o;
        return getName().equals(topic.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
