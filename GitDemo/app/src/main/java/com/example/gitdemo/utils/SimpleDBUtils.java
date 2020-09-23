package com.example.gitdemo.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.core.content.ContextCompat;

import com.example.gitdemo.MainApplication;
import com.example.gitdemo.db.PlaceDBHelper;

public class SimpleDBUtils {

    private static final int CURRENT_VERSION_DB = 1;

    private SQLiteOpenHelper mSQLiteHelper;

    private SQLiteDatabase mDB;

    private Context mContext;

    private static SimpleDBUtils sInstance;

    public static SimpleDBUtils getInstance(Context context){
        if (sInstance == null) {
            synchronized (SimpleDBUtils.class){
                if (sInstance == null) {
                    sInstance = new SimpleDBUtils(context);
                }
            }
        }
        return sInstance;
    }

    private SimpleDBUtils(Context context) {
        mSQLiteHelper = new PlaceDBHelper(context,"place.db", null,CURRENT_VERSION_DB);
        mContext = context;
        mDB = mSQLiteHelper.getWritableDatabase();
    }


    public void insertData(ContentValues contentValues, String tableName){
        mDB.insert(tableName, null, contentValues);
    }

    public void updateData(ContentValues contentValues, String tableName, String where, String []whereArgs){
        mDB.update(tableName,contentValues, where, whereArgs);
    }

    public void deleteData(String tableName, String where, String []whereArgs){
        mDB.delete(tableName, where, whereArgs);
    }

    public Cursor queryData(String tableName, String selection, String[] selectionArgs){
        Cursor cr = mDB.query(tableName, null, selection, selectionArgs, null, null, null);
        return cr;
    }

    public boolean hasDataBySelection(String tableName, String selection, String[] selectionArgs){
        Cursor cr = mDB.query(tableName, null, selection, selectionArgs, null, null, null);
        if (cr != null){
            return cr.moveToFirst();
        }
        return false;
    }
}
