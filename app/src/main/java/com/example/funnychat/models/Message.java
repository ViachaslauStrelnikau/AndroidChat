package com.example.funnychat.models;

public class Message {

    private int id;
    private int ownUserId;
    private int channelId;
    private int rcpUserId;
    private long time;
    private String text;

    public Message(int id, int ownUserId, int channelId, int rcpUserId, long time, String text) {
        this.id = id;
        this.ownUserId = ownUserId;
        this.channelId = channelId;
        this.rcpUserId = rcpUserId;
        this.time = time;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwnUserId() {
        return ownUserId;
    }

    public void setOwnUserId(int ownUserId) {
        this.ownUserId = ownUserId;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public int getRcpUserId() {
        return rcpUserId;
    }

    public void setRcpUserId(int rcpUserId) {
        this.rcpUserId = rcpUserId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
