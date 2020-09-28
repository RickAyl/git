package com.example.gitdemo.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.gitdemo.R;
import com.example.gitdemo.adapter.CityAdapter;
import com.example.gitdemo.bean.City;
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

import static com.example.gitdemo.utils.Utils.CONTENT_BASE_DB_CITY_URI;

public class CityActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    CityAdapter mAdapter;

    List<City> mList;

    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        recyclerView = findViewById(R.id.city_list);

        mToolbar = findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mList = new ArrayList<City>();
        mAdapter = new CityAdapter(mList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        getCityDataFromDB();
        mAdapter.setOnItemClickListener(new CityAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int cityCode) {
                Intent intent = new Intent(CityActivity.this, CountryActivity.class);
                intent.putExtra("city_code", cityCode);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return true;
        //return super.onOptionsItemSelected(item);
    }

    private int getProvinceCode() {
        return getIntent().getIntExtra("province_code", -1);
    }

    private void getCityDataFromDB() {
        mList.clear();
        int provinceCode = getProvinceCode();
        if (provinceCode < 0) {
            Toast.makeText(this, "请求省份代码错误！", Toast.LENGTH_SHORT).show();
            finish();
        }
        Cursor cr = getContentResolver().query(CONTENT_BASE_DB_CITY_URI, null, "province_id = ?", new String[]{String.valueOf(provinceCode)}, null);
        Log.d("city", "getColumnCount" + cr.getColumnCount() + "getCount" +cr.getCount());
        if (cr == null || cr.getCount() == 0) {
            getCityDataFromInternet(provinceCode);
            return;
        }
        cr.moveToFirst();
        do {
            City city = new City();
            int cityCode = cr.getInt(cr.getColumnIndex("city_code"));
            String cityName = cr.getString(cr.getColumnIndex("city_name"));
            int province_id = cr.getInt(cr.getColumnIndex("province_id"));
            city.setProvinceId(province_id);
            city.setCityCode(cityCode);
            city.setCityName(cityName);
            mList.add(city);
        } while (cr.moveToNext());
        mAdapter.notifyDataSetChanged();
    }

    private void getCityDataFromInternet(final int provinceCode) {
        String requestCode = "http://guolin.tech/api/china/" + provinceCode;
        mList.clear();
        HttpUtil.sendOkHttpRequest(requestCode, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String json = response.body().string();
                Utility.handleCityResponse(json, new Utility.HandleCallBack() {
                    @Override
                    public void onFinished(List list) {
                        Iterator<City> it = list.iterator();
                        ContentValues values = new ContentValues();
                        while (it != null && it.hasNext()) {
                            City city = it.next();
                            mList.add(city);
                            if (!SimpleDBUtils.getInstance().hasDataBySelection("city", "city_code = ?",
                                    new String[]{city.getCityCode() + ""})) {
                                values.clear();
                                values.put("city_code", city.getCityCode());
                                values.put("city_name",city.getCityName());
                                values.put("province_id", city.getProvinceId());
                                getContentResolver().insert(CONTENT_BASE_DB_CITY_URI, values);
                            }
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.notifyDataSetChanged();
                            }
                        });
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                }, provinceCode);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}