package com.example.gitdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gitdemo.R;
import com.example.gitdemo.bean.City;

import java.util.List;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {

    List<City> mList;

    Context mContext;



    public CityAdapter(@NonNull List<City> list, @NonNull Context context) {
        this.mList = list;
        this.mContext = context;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;

        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.city_cardview);
            name = itemView.findViewById(R.id.name);
        }
    }

    @NonNull
    @Override
    public CityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.city_item, parent, false);
        CityAdapter.ViewHolder holder = new CityAdapter.ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CityAdapter.ViewHolder holder, int position) {
        City city = mList.get(position);
        holder.name.setText(city.getCityCode() + " : " + city.getCityName());
        final int cityCode = city.getCityCode();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(cityCode);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private ItemClickListener listener;

    public void setOnItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    public interface ItemClickListener {
        void onItemClick(int cityCode);
    }
}
