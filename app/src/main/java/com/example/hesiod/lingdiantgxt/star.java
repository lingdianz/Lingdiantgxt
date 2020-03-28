package com.example.hesiod.lingdiantgxt;
import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.hesiod.lingdiantgxt.activity.BaseAcvtivity;
import com.example.hesiod.lingdiantgxt.activity.StartActivity;
import com.example.hesiod.lingdiantgxt.activity.messagerom;
import com.example.hesiod.lingdiantgxt.activity.mysocket;
import com.example.hesiod.lingdiantgxt.baseadapter.myset;
import com.example.hesiod.lingdiantgxt.fragment_java.group0;
import com.yzq.zxinglibrary.common.Constant;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static android.support.v4.content.PermissionChecker.PERMISSION_DENIED;

public class star extends BaseAcvtivity {

    private ProgressBar ring;

    private String username="",password="",userlp="";
    private Boolean autoload=false;

    private link app;
    private messagerom messagerom;
    private Button btnnext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.star);
        btnnext = (Button)findViewById(R.id.btnnext);
        ring = (ProgressBar)findViewById(R.id.ring);
        app=(link)getApplication();
        app.setmessagerom();
        messagerom=app.getmessagerom();
        app.getdensity();   //获取屏幕密度
        quanxian();//动态申请权限
        GBcknet();
    }

    private void getversion(){
        ring.setVisibility(View.VISIBLE);
        final mysocket socket = new mysocket();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    for(int ct=0;ct<2;ct++){
                        socket.setbusy();
                        socket.getvesions("vesion");
                        do{Thread.sleep(100);}while (socket.getbusy());
                        if(socket.getpsw().equals("true")){
                            if(socket.getresmsg().length()==5){ //版本信息固定长度为5
                                break;
                            }else{
                                socket.setpsw("数据错误");
                            }
                        }
                    }
                }catch (Exception e){
                    socket.setpsw("网络异常");
                }
                star.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ring.setVisibility(View.GONE);
                        String psw = socket.getpsw();
                        String newvesion = socket.getresmsg();
                        app.setNewvesion(newvesion);
                        if(psw.equals("true")){
                            String oldvision =app.getVersion();
                            if (!newvesion.equals(oldvision)) {
                                app.setRbnew3(1);   //首页消息红点
                                app.setNewapp(myset.TYPE_NEWS);  //设置页消息红点
                                AlertDialog.Builder builder = new AlertDialog.Builder(star.this);
                                builder.setTitle("提示更新");
                                builder.setMessage("有版本更新：V" + newvesion + "");
                                builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String addr = "https://49.235.7.35/lingdiantg.apk";  //网址
                                        Uri webdress = Uri.parse(addr);  //解析网址
                                        Intent intent = new Intent(Intent.ACTION_VIEW, webdress); //创建绑定
                                        startActivity(intent); //开始活动
                                        finish();
                                        return;
                                    }
                                });
                                builder.setNegativeButton("稍后更新", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        login();
                                    }
                                });
                                builder.setCancelable(false);
                                builder.show();
                            } else {
                                login();
                            }
                        }else{
                            app.shownet(ring);
                            Toast.makeText(star.this, psw, Toast.LENGTH_SHORT).show();
                            login();
                        }
                    }
                });
            }}).start();
    }
    private String psw="";
    //登录
    private void login() {
        readtxt("user1");
        if(!autoload) {      //判断是否自动登录
            Intent intent = new Intent(star.this, load.class);
            startActivity(intent);
            finish();
            return;
        }
        ring.setVisibility(View.VISIBLE);
        final mysocket socket = new mysocket();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    for(int a=0;a<2;a++) {
                        socket.Login(username,password,userlp);
                        do{Thread.sleep(100);}while (socket.getbusy());
                        if(socket.getpsw().equals("true")){
                            if(socket.getresmsg().equals("true")){
                                messagerom.seveUsername(username);
                                messagerom.sevePassword(password);
                                messagerom.seveUserlp(userlp);
                                break;
                            }else{
                                socket.setpsw("数据错误");
                            }
                        }
                    }
                }catch (Exception e){
                    socket.setpsw("网络异常");
                }
                star.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ring.setVisibility(View.GONE);
                        psw = socket.getpsw();
                        if(psw.equals("true")) {
                            Intent intent = new Intent(star.this, buttomgo.class);
                            startActivity(intent);
                            finish();
                        }else{
                            app.shownet(ring);
                            Toast.makeText(star.this, psw, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(star.this, load.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }}).start();
    }

    //第一次开启程序动态申请权限Manifest.permission.READ_EXTERNAL_STORAGE
    private void quanxian(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> plist = new ArrayList<>();
            plist = getpromissionlist();
            if (!plist.isEmpty()) {
                String[] ps = plist.toArray(new String[plist.size()]);
                ActivityCompat.requestPermissions(star.this, ps, 1);
                initclick();    //初始化按钮
            } else {
                getversion();//开始加载数据，首先加载软件版本号
            }
        }else{
            getversion();//开始加载数据，首先加载软件版本号
        }
    }


    private List<String> getpromissionlist(){
        List<String> plist = new ArrayList<>();
        if(ContextCompat.checkSelfPermission(star.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            plist.add(Manifest.permission.CAMERA);
        }
        if(ContextCompat.checkSelfPermission(star.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            plist.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (ActivityCompat.checkSelfPermission(star.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            plist.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        return plist;
    }

    private void initclick(){
        btnnext.setVisibility(View.VISIBLE);
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId()==R.id.btnnext){
                    List<String> plist = new ArrayList<>();
                    plist = getpromissionlist();
                    if(!plist.isEmpty()) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, 1);
                        Toast.makeText(star.this,"请设置开启权限！",Toast.LENGTH_LONG).show();
                    }else{
                        getversion();
                    }
                }
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            //申请相机权限
            if(ContextCompat.checkSelfPermission(star.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(star.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                quanxian();
            }else{
                getversion();
            }
        }
    }

/*
    public void megshow(){
        ObjectAnimator translationX = ObjectAnimator.ofFloat(img, "translationX", 0, 1f);
        ObjectAnimator ra = ObjectAnimator.ofFloat(img, "rotation", 0, 720.0F);
        AnimatorSet animatorSet = new AnimatorSet();  //组合动画
        animatorSet.playTogether(translationX,ra); //设置动画
        animatorSet.setDuration(2000);
        animatorSet.start();
    }
*/
                //读文本是否自动登录
    public void readtxt(String filename) {
        try {
            if(messagerom.readusermessage(star.this,filename))
            username=messagerom.getFusername();
            password=messagerom.getFpassword();
            String lprandom=messagerom.getFlprandom();
            if(lprandom.equals("")){
                messagerom.sevelprandom(star.this);
            }
            lprandom = messagerom.getFlprandom();
            userlp = username+lprandom;
            autoload = messagerom.getFisremenber() && messagerom.getFisautoload();
        } catch (Exception e) {
        }
    }

    private IntentFilter intentFilter;
    private NetworkChangeReceiver networkChangeReceiver;
    private void GBcknet(){
        intentFilter =new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver=new star.NetworkChangeReceiver();
        this.registerReceiver(networkChangeReceiver,intentFilter);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        this.unregisterReceiver(networkChangeReceiver);
    }

    //监听连接网络改变的广播
    class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context,Intent intent){
            ConnectivityManager connectivityManager=(ConnectivityManager)
            star.this.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
            if(networkInfo!=null&&networkInfo.isAvailable()){
                app.setcknet(true);   //连接正常
            }else {
                app.setcknet(false); //连接断开
            }
        }
    }
}
