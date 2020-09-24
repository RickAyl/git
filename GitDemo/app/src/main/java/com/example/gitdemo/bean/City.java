package com.example.gitdemo.bean;

import com.google.gson.annotations.SerializedName;

public class City {

    private int _id;

    @SerializedName("name")
    private String cityName;
    @SerializedName("id")
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
