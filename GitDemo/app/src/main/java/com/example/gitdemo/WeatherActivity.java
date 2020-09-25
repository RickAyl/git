package com.example.gitdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.example.gitdemo.utils.HttpUtil;
import com.example.gitdemo.utils.Utility;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/*
    key : 5355f80e02e54724ac33d21a954b3edf
    example : http://guolin.tech/api/weather?cityid=CN101042100&key=5355f80e02e54724ac33d21a954b3edf
 */


public class WeatherActivity extends AppCompatActivity {

    private static final String REQUEST_KEY_MY = "5355f80e02e54724ac33d21a954b3edf";

    private static final String APPEND_ADDR_KEY = "&key=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        String weatherId = getIntent().getStringExtra("weather_id");
        requestWeatherDataFromInternet(weatherId);

    }

    void requestWeatherDataFromInternet(String weatherId) {
        Log.d("weather", "weather id : " + weatherId);
        String requestAddress = "http://guolin.tech/api/weather?cityid=" + weatherId + APPEND_ADDR_KEY + REQUEST_KEY_MY;
        Log.d("weather", "requestAddress : " + requestAddress);
        if (!TextUtils.isEmpty(weatherId)) {
            HttpUtil.sendOkHttpRequest(requestAddress, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    Utility.handleWeatherResponse(response.body().string());
                }
            });
        }
    }
}