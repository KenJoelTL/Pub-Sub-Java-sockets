package model;

import interfaces.IPublication;
import interfaces.ISubscriber;

import java.util.Objects;

public class Subscription {
    private ISubscriber subscriber;
    private IPublication.Format format;

    public Subscription(ISubscriber subscriber, IPublication.Format format) {
        this.subscriber = subscriber;
        this.format = format;
    }

    public ISubscriber getSubscriber() {
        return subscriber;
    }

    public IPublication.Format getFormat() {
        return format;
    }

    public void setFormat(IPublication.Format format) {
        this.format = format;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subscription)) return false;
        Subscription that = (Subscription) o;
        return getSubscriber().equals(that.getSubscriber()) && getFormat() == that.getFormat();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSubscriber(), getFormat());
    }
}
