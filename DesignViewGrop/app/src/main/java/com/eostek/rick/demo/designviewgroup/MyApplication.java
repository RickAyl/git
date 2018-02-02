package com.eostek.rick.demo.designviewgroup;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by a on 17-6-12.
 */

public class MyApplication extends Application {

    public ImageLoaderConfiguration mImageLoader;

    public static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        ImageLoaderConfiguration imageLoaderConfiguration = ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(imageLoaderConfiguration);

    }

    /**
     * @description
     *
     * @return 得到需要分配的缓存大小，这里用八分之一的大小来做
     */
    public int getMemoryCacheSize() {
        final int memClass = ((ActivityManager)getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();

        // Use 1/8th of the available memory for this memory cache.
        return 1024 * 1024 * memClass / 8;
    }

    public static MyApplication getIntance() {
        if (mInstance == null) {
            throw new NullPointerException("Fatal Exception : MyApplication is null !");
        }
        return mInstance;
    }
}
