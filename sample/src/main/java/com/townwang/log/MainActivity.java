package com.townwang.log;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.townwang.logutils.Log;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
private String jsonstr = "{\"status\":true,\"version\":1,\"downloadUrl\":\"https:www.townwang.com\",\"mandatory\":false}";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化设置过滤关键词以及是否打印
        Log.setConfig("Demo",true);
    }

    public void onClick(View view) {
        Log.d("别的就不多打印了 ，不同的只有个Json");

        Log.json("JSON打印","地址",jsonstr);
    }

}
