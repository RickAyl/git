package com.example.gitdemo.bean;

import com.google.gson.annotations.SerializedName;

public class Weather {

    @SerializedName("basic")
    private BasicData mBasic;
    @SerializedName("update")
    private UpdateTime mUpdate;

    private String status;
    @SerializedName("now")
    private CurrentWeatherData mNow;
    @SerializedName("daily_forecast")
    private DailyForecast mDailyForecast[];
    @SerializedName("aqi")
    private Aqi mAqi;
    @SerializedName("suggestion")
    private Suggestion mSuggestion;

    private String msg;

    public BasicData getmBasic() {
        return mBasic;
    }

    public void setmBasic(BasicData mBasic) {
        this.mBasic = mBasic;
    }

    public UpdateTime getmUpdate() {
        return mUpdate;
    }

    public void setmUpdate(UpdateTime mUpdate) {
        this.mUpdate = mUpdate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public CurrentWeatherData getmNow() {
        return mNow;
    }

    public void setmNow(CurrentWeatherData mNow) {
        this.mNow = mNow;
    }

    public DailyForecast[] getmDailyForecast() {
        return mDailyForecast;
    }

    public void setmDailyForecast(DailyForecast[] mDailyForecast) {
        this.mDailyForecast = mDailyForecast;
    }

    public Aqi getmAqi() {
        return mAqi;
    }

    public void setmAqi(Aqi mAqi) {
        this.mAqi = mAqi;
    }

    public Suggestion getmSuggestion() {
        return mSuggestion;
    }

    public void setmSuggestion(Suggestion mSuggestion) {
        this.mSuggestion = mSuggestion;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
