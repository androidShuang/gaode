package com.amap.apis.cluster;

import com.amap.api.maps.model.LatLng;
import com.amap.apis.cluster.demo.ItemBean;

/**
 * Created by yiyi.qi on 16/10/10.
 */

public interface ClusterItem {
    /**
     * 返回聚合元素的地理位置
     *
     * @return
     */
     LatLng getPosition();
     ItemBean getItemBean();
}
