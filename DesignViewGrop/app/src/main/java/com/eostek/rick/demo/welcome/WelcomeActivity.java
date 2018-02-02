package com.eostek.rick.demo.welcome;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.eostek.rick.demo.designviewgroup.MainActivity;
import com.eostek.rick.demo.designviewgroup.R;

public class WelcomeActivity extends Activity {

    private static final String TAG = WelcomeActivity.class.getSimpleName();

    private ImageView welcomeImage;

    private TextView versionTxt;

    private Bitmap localBitmap;

    private String imagePath = null;

    private Bitmap netBitmap;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            Intent intent = new Intent();
            intent.setClass(WelcomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_layout);
        imagePath = getFilesDir().getPath() + "/" + "welcome.jpg";

        welcomeImage = (ImageView) findViewById(R.id.welcome_bg);
        versionTxt = (TextView) findViewById(R.id.welcome_versionTxt);

        versionTxt.setText("Version : v" + getVersionName());

        localBitmap = BitmapFactory.decodeFile(imagePath);
        Log.i(TAG, "local welcome image: " + localBitmap);
        // 如果之前本地有已经从网上下下来的图片，则用本地路径存的图片
        if (localBitmap != null) {
            welcomeImage.setImageBitmap(localBitmap);
        } else {
            welcomeImage.setImageResource(R.mipmap.welcome);
        }
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume");
        mHandler.sendEmptyMessageDelayed(0, 3000);
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "onBackPressed");
        // 当前界面按返回就不跳转
        mHandler.removeMessages(0);
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy");
        if (localBitmap != null) {
            Log.i(TAG, "recycle localBitmap");
            localBitmap.recycle();
            localBitmap = null;
        }

        if (netBitmap != null) {
            Log.i(TAG, "recycle netBitmap");
            netBitmap.recycle();
            netBitmap = null;
        }

        super.onDestroy();
    }



    private String getVersionName() {
        String versionName = "";
        try {
            PackageInfo packInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }


}
