package com.example.gitdemo.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.gitdemo.R;
import com.example.gitdemo.WeatherActivity;
import com.example.gitdemo.adapter.CountryAdapter;
import com.example.gitdemo.bean.Country;
import com.example.gitdemo.utils.HttpUtil;
import com.example.gitdemo.utils.SimpleDBUtils;
import com.example.gitdemo.utils.Utility;
import com.example.gitdemo.utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.gitdemo.utils.Utils.CONTENT_BASE_DB_COUNTRY_URI;

/*
[{"id":1,"name":"北京","weather_id":"CN101010100"},{"id":2,"name":"海淀","weather_id":"CN101010200"},
{"id":3,"name":"朝阳","weather_id":"CN101010300"},{"id":4,"name":"顺义","weather_id":"CN101010400"},
{"id":5,"name":"怀柔","weather_id":"CN101010500"},{"id":6,"name":"通州","weather_id":"CN101010600"},
{"id":7,"name":"昌平","weather_id":"CN101010700"},{"id":8,"name":"延庆","weather_id":"CN101010800"},
{"id":9,"name":"丰台","weather_id":"CN101010900"},{"id":10,"name":"石景山","weather_id":"CN101011000"},
{"id":11,"name":"大兴","weather_id":"CN101011100"},{"id":12,"name":"房山","weather_id":"CN101011200"},
{"id":13,"name":"密云","weather_id":"CN101011300"},{"id":14,"name":"门头沟","weather_id":"CN101011400"},
{"id":15,"name":"平谷","weather_id":"CN101011500"}]
 */

public class CountryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private List<Country> mList;

    private CountryAdapter mAdapter;

    private ProgressBar progressBar;

    private int cityCode = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);
        mList = new ArrayList<Country>();
        recyclerView = findViewById(R.id.country_list);
        progressBar = findViewById(R.id.progressBar);

        mAdapter = new CountryAdapter(mList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new CountryAdapter.ItemClickListener() {
            @Override
            public void onItemClick(String weatherID) {
                Intent intent = new Intent(CountryActivity.this, WeatherActivity.class);
                intent.putExtra("weather_id", weatherID);
                startActivity(intent);
            }
        });
        cityCode = getIntent().getIntExtra("city_code", -1);
        if (cityCode > 0) {
            updateDataFromDbOrInternet(cityCode);
        }
    }

    private void showList() {
        progressBar.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        recyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    /*
    + "_id integer primary key autoincrement, "
            + "country_name text, "
            + "weather_id text, "
            + "city_id integer)";
     */
    private void updateDataFromDbOrInternet(final int cityCode){
        Cursor c_cr = getContentResolver().query(CONTENT_BASE_DB_COUNTRY_URI, null, "city_id = ?", new String[]{String.valueOf(cityCode)}, null);
        if (c_cr != null && c_cr.getCount() > 0) {
            mList.clear();
            c_cr.moveToFirst();
            do {
                Country country = new Country();
                int id = c_cr.getInt(c_cr.getColumnIndex("_id"));
                int countryCode = c_cr.getInt(c_cr.getColumnIndex("country_code"));
                String name = c_cr.getString(c_cr.getColumnIndex("country_name"));
                String weather_id = c_cr.getString(c_cr.getColumnIndex("weather_id"));
                int city_id = c_cr.getInt(c_cr.getColumnIndex("city_id"));
                country.setId(id);
                country.setCountryName(name);
                country.setWeatherId(weather_id);
                country.setCityId(city_id);
                mList.add(country);
            } while (c_cr.moveToNext());
            if (mList != null && mList.size() > 0) {
                Log.d("country","mList.size() : " + mList.size());
                showList();
                mAdapter.notifyDataSetChanged();
            }

        } else {
            Cursor cr = getContentResolver().query(Utils.CONTENT_BASE_DB_CITY_URI, null, "city_code = ?", new String[]{String.valueOf(cityCode)}, null);
            if(cr != null && cr.getCount() > 0){
                cr.moveToFirst();
                int province_code = cr.getInt(cr.getColumnIndex("province_id"));
                HttpUtil.sendOkHttpRequest("http://guolin.tech/api/china/" + province_code + "/" + cityCode, new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String responseStr = response.body().string();
                        Utility.handleCountryResponse(responseStr, new Utility.HandleCallBack() {
                            @Override
                            public void onFinished(List list) {
                                Iterator<Country> iterator = list.iterator();
                                ContentValues values = new ContentValues();
                                mList.clear();
                                while (iterator != null && iterator.hasNext()) {
                                    Country country = iterator.next();
                                    mList.add(country);
                                    if (!SimpleDBUtils.getInstance()
                                            .hasDataBySelection("country", "weather_id = ?", new String[]{country.getWeatherId()})) {
                                        Log.d("city", "will insert >>>>>>>>>>>");
                                        values.clear();
                                        values.put("country_code",country.getCountryCode());
                                        values.put("country_name", country.getCountryName());
                                        values.put("weather_id", country.getWeatherId());
                                        values.put("city_id", country.getCityId());
                                        getContentResolver().insert(CONTENT_BASE_DB_COUNTRY_URI,values);
                                        Log.d("city", "finish insert >>>>>>>>>>>");
                                    }
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (mList != null && mList.size() > 0) {
                                            Log.d("country","mList.size() : " + mList.size());
                                            showList();
                                            mAdapter.notifyDataSetChanged();
                                        }
                                    }
                                });

                            }

                            @Override
                            public void onFinished(Object o) {

                            }

                            @Override
                            public void onError(Exception e) {
                                e.printStackTrace();
                            }
                        }, cityCode);
                    }
                });
            }

        }

    }
}