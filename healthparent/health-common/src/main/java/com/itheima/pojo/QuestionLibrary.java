package com.itheima.pojo;

import java.io.Serializable;

public class QuestionLibrary implements Serializable {

    private Integer id;
    private String name;
    private String type;
    private String peopleArea;
    private String helpCode;
    private Integer status;
    private Integer togender;
    private String desc;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPeopleArea() {
        return peopleArea;
    }

    public void setPeopleArea(String peopleArea) {
        this.peopleArea = peopleArea;
    }

    public String getHelpCode() {
        return helpCode;
    }

    public void setHelpCode(String helpCode) {
        this.helpCode = helpCode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getTogender() {
        return togender;
    }

    public void setTogender(Integer togender) {
        this.togender = togender;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
