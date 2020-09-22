package com.example.gitdemo.utils;

import android.text.TextUtils;
import android.util.Log;

import com.example.gitdemo.MainActivity;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

public class Utility {

    public static boolean handleProviceResponse(String response){
        Log.d(MainActivity.TAG,"response : " + response);
       if(!TextUtils.isEmpty(response)){
           try {
               JsonArray allProvices = new JsonArray();
               allProvices.add(response);

               /*Gson gson = new Gson();
               String[] strings = gson.fromJson(response, String[].class);
               for (int i = 0; i < strings.length; i ++){
                   Log.d(MainActivity.TAG,"string" + "[" +i+ "]" + " : " + strings[i]);
               }*/


           } catch (Exception e) {
                e.printStackTrace();
           }
       }
        return false;
    }
}
