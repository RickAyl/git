
package com.eostek.tv.launcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.XmlResourceParser;
import android.R.integer;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.eostek.tv.launcher.R;
import com.eostek.tv.launcher.business.database.DBManager;
import com.eostek.tv.launcher.business.listener.OnAppChangedListener;
import com.eostek.tv.launcher.model.JsonHeadBean;
import com.eostek.tv.launcher.model.MetroInfo;
import com.eostek.tv.launcher.model.MetroPage;
import com.eostek.tv.launcher.ui.adapter.MenuAdapter;
import com.eostek.tv.launcher.ui.adapter.SourceAdapter;
import com.eostek.tv.launcher.ui.adapter.TitleAdapter;
import com.eostek.tv.launcher.ui.adapter.ViewPagerAdapter;
import com.eostek.tv.launcher.ui.view.AppAddActivity;
import com.eostek.tv.launcher.ui.view.FocusView;
import com.eostek.tv.launcher.ui.view.ObservableScrollView;
import com.eostek.tv.launcher.ui.view.PagedGroup;
import com.eostek.tv.launcher.ui.view.ReflectImage;
import com.eostek.tv.launcher.ui.view.ReflectionTView;
import com.eostek.tv.launcher.util.LConstants;
import com.eostek.tv.launcher.util.TvUtils;
import com.eostek.tv.launcher.util.UIUtil;
import com.eostek.tv.launcher.util.ViewPositionUtil;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.mstar.android.tvapi.common.vo.TvOsType.EnumInputSource;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/*
 * projectName： TVLauncher
 * moduleName： ViewHolder.java
 *
 * @author chadm.xiang
 * @version 1.0.0
 * @time  2014-7-11 下午1:47:55
 * @Copyright © 2014 Eos Inc.
 */

public class ViewHolder {

    public final String TAG = ViewHolder.class.getSimpleName();
    
    public static int curPosition = 0;

    private final float ScaleFactor = 1.1f;

    private HomeActivity mContext;

    private Handler mHandler;

    ViewPager viewPager;

    private FocusView focusView;

    GridView titGridView;

    private ReflectionTView mRelativeTvView;

    private ListView mListView;

    private SurfaceView mSurfaceView;

    private ImageButton tvImageButton;

    private ImageView mLogoView;
    
    private ImageView mLeftView;

    private DBManager mDbManager;

    private TitleAdapter titleAdapter;

    private List<String> titleList;

    volatile ViewPagerAdapter vpAdapter;

    private ArrayList<View> pageViewsList;

    private List<MetroPage> mMetroPages = new ArrayList<MetroPage>();

    private JsonHeadBean mHeadBean;
    
    private Dialog mEnMenuDlg;

    private int lastSelect = -1;

    private volatile int mCurTitleIndex = 0;

    private int mFocusBorderGap;

    private int focusType;

    private boolean hasTV = false;
    
    private boolean languageChange = false;
    
    private final int[] EN_MENU_ICON_IDS = {
            R.drawable.remove_app, R.drawable.device_mgr, R.drawable.media_browser
    };
    
    private final int[] EN_MENU_TITLE_IDS = {
            R.string.apppage_menu_remove_app, R.string.apppage_menu_devicemanager,
            R.string.apppage_menu_mediabrowser
    };
    
    private OnAppChangedListener mAppChangedListener = new OnAppChangedListener() {


		@Override
		public void onEnAppAdd(MetroInfo appInfo) {
			if (vpAdapter != null) {
				vpAdapter.getPagedGroup(0).addEnAppView(appInfo);
				initTView();
            }
		}

		@Override
		public void onEnAppRemove(MetroInfo appInfo) {
			if (vpAdapter != null) {
				vpAdapter.getPagedGroup(0).removeEnAppView(appInfo);
            }
		}

		@Override
		public void onEnAppUninstall(String pkg) {
			if (vpAdapter != null) {
				vpAdapter.getPagedGroup(0).removeEnAppView(pkg);
            }
		}

		@Override
		public void onEnAppMoveCancel() {
			// TODO Auto-generated method stub
		}

    };

    public ViewHolder(Context context, Handler handler) {
        this.mContext = (HomeActivity) context;
        mHandler = handler;
        mDbManager = DBManager.getDbManagerInstance(HomeApplication.getInstance());
        hasTV = HomeApplication.isHasTVModule();
        focusType = HomeApplication.getFocusType();
        curPosition = TvUtils.getCurPositionBySource(TvUtils.queryCurInputSrc(mContext));
        
        DBManager.getDbManagerInstance(mContext).setAppChangedListener(mAppChangedListener);
        AppAddActivity.setAppChangedListener(mAppChangedListener);
    }
    
    public void updateItemView() {
        View view = mListView.getChildAt(0);
        if (view != null) {
            Log.v(TAG, "child index 0");
            view.findViewById(R.id.source_ll).setBackgroundResource(R.color.defaultsource_color);
        }
    }

    /**
     * find views from the xml
     */
    public void findViews() {
        titGridView = (GridView) mContext.findViewById(R.id.title_grid);
        mLogoView = (ImageView) mContext.findViewById(R.id.logo_image);
        mLogoView.setVisibility(View.VISIBLE);
        mLeftView = (ImageView) mContext.findViewById(R.id.left_image);
        pageViewsList = new ArrayList<View>();
        viewPager = (ViewPager) mContext.findViewById(R.id.viewpager);
        mFocusBorderGap = mContext.getResources().getInteger(R.integer.focus_border_gap);
//        if (UIUtil.getLanguage().equals("en-us")) {
//            titGridView.setVisibility(View.INVISIBLE);
//        }
        
        mLeftView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent("com.eostek.tv.SOURCE");
		    	mContext.sendBroadcast(intent);
			}
		});
    }

    /**
     * init view from page.xml or db.
     * 
     * @since V1.2.1
     */
    public void initDataFromLocal(String country) {
        mMetroPages.clear();
        mMetroPages = mDbManager.getMetroPages(country);
        mHeadBean = mDbManager.getJsonHeadBean(country);
        initViewData();
    }

    /**
     * init the view from network json data
     * 
     * @param metropages MetroPage list
     * @param headBean JsonHeadBean object
     * @param updateUI True to udpate data,false not
     */
    public void updateDataFromNetwork(List<MetroPage> metropages, JsonHeadBean headBean, boolean updateUI) {
        Log.v(TAG, "updateDataFromNetwork updateUI = " + updateUI);
        mMetroPages.clear();
        mMetroPages = metropages;
        mHeadBean = headBean;
        if (updateUI) {
            reloadViewData();
        }
    }

    public ReflectionTView getmRelativeTvView() {
        return mRelativeTvView;
    }

    public DBManager getmDbManager() {
        return mDbManager;
    }

    public int getmCurTitleIndex() {
        return mCurTitleIndex;
    }
    
    public void setCurTitleIndex(int index) {
        mCurTitleIndex = index;
    }

    /**
     * release all reflection bitmap,include image reflection and TView
     * reflection
     */
    public void releaseResources() {
        Log.v(TAG, "releaseResources");
        // release image reflection bitmap
        if (vpAdapter != null) {
            for (int i = 0; i < vpAdapter.getCount(); i++) {
                PagedGroup group = vpAdapter.getPagedGroup(i);
                group.releasePageReSource();
            }
            vpAdapter = null;
        }
        // release TView reflection bitmap
        if (mRelativeTvView != null) {
            mRelativeTvView.releaseTViewReflection();
            mRelativeTvView.removeAllViews();
            mRelativeTvView = null;
        }
    }

    /**
     * reload string resource when local change
     */
    public void reloadLocaleString() {
        initTitleGridView();
        if (hasTV && mRelativeTvView != null) {
            mRelativeTvView.notifiDateSetChange();
        }
    }

    /**
     * reload data from local db when locale change
     * 
     * @since V1.2.1
     */
    public void reloadLocalData() {
        // the db do not current country data
        languageChange = true;
        initDataFromLocal(UIUtil.getLanguage());
        if (mDbManager.isDBEmpty(UIUtil.getLanguage())) {
            reloadLocaleString();
        }

    }

    // get SurfaceView
    public SurfaceView getMsurfaceView() {
        return mSurfaceView;
    }

    /**
     * load all image resource
     */
    public void setImageResouce() {
        Log.v(TAG, "setImageResouce");
        int tmp = lastSelect;
        if (mMetroPages == null || mMetroPages.isEmpty()) {
            return;
        }
        long time = System.currentTimeMillis();
        // to speed load the all view, load the current page immediately,then
        // delay load the other pages
        if (tmp == -1) {
            setReflectResource(mMetroPages.get(0), 0);
            tmp = 0;
        } else {
            setReflectResource(mMetroPages.get(lastSelect), lastSelect);
        }
        Log.v(TAG, "load page " + tmp);
        for (int i = 0; i < mMetroPages.size(); i++) {
            if (tmp != i) {
                mHandler.postDelayed(new LoadImageRunnable(mMetroPages.get(i), i), i * 100);
            }
        }
        Log.v(TAG, "setImageResouce time = " + (System.currentTimeMillis() - time));
    }

    private void reloadViewData() {
        lastSelect = -1;
        // releaseResources();
        initViewData();
        if (hasTV) {
            // when udpate data from network,we need check source
            mContext.backHomeSource();
            titGridView.setSelection(0);
            mCurTitleIndex = 0;
        }
    }

    /**
     * init view data to show the UI interface
     */
    private void initViewData() {
        Log.v(TAG, "initViewData mCurTitleIndex = " + mCurTitleIndex);
        initLogo();
        // 1.init the title gridview
        initTitleGridView();
        // 2.init the viewpager
        initViewPager();
        // 3. set the default value
        focusView = vpAdapter.getPageFocusView(0);
        setTitleGridViewListener();
        // setTitleGridViewBackgroud(0);
        if (hasTV) {
            // after load all view,init the tv view and set tv listener
            initTView();
            mContext.mTvRunningStat = Settings.System.getInt(mContext.getContentResolver(), "tvplayer_visible", 0);
            if (UIUtil.getBooleanFromXml(mContext, "isLocaleChange")) {
                titGridView.setSelection(mCurTitleIndex);
                if (lastSelect == -1) {
                    lastSelect = 0;
                }
                viewPager.setCurrentItem(lastSelect);
                setTitleGridViewBackgroud(mCurTitleIndex);
            } else if (mContext.mTvRunningStat == 1) {
                titGridView.setSelection(0);
                mCurTitleIndex = 0;
            } else {
                titGridView.setSelection(0);
                mCurTitleIndex = 0;
            }
        } else {
            titGridView.setSelection(0);
            mCurTitleIndex = 0;
        }
        Log.v(TAG, "initViewData mCurTitleIndex = " + mCurTitleIndex);
    }

    private void initLogo() {
        // if in dangle,use the default picture
		if (LConstants.FEATURE_BLL_828.equals(UIUtil.getSpecialCode())) {
			if (UIUtil.getLanguage().equals("zh-cn")) {
				//mLogoView.setVisibility(View.VISIBLE);
				//mLogoView.setBackgroundResource(R.drawable.cibn_log);
			    mLogoView.setVisibility(View.GONE);
			} else {
				mLogoView.setVisibility(View.GONE);
			}
		}
        if (!hasTV) {
        	return;
        }
        if (mHeadBean != null) {
            final int left = mContext.getResources().getInteger(R.integer.logo_iv_margin_left);
            final int top = mContext.getResources().getInteger(R.integer.logo_iv_margin_top);
            if (TextUtils.isEmpty(mHeadBean.getLogoUrl())) {
            	if(UIUtil.getLanguage().equals("zh-cn")){
            		//mLogoView.setVisibility(View.VISIBLE);
            		//mLogoView.setBackgroundResource(R.drawable.cibn_log);
            	    mLogoView.setVisibility(View.GONE);
            	}else{
            		mLogoView.setVisibility(View.GONE);
            	}
                ViewPositionUtil.setViewLayout(mLogoView, left, top);
            } else {
                ImageLoader imageLoader = ImageLoader.getInstance();
                imageLoader.loadImage(mHeadBean.getLogoUrl(), new SimpleImageLoadingListener() {

                    @Override
                    public void onLoadingComplete(String requestUri, View view, Object dataObject) {
                        super.onLoadingComplete(requestUri, view, dataObject);
                        if (dataObject == null) {
                        	if(UIUtil.getLanguage().equals("zh-cn")){
                        		//mLogoView.setVisibility(View.VISIBLE);
                        		//mLogoView.setBackgroundResource(R.drawable.cibn_log);
                        	    mLogoView.setVisibility(View.GONE);
                        	}else{
                        		mLogoView.setVisibility(View.GONE);
                        	}
                            ViewPositionUtil.setViewLayout(mLogoView, left, top);
                        } else {
                            mLogoView.setImageBitmap((Bitmap) dataObject);
                            ViewPositionUtil.setViewLayout(mLogoView, mHeadBean.getLogoX(), mHeadBean.getLogoY());
                        }

                    }

                    @Override
                    public void onLoadingFailed(String requestUri, View view, FailReason failReason) {
                        super.onLoadingFailed(requestUri, view, failReason);
                    }

                    @Override
                    public void onLoadingStarted(String requestUri, View view) {
                        super.onLoadingStarted(requestUri, view);
                    }

                });
            }
        }
    }

    /**
     * init title gridview
     */
    private void initTitleGridView() {
        // init title gridview
        if (titleList == null) {
            titleList = new ArrayList<String>();
        } else {
            titleList.clear();
        }
        // only add my_tv when has TV module
        if (hasTV) {
            titleList.add(mContext.getResources().getString(R.string.my_tv));
        }

        for (int i = 0; i < mMetroPages.size(); i++) {
            titleList.add(mMetroPages.get(i).getTitle());
        }
        titleAdapter = new TitleAdapter(mContext, titleList);
        titGridView.setAdapter(titleAdapter);
    }

    /**
     * init viewpager,the main UI interface
     */
    private void initViewPager() {
        synchronized (viewPager) {
            viewPager.removeAllViews();
            releaseResources();
            // load all page view
            if (pageViewsList == null) {
                pageViewsList = new ArrayList<View>();
            } else {
                pageViewsList.clear();
            }
            for (int i = 0; i < titleList.size(); i++) {
                // only add my_tv when has TV module
                if (hasTV) {
                    if (i == 0) {
                        continue; // the first page is my tv,just skip
                    }
                    loadPageView(titleList.get(i), i - 1);
                } else {
                    loadPageView(titleList.get(i), i);
                }
            }
            vpAdapter = new ViewPagerAdapter(pageViewsList, mContext);
            vpAdapter.notifyDataSetChanged();
            viewPager.setAdapter(vpAdapter);
            viewPager.setOnPageChangeListener(new PageListener());
            setImageResouce();
        }
    }

    private class LoadImageRunnable implements Runnable {

        private MetroPage mPage;

        private int mPageNum;

        public LoadImageRunnable(MetroPage page, int num) {
            this.mPage = page;
            this.mPageNum = num;
        }

        /*
         * (non-Javadoc)
         * @see java.lang.Runnable#run()
         */
        @Override
        public void run() {
            setReflectResource(mPage, mPageNum);
            Log.v(TAG, "load page " + mPageNum);
        }

    }

    /**
     * load each page view
     * 
     * @param title The page title name
     * @param pageNum The current page num
     */
    private void loadPageView(String title, int pageNum) {
        List<MetroInfo> metroInfos = new ArrayList<MetroInfo>();
        metroInfos = mMetroPages.get(pageNum).getListInPage();

        LayoutInflater inflater = mContext.getLayoutInflater();
        View pageView = inflater.inflate(R.layout.page_layout, null);
        focusView = (FocusView) pageView.findViewById(R.id.focusview);
        ObservableScrollView hScrollView = (ObservableScrollView) pageView.findViewById(R.id.scroll);
        hScrollView.setPageNum(pageNum);
        // hScrollView.seton
        PagedGroup pagedGroup = (PagedGroup) pageView.findViewById(R.id.content);
        pagedGroup.setFocusView(focusView);
        pagedGroup.loadAllView(metroInfos, pageNum);
        pagedGroup.setScrollView(hScrollView);
        
        mMetroPages.get(pageNum).setList(pagedGroup.getMetroInfos());

        pageViewsList.add(pageView);
    }

    /**
     * set reflectImage resource
     * 
     * @param metroPage
     * @param pageNum
     */
    private void setReflectResource(MetroPage metroPage, int pageNum) {
        List<MetroInfo> metroInfos = metroPage.getListInPage();
        if (metroInfos != null) {
            PagedGroup group = vpAdapter.getPagedGroup(pageNum);
            // set pic for each view
            for (int j = 0; j < metroInfos.size(); j++) {
                ReflectImage vReflectLayout = (ReflectImage) group.getChildAt(j);
                String filePath = vReflectLayout.getMetroInfo().getIconPathB();
                
                Log.d("ahri","vReflectLayout.getMetroInfo().getX() =" + vReflectLayout.getMetroInfo().getX());
                if(vReflectLayout.getMetroInfo().getItemType() == 9 && !vReflectLayout.getMetroInfo().getClsName().equals(LConstants.CLS_ADD_ACTIVITY)){
               	 	// vReflectLayout.set
               	 	final PackageManager packageManager = mContext.getPackageManager();
               	 	final Intent intent = new Intent(Intent.ACTION_MAIN, null);
               	 	intent.addCategory(Intent.CATEGORY_LAUNCHER);
               	 	intent.setClassName(vReflectLayout.getMetroInfo().getPkgName(), vReflectLayout.getMetroInfo()
               	 			.getClsName());
               	 	List<ResolveInfo> tempInfos = packageManager.queryIntentActivities(intent, 0);
               	 	vReflectLayout.setImgResource(((BitmapDrawable) (tempInfos.get(0).loadIcon(packageManager)))
               	 			.getBitmap(), vReflectLayout.getMetroInfo().getTitle());
                }
                
                
                if (vReflectLayout.getMetroInfo().getItemType() == LConstants.METRO_ITEM_WIDGET) {
                    vReflectLayout.loadWidget(filePath, mContext.getAppWidgetManager(), mContext.getAppWidgetHost());
                } else {
                    if (TextUtils.isEmpty(filePath)) {
                        Log.e(TAG, " illeagle backgroud path " + filePath);
                        UIUtil.uploadNetworkDataError(mContext, "illeagle backgroud path");
                    } else {
                        int length = filePath.length();
                        int resId = UIUtil.getResourceId(mContext, filePath.substring(0, length - 4));
                        if (resId != 0) {
                            vReflectLayout.loadDrawable(resId);
                        } else {
                            vReflectLayout.loadHttp(filePath);
                        }
                    }
                }
            }
        }
    }

    private void initTView() {
        View mTvpageView = pageViewsList.get(0);
        PagedGroup pagedGroup = (PagedGroup) mTvpageView.findViewById(R.id.content);
        mRelativeTvView = pagedGroup.getTvViewRl();
        mRelativeTvView.isInitLV = true;
        mListView = mRelativeTvView.getSourceListView();
        boolean[] signalStatus = TvCommonManager.getInstance().GetInputSourceStatus();
        SourceAdapter sa = ((SourceAdapter) (mListView.getAdapter()));
        
        // 0041352: [India test] 在UI上面屏蔽DTV 功能
        if (LConstants.FEATURE_638.equals(UIUtil.getSpecialCode())) {
            // ATV do not support ,so do not change the ATV and DTV value
            if (signalStatus != null && signalStatus.length != 0) {
                sa.signalStatus_638[1] = signalStatus[EnumInputSource.E_INPUT_SOURCE_HDMI.ordinal()]; // HDMI1
                sa.signalStatus_638[2] = signalStatus[EnumInputSource.E_INPUT_SOURCE_HDMI2.ordinal()]; // HDMI2
                sa.signalStatus_638[3] = signalStatus[EnumInputSource.E_INPUT_SOURCE_HDMI3.ordinal()]; // HDMI3
                sa.signalStatus_638[4] = signalStatus[EnumInputSource.E_INPUT_SOURCE_CVBS.ordinal()]; // AV
                sa.signalStatus_638[5] = signalStatus[EnumInputSource.E_INPUT_SOURCE_YPBPR.ordinal()]; // YPBPR
                sa.signalStatus_638[6] = signalStatus[EnumInputSource.E_INPUT_SOURCE_VGA.ordinal()]; // VGA
            } else {
                Log.i(TAG, "GetInputSourceStatus return empty");
                for (int i = 1; i < sa.signalStatus_638.length; i++) {
                    sa.signalStatus_638[i] = false;
                }
            }
        } else if(LConstants.FEATURE_LX828.equals(UIUtil.getSpecialCode())){
        	// only support hdmi1,hdmi2
            if (signalStatus != null && signalStatus.length != 0) {
                sa.signalStatus_638[0] = signalStatus[EnumInputSource.E_INPUT_SOURCE_HDMI2.ordinal()]; // HDMI1
                sa.signalStatus_638[1] = signalStatus[EnumInputSource.E_INPUT_SOURCE_HDMI3.ordinal()]; // HDMI2
            } else {
                Log.i(TAG, "GetInputSourceStatus return empty");
                for (int i = 1; i < sa.signalStatus_638.length; i++) {
                    sa.signalStatus_LX828[i] = false;
                }
            }
        } else {
        	boolean  hasTuner = true;
        	/*try {
                hasTuner = TvManager.getInstance().getFactoryManager().getTunerStatus();
                Log.i("SourceAdapter", String.valueOf(hasTuner));
            } catch (TvCommonException e) {
                e.printStackTrace();
            }*/
            if (!hasTuner) {
            	 if (signalStatus != null && signalStatus.length != 0) {
                     sa.signalStatus_notuner[0] = signalStatus[EnumInputSource.E_INPUT_SOURCE_HDMI.ordinal()]; // HDMI1
                     sa.signalStatus_notuner[1] = signalStatus[EnumInputSource.E_INPUT_SOURCE_HDMI2.ordinal()]; // HDMI2
                     sa.signalStatus_notuner[2] = signalStatus[EnumInputSource.E_INPUT_SOURCE_HDMI3.ordinal()]; // HDMI3
                     sa.signalStatus_notuner[3] = signalStatus[EnumInputSource.E_INPUT_SOURCE_CVBS.ordinal()]; // AV
                     sa.signalStatus_notuner[4] = signalStatus[EnumInputSource.E_INPUT_SOURCE_YPBPR.ordinal()]; // YPBPR
                     sa.signalStatus_notuner[5] = signalStatus[EnumInputSource.E_INPUT_SOURCE_VGA.ordinal()]; // VGA
                 } else {
                     Log.i(TAG, "GetInputSourceStatus return empty");
                     for (int i = 0; i < sa.signalStatus_notuner.length; i++) {
                         sa.signalStatus_notuner[i] = false;
                     }
                 }
            }else{
            	// ATV and DTV do not support ,so do not change the ATV and DTV value
            	if (signalStatus != null && signalStatus.length != 0) {
            		//sa.signalStatus_hastuner[2] = signalStatus[EnumInputSource.E_INPUT_SOURCE_HDMI.ordinal()]; // HDMI1
            		sa.signalStatus_hastuner[2] = signalStatus[EnumInputSource.E_INPUT_SOURCE_HDMI2.ordinal()]; // HDMI2
            		sa.signalStatus_hastuner[3] = signalStatus[EnumInputSource.E_INPUT_SOURCE_HDMI3.ordinal()]; // HDMI3
            		sa.signalStatus_hastuner[4] = signalStatus[EnumInputSource.E_INPUT_SOURCE_CVBS.ordinal()]; // AV
            		sa.signalStatus_hastuner[5] = signalStatus[EnumInputSource.E_INPUT_SOURCE_YPBPR.ordinal()]; // YPBPR
            		sa.signalStatus_hastuner[6] = signalStatus[EnumInputSource.E_INPUT_SOURCE_VGA.ordinal()]; // VGA
            	} else {
            		Log.i(TAG, "GetInputSourceStatus return empty");
            		for (int i = 2; i < sa.signalStatus_hastuner.length; i++) {
            			sa.signalStatus_hastuner[i] = false;
            		}
            	}
            }
        }

        mSurfaceView = mRelativeTvView.getSurfaceView();
        tvImageButton = mRelativeTvView.getTvImageButton();

        TViewOnKeyListener onKeyListener = new TViewOnKeyListener();
        TViewOnFocusLisener onFocusLisener = new TViewOnFocusLisener();

        tvImageButton.setOnKeyListener(onKeyListener);
        tvImageButton.setOnFocusChangeListener(onFocusLisener);
        tvImageButton.setOnClickListener(new TvImageButtonOnclickListener());

        mListView.setOnFocusChangeListener(onFocusLisener);
        mListView.setOnItemClickListener(new SouceItemclickListener());
        mListView.setOnKeyListener(onKeyListener);
        mHandler.sendEmptyMessageDelayed(LConstants.REFRESH_LISTVIEW_ITEM, 20);
    }

    /**
     * set the backgroud and text color for the textview in gridview given
     * position
     * 
     * @param position
     */
    void setTitleGridViewBackgroud(int position) {
        Log.v(TAG, "setTitleGridViewBackgroud -->position = " + position + "; mCurTitleIndex = " + mCurTitleIndex);
        if (hasTV) {
            if (position == 1 && mCurTitleIndex == 0 && mRelativeTvView != null) {
                UIUtil.viewScaleDown(mContext, mRelativeTvView, ScaleFactor, ScaleFactor);
            }
        }
        mHandler.removeCallbacks(sourceRunnable);
        for (int i = 0; i < titGridView.getChildCount(); i++) {
            TextView textView = (TextView) titGridView.getChildAt(i).findViewById(R.id.title);
            textView.setTextColor(Color.parseColor("#80FFFFFF"));
            if (i == position) {
                textView.setBackgroundResource(R.drawable.gridview_title_bg);
                if (titGridView.hasFocus()) {
                    textView.setTextColor(Color.parseColor("#FFFFFFFF"));
                }
            } else {
                textView.setBackgroundResource(R.color.transparent);
            }
        }
        if (mCurTitleIndex != position) {
            mHandler.postDelayed(sourceRunnable, 10);
        } else {
            if (languageChange) {
                languageChange = false;
                mHandler.postDelayed(sourceRunnable, 10);
                Log.i(TAG, "disable mute when language change");
            }
        }
        mCurTitleIndex = position;
    }

    private Runnable sourceRunnable = new Runnable() {

        @Override
        public void run() {
            if (hasTV) {
                if (mCurTitleIndex == 0) {
                    EnumInputSource curSource = TvCommonManager.getInstance().getCurrentInputSource();
                    if (curSource == EnumInputSource.E_INPUT_SOURCE_STORAGE) {
                        curSource = EnumInputSource.values()[TvUtils.queryCurInputSrc(HomeApplication.getInstance())];
                    }
                    mContext.toChangeInputSource = curSource;
                    mHandler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            mSurfaceView.setBackgroundColor(Color.TRANSPARENT);
                        }
                    }, 300);
                    TvUtils.pageChangeMute(mContext, false);
                } else {
                    TvUtils.pageChangeMute(mContext, true);
                    mSurfaceView.setBackgroundColor(Color.BLACK);
                }
            }
        }
    };

    /**
     * set the listener for title gridview
     */
    private void setTitleGridViewListener() {
        if (hasTV) {
            setGridViewListenerHasTV();
        } else {
            setGridViewListenerWithoutTV();
        }

        titGridView.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Log.v(TAG, "titGridView onFocusChange gain focus");
                    titGridView.setSelection(mCurTitleIndex);
                    focusView.setVisibility(View.GONE);
                } else {
                    if (focusType == LConstants.FOCUS_TYPE_STATIC) {
                        focusView.setVisibility(View.GONE);
                    } else if (focusType == LConstants.FOCUS_TYPE_DYNAMIC) {
                        focusView.setVisibility(View.VISIBLE);
                    }
                }
                setTitleGridViewBackgroud(mCurTitleIndex);
            }
        });
    }

    private void setGridViewListenerHasTV() {
        titGridView.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.v(TAG, "onItemSelected postion = " + position);
                if (position == 0) {
                    if (viewPager.getCurrentItem() != 0) {
                        viewPager.setCurrentItem(0);
                    } else {
                        vpAdapter.scrollToOrigin(0);
                    }
                } else if (position == 1) {
                    if (viewPager.getCurrentItem() != 0) {
                        viewPager.setCurrentItem(0);
                    } else {
                        if (vpAdapter.getScrollDistance(0) == 0) {
                            vpAdapter.scrollTv(0);
                        }
                    }
                } else {
                    if (viewPager.getCurrentItem() != position - 1) {
                        viewPager.setCurrentItem(position - 1);
                    }
                }
                setTitleGridViewBackgroud(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        titGridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    if (viewPager.getCurrentItem() != 0) {
                        viewPager.setCurrentItem(0);
                    } else {
                        vpAdapter.scrollToOrigin(0);
                    }
                } else if (position == 1) {
                    if (viewPager.getCurrentItem() != 0) {
                        viewPager.setCurrentItem(0);
                    } else {
                        vpAdapter.scrollTv(0);
                    }
                } else {
                    viewPager.setCurrentItem(position - 1);
                }
                setTitleGridViewBackgroud(position);
                if (!titGridView.hasFocus()) {
                    mListView.clearFocus();
                    titGridView.requestFocus();
                }
                Log.v(TAG, "onItemClick postion = " + position);
            }
        });
    }

    private void setGridViewListenerWithoutTV() {
        titGridView.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.v(TAG, "onItemSelected postion = " + position);
                viewPager.setCurrentItem(position);
                setTitleGridViewBackgroud(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        titGridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v(TAG, "onItemClick");
                viewPager.setCurrentItem(position);
                setTitleGridViewBackgroud(position);
            }
        });
    }

    private void removeEnApp(MetroInfo appInfo) {
    	Log.d("ahri", "appInfo.getPositionId() =" + appInfo.getPositionId());
        DBManager.getDbManagerInstance(mContext).removeEnAppFromPage(appInfo);
    }
    
    /**
     * whether to show apppage menu while lacal is en-us.
     * 
     * @return
     */
    public boolean isShowEnAppPageMenu() {
        if(UIUtil.getLanguage().equals("en-us")){
        	PagedGroup appPage = vpAdapter.getPagedGroup(0);
        	View child = appPage.getFocusedChild();
        	if (child instanceof ReflectImage) {
        		ReflectImage hoverView = (ReflectImage) child;
        		MetroInfo info = hoverView.getMetroInfo();
        		if(info.getItemType() == 9){
        			return true;
        		}
        	}
        }
        return false;
    }
    
    /**
     * show apppage menu
     */
    public void showEnAppPageMenu() {
    	mEnMenuDlg = new Dialog(mContext, R.style.DialogFullscreen);
    	mEnMenuDlg.setContentView(R.layout.apppage_menu_dlg);
        MenuAdapter adapter = new MenuAdapter(mContext, EN_MENU_ICON_IDS, EN_MENU_TITLE_IDS);
        OnItemClickListener listener = new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
                switch (position) {
                    case 0:
                    	
                    	List<String> sysAppList  = getAPPTitleFromXml(mContext);
                    	if(sysAppList.contains(vpAdapter
                                .getPagedGroup(0).getFocusedItem().getTitle())){
                    		new AlertDialog.Builder(mContext)
                            .setMessage(
                                    String.format(mContext.getString(R.string.app_remove), vpAdapter
                                            .getPagedGroup(0).getFocusedItem().getTitle()) + 
                                            mContext.getResources().getString(R.string.apppage_menu_remove_sys_app))
                            .setPositiveButton(android.R.string.ok, new android.content.DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                	removeEnApp(vpAdapter.getPagedGroup(0).getFocusedItem());
                                }

                            }).setNegativeButton(android.R.string.cancel, null).show();
                    		
                    	}else{
                    		new AlertDialog.Builder(mContext)
                            .setMessage(
                                    String.format(mContext.getString(R.string.app_remove), vpAdapter
                                            .getPagedGroup(0).getFocusedItem().getTitle()))
                            .setPositiveButton(android.R.string.ok, new android.content.DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                	Log.d("ahri", "removeEnApp PositionId =" + vpAdapter.getPagedGroup(0).getFocusedItem().getPositionId());
                                	removeEnApp(vpAdapter.getPagedGroup(0).getFocusedItem());
                                }

                            }).setNegativeButton(android.R.string.cancel, null).show();
                    	}
                    	
                        break;
                    case 1:
                        UIUtil.startApk(mContext, LConstants.PKG_DEVICEMANAGER, LConstants.CLS_DEVICEMANAGER);
                        break;
                    case 2:
                        UIUtil.startApk(mContext, LConstants.PKG_MEDIABROWSER, LConstants.CLS_MEDIABROWSER);
                        break;
                    default:
                        break;
                }
                mEnMenuDlg.dismiss();
            }
        };

        ((ListView) (mEnMenuDlg.findViewById(R.id.apppage_menu_lv))).setAdapter(adapter);
        ((ListView) (mEnMenuDlg.findViewById(R.id.apppage_menu_lv))).setOnItemClickListener(listener);
        mEnMenuDlg.show();

    }
    
    private class PageListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        /**
         */
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // Log.v(TAG_LISTENER, "onPageScrolled = " + arg1);
        }

        @Override
        public void onPageSelected(int position) {
            focusView = vpAdapter.getPageFocusView(position);
            focusView.setVisibility(View.GONE);
            if (lastSelect == -1) {
                lastSelect = 0;
            }
            Log.i(TAG, "position======="+position);
            if(position == 0){
            	mLeftView.setVisibility(View.VISIBLE);
            }else{
            	mLeftView.setVisibility(View.GONE);
            }
            
            if (hasTV) {
                if (position == 0) {
                    if (vpAdapter.getScrollDistance(position) == 0) {
                        setTitleGridViewBackgroud(0);
                    } else {
                        setTitleGridViewBackgroud(1);
                    }
                } else {
                    setTitleGridViewBackgroud(position + 1);
                }
            } else {
                setTitleGridViewBackgroud(position);
            }

            Log.v(TAG, "lastSelect = " + lastSelect + "; position = " + position);
            lastSelect = position;

        }
    }

    private class TViewOnFocusLisener implements OnFocusChangeListener {

        /*
         * (non-Javadoc)
         * @see
         * android.view.View.OnFocusChangeListener#onFocusChange(android.view
         * .View, boolean)
         */
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                Log.v(TAG, "TViewOnkeyListener onFocusChange ");
                focusView.setVisibility(View.GONE);
                if (mCurTitleIndex != 0) {
                    setTitleGridViewBackgroud(0);
                }
                vpAdapter.scrollToOrigin(0);
                if (v.getId() == R.id.tv_image) {
                    if (focusType == LConstants.FOCUS_TYPE_STATIC) {
                        tvImageButton.setBackgroundResource(R.drawable.imagebutton_focus_border);
                    } else if (focusType == LConstants.FOCUS_TYPE_DYNAMIC) {
                        Rect rect = new Rect();
                        tvImageButton.getGlobalVisibleRect(rect);
                        if (rect.top > mContext.getResources().getInteger(R.integer.view_holder_off)) {
                            rect.top -= mContext.getResources().getInteger(R.integer.view_holder_off);
                            rect.bottom -= mContext.getResources().getInteger(R.integer.view_holder_off);
                        }
                        rect.left -= mFocusBorderGap;
                        rect.top -= mFocusBorderGap;
                        rect.bottom += mFocusBorderGap;
                        rect.right += mFocusBorderGap;
                        focusView.startAnimation(rect, false);
                    }
                    if (null != mRelativeTvView.tmpView) {
                        UIUtil.viewScaleDown(mContext, mRelativeTvView.tmpView, ScaleFactor, ScaleFactor);
                        mRelativeTvView.tmpView = null;
                    }
                    
                    View view = mListView.getChildAt(0);
                    if (view != null) {
                        view.findViewById(R.id.source_ll).setBackgroundResource(R.color.defaultsource_color);
                    }
                    
                } else if (v.getId() == R.id.source_list) {
                    if (null != mRelativeTvView.tmpView) {
                        UIUtil.viewScaleDown(mContext, mRelativeTvView.tmpView, ScaleFactor, ScaleFactor);
                    }
                    if (null != mListView.getSelectedView()) {
                        UIUtil.viewScaleUp(mContext, mListView.getSelectedView(), ScaleFactor, ScaleFactor);
                    }
                    mRelativeTvView.tmpView = mListView.getSelectedView();
                    if (mListView.getSelectedView() != null) {
                        ((ViewGroup) (mListView.getSelectedView())).getChildAt(0).setBackgroundResource(
                                R.drawable.source_item_backgroud);
                    }
                }
            } else {
                if (null != mRelativeTvView.tmpView) {
                    UIUtil.viewScaleDown(mContext, mRelativeTvView.tmpView, ScaleFactor, ScaleFactor);
                    mRelativeTvView.tmpView = null;
                }
                if (focusType == LConstants.FOCUS_TYPE_STATIC) {
                    tvImageButton.setBackgroundResource(R.color.transparent);
                }
                if (mListView.getSelectedView() != null) {
                    ((ViewGroup) (mListView.getSelectedView())).getChildAt(0).setBackgroundResource(
                            R.color.defaultsource_color);
                }
            }
        }
    };

    private class TViewOnKeyListener implements OnKeyListener {

        /*
         * (non-Javadoc)
         * @see android.view.View.OnKeyListener#onKey(android.view.View, int,
         * android.view.KeyEvent)
         */
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    Log.v(TAG, "onKey KEYCODE_DPAD_RIGHT ");
                    setTitleGridViewBackgroud(1);
                    vpAdapter.scrollTv(0);
                    focusView.setVisibility(View.VISIBLE);
                }
            }
            return false;
        }

    };

    private class TvImageButtonOnclickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            // mSurfaceView.setBackgroundColor(Color.BLACK);
            TvUtils.startTV(mContext);
        }

    };

    private class SouceItemclickListener implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if (curPosition == position) {
                Log.d(TAG, "the source is current source .");
                Log.e(TAG, "curSource == TvUtils.getCurInputSource(position)");
                return;
            }
            curPosition = position;
            Log.e(TAG, "curSource != TvUtils.getCurInputSource(position)");
            Log.i("debug", "hdmi--->" + position);
            mRelativeTvView.setSourceText(position);
            mContext.sourceChageHandle(TvUtils.getCurInputSource(position), 0);
            mRelativeTvView.setSourceText(position);
        }
    }
    
    /**
     * the xml file is something like this <packages> 
     * <app title="媒体播放器(0107)"/>
     * 
     * @param ctx
     * @return
     */
    public List<String> getAPPTitleFromXml(Context ctx) {
        List<String> list = new ArrayList<String>();
        XmlPullParser parser;
        parser = ctx.getResources().getXml(R.xml.system_app);
        try {
            while (parser.getEventType() != XmlResourceParser.END_DOCUMENT) {
                if (parser.getEventType() == XmlResourceParser.START_TAG) {
                    String tagName = parser.getName();
                    if (tagName.equals("app")) {
                        String pkgName = parser.getAttributeValue(null, "title");
                        list.add(pkgName);
                    }
                }
                parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

}
