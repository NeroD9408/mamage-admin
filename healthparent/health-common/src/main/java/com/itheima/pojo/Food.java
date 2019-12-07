package com.itheima.pojo;

import java.io.Serializable;

public class Food implements Serializable {
    //序号
    private Integer id;
    //种类
    private Integer type;
    //食物名称
    private String name;
    //含水量
    private Integer kj;
    //蛋白质含量
    private Float protein;
    //脂肪含量
    private Float fat;
    //食用尺寸
    private String unit;

    public Food() {
    }

    public Food(Integer id, Integer type, String name, Integer kj, Float protein, Float fat, String unit) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.kj = kj;
        this.protein = protein;
        this.fat = fat;
        this.unit = unit;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getKj() {
        return kj;
    }

    public void setKj(Integer kj) {
        this.kj = kj;
    }

    public Float getProtein() {
        return protein;
    }

    public void setProtein(Float protein) {
        this.protein = protein;
    }

    public Float getFat() {
        return fat;
    }

    public void setFat(Float fat) {
        this.fat = fat;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
