package com.example.SerarchNearBy;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class MapListActivity  extends Activity {
    private MapView mapView;
    private MapController mapController;
    private TextView titleTextView;
    private LayoutInflater layoutInflater;
    private View mapPopWindow;
    private TextView spinner;
    private String tempText;
    private int  tempRange = 3000;
    private int  temp = 1;
    private PoiOverlay<OverlayItem> itemItemizedOverlay;
    private Button leftButton;
    private Button rightButton;
    public static  String[] spn1Data  = new String[] { "1000米内", "2000米内", "3000米内","4000米内","5000米内","6000米内" };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyApplication app = (MyApplication)this.getApplication();
        if (app.mBMapManager == null) {
            app.mBMapManager = new BMapManager(this);
            app.mBMapManager.init(MyApplication.strKey,new MyApplication.MyGeneralListener()
            );
        }
        setContentView(R.layout.map);
        titleTextView = (TextView) findViewById(R.id.map_title_textView);
        spinner = (TextView) findViewById(R.id.map_spinner);


        mapView = (MapView) findViewById(R.id.BDMap);
//             获取地图控制
        mapController = mapView.getController();
        mapController.setZoom(14);
        mapView.getController().enableClick(true);
//        mapView.setBuiltInZoomControls(true);
        Drawable marker = getResources().getDrawable(R.drawable.ic_loc_normal);
        itemItemizedOverlay = new PoiOverlay<OverlayItem>(marker, mapView);
//
        leftButton = (Button) findViewById(R.id.map_left_button);
        rightButton = (Button) findViewById(R.id.map_right_button);
        layoutInflater = LayoutInflater.from(this);
        mapPopWindow = layoutInflater.inflate(R.layout.custom_text_view, null);
        mapPopWindow.setVisibility(View.GONE);
        mapView.addView(mapPopWindow);
        Intent intent = getIntent();
        String title = intent.getStringExtra(SecondActivity.dataText);
        titleTextView.setText(title);
        tempText= title;
        initData(title);
        spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MapListActivity.this);
                builder.setItems(spn1Data,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        spinner.setText("范围："+spn1Data[which]);
                        switch (which)
                        {
                            case  0:

                                tempRange = 1000;
                                initData(tempText);
                                break;
                            case  1:
                                tempRange =2000;
                                initData(tempText);
                                break;
                            case  2:
                                tempRange = 3000;
                                initData(tempText);
                                break;
                            case  3:
                                tempRange = 4000;
                                initData(tempText);
                                break;
                            case  4:
                                tempRange = 5000;
                                initData(tempText);
                                break;
                            case  5:
                                tempRange = 6000;
                                initData(tempText);
                                break;


                        }
                    }
                });
                builder.create().show();
            }
        });
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (temp>1)
                {
                    temp-=1;
                    initData(tempText);
                }else
                {
                    Toast.makeText(MapListActivity.this,"这是第一页",Toast.LENGTH_SHORT);

                }
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (temp<40)
                {
                    temp+=1;

                    initData(tempText);
                }else
                {

                    Toast.makeText(MapListActivity.this,"这是最后一页",Toast.LENGTH_SHORT);
                }
            }
        });
    }

    @Override
    protected void onResume() {

        mapView.onResume();
        super.onResume();
    }


    private void initData(String text) {

        final String title = text;

        final ProgressDialog progressDialog = new ProgressDialog(MapListActivity.this);
        progressDialog.setMessage("刷新中。。。。");

        AsyncTask<Integer, Float, Integer> asyncTask = new AsyncTask<Integer, Float, Integer>() {


            @Override
            protected Integer doInBackground(Integer... string) {
                String php ="https://api.weibo.com/2/location/pois/search/by_geo.json";
                String coordinate = MyApplication.locData.longitude+","+MyApplication.locData.latitude;
                String centre =title;

                String url_username =php+"?coordinate="+coordinate+"&q="+centre+"&access_token=2.00KZZWLEREyGdC3cba17f3a70wTEoO&range="+tempRange+"&page="+temp+"&count=20";
                try {
                    mapView.getOverlays().clear();
                    itemItemizedOverlay.removeAll();
                    String requestStr  = Tools.requestServerDate(url_username);
                    JSONObject jsonObject = new JSONObject(requestStr);
                    JSONArray ja = jsonObject.getJSONArray("poilist");
                    GeoPoint geoPoint=new GeoPoint((int) (MyApplication.locData.latitude * 1E6), (int) (MyApplication.locData.longitude * 1E6));
                    int MaxX=0;
                    int MinY=(int)(MyApplication.locData.latitude*1E6);
                    int MinX=(int)(MyApplication.locData.longitude*1E6);
                    int MaxY=0;
                    OverlayItem myOverlayItem = new OverlayItem(geoPoint, "我的位置", "");
                    myOverlayItem.setMarker(getResources().getDrawable(R.drawable.ic_current_loc));
                    mapController.setCenter(geoPoint);
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jo = (JSONObject) ja.get(i);
                        int y = (int)(jo.getDouble("y")*1E6);
                        int x = (int)(jo.getDouble("x")*1E6);
                        MaxX =  Math.max(MaxX,x);
                        MinX =  Math.min(MinX,x);
                        MaxY =  Math.max(MaxY,y);
                        MinY =  Math.min( MinY,y);
                        GeoPoint point = new GeoPoint(y,x);
                        OverlayItem overlayItem = new OverlayItem(point, jo.getString("name"),  jo.getString("address"));
                        itemItemizedOverlay.addItem(overlayItem);
//                      获取最大值
//                      mapView.getLongitudeSpan();
//                      mapView.getLatitudeSpan();
                    }
                    itemItemizedOverlay.addItem(myOverlayItem);
                    mapController.zoomToSpan(Math.abs(MaxY-MinY),Math.abs(MaxX-MinX));
                    mapView.getOverlays().add(itemItemizedOverlay);
                } catch (IOException e) {
                    return ListActivity.ERROR_SERVER;
                } catch (JSONException e) {
                    return ListActivity.ERROR_DATA_FORMAT;
                }


                return ListActivity.SUSSES;
            }

            @Override
            protected void onPreExecute() {
                progressDialog.show();
            }

            @Override
            protected void onPostExecute(Integer request) {
                progressDialog.dismiss();
                if (request == ListActivity.SUSSES) {
                    mapView.refresh();
                } else if (request ==ListActivity.ERROR_SERVER)
                {
                    showServerErrorMessage();

                } else if (request ==ListActivity.ERROR_DATA_FORMAT)
                {
                    showDataErrorMessage();
                }
            }
        };
        asyncTask.execute(0);
    }



    private void showDataErrorMessage() {
        Toast.makeText(this, "数据格式错误", Toast.LENGTH_LONG).show();
    }

    private void showServerErrorMessage() {
        Toast.makeText(this,"请求数据失败",Toast.LENGTH_LONG).show();
    }

    public void goBackClick(View view){
        this.finish();
    }

    class PoiOverlay<OverlayItem> extends ItemizedOverlay {

        public PoiOverlay(Drawable drawable, MapView mapView) {
            super(drawable, mapView);
        }

        @Override
        protected boolean onTap(int i) {
            com.baidu.mapapi.map.OverlayItem item = itemItemizedOverlay.getItem(i);
            final GeoPoint point = item.getPoint();
            String title = item.getTitle();
            String content = item.getSnippet();

            TextView titleTextView = (TextView) mapPopWindow.findViewById(R.id.custom_name_textView);
            TextView contentTextView = (TextView) mapPopWindow.findViewById(R.id.custom_text_textView);
            titleTextView.setText(title);
            contentTextView.setText(content);
            contentTextView.setVisibility(View.VISIBLE);

            MapView.LayoutParams layoutParam = new MapView.LayoutParams(
                    //控件宽,继承自ViewGroup.LayoutParams
                    MapView.LayoutParams.WRAP_CONTENT,
                    //控件高,继承自ViewGroup.LayoutParams
                    MapView.LayoutParams.WRAP_CONTENT,
                    //使控件固定在某个地理位置
                    point,
                    0,
                    -40,
                    //控件对齐方式
                    MapView.LayoutParams.BOTTOM_CENTER);

            mapPopWindow.setVisibility(View.VISIBLE);

            mapPopWindow.setLayoutParams(layoutParam);
            mapPopWindow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MapListActivity.this,JumpActivity.class);
//                    mapView.getOverlays().clear();
                    intent.putExtra(JumpActivity.POILatitude,point.getLatitudeE6());
                    intent.putExtra(JumpActivity.POILongitude,point.getLongitudeE6());
                    startActivity(intent);
                }
            });

            mapController.animateTo(point);

            return super.onTap(i);
        }

        @Override
        public boolean onTap(GeoPoint geoPoint, MapView mapView) {
            mapPopWindow.setVisibility(View.GONE);

            return super.onTap(geoPoint, mapView);    //To change body of overridden methods use File | Settings | File Templates.
        }
    }

    @Override
    protected void onPause() {
//        mapPopWindow.setVisibility(View.GONE);
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.destroy();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mapView.onRestoreInstanceState(savedInstanceState);
    }

    public void onClickRefresh(View view)
    {
        initData(tempText);
    }

}