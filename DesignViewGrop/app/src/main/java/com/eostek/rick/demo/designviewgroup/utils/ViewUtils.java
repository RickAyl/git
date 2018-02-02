package com.eostek.rick.demo.designviewgroup.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by a on 17-6-23.
 */

public class ViewUtils {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        final float scale = dm.density;

        Log.v("rickss","scale : " + scale + " dpi : " + dm.densityDpi);
        return (int) (pxValue / scale + 0.5f);
    }
}
