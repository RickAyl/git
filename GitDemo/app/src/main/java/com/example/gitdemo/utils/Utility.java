package com.example.gitdemo.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import com.example.gitdemo.MainActivity;
import com.example.gitdemo.bean.City;
import com.example.gitdemo.bean.Province;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.Iterator;
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

    /*
    [{"id":113,"name":"南京"},{"id":114,"name":"无锡"},{"id":115,"name":"镇江"},
    {"id":116,"name":"苏州"},{"id":117,"name":"南通"},{"id":118,"name":"扬州"},
    {"id":119,"name":"盐城"},{"id":120,"name":"徐州"},{"id":121,"name":"淮安"},
    {"id":122,"name":"连云港"},{"id":123,"name":"常州"},{"id":124,"name":"泰州"},
    {"id":125,"name":"宿迁"}]
     */

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



    public static boolean handleCountryResponse(String response, HandleCallBack callBack){
        return false;
    }


    public static interface HandleCallBack<E> {
        void onFinished(List<E> list);
        void onError(Exception e);
    }



}
