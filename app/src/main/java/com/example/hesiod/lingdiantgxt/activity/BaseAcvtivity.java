package com.example.hesiod.lingdiantgxt.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.hesiod.lingdiantgxt.baseadapter.ActivityCollector;

/**
 * Created by Hesiod on 2019/9/17.
 */

public class BaseAcvtivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            //设置底部虚拟菜单背景色
            getWindow().setNavigationBarColor(Color.parseColor("#e0e0ff"));
        }
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
