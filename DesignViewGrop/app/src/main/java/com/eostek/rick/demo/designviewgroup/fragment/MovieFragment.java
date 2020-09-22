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
 * Created by a on 17-6-29.
 */

public class MovieFragment extends BaseFragment {

    private ViewPager mViewPager;

    private int[] mImgIds = new int[]{R.drawable.guide_movie1,
            R.drawable.guide_movie2, R.drawable.guide_movie3, R.drawable.guide_movie4b};

    private String[] strings = {"好莱坞巨制", "经典佳片", "浪漫迪士尼", "日韩催泪"};
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
        final View movieView = inflater.inflate(R.layout.movie_fragment, container, false);
        init(movieView);
        return movieView;
    }


    private void init(View view) {
        mViewPager = (ViewPager) view.findViewById(R.id.top_recommend);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setPageMargin(10);
        for (int i = 0; i < 4; i++) {
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
            mViewPager.setPageTransformer(false, new ScaleTransformer1());
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

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    @Override
    public void onStart() {

        super.onStart();
    }

    @Override
    public void onStop() {

        super.onStop();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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

    public static class ScaleTransformer1 implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.90f;
        private static final float MIN_ALPHA = 0.5f;

        @Override
        public void transformPage(View page, float position) {
            if (position <= -1 || position >= 1) {
                page.setAlpha(MIN_ALPHA);
                page.setScaleX(MIN_SCALE);
                page.setScaleY(MIN_SCALE);
            } else if (position < 1) { // [-1,1]
                float scaleFactor = 1 - Math.abs(position)*(1 - MIN_ALPHA);
                if (position < 0) {
                    float scaleX = 1 - Math.abs(position)*(1 - MIN_SCALE);
                    page.setScaleX(scaleX);
                    page.setScaleY(scaleX);
                } else {
                    float scaleX = 1 - Math.abs(position)*(1 - MIN_SCALE);
                    page.setScaleX(scaleX);
                    page.setScaleY(scaleX);
                }
                page.setAlpha(scaleFactor);
                //page.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
            }
        }
    }


    public class ScaleTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.90f;
        private static final float MIN_ALPHA = 0.6f;

        @Override
        public void transformPage(View page, float position) {
            if (position < -1 || position > 1) {
                page.setAlpha(MIN_ALPHA);
                page.setScaleX(MIN_SCALE);
                page.setScaleY(MIN_SCALE);
            } else if (position <= 1) { // [-1,1]
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                if (position < 0) {
                    float scaleX = 1 + 0.3f * position;
                    page.setScaleX(scaleX);
                    page.setScaleY(scaleX);
                } else {
                    float scaleX = 1 - 0.3f * position;
                    page.setScaleX(scaleX);
                    page.setScaleY(scaleX);
                }
                page.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
            }
        }
    }

    public class DepthPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1);
                view.setTranslationX(0);
                view.setScaleX(1);
                view.setScaleY(1);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }


}
