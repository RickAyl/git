package com.example.gitdemo.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SimpleDBUtils {

    private SQLiteOpenHelper mSQLiteHelper;

    private SQLiteDatabase mDB;

    private Context mContext;

    public SimpleDBUtils(Context context, SQLiteOpenHelper helper) {
        mSQLiteHelper = helper;
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
        Cursor cr = null;
        cr = mDB.query(tableName, null, selection, selectionArgs, null, null, null);
        return cr;
    }
}
