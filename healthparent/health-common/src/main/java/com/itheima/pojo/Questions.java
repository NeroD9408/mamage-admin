package com.itheima.pojo;

import java.io.Serializable;

public class Questions implements Serializable {
    private int id;
    private String question;
    private String type;
    private int status;
    private int togender;
    private int rev;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTogender() {
        return togender;
    }

    public void setTogender(int togender) {
        this.togender = togender;
    }

    public int getRev() {
        return rev;
    }

    public void setRev(int rev) {
        this.rev = rev;
    }
}
