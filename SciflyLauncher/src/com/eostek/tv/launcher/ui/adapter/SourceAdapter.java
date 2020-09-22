
package com.eostek.tv.launcher.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.eostek.tv.launcher.R;
import com.eostek.tv.launcher.util.LConstants;
import com.eostek.tv.launcher.util.UIUtil;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;

/*
 * projectName： TVLauncher
 * moduleName： SourceAdapter.java
 *
 * @author chadm.xiang
 * @version 1.0.0
 * @time  2014-7-12 上午11:09:59
 * @Copyright © 2014 Eos Inc.
 */

public class SourceAdapter extends BaseAdapter {

    private Context mContext;

    private String[] sourceList;

    private int[] sourcePics_hastuner = {
            R.drawable.tv_signal_analog, R.drawable.tv_signal_digital, R.drawable.tv_signal_hdmi,
            R.drawable.tv_signal_hdmi,R.drawable.tv_signal_av, R.drawable.tv_signal_ypbpr,
            R.drawable.tv_signal_vga
    };
    
    private int[] sourcePics_notuner = {
    		R.drawable.tv_signal_hdmi,
    		R.drawable.tv_signal_hdmi, R.drawable.tv_signal_hdmi, R.drawable.tv_signal_av, R.drawable.tv_signal_ypbpr,
    		R.drawable.tv_signal_vga
    };
    
    private int[] sourcePics_638 = {
            R.drawable.tv_signal_analog,R.drawable.tv_signal_hdmi,
            R.drawable.tv_signal_hdmi, R.drawable.tv_signal_hdmi, R.drawable.tv_signal_av, R.drawable.tv_signal_ypbpr,
            R.drawable.tv_signal_vga
    };
    
    private int[] sourcePics_LX828 = {
            R.drawable.tv_signal_hdmi,R.drawable.tv_signal_hdmi
    };

    // the signal status
    public boolean[] signalStatus_hastuner = {
            false, false, false, false, false, false, false
    };
    
    public boolean[] signalStatus_notuner = {
    		false, false, false, false, false, false
    };
    
    // the signal status
    public boolean[] signalStatus_638 = {
            true, false, false, false, false, false, false
    };
    
 // the signal status
    public boolean[] signalStatus_LX828 = {
            false, false
    };

    public SourceAdapter(Context context, String[] sources) {
        this.mContext = context;
        this.sourceList = sources;
    }

    /*
     * (non-Javadoc)
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public int getCount() {
        return sourceList.length;
    }

    /*
     * (non-Javadoc)
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public Object getItem(int position) {
        return sourceList[position];
    }

    /*
     * (non-Javadoc)
     * @see android.widget.Adapter#getItemId(int)
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /*
     * (non-Javadoc)
     * @see android.widget.Adapter#getView(int, android.view.View,
     * android.view.ViewGroup)
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String name = sourceList[position];
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            View view = LayoutInflater.from(mContext).inflate(R.layout.source_listview_item, null);
            holder.mTitle = (TextView) view.findViewById(R.id.source_name);
            holder.mImageView = (ImageView) view.findViewById(R.id.source_img);
            view.setTag(holder);
            convertView = view;
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        if(LConstants.FEATURE_638.equals(UIUtil.getSpecialCode())){
        	if (signalStatus_638[position]) {
        	    holder.mTitle.setTextColor(mContext.getResources().getColor(R.color.text_white));
            } else {
                holder.mTitle.setTextColor(mContext.getResources().getColor(R.color.text_grey));
            }
        } else if(LConstants.FEATURE_LX828.equals(UIUtil.getSpecialCode())){
        	if (signalStatus_LX828[position]) {
        	    holder.mTitle.setTextColor(mContext.getResources().getColor(R.color.text_white));
            } else {
                holder.mTitle.setTextColor(mContext.getResources().getColor(R.color.text_grey));
            }
        } else{
        	boolean  hasTuner = true;
        	/*try {
                hasTuner = TvManager.getInstance().getFactoryManager().getTunerStatus();
                Log.i("SourceAdapter", String.valueOf(hasTuner));
            } catch (TvCommonException e) {
                e.printStackTrace();
            }*/
            if (!hasTuner) {
            	if (signalStatus_notuner[position]) {
            	    holder.mTitle.setTextColor(mContext.getResources().getColor(R.color.text_white));
                } else {
                    holder.mTitle.setTextColor(mContext.getResources().getColor(R.color.text_grey));
                }
            }else{
            	if (signalStatus_hastuner[position]) {
            		holder.mTitle.setTextColor(mContext.getResources().getColor(R.color.text_white));
            	} else {
            		holder.mTitle.setTextColor(mContext.getResources().getColor(R.color.text_grey));
            	}
            }
        }
        holder.mTitle.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, mContext.getResources()
                .getInteger(R.integer.source_adapter_title)));
        holder.mTitle.setText(name);
        if(LConstants.FEATURE_638.equals(UIUtil.getSpecialCode())){
        	holder.mImageView.setBackgroundResource(sourcePics_638[position]);
        } else if(LConstants.FEATURE_LX828.equals(UIUtil.getSpecialCode())){
        	holder.mImageView.setBackgroundResource(sourcePics_LX828[position]);
        } else{
        	boolean  hasTuner = true;
        	/*try {
                hasTuner = TvManager.getInstance().getFactoryManager().getTunerStatus();
                Log.i("SourceAdapter", String.valueOf(hasTuner));
            } catch (TvCommonException e) {
                e.printStackTrace();
            }*/
            if (!hasTuner) {
            	holder.mImageView.setBackgroundResource(sourcePics_notuner[position]);
            }else{
            	holder.mImageView.setBackgroundResource(sourcePics_hastuner[position]);
            }
        }
        return convertView;
    }

    class ViewHolder {
        TextView mTitle;

        ImageView mImageView;
    }

}
