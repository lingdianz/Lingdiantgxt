package com.example.hesiod.lingdiantgxt;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.hesiod.lingdiantgxt.activity.BaseAcvtivity;
import com.example.hesiod.lingdiantgxt.activity.messagerom;
import com.example.hesiod.lingdiantgxt.activity.mysocket;
import com.example.hesiod.lingdiantgxt.baseadapter.ActivityCollector;
import com.example.hesiod.lingdiantgxt.baseadapter.mylist_adapter;
import com.example.hesiod.lingdiantgxt.baseadapter.myset;
import com.google.zxing.WriterException;
import com.yzq.zxinglibrary.encode.CodeCreator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class appset extends BaseAcvtivity {

    private Button btnexitapp,btnback;
    private link app;
    private messagerom messagerom;
    private ListView lvset;
    private ProgressBar ring;

    private List<myset> mysetlist =new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appset);
        app=(link)getApplication();
        messagerom = app.getmessagerom();
        initview();
    }
    private void initview(){
        ring =(ProgressBar)findViewById(R.id.ring);
        lvset = (ListView)findViewById(R.id.lvset);
        btnexitapp=(Button)findViewById(R.id.btnexitapp);
        btnexitapp.setOnClickListener(new mClick());
        btnback=(Button)findViewById(R.id.btnreturn);
        btnback.setOnClickListener(new mClick());
        initlist();
    }

    private void initlist() {
        if(!mysetlist.isEmpty()){
            mysetlist.clear();
        }
        Intent intent = new Intent();
        myset list0 = new myset(R.mipmap.setupset, "主题", "默认主题（天空蓝）", myset.TYPE_SHOW, intent);
        mysetlist.add(list0);
        myset list1 = new myset(R.mipmap.setupset, "系统配置", "Android 6.0", myset.TYPE_SHOW, intent);
        mysetlist.add(list1);
        String showversion = "当前版本：V" + app.getVersion();
        if (!app.getNewvesion().equals(app.getVersion())) {
            showversion = "有新本更新：V" + app.getNewvesion();
        }
        myset list2 = new myset(R.mipmap.setupset, "版本", showversion, app.getNewapp(), intent);
        mysetlist.add(list2);
        myset list3 = new myset(R.mipmap.setupset, "二维码分享App", "", myset.TYPE_MENU, intent);
        mysetlist.add(list3);
        myset list4 = new myset(R.mipmap.setupset, "二维码共享账号", messagerom.username, myset.TYPE_MENU, intent);
        mysetlist.add(list4);
        myset list5 = new myset(R.mipmap.setupset, "关于", "物联网App", myset.TYPE_SHOW, intent);
        mysetlist.add(list5);

        mylist_adapter adapter = new mylist_adapter(appset.this, R.layout.mysetmune, mysetlist);
        lvset.setAdapter(adapter);
        initlistclick();
    }

    private void initlistclick(){
        lvset.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String psw = mysetlist.get(position).getstrsetname();
                switch (psw){
                    case "二维码分享App":
                        String contentEtString = "https://49.235.7.35/lingdiantg.apk";
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.logo);
                        try {
                            /*contentEtString：字符串内容w：图片的宽h：图片的高logo：不需要logo的话直接传null*/
                            Bitmap logo = BitmapFactory.decodeResource(getResources(), R.mipmap.logo);
                            bitmap = CodeCreator.createQRCode(contentEtString, 400, 400, logo);
                        } catch (WriterException e) { break;}
                        final ImageView imag = new ImageView(appset.this);
                        imag.setImageBitmap(bitmap);
                        AlertDialog.Builder builder= new AlertDialog.Builder(appset.this);
                        builder.setTitle("分享App");
                        builder.setMessage("用浏览器扫描二维码下载App\r\n");
                        builder.setView(imag);
                        builder.show();
                        break;
                    case "版本":
                        if (!app.getNewvesion().equals(app.getVersion())) {
                            showgetnew();
                        }else{
                            cknewvesion();
                        }
                        break;
                    case "二维码共享账号":
                        final EditText input = new EditText(appset.this);
                        input.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        input.setHint("password");
                        input.setGravity(1);
                        input.setBackgroundResource(R.drawable.yuajiaotxqian);
                        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
                        AlertDialog.Builder builder2= new AlertDialog.Builder(appset.this);
                        builder2.setTitle("共享账号");
                        builder2.setMessage("需要权限，请输入验证密码");
                        builder2.setView(input);
                        builder2.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String inputstr=input.getText().toString();
                                String psw=messagerom.getPassword();
                                if(inputstr.isEmpty()){return;}
                                if(!inputstr.equals(psw)){
                                    Toast.makeText(appset.this,"验证密码错误",Toast.LENGTH_SHORT).show();
                                    return;
                                }else{
                                    //密码核对正确
                                    showshapuser(); //共享账号
                                }
                            }
                        });
                        builder2.setNegativeButton("取消",null);
                        builder2.show();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void showgetnew(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(appset.this);
        builder1.setTitle("版本更新：V" + app.getNewvesion());
        builder1.setMessage("版本更新，请谨记账号密码！");
        builder1.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
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
        builder1.setNegativeButton("稍后更新", null);
        builder1.setCancelable(true);
        builder1.show();
    }

    private void showshapuser(){
        String result = "login:"+messagerom.username+":" +messagerom.password;
        char[] datachar = result.toCharArray();
        char[] ydata = new char[result.length()];
        for (int i = 0; i < result.length(); i++) {
            ydata[i] = (char) (datachar[i] ^ 8);    //每一位都异或8
        }
        String contentEtString = new String(ydata);      //实例出一个新的字符串

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.logo);
        try {
                    /*contentEtString：字符串内容w：图片的宽h：图片的高logo：不需要logo的话直接传null*/
            Bitmap logo = BitmapFactory.decodeResource(getResources(), R.mipmap.logo);
            bitmap = CodeCreator.createQRCode(contentEtString, 400, 400, logo);
        } catch (WriterException e) {   }
        final ImageView imag = new ImageView(appset.this);
        imag.setImageBitmap(bitmap);
        AlertDialog.Builder builder= new AlertDialog.Builder(appset.this);
        builder.setTitle("共享登录");
        builder.setMessage("在登录界面扫描共享账号登录\r\n");
        builder.setView(imag);
        builder.show();
    }
    private class mClick implements View.OnClickListener{
        @Override
        public void onClick(View v){
            switch (v.getId()){
                case R.id.btnexitapp:
                    AlertDialog.Builder builder = new AlertDialog.Builder(appset.this);
                    builder.setMessage("点击确定退出程序");
                    builder.setTitle("提示");
                    builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //取消则什么都没做
                        }});
                    builder.setNegativeButton("退出登录", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            mysocket socket = new mysocket();
                            socket.offLogin(messagerom.username,messagerom.userlp);  //发送一条注销登录信息，不管是否成功都退出
                            app.toload1(appset.this);
                        }});
                    builder.setPositiveButton("退出程序", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                           app.exitapp(appset.this);
                        }});
                    builder.create().show();
                    break;
                case R.id.btnreturn:
                    finish();
                    break;
                default:
                    break;
            }
        }
    }

    //检查新版本
    private Boolean busy = false;
    private void cknewvesion(){
        if(busy){
            return;
        }else{busy=true;}
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
                appset.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ring.setVisibility(View.GONE);
                        String psw = socket.getpsw();
                        String newvesion = socket.getresmsg();
                        app.setNewvesion(newvesion);
                        if(psw.equals("true")){
                            String oldvision =app.getVersion();
                            if (!newvesion.equals(oldvision)) {
                                app.setNewapp(myset.TYPE_NEWS);
                                Toast.makeText(appset.this,"检测到有新版本更新！",Toast.LENGTH_SHORT).show();
                                initlist();   //检查到有新版本
                            }else{
                                Toast.makeText(appset.this,"当前为最新版本！",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(appset.this, psw, Toast.LENGTH_SHORT).show();
                            if(psw.equals("未登录")){
                                app.toload(appset.this);
                                busy=false;
                                return;
                            }
                        }
                        app.shownet(ring);
                        busy=false;
                    }
                });
            }}).start();
    }

}

