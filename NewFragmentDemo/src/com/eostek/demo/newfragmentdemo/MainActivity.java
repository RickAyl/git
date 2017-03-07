
package com.eostek.demo.newfragmentdemo;

import java.util.ArrayList;
import java.util.List;

import com.eostek.demo.newfragmentdemo.R;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.View.OnKeyListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements OnPageChangeListener {

    private ViewPager mViewPager;

    private MyFragmentPagerAdapter mFragmentViewPageAdapter;

    /**
     * 用于展示消息的Fragment
     */
    private MessageFragment messageFragment;

    /**
     * 用于展示联系人的Fragment
     */
    private ContactsFragment contactsFragment;

    /**
     * 用于展示动态的Fragment
     */
    private NewsFragment newsFragment;

    /**
     * 用于展示设置的Fragment
     */
    private SettingFragment settingFragment;

    private List<Fragment> mFragmentList;

    /**
     * 消息界面布局
     */
    private View messageLayout;

    /**
     * 联系人界面布局
     */
    private View contactsLayout;

    /**
     * 动态界面布局
     */
    private View newsLayout;

    /**
     * 设置界面布局
     */
    private View settingLayout;

    private List<View> layoutList;

    /**
     * 在Tab布局上显示消息图标的控件
     */
    private ImageView messageImage;

    /**
     * 在Tab布局上显示联系人图标的控件
     */
    private ImageView contactsImage;

    /**
     * 在Tab布局上显示动态图标的控件
     */
    private ImageView newsImage;

    /**
     * 在Tab布局上显示设置图标的控件
     */
    private ImageView settingImage;

    /**
     * 在Tab布局上显示消息标题的控件
     */
    private TextView messageText;

    /**
     * 在Tab布局上显示联系人标题的控件
     */
    private TextView contactsText;

    /**
     * 在Tab布局上显示动态标题的控件
     */
    private TextView newsText;

    /**
     * 在Tab布局上显示设置标题的控件
     */
    private TextView settingText;

    private Handler mHandler = new Handler();

    private static int mPosition = 0;

    // private FragmentManager fragmentManager;

    private android.support.v4.app.FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        // 初始化布局元素
        initViewPager();
        mFragmentManager = getSupportFragmentManager();
        mFragmentViewPageAdapter = new MyFragmentPagerAdapter(mFragmentManager);
        initViews();
        addToLayoutList();
        setListener();
        // 第一次启动时选中第0个tab
        setTabSelection(0);

    }

    private void initViewPager() {
        if (messageFragment == null) {
            messageFragment = new MessageFragment();
        }
        if (contactsFragment == null) {
            contactsFragment = new ContactsFragment();
        }
        if (newsFragment == null) {
            newsFragment = new NewsFragment();
        }
        if (settingFragment == null) {
            settingFragment = new SettingFragment();
        }
        if (mFragmentList != null) {
            mFragmentList.clear();
        } else {
            mFragmentList = new ArrayList<Fragment>();
        }
        mFragmentList.add(messageFragment);
        mFragmentList.add(contactsFragment);
        mFragmentList.add(newsFragment);
        mFragmentList.add(settingFragment);
    }

    /**
     * 在这里获取到每个需要用到的控件的实例，并给它们设置好必要的点击事件。
     */
    private void initViews() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        messageLayout = findViewById(R.id.message_layout);
        contactsLayout = findViewById(R.id.contacts_layout);
        newsLayout = findViewById(R.id.news_layout);
        settingLayout = findViewById(R.id.setting_layout);
        messageImage = (ImageView) findViewById(R.id.message_image);
        contactsImage = (ImageView) findViewById(R.id.contacts_image);
        newsImage = (ImageView) findViewById(R.id.news_image);
        settingImage = (ImageView) findViewById(R.id.setting_image);
        messageText = (TextView) findViewById(R.id.message_text);
        contactsText = (TextView) findViewById(R.id.contacts_text);
        newsText = (TextView) findViewById(R.id.news_text);
        settingText = (TextView) findViewById(R.id.setting_text);
        
    }

    private void setListener() {
        setViewFocusableTrue();
        setOnClickListener();
        setOnKeyListener();
        setOnFocusChangeListener();
        mViewPager.setAdapter(mFragmentViewPageAdapter);

        mViewPager.setOnPageChangeListener(this);

        mHandler.post(new Runnable() {

            @Override
            public void run() {
                layoutList.get(0).requestFocus();
            }
        });
    }

    private void addToLayoutList() {
        if (layoutList != null) {
            layoutList.clear();
        } else {
            layoutList = new ArrayList<View>();
        }
        layoutList.add(messageLayout);
        layoutList.add(contactsLayout);
        layoutList.add(newsLayout);
        layoutList.add(settingLayout);

    }

    private void setViewFocusableTrue() {
        for (View layout : layoutList) {
            layout.setFocusable(true);
        }
    }

    private void setOnClickListener() {
        MyOnClickListener onClickListener = new MyOnClickListener();
        for (View layout : layoutList) {
            layout.setOnClickListener(onClickListener);
        }
    }

    private void setOnFocusChangeListener() {
        MyFocusChangeListener onFocusChangListener = new MyFocusChangeListener();
        for (View layout : layoutList) {
            layout.setOnFocusChangeListener(onFocusChangListener);
        }
    }

    private void setOnKeyListener() {
        MyOnKeyListener onKeyListener = new MyOnKeyListener();
        // messageLayout.setOnKeyListener(onKeyListener);
        for (View layout : layoutList) {
            layout.setOnKeyListener(onKeyListener);
        }
        mViewPager.setOnKeyListener(onKeyListener);
    }

    public void showSpecifiedPage(int index) {
        if (mViewPager != null && index >= 0) {
            try {
                mViewPager.setCurrentItem(index);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class MyOnClickListener implements OnClickListener {

        @Override
        public void onClick(View view) {
            // TODO Auto-generated method stub
            switch (view.getId()) {
                case R.id.message_layout:
                    setTabSelection(0);
                    showSpecifiedPage(0);
                    break;
                case R.id.contacts_layout:
                    setTabSelection(1);
                    showSpecifiedPage(1);
                    break;
                case R.id.news_layout:
                    setTabSelection(2);
                    showSpecifiedPage(2);
                    break;
                case R.id.setting_layout:
                    setTabSelection(3);
                    showSpecifiedPage(3);
                    break;

                default:
                    break;
            }
            view.requestFocus();
        }

    }

    private class MyFocusChangeListener implements OnFocusChangeListener {

        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            // TODO Auto-generated method stub
            if (view.hasFocus()) {
                switch (view.getId()) {
                    case R.id.message_layout:
                        setTabSelection(0);
                        showSpecifiedPage(0);
                        if (mPosition != 0) {
                            mPosition = 0;
                        }
                        break;
                    case R.id.contacts_layout:
                        setTabSelection(1);
                        showSpecifiedPage(1);
                        if (mPosition != 1) {
                            mPosition = 1;
                        }
                        break;
                    case R.id.news_layout:
                        setTabSelection(2);
                        showSpecifiedPage(2);
                        if (mPosition != 2) {
                            mPosition = 2;
                        }
                        break;
                    case R.id.setting_layout:
                        setTabSelection(3);
                        showSpecifiedPage(3);
                        if (mPosition != 3) {
                            mPosition = 3;
                        }
                        break;

                    default:
                        break;
                }
            }
        }

    }

    private class MyOnKeyListener implements OnKeyListener {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            // TODO Auto-generated method stub
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_DPAD_LEFT:
                        if (layoutList.contains(v)) {
                            if (--mPosition < 0) {
                                mPosition = layoutList.size() - 1;
                                layoutList.get(mPosition).requestFocus();
                                return true;
                            }
                            setTabSelection(mPosition);
                            break;
                        } else if (v.getId() == R.id.viewpager) {
                            Log.v("rick", "v.getId() == R.id.viewpager");
                            if (--mPosition < 0) {
                                Log.v("rick", "mPosition < 0 : " + mPosition);
                                mPosition = layoutList.size() - 1;
                                showSpecifiedPage(mPosition);
                                setTabSelection(mPosition);
                                return true;
                            }
                            Log.v("rick", "mPosition >= 0 : " + mPosition);
                            setTabSelection(mPosition);
                        }
                    case KeyEvent.KEYCODE_DPAD_RIGHT:
                        if (layoutList.contains(v)) {
                            if (++mPosition > layoutList.size() - 1) {
                                mPosition = 0;
                                layoutList.get(mPosition).requestFocus();
                                return true;
                            }
                            setTabSelection(mPosition);
                            break;
                        } else if (v.getId() == R.id.viewpager) {
                            if (++mPosition > layoutList.size() - 1) {
                                mPosition = 0;
                                showSpecifiedPage(mPosition);
                                setTabSelection(mPosition);
                                return true;
                            }
                            setTabSelection(mPosition);
                        }
                    default:
                        break;
                }

            }
            return false;
        }

    }

    /**
     * 根据传入的index参数来设置选中的tab页。
     * 
     * @param index 每个tab页对应的下标。0表示消息，1表示联系人，2表示动态，3表示设置。
     */
    private void setTabSelection(int index) {
        // 每次选中之前先清楚掉上次的选中状态
        clearSelection();
        switch (index) {
            case 0:
                // 当点击了消息tab时，改变控件的图片和文字颜色
                messageImage.setImageResource(R.drawable.message_selected);
                messageText.setTextColor(Color.WHITE);
                break;
            case 1:
                // 当点击了联系人tab时，改变控件的图片和文字颜色
                contactsImage.setImageResource(R.drawable.contacts_selected);
                contactsText.setTextColor(Color.WHITE);
                break;
            case 2:
                // 当点击了动态tab时，改变控件的图片和文字颜色
                newsImage.setImageResource(R.drawable.news_selected);
                newsText.setTextColor(Color.WHITE);
                break;
            case 3:
            default:
                // 当点击了设置tab时，改变控件的图片和文字颜色
                settingImage.setImageResource(R.drawable.setting_selected);
                settingText.setTextColor(Color.WHITE);
                break;
        }
    }

    /**
     * 清除掉所有的选中状态。
     */
    private void clearSelection() {
        messageImage.setImageResource(R.drawable.message_unselected);
        messageText.setTextColor(Color.parseColor("#82858b"));
        contactsImage.setImageResource(R.drawable.contacts_unselected);
        contactsText.setTextColor(Color.parseColor("#82858b"));
        newsImage.setImageResource(R.drawable.news_unselected);
        newsText.setTextColor(Color.parseColor("#82858b"));
        settingImage.setImageResource(R.drawable.setting_unselected);
        settingText.setTextColor(Color.parseColor("#82858b"));
    }

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
            // TODO Auto-generated constructor stub
        }

        @Override
        public Fragment getItem(int position) {
            // TODO Auto-generated method stub
            Log.v("rick", "======> getItem : " + position);
            return mFragmentList.get(position % mFragmentList.size());
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mFragmentList.size();
        }

    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageSelected(int position) {
        // TODO Auto-generated method stub
        setTabSelection(position);
        mPosition = position;
    }
}
