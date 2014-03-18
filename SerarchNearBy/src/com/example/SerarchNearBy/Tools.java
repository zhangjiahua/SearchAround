package com.example.SerarchNearBy;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tools {
    public  static List<Map<String,Object>> initSecondData(String[] strings,String Key,String isSubordinate,boolean[] flag,List<Map<String,Object>> list) {
        for (int i = 0;i<strings.length;i++){
            Map<String,Object> map = new HashMap<String, Object>();
            map.put(Key,strings[i]);
            map.put(isSubordinate,flag[i]);
            list.add(map);
        }
        return  list;
    }
    public  static List<Map<String,Object>> initThridData(String[] strings,String Key,String code, Object[] codes,List<Map<String,Object>> list) {
        for (int i = 0;i<strings.length;i++){
            Map<String,Object> map = new HashMap<String, Object>();
            map.put(Key,strings[i]);
            map.put(code,codes[i]);
            list.add(map);
        }
        return  list;
    }
    public static String requestServerDate(String url) throws IOException {

        HttpGet httpGet = new HttpGet(url);
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
        HttpResponse response = defaultHttpClient.execute(httpGet);
        String requestStr = EntityUtils.toString(response.getEntity(), "UTF-8");
        return requestStr;
    }


}
