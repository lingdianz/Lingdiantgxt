package com.example.hesiod.lingdiantgxt;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hesiod.lingdiantgxt.activity.BaseAcvtivity;
import com.example.hesiod.lingdiantgxt.activity.messagerom;
import com.example.hesiod.lingdiantgxt.activity.mysocket;

/**
 * Created by Hesiod on 2019/9/12.
 */

public class setpassword extends BaseAcvtivity {
    private Button btnback,btntijiao;
    private EditText etmessage,etmessageold;
    private TextView tvnews,tvnews2,tvcount,tvmax,tvcountold,tvmaxold;
    private ProgressBar ring;

    private link app;
    private messagerom messagerom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setpassword);
        app=(link)getApplication();
        messagerom =app.getmessagerom();
        initview();
    }
    private void initview(){
        ring = (ProgressBar)findViewById(R.id.ring);
        tvnews=(TextView)findViewById(R.id.tvnews);
        tvnews2=(TextView)findViewById(R.id.tvnews2);

        tvcount=(TextView)findViewById(R.id.tvcount);
        tvmax=(TextView)findViewById(R.id.tvmax);
        etmessage=(EditText)findViewById(R.id.etmessage);

        btnback=(Button)findViewById(R.id.btnreturn);
        btnback.setOnClickListener(new mClick());
        btntijiao=(Button)findViewById(R.id.btntijiao);
        btntijiao.setOnClickListener(new mClick());

        tvcountold=(TextView)findViewById(R.id.tvcountold);
        tvmaxold=(TextView)findViewById(R.id.tvmaxold);
        etmessageold=(EditText)findViewById(R.id.etmessageold);

        String news,max;
        Intent intent = getIntent();//获取用于启动第二个界面的intent
        news = intent.getStringExtra("news");//调用getStringExtra()，传入对应键值，得到传递的数据
        max = intent.getStringExtra("max");
        int imax=16;
        try{imax= Integer.parseInt(max);}catch (Exception ex){}
        tvnews.setText("更改"+news);
        tvnews2.setText("请输入新的"+news);
        tvmax.setText(max);
        tvmaxold.setText(max);
        InputFilter[] filters1 = {new InputFilter.LengthFilter(imax)};//最大输入字数
        etmessageold.setFilters(filters1);
        InputFilter[] filters = {new InputFilter.LengthFilter(imax)};//最大输入字数
        etmessage.setFilters(filters);
        switch (news) {
            case "用户名":
                etmessage.setInputType(InputType.TYPE_CLASS_TEXT);//输入纯文本
                etmessageold.setInputType(InputType.TYPE_CLASS_TEXT);//输入纯文本
                break;
            case "密码":
                etmessage.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_PASSWORD);//输入不可见密码格式
                etmessageold.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_PASSWORD);//输入不可见密码格式
                break;
            default:
                break;
        }

        etmessageold.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged (CharSequence s,int start, int before, int count){
                mHandler.sendEmptyMessage(1);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        etmessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged (CharSequence s,int start, int before, int count){
                mHandler.sendEmptyMessage(2);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    //handler
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    int coutold;
                    coutold=etmessageold.getText().length();
                    String scountold=String.valueOf(coutold);
                    tvcountold.setText(scountold);
                case 2:
                    int count;
                    count=etmessage.getText().length();
                    String scount=String.valueOf(count);
                    tvcount.setText(scount);
                    break;
                default:
                    break;
            }}};

    public class mClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnreturn:
                    finish();
                    break;
                case R.id.btntijiao:
                    String msg=etmessage.getText().toString().trim();
                    String msg1=etmessageold.getText().toString().trim();
                    if(!msg.equals(msg1)){
                        Toast.makeText(setpassword.this,"两次密码输入不一致", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(msg.length()<6||msg.length()>16){
                        Toast.makeText(setpassword.this,"字数范围：6-16", Toast.LENGTH_LONG).show();
                        return;
                    }
                    changepassword(msg);
                    break;
                default:
                    break;
            }
        }
    }

    private  Boolean busy = false;
    //改名
    private void changepassword(final String password1){
        if(busy){
            Toast.makeText(setpassword.this,"网络忙碌，请稍后再试！",Toast.LENGTH_SHORT).show();
            return;
        }else {busy=true; }
        ring.setVisibility(View.VISIBLE);
        final mysocket socket = new mysocket();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int a = 0; a < 3; a++) {
                        socket.setPassword(messagerom.username,messagerom.password,password1,messagerom.userlp);
                        do {Thread.sleep(100);} while (socket.getbusy());
                        if (socket.getpsw().equals("true")) {
                            if(socket.getresmsg().equals("true")) {
                                messagerom.sevePassword(password1);
                                break;
                            }
                        }
                    }
                }catch (Exception e){
                    socket.setpsw("网络异常");
                }
                setpassword.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ring.setVisibility(View.GONE);
                        String psw=socket.getpsw();
                        if(psw.equals("true")){
                            Toast.makeText(setpassword.this,"更改成功，需要重新登录！", Toast.LENGTH_LONG).show();
                            app.toload(setpassword.this);
                        } else {
                            Toast.makeText(setpassword.this,psw, Toast.LENGTH_SHORT).show();
                            if(psw.equals("未登录")){
                                app.toload(setpassword.this);
                            }
                        }
                            app.shownet(tvcount);
                            busy=false;
                    }
                });
            }
        }).start();
    }
}
