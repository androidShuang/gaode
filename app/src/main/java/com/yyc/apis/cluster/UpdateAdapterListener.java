package com.yyc.apis.cluster;

import java.util.List;

/**
 * Created by Administrator on 2019/1/31.
 */

public interface UpdateAdapterListener {
    void onAdapterUpdate(List<Cluster> clusters, boolean updateFlag);
}
