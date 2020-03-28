package com.example.hesiod.lingdiantgxt;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.hesiod.lingdiantgxt.activity.BaseAcvtivity;
import com.example.hesiod.lingdiantgxt.activity.messagerom;
import com.example.hesiod.lingdiantgxt.activity.mysocket;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class getmima extends BaseAcvtivity {

    private Button btnback,btngetmima;
    private EditText etusername,etphone;
    private link app;
    private messagerom messagerom;
    private ProgressBar ring;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.getmima);
        app=(link)getApplication();
        messagerom=app.getmessagerom();
        initview();
    }

    private void initview(){
        ring =(ProgressBar)findViewById(R.id.ring);
        etusername=(EditText)findViewById(R.id.etusername);
        etphone=(EditText)findViewById(R.id.etphone);
        btnback=(Button)findViewById(R.id.btnback);
        btnback.setOnClickListener(new mClick());
        btngetmima=(Button)findViewById(R.id.btngetmima);
        btngetmima.setOnClickListener(new mClick());
    }

    public class mClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnback:
                    finish();
                    break;
                case R.id.btngetmima:
                    btngetmima.setEnabled(false);
                    String username1,phone1;
                    username1=etusername.getText().toString();
                    if(username1.length()!=9){
                        Toast.makeText(getmima.this,"请输入正确的账号",Toast.LENGTH_SHORT).show();
                        btngetmima.setEnabled(true);
                        return;
                    }
                    phone1=etphone.getText().toString();
                    if(!isphone(phone1)){
                        Toast.makeText(getmima.this,"请输入正确的手机号",Toast.LENGTH_SHORT).show();
                        btngetmima.setEnabled(true);
                        return;
                    }
                    togetmima(username1,phone1);
                    break;
                default:
                    break;
            }}
    }

    private Boolean busy=false;
    private void togetmima(final String username,final String phonenumber){
        if(busy){
            Toast.makeText(getmima.this,"网络忙碌，请稍后再试！",Toast.LENGTH_SHORT).show();
            return;
        }else {
            busy=true;
        }
        ring.setVisibility(View.VISIBLE);
        final mysocket socket = new mysocket();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int ct = 0; ct < 3; ct++) {
                        socket.getMima(username, phonenumber);
                        do {Thread.sleep(100);} while (socket.getbusy());
                        if (socket.getpsw().equals("true")) {
                            String resmsg = socket.getresmsg();
                            if (resmsg.equals("null")) {
                                socket.setpsw("账号或手机号不存在");
                                break;
                            } else if (resmsg.length() >= 6 && resmsg.length() <= 16) {
                                messagerom.sevePassword(resmsg);
                                break;
                            } else {
                                socket.setpsw("数据错误");
                            }
                        }
                    }
                } catch (Exception e) {
                    socket.setpsw("网络异常");
                }
                getmima.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ring.setVisibility(View.GONE);
                        String psw = socket.getpsw();
                        if (psw.equals("true")) {
                            final String password1 = messagerom.getPassword();
                            AlertDialog.Builder builder = new AlertDialog.Builder(getmima.this);
                            builder.setTitle("成功找回");
                            builder.setMessage("账号：" + username + "\r\n手机号：" + phonenumber + "\r\n");
                            builder.setPositiveButton("立即登录", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    messagerom.seveusermessage(getmima.this, username, password1, true, true);
                                    finish();
                                }
                            });
                            builder.setCancelable(true);
                            builder.show();
                        } else {
                            Toast.makeText(getmima.this, psw, Toast.LENGTH_SHORT).show();
                            btngetmima.setEnabled(true);
                        }
                        app.shownet(etphone);
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

}
