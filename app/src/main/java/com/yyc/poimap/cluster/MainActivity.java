package com.yyc.poimap.cluster;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.apis.cluster.R;
import com.yyc.poimap.cluster.demo.ItemBean;
import com.yyc.poimap.cluster.demo.RegionItem;
import com.zyyoona7.popup.EasyPopup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class MainActivity extends Activity implements ClusterRender, AMap.OnMapLoadedListener ,UpdateAdapterListener{


    private MapView mMapView;
    private AMap mAMap;

    private AMapLocationClient locationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    private int clusterRadius = 60;

    private Map<String, Drawable> mBackDrawAbles = new HashMap<String, Drawable>();

    private ClusterOverlay mClusterOverlay;
    private PoiSearch poiSearch;
    //制造假数据(如果搭建了服务器应该从服务器进行请求)
    private int[] drawables = new int[]{R.mipmap.l1, R.mipmap.l2, R.mipmap.l3, R.mipmap.l4
    , R.mipmap.l5, R.mipmap.l6, R.mipmap.l7, R.mipmap.l8, R.mipmap.l9, R.mipmap.l10};
    private String[] price = new String[]{"11 元/小时","21 元/小时","31 元/小时","23 元/小时","22 元/小时","18 元/小时","51 元/小时","41 元/小时","9 元/小时","10.5 元/小时","11.9 元/小时"};
    private String[] name = new String[]{"北京三友充电","北京国家电网","北京比亚迪","特斯拉充电","艾蓓英充电","路特易充电","拉斯特充电","三优充电","梅迪国充电","爱中华充电","华为充电"};

    //用于详情弹窗
    private WindowAdapter windowAdapter;

    public void setCurrentTitle(String currentTitle) {
        this.currentTitle = currentTitle;
    }

    private String currentTitle;

    //动态申请权限
    private void requestPr(final Bundle savedInstanceState) {
        PermissionHelper.requestLocation(new PermissionHelper.OnPermissionGrantedListener() {
            @Override
            public void onPermissionGranted() {
                mMapView.onCreate(savedInstanceState);
                init();
                initLocation();
                windowAdapter = new WindowAdapter(MainActivity.this,mAMap,poiSearch);
                mAMap.setInfoWindowAdapter(windowAdapter);
                mAMap.setOnMarkerClickListener(windowAdapter);
                mAMap.setOnInfoWindowClickListener(windowAdapter);
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMapView = (MapView) findViewById(R.id.map);
        requestPr(savedInstanceState);
    }

    private void init() {
        if (mAMap == null) {
            // 初始化地图
            mAMap = mMapView.getMap();
            mAMap.setOnMapLoadedListener(this);
        }
    }


    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    //初始化定位信息
    private void initLocation() {
        locationClient = new AMapLocationClient(getApplicationContext());
        locationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        //可在其中解析amapLocation获取相应内容。
                        Log.e("Location",aMapLocation.getLatitude()+"==="+aMapLocation.getLongitude());
                        mAMap.moveCamera(CameraUpdateFactory.zoomTo(17));
                        //将地图移动到定位点
                        mAMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
                        MyLocationStyle myLocationStyle = new MyLocationStyle();
                        mAMap.setMyLocationEnabled(true);
                        //定位一次，且将视角移动到地图中心点。
                        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
                        //设置定位蓝点的Style
                        mAMap.setMyLocationStyle(myLocationStyle);
                    }else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError","location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                    }
                }
            }
        });
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        /**
         * 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
         */
        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
        if(null != locationClient){
            locationClient.setLocationOption(mLocationOption);
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            locationClient.stopLocation();
            locationClient.startLocation();
        }
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(1000);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否允许模拟位置,默认为true，允许模拟位置
        mLocationOption.setMockEnable(false);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(30000);
        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);
        //给定位客户端对象设置定位参数
        locationClient.setLocationOption(mLocationOption);
        //启动定位
        locationClient.startLocation();

    }

    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    protected void onDestroy() {
        super.onDestroy();
        //销毁资源
        if(mClusterOverlay!=null) {
            mClusterOverlay.onDestroy();
        }
        mMapView.onDestroy();
    }

    @Override
    public void onMapLoaded() {
        //通过子线程异步加载测试数据
        new Thread() {
            public void run() {
                Random random = new Random();
                List<ClusterItem> items = new ArrayList<ClusterItem>();
                //随机10000个点，以北京为中心
                for (int i = 0; i < 10000; i++) {
                    double lat = -1;
                    double lon = -1;
                    int x = random.nextInt(4);
                    if(x==0) {
                        Log.e("CCC","负数");
                        lat = Math.random() * 0.22 + 39.908823;
                        lon = Math.random()*0.22 + 116.397470;
                    }else if(x==1){
                        lat = Math.random() * -0.22 + 39.908823;
                        lon = Math.random()* -0.22 + 116.397470;
                        Log.e("CCC","正数");
                    }else if(x==2){
                        Log.e("CCC","负数");
                        lat = Math.random() * -0.22 + 39.908823;
                        lon = Math.random()*0.22 + 116.397470;
                    }else if(x==3){
                        Log.e("CCC","负数");
                        lat = Math.random() * 0.22 + 39.908823;
                        lon = Math.random()* -0.22 + 116.397470;
                    }
//                    Log.e("eee","lat="+lat+"=======lon"+lon);
                    LatLng latLng = new LatLng(lat, lon, false);
                    String uuid = UUID.randomUUID().toString().replaceAll("-","");
                    RegionItem regionItem = new RegionItem(latLng,new ItemBean(i+"--test",uuid,price[random.nextInt(10)]
                            ,name[random.nextInt(10)]));
                    items.add(regionItem);
                }
                mClusterOverlay = new ClusterOverlay(mAMap, items, dp2px(getApplicationContext(), clusterRadius), getApplicationContext(),MainActivity.this);
                mClusterOverlay.setClusterRenderer(MainActivity.this);
            }
        }.start();
    }

    //根据聚合点数量返回不同的LOGO
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public Drawable getDrawAble(int clusterNum, Cluster cluster) {
        int radius = dp2px(getApplicationContext(), 40);
        if (clusterNum == 1) {
            Drawable bitmapDrawable = mBackDrawAbles.get(cluster.getClusterItems().get(0).getItemBean().getUrl());
            if (bitmapDrawable == null) {
                bitmapDrawable = getApplication().getResources().getDrawable(drawables[new Random().nextInt(10)],getTheme());
                mBackDrawAbles.put(cluster.getClusterItems().get(0).getItemBean().getUrl(), bitmapDrawable);
            }
            return  bitmapDrawable;
        } else if (clusterNum < 10) {
            Drawable bitmapDrawable = mBackDrawAbles.get(11);
            if (bitmapDrawable == null) {bitmapDrawable = new BitmapDrawable(null, drawCircle(radius, Color.argb(159, 210, 154, 6)));
                mBackDrawAbles.put(11+"", bitmapDrawable);
            }
            return bitmapDrawable;
        } else if (clusterNum < 15) {
            Drawable bitmapDrawable = mBackDrawAbles.get(12);
            if (bitmapDrawable == null) {
                bitmapDrawable = new BitmapDrawable(null, drawCircle(radius, Color.argb(199, 217, 114, 0)));
                mBackDrawAbles.put(12+"", bitmapDrawable);
            }
            return bitmapDrawable;
        } else {
            Drawable bitmapDrawable = mBackDrawAbles.get(13);
            if (bitmapDrawable == null) {
                bitmapDrawable = new BitmapDrawable(null, drawCircle(radius, Color.argb(235, 215, 66, 2)));
                mBackDrawAbles.put(13+"", bitmapDrawable);
            }
            return bitmapDrawable;
        }
    }

    //绘制圆形聚合点
    private Bitmap drawCircle(int radius, int color) {
        Bitmap bitmap = Bitmap.createBitmap(radius * 2, radius * 2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        RectF rectF = new RectF(0, 0, radius * 2, radius * 2);
        paint.setColor(color);
        canvas.drawArc(rectF, 0, 360, true, paint);
        return bitmap;
    }


     //dp转px，用于简单的适配
    public int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    //详情气泡的显示与隐藏条件
    public boolean isUpdateFlag() {
        return updateFlag;
    }

    public void setUpdateFlag(boolean updateFlag) {
        this.updateFlag = updateFlag;
    }

    private boolean updateFlag = false;

    //详情气泡的更新
    @Override
    public void onAdapterUpdate(List<Cluster> clusters, boolean updateFlag) {
        if(clusters!=null){
            for (Cluster cluster:clusters){
                for(ClusterItem clusterItem:cluster.getClusterItems()){
                    if(clusterItem.getItemBean()!=null&&clusterItem.getItemBean().getmTitle().equals(currentTitle)){
                        if(!cluster.getMarker().isInfoWindowShown()) {
                            this.updateFlag = updateFlag;
                            cluster.getMarker().showInfoWindow();
                        }
                        if(cluster.getClusterItems().size()>1){
                            this.updateFlag = updateFlag;
                            cluster.getMarker().hideInfoWindow();
                        }
                    }
                }
            }
        }
    }
}
