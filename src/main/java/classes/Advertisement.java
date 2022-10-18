package classes;

import interfaces.IPublication;

public class Advertisement {
    private Publisher publisher;
    private IPublication.Format format;


    public Advertisement(Publisher publisher, IPublication.Format format) {
        this.publisher = publisher;
        this.format = format;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public IPublication.Format getFormat() {
        return format;
    }

    public void setFormat(IPublication.Format format) {
        this.format = format;
    }
}
