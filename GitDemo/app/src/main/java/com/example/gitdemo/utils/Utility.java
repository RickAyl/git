package com.example.gitdemo.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import com.example.gitdemo.MainActivity;
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

    public static boolean handleProviceResponse(String response){
        Log.d(MainActivity.TAG,"response : " + response);
       if(!TextUtils.isEmpty(response)){
           Gson gson = new Gson();
           try {
               List<Province> provinces = gson.fromJson(response,new TypeToken<List<Province>>(){}.getType());
               for (int i = 0; i < provinces.size(); i++) {
                   Log.d(MainActivity.TAG,"城市" + provinces.get(i).getProvinceCode() + ":" + provinces.get(i).getProvinceName());
               }
           } catch (Exception e) {
               e.printStackTrace();
           }

       }
        return false;
    }



}
