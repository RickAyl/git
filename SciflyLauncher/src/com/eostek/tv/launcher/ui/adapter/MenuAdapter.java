
package com.eostek.tv.launcher.ui.adapter;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eostek.tv.launcher.R;
import com.eostek.tv.launcher.util.LConstants;

public class MenuAdapter extends BaseAdapter {

    private Context mContext;

    private int[] mTitleArray;

    private int[] mIconIdArray;
    
    public MenuAdapter(Context context, int[] ids, int[] titles) {
        this.mContext = context;
        this.mTitleArray = titles;
        this.mIconIdArray = ids;
    }

    @Override
    public int getCount() {
        return mTitleArray == null ? 0 : mTitleArray.length;
    }

    @Override
    public Object getItem(int position) {
        return mTitleArray[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String name = mContext.getString(mTitleArray[position]);
        int iconId = mIconIdArray[position];
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LinearLayout view = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.apppage_menu_item, null);
            holder.mTitle = (TextView) view.findViewById(R.id.apppage_menuitem_title);
            holder.mIcon = (ImageView) view.findViewById(R.id.apppage_menuitem_icon);
            view.setTag(holder);
            convertView = view;
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mIcon.setImageResource(iconId);
        holder.mTitle.setText(name);

        Configuration config = mContext.getResources().getConfiguration();
        if (config.fontScale == 1.0f) {
            holder.mTitle.setTextSize(LConstants.TXT_SIZE_NORMAL);
        } else {
            holder.mTitle.setTextSize(LConstants.TXT_SIZE_OTHER);
        }

        return convertView;
    }

    class ViewHolder {
        TextView mTitle;

        ImageView mIcon;
    }

    public void setIconData(int[] ids) {
        mIconIdArray = ids;
    }

    public void setTitleData(int[] titles) {
        mTitleArray = titles;
    }

}
