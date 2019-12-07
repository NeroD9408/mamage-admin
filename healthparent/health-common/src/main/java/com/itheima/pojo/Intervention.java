package com.itheima.pojo;

import java.io.Serializable;

public class Intervention implements Serializable{
    private Integer id;
    private String medicineOne;
    private String medicineTwo;
    private String sportOne;
    private String sportTwo;
    private String foodOne;
    private String foodTwo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMedicineOne() {
        return medicineOne;
    }

    public void setMedicineOne(String medicineOne) {
        this.medicineOne = medicineOne;
    }

    public String getMedicineTwo() {
        return medicineTwo;
    }

    public void setMedicineTwo(String medicineTwo) {
        this.medicineTwo = medicineTwo;
    }

    public String getSportOne() {
        return sportOne;
    }

    public void setSportOne(String sportOne) {
        this.sportOne = sportOne;
    }

    public String getSportTwo() {
        return sportTwo;
    }

    public void setSportTwo(String sportTwo) {
        this.sportTwo = sportTwo;
    }

    public String getFoodOne() {
        return foodOne;
    }

    public void setFoodOne(String foodOne) {
        this.foodOne = foodOne;
    }

    public String getFoodTwo() {
        return foodTwo;
    }

    public void setFoodTwo(String foodTwo) {
        this.foodTwo = foodTwo;
    }
}
