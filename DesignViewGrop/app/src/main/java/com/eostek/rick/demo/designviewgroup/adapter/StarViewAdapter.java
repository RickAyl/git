package com.eostek.rick.demo.designviewgroup.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.eostek.rick.demo.designviewgroup.DataBean.ZhouStar;
import com.eostek.rick.demo.designviewgroup.R;

/**
 * Created by rick on 2017/7/8.
 */

public class StarViewAdapter extends RecyclerView.Adapter {

    List<ZhouStar> starList = null;

    private Context mContext;


    public StarViewAdapter(List<ZhouStar> starList, Context context) {
        this.starList = starList;
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.star_cards_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // 给ViewHolder设置元素
        ZhouStar p = starList.get(position);
        ViewHolder viewHolder = (ViewHolder)holder;
        viewHolder.mTextView.setText(p.getName());
        viewHolder.mImageView.setImageResource(p.getImageResourceId(mContext));
    }

    @Override
    public int getItemCount() {
        return starList.size();
    }


    // 重写的自定义ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;

        public ImageView mImageView;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.name);
            mImageView = (ImageView) v.findViewById(R.id.pic);
        }
    }
}
