package com.yyc.poimap.cluster.demo;

import com.amap.api.maps.model.LatLng;
import com.yyc.poimap.cluster.ClusterItem;

/**
 * Created by Administrator on 2019/1/29.
 * 区域BEAN
 */

public class RegionItem implements ClusterItem {
    private LatLng mLatLng;
    private ItemBean itemBean;
    public RegionItem(LatLng latLng,ItemBean itemBean) {
        mLatLng=latLng;
        this.itemBean = itemBean;
    }

    @Override
    public LatLng getPosition() {
        return mLatLng;
    }

    @Override
    public ItemBean getItemBean() {
        return itemBean;
    }

}
