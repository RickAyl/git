package com.eostek.rick.demo.designviewgroup.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import com.eostek.rick.demo.designviewgroup.DataBean.ZhouStar;
import com.eostek.rick.demo.designviewgroup.R;
import com.eostek.rick.demo.designviewgroup.adapter.StarViewAdapter;
import com.eostek.rick.demo.designviewgroup.view.DividerItemDecoration;

/**
 * Created by rick on 2017/7/8.
 */

public class ZhouStarFragment extends Fragment {

    private String[] names = { "朱茵", "张柏芝", "张敏", "巩俐", "黄圣依", "赵薇", "莫文蔚", "林允","张雨绮"};

    private String[] pics = { "p1", "p2", "p3", "p4", "p5", "p6", "p7", "p8","p9" };

    private List<ZhouStar> starList;

    private RecyclerView mStarView;

    private RecyclerView.Adapter mAdapter;

    private LinearLayoutManager mLinearLayoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        starList = new ArrayList<ZhouStar>();

        for(int i = 0;i < names.length;i ++){
            starList.add(new ZhouStar(names[i],pics[i]));
        }
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View zhouView = inflater.inflate(R.layout.cardview_layout,container,false);
        initView(zhouView);
        return  zhouView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new StarViewAdapter(starList,this.getActivity());
        mStarView.setAdapter(mAdapter);
    }

    private void initView(View view){
        mLinearLayoutManager = new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false);
        mStarView = (RecyclerView) view.findViewById(R.id.star_cards);
        mStarView.setLayoutManager(mLinearLayoutManager);
        mStarView.addItemDecoration(new DividerItemDecoration(this.getActivity(),
                DividerItemDecoration.VERTICAL_LIST));
    }
}
