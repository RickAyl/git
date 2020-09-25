package com.example.gitdemo.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;


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
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        switch (matcher.match(uri)) {
            case 0:
                return db.delete("province", selection, selectionArgs);
            case 1:
                String p_id = uri.getPathSegments().get(1);
                return db.delete("province", "_id = ?", new String[]{p_id});
            case 2:
                return db.delete("city", selection, selectionArgs);
            case 3:
                String c_id = uri.getPathSegments().get(1);
                return db.delete("city", "_id = ?", new String[]{c_id});
            case 4:
                return db.delete("country", selection, selectionArgs);
            case 5:
                String co_id = uri.getPathSegments().get(1);
                return db.delete("country", "_id = ?", new String[]{co_id});
        }
        return -1;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        switch (matcher.match(uri)) {
            case 0:
                return "vnd.android.cursor.dir/com.example.gitdemo.provider.province";
            case 1:
                return "vnd.android.cursor.item/com.example.gitdemo.provider.province";
            case 2:
                return "vnd.android.cursor.dir/com.example.gitdemo.provider.city";
            case 3:
                return "vnd.android.cursor.item/com.example.gitdemo.provider.city";
            case 4:
                return "vnd.android.cursor.dir/com.example.gitdemo.provider.country";
            case 5:
                return "vnd.android.cursor.item/com.example.gitdemo.provider.country";
        }
        return null;
    }

    private void notifyContentChanged(@NonNull Uri uri, int uriMatch) {
        long downloadId ;
        switch (uriMatch) {
            case 1:
                downloadId = Long.parseLong(getIdFromUri(uri));
                uri = ContentUris.withAppendedId(Uri.parse("content://com.example.gitdemo.provider/province"), downloadId);
                break;
            case 3:
                downloadId = Long.parseLong(getIdFromUri(uri));
                uri = ContentUris.withAppendedId(Uri.parse("content://com.example.gitdemo.provider/city"), downloadId);
                break;
            case 5:
                downloadId = Long.parseLong(getIdFromUri(uri));
                uri = ContentUris.withAppendedId(Uri.parse("content://com.example.gitdemo.provider/country"), downloadId);
                break;

        }

        getContext().getContentResolver().notifyChange(uri, null);
    }

    private String getIdFromUri(final Uri uri) {
        return uri.getPathSegments().get(1);
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        long _id ;
        switch (matcher.match(uri)) {
            case 0:
            case 1:
                _id = db.insert("province",null, values);
                notifyContentChanged(ContentUris.withAppendedId(Uri.parse("content://com.example.gitdemo.provider/province"), _id), 0);
                return ContentUris.withAppendedId(Uri.parse("content://com.example.gitdemo.provider/province"), _id);
            case 2:
            case 3:
                _id = db.insert("city",null, values);
                notifyContentChanged(ContentUris.withAppendedId(Uri.parse("content://com.example.gitdemo.provider/city"), _id), 2);
                return ContentUris.withAppendedId(Uri.parse("content://com.example.gitdemo.provider/city"), _id);
            case 4:
            case 5:
                _id = db.insert("country",null, values);
                notifyContentChanged(ContentUris.withAppendedId(Uri.parse("content://com.example.gitdemo.provider/country"), _id), 4);
                return ContentUris.withAppendedId(Uri.parse("content://com.example.gitdemo.provider/country"), _id);

        }

        return null;
    }

    private SQLiteDatabase db;

    private static final int CURRENT_VERSION_DB = 1;

    private static final int CURRENT_VERSION_DB_NEW = 2;

    private static final int CURRENT_VERSION_DB_NEW_3 = 3;

    @Override
    public boolean onCreate() {
        PlaceDBHelper mHelper = new PlaceDBHelper(getContext(), "place.db", null, CURRENT_VERSION_DB_NEW_3);
        db = mHelper.getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        switch (matcher.match(uri)) {
            case 0:
                return db.query("province", projection, selection, selectionArgs, null, null, sortOrder);
            case 1:
                String p_id = uri.getPathSegments().get(1);
                return db.query("province", projection, "_id = ?", new String[]{p_id}, null, null, sortOrder);
            case 2:
                return db.query("city", projection, selection, selectionArgs, null, null, sortOrder);
            case 3:
                String c_id = uri.getPathSegments().get(1);
                return db.query("city", projection, "_id = ?", new String[]{c_id}, null, null, sortOrder);
            case 4:
                return db.query("country", projection, selection, selectionArgs, null, null, sortOrder);
            case 5:
                String co_id = uri.getPathSegments().get(1);
                return db.query("country", projection, "_id = ?", new String[]{co_id}, null, null, sortOrder);

        }
        return null;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        switch (matcher.match(uri)) {
            case 0:
                return  db.update("province", values, selection, selectionArgs);
            case 1:
                String p_id = getIdFromUri(uri);
                return  db.update("province", values, "_id = ?", new String[]{p_id});
            case 2:
                return  db.update("city", values, selection, selectionArgs);
            case 3:
                String c_id = getIdFromUri(uri);
                return  db.update("city", values, "_id = ?", new String[]{c_id});
            case 4:
                return  db.update("country", values, selection, selectionArgs);
            case 5:
                String co_id = getIdFromUri(uri);
                return  db.update("country", values, "_id = ?", new String[]{co_id});
        }
        return -1;
    }


}
