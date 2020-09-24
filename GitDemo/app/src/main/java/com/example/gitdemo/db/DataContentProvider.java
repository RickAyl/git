package com.example.gitdemo.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
        switch (matcher.match(uri)) {
            case 0:
                return db.delete("province", selection, selectionArgs);
            case 1:
                String p_id = uri.getPathSegments().get(1);
                return db.delete("province", "_id = ?", new String[]{p_id});
        }
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

    private void notifyContentChanged(final Uri uri, int uriMatch) {
        Long downloadId = null;
        downloadId = Long.parseLong(getIdFromUri(uri));
        Uri uriToNotify = uri;
        switch (uriMatch) {
            case 0:
                uriToNotify = Uri.parse("content://com.example.gitdemo.provider/province");
            case 1:
                uriToNotify = ContentUris.withAppendedId(Uri.parse("content://com.example.gitdemo.provider/province"), downloadId);
                break;
        }
        getContext().getContentResolver().notifyChange(uriToNotify, null);
    }

    private String getIdFromUri(final Uri uri) {
        return uri.getPathSegments().get(1);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        switch (matcher.match(uri)) {
            case 0:
            case 1:
                long p_id = db.insert("province",null, values);
                notifyContentChanged(ContentUris.withAppendedId(Uri.parse("content://com.example.gitdemo.provider/province"), p_id), 0);
                return ContentUris.withAppendedId(Uri.parse("content://com.example.gitdemo.provider/province"), p_id);

        }
        return null;
    }

    private PlaceDBHelper mHelper;

    private SQLiteDatabase db;

    private static final int CURRENT_VERSION_DB = 1;

    @Override
    public boolean onCreate() {
        mHelper = new PlaceDBHelper(getContext(),"place.db", null,CURRENT_VERSION_DB);
        db = mHelper.getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cr = null;
        switch (matcher.match(uri)) {
            case 0:
                return db.query("province", projection, selection, selectionArgs, null, null, sortOrder);
            case 1:
                String p_id = uri.getPathSegments().get(1);
                return db.query("province", projection, "_id = ?", new String[]{p_id}, null, null, sortOrder);
            default:
                break;
        }
        return cr;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        switch (matcher.match(uri)) {
            case 0:
                return  db.update("province", values, selection, selectionArgs);
            case 1:
                String p_id = getIdFromUri(uri);
                return  db.update("province", values, "_id = ?", new String[]{p_id});

        }
        return -1;
    }


}
