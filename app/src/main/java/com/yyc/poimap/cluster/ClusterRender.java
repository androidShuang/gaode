package com.yyc.poimap.cluster;

import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2019/1/29.
 */

public interface ClusterRender {
    /**
     * 根据聚合点的元素数目返回渲染背景样式
     */
     Drawable getDrawAble(int clusterNum, Cluster cluster);
}
