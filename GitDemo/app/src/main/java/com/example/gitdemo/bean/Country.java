package com.example.gitdemo.bean;

import com.google.gson.annotations.SerializedName;

public class Country {

    private int _id;

    @SerializedName("name")
    private String countryName;
    @SerializedName("id")
    private int countryCode;

    @SerializedName("weather_id")
    private String weatherId;

    private int cityId;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(int countryCode) {
        this.countryCode = countryCode;
    }
}
