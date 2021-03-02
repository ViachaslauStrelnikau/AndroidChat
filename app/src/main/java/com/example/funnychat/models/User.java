package com.example.funnychat.models;

public class User {

    private int id;
    private int channelId;
    private String name;
    private String sex;
    private int age;
    private boolean loggedIn = false;
    private boolean selected = false;

    public User(int id, int channelId, String name, String sex, int age) {
        this.id = id;
        this.channelId = channelId;
        this.name = name;
        this.sex = sex;
        this.age = age;
    }

    public User(int id, int channelId, String name, String sex, int age, boolean loggedIn) {
        this.id = id;
        this.channelId = channelId;
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.loggedIn = loggedIn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
