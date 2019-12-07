package com.itheima.pojo;

import java.io.Serializable;

public class Sport implements Serializable {
    //序号
    private Integer id;
    //编号
    private String number;
    //运动名称
    private String name;
    //运动风险
    private String risk;
    //运动种类
    private Integer sportType;
    //运动时间
    private String time;
    //运动频率
    private String frequency;
    //类型
    private Integer type;
    //强度
    private Integer strength;
    //MeT
    private Float met;
    //千步当量
    private Float equivalent;
    //千步当量(时间)
    private Float equivalent_time;
    //最小年龄
    private Integer minAge;
    //最大年龄
    private Integer maxAge;
    //运动部位
    private String position;
    //动作方法
    private String method;
    //是否启用
    private Integer state;
    //卡路里
    private Float calorie;
    //消耗能量
    private Float energy;

    public Sport() {
    }

    public Sport(Integer id, String number, String name, Integer sportType,String time, String frequency, String position, String method) {
        this.id = id;
        this.number = number;
        this.name = name;
        this.sportType=sportType;
        this.time = time;
        this.frequency = frequency;
        this.position = position;
        this.method = method;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

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

    public String getRisk() {
        return risk;
    }

    public void setRisk(String risk) {
        this.risk = risk;
    }

    public Integer getSportType() {
        return sportType;
    }

    public void setSportType(Integer sportType) {
        this.sportType = sportType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStrength() {
        return strength;
    }

    public void setStrength(Integer strength) {
        this.strength = strength;
    }

    public Float getMet() {
        return met;
    }

    public void setMet(Float met) {
        this.met = met;
    }

    public Float getEquivalent() {
        return equivalent;
    }

    public void setEquivalent(Float equivalent) {
        this.equivalent = equivalent;
    }

    public Float getEquivalent_time() {
        return equivalent_time;
    }

    public void setEquivalent_time(Float equivalent_time) {
        this.equivalent_time = equivalent_time;
    }

    public Integer getMinAge() {
        return minAge;
    }

    public void setMinAge(Integer minAge) {
        this.minAge = minAge;
    }

    public Integer getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(Integer maxAge) {
        this.maxAge = maxAge;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Float getCalorie() {
        return calorie;
    }

    public void setCalorie(Float calorie) {
        this.calorie = calorie;
    }

    public Float getEnergy() {
        return energy;
    }

    public void setEnergy(Float energy) {
        this.energy = energy;
    }
}
