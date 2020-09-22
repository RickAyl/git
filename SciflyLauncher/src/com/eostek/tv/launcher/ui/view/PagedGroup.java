
package com.eostek.tv.launcher.ui.view;

import java.util.ArrayList;
import java.util.List;

import com.eostek.tv.launcher.R;
import com.eostek.tv.launcher.HomeApplication;
import com.eostek.tv.launcher.business.database.DBManager;
import com.eostek.tv.launcher.model.MetroInfo;
import com.eostek.tv.launcher.util.LConstants;
import com.eostek.tv.launcher.util.UIUtil;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.OnHierarchyChangeListener;

/*
 * projectName： ViewPager4Android
 * moduleName： CustomGroup.java
 *
 * @author chadm.xiang
 * @version 1.0.0
 * @time  2014-7-8 上午11:52:26
 * @Copyright © 2014 Eos Inc.
 */

public class PagedGroup extends ViewGroup {

    private final String TAG = PagedGroup.class.getSimpleName();

    private Context mContext;

    private int margin;

    private List<View> childViews = new ArrayList<View>();

    private List<MetroInfo> positionList = new ArrayList<MetroInfo>();
    
    private ObservableScrollView mScrollView;

    private int widthMargin;

    private int heightMargin;

    private boolean showTV = false;

    private FocusView focusView;

    private int mPageNum = -1;

    private ReflectionTView tvViewRl;

    private int mTvSurfaceviewWidth;

    private int mTvViewHeight;

    private int mPageGroupLeft;

    private int mPageTvViewGapRight;

    private int mReflectViewHeight;

    private int mReflectViewGap;

    private int mScaleGap;

    private float factor = 1;

    private int mFocusBorderGap;

    // the view which reponse to hover event
    private View hoverView;
    
    private MetroInfo mFocusedItem;

    /**
     * @param context
     */
    public PagedGroup(Context context) {
        super(context);
        initGroup(context);
    }

    public PagedGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGroup(context);
    }

    public PagedGroup(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initGroup(context);
    }

    /**
     * load all view with given positions
     * 
     * @param metroInfos The position info of views
     * @param pageNum
     */
    public void loadAllView(List<MetroInfo> metroInfos, int pageNum) {
        if (metroInfos == null || metroInfos.isEmpty()) {
            return;
        }
        Log.v(TAG, "loadAllView pageNum = " + pageNum);
        removeAllViews();
        childViews.clear();
        positionList.clear();
        mPageNum = pageNum;
        
        if (pageNum == 0 && HomeApplication.isHasTVModule()) {
            widthMargin += mTvSurfaceviewWidth + mPageTvViewGapRight;
            showTV = true;
        }
        
        if(UIUtil.getLanguage().equals("en-us")){
        	List<MetroInfo> appList = DBManager.getDbManagerInstance(mContext).getAppPageInfo(UIUtil.getLanguage());
        	
        	positionList = metroInfos;
        	
    		List<String> AppPkgList = new ArrayList<String>();
    		for (int i = 0; i < appList.size(); i++) {
    			AppPkgList.add(appList.get(i).getClsName());
    		}
    		
    		for(int i = 0;i < positionList.size();i++){
            	if(AppPkgList.contains(positionList.get(i).getClsName())){
            		positionList.remove(i);
            	}
            }
        	
            for (MetroInfo info : appList) {
                if (info.getHide() != LConstants.APP_HIDE) {
                    positionList.add(info);
                }
            }
            
            for(int i = 0;i < positionList.size();i++){
            	Log.d("ahri", "positionList.get(" + i + ") = " + positionList.get(i).getClsName());
            }
            
            for (int i = 0; i < positionList.size(); i++) {
                MetroInfo metro = positionList.get(i);
                final View view = getView();
                ReflectImage layout = (ReflectImage) view;
                layout.setPageNum(pageNum);
                layout.setMetroInfo(metro);
                addView(view);
                childViews.add(view);
                Log.d("ahri", "loadAllView childViews.size() =" + childViews.size());
            }
        }else{
        	positionList = metroInfos;
        	for (int i = 0; i < metroInfos.size(); i++) {
                View view = getView();
                ReflectImage layout = (ReflectImage) view;
                layout.setPageNum(mPageNum);
                layout.setMetroInfo(metroInfos.get(i));
                addView(view);
                childViews.add(view);
            }
            Log.d("ahri", "loadAllView childViews.size() =" + childViews.size());
        }
        
        if (showTV) {
            tvViewRl = (ReflectionTView) getTvView();
            addView(tvViewRl);
            childViews.add(tvViewRl);
        }
        Log.d("ahri", "loadAllView childViews.size()11 =" + childViews.size());
    }

    /**
     * get metro infos
     * 
     * @return metro infos
     */
    public List<MetroInfo> getMetroInfos() {
        return positionList;
    }
    
    public View getHoverView() {
        return hoverView;
    }

    public void setHoverView(View view) {
        this.hoverView = view;
    }
    
    /**
     * set focused item
     * 
     * @param metroInfo
     */
    public void setFocusedItem(MetroInfo metroInfo) {
        this.mFocusedItem = metroInfo;
    }

    /**
     * get focused item
     * 
     * @return focused item
     */
    public MetroInfo getFocusedItem() {
        return mFocusedItem;
    }

    /**
     * release the resouce in the page,include the view and bitmap
     */
    public void releasePageReSource() {
        if (showTV && tvViewRl != null) {
            tvViewRl.removeAllViews();
            tvViewRl.releaseTViewReflection();
            tvViewRl = null;
        }
        for (View view : childViews) {
            if (view instanceof ReflectImage) {
                ReflectImage tmpImage = (ReflectImage) view;
                tmpImage.releaseReflectBitmap();
                tmpImage.removeAllViews();
            } else if (view instanceof ReflectionTView) {
                ReflectionTView tmpTView = (ReflectionTView) view;
                tmpTView.releaseTViewReflection();
                tmpTView.removeAllViews();
            }
        }
        removeAllViews();
        childViews.clear();
        positionList.clear();
        if (hoverView != null) {
            if (hoverView instanceof ReflectImage) {
                ReflectImage tmpImage = (ReflectImage) hoverView;
                tmpImage.releaseReflectBitmap();
                tmpImage.removeAllViews();
            }
            hoverView = null;
        }
    }

    /**
     * get tv view,include tv surface and source
     * 
     * @return
     */
    private View getTvView() {
        ReflectionTView tView = new ReflectionTView(mContext,mScrollView);
        return tView;
    }

    private View getView() {
        ReflectImage view = new ReflectImage(mContext, focusView);
        return view;
    }
    
    /**
     * set scroll view of the PagedGroup
     * 
     * @param view
     */
    public void setScrollView(ObservableScrollView view) {
        mScrollView = view;
    }

    private void initGroup(Context context) {
        this.mContext = context;
        mTvSurfaceviewWidth = context.getResources().getInteger(R.integer.tv_surfaceview_width);
        mTvViewHeight = context.getResources().getInteger(R.integer.tv_view_height);
        mPageGroupLeft = context.getResources().getInteger(R.integer.page_group_left);
        mPageTvViewGapRight = context.getResources().getInteger(R.integer.page_tv_view_gap_right);
        widthMargin = context.getResources().getInteger(R.integer.width_margin);
        heightMargin = context.getResources().getInteger(R.integer.height_margin);
        margin = context.getResources().getInteger(R.integer.margin);
        mReflectViewHeight = context.getResources().getInteger(R.integer.reflect_image_height);
        mReflectViewGap = context.getResources().getInteger(R.integer.reflect_image_gap);
        mScaleGap = context.getResources().getInteger(R.integer.tv_scale_gap);
        factor = HomeApplication.getFactor();
        mFocusBorderGap = mContext.getResources().getInteger(R.integer.focus_border_gap);
        
        if(UIUtil.getLanguage().equals("en-us")){
        	setOnHierarchyChangeListener(OnHierarchyChangeListener);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (positionList.isEmpty()) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            // int xMax = positionList.get(positionList.size() - 1).getX()
            // + positionList.get(positionList.size() - 1).getWidthSize();
            int xMax = getMaxWidth();
            int yMax = 6 * mPageGroupLeft + margin + mReflectViewHeight + mReflectViewGap;
            int minWidth = getResources().getInteger(R.integer.pagegroup_min_width);
            int width = minWidth;
            int height = yMax + mPageGroupLeft;
            if (showTV) {
                width = (int) (xMax * factor + mPageGroupLeft * 2 + mPageTvViewGapRight + mTvSurfaceviewWidth);
                if (width < minWidth) {
                    width = minWidth;
                }
            } else {
                width = (int) (xMax * factor + mPageGroupLeft * 2);
            }
            setMeasuredDimension(width, height);
            // Log.v(TAG, "onMeasure --- width = " + width + ";height = " +
            // height);
        }
    }

    /*
     * (non-Javadoc)
     * @see android.view.ViewGroup#onLayout(boolean, int, int, int, int)
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (positionList.isEmpty()) {
            return;
        }

        for (int i = 0; i < childViews.size(); i++) {
            layoutAChild(i);
        }
    }

    private int getMaxWidth() {
        int max = 0;
        if (positionList != null && !positionList.isEmpty()) {
            for (MetroInfo info : positionList) {
                int width = info.getWidthSize() + info.getX();
                if (width > max) {
                    max = width;
                }
            }
        }
        // Log.v(TAG, "getMaxWidth = " + max);
        return max;
    }

    private void layoutAChild(int index) {
        if (showTV && index == positionList.size()) {
            View view = childViews.get(index);
            measureChildren(view, mTvSurfaceviewWidth, mTvViewHeight);
            view.layout(mPageGroupLeft - mScaleGap, heightMargin, mTvSurfaceviewWidth + mPageGroupLeft + mScaleGap,
                    mTvViewHeight + heightMargin + mReflectViewHeight + mReflectViewGap);
            return;
        }

        ReflectImage view = (ReflectImage) childViews.get(index);
        MetroInfo metroInfo = positionList.get(index);
        int x = (int) (metroInfo.getX() * factor);
        int y = (int) (metroInfo.getY() * factor);
        int wSize = (int) (metroInfo.getWidthSize() * factor);
        int hSzie = (int) (metroInfo.getHeightSize() * factor);

        view.setOriginLayoutParams(wSize + mFocusBorderGap * 2, hSzie + mFocusBorderGap * 2);

        // When UI adaptation, calculate PX could have error
        if (Math.abs(y + hSzie - mTvViewHeight) < 2) {
            view.setReflectionMode(false);
            int reflectHeigh = mContext.getResources().getInteger(R.integer.reflect_image_gap)
                    + mContext.getResources().getInteger(R.integer.reflect_image_height);
            hSzie += reflectHeigh;
            view.setmReflectImageParams(wSize, reflectHeigh);
        } else {
            view.setReflectionMode(false);
        }

        int top = y + heightMargin;
        int left = x + widthMargin;

        measureChildren(view, wSize + mFocusBorderGap * 2, hSzie + mFocusBorderGap * 2);
        view.layout(left - mFocusBorderGap, top - mFocusBorderGap, left + wSize + mFocusBorderGap, top + hSzie
                + mFocusBorderGap);
        // Log.v(TAG, "left = " + x + ";top = " + y + ";width = " + wSize +
        // ";height = " + hSzie);
    }

    /**
     * meashure child view width and height
     * 
     * @param child
     * @param width
     * @param height
     */
    protected void measureChildren(View child, final int width, final int height) {
        final int heightSpec;
        if (height == LayoutParams.WRAP_CONTENT) {
            heightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        } else {
            heightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }

        final int widthSpec;
        if (width == LayoutParams.WRAP_CONTENT) {
            widthSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        } else {
            widthSpec = MeasureSpec.makeMeasureSpec(width + mScaleGap * 2, MeasureSpec.EXACTLY);
        }
        child.measure(widthSpec, heightSpec);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.v(TAG, "time = " + System.currentTimeMillis());
    }

    @Override
    public View findFocus() {
        View foView = super.findFocus();
        Rect rect = new Rect();
        foView.getGlobalVisibleRect(rect);
        // Log.v(TAG, "findFocus rect =  " + rect.toShortString());
        return foView;
    }

    @Override
    public View getFocusedChild() {
        View foView = super.getFocusedChild();
        if (foView != null) {
            Rect rect = new Rect();
            foView.getGlobalVisibleRect(rect);
            // Log.v(TAG, "getFocusedChild rect =  " + rect.toShortString());
            focusView.changeDuration();
            focusView.startAnimation(rect);
        }
        return foView;
    }

    public ReflectionTView getTvViewRl() {
        return tvViewRl;
    }

    public void setFocusView(FocusView view) {
        this.focusView = view;
    }

    public FocusView getFocusView() {
        return this.focusView;
    }
    
    /**
     * add app view when local is EN-US.
     * 
     * @param appInfo
     */
    public void addEnAppView(MetroInfo appInfo) {
    	int lastIndex = getChildCount() - 1;
    	removeView(childViews.get(childViews.size() - 1));
        removeView(childViews.get(childViews.size() - 2));
        childViews.remove(lastIndex);
        childViews.remove(lastIndex - 1);
        positionList.remove(lastIndex - 1);
        final View view = getView();
        ReflectImage layout = (ReflectImage) view;
        layout.setPageNum(mPageNum);
        layout.setMetroInfo(appInfo);
        final PackageManager packageManager = mContext.getPackageManager();
        final Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setClassName(appInfo.getPkgName(), appInfo.getClsName());
        List<ResolveInfo> tempInfos = packageManager.queryIntentActivities(intent, 0);
        if (!tempInfos.isEmpty()) {
            layout.setImgResource(((BitmapDrawable) (tempInfos.get(0).loadIcon(packageManager))).getBitmap(),
                    appInfo.getTitle());
        }
        addView(view);
        positionList.add(appInfo);
        childViews.add(view);
        
        addEnAddView(appInfo.getPositionId() + 1);
        ReflectImage addView = (ReflectImage) childViews.get(childViews.size() - 1);
        addView.setAddImg();
        if (positionList.size() > LConstants.APP_MAX_NUM) {
            addView.setVisibility(View.GONE);
        }
        
        addEnTvView();
    }
    
    private void addEnTvView() {
    	if (showTV) {
            tvViewRl = (ReflectionTView) getTvView();
            addView(tvViewRl);
            childViews.add(tvViewRl);
        }
	}

	private void addEnAddView(int positionId) {
        MetroInfo addInfo = new MetroInfo();
        addInfo.setClsName(AppAddActivity.mLastAddInfo.getClsName());
        addInfo.setPkgName(AppAddActivity.mLastAddInfo.getPkgName());
        addInfo.setY(AppAddActivity.mLastAddInfo.getY());
        addInfo.setX(AppAddActivity.mLastAddInfo.getX());
        addInfo.setWidthSize(170);
        addInfo.setHeightSize(170);
        addInfo.setPositionId(positionId);
        Log.d("ahri", "add view PositionId =" + addInfo.getPositionId());
        addInfo.setCounLang("en-us");
        addInfo.setItemType(1);
        final View view = getView();
        ReflectImage layout = (ReflectImage) view;
        layout.setPageNum(mPageNum);
        layout.setMetroInfo(addInfo);
        addView(view);
        view.requestFocus();
        childViews.add(view);
        positionList.add(addInfo);
    }
    
	/**
     * remove app view when local is en-us
     * 
     * @param appInfo
     */
    public void removeEnAppView(MetroInfo appInfo) {
        int tmpPosId = appInfo.getPositionId();
        Log.d("ahri", "tmpPosId =" + tmpPosId);
        int index = positionList.indexOf(appInfo);
        Log.d("ahri", "index =" + index);
        removeEnApp(tmpPosId, index);
        removeView(hoverView);
    }

    private void removeEnApp(int tmpPosId, int index) {
    	
    	Log.d("ahri", "index =" + index);
    	removeView(childViews.get(index));
    	positionList.remove(index);
        childViews.remove(index);
        MetroInfo tmpInfo;
        for (int i = 0; i < positionList.size(); i++) {
            tmpInfo = positionList.get(i);
            if (tmpInfo.getPositionId() > tmpPosId) {
                tmpInfo.setY(UIUtil.getEnYById(tmpInfo.getPositionId() - 1));
                tmpInfo.setX(UIUtil.getEnXById(tmpInfo.getPositionId() - 1));
                tmpInfo.setPositionId(tmpInfo.getPositionId() - 1);
                Log.d("ahri", "tmpInfo.getPositionId() - 1 =" + (tmpInfo.getPositionId() - 1));
                Log.d("ahri", "tmpInfo remove X = " + (UIUtil.getEnXById(tmpInfo.getPositionId() - 1)));
                Log.d("ahri", "tmpInfo remove Y = " + (UIUtil.getEnYById(tmpInfo.getPositionId() - 1)));
            }
            ((ReflectImage) childViews.get(i)).setMetroInfo(tmpInfo);
        }
    	
    }
    
    /**
     * remove view when uninstall package and local is en-us.
     * 
     * @param pkg
     */
    public void removeEnAppView(String pkg) {

        int index = -1;
        for (MetroInfo info : positionList) {
            if (null != info.getPkgName() && info.getPkgName().equalsIgnoreCase(pkg)) {
                index = positionList.indexOf(info);
                break;
            }
        }
        
        if (index == -1) {
            return;
        }
        View appView = childViews.get(index);
        MetroInfo appInfo = positionList.get(index);
        int tmpPosId = appInfo.getPositionId();
        
        if(appInfo.getItemType() == 9){
        	removeEnApp(tmpPosId, index);
            removeView(appView);
        }
    }
    
    OnHierarchyChangeListener OnHierarchyChangeListener = new OnHierarchyChangeListener(){
		@Override
		public void onChildViewAdded(View arg0, View arg1) {
			//do nothing
		}

		@Override
		public void onChildViewRemoved(View arg0, View arg1) {
			Log.d("ahri", "onChildViewRemoved");
//			childViews.get(childViews.size() - 1).requestFocus();
//			
//			if(mScrollView != null){
//				mScrollView.smoothScrollTo(mScrollView.getWidth(), 0);
//			}
		}
		
    };
}
