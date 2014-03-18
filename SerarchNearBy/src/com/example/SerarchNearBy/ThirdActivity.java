package com.example.SerarchNearBy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ThirdActivity extends Activity {
    private List<Map<String,Object>> dataList =new ArrayList<Map<String, Object>>() ;
    private SimpleAdapter listViewAdapter;
    private ListView  thirdListView;
    private TextView titleTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.third);

        titleTextView = (TextView) findViewById(R.id.thirdly_title_title_TextView);
        thirdListView = (ListView) findViewById(R.id.thirdly_listView);
        listViewAdapter  = new SimpleAdapter(this,dataList,R.layout.third_content_item,new String[]{"text"},new int[]{R.id.third_content_list_textView})
        {
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView==null)
                {
                    LayoutInflater layoutInflater = getLayoutInflater() ;
                    convertView = layoutInflater.inflate(R.layout.third_content_item,parent,false);
                }
                Map<String,Object> map = (Map<String, Object>) getItem(position);
                TextView textView= (TextView) convertView.findViewById(R.id.third_content_list_textView);
                final  int index = position;
                final  String  title = map.get("text").toString();
                textView.setText(title);

                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // itemClick(index,title);
                    }
                });
                return  convertView;
            }
        };
        thirdListView.setAdapter(listViewAdapter);

    }

//    private void itemClick(int position,String title) {
//        Intent intent = new Intent(this,ListActivity.class);
//        intent.putExtra(SecondActivity.dataIndex, position);
//        intent.putExtra(SecondActivity.dataText, title);
//        startActivity(intent);
//    }

    protected void onResume() {

        super.onResume();
        initData();
    }

    private void initData() {
        dataList.clear();
        Intent intent = getIntent();
        int index = intent.getIntExtra(SecondActivity.dataIndex, 1);
        Log.d("third","index:"+index);
        String title = intent.getStringExtra(SecondActivity.dataText);
        Log.d("third","title:"+title);
        titleTextView.setText(title);
        String[] twoStrings;
        List<Long> list = new ArrayList<Long>();;
        if(title.equals("中餐厅")){
                twoStrings   = new String[]{"中餐厅","综合酒楼","四川菜（川菜）","广东菜（粤菜）","山东菜（鲁菜)","江苏菜","浙江菜","上海菜","湖南菜（湘菜）"};
                for (long i = 050100;i<050123;i++)
                {
                    list.add(i);
                }
                dataList = Tools.initThridData(twoStrings, "text", "code", list.toArray(), dataList);
        }else if(title.equals("外国餐厅")){
                twoStrings   = new String[]{"西餐厅（综合风味）","日本料理","韩国料理","法式菜品餐厅","意式菜品餐厅","泰国／越南菜品餐厅","地中海风格菜品","美式风味","印度风味"};
            for (long i = 050100;i<050123;i++)
            {
                list.add(i);
            }
                dataList = Tools.initThridData(twoStrings, "text", "code", list.toArray(), dataList);
         }else if(title.equals("快餐厅")){
                twoStrings   = new String[]{"肯德基","麦当劳","必胜客","永和豆浆","茶餐厅","大家乐","大快活","美心","吉野家"};

                for (long i = 050300;i<=050310;i++)
                {
                    list.add(i);
                }
                dataList = Tools.initThridData(twoStrings, "text", "code", list.toArray(), dataList);
         }else if(title.equals("咖啡厅")){
                twoStrings   = new String[]{"星巴克咖啡","上岛咖啡","Pacific Coffee Company","巴黎咖啡店"};
                for (long i = 050500;i<050504;i++)
                {
                    list.add(i);
                }
                dataList = Tools.initThridData(twoStrings, "text", "code", list.toArray(), dataList);
         }else if(title.equals("商场")){
                twoStrings   = new String[]{"商场","购物中心","普通商店","免税商品店"};
                for (long i = 060100;i<060104;i++)
                {
                    list.add(i);
                }
                dataList = Tools.initThridData(twoStrings, "text", "code", list.toArray(), dataList);
         }else if(title.equals("便利店")){
                twoStrings   = new String[]{"便民商店／便利店","7-ELEVEN便利店","OK便利店"};
                for (long i = 060200;i<=060202;i++)
                {
                    list.add(i);
                }
                dataList = Tools.initThridData(twoStrings, "text", "code", list.toArray(), dataList);
         }else if(title.equals("家电电子卖场")){
                twoStrings   = new String[]{"家电电子卖场","综合家电商场","国美","大中","苏宁","手机销售","数码电子","丰泽"};
                for (long i = 060300;i<=060307;i++)
                {
                    list.add(i);
                }
                dataList = Tools.initThridData(twoStrings, "text", "code", list.toArray(), dataList);
          }else if(title.equals("超级市场")){
            twoStrings   = new String[]{"超市","家乐福","沃尔玛","北京华联","上海华联","麦德龙","万客隆","华堂","易初莲花","好又多","屈臣氏","乐购","惠康超市","百佳超市","万宁超市"};
            for (long i = 060400;i<=060416;i++)
            {
                list.add(i);
            }
            dataList = Tools.initThridData(twoStrings, "text", "code", list.toArray(), dataList);
        }else if(title.equals("花鸟鱼虫市场")){
            twoStrings   = new String[]{"花鸟鱼虫市场","花卉市场","宠物市场"};
            for (long i = 060500;i<=060502;i++)
            {
                list.add(i);
            }
            dataList = Tools.initThridData(twoStrings, "text", "code", list.toArray(), dataList);
        }else if(title.equals("家居建材市场")){
            twoStrings   = new String[]{"家居建材市场","家具建材综合市场","家具城", "建材五金市场", "厨卫市场","布艺市场", "灯具瓷器市场"};
            for (long i = 060600;i<=060606;i++)
            {
                list.add(i);
            }
            dataList = Tools.initThridData(twoStrings, "text", "code", list.toArray(), dataList);
        } else if(title.equals("综合市场")){
            twoStrings   = new String[]{"综合市场","小商品市场",
                    "旧货市场",
                    "农副产品市场",
                    "果品市场",
                    "蔬菜市场",
                    "水产海鲜市场"};
            for (long i = 060700;i<=060706;i++)
            {
                list.add(i);
            }
            dataList = Tools.initThridData(twoStrings, "text", "code", list.toArray(), dataList);
        }  else if(title.equals("售票处")){
            twoStrings   = new String[]{"售票处",
                    "飞机票代售点",
                    "火车票代售点",
                    "长途汽车票代售点",
                    "船票代售点",
                    "公交卡／月票代售点",
                    "公园景点售票处"};
            for (long i = 070300;i<=070306;i++)
            {
                list.add(i);
            }
            dataList = Tools.initThridData(twoStrings, "text", "code", list.toArray(), dataList);
        }else if(title.equals("邮局")){
            twoStrings   = new String[]{"邮局","邮政速递"};
            for (long i = 070300;i<=070306;i++)
            {
                list.add(i);
            }
            dataList = Tools.initThridData(twoStrings, "text", "code", list.toArray(), dataList);
        } else if(title.equals("电讯营业厅")){
            twoStrings   = new String[]{"电讯营业厅",
                    "中国电信营业厅",
                    "中国网通营业厅",
                    "中国移动营业厅",
                    "中国联通营业厅",
                    "中国铁通营业厅",
                    "中国卫通营业厅",
                    "和记电讯",
                    "数码通电讯" ,
                    "电讯盈科" ,
                    "中国移动万众/Peoples"};
            for (long i = 70600;i<=70610;i++)
            {
                list.add(i);
            }
            dataList = Tools.initThridData(twoStrings, "text", "code", list.toArray(), dataList);
        }
        else if(title.equals("运动场馆")){
            twoStrings   = new String[]{"运动场所", "综合体育馆","保龄球馆",  "网球场", "篮球场馆", "足球场","滑雪场", "溜冰场","户外健身场所", "海滨浴场","游泳馆","健身中心","乒乓球馆","台球厅","壁球场", "马术俱乐部","赛马场", "橄榄球场", "羽毛球场","跆拳道场馆"};
            for (long i = 80100;i<=80119;i++)
            {
                list.add(i);
            }
            dataList = Tools.initThridData(twoStrings, "text", "code", list.toArray(), dataList);
        }else if(title.equals("高尔夫相关")){
            twoStrings   = new String[]{"高尔夫相关", "高尔夫球场","高尔夫练习场"};
            for (long i = 80200;i<=80202;i++)
            {
                list.add(i);
            }
            dataList = Tools.initThridData(twoStrings, "text", "code", list.toArray(), dataList);
        } else if(title.equals("娱乐场所")){
            twoStrings   = new String[]{"娱乐场所",
                    "夜总会",
                    "ＫＴＶ",
                    "迪厅",
                    "酒吧",
                    "游戏厅",
                    "棋牌室" ,
                    "博采中心",
                    "网吧"};
            for (long i = 80300;i<=80308;i++)
            {
                list.add(i);
            }
            dataList = Tools.initThridData(twoStrings, "text", "code", list.toArray(), dataList);
        }else if(title.equals("度假疗养场所")){
            twoStrings   = new String[]{"度假疗养场所",
                    "度假村",
                    "疗养院",};
            for (long i = 80400;i<=80402;i++)
            {
                list.add(i);
            }
            dataList = Tools.initThridData(twoStrings, "text", "code", list.toArray(), dataList);
        }else if(title.equals("休闲场所")){
            twoStrings   = new String[]{"休闲场所",
                    "游乐场",
                    "垂钓园",
                    "采摘园",
                    "露营地",
                    "水上活动中心"};
            for (long i = 80500;i<=80505;i++)
            {
                list.add(i);
            }
            dataList = Tools.initThridData(twoStrings, "text", "code", list.toArray(), dataList);
        }  else if(title.equals("影剧院")){
            twoStrings   = new String[]{ "影剧院相关",
                    "电影院",
                    "音乐厅",
                    "剧场"};
            for (long i = 80600;i<=80603;i++)
            {
                list.add(i);
            }
            dataList = Tools.initThridData(twoStrings, "text", "code", list.toArray(), dataList);
        }else if(title.equals("综合医院")){
            twoStrings   = new String[]{ "综合医院",
                    "三级甲等医院",
                    "卫生院"};
            for (long i = 80600;i<=80603;i++)
            {
                list.add(i);
            }
            dataList = Tools.initThridData(twoStrings, "text", "code", list.toArray(), dataList);
        } else if(title.equals("专科医院")){
            twoStrings   = new String[]{ "专科医院",
                            "整形美容",
                            "口腔医院",
                            "眼科医院",
                            "耳鼻喉医院",
                            "胸科医院",
                            "骨科医院",
                            "肿瘤医院",
                            "脑科医院",
                            "妇科医院",
                            "精神病医院",
                            "传染病医院",};
            for (long i = 90200;i<=90211;i++)
            {
                list.add(i);
            }
            dataList = Tools.initThridData(twoStrings, "text", "code", list.toArray(), dataList);
        }else if(title.equals("医药保健相关")){
            twoStrings   = new String[]{
                    "医药保健相关",
                    "药房",
                    "医疗保健用品"};
            for (long i = 90200;i<=90211;i++)
            {
                list.add(i);
            }
            dataList = Tools.initThridData(twoStrings, "text", "code", list.toArray(), dataList);
        } else if(title.equals("动物医疗场所")){
            twoStrings   = new String[]{
                    "动物医疗场所",
                    "宠物诊所",
                    "兽医站"};
            for (long i = 90200;i<=90211;i++)
            {
                list.add(i);
            }
            dataList = Tools.initThridData(twoStrings, "text", "code", list.toArray(), dataList);
        }  else if(title.equals("宾馆酒店")){
            twoStrings   = new String[]{
                    "宾馆酒店",
                            "六星级宾馆",
                            "五星级宾馆",
                            "四星级宾馆",
                            "三星级宾馆",
                            "经济型连锁酒店"};
            for (long i = 100100;i<=100105;i++)
            {
                list.add(i);
            }
            dataList = Tools.initThridData(twoStrings, "text", "code", list.toArray(), dataList);
        }    else if(title.equals("旅馆招待所")){
            twoStrings   = new String[]{
                    "旅馆招待所",
                    "青年旅社",};
            for (long i = 100200;i<=100201;i++)
            {
                list.add(i);
            }
            dataList = Tools.initThridData(twoStrings, "text", "code", list.toArray(), dataList);
        }else if(title.equals("博物馆")){
            twoStrings   = new String[]{
                    "博物馆",
                    "奥迪博物馆",
                    "奔驰博物馆"};
            for (long i = 140100;i<=140102;i++)
            {
                list.add(i);
            }
            dataList = Tools.initThridData(twoStrings, "text", "code", list.toArray(), dataList);
        } else if(title.equals("港口码头")){
            twoStrings   = new String[]{
                    "港口码头",
                    "客运港",
                    "车渡口",
                     "人渡口"};
            for (long i = 150300;i<=150303;i++)
            {
                list.add(i);
            }
            dataList = Tools.initThridData(twoStrings, "text", "code", list.toArray(), dataList);
        }   else if(title.equals("公交车站")){
            twoStrings   = new String[]{
                    "公交车站相关",
                    "旅游专线相关",
                    "普通公交站"};
            for (long i = 150700;i<=150702;i++)
            {
                list.add(i);
            }
            dataList = Tools.initThridData(twoStrings, "text", "code", list.toArray(), dataList);
        }     else if(title.equals("停车场")){
            twoStrings   = new String[]{
                    "停车场相关",
                    "室内停车场",
                    "室外停车场","停车换乘点"};
            for (long i = 150900;i<=150903;i++)
            {
                list.add(i);
            }
            dataList = Tools.initThridData(twoStrings, "text", "code", list.toArray(), dataList);
        }

        listViewAdapter.notifyDataSetChanged();
   }


    public void goBackClick(View view){
        this.finish();
    }
}
