package com.example.gitdemo.bean;

public class City {

    private int _id;

    private String cityName;

    private int cityCode;

    private int provinceId;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityName() {
        return cityName;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }
}
