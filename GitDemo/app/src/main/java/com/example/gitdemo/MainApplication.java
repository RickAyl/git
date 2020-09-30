package com.example.gitdemo;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class MainApplication extends Application {

    private static final String TAG = MainApplication.class.getSimpleName();

    private ThreadPoolExecutor threadPoolExecutor;

    public static MainApplication mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        threadPoolExecutor = new ThreadPoolExecutor(2, 4, 1000,
                TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(5), Executors.defaultThreadFactory(),
                new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        Log.d(TAG, "Task was rejected by System : Task queue upper limit!");
                    }
                });

        registerActivityLifecycle();

    }

    private void registerActivityLifecycle() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {

            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {

            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {

            }
        });
    }

    public ThreadPoolExecutor getThreadPoolExecutor() {
        if (threadPoolExecutor == null) {
            throw new NullPointerException("ThreadPoolExecutor is null");
        }
        return threadPoolExecutor;
    }

    public static MainApplication getInstance() {
        return mContext;
    }

}
