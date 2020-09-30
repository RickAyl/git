package com.example.gitdemo.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.example.gitdemo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class ViewPagerActivity extends AppCompatActivity {

    private ViewPager viewPager;

    private View view1, view2, view3;

    private List<View> viewList;

    private String[] s = new String[] {"page0", "page1", "page2"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        viewPager = findViewById(R.id.viewpager);

        view1 = getLayoutInflater().inflate(R.layout.weather_title, null);
        view2 = getLayoutInflater().inflate(R.layout.temp_layout, null);
        view3 = getLayoutInflater().inflate(R.layout.activity_main, null);

        viewList = new ArrayList<>();
        viewList.add(view1);
        viewList.add(view2);
        viewList.add(view3);

        viewPager.setAdapter(new MyViewPagerAdapter(this, viewList));
    }


    class MyViewPagerAdapter extends PagerAdapter {

        List<View> list;

        Context context;

        public MyViewPagerAdapter(@NonNull Context context, List list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }


        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            container.addView(list.get(position));
            return list.get(position);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(list.get(position));
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return s[position];
        }
    }
}