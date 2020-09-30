package com.example.gitdemo.bean;

/*
"cloud":"91",
"cond_code":"101",
"cond_txt":"小雨",
"fl":"31",
"hum":"72",
"pcpn":"0.0",
"pres":"1010",
"tmp":"28",
"vis":"16",
"wind_deg":"301",
"wind_dir":"西北风",
"wind_sc":"2",
"wind_spd":"7",
"cond":{
"code":"101",
"txt":"多云"
}
 */
public class CurrentWeatherData {
    String cloud;
    String cond_code;
    String cond_txt;
    String fl;
    String hum;
    String pcpn;
    String pres;
    String tmp;
    String vis;
    String wind_deg;
    String wind_dir;
    String wind_sc;
    String wind_spd;

    Cond cond;

    public String getCloud() {
        return cloud;
    }

    public String getCond_code() {
        return cond_code;
    }

    public String getCond_txt() {
        return cond_txt;
    }

    public String getFl() {
        return fl;
    }

    public String getHum() {
        return hum;
    }

    public String getPcpn() {
        return pcpn;
    }

    public String getPres() {
        return pres;
    }

    public String getTmp() {
        return tmp;
    }

    public String getVis() {
        return vis;
    }

    public String getWind_deg() {
        return wind_deg;
    }

    public String getWind_dir() {
        return wind_dir;
    }

    public String getWind_sc() {
        return wind_sc;
    }

    public String getWind_spd() {
        return wind_spd;
    }

    public Cond getCond() {
        return cond;
    }

    public String getCondCode() {
        return cond.code;
    }

    public String getCondTxt() {
        return cond.txt;
    }

    private class Cond {
        String code;
        String txt;
    }


}
