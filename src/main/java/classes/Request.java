package classes;

import interfaces.IPublication;

import java.io.Serializable;

public class Request implements Serializable {
    private long senderClientId;
    private String action;
    private String format;
    private String content = "";
    private String topic;

    public Request(long senderClientId, String action, String format, String topic, String content) {
        this.senderClientId = senderClientId;
        this.action = action;
        this.format = format;
        this.topic = topic;
        this.content = content;
    }

    public Request(long senderClientId, String action, String format, String topic) {
        this.senderClientId = senderClientId;
        this.action = action;
        this.format = format;
        this.topic = topic;
    }

    public long getSenderClientId() {
        return senderClientId;
    }

    public void setSenderClientId(long senderClientId) {
        this.senderClientId = senderClientId;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
