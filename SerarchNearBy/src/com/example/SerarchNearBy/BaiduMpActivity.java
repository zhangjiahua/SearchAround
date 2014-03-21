package com.example.SerarchNearBy;

import android.app.Activity;
import android.content.Intent;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.platform.comapi.basestruct.GeoPoint;
public class BaiduMpActivity extends Activity {
    private BMapManager bMapManager = null;
    private static  final String  strKey = "7b856da0b9349192f6bf6151a703e24c";
    private LayoutInflater layoutInflater;
    private LocationClient locationClient;
    private MapView mapView;
    private CheckBox satelliteCheckBox;
    private CheckBox trafficCheckBox;
    private CheckBox builtInZoomControlsCheckBox;
    private MapController mapController;
    private TextView titleTextView;
    LocationListener mLocationListener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        bMapManager = new BMapManager(this);

        boolean initResult = bMapManager.init(strKey,new MKGeneralListener() {
            public void onGetNetworkState(int i) {
                if (i == MKEvent.ERROR_NETWORK_CONNECT)
                {
                    Toast.makeText(BaiduMpActivity.this,"您的网络连接错误",Toast.LENGTH_LONG).show();

                }
                if (i== MKEvent.ERROR_NETWORK_DATA)
                {
                    Toast.makeText(BaiduMpActivity.this,"请正确输入索引文件",Toast.LENGTH_LONG).show();
                }
            }
            public void onGetPermissionState(int i) {
                if (i == MKEvent.ERROR_PERMISSION_DENIED)
                {
                    Toast.makeText(BaiduMpActivity.this ,"请输入正确的strKey值",Toast.LENGTH_LONG).show();
                }
            }
        }) ;
        if (!initResult)
        {
            Toast.makeText(BaiduMpActivity.this,"  BapManager   初始化错误",Toast.LENGTH_LONG).show();
        }

        setContentView(R.layout.baidumap);
        titleTextView = (TextView) findViewById(R.id.map_title_textView);
        layoutInflater = LayoutInflater.from(this);

        mapView = (MapView) findViewById(R.id.BDMap);
        satelliteCheckBox = (CheckBox) findViewById(R.id.map_satellite_checkBox);
        trafficCheckBox = (CheckBox) findViewById(R.id.map_traffic_checkBox);
        builtInZoomControlsCheckBox = (CheckBox) findViewById(R.id.map_BuiltInZoomControls_checkBox);
        mapController = mapView.getController();
        mapController.setZoom(14);
        GeoPoint geoPoint=new GeoPoint((int) (MyApplication.locData.latitude * 1E6), (int) (MyApplication.locData.longitude * 1E6));
        mapController.setCenter(geoPoint);

    }
    protected void onResume() {
        Intent intent = getIntent();

        String title = intent.getStringExtra(SecondActivity.dataText);
        titleTextView.setText(title);
        super.onResume();
    }

    public void Check1OnClick(View view)
    {

        if (satelliteCheckBox.isChecked())
        {
            mapView.setSatellite(true);
        }else
        {
            mapView.setSatellite(false);
        }
    }
    public void Check2OnClick(View view)
    {
        if (trafficCheckBox.isChecked())
        {
            mapView.setTraffic(true);
        }else
        {
            mapView.setTraffic(false);
        }
    }
    public void Check3OnClick(View view)
    {
        if (builtInZoomControlsCheckBox.isChecked())
        {
            mapView.setBuiltInZoomControls(true);
        }else
        {
            mapView.setBuiltInZoomControls(false);
        }
    }
    public void goBackClick(View view){
        this.finish();
    }
}

