package com.example.gitdemo.utils;

import android.net.Uri;

import java.net.URI;

public class Utils {

    public static final String CONTENT_BASE_DB_URI_STR = "content://com.example.gitdemo.provider";

    public static final Uri CONTENT_BASE_DB_URI = Uri.parse("content://com.example.gitdemo.provider");

    public static final Uri CONTENT_BASE_DB_CITY_URI = Uri.parse("content://com.example.gitdemo.provider/city");

}
