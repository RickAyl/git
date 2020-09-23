package com.example.gitdemo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class PlaceDBHelper extends SQLiteOpenHelper {

    public static final String CREATE_PROVICE_TABLE = "create table province ("
                                                    + "_id integer primary key autoincrement, "
                                                    + "province_name text, "
                                                    + "province_code integer)";

    public static final String CREATE_CITY_TABLE = "create table city ("
                                                 + "_id integer primary key autoincrement, "
                                                 + "city_name text, "
                                                 + "city_code text, "
                                                 + "province_id integer)";

    public static final String CREATE_COUNTRY_TABLE = "create table country ("
            + "_id integer primary key autoincrement, "
            + "country_name text, "
            + "weather_id text, "
            + "city_id integer)";

    public PlaceDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_PROVICE_TABLE);
        sqLiteDatabase.execSQL(CREATE_CITY_TABLE);
        sqLiteDatabase.execSQL(CREATE_COUNTRY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //update db
    }
}
