package com.example.appstartmanager;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class AllAppAdapter extends BaseAdapter {

    
    private List<AppInfo> mlist;
    
    private Context mContext;
    
    public AllAppAdapter(Context context,List<AppInfo> list) {
        // TODO Auto-generated constructor stub
        mContext = context;
        mlist = list;
    }
    
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mlist.size();
    }

    @Override
    public AppInfo getItem(int position) {
        // TODO Auto-generated method stub
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ImageButton image;
        TextView text;
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.all_app_gridview_item, null);
            convertView = view;
            ViewHolder viewHolder= new ViewHolder();
            viewHolder.imageView = (ImageButton) view.findViewById(R.id.app_item_image);
            viewHolder.textView = (TextView) view.findViewById(R.id.app_name);
            convertView.setTag(viewHolder);
            viewHolder.imageView.setImageDrawable(getItem(position).getDrawable());
            viewHolder.textView.setText(getItem(position).getAppName());
            
        }else{
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.imageView.setImageDrawable(getItem(position).getDrawable());
            viewHolder.textView.setText(getItem(position).getAppName());
            
        }
        return convertView;
    }

    class ViewHolder{
        ImageButton imageView;
        TextView textView;
    }
}
