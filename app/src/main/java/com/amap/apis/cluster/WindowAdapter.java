package com.amap.apis.cluster;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.apis.cluster.demo.ItemBean;

/**
 * Created by Administrator on 2019/1/31.
 */

public class WindowAdapter implements AMap.InfoWindowAdapter,AMap.OnMarkerClickListener,AMap.OnInfoWindowClickListener{
    private Context context;
    private Cluster cluster;
    private static final String TAG = "WindowAdapter";
    private AMap aMap;
    public WindowAdapter(Context context, AMap mAMap,PoiSearch poiSearch) {
        this.context = context;
        this.aMap = mAMap;
    }

    public Cluster getCluster() {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        //关联布局
        View view = LayoutInflater.from(context).inflate(R.layout.layout_circle_comment, null);
        //标题
        TextView title = (TextView) view.findViewById(R.id.info_title);
        //地址信息
        final TextView address = (TextView) view.findViewById(R.id.info_address);
        //纬度
        final TextView latitude = (TextView) view.findViewById(R.id.info_latitude);
        //经度
        final TextView longitude = (TextView) view.findViewById(R.id.info_longitude);



        Cluster cluster = (Cluster) marker.getObject();
        ItemBean itemBean = null;
        if(cluster!=null){
            ClusterItem clusterItem = cluster.getClusterItems().get(0);
            if(clusterItem!=null&&clusterItem.getItemBean()!=null) {
                itemBean = clusterItem.getItemBean();
            }
        }
        LatLonPoint latLonPoint = new LatLonPoint(marker.getPosition().latitude,marker.getPosition().longitude);
        GeocodeSearch geocodeSearch = new GeocodeSearch(context);
        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                Log.e(TAG, String.valueOf(regeocodeResult.getRegeocodeAddress().getFormatAddress()));
                latitude.setText(regeocodeResult.getRegeocodeAddress().getFormatAddress());
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
                Log.e(TAG, String.valueOf(geocodeResult.getGeocodeAddressList().get(0)));
            }
        });
        geocodeSearch.getFromLocationAsyn(new RegeocodeQuery(latLonPoint,200,GeocodeSearch.AMAP));
        address.setText(itemBean.getName());
        title.setText(itemBean.getPrice());
//        latitude.setText(marker.getPosition().latitude + "");
//        longitude.setText(marker.getPosition().longitude + "");
        ((MainActivity)context).setCurrentTitle(itemBean.getmTitle());
        Log.e(TAG, "getInfoWindow1: "+itemBean.getmTitle());
        Log.e(TAG, "getInfoWindow: "+itemBean.getUrl());
        Log.e(TAG, "getInfoWindow: "+marker.getSnippet());
        Log.e(TAG, "getInfoWindow: "+marker.getPosition().latitude );
        Log.e(TAG, "getInfoWindow: "+marker.getPosition().longitude );
        Log.e(TAG,marker.toString());
        if(((MainActivity)context).isUpdateFlag()) {
            ((MainActivity)context).setUpdateFlag(false);
            return view;
        }else{
            return null;
        }
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Log.e(TAG, "InfoWindow被点击了");
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Cluster cluster = (Cluster) marker.getObject();
        Log.e(TAG, "Marker被点击了=="+marker.toString());
        if(cluster!=null&&cluster.getClusterItems()!=null&&cluster.getClusterItems().size()==1) {
//            aMap.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
//            aMap.setInfoWindowAdapter(new WindowAdapter(context,aMap));
            return false;
        }else{
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (ClusterItem clusterItem : cluster.getClusterItems()) {
                builder.include(clusterItem.getPosition());
            }
            LatLngBounds latLngBounds = builder.build();
            aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 0));
            return true;
        }
    }


}
