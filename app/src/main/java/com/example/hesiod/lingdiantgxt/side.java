package com.example.hesiod.lingdiantgxt;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hesiod.lingdiantgxt.activity.BaseAcvtivity;
import com.example.hesiod.lingdiantgxt.activity.StartActivity;
import com.example.hesiod.lingdiantgxt.activity.messagerom;
import com.example.hesiod.lingdiantgxt.activity.mysocket;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class side extends BaseAcvtivity {

    private TextView btnreadxieyi;
    private ImageView btneye;
    private Button btnside,btnback;
    private EditText etname,etphone,etpassword1,etpassword2;
    private CheckBox cbagrea;
    private ProgressBar ring;

    private link app;
    private messagerom messagerom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.side);
        app=(link)getApplication();
        messagerom=app.getmessagerom();
        initview();
    }
    private void initview(){
        ring =(ProgressBar)findViewById(R.id.ring);
        etname=(EditText) findViewById(R.id.etname);
        etphone=(EditText)findViewById(R.id.etphone);
        etpassword1=(EditText)findViewById(R.id.etpassword1);
        etpassword2=(EditText)findViewById(R.id.etpassword2);

        btneye=(ImageView)findViewById(R.id.btneye);
        btneye.setOnTouchListener(new tClick());
        btnreadxieyi=(TextView)findViewById(R.id.tvreadxieyi);
        btnreadxieyi.setOnClickListener(new mClick());
        btnback=(Button)findViewById(R.id.btnback);
        btnback.setOnClickListener(new mClick());
        btnside=(Button)findViewById(R.id.btnside);
        btnside.setOnClickListener(new mClick());

        cbagrea=(CheckBox)findViewById(R.id.cbagrea);//勾选框发送改变
        cbagrea.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){
                btnside.setEnabled(true);//勾选上表示同意协议，允许注册
                btnside.setText("注册");
            }else {
                btnside.setEnabled(false);//没勾选表示不同意协议，不允许注册
                btnside.setText("需要同意协议");
            }
            }
        });
    }

    //按键事件
    private class mClick implements View.OnClickListener{
        @Override
        public void onClick(View v){
            switch (v.getId()){
                case R.id.btnback:
                    finish();
                    break;
                case R.id.tvreadxieyi:
                    Toast.makeText(side.this,"我们定全力保密您的信息",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btnside:
                    btnside.setEnabled(false);
                    String psw,pass1,pass2,name1,phone1;
                    name1 = etname.getText().toString();
                    if(name1.length()<1||name1.length()>10){
                        Toast.makeText(side.this,"请输入1-10位用户名",Toast.LENGTH_SHORT).show();
                        btnside.setEnabled(true);
                        return;
                    }
                    phone1=etphone.getText().toString();
                    if(!isphone(phone1)){
                        Toast.makeText(side.this,"请输入正确的手机号",Toast.LENGTH_SHORT).show();
                        btnside.setEnabled(true);
                        return;
                    }
                    pass1=etpassword1.getText().toString();
                    pass2=etpassword2.getText().toString();
                    if(pass1.length()<6||pass1.length()>16){
                        Toast.makeText(side.this,"请输入6-16位密码",Toast.LENGTH_SHORT).show();
                        btnside.setEnabled(true);
                        return;
                    }
                    if(!pass1.equals(pass2)){
                        Toast.makeText(side.this,"两次密码输入不一致",Toast.LENGTH_SHORT).show();
                        btnside.setEnabled(true);
                        return;
                    }
                    toside(name1,pass1,phone1);
                    break;
                    default:
                        break;
            }
        }
    }
    private Boolean busy=false;
    //注册
    private void toside(final String username,final String password,final String phonenumber){
        if(busy){
            Toast.makeText(side.this,"网络忙碌，请稍后再试！",Toast.LENGTH_SHORT).show();
            return;
        }else {
            busy=true;
        }
        ring.setVisibility(View.VISIBLE);
        final mysocket socket = new mysocket();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                for(int ct=0;ct<3;ct++){
                   socket.Side(username,password,phonenumber);
                    do{Thread.sleep(100);}while (socket.getbusy());
                    if(socket.getpsw().equals("true")){
                        String resmsg=socket.getresmsg();
                        if(resmsg.length()==9){
                            messagerom.seveUsername(resmsg);
                            break;
                        }else{
                            socket.setpsw("注册异常");
                        }
                    }
                }}catch (Exception e){
                    socket.setpsw("网络异常");
                }
                side.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ring.setVisibility(View.GONE);
                        String psw=socket.getpsw();
                        if(psw.equals("true")){
                            final String username1=messagerom.getUsername();
                            final String password1=messagerom.getPassword();
                            AlertDialog.Builder builder= new AlertDialog.Builder(side.this);
                            builder.setTitle("注册成功");
                            builder.setMessage("账号："+username1+"\r\n用户名："+username+"\r\n手机号："+phonenumber+"\r\n");
                            builder.setPositiveButton("立即登录", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    messagerom.seveusermessage(side.this,username1,password,true,true);
                                    finish();
                                }
                            });
                            //builder.setNegativeButton("取消",null);
                            builder.setCancelable(false);
                            builder.show();
                        }else if(psw.equals("操作失败")){
                            Toast.makeText(side.this,"注册失败",Toast.LENGTH_SHORT).show();
                            btnside.setEnabled(true);
                        }else{
                            Toast.makeText(side.this,psw,Toast.LENGTH_SHORT).show();
                            btnside.setEnabled(true);
                        }
                        app.shownet(etname);
                        busy=false;
                    }
                });
            }
        }).start();
    }

    public static boolean isphone(String number) {

        String telRegex = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (number.isEmpty()){
            return false;
        }
        else return number.matches(telRegex);
    }
    //查看密码
    private class tClick implements View.OnTouchListener{
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    etpassword1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    break;
                case MotionEvent.ACTION_MOVE:
                    etpassword1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    break;
                case MotionEvent.ACTION_UP:
                    etpassword1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    break;
            }
            return true;
        }
    }

}
