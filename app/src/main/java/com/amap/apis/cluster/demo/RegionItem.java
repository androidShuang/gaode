package com.amap.apis.cluster.demo;

import com.amap.api.maps.model.LatLng;
import com.amap.apis.cluster.ClusterItem;

/**
 * Created by yiyi.qi on 16/10/10.
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
        // TODO Auto-generated method stub
        return mLatLng;
    }

    @Override
    public ItemBean getItemBean() {
        return itemBean;
    }

}
