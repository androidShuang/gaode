package com.amap.apis.cluster.demo;

/**
 * Created by Administrator on 2019/1/29.
 */

public class ItemBean {
    private String mTitle;
    private String url;
    private String price;

    public ItemBean(String mTitle, String url, String price, String name) {
        this.mTitle = mTitle;
        this.url = url;
        this.price = price;
        this.name = name;
    }

    private String name;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
