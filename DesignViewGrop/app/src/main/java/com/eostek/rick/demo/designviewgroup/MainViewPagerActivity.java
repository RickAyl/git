package com.eostek.rick.demo.designviewgroup;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.KeyEvent;

public class MainViewPagerActivity extends FragmentActivity {

    private MainActivityHolder mainActivityHolder;

    private LruCache<String, Bitmap> mMemoryCache;

    private static final int memoryCacheSize = MyApplication.getIntance().getMemoryCacheSize();


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMemoryCache = new LruCache<String,Bitmap>(memoryCacheSize){

            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount() / 1024;
            }
        };
        Log.d("rick","memoryCacheSize : " + memoryCacheSize);
        setContentView(R.layout.viewpager_layout);
        mainActivityHolder = new MainActivityHolder(this ,mHandler);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(mainActivityHolder.isOpenedLeftMenu()){
                mainActivityHolder.closeLeftMenu();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    private Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }
}
