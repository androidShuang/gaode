package com.yyc.poimap.cluster;

import com.amap.api.maps.model.LatLng;
import com.yyc.poimap.cluster.demo.ItemBean;

/**
 * Created by Administrator on 2019/1/29.
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
