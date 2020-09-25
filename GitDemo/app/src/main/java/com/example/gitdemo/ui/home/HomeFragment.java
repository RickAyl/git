package com.example.gitdemo.ui.home;

import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gitdemo.R;
import com.example.gitdemo.adapter.ProvinceAdapter;
import com.example.gitdemo.bean.Province;
import com.example.gitdemo.ui.CityActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final int UPDATE_DATA_MSG = 0;

    private HomeViewModel homeViewModel;

    private RecyclerView mProvinceListView;

    private ProvinceAdapter mAdapter;

    private Handler mHandler;

    private List<Province> mProvinceList;

    private void initHandler() {
        mHandler = new Handler(getActivity().getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case UPDATE_DATA_MSG:
                        getProvinceDataFromDB();
                        break;
                }
            }
        };
    }

    private Uri BASE_URI = Uri.parse("content://com.example.gitdemo.provider/province");

    private void getProvinceDataFromDB() {
        Cursor cr = getActivity().getContentResolver().query(BASE_URI, null, null, null, null);
        if (cr == null || cr.getCount() <= 0) {
            return;
        }
        cr.moveToFirst();
        mProvinceList.clear();
        do {
            Province pv = new Province();
            int id = cr.getInt(cr.getColumnIndex("_id"));
            String name = cr.getString(cr.getColumnIndex("province_name"));
            int code = cr.getInt(cr.getColumnIndex("province_code"));
            pv.setId(id);
            pv.setProvinceName(name);
            pv.setProvinceCode(code);
            mProvinceList.add(pv);
        } while (cr.moveToNext());
        mAdapter.notifyDataSetChanged();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        initHandler();
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mProvinceListView = root.findViewById(R.id.province_listview);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        mProvinceList = new ArrayList<Province>();
        registerContentObserver();

        mProvinceListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ProvinceAdapter(mProvinceList, getActivity());
        mProvinceListView.setAdapter(mAdapter);
        getProvinceDataFromDB();
        setOnItemClickListener();
        return root;
    }

    private void setOnItemClickListener() {
        mAdapter.setOnItemClickListener(new ProvinceAdapter.ItemClickListener() {
            @Override
            public void onClick(int provinceCode) {
                Intent intent = new Intent(getActivity(), CityActivity.class);
                intent.putExtra("province_code", provinceCode);
                startActivity(intent);
            }
        });
    }


    private static final String PROVINCE_PROVIDER_URI = "content://com.example.gitdemo.provider/province";

    private void registerContentObserver(){
        getActivity().getContentResolver().registerContentObserver(Uri.parse(PROVINCE_PROVIDER_URI), true, new ContentObserver(mHandler) {
            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                Log.d("provider", "onChange >>>>>>>>>>>>>>>>>>>>>>>");
                mHandler.removeMessages(0);
                mHandler.sendEmptyMessageDelayed(0,1500);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}