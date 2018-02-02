package com.eostek.rick.demo.designviewgroup.viewActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.eostek.rick.demo.designviewgroup.R;



public class Main2Activity extends Activity {

    private ViewPager mViewPager;

    private int[] mImgIds = new int[]{R.drawable.guide_movie1,
            R.drawable.guide_movie2, R.drawable.guide_movie3};

    private String[] strings = {"好莱坞巨制", "经典佳片", "浪漫迪士尼"};

    private List<CardView> mCardViews = new ArrayList<CardView>();

    private List<ImageView> views = new ArrayList<ImageView>();

    private RecommendVpAdater mAdapter;

    List<Integer> list = new ArrayList<>();

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d("rick", "===============================>");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_fragment);
        list.add(mImgIds[0]);
        list.add(mImgIds[1]);
        list.add(mImgIds[2]);
        init();
    }

    private void init() {
        mViewPager = (ViewPager) findViewById(R.id.top_recommend);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setPageMargin(30);
        MyVpAdater adater = new MyVpAdater(this, list);
        for (int i = 0; i < 3; i++) {
            View view1 = getLayoutInflater().inflate(R.layout.star_cards_item, null);
            Log.d("rick","view : "+ view1.toString() + "\r\n" + view1.getClass().getName());
            mCardViews.add((CardView) view1);
        }
        mAdapter = new RecommendVpAdater(this, mCardViews);
        mViewPager.setAdapter(mAdapter);
        //mViewPager.setAdapter(adater);
        /*mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mViewPager.setCurrentItem(1);
            }
        },1000);*/
        // 先强制设置到指定页面
        try {
            Field field = mViewPager.getClass().getField("mCurItem");
            field.setAccessible(true);
            field.setInt(mViewPager, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

// 通过数据修改
        adater.notifyDataSetChanged();

// 切换到指定页面
        mViewPager.setCurrentItem(1);
    }


    public class MyVpAdater extends PagerAdapter {
        private List<Integer> list;
        private Context context;

        public MyVpAdater(Context context, List<Integer> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView iv = new ImageView(context);
            iv.setImageResource(list.get(position));
            container.addView(iv);
            return iv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


    public class RecommendVpAdater extends PagerAdapter {
        private List<CardView> list;
        private Context context;

        public RecommendVpAdater(Context context, List<CardView> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v = list.get(position);
            ImageView img = (ImageView)v.findViewById(R.id.pic);
            TextView tv = (TextView)v.findViewById(R.id.name);
            img.setImageResource(mImgIds[position]);
            tv.setText(strings[position]);
            container.addView(v);
            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(list.get(position));
        }
    }
}
