package com.example.gitdemo.service;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.gitdemo.MainApplication;
import com.example.gitdemo.bean.Province;
import com.example.gitdemo.utils.HttpUtil;
import com.example.gitdemo.utils.SimpleDBUtils;
import com.example.gitdemo.utils.Utility;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainService extends Service {

    private MainApplication mMainApplication;

    public static final String TAG = MainService.class.getSimpleName();

    private static final boolean DBUG = true;

    private static Context mService;

    private static Handler mHandler = null;

    private void initHandler(){
        mHandler = new Handler(getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        break;
                }
            }
        };
    }

    public static Context getService() {
        return mService;
    }

    public static Handler getMainHandler() {
        return mHandler;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mService = this;
        mMainApplication = MainApplication.getInstance();
        initHandler();
        mainServiceLog("onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mainServiceLog("onStartCommand");
        mSimpleDBUtils = SimpleDBUtils.getInstance();
        mMainApplication.getThreadPoolExecutor().execute(new Runnable() {
            @Override
            public void run() {
                requestInternet();
            }
        });

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        mainServiceLog("onBind");
        mSimpleDBUtils = SimpleDBUtils.getInstance();
        mBinder = new MyBinder();
        mMainApplication.getThreadPoolExecutor().execute(new Runnable() {
            @Override
            public void run() {
                requestInternet();
            }
        });
        return mBinder;
    }

    private MyBinder mBinder;

    public class MyBinder extends Binder{

        public void onDBFininshed(){

        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mainServiceLog("onUnbind");
        mBinder = null;
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        mainServiceLog("onRebind");
        super.onRebind(intent);
    }

    public boolean isActive() {
        return mService != null;
    }

    @Override
    public void onDestroy() {
        mainServiceLog("onDestroy");
        mService = null;
        super.onDestroy();
    }

    private void mainServiceLog(String msg){
        if (DBUG)
            Log.d(TAG, msg);
    }

    private SimpleDBUtils mSimpleDBUtils = null;


    private void requestInternet(){
        //http://www.baidu.com http://guolin.tech/api/china
        HttpUtil.sendOkHttpRequest("http://guolin.tech/api/china", new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                Utility.handleProviceResponse(response.body().string(), new Utility.HandleCallBack<Province>() {
                    @Override
                    public void onFinished(List<Province> list) {
                        Iterator<Province> iterator = list.iterator();
                        while (iterator != null && iterator.hasNext()) {
                            Province province = iterator.next();
                            boolean hasData = mSimpleDBUtils.hasDataBySelection("province","province_code = ?",new String[]{String.valueOf(province.getProvinceCode())});
                            ContentValues values = new ContentValues();
                            if(hasData){
                                values.put("province_name",province.getProvinceName());
                                values.put("province_code",province.getProvinceCode());
                                getContentResolver().update(Uri.parse("content://com.example.gitdemo.provider/province"),
                                        values, "province_code = ?",new String[]{String.valueOf(province.getProvinceCode())});
                                //mSimpleDBUtils.updateData(values,"province","province_code = ?",new String[]{String.valueOf(province.getProvinceCode())} );
                            }else {
                                values.put("province_name",province.getProvinceName());
                                values.put("province_code",province.getProvinceCode());
                                getContentResolver().insert(Uri.parse("content://com.example.gitdemo.provider/province"), values);
                                //mSimpleDBUtils.insertData(values, "province");
                            }
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        });

    }
}
