package classes;

import interfaces.IPublication;

import java.io.Serializable;

public class Request implements Serializable {
    private long senderId;
    private String action;
    private String format;
    private IPublication publication;
    private String content;
    private String topic = "";

    public Request(long senderId, String action, String format, String content, String topic) {
        this.senderId = senderId;
        this.action = action;
        this.format = format;
        this.content = content;
        this.topic = topic;
    }

    public Request(long senderId, String action, String format, String content) {
        this.senderId = senderId;
        this.action = action;
        this.format = format;
        this.content = content;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public IPublication getPublication() {
        return publication;
    }

    public void setPublication(IPublication publication) {
        this.publication = publication;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
