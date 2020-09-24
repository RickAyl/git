package com.example.gitdemo;

import android.app.Application;

public class MainApplication extends Application {

    public static MainApplication mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static MainApplication getInstance() {
        return mContext;
    }
}
