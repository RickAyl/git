package com.example.gitdemo.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import com.example.gitdemo.MainActivity;
import com.example.gitdemo.bean.BasicData;
import com.example.gitdemo.bean.City;
import com.example.gitdemo.bean.Country;
import com.example.gitdemo.bean.Province;
import com.example.gitdemo.bean.Suggestion;
import com.example.gitdemo.bean.Weather;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Utility {

    public static SQLiteDatabase createWriteableDB(Context context, SQLiteOpenHelper helper){
        return helper.getWritableDatabase();
    }

    public static boolean handleProviceResponse(String response, HandleCallBack callBack){
        Log.d(MainActivity.TAG,"response : " + response);
       if(!TextUtils.isEmpty(response)){
           Gson gson = new Gson();
           try {
               List<Province> provinces = gson.fromJson(response,new TypeToken<List<Province>>(){}.getType());
               for (int i = 0; i < provinces.size(); i++) {
                   Log.d(MainActivity.TAG,"省份" + provinces.get(i).getProvinceCode() + ":" + provinces.get(i).getProvinceName());
               }
               if (callBack != null){
                   callBack.onFinished(provinces);
               }
               return true;
           } catch (Exception e) {
               callBack.onError(e);
           }

       }
        return false;
    }


    public static boolean handleCityResponse(String response, HandleCallBack callBack, int provinceCode){
        Log.d(MainActivity.TAG,"city response : " + response);
        if(!TextUtils.isEmpty(response)){
            Gson gson = new Gson();
            try {
                List<City> cities = gson.fromJson(response,new TypeToken<List<City>>(){}.getType());
                for (int i = 0; i < cities.size(); i++) {
                    Log.d(MainActivity.TAG,"城市" + cities.get(i).getCityCode() + ":" + cities.get(i).getCityName());
                    cities.get(i).setProvinceId(provinceCode);
                }
                if (callBack != null){
                    callBack.onFinished(cities);
                }
                return true;
            } catch (Exception e) {
                callBack.onError(e);
            }

        }
        return false;
    }



    public static boolean handleCountryResponse(String response, HandleCallBack callBack, int cityCode){
        if(!TextUtils.isEmpty(response)){
            Gson gson = new Gson();
            try {
                List<Country> countries = gson.fromJson(response,new TypeToken<List<Country>>(){}.getType());
                for (int i = 0; i < countries.size(); i++) {
                    Log.d(MainActivity.TAG,"乡镇" + countries.get(i).getCountryCode() + ":" + countries.get(i).getCountryName());
                    countries.get(i).setCityId(cityCode);
                }
                if (callBack != null){
                    callBack.onFinished(countries);
                }
                return true;
            } catch (Exception e) {
                callBack.onError(e);
            }

        }
        return false;
    }
    /*
    {"HeWeather":[{"basic":{"cid":"CN101201402","location":"钟祥","parent_city":"荆门","admin_area":"湖北","cnty":"中国"
,"lat":"24.90885353","lon":"118.58942413","tz":"+8.00","city":"钟祥","id":"CN101201402","update":{"loc":"2020-09-25 17:35","utc":"2020-09-25 09:35"}},"update":{"loc":"20
20-09-25 17:35","utc":"2020-09-25 09:35"},"status":"ok","now":{"cloud":"92","cond_code":"104","cond_txt":"中雨","fl":"18","hum":"78","pcpn":"0.0","pres":"1010","tmp":"19
","vis":"16","wind_deg":"86","wind_dir":"东风","wind_sc":"3","wind_spd":"14","cond":{"code":"104","txt":"中雨"}},"daily_forecast":[{"date":"2020-09-26","cond":{"txt_d":"
小雨"},"tmp":{"max":"22","min":"16"}},{"date":"2020-09-27","cond":{"txt_d":"多云"},"tmp":{"max":"25","min":"16"}},{"date":"2020-09-28","cond":{"txt_d":"多云"},"tmp":{"ma
x":"19","min":"13"}},{"date":"2020-09-29","cond":{"txt_d":"小雨"},"tmp":{"max":"22","min":"16"}},{"date":"2020-09-30","cond":{"txt_d":"小雨"},"tmp":{"max":"20","min":"18
"}},{"date":"2020-10-01","cond":{"txt_d":"多云"},"tmp":{"max":"19","min":"16"}}],"aqi":{"city":{"aqi":"51","pm25":"25","qlty":"良"}},"suggestion":{"comf":{"type":"comf",
"brf":"舒适","txt":"白天不太热也不太冷，风力不大，相信您在这样的天气条件下，应会感到比较清爽和舒适。"},"sport":{"type":"sport","brf":"较不宜","txt":"有降水，且风力较强，
气压较低，推荐您在室内进行低强度运动；若坚持户外运动，须注意避雨防风。"},"cw":{"type":"cw","brf":"不宜","txt":"不宜洗车，未来24小时内有雨，如果在此期间洗车，雨水和路上的
泥水可能会再次弄脏您的爱车。"}},"msg":"所有天气数据均为模拟数据，仅用作学习目的使用，请勿当作真实的天气预报软件来使用。"}]}



     */


    public static boolean handleWeatherResponse(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray array = jsonObject.getJSONArray("HeWeather");
            JSONObject main = array.getJSONObject(0);
            String gsonObjectStr = main.toString();
            handleMainJson(gsonObjectStr);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return false;
    }

    public static boolean handleMainJson(String gsonObjectStr) {
        Log.d("tag", "gsonObjectStr : " + gsonObjectStr);
        Gson gson = new Gson();
        Weather weather = gson.fromJson(gsonObjectStr, Weather.class);
        Log.d("tag", "weather suggestion : " + weather.getmSuggestion());
        Log.d("tag", "weather msg : " + weather.getMsg());
        BasicData basicData = weather.getmBasic();
        Log.d("tag", "getAdmin_area : " + basicData.getAdmin_area());
        Suggestion suggestion = weather.getmSuggestion();
        Suggestion.Sport sport = suggestion.getSport();
        Log.d("tag", "sport suggestion : " + sport.getTxt());
        return false;
    }


    public static interface HandleCallBack<E> {
        void onFinished(List<E> list);
        void onError(Exception e);
    }



}
