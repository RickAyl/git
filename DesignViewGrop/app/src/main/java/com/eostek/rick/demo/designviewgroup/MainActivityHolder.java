package com.eostek.rick.demo.designviewgroup;

import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import com.eostek.rick.demo.designviewgroup.fragment.CartoonFragment;
import com.eostek.rick.demo.designviewgroup.fragment.ImagViewFragment;
import com.eostek.rick.demo.designviewgroup.fragment.MovieFragment;
import com.eostek.rick.demo.designviewgroup.fragment.TvShowFragment;
import com.eostek.rick.demo.designviewgroup.fragment.ZhouStarFragment;

/**
 * Created by a on 17-6-29.
 */

public class MainActivityHolder {

    private static final String TAG = MainActivityHolder.class.getSimpleName();

    public static String[] viewlist = {"美图", "电影", "剧集", "动漫", "鬼畜", "记录", "直播"};

    private FragmentActivity mActivity;

    private MainFragmentPageAadpter mAdapter;

    private DrawerLayout mDrawerLayout;

    private TabLayout mTabLayout;

    private Toolbar mToolbar;

    private ViewPager mViewPager;

    private List<Fragment> mFragmentsList;

    private ImagViewFragment mImageViewFragment;

    private MovieFragment mMovieFragment;

    private TvShowFragment mTvShowFragment;

    private CartoonFragment mCartoonFragment;

    private ZhouStarFragment mZhouStarFragment;

    private Handler mHandler;

    public FragmentManager mFragmentManager;

    private int mCurrentPosition = 0;


    public MainActivityHolder(FragmentActivity activity, Handler handler) {
        this.mActivity = activity;
        this.mHandler = handler;
        onCreat();
    }


    private void onCreat() {
        initView();
        initViewPager();
    }

    private void initView() {
        mFragmentManager = mActivity.getSupportFragmentManager();

        mDrawerLayout = (DrawerLayout) mActivity.findViewById(R.id.drawerLayout);

        mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        mTabLayout = (TabLayout) mActivity.findViewById(R.id.tabLayout);
        mViewPager = (ViewPager) mActivity.findViewById(R.id.main_view_pager);
        setToolTitle();
        addTab();
        setListener();
    }

    private void setToolTitle() {
        mToolbar.setNavigationIcon(R.mipmap.ic_drawer_home);
        mToolbar.setTitleTextAppearance(mActivity, R.style.AppTheme_PopupOverlay);
        mToolbar.setSubtitleTextAppearance(mActivity, R.style.AppTheme_PopupOverlay);
        mToolbar.setTitleMarginStart(90);
        mToolbar.setSubtitleTextColor(Color.WHITE);
        mToolbar.inflateMenu(R.menu.app_bar_menu);
        //mToolbar.setNavigationIcon(R.mipmap.ic_launcher);
        //setSupportActionBar(mToolbar);

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (mCurrentPosition == 0) {
                    if (!TextUtils.isEmpty(item.getTitle()) && item.getTitle().equals("delete")) {
                        mImageViewFragment.getGridAdapter().removeItem(0);
                    }
                }
                return false;

            }
        });
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void addTab() {
        mTabLayout.setSelectedTabIndicatorColor(Color.WHITE);
        mTabLayout.addTab(mTabLayout.newTab().setText("美图"));
        mTabLayout.addTab(mTabLayout.newTab().setText("电影"));
        mTabLayout.addTab(mTabLayout.newTab().setText("剧集"));
        mTabLayout.addTab(mTabLayout.newTab().setText("动漫"));
        mTabLayout.addTab(mTabLayout.newTab().setText("鬼畜"));
        mTabLayout.addTab(mTabLayout.newTab().setText("记录"));
        mTabLayout.addTab(mTabLayout.newTab().setText("直播"));

    }

    private void setListener() {

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                showCurrentSelectedPage(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.v(TAG, "position : " + position);
                Log.v(TAG, "positionOffset : " + positionOffset);
                Log.v(TAG, "positionOffsetPixels : " + positionOffsetPixels);
                //TabLayout可以滑动，通过源码得知TabLayout
                // 滑动会回调TabLayoutOnPageChangeListener
                // 的onPageScrolled调用了setScrollPosition
                //故我们在viewpager的滑动监听中调用setScrollPosition就可实现拖动
                //viewpager时，tab下划线一起滑动
                mTabLayout.setScrollPosition(position, positionOffset, false);

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPosition = position;
                setSelectedTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void showCurrentSelectedPage(int position) {
        if (mViewPager != null && mAdapter.getCount() > position) {
            mCurrentPosition = position;
            mViewPager.setCurrentItem(position);
        }
    }

    private void setSelectedTab(int position) {
        mTabLayout.getTabAt(position).select();
    }

    private void initViewPager() {
        if (mImageViewFragment == null) {
            mImageViewFragment = new ImagViewFragment();
        }
        if (mMovieFragment == null) {
            mMovieFragment = new MovieFragment();
        }
        if (mTvShowFragment == null) {
            mTvShowFragment = new TvShowFragment();
        }
        if (mCartoonFragment == null) {
            mCartoonFragment = new CartoonFragment();
        }
        if (mZhouStarFragment == null) {
            mZhouStarFragment = new ZhouStarFragment();
        }
        if (mFragmentsList == null) {
            mFragmentsList = new ArrayList<Fragment>();
        } else {
            mFragmentsList.clear();
        }
        mFragmentsList.add(mImageViewFragment);
        mFragmentsList.add(mMovieFragment);
        mFragmentsList.add(mTvShowFragment);
        mFragmentsList.add(mCartoonFragment);
        mFragmentsList.add(mZhouStarFragment);

        if (mAdapter == null) {
            mAdapter = new MainFragmentPageAadpter(mFragmentManager);
        }

        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(mFragmentsList.size());
    }

    class MainFragmentPageAadpter extends FragmentPagerAdapter {


        public MainFragmentPageAadpter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentsList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentsList.size();
        }


    }

    public boolean isOpenedLeftMenu(){
        return mDrawerLayout.isDrawerOpen(Gravity.LEFT);
    }

    public void closeLeftMenu(){
        mDrawerLayout.closeDrawer(Gravity.LEFT);
    }

}
