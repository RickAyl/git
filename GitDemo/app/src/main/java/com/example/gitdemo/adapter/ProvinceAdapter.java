package com.example.gitdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gitdemo.R;
import com.example.gitdemo.bean.Province;


import java.util.List;

public class ProvinceAdapter extends RecyclerView.Adapter<ProvinceAdapter.ViewHolder> {

    List<Province> mList;

    Context mContext;

    public ProvinceAdapter(@NonNull List<Province> list, @NonNull Context context) {
        this.mList = list;
        this.mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;

        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.province_cardview);
            name = itemView.findViewById(R.id.name);
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.province_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Province province = mList.get(position);
        holder.name.setText(province.getProvinceName());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
