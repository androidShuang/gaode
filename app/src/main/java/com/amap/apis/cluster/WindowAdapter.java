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

/**
 * Created by Administrator on 2019/1/31.
 */

public class WindowAdapter implements AMap.InfoWindowAdapter,AMap.OnMarkerClickListener,AMap.OnInfoWindowClickListener{
    private Context context;
    private Cluster cluster;
    private static final String TAG = "WindowAdapter";
    private AMap aMap;
    private boolean updateFlag = true;
    public WindowAdapter(Context context, AMap mAMap) {
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
        TextView address = (TextView) view.findViewById(R.id.info_address);
        //纬度
        TextView latitude = (TextView) view.findViewById(R.id.info_latitude);
        //经度
        TextView longitude = (TextView) view.findViewById(R.id.info_longitude);

        title.setText(marker.getTitle());
        address.setText(marker.getSnippet());
        latitude.setText(marker.getPosition().latitude + "");
        longitude.setText(marker.getPosition().longitude + "");
        Log.e(TAG, "getInfoWindow1: "+marker.getTitle() );
        Log.e(TAG, "getInfoWindow: "+marker.getSnippet() );
        Log.e(TAG, "getInfoWindow: "+marker.getPosition().latitude );
        Log.e(TAG, "getInfoWindow: "+marker.getPosition().longitude );
        Log.e(TAG,marker.toString());
        return view;
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
            aMap.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
            aMap.setInfoWindowAdapter(new WindowAdapter(context,aMap));
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
