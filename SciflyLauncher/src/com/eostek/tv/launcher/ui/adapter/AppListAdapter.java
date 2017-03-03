
package com.eostek.tv.launcher.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eostek.tv.launcher.R;

public class AppListAdapter extends BaseAdapter {

    private Context mContext;

    private PackageManager mPm;

    private List<ResolveInfo> mList;

    public AppListAdapter(Context context, List<ResolveInfo> list) {
        mContext = context;
        mPm = context.getPackageManager();
        mList = new ArrayList<ResolveInfo>();
        mList.addAll(list);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ResolveInfo appInfo = mList.get(position);
        AppItem appItem;
        if (convertView == null) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.app_list_item, null);
            appItem = new AppItem();
            appItem.mAppIcon = (ImageView) v.findViewById(R.id.app_item_icon);
            appItem.mAppTitle = (TextView) v.findViewById(R.id.app_item_title);
            v.setTag(appItem);
            convertView = v;
        } else {
            appItem = (AppItem) convertView.getTag();
        }
        // Get the application icon and name
        appItem.mAppIcon.setImageDrawable(appInfo.loadIcon(mPm));
        appItem.mAppTitle.setText(appInfo.loadLabel(mPm));

        return convertView;
    }

    // the listview item holder
    private class AppItem {
        ImageView mAppIcon;

        TextView mAppTitle;
    }

    public void setAppList(ArrayList<ResolveInfo> appList) {
        mList.clear();
        mList.addAll(appList);
    }
}
