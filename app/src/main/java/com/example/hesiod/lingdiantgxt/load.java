package com.example.hesiod.lingdiantgxt;
import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Bundle;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hesiod.lingdiantgxt.activity.BaseAcvtivity;
import com.example.hesiod.lingdiantgxt.activity.StartActivity;
import com.example.hesiod.lingdiantgxt.activity.messagerom;
import com.example.hesiod.lingdiantgxt.activity.mysocket;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class load extends BaseAcvtivity {
    private Button btnload;
    private EditText etusername;
    private EditText etpassword;
    private CheckBox cbwrite;
    private CheckBox cbauto;
    private ImageView imgeye;
    private TextView sideuser,fogotmima;
    private ProgressBar ring;
    private Button btnsysload;

    private link app;
    private messagerom messagerom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load);
        app=(link)getApplication();
        messagerom=app.getmessagerom();
        initview();
        GBcknet();//开启广播查网络连接
        readtxt("user1");//读账号文件
    }

    private void initview(){
        btnsysload = (Button)findViewById(R.id.btnsysload);
        ring = (ProgressBar)findViewById(R.id.ring);
        etusername=(EditText)findViewById(R.id.username);
        etpassword=(EditText)findViewById(R.id.password);
        imgeye=(ImageView)findViewById(R.id.imgeye);
        cbauto=(CheckBox)findViewById(R.id.cbauto);
        cbwrite=(CheckBox)findViewById(R.id.cbwrite);
        sideuser=(TextView)findViewById(R.id.sideuser);
        sideuser.setOnClickListener(new mClick());
        fogotmima=(TextView)findViewById(R.id.fogotmima);
        fogotmima.setOnClickListener(new mClick());
        btnsysload.setOnClickListener(new mClick());

        btnload=(Button)findViewById(R.id.btnload);
        btnload.setOnClickListener(new mClick());
        imgeye.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        etpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        break;
                    case MotionEvent.ACTION_MOVE:
                        etpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        break;
                    case MotionEvent.ACTION_UP:
                        etpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        break;
                }
                return true;
            }
        });
    }

    public class mClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnload:
                String username1=etusername.getText().toString();
                String password1=etpassword.getText().toString();
                if(TextUtils.isEmpty(username1) || TextUtils.isEmpty(password1)){
                    Toast.makeText(load.this,"账号密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    lpramdom=messagerom.getFlprandom();
                    if(lpramdom.equals("")){
                        try{
                            messagerom.sevelprandom(load.this);//保存令牌
                            lpramdom=messagerom.getFlprandom();
                        }catch (Exception e){}
                    }
                    String userlp1=username1+lpramdom;
                    login(username1,password1,userlp1);
                }
                    break;
                case R.id.fogotmima:
                    Intent intent1=new Intent(load.this,getmima.class);
                    startActivityForResult(intent1,2);
                    break;
                case R.id.sideuser:
                    Intent intent2=new Intent(load.this,side.class);
                    startActivityForResult(intent2,2);
                    break;
                case R.id.btnsysload:
                    opencamera();
                    break;
                default:
                    break;
            }}
        }

    private Boolean busy=false;
    //登录
    private void login(final String username1,final String password1,final  String userlp1) {
        if(busy){
            return;
        }else{busy=true;}
        ring.setVisibility(View.VISIBLE);
        final mysocket socket = new mysocket();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    for(int a=0;a<3;a++) {
                        app.delmessagerom();
                        app.setmessagerom();    //重新建立缓存文件
                        messagerom=app.getmessagerom();
                        socket.setbusy();
                        socket.Login(username1, password1, userlp1);
                        do{Thread.sleep(100);} while (socket.getbusy());
                        if(socket.getpsw().equals("true")){
                            if(socket.getresmsg().equals("true")){
                                messagerom.seveUsername(username1);
                                messagerom.sevePassword(password1);
                                messagerom.seveUserlp(userlp1);
                                break;
                            }else{
                                socket.setpsw("数据错误");
                            }
                        }
                    }
                }catch (Exception e){
                    socket.setpsw("网络异常");
                }
                load.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ring.setVisibility(View.GONE);
                        String psw=socket.getpsw();
                        //判断是否登录成功
                        if(psw.equals("true")) {
                            seveuser(username1,password1,cbwrite.isChecked(),cbauto.isChecked());//保存用户账号密码
                            Intent intent = new Intent(load.this, buttomgo.class);
                            startActivity(intent);
                            finish();
                            return;
                        }else if(psw.equals("操作失败")) {
                            Toast.makeText(load.this, "登录失败，请检查账号密码是否正确！", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(load.this, psw, Toast.LENGTH_SHORT).show();
                        }
                        app.shownet(etusername);
                        busy=false;
                    }
                });
                }
            }).start();
        }

    private String lpramdom="";
    //保存账号到文本文件
    private void seveuser(String username,String password,Boolean cbre,Boolean cbau) {
        try{
            messagerom.seveusermessage(load.this,username,password,cbre,cbau);
        }catch (Exception e){}
    }

    //读账号密码信息
    public void readtxt(String filename) {
        try {
           if(messagerom.readusermessage(load.this,filename)){
               etusername.setText(messagerom.getFusername());
               etpassword.setText(messagerom.getFpassword());
               cbwrite.setChecked(messagerom.getFisremenber());
               cbauto.setChecked(messagerom.getFisautoload());
               lpramdom=messagerom.getFlprandom();
               if(etpassword.getText().toString().trim().equals("")){
                   imgeye.setVisibility(View.VISIBLE);
               }
           }
        } catch (Exception e) {}
    }

    private static int REQUEST_CODE_SCAN =2;
    private void opencamera(){
        //申请相机权限
        if(ContextCompat.checkSelfPermission(load.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(load.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            Intent intent1 = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
            intent1.setData(uri);
            startActivity(intent1);
            Toast.makeText(load.this,"打开相机需要权限，请设置权限！",Toast.LENGTH_LONG).show();
        }else{
            try {
                Intent intent = new Intent(load.this, CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SCAN);
            }catch (Exception e){
                Toast.makeText(load.this,"打开相机异常",Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void toload(String result){
        if(result.length()>=22) {               //登录数据位数至少为22位，比如  login:500010001:123456
            char[] datachar = result.toCharArray();
            char[] ydata = new char[result.length()];
            for (int i = 0; i < result.length(); i++) {
                ydata[i] = (char) (datachar[i] ^ 8);    //每一位都异或8
            }
            String jmresult = new String(ydata);      //实例出一个新的字符串

            String[] cutrs = jmresult.split(":");
            if (cutrs.length == 3) {
                if (cutrs[0].equals("login") && cutrs[1].length() == 9) {
                    String username = cutrs[1], password = cutrs[2];
                    lpramdom=messagerom.getFlprandom();
                    if(lpramdom.equals("")){
                        try{
                            messagerom.sevelprandom(load.this);//保存令牌
                            lpramdom=messagerom.getFlprandom();
                        }catch (Exception e){}
                    }
                    String userlp1=username+lpramdom;
                    cbwrite.setChecked(true);
                    cbauto.setChecked(true);
                    seveuser(username,password,true,true);
                    login(username,password,userlp1);
                } else {
                    Toast.makeText(load.this, "未识别", Toast.LENGTH_LONG).show();
                    return;
                }
            } else {
                Toast.makeText(load.this, "未识别", Toast.LENGTH_LONG).show();
                return;
            }
        }else{
            Toast.makeText(load.this, "未识别", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                String result = data.getStringExtra(Constant.CODED_CONTENT);
                toload(result);
            }
        }else if(requestCode == 2){
            readtxt("user1");//读账号文件
        }
    }



    //      按返回键返回桌面，不退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();// 为Intent设置Action、Category属性
            intent.setAction(Intent.ACTION_MAIN);// "android.intent.action.MAIN"
            intent.addCategory(Intent.CATEGORY_HOME); //"android.intent.category.HOME"
            startActivity(intent);

            return true;
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private IntentFilter intentFilter;
    private NetworkChangeReceiver networkChangeReceiver;
    private void GBcknet(){
        intentFilter =new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver=new load.NetworkChangeReceiver();
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
            load.this.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
            if(networkInfo!=null&&networkInfo.isAvailable()){
                app.setcknet(true);   //连接正常
            }else {
                app.setcknet(false); //连接断开
            }
        }
    }

}
