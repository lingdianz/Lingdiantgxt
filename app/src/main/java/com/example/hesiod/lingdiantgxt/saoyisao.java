package com.example.hesiod.lingdiantgxt;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hesiod.lingdiantgxt.activity.BaseAcvtivity;
import com.example.hesiod.lingdiantgxt.activity.messagerom;
import com.example.hesiod.lingdiantgxt.activity.mysocket;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;

import java.util.ArrayList;
import java.util.List;

public class saoyisao extends BaseAcvtivity {

    private String dowhat="",nextdo = "saoyisao",what = "";
    private Boolean busy=false;

    private Button btnsys,btnback,btntijiao;
    private EditText tvresult;
    private LinearLayout layoutsys;
    private ProgressBar ring;

    private link app;
    private messagerom messagerom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saoyisao);
        app=(link)getApplication();
        messagerom=app.getmessagerom();

        initview();
        Intent intent=getIntent();
        dowhat = intent.getStringExtra("dowhat");
        what = intent.getStringExtra("what");
        opencamera();   //打开相机
    }

    private void opencamera(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //申请相机权限
            if (ContextCompat.checkSelfPermission(saoyisao.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(saoyisao.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                Intent intent1 = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
                intent1.setData(uri);
                startActivity(intent1);
                Toast.makeText(saoyisao.this, "打开相机需要权限，请设置权限！", Toast.LENGTH_LONG).show();
            } else {
                try {
                    Intent intent = new Intent(saoyisao.this, CaptureActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_SCAN);
                } catch (Exception e) {
                    Toast.makeText(saoyisao.this, "打开相机异常", Toast.LENGTH_SHORT).show();
                }
            }
        }else{
            try {
                Intent intent = new Intent(saoyisao.this, CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SCAN);
            } catch (Exception e) {
                Toast.makeText(saoyisao.this, "打开相机异常", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //界面控件初始化
    private void initview(){
        ring = (ProgressBar)findViewById(R.id.ring);
        layoutsys=(LinearLayout)findViewById(R.id.layoutsys);
        //layoutsys.setVisibility(View.VISIBLE);
        tvresult=(EditText)findViewById(R.id.tvresult);
        btnback=(Button)findViewById(R.id.btnback);
        btnback.setOnClickListener(new mClick());
        btnsys=(Button)findViewById(R.id.btnsys);
        btnsys.setOnClickListener(new mClick());
        btntijiao=(Button)findViewById(R.id.btntijiao);
        btntijiao.setOnClickListener(new mClick());
    }

    private class mClick implements View.OnClickListener{
        @Override
        public void onClick(View v){
            switch (v.getId()){
                case R.id.btnback:
                    finish();
                    break;
                case R.id.btnsys:
                    nextdo = "saoyisao";
                    opencamera();   //打开相机
                    break;
                case R.id.btntijiao:
                    nextdo = "input";
                    String result=tvresult.getText().toString().trim();
                    toaddshebei(result);
                    break;
                default:
                    break;
            }
        }
    }

    private void toaddshebei(String result){
        if(result.length()>=18) {               //添加指令大于等于18，比如  client:c10010001:1
            char[] datachar = result.toCharArray();
            char[] ydata = new char[result.length()];
            for (int i = 0; i < result.length(); i++) {
                ydata[i] = (char) (datachar[i] ^ 8);    //每一位都异或8
            }
            String jmresult = new String(ydata);      //实例出一个新的字符串

            String[] cutrs = jmresult.split(":");
            if (cutrs.length == 3) {
                if (cutrs[0].equals("client") && cutrs[1].length() == 9) {
                    String shebei1 = cutrs[1], id1 = cutrs[2];
                    chooseproject(shebei1, id1);
                } else if (cutrs[0].equals("driver") && cutrs[1].length() == 9) {
                    String shebei1 = cutrs[1], id1 = cutrs[2];
                    chooseclient(shebei1,id1);
                } else {
                    Toast.makeText(saoyisao.this, "未识别", Toast.LENGTH_LONG).show();
                    initview();
                    return;
                }
            } else {
                Toast.makeText(saoyisao.this, "未识别", Toast.LENGTH_LONG).show();
                initview();
                return;
            }
        }else{
            Toast.makeText(saoyisao.this, "未识别", Toast.LENGTH_LONG).show();
        }
    }

    //添加控制器，选择工程
    private void chooseproject(final String client1, final String id1){
        final List<String> groupstring=messagerom.getProjectlist();//1取值解析顺序不能变，已在加载中解析
        final Spinner listView=new Spinner(saoyisao.this);
        listView.setPadding(20,0,20,0);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(saoyisao.this,android.R.layout.simple_list_item_1,groupstring);
        listView.setAdapter(adapter);
        AlertDialog.Builder builder= new AlertDialog.Builder(saoyisao.this);
        builder.setTitle("请选择归属工程");
        builder.setMessage("添加控制器"+client1+"到。。。");
        builder.setView(listView);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String project1=(String)listView.getSelectedItem();
                addclient(project1,client1,id1);
            }});
        builder.setNeutralButton("新建工程",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newproject(client1,id1);
            }});
        builder.setCancelable(false);
        builder.setNegativeButton("取消",null);
        builder.show();
    }
    //新建工程
    private void newproject(final String client1, final String id1){
        final EditText input = new EditText(saoyisao.this);
        AlertDialog.Builder builder= new AlertDialog.Builder(saoyisao.this);
        input.setHint("新工程");
        input.setGravity(1);
        input.setBackgroundResource(R.drawable.yuajiaotxqian);
        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        builder.setTitle("请输入1-10位的新工程名");
        builder.setMessage("添加"+client1+"到新的工程。。。");
        builder.setView(input);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newproject=input.getText().toString().trim();
                if(newproject.length()==0){
                    Toast.makeText(saoyisao.this,"添加失败，输入工程名不能为空",Toast.LENGTH_LONG).show();
                    return;
                }
                if(newproject.equals("0")){
                    Toast.makeText(saoyisao.this,"添加失败，输入工程名不能为0",Toast.LENGTH_LONG).show();
                    return;
                }
                if(newproject.length()>10){
                    Toast.makeText(saoyisao.this,"添加失败，输入工程名过长",Toast.LENGTH_LONG).show();
                    return;
                }
                List<String> groupstring=messagerom.getProjectlist();//1取值解析顺序不能变，已在加载中解析
                for(int ct=0;ct<groupstring.size();ct++){
                    if(newproject.equals(groupstring.get(ct).toString())){
                        Toast.makeText(saoyisao.this,"添加失败，存在同名工程",Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                addclient(newproject,client1,id1);
            }});
        builder.setCancelable(false);
        builder.setNegativeButton("取消",null);
        builder.show();
    }

    //移动控制器
    private void addclient(final String project1,final String client1,final String id1){
        if(busy){
            Toast.makeText(saoyisao.this,"网络忙碌，请稍后再试！",Toast.LENGTH_SHORT).show();
            return;
        }else {busy=true; }
        ring.setVisibility(View.VISIBLE);
        final mysocket socket = new mysocket();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    for(int ct=0;ct<2;ct++){
                        socket.addClient(messagerom.username,project1,client1,id1,messagerom.userlp);//传递带密码的删除主机
                        do{Thread.sleep(100);}while (socket.getbusy());
                        if(socket.getpsw().equals("true")){
                            if(socket.getresmsg().equals("true")) {
                                break;
                            }else{
                                socket.setpsw("数据错误");
                            }
                        }
                    }
                }catch (Exception e){
                    socket.setpsw("网络异常");
                }
                saoyisao.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ring.setVisibility(View.GONE);
                        String psw = socket.getpsw();
                        if(psw.equals("true")){
                            //reloadall();//后台刷新控制器列表
                            tvresult.setText("");
                            backorgoon("控制器");
                        }else {
                            Toast.makeText(saoyisao.this,psw,Toast.LENGTH_SHORT).show();
                            if(psw.equals("未登录")){
                                app.toload(saoyisao.this);
                                busy = false;
                                return;
                            }
                        }
                        app.shownet(tvresult);
                        busy=false;
                    }
                });
            }}).start();
    }

    private void backorgoon(String shebei1){
        AlertDialog.Builder builder= new AlertDialog.Builder(saoyisao.this);
        builder.setTitle("添加"+shebei1+"成功");
        builder.setPositiveButton("继续添加", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(nextdo.equals("saoyisao")){
                    Intent intent = new Intent(saoyisao.this, CaptureActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_SCAN);
                }else{
                    return;
                }
            }});
        builder.setNegativeButton("返回",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }});
        builder.setCancelable(false);
        builder.show();
    }
    //添加设备，选择控制器
    private void chooseclient(final String driver1,  final String id1)
    {
        if(messagerom.getClientlist().size()>0){
            if(messagerom.getClientlist().get(0).size()<1){
            Toast.makeText(saoyisao.this,"还没有控制器？请先添加控制器！",Toast.LENGTH_SHORT).show();
            return;
            }
        }
        List<String> child = new ArrayList<>();
        for(int ct=0;ct<messagerom.getClientlist().get(0).size();ct++){
            child.add(messagerom.getClientlist().get(0).get(ct).getClientname());
        }
        final List<String> childstring=child;//1取值解析顺序不能变，已在加载中解析
        String client1 = "0",group1 = "0";
        final Spinner listView=new Spinner(saoyisao.this);
        listView.setPadding(20,0,20,0);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(saoyisao.this,android.R.layout.simple_list_item_1,childstring);
        listView.setAdapter(adapter);
        AlertDialog.Builder builder= new AlertDialog.Builder(saoyisao.this);
        builder.setTitle("添加驱动："+driver1);
        if(dowhat.equals("adddriver")&& !what.equals("choose")){
            String[] cutwhat = what.split(":");
            client1 = cutwhat[0];
            group1 = cutwhat[1];
            builder.setMessage("添加到：控制器（"+ client1+"),分组（" +group1+"）下");
        }else {
            builder.setMessage("请选择归属控制器（未分组）");
            builder.setView(listView);
        }
        final String client = client1,group = group1;
        builder.setPositiveButton("确定添加", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String client1="",group1 = "";
                if(client.equals("0")||group.equals("0")) {
                    client1 = (String) listView.getSelectedItem();
                    group1 = "0";
                }else{
                    client1 = client;
                    group1 = group;
                }
                adddriver(client1, driver1, group1, id1);
            }});
        builder.setCancelable(false);
        builder.setNegativeButton("取消",null);
        builder.show();
    }

    //添加驱动

    //移动控制器
    private void adddriver(final String client1,final String driver1,final String group1,final String id1){
        if(busy){
            Toast.makeText(saoyisao.this,"网络忙碌，请稍后再试！",Toast.LENGTH_SHORT).show();
            return;
        }else {busy=true;   }
        ring.setVisibility(View.VISIBLE);
        final mysocket socket =new mysocket();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                for(int ct=0;ct<1;ct++){
                    socket.addDriver(messagerom.username,client1,group1,driver1,id1,messagerom.userlp);
                    do{Thread.sleep(100);}while (socket.getbusy());
                    if(socket.getpsw().equals("true")){
                        //messagerom.seveaddDriver(client1,group1,driver1,id1);
                        break;
                    }
                }}catch (Exception e){
                    socket.setpsw("网络异常");
                }
                saoyisao.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ring.setVisibility(View.GONE);
                        String psw = socket.getpsw();
                        if (psw.equals("true")) {
                            tvresult.setText("");
                            backorgoon("驱动");
                        }else {
                            Toast.makeText(saoyisao.this,psw,Toast.LENGTH_SHORT).show();
                            if(psw.equals("未登录")){
                                app.toload(saoyisao.this);
                                busy = false;
                                return;
                            }
                        }
                        app.shownet(tvresult);
                        busy=false;
                    }
                });
            }}).start();
    }


    private static int REQUEST_CODE_SCAN =2;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                String result = data.getStringExtra(Constant.CODED_CONTENT);
                tvresult.setText(result);
                toaddshebei(result);
            }else{
                if(dowhat.equals("saoyisao")){
                    finish();
                }
            }
        }else{
            if(dowhat.equals("saoyisao")){
                finish();
            }
        }
    }
}