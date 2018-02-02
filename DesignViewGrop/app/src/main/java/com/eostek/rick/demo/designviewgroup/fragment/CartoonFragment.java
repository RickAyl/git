package com.eostek.rick.demo.designviewgroup.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.eostek.rick.demo.designviewgroup.R;

/**
 * Created by rick on 2017/7/12.
 */

public class CartoonFragment extends BaseFragment {
    private ViewPager mViewPager;

    private int[] mImgIds = new int[]{R.drawable.guide_car1,
            R.drawable.guide_car2, R.drawable.guide_car3, R.drawable.guide_car4,R.drawable.guide_car5};

    private String[] strings = {"经典", "最新上线", "热血","国漫崛起","催泪治愈"};

    private List<View> mCardViews = new ArrayList<View>();

    private List<ImageView> views = new ArrayList<ImageView>();

    private RecommendVpAdater mAdapter;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View tvshowView = inflater.inflate(R.layout.tvshow_fragment,container,false);
        init(tvshowView);
        return  tvshowView;
    }


    private void init(View view) {
        mViewPager = (ViewPager) view.findViewById(R.id.top_recommend);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setPageMargin(10);
        for (int i = 0; i < strings.length; i++) {
            View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.star_cards_item, null);
            mCardViews.add(view1);
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter != null) {
        } else {
            mAdapter = new RecommendVpAdater(getActivity(), mCardViews);
            mViewPager.setAdapter(mAdapter);
            mViewPager.setPageTransformer(false, new MovieFragment.ScaleTransformer1());
        }

        try {
            Field field = mViewPager.getClass().getField("mCurItem");
            field.setAccessible(true);
            field.setInt(mViewPager, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 通过数据修改
        mAdapter.notifyDataSetChanged();

        // 切换到指定页面
        mViewPager.setCurrentItem(1);
    }

    public class RecommendVpAdater extends PagerAdapter {
        private List<View> list;
        private Context context;

        public RecommendVpAdater(Context context, List<View> list) {
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
        public float getPageWidth(int position) {
            return (float) 1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v = list.get(position);

            ImageView img = (ImageView) v.findViewById(R.id.pic);
            TextView tv = (TextView) v.findViewById(R.id.name);
            //img.setImageResource(mImgIds[position]);
            Glide.with(context).load(mImgIds[position]).crossFade(500).into(img);
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
