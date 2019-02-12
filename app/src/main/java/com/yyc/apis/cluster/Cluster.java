package com.yyc.apis.cluster;

import android.view.View;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yiyi.qi on 16/10/10.
 */

public class Cluster {


    private LatLng mLatLng;
    private List<ClusterItem> mClusterItems;
    private Marker mMarker;
    private Map<String,View> viewMap = new HashMap<>();


    Cluster( LatLng latLng) {
        mLatLng = latLng;
        mClusterItems = new ArrayList<ClusterItem>();
    }

    public void putView(String key,View view){
        viewMap.put(key,view);
    }

    public View getView(String key){
        return viewMap.get(key);
    }

    void addClusterItem(ClusterItem clusterItem) {
        mClusterItems.add(clusterItem);
    }

    int getClusterCount() {
        return mClusterItems.size();
    }



    LatLng getCenterLatLng() {
        return mLatLng;
    }

    void setMarker(Marker marker) {
        mMarker = marker;
    }

    Marker getMarker() {
        return mMarker;
    }

    List<ClusterItem> getClusterItems() {
        return mClusterItems;
    }
}
