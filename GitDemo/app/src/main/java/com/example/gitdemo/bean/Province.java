package com.example.gitdemo.bean;

import com.google.gson.annotations.SerializedName;

public class Province {


    private int _id;
    @SerializedName("name")
    private String provinceName;
    @SerializedName("id")
    private int provinceCode;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }

}
