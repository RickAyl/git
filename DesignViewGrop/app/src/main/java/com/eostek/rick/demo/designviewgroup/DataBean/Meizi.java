package com.eostek.rick.demo.designviewgroup.DataBean;

/**
 * Created by a on 17-6-19.
 */

public class Meizi {
    private String url;//图片地址

    private int page;//页数

    private String _id;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId(){ return  _id; }

    public void setId(String id){this._id = id;}

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String toString() {
        return "id : " + _id + "\r\n" + "url : " + url + "\r\n" + "page : " + page;
    }
}
