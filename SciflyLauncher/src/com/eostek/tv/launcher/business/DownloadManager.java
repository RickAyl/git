
package com.eostek.tv.launcher.business;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.eostek.tv.launcher.R;
import com.eostek.tv.launcher.model.DownloadInfo;
import com.eostek.tv.launcher.util.UIUtil;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.app.AlertDialog;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.os.Environment;
import android.util.Log;
import scifly.dm.EosDownloadListener;
import scifly.dm.EosDownloadManager;
import scifly.dm.EosDownloadTask;
import scifly.storage.StorageManagerExtra;

/*
 * projectName： SciflyLauncher2
 * moduleName： DownloadManager.java
 *
 * @author chadm.xiang
 * @version 1.0.0
 * @time  2014-6-20 上午10:57:42
 * @Copyright © 2013 Eos Inc.
 */

/**
 * to handler the download apk logic
 **/
public final class DownloadManager {

    private final String TAG = DownloadManager.class.getSimpleName();

    private static DownloadManager dManager;

    private EosDownloadManager eosDownloadManager;

    private Context mContext;

    private final int FINISH_DOWNLOAD_STATUS = 100;

    public static DownloadManager getDownloadManagerInstance(Context context) {
        if (dManager == null) {
            dManager = new DownloadManager(context);
        }
        return dManager;
    }

    private DownloadManager(Context context) {
        this.mContext = context;
        eosDownloadManager = new EosDownloadManager(mContext);
    }

    /**
     * start download from the given url
     * 
     * @param path download path
     */
    public void startDownload(final String path) {
        Uri uri = Uri.parse(path);
        String fileName = path.substring(path.lastIndexOf("/") + 1);
        Log.d(TAG, "fileName: " + fileName);
        Request request = new Request(uri);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
        EosDownloadListener listener = new EosDownloadListener() {
            public void onDownloadStatusChanged(int status) {
                // Log.d(TAG, "status: " + status);
            }

            public void onDownloadSize(long size) {
                // Log.d(TAG, "size: " + size);
            }

            // the percent is in terms of percentage
            public void onDownloadComplete(int percent) {
                Log.d(TAG, "percent: " + percent);
                if (percent == FINISH_DOWNLOAD_STATUS) {
                    // install apk when finish download
                    String fileName = path.substring(path.lastIndexOf("/") + 1);
                    String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                            .getAbsolutePath() + File.separator + fileName;
                    Log.v(TAG, "filePath = " + filePath);
		            String realFileName = filePath.split("\\?")[0];
		            int savedId =getSavedId(mContext);
                    if(savedId == 0){
                    	String[] udiskPaths = StorageManagerExtra.getInstance(mContext).getUdiskPaths();
                        Log.d(TAG, "路径-----"+udiskPaths[0]);
                        filePath = udiskPaths[0] + "/" + "Download";
                        realFileName = filePath + "/" + fileName.split("\\?")[0];
                    }
                    Log.v(TAG, "realFileName = " + realFileName);
//                    new AlertDialog.Builder(mContext)
//                    .setMessage(
//                    		mContext.getResources().getString(R.string.if_install_apk))
//                    .setPositiveButton(android.R.string.ok, new android.content.DialogInterface.OnClickListener() {
//
//                        @Override
//                        public void onClick(DialogInterface arg0, int arg1) {
                        	UIUtil.install(mContext, realFileName);
//                        }
//
//                    }).setNegativeButton(android.R.string.cancel, new android.content.DialogInterface.OnClickListener() {
//
//                        @Override
//                        public void onClick(DialogInterface arg0, int arg1) {
//                        }
//                        }).show();
                }
            }
        };
        EosDownloadTask task = new EosDownloadTask(request, listener);
        eosDownloadManager.addTask(task);
    }

    /**
     * judge the storage location is inside or outside
     * @return
     */
    public static int getSavedId(Context mContext){
        int save_dir_path = -1;
        final Uri DOWDLODE_URL = Uri.parse("content://com.android.providers.downloads.setting/setting");
        ContentResolver resolver = mContext.getContentResolver();
        Cursor cursor = resolver.query(DOWDLODE_URL, null, null, null, null);
        if(cursor!=null && cursor.getCount() > 0) {
            while(cursor.moveToNext()) { 
                 int index = cursor.getColumnIndex("is_data_cache");
                 if (index >= 0)
                     save_dir_path = cursor.getInt(index);
            }  
        }
        return save_dir_path;
    }
    
    /**
     * query the download info in the download list.
     * @return list<DownloadInfo>
     */
    public List<DownloadInfo> queryDownloadInfo() {
        List<DownloadInfo> list = new ArrayList<DownloadInfo>();
        Query query = new Query();
        android.app.DownloadManager dm = (android.app.DownloadManager) mContext.getSystemService(mContext.DOWNLOAD_SERVICE);
        Cursor cursor = dm.query(query);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                DownloadInfo info = new DownloadInfo(null, 0, 0);
                info.setUri(cursor.getString(cursor.getColumnIndex(dm.COLUMN_URI)));
                info.setPresentBytes(cursor.getLong(cursor.getColumnIndex(dm.COLUMN_BYTES_DOWNLOADED_SO_FAR)));
                info.setTotalBytes(cursor.getLong(cursor.getColumnIndex(dm.COLUMN_TOTAL_SIZE_BYTES)));
                list.add(info);
            }
            cursor.close();
        }
        return list;
    }
    
    /**
     * get DownloadInfo from the list.
     * @param url
     * @return DownloadInfo
     */
    public DownloadInfo getDownloadInfo(String url){
        List<DownloadInfo> list = queryDownloadInfo();
        for (DownloadInfo downloadInfo : list) {
            String uri = downloadInfo.getUri();
            if (uri.equals(url)) {
                return downloadInfo;
            }
        }
        return null;
    }
    
}
