package com.example.SerarchNearBy;

import android.app.Activity;
import android.content.Intent;
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
import com.baidu.mapapi.map.LocationData;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyActivity extends Activity {
        private ListView indexListView;
        public  static double lng;
        public  static double lat;
        private List<Map<String,Object>> dataList = new ArrayList<Map<String, Object>>();
        private BDLocation currentLocation;
        private SimpleAdapter listViewAdapter ;
        private LocationClient locationClient;
        private TextView bottomTextView;
        private ImageButton settingImageButton;
        public static   String[] strings = new String[]{"餐饮服务","购物服务","生活服务","体育休闲服务","医疗保健服务","住宿服务","科教文化服务","交通设施服务","公共设施服务"};
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.main);
            bottomTextView = (TextView) findViewById(R.id.index_bottom_textView);
            indexListView = (ListView) findViewById(R.id.index_listView);
            settingImageButton = (ImageButton) findViewById(R.id.settingImageButton);
            settingImageButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent intent = new Intent(MyActivity.this,SettingActivity.class);
                    startActivity(intent);
                }
            });
            listViewAdapter  = new SimpleAdapter(this,dataList,R.layout.index_content_item,new String[]{"text"},new int[]{R.id.index_content_list_textView})
            {
                public View getView(int position, View convertView, ViewGroup parent) {
                    if (convertView==null)
                    {
                        LayoutInflater layoutInflater = getLayoutInflater() ;
                        convertView = layoutInflater.inflate(R.layout.index_content_item,parent,false);
                    }
                    Map<String,Object> map = (Map<String, Object>) getItem(position);
                    TextView textView= (TextView) convertView.findViewById(R.id.index_content_list_textView);
                    Button secondutton= (Button) convertView.findViewById(R.id.index_content_next_button);
                    final  int index = position;
                    final  String title = map.get("text").toString();
                    textView.setText(title);
                    textView.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            //itemClick(index,title);
                        }
                    });
                    secondutton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            gotoNextView(index,title);
                        }
                    });
                    return  convertView;
                }
            };
            indexListView.setAdapter(listViewAdapter);


            locationClient = new LocationClient(getApplicationContext());
            locationClient.registerLocationListener(new BDLocationListener() {

                public void onReceiveLocation(BDLocation bdLocation) {
                    //定位成功后，关闭location client，不然通知栏会一直有gps定位中得图标
                    locationClient.stop();
                    Toast.makeText(MyActivity.this, "定位完毕 " + bdLocation.getAddrStr(), Toast.LENGTH_SHORT).show();
                    doReceiveLocation(bdLocation);
                }

                public void onReceivePoi(BDLocation bdLocation) {
                    Toast.makeText(MyActivity.this, "定位完毕 " + bdLocation.getAddrStr(), Toast.LENGTH_SHORT).show();
                }
            });
            LocationClientOption locationClientOption = new LocationClientOption();

            locationClientOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置定位模式
            locationClientOption.setCoorType("bd09ll");//返回的定位结果是百度经纬度，默认值gcj02
            locationClientOption.setScanSpan(0);//设置发起定位请求的间隔时间为5000ms
            locationClientOption.setIsNeedAddress(true);//返回的定位结果包含地址信息
            locationClientOption.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向

            locationClient.setLocOption(locationClientOption);
            locationOnclick(bottomTextView);
        }

//        private void itemClick(int position,String title) {
//            Intent intent = new Intent(this,ListActivity.class);
//            intent.putExtra(SecondActivity.dataIndex,position);
//            intent.putExtra(SecondActivity.dataText,title);
//            startActivity(intent);
//        }

        @Override
        protected void onResume() {
            initData();
            super.onResume();
        }

        private void initData() {
            dataList.clear();
            for (int i = 0;i<strings.length;i++){
                Map<String,Object> map = new HashMap<String, Object>();
                map.put("text",strings[i]);
                //map.put(SecondActivity.dataIndex,i);
                dataList.add(map);
            }
            listViewAdapter.notifyDataSetChanged();
        }

        private void gotoNextView(int index,String title) {
            Intent intent = new Intent(MyActivity.this,SecondActivity.class);
            intent.putExtra(SecondActivity.dataIndex,index);
            intent.putExtra(SecondActivity.dataText,title);
            startActivity(intent);
        }

    private void doReceiveLocation(BDLocation bdLocation) {
            currentLocation = bdLocation;

            Log.d(getClass().getName(), "Current location, " + bdLocation.getLatitude() + " " + bdLocation.getLongitude());

            setTitle(bdLocation.getAddrStr());

            //让地图中心点移动
            GeoPoint geoPoint = new GeoPoint((int) (bdLocation.getLatitude() * 1E6), (int) (bdLocation.getLongitude() * 1E6));

            LocationData locationData = new LocationData();
            locationData.latitude = bdLocation.getLatitude();
            locationData.longitude = bdLocation.getLongitude();

            bottomTextView.setText(bdLocation.getAddrStr());
        }
        public void locationOnclick(View view)
        {
            locationClient.start();
            bottomTextView.setText("");
            Toast.makeText(this, "正在定位……", Toast.LENGTH_SHORT).show();
            locationClient.requestLocation();
        }
    }


