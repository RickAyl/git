package com.eostek.rick.demo.designviewgroup.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import com.eostek.rick.demo.designviewgroup.R;
import com.eostek.rick.demo.designviewgroup.view.RoundImageDrawable;

/**
 * Created by a on 17-6-28.
 */

public class ImageViewDialog extends Dialog {

    private Context mContext;

    private ImageView imageView;

    private Drawable drawable;

    private String mUrl;

    private int width = 0;

    private int height = 0;

    private DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.mipmap.loading2)
            .cacheInMemory(false)
            .cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .build();

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };


    public ImageViewDialog(@NonNull Context context, String extra) {
        super(context);
        mContext = context;
        mUrl = extra;
    }

    public ImageViewDialog(@NonNull Context context, @StyleRes int themeResId, String extra) {
        super(context, themeResId);
        mContext = context;
        mUrl = extra;
    }

    protected ImageViewDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.image_layout);
        initView();
        setListener();
    }

    private void setListener() {

    }

    @Override
    public void cancel() {
        super.cancel();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }


    private void initView() {
        imageView = (ImageView) findViewById(R.id.imageview);
        //ImageLoader.getInstance().displayImage(mUrl,imageView,options);
        Glide.with(mContext).load(mUrl)
                .asBitmap()
                .placeholder(R.mipmap.loading2)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                        imageView.setImageDrawable(new RoundImageDrawable(bitmap));
                    }
                });
    }

    @Override
    public void show() {
        Window dialogWindow = getWindow();
        dialogWindow.setWindowAnimations(R.style.dialogWindowAnim);
        super.show();
    }
}
