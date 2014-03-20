package com.example.SerarchNearBy;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.*;
import com.baidu.location.BDLocation;
import com.example.SerarchNearBy.view.CustomListView;
import com.example.SerarchNearBy.view.MapActivity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Administrator on 14-3-10.
 */
public class SerchActivity extends Activity {
    private ImageButton back,goserch;
    private EditText condition;
    private SimpleAdapter simpleAdapter=null;
    private BDLocation currentLocation;
    private JSONObject rootJsonObject;
    private int load_Index = 1;
    private ArrayList<HashMap<String, String>> datas = new ArrayList<HashMap<String, String>>();
    private  ArrayList<HashMap<String,Double>> locatondatas = new  ArrayList<HashMap<String, Double>>();

    private static final String TAG = "MyActivity";

    private static final int LOAD_DATA_FINISH = 10;//表示加载数据
    private static final int REFRESH_DATA_FINISH = 11;//表示刷新数据
    private CustomListView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//这个是取消title
        setContentView(R.layout.serch);
        init();


        currentLocation = getIntent().getParcelableExtra("currentLocation");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        goserch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                load_Index = 0;
                //进行判断，是否定位成功
                if(currentLocation==null){
                    Toast.makeText(SerchActivity.this,"无法确定您的位置信息，请重试",Toast.LENGTH_SHORT).show();
                    return;
                }
                //对二次查询进行判断
                if(datas.size()!=0){
                    datas.clear();
                }
                search(LOAD_DATA_FINISH);
            }
        });
        initView();
    }

    public void init(){
        back = (ImageButton) findViewById(R.id.back);
        goserch = (ImageButton) findViewById(R.id.goseach);
        condition = (EditText) findViewById(R.id.condition);
        result = (CustomListView) findViewById(R.id.result);

    }

    private void initView(){
        simpleAdapter = new SimpleAdapter(this,datas,R.layout.serch_list,
                new String[]{"name","address","distance"},
                new int[]{R.id.result_message,R.id.result_address,R.id.result_distance});
        result.setAdapter(simpleAdapter);

        result.setOnRefreshListener(new CustomListView.OnRefreshListener() {

            @Override
            public void onRefresh() {
                // TODO 下拉刷新
                // Log.e(TAG, "onRefresh");
                loadData(0);
            }
        });

        result.setOnLoadListener(new CustomListView.OnLoadMoreListener() {

            @Override
            public void onLoadMore() {
                // TODO 加载更多
                loadData(1);
            }
        });

        result.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // 此处传回来的position和mAdapter.getItemId()获取的一致;
                Log.e(TAG, "click position:" + position);
                Log.e(TAG,view+"");

                Double x=locatondatas.get(position).get("longitude");
                Double y=locatondatas.get(position).get("latitude");
                Intent intent = new Intent(SerchActivity.this, MapActivity.class);
                intent.putExtra("currentLocation",currentLocation);
                intent.putExtra("longitude",x);
                intent.putExtra("latitude",y);
                intent.putExtra("addressname",datas.get(position).get("name"));
                intent.putExtra("address",datas.get(position).get("address"));
                intent.putExtra("distance",datas.get(position).get("distance"));
                startActivity(intent);

            }
        });
    }

    public void loadData(final int type){

        switch (type) {
            case 0:

                search(REFRESH_DATA_FINISH);
                result.onRefreshComplete();	//下拉刷新完成
                break;

            case 1:
                search(LOAD_DATA_FINISH);
                result.onLoadMoreComplete();	//加载更多完成
                break;
        }
    }



    private void search(final int style) {
        if(style==REFRESH_DATA_FINISH){
            load_Index=1;
        }else if(style==LOAD_DATA_FINISH){
            load_Index++;
        }
        String url = "https://api.weibo.com/2/location/pois/search/by_geo.json";
        ArrayList<NameValuePair> getParams = new ArrayList<NameValuePair>();
        getParams.add(new BasicNameValuePair("access_token", "2.00O2b7ECTKvEwD8507bbee2fHNrTqD"));
        getParams.add(new BasicNameValuePair("coordinate", currentLocation.getLongitude() + "," + currentLocation.getLatitude()));
        getParams.add(new BasicNameValuePair("range", "3000"));
        getParams.add(new BasicNameValuePair("count", "10"));
        getParams.add(new BasicNameValuePair("page", load_Index + ""));
        getParams.add(new BasicNameValuePair("q", condition.getText().toString()));


        String getParamsStr = URLEncodedUtils.format(getParams, "UTF-8");

        Log.d(getClass().getName(), "getParamsStr " + getParamsStr);

        url += "?" + getParamsStr;

        final HttpGet request = new HttpGet(url);

        final DefaultHttpClient client = new DefaultHttpClient();

        AsyncTask<Integer, Integer, Integer> task = new AsyncTask<Integer, Integer, Integer>() {
            private static final int ERROR_IOEXCEPTION = 1;
            private static final int ERROR_JSONException = 2;
            private static final int ERROR_NoMoreData = 3;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Integer doInBackground(Integer... params) {
                try {
                    HttpResponse response = client.execute(request);


                    String resultJsonStr = EntityUtils.toString(response.getEntity());

                    Log.d(getClass().getName(), "Response json " + resultJsonStr);

                    rootJsonObject = new JSONObject(resultJsonStr);
                    if (rootJsonObject.optJSONArray("poilist") == null) {
                        throw new  NullPointerException("没有数据了");

                    }

                } catch (IOException e) {
                    Log.e(getClass().getName(), e.getMessage(), e);
                    return ERROR_IOEXCEPTION;
                } catch (JSONException e) {
                    Log.e(getClass().getName(), e.getMessage(), e);
                    return ERROR_JSONException;
                } catch (NullPointerException e) {
                    Log.e(getClass().getName(), e.getMessage(), e);
                    return ERROR_NoMoreData;
                }

                return 0;
            }

            @Override
            protected void onPostExecute(Integer integer) {
                if (integer == ERROR_IOEXCEPTION) {
                    Toast.makeText(SerchActivity.this, "网络错误，请稍后重试", Toast.LENGTH_SHORT).show();
                    return;
                } else if (integer == ERROR_JSONException || integer == ERROR_NoMoreData) {
                    Toast.makeText(SerchActivity.this, "没有数据了", Toast.LENGTH_SHORT).show();
                    return;
                }

                displayAddress(style);

            }
        };

        task.execute(0);
    }

    private void displayAddress(int style) {
        HashMap<String,String> item =null;
        HashMap<String,Double> locaton=null;
        JSONArray poilistJsonArray = rootJsonObject.optJSONArray("poilist");
        if(style==REFRESH_DATA_FINISH){//刷新时先清空原来的数据，从新请求加载
            datas.clear();
        }else if(style==LOAD_DATA_FINISH){
            for (int i = 0; i < poilistJsonArray.length(); i++) {
                JSONObject poiJsonObject = poilistJsonArray.optJSONObject(i);

                double longitude = poiJsonObject.optDouble("x");
                double latitude = poiJsonObject.optDouble("y");
                String name = poiJsonObject.optString("name");
                String address = poiJsonObject.optString("address");
                //封装每一个item的经纬度
                locaton = new HashMap<String, Double>();
                locaton.put("longitude",longitude);
                locaton.put("latitude",latitude);
                locatondatas.add(locaton);
                // 距离单位M（米）
                double distance = getDistance(latitude, longitude, currentLocation.getLatitude(), currentLocation.getLongitude());
                String distanceStr = "";
                if (distance > 1000) {
                    distanceStr = distance / 1000 + "km";
                } else {
                    distanceStr = distance + "m";
                }
                item = new HashMap<String, String>();
                item.put("name", name);
                item.put("address", address);
                item.put("distance", distanceStr);

                datas.add(item);
            }
        }
        simpleAdapter.notifyDataSetChanged();
    }


    /**
     * google maps的脚本里代码
     */
    private static double EARTH_RADIUS = 6378.137;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 根据两点间经纬度坐标（double值），计算两点间距离，单位为米
     */
    public static double getDistance(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 1000);
        return s;
    }
}