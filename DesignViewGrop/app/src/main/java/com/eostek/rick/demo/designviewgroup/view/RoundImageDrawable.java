package com.eostek.rick.demo.designviewgroup.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.WindowManager;

/**
 * Created by a on 17-7-7.
 */

public class RoundImageDrawable extends Drawable{

    private Context mContext;

    /**
     * 需要绘制的bitmap
     */

    private Bitmap mBitmap;

    /**
     * 画笔
     */

    private Paint mPaint;

    /**
     * 绘制的矩形
     */

    private RectF rectF;

    /**
     * drawable 的宽
     */

    private int width = 0;

    /**
     * drawable 的高
     */

    private int height = 0;

    /**
     * 圆角的半径
     */

    private int rad = 0;

    public RoundImageDrawable(int res, Context context) {
        this(BitmapFactory.decodeResource(context.getResources(), res));
        mContext = context;
    }

    public RoundImageDrawable(Drawable drawable) {
        this(drawableToBitamp(drawable));
    }

    public RoundImageDrawable(Bitmap bitmap) {
        mBitmap = bitmap;
        BitmapShader bitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP,
                Shader.TileMode.CLAMP);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setShader(bitmapShader);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawRoundRect(rectF, rad, rad, mPaint);
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        if (mContext != null) {
            int wid = 0;
            int hei = 0;
            if (mContext instanceof Activity) {
                WindowManager wm = ((Activity) mContext).getWindowManager();
                hei = wm.getDefaultDisplay().getHeight();
                wid = wm.getDefaultDisplay().getWidth();
            } else {
                hei = mContext.getWallpaper().getIntrinsicHeight();
                wid = mContext.getWallpaper().getIntrinsicWidth();
            }
            if ((right - left) >= wid) {
                left = (int) (left + 0.1 * wid);
                right = (int) (right - 0.1 * wid);
            }
            if ((bottom - top) >= hei) {
                top = (int) (top + 0.1 * top);
                bottom = (int) (bottom - 0.1 * bottom);
            }

        }
        width = right - left;
        height = bottom - top;
        rad = (int) (Math.min((right - left), (bottom - top)) * 0.2);
        super.setBounds(left, top, right, bottom);
        rectF = new RectF(left, top, right, bottom);


    }

    @Override
    public int getIntrinsicHeight() {
        if (height > 0) {
            return height;
        } else if (mBitmap != null) {
            return mBitmap.getHeight();
        } else {
            return super.getIntrinsicHeight();
        }
    }

    @Override
    public int getIntrinsicWidth() {
        if (width > 0) {
            return width;
        } else if (mBitmap != null) {
            return mBitmap.getWidth();
        } else {
            return super.getIntrinsicWidth();
        }
    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public static Bitmap drawableToBitamp(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config =
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565;
        return Bitmap.createBitmap(width, height, config);
    }


}
