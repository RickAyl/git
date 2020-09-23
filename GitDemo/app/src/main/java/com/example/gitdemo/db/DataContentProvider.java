package com.example.gitdemo.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class DataContentProvider extends ContentProvider {

    private static UriMatcher matcher;

    static {
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI("com.example.gitdemo.provider","province", 0);
        matcher.addURI("com.example.gitdemo.provider","province/#", 1);

        matcher.addURI("com.example.gitdemo.provider","city", 2);
        matcher.addURI("com.example.gitdemo.provider","city/#", 3);

        matcher.addURI("com.example.gitdemo.provider","country", 4);
        matcher.addURI("com.example.gitdemo.provider","country/#", 5);
    }

    public DataContentProvider() {
    }



    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return -1;
    }

    @Override
    public String getType(Uri uri) {
        switch (matcher.match(uri)) {
            case 0:
                return "vnd.android.cursor.dir/com.example.gitdemo.provider.province";
            case 1:
                return "vnd.android.cursor.item/com.example.gitdemo.provider.province";
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        return null;

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        return -1;
    }
}
