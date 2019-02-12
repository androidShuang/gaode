package com.yyc.poimap.cluster;

import com.amap.api.maps.model.Marker;

/**
 * Created by Administrator on 2019/1/29.
 */

public interface ClusterClickListener{
        //聚合点回调函数
        public void onClick(Marker marker, Cluster clusterItems);
}
