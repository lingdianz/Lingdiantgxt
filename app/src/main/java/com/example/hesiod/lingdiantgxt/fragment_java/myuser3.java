package com.example.hesiod.lingdiantgxt.fragment_java;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.hesiod.lingdiantgxt.R;
import com.example.hesiod.lingdiantgxt.activity.mysocket;
import com.example.hesiod.lingdiantgxt.appset;
import com.example.hesiod.lingdiantgxt.baseadapter.MsgAdapter;
import com.example.hesiod.lingdiantgxt.baseadapter.mylist_adapter;
import com.example.hesiod.lingdiantgxt.baseadapter.myset;
import com.example.hesiod.lingdiantgxt.baseadapter.myset_adapter;
import com.example.hesiod.lingdiantgxt.kefu;
import com.example.hesiod.lingdiantgxt.link;
import com.example.hesiod.lingdiantgxt.talk;
import com.example.hesiod.lingdiantgxt.activity.messagerom;
import com.example.hesiod.lingdiantgxt.usermessage;
import com.google.zxing.WriterException;
import com.yzq.zxinglibrary.encode.CodeCreator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dm on 16-3-29.
 * 第一个页面
 */
public class myuser3 extends Fragment {
    private ListView lvmyset;
    private TextView tvname;

    private String username="";
    private List<myset> mysetlist =new ArrayList<>();

    private Activity myactivity;
    private Context context;
    private link app;
    private messagerom messagerom;
    private View view;
    private ProgressBar ring;
    private ImageView imglogo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_myuser3, container, false);
        myactivity=getActivity();
        context = getContext();
        app = (link)myactivity.getApplication();
        messagerom = app.getmessagerom();
        initview();
        return view;
    }
    @Override
    public void onStart(){
        super.onStart();
        if(tvname.getText().toString().trim().length()<1) {
            reloadname();//加载名称
        }
        if(messagerom.getName().trim().length()>1) {
            tvname.setText(messagerom.getName().trim());
        }
    }
    private void initview(){
        imglogo = (ImageView)view.findViewById(R.id.imglogo);
        imglogo.setOnClickListener(new mClick());
        ring = (ProgressBar)view.findViewById(R.id.ring);
        tvname=(TextView)view.findViewById(R.id.tvname);
        mylist_adapter adapter=new mylist_adapter(getContext(),R.layout.mysetmune,mysetlist);
        lvmyset =(ListView)view.findViewById(R.id.lvmyset);
        lvmyset.setAdapter(adapter);
        lvmyset.setOnItemClickListener(new mItemClick());

        username=messagerom.getUsername();
        initlist();
    }
    private void initlist(){
        Intent intent;
        intent=new Intent(getContext(),usermessage.class);
        myset user=new myset(R.mipmap.logo,"账号",username,myset.TYPE_MENU,intent);
        mysetlist.add(user);
        intent=new Intent(getContext(),kefu.class);
        myset lxkf=new myset(R.mipmap.dianhua0,"联系客服","",myset.TYPE_MENU,intent);
        mysetlist.add(lxkf);
        intent=new Intent(getContext(),appset.class);
        myset set=new myset(R.mipmap.setupset,"设置","",app.getNewapp(),intent);
        mysetlist.add(set);
    }
    private class mItemClick implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
            myset msg= mysetlist.get(position);
            Intent intent=msg.getintent();
            if(intent==null){return;}
            startActivity(intent);
        }
    }

    private class mClick implements View.OnClickListener{
        @Override
        public void onClick(View v){
            switch (v.getId()){
                case R.id.imglogo:
                    Toast.makeText(context,messagerom.username,Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    }

    private Boolean busy=false;
    //加载用户名字
    private void reloadname(){
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
                    socket.reloadName(username, messagerom.userlp);
                    while (socket.getbusy()){}
                    if(socket.getpsw().equals("true")){
                        String resmsg = socket.getresmsg();
                        if(resmsg.length()>0&&resmsg.length()<=10) {
                            messagerom.sevename(resmsg);
                            break;
                        }
                    }
                }
            }catch (Exception e){
                socket.setpsw("网络异常");
            }
            myactivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ring.setVisibility(View.GONE);
                    String psw=socket.getpsw();
                    if(psw.equals("true")){
                        tvname.setText(messagerom.getName());
                    }else {
                        Toast.makeText(context,psw,Toast.LENGTH_SHORT).show();
                        if(psw.equals("未登录")) {
                            app.toload(context);
                            busy=false;
                            return;
                        }
                    }
                    app.shownet(tvname);
                    busy=false;
                }
            });
            }
        }).start();
    }
}