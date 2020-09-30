package com.example.gitdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.example.gitdemo.bean.DailyForecast;
import com.example.gitdemo.bean.Weather;
import com.example.gitdemo.utils.HttpUtil;
import com.example.gitdemo.utils.Utility;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Timer;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/*
    key : 5355f80e02e54724ac33d21a954b3edf
    example : http://guolin.tech/api/weather?cityid=CN101042100&key=5355f80e02e54724ac33d21a954b3edf
 */


public class WeatherActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String REQUEST_KEY_MY = "5355f80e02e54724ac33d21a954b3edf";

    private static final String APPEND_ADDR_KEY = "&key=";

    private SwipeRefreshLayout swipeRefreshLayout;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        initHandler();
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_view);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);

        String weatherId = getIntent().getStringExtra("weather_id");
        requestWeatherDataFromInternet(weatherId);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void initHandler() {
        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
            }
        };
    }

    public void setSwipeRefreshTimeOut(long time) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, time);
    }

    private void requestWeatherDataFromInternet(String weatherId) {
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
                    Utility.handleWeatherResponse(response.body().string(), new Utility.HandleCallBack<Weather>() {

                        @Override
                        public void onFinished(List<Weather> list) {
                            //do nothing
                        }

                        @Override
                        public void onFinished(Weather weather) {
                            DailyForecast[] forecast = weather.getDailyForecasts();
                            for (int i = 0; i < forecast.length; i++) {
                                //do something
                            }

                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
                }
            });
        }
    }

    @Override
    public void onRefresh() {
        setSwipeRefreshTimeOut(5000);
    }
}