package com.yyc.poimap.cluster;

import java.util.List;

/**
 * Created by Administrator on 2019/1/31.
 * 气泡更新监听
 */

public interface UpdateAdapterListener {
    void onAdapterUpdate(List<Cluster> clusters, boolean updateFlag);
}
