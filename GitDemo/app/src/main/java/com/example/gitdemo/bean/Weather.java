package com.example.gitdemo.bean;

import com.google.gson.annotations.SerializedName;

public class Weather {

    @SerializedName("basic")
    private BasicData basicData;
    @SerializedName("update")
    private UpdateTime updateTime;

    private String status;
    @SerializedName("now")
    private CurrentWeatherData currentWeatherData;
    @SerializedName("daily_forecast")
    private DailyForecast dailyForecasts[];
    @SerializedName("aqi")
    private Aqi aqi;
    @SerializedName("suggestion")
    private Suggestion suggestion;

    private String msg;

    public BasicData getBasicData() {
        return basicData;
    }

    public void setBasicData(BasicData basicData) {
        this.basicData = basicData;
    }

    public UpdateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(UpdateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public CurrentWeatherData getCurrentWeatherData() {
        return currentWeatherData;
    }

    public void setCurrentWeatherData(CurrentWeatherData currentWeatherData) {
        this.currentWeatherData = currentWeatherData;
    }

    public DailyForecast[] getDailyForecasts() {
        return dailyForecasts;
    }

    public void setDailyForecasts(DailyForecast[] dailyForecasts) {
        this.dailyForecasts = dailyForecasts;
    }

    public Aqi getAqi() {
        return aqi;
    }

    public void setAqi(Aqi aqi) {
        this.aqi = aqi;
    }

    public Suggestion getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(Suggestion suggestion) {
        this.suggestion = suggestion;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
