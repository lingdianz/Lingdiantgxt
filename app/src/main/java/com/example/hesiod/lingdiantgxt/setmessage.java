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

public class setmessage extends BaseAcvtivity {
    private Button btnback,btntijiao;
    private EditText etmessage;
    private TextView tvnews,tvnews2,tvcount,tvmax;
    private ProgressBar ring;
    link app;
    messagerom messagerom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setmessage);
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

        String news,max;
        Intent intent = getIntent();//获取用于启动第二个界面的intent
        news = intent.getStringExtra("news");//调用getStringExtra()，传入对应键值，得到传递的数据
        max = intent.getStringExtra("max");
        int imax=10;
        try{imax= Integer.parseInt(max);}catch (Exception ex){}
        tvnews.setText("更改"+news);
        tvnews2.setText("请输入新的"+news);
        tvmax.setText(max);
        InputFilter[] filters = {new InputFilter.LengthFilter(imax)};//最大输入字数
        etmessage.setFilters(filters);
        switch (news) {
            case "用户名":
                etmessage.setInputType(InputType.TYPE_CLASS_TEXT);//输入纯文本
                break;
            default:
                break;
        }
        etmessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged (CharSequence s,int start, int before, int count){
                mHandler.sendEmptyMessage(0);
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
                case 0:
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
                    String name=etmessage.getText().toString().trim();
                    if(name.isEmpty()||name.length()>10){
                        Toast.makeText(setmessage.this,"字数范围：1-10", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    changename(name);
                    break;
                default:
                    break;
            }
        }
    }

    private Boolean busy =false;
    //改名
    private void changename(final String name){
        if(busy){
            Toast.makeText(setmessage.this,"网络忙碌，请稍后再试！",Toast.LENGTH_SHORT).show();
            return;
        }else {busy=true; }
        ring.setVisibility(View.VISIBLE);
        final mysocket socket = new mysocket();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int a = 0; a < 3; a++) {
                        socket.setName(messagerom.username,name,messagerom.userlp);
                        do {Thread.sleep(100);} while (socket.getbusy());
                        if (socket.getpsw().equals("true")) {
                            if(socket.getresmsg().equals("true")) {
                                messagerom.sevename(name);
                                break;
                            }
                        }
                    }
                }catch (Exception e){
                    socket.setpsw("网络异常");
                }
                setmessage.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ring.setVisibility(View.GONE);
                        String psw=socket.getpsw();
                        if(psw.equals("true")){
                            Toast.makeText(setmessage.this,"更改成功", Toast.LENGTH_SHORT).show();
                            finish();
                        } else{
                            Toast.makeText(setmessage.this,psw, Toast.LENGTH_SHORT).show();
                            if(psw.equals("未登录")){
                                app.toload(setmessage.this);
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
