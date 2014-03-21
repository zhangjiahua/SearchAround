package com.example.SerarchNearBy;

import android.app.Activity;

import android.graphics.drawable.Drawable;

import android.os.Bundle;

import android.util.Log;

import android.view.View;
import android.view.Window;
import android.widget.*;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.search.*;
import com.baidu.platform.comapi.basestruct.GeoPoint;

/**
 * Created by Administrator on 14-3-18.
 */
public class MapActivity extends Activity {
    private MapView mapView = null;
    private LocationClient locationClient;
    private MapController mapController;
    private MyLocationOverlay myLocationOverlay;

    private MKSearch search;
    private int selectedPoiItemIndex;
    private BDLocation getCurrentLocation;
    private Double longitude, latitude;
    private TextView textviewphone, textviewrootMessage, textviewaddressname, textviewaddressmessage;
    private ImageButton seting_back, btroot;
    private Button btfoot, btbus, btcar;
    private String distance;
    private String addressname;
    private String address;
    private String phone;
    private double time;
    private String hour;
    private final Double BUSSPEED = 12.76d, FOOTSPEED = 4.3d, CARSPEED = 30d;
    private final int FOOT = 1, BUS = 2, CAR = 3;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//这个是取消title
        //注意：请在试用setContentView前初始化BMapManager对象，否则会报错
        setContentView(R.layout.mapview);
        //接受intent传值
        getIntentPamars();
        //初始化数据调用
        init();

        mapView = (MapView) findViewById(R.id.bmapsView);
        mapView.setBuiltInZoomControls(true);
        //设置启用内置的缩放控件
        mapController = mapView.getController();
        // 得到mMapView的控制权,可以用它控制和驱动平移和缩放
        //      GeoPoint point = new GeoPoint((int) (34.918 * 1E6), (int) (108.404 * 1E6));
        //设置当前定位为中心点
        // 用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)
        //     mapController.setCenter(point);//设置地图中心点
        mapController.setZoom(12);

        locationClient = new LocationClient(this);
        locationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                //定位成功后，关闭location client，不然通知栏会一直有gps定位中得图标
                locationClient.stop();


                doReceiveLocation(bdLocation);
            }

            @Override
            public void onReceivePoi(BDLocation bdLocation) {

            }
        });

        LocationClientOption locationClientOption = new LocationClientOption();

        locationClientOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置定位模式
        locationClientOption.setCoorType("bd09ll");//返回的定位结果是百度经纬度，默认值gcj02
        locationClientOption.setScanSpan(0);//设置发起定位请求的间隔时间为5000ms
        locationClientOption.setIsNeedAddress(true);//返回的定位结果包含地址信息
        locationClientOption.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向

        locationClient.setLocOption(locationClientOption);
        //开始定位
        locateMe();

        serchpoi();
        //绘画路线
        onclickbtRoot(FOOT);

    }

    private String gettime(String distance, int style) {
        double length = 0;
        if (distance.contains("km")) {
            length = Double.parseDouble(distance.substring(0, distance.length() - 2));
        } else if (distance.contains("m")) {
            length = Double.parseDouble(distance.substring(0, distance.length() - 1)) / 1000;
        }
        switch (style) {
            case 1:
                time = (length * 1 / FOOTSPEED);
                break;
            case 2:
                time = (length * 1 / BUSSPEED);
                break;
            case 3:
                time = (length * 1 / CARSPEED);
                break;
        }
        hour = (int) (time * 60) + "";
        if (time * 60 < 1) {
            hour = "1";
        }
        return hour;
    }

    //接受Intent的传值
    private void getIntentPamars() {
        getCurrentLocation = getIntent().getParcelableExtra("currentLocation");
        longitude = getIntent().getDoubleExtra("longitude", 0);
        latitude = getIntent().getDoubleExtra("latitude", 0);
        phone = getIntent().getStringExtra("phone");
        addressname = getIntent().getStringExtra("addressname");
        address = getIntent().getStringExtra("address");
        distance = getIntent().getStringExtra("distance");
    }

    //初始化数据
    private void init() {

        textviewphone = (TextView) findViewById(R.id.textviewphone);


        textviewrootMessage = (TextView) findViewById(R.id.textviewrootMessage);
        textviewrootMessage.setTextColor(0xffff00ff);
        //默认路线为步行
        textviewrootMessage.setText("全程约" + distance + ",耗时约" + gettime(distance, FOOT) + "分钟");

        textviewaddressmessage = (TextView) findViewById(R.id.textviewaddressmessage);
        textviewaddressmessage.setText(address);

        textviewaddressname = (TextView) findViewById(R.id.textviewaddressname);
        textviewaddressname.setText(addressname);


        seting_back = (ImageButton) findViewById(R.id.seting_back);
        seting_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btcar = (Button) findViewById(R.id.btcar);
        btcar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RouteOverlay routeOverlay = new RouteOverlay(MapActivity.this, mapView);
                //清除其他图层
                if (routeOverlay != null) {
                    mapView.getOverlays().remove(routeOverlay);
                }
                onclickbtRoot(CAR);
                String str = gettime(distance, CAR);
                btbus.setBackgroundResource(R.color.background);
                btfoot.setBackgroundResource(R.color.background);
                textviewrootMessage.setText("全程约" + distance + ",耗时约" + str + "分钟");
                btcar.setBackgroundResource(R.drawable.bg_tab_top);
            }
        });

        btbus = (Button) findViewById(R.id.btbus);
        btbus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RouteOverlay routeOverlay = new RouteOverlay(MapActivity.this, mapView);
                //清除其他图层
                if (routeOverlay != null) {
                    mapView.getOverlays().remove(routeOverlay);
                }
                onclickbtRoot(BUS);
                String str = gettime(distance, BUS);
                textviewrootMessage.setText("全程约" + distance + ",耗时约" + str + "分钟");
                btcar.setBackgroundResource(R.color.background);
                btfoot.setBackgroundResource(R.color.background);
                btbus.setBackgroundResource(R.drawable.bg_tab_top);
            }
        });
        btfoot = (Button) findViewById(R.id.btfoot);
        btfoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RouteOverlay routeOverlay = new RouteOverlay(MapActivity.this, mapView);
                //清除其他图层
                if (routeOverlay != null) {
                    mapView.getOverlays().remove(routeOverlay);
                }
                onclickbtRoot(FOOT);
                String str = gettime(distance, FOOT);
                textviewrootMessage.setText("全程约" + distance + ",耗时约" + str + "分钟");
                btcar.setBackgroundResource(R.color.background);
                btbus.setBackgroundResource(R.color.background);
                btfoot.setBackgroundResource(R.drawable.bg_tab_top);
            }
        });

        btroot = (ImageButton) findViewById(R.id.btroot);
        btroot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onclickbtRoot(FOOT);
            }
        });
        //获取电话
        getphone();

    }

    private void getphone() {
        if ("".equals(phone)) {
            textviewphone.setText("暂无电话");
        } else {
            textviewphone.setText(phone);
        }
    }

    private void onclickbtRoot(int style) {

        GeoPoint startPoint = new GeoPoint((int) (getCurrentLocation.getLatitude() * 1E6),
                (int) (getCurrentLocation.getLongitude() * 1E6));
        MKPlanNode startNode = new MKPlanNode();
        startNode.pt = startPoint;

        GeoPoint endPoint = new GeoPoint((int) (latitude * 1E6),
                (int) (longitude * 1E6));
        MKPlanNode endNode = new MKPlanNode();
        endNode.pt = endPoint;
        search = new MKSearch();
        //事件监听
        search.init(MyApplication.getInstance().mBMapManager, new MKSearchListener() {
            @Override
            public void onGetPoiResult(MKPoiResult mkPoiResult, int i, int i2) {

            }

            @Override
            public void onGetTransitRouteResult(MKTransitRouteResult mkTransitRouteResult, int i) {
                displayTransit(mkTransitRouteResult, i);
            }

            @Override
            public void onGetDrivingRouteResult(MKDrivingRouteResult mkDrivingRouteResult, int i) {
                displayDriving(mkDrivingRouteResult, i);
            }

            @Override
            public void onGetWalkingRouteResult(MKWalkingRouteResult mkWalkingRouteResult, int i) {
                Log.d(getClass().getName(), "onGetWalkingRouteResult " + mkWalkingRouteResult);

                displayWalking(mkWalkingRouteResult, i);


            }

            @Override
            public void onGetAddrResult(MKAddrInfo mkAddrInfo, int i) {

            }

            @Override
            public void onGetBusDetailResult(MKBusLineResult mkBusLineResult, int i) {
                if (i != 0 || mkBusLineResult == null) {
                    RouteOverlay routeOverlay = new RouteOverlay(MapActivity.this, mapView);
                    mapView.getOverlays().remove(routeOverlay);
                    Toast.makeText(MapActivity.this, "抱歉，未找到结果", Toast.LENGTH_LONG).show();
                    return;
                }
            }

            @Override
            public void onGetSuggestionResult(MKSuggestionResult mkSuggestionResult, int i) {

            }

            @Override
            public void onGetPoiDetailSearchResult(int i, int i2) {

            }

            @Override
            public void onGetShareUrlResult(MKShareUrlResult mkShareUrlResult, int i, int i2) {

            }
        });

        //开始画路线
        switch (style) {
            case FOOT:
                search.walkingSearch(getCurrentLocation.getCity(), startNode,
                        getCurrentLocation.getCity(), endNode);
                break;
            case BUS:
                search.transitSearch(getCurrentLocation.getCity(), startNode, endNode);
                break;
            case CAR:
                search.drivingSearch(getCurrentLocation.getCity(), startNode,
                        getCurrentLocation.getCity(), endNode);
                break;
        }
    }

    private void displayWalking(MKWalkingRouteResult mkWalkingRouteResult, int i) {
        RouteOverlay routeOverlay = new RouteOverlay(MapActivity.this, mapView);
        //清除其他图层
        if (routeOverlay != null) {
            mapView.getOverlays().remove(routeOverlay);
        }

        routeOverlay.setData(mkWalkingRouteResult.getPlan(0).getRoute(0));

        mapView.getOverlays().add(routeOverlay);
        //执行刷新使生效
        mapView.refresh();
        // 使用zoomToSpan()绽放地图，使路线能完全显示在地图上
        mapView.getController().zoomToSpan(routeOverlay.getLatSpanE6(), routeOverlay.getLonSpanE6());

    }

    private void displayDriving(MKDrivingRouteResult mkDrivingRouteResult, int i) {
        RouteOverlay routeOverlay = new RouteOverlay(MapActivity.this, mapView);
        //清除其他图层
        if (routeOverlay != null) {
            mapView.getOverlays().remove(routeOverlay);
        }

        routeOverlay.setData(mkDrivingRouteResult.getPlan(0).getRoute(0));

        mapView.getOverlays().add(routeOverlay);
        //执行刷新使生效
        mapView.refresh();
        // 使用zoomToSpan()绽放地图，使路线能完全显示在地图上
        mapView.getController().zoomToSpan(routeOverlay.getLatSpanE6(), routeOverlay.getLonSpanE6());

    }

    private void displayTransit(MKTransitRouteResult mkTransitRouteResult, int i) {
        RouteOverlay routeOverlay = new RouteOverlay(MapActivity.this, mapView);
        //清除其他图层

        if(routeOverlay != null){
            Toast.makeText(MapActivity.this,"抱歉未找到路线",Toast.LENGTH_SHORT).show();
            mapView.getOverlays().remove(routeOverlay);
            return;
        }
        routeOverlay.setData(mkTransitRouteResult.getPlan(0).getRoute(0));

        mapView.getOverlays().add(routeOverlay);
        //执行刷新使生效
        mapView.refresh();
        // 使用zoomToSpan()绽放地图，使路线能完全显示在地图上
        mapView.getController().zoomToSpan(routeOverlay.getLatSpanE6(), routeOverlay.getLonSpanE6());

    }

    private void locateMe() {

        if (!locationClient.isStarted()) {
            locationClient.start();
        }
        locationClient.requestLocation();

    }

    private void doReceiveLocation(BDLocation bdLocation) {

        getCurrentLocation = bdLocation;

        Log.d(getClass().getName(), "Current location, " + bdLocation.getLatitude() + " " + bdLocation.getLongitude());


        //让地图中心点移动
        GeoPoint geoPoint = new GeoPoint((int) (bdLocation.getLatitude() * 1E6), (int) (bdLocation.getLongitude() * 1E6));
        //mapController.setCenter(geoPoint);
        mapController.animateTo(geoPoint);

        //添加当前位置覆盖物
        if (myLocationOverlay != null) {
            mapView.getOverlays().remove(myLocationOverlay);
        }
        Drawable mark = getResources().getDrawable(R.drawable.ic_current_loc);
        myLocationOverlay = new MyLocationOverlay(mapView);
        LocationData locationData = new LocationData();
        locationData.latitude = bdLocation.getLatitude();
        locationData.longitude = bdLocation.getLongitude();
        myLocationOverlay.setMarker(mark);
        myLocationOverlay.setData(locationData);

        //添加图层
        mapView.getOverlays().add(myLocationOverlay);

        mapView.refresh();
    }


    private void serchpoi() {
        GeoPoint startPoint = new GeoPoint((int) (getCurrentLocation.getLatitude() * 1E6),
                (int) (getCurrentLocation.getLongitude() * 1E6));
        GeoPoint endPoint = new GeoPoint((int) (latitude * 1E6),
                (int) (longitude * 1E6));

        Drawable smark = getResources().getDrawable(R.drawable.ic_loc_from);
        Drawable emark = getResources().getDrawable(R.drawable.ic_loc_to);
//用OverlayItem准备Overlay数据
        OverlayItem item1 = new OverlayItem(startPoint, "item1", "item1");
        item1.setMarker(smark);
//使用setMarker()方法设置overlay图片,如果不设置则使用构建ItemizedOverlay时的默认设置
        OverlayItem item2 = new OverlayItem(endPoint, "item2", "item2");
        item2.setMarker(emark);
        //创建IteminizedOverlay
        PoiOverlay itemOverlay = new PoiOverlay(smark, mapView);
        //将IteminizedOverlay添加到MapView中
        mapView.getOverlays().add(itemOverlay);
        itemOverlay.addItem(item1);
        itemOverlay.addItem(item2);
        mapView.refresh();
    }


    @Override
    protected void onDestroy() {
        mapView.destroy();

        super.onDestroy();
    }

    @Override
    protected void onPause() {
        mapView.onPause();

        super.onPause();
    }

    @Override
    protected void onResume() {
        mapView.onResume();

        super.onResume();
    }


    class PoiOverlay extends ItemizedOverlay {

        public PoiOverlay(Drawable drawable, MapView mapView) {
            super(drawable, mapView);
        }

        @Override
        public boolean onTap(GeoPoint geoPoint, MapView mapView) {
            Toast.makeText(MapActivity.this, "PoiOverlay onTap", Toast.LENGTH_SHORT).show();
            return super.onTap(geoPoint, mapView);
        }

        @Override
        protected boolean onTap(int i) {
            selectedPoiItemIndex = i;

            Toast.makeText(MapActivity.this, "PoiOverlay onTap at " + i, Toast.LENGTH_SHORT).show();


            return super.onTap(i);
        }
    }
}