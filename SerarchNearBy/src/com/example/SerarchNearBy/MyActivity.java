package com.example.SerarchNearBy;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyActivity extends Activity {
    private ListView indexListView;
    public static double lng;
    public static double lat;
    private List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    private BDLocation currentLocation;
    private SimpleAdapter listViewAdapter;
    private LocationClient locationClient;
    private TextView bottomTextView;
    private ImageButton settingImageButton;
    private ImageButton localbar;
    private LocationClientOption option;
    public static String[] strings = new String[]{"餐饮服务", "购物服务", "生活服务", "体育休闲服务", "医疗保健服务", "住宿服务", "科教文化服务", "交通设施服务", "公共设施服务"};

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

        //显示定位信息的TextView
        bottomTextView = (TextView) findViewById(R.id.index_bottom_textView);

        indexListView = (ListView) findViewById(R.id.index_listView);

        locationClient = new LocationClient(this);
        //点击按钮定位
        localbar = (ImageButton) findViewById(R.id.localbar);
        localbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomTextView.setText("正在定位请稍等......");
                requestLocation();
            }
        });

        settingImageButton = (ImageButton) findViewById(R.id.settingImageButton);
        settingImageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MyActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
        listViewAdapter = new SimpleAdapter(this, dataList, R.layout.index_content_item, new String[]{"text"}, new int[]{R.id.index_content_list_textView}) {
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    LayoutInflater layoutInflater = getLayoutInflater();
                    convertView = layoutInflater.inflate(R.layout.index_content_item, parent, false);
                }
                Map<String, Object> map = (Map<String, Object>) getItem(position);
                TextView textView = (TextView) convertView.findViewById(R.id.index_content_list_textView);
                Button secondutton = (Button) convertView.findViewById(R.id.index_content_next_button);
                final int index = position;
                final String title = map.get("text").toString();
                textView.setText(title);
                textView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        //itemClick(index,title);
                    }
                });
                secondutton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        gotoNextView(index, title);
                    }
                });
                return convertView;
            }
        };
        indexListView.setAdapter(listViewAdapter);
        //  locationOnclick(bottomTextView);

        getLocationAsyncTask();
    }
    public void getLocationAsyncTask() {
        option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置定位模式
        option.setCoorType("bd09ll");//返回的定位结果是百度经纬度，默认值gcj02
        option.setScanSpan(0);//设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);//返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向
        locationClient = new LocationClient(getApplicationContext());
        AsyncTask<String, Integer, Boolean> asyncTask = new AsyncTask<String, Integer, Boolean>() {
            @Override
            protected Boolean doInBackground(String... params) {
                locationClient.registerLocationListener(new BDLocationListener() {
                    public void onReceiveLocation(BDLocation bdLocation) {
                        if (bdLocation.getAddrStr() == null) {
                            locationClient.stop();
                            bottomTextView.setText("获得定位失败，请稍后重试");
                        } else {
                            locationClient.stop();
                            bottomTextView.setText(bdLocation.getAddrStr());
                            currentLocation = bdLocation;
                        }
                    }
                    @Override
                    public void onReceivePoi(BDLocation bdLocation) {
                        Log.d("TAG", "onReceivePoi");
                    }
                });
                locationClient.setLocOption(option);
                return null;
            }
        };
        asyncTask.execute();
    }
    //打开定位
    private void requestLocation() {
        if (!locationClient.isStarted()) {
            locationClient.start();//打开定位
        }
        locationClient.requestLocation();
    }

    @Override
    protected void onResume() {
        initData();
        super.onResume();
    }

    private void initData() {
        dataList.clear();
        for (int i = 0; i < strings.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("text", strings[i]);
            //map.put(SecondActivity.dataIndex,i);
            dataList.add(map);
        }
        listViewAdapter.notifyDataSetChanged();
    }

    private void gotoNextView(int index, String title) {
        Intent intent = new Intent(MyActivity.this, SecondActivity.class);
        intent.putExtra(SecondActivity.dataIndex, index);
        intent.putExtra(SecondActivity.dataText, title);
        startActivity(intent);
    }


}

