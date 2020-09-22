package com.eostek.rick.demo.designviewgroup.DataBean;

import android.content.Context;

/**
 * Created by rick on 2017/7/7.
 */

public class ZhouStar {

    private String name;

    private String picName;

    public ZhouStar(String name, String picName) {
        this.name = name;
        this.picName = picName;
    }

    public String getName(){
        return name;
    }

    public String getPicName(){
        return picName;
    }

    public int getImageResourceId(Context context) {
        try {
            return context.getResources().getIdentifier(this.picName, "mipmap", context.getPackageName());

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
