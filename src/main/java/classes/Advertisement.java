package classes;

import interfaces.IPublication;
import interfaces.IPublisher;

import java.util.Objects;

/**
 * Annonce qui permet à un publisher de publier dans un format spécifié
 */
public class Advertisement {

    private IPublisher publisher;
    private IPublication.Format format;

    /**
     *
     * @param publisher Celui qui publie des messages
     * @param format Le format dans lequel le message source est envoyé au Broker
     */
    public Advertisement(IPublisher publisher, IPublication.Format format) {
        this.publisher = publisher;
        this.format = format;
    }

    public IPublisher getPublisher() {
        return publisher;
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
        if (!(o instanceof Advertisement)) return false;
        Advertisement that = (Advertisement) o;
        return getPublisher().equals(that.getPublisher()) && getFormat() == that.getFormat();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPublisher(), getFormat());
    }
}
