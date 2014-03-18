package com.example.SerarchNearBy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-3-17
 * Time: 上午10:53
 * To change this template use File | Settings | File Templates.
 */
public class SettingActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.setting);
    }
    public  void  backOnClick(View view)
    {
        Intent intent = new Intent(SettingActivity.this,MyActivity.class);
        this.finish();
        startActivity(intent);
    }
}
