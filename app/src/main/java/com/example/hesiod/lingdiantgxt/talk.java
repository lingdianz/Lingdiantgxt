package com.example.hesiod.lingdiantgxt;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hesiod.lingdiantgxt.baseadapter.Msg;
import com.example.hesiod.lingdiantgxt.baseadapter.MsgAdapter;
import com.example.hesiod.lingdiantgxt.activity.BaseAcvtivity;
import com.example.hesiod.lingdiantgxt.activity.messagerom;

public class talk extends BaseAcvtivity {

    private EditText mMessageEdt;
    private Button btn_send,btn_back;
    private static TextView tvkfname;
    private String oldmsg=null;
    private static StringBuffer mConsoleStr = new StringBuffer();
    private Handler handler;
    private Boolean cklink=false,ckload=false;

    private List<Msg> msgList = new ArrayList<>();
    private RecyclerView msgRecyclerView;
    private MsgAdapter adapter;
    public static final int RES=0;
    public static final int SEND=1;
    public static final int TISHI=2;

    private link app;
    private messagerom messagerom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.talk);
        app=(link)getApplication();
        messagerom=app.getmessagerom();
        initMsgs();
        initView();     //界面初始化
        handmessage();  //handler等待从后台传过来的消息和状态
    }

    private void initView() {
        mMessageEdt = (EditText) findViewById(R.id.msg_edt);
        btn_send=(Button)findViewById(R.id.send_btn);
        btn_back=(Button)findViewById(R.id.btnreturn);
        btn_send.setOnClickListener(new mClick());
        btn_back.setOnClickListener(new mClick());

        tvkfname=(TextView)findViewById(R.id.tvkfname);
        Intent intent = getIntent();//获取用于启动第二个界面的intent
        String kefuname = intent.getStringExtra("kefu");//调用getStringExtra()，传入对应键值，得到传递的数据
        String line = intent.getStringExtra("online");
        tvkfname.setText(kefuname+line);

        msgRecyclerView =(RecyclerView)findViewById(R.id.msg_recycler_view);
        LinearLayoutManager layouManager=new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layouManager);
        adapter=new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);
    }

    public class mClick implements View.OnClickListener{
        @Override
        public void onClick (View v){
        switch (v.getId()) {
            case R.id.send_btn:
                getck();
                if (!cklink) {
                    Toast.makeText(talk.this,"网络异常，正在连接...", Toast.LENGTH_SHORT).show();
                    return;
                }
                String msg = mMessageEdt.getText().toString().trim();
                if ("".equals(msg)) {
                    Toast.makeText(talk.this,"发送内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                oldmsg = msg;//保存上一次发送的数据
                String goldaddr = "49.235.7.35:59287";
                String hdmsg = "message#cmd#"+msg+"#news#";
                //appUtil.sendmsg(goldaddr, hdmsg);
                mConsoleStr.append("\n\n  " + getTime(System.currentTimeMillis()) + "\n我:" + msg + "\r");
                mMessageEdt.setText(null);
                addlist(msg, SEND);////
                break;
            case R.id.btnreturn:
                finish();
                break;
            default:
                break;
            }
        }
    }
    private void addlist(String msg,int trx){
        Msg msg1=new Msg(msg,trx);
        msgList.add(msg1);
        adapter.notifyItemInserted(msgList.size()-1);
        msgRecyclerView.scrollToPosition(msgList.size()-1);
    }
    private void initMsgs(){
        Msg msg1=new Msg(getTime(System.currentTimeMillis())+"\n欢迎咨询，很高兴为您服务。。。",Msg.TYPE_TISHI);
        msgList.add(msg1);
    }
    private void handmessage(){
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                String listmsg;
                //如果来自子线程
                switch (msg.what){
                    case 0x123:
                        listmsg="\n"+msg.obj.toString()+"\n";
                        mConsoleStr.append(listmsg);
                        addlist(listmsg,RES);////
                        cklink=true;
                        ckload=true;
                        break;
                    case 0x124:
                            String odmsg=msg.obj.toString();
                            switch (odmsg){
                                case "linkfalse":
                                    cklink=false;
                                    listmsg="{"+oldmsg+"}发送失败，网络异常";
                                    mConsoleStr.append(listmsg);
                                    addlist(listmsg,TISHI);////
                                    mMessageEdt.setText(oldmsg);
                                    break;
                            case "loadfalse":
                                ckload=false;
                                listmsg="{"+oldmsg+"}发送失败,你已下线";
                                mConsoleStr.append("{"+oldmsg+"}发送失败,你已下线");
                                addlist(listmsg,TISHI);////
                                mMessageEdt.setText(oldmsg);
                                break;
                            default:
                                break;
                        }

                        break;
                    default:
                        break;
                }
            }
        };
        link appUtil = (link)getApplication();
        appUtil.setHandler(handler);
    }
    private void getck(){
        messagerom=app.getmessagerom();
        cklink=app.getcknet();
        ckload=app.getckload();
    }

    private static String getTime(long millTime) {
        Date d = new Date(millTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(d);
    }

}

