package com.eostek.tv.launcher.ui.view;

import java.util.ArrayList;

import com.eostek.tv.launcher.business.database.DBManager;
import com.eostek.tv.launcher.business.listener.OnAppChangedListener;
import com.eostek.tv.launcher.model.MetroInfo;
import com.eostek.tv.launcher.ui.adapter.AppListAdapter;
import com.eostek.tv.launcher.R;

import android.R.integer;
import android.app.Activity;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;


public class AppAddActivity extends Activity{
	
	private String TAG = "ahri";
	
	private GridView mGridView;
	
	private AppListAdapter mAppAdapter;
	
	private ArrayList<ResolveInfo> mAppList;
	
	private int mOldAddX;
	
	private int mOldAddY;
	
	private int mOldAddId;
	
	private int mNewAddId;
	
	private int mNewAddX;
	
	private int mNewAddY;
	
	private MetroInfo tempMetroInfo;
	
	public static MetroInfo mLastAddInfo;
	
	public static OnAppChangedListener mListener = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_add_layout); 
		
		mGridView = (GridView)findViewById(R.id.app_add_gridview);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		mAppList = DBManager.getDbManagerInstance(AppAddActivity.this).getEnAppToAdd(AppAddActivity.this);
		Log.d("ahri", "mAppList.size()" + mAppList.size());
		
		mAppAdapter = new AppListAdapter(AppAddActivity.this, mAppList);
		
		mGridView.setAdapter(mAppAdapter);
		
		mGridView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,long id) {
				
				ResolveInfo appInfo = (ResolveInfo) mAppAdapter.getItem(position);
				
				copyAddView(appInfo);
				finish();
			}

		});
	}

	private void copyAddView(ResolveInfo appInfo) {
		mLastAddInfo = new MetroInfo();
		mLastAddInfo = DBManager.getDbManagerInstance(AppAddActivity.this).getLastDBData();
		mOldAddX = mLastAddInfo.getX();
		mOldAddY = mLastAddInfo.getY();
		mOldAddId = mLastAddInfo.getPositionId();
		Log.d("ahri", "mOldAddPositionId =" + mOldAddId);
		calculateAddPosition(mOldAddX,mOldAddY);
		
		mLastAddInfo.setX(mNewAddX);
		mLastAddInfo.setY(mNewAddY);
		mLastAddInfo.setPositionId(mOldAddId + 1);
		Log.d("ahri", "mNewAddPositionId =" + mLastAddInfo.getPositionId());
		DBManager.getDbManagerInstance(AppAddActivity.this).updateLastDBData(mLastAddInfo);
		
		updateAddAppDBData(appInfo,mOldAddX,mOldAddY,mOldAddId);
	}
	
	private void updateAddAppDBData(ResolveInfo appInfo, int x, int y, int positionId) {
		tempMetroInfo = new MetroInfo();
		tempMetroInfo.setPkgName(appInfo.activityInfo.packageName);
		tempMetroInfo.setClsName(appInfo.activityInfo.name);
		tempMetroInfo.setX(x);
		tempMetroInfo.setY(y);
		tempMetroInfo.setPositionId(positionId);
		tempMetroInfo.setTitle((String) appInfo.loadLabel(this.getPackageManager()));
		tempMetroInfo.setTypeTitle("hide");
		tempMetroInfo.setWidthSize(170);
		tempMetroInfo.setHeightSize(170);
		tempMetroInfo.setItemType(9);
		tempMetroInfo.setMoveType(1);
		tempMetroInfo.setCounLang("en-us");
		tempMetroInfo.setAppCategory(0);
		tempMetroInfo.setExtraIntInfo(0);
		
		DBManager.getDbManagerInstance(AppAddActivity.this).updateAddAppDBData(tempMetroInfo,mOldAddId);
		
		if (mListener != null) {
            mListener.onEnAppAdd(tempMetroInfo);
        }
		
	}
	
	private void calculateAddPosition(int x, int y) {
		if(y < 356){
			mNewAddY = y + 178;
			mNewAddX = mOldAddX;
		}else{
			mNewAddX = x + 178;
			mNewAddY = 0;
		}
	}

	public static void setAppChangedListener(OnAppChangedListener appChangedListener) {
        mListener = appChangedListener;
    }
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
}
