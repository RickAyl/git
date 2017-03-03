
package com.eostek.tv.launcher.util;

import java.util.Locale;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

/**
 * projectName： WasuWidgetHost. moduleName： SettingsObserver.java
 * 
 * @author Admin
 * @function monitor city change.
 */
public class SettingsObserver extends ContentObserver {

    private Context mContext;

    private ContentResolver resolver;

    private Handler mHandler;
    
    private String lastCountry;

    /**
     * construction method.
     * 
     * @param handler
     * @param context
     */
    public SettingsObserver(Handler handler, Context context) {
        super(handler);
        this.mContext = context;
        this.mHandler = handler;
        lastCountry=Locale.getDefault().getCountry();
    }

    /**
     * register content observer.
     * 
     * @param context
     */
    public final void observe(Context context) {
        resolver = context.getContentResolver();
        resolver.registerContentObserver(Uri.parse("content://com.eostek.scifly.provider/global"), true, this);
    }

    /**
     * unregister content observer.
     */
    public final void unregisterContentObserver() {
        resolver.unregisterContentObserver(this);
    }

    @Override
    public void onChange(boolean selfChange) {
   String currentCountry = Locale.getDefault().getCountry();
        // if(!lastCountry.equals(currentCountry)){
        // lastCountry=currentCountry;
        mHandler.sendEmptyMessage(LConstants.MSG_CITY_CHANGE);
        // }
    }
}
