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

public class StartActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //设置头部隐藏
            //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                //getWindow().setFlags(Color.TRANSPARENT,Color.TRANSPARENT);
                //设置底部虚拟菜单背景色Color.parseColor("#e0e0ff"
                //getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}

