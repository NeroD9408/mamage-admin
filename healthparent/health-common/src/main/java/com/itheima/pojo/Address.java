package com.itheima.pojo;

import java.io.Serializable;
import java.util.List;

public class Address implements Serializable {
    private Integer id;//主键
    private String name;//机构名称
    private String helpCode;//助记码
    private String location;//机构具体位置
    private String telephone;//机构电话
    private String openTime;//营业时间
    private String longitude;//经度
    private String latitude;//纬度
    private String remark;//备注
    private String img;//图片名称
    private String status;//状态码,正常运营表示还在运营中,可正常预约,暂停运营表示,暂时不可预约该机构
    private List<Setmeal> setMeals;//表示该医疗机构关联的套餐信息

    public List<Setmeal> getSetMeals() {
        return setMeals;
    }

    public void setSetMeals(List<Setmeal> setMeals) {
        this.setMeals = setMeals;
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

    public String getHelpCode() {
        return helpCode;
    }

    public void setHelpCode(String helpCode) {
        this.helpCode = helpCode;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
