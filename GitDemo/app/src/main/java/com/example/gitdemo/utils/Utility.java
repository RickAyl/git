package com.example.gitdemo.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.gitdemo.MainActivity;
import com.example.gitdemo.MainApplication;
import com.example.gitdemo.bean.City;
import com.example.gitdemo.bean.Country;
import com.example.gitdemo.bean.Province;
import com.example.gitdemo.bean.Weather;
import com.google.gson.Gson;
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
        return true;
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
        return true;
    }



    public static boolean handleCountryResponse(@NonNull String response, HandleCallBack callBack, int cityCode){
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



    public static boolean handleWeatherResponse(@NonNull final String response, final HandleCallBack<Weather> callBack) {
        MainApplication.getInstance().getThreadPoolExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("HeWeather");
                    JSONObject main = array.getJSONObject(0);
                    String gsonObjectStr = main.toString();
                    handleMainJson(gsonObjectStr, callBack);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return true;
    }

    public static boolean handleMainJson(String gsonObjectStr, HandleCallBack<Weather> callBack) {
        Gson gson = new Gson();
        Weather weather = gson.fromJson(gsonObjectStr, Weather.class);
        callBack.onFinished(weather);
        return true;
    }


    public static interface HandleCallBack<E> {
        void onFinished(List<E> list);
        void onFinished(E e);
        void onError(Exception e);
    }



}
