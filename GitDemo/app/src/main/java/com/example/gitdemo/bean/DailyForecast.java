package com.example.gitdemo.bean;

public class DailyForecast {


    private String date;

    private Cond cond;

    private Tmp tmp;

    public Cond getCond() {
        return cond;
    }

    public void setCond(Cond cond) {
        this.cond = cond;
    }

    public Tmp getTmp() {
        return tmp;
    }

    public void setTmp(Tmp tmp) {
        this.tmp = tmp;
    }

    private class Cond {
        String txt_d;
    }

    public String getCondStr() {
        return cond.txt_d;
    }

    private class Tmp {
        String max;
        String min;
    }

    public String getTmpMaxStr() {
        return tmp.max;
    }

    public String getTmpMinStr() {
        return tmp.min;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
