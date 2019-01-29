package com.amap.apis.cluster.demo;

/**
 * Created by Administrator on 2019/1/29.
 */

public class ItemBean {
    private String mTitle;
    private String url;

    public ItemBean(String mTitle, String url) {
        this.mTitle = mTitle;
        this.url = url;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
