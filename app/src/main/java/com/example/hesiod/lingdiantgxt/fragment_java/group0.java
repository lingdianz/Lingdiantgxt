package com.example.hesiod.lingdiantgxt.fragment_java;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hesiod.lingdiantgxt.R;
import com.example.hesiod.lingdiantgxt.activity.messagerom;
import com.example.hesiod.lingdiantgxt.activity.mysocket;
import com.example.hesiod.lingdiantgxt.baseadapter.myeplvadapter;
import com.example.hesiod.lingdiantgxt.control_room;
import com.example.hesiod.lingdiantgxt.link;
import com.example.hesiod.lingdiantgxt.myJavaBean.ce_clients;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dm on 16-3-29.
 * 第一个页面
 */
public class group0 extends Fragment {

    private View view;
    private Activity myactivity;

    private SwipeRefreshLayout swipeRefreshLayout;
    private FragmentManager fragmentManager;
    private ExpandableListView expandableListView;
    private LinearLayout layoutback;

    private List<String> groupstring=new ArrayList<>();
    private List<String> groupcount=new ArrayList<>();
    private List<String> onlinechild=new ArrayList<>();
    List<List<ce_clients>> clientlist =new ArrayList<>();
    private link app;
    private messagerom messagerom;
    private Context context;
    private ProgressBar ring;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_group0, container, false);
        myactivity=getActivity();
        context=getContext();
        app = (link)myactivity.getApplication();
        messagerom=app.getmessagerom();

        initview(view);
        //showpaopao();
        reloadall();//第一次加载页面主动刷新
        return view;
    }
    @Override
    public void onStart(){
        super.onStart();
        updategroup();
    }
    private void initview(View view){
        ring =(ProgressBar)view.findViewById(R.id.ring);
        expandableListView=(ExpandableListView)view.findViewById(R.id.eplvfenzu);
        updategroup();//更新分组列表
        initeplv();//使能子列表点击事件
        Instantiation();//下拉刷新
    }

    private ImageView imgbk;

    //动画泡泡
    /*private void showpaopao(){
        layoutback=(LinearLayout)view.findViewById(R.id.layoutback);
        imgbk=(ImageView)view.findViewById(R.id.imgbk);
        imgbk.setBackgroundResource(R.mipmap.hihi);
        String addr = "https://lingdianz.xyz/paopao.gif";  //网址
        Uri uri = Uri.parse(addr);  //解析网址
        Glide.with(this)
                .load(uri)  //加载连接gif
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.mipmap.bluepp)   //缓存过程中占位
                .signature(new StringSignature(UUID.randomUUID().toString())).error(R.mipmap.bluepp)    //每次都加载，错误时使用默认gif
                .into(imgbk);

    }*/

    private void updategroup(){         //刷新列表
        groupstring=messagerom.getProjectlist();//1取值解析顺序不能变，已在加载中解析//
                // childstring=messagerom.getClientslist();//2取值解析顺序不能变//
        onlinechild=messagerom.getOnlineclient();   //3取值解析顺序不能变
        groupcount=messagerom.getLinecountlist();//4取值解析顺序不能变
                //Groupstring msglist=new Groupstring(groupstring,childstring,onlinechild,groupcount);
        clientlist=messagerom.getClientlist();
                //Groupstring msglist=new Groupstring(clientlist,onlinechild);///msglist
        myeplvadapter adapter=new myeplvadapter(clientlist,onlinechild,groupcount);
        expandableListView.setAdapter(adapter);
    }

    private void initeplv(){        //子列表点击监听
        //设置子项布局监听
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick (ExpandableListView parent, View v,int groupPosition, int childPosition, long id){
                Intent intent=new Intent(context,control_room.class);
                String shebeiname= clientlist.get(groupPosition).get(childPosition).getClientname();
                intent.putExtra("shebeiname",shebeiname);
                startActivityForResult(intent,1);
                return true;
            }
        });
        expandableListView.setOnItemLongClickListener(onItemLongClickListener);
    }
    //子项长按触发事件
    private AdapterView.OnItemLongClickListener onItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
            final long packedPosition = expandableListView.getExpandableListPosition(position);
            final int groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);
            final int childPosition = ExpandableListView.getPackedPositionChild(packedPosition);
            final String choose;
            //长按的是group的时候，childPosition = -1
            if (childPosition != -1) {
                choose=clientlist.get(groupPosition).get(childPosition).getClientname();
                //如果只存在一个或以下工程则打开新建工程的dialog
                if(clientlist.size()<2){newproject(choose,"新工程");return true;}

                final Spinner listView=new Spinner(context);
                listView.setPadding(20,0,20,0);
                List<String> showstring=new ArrayList<>(messagerom.getProjectlist());
                showstring.remove(groupPosition);
                ArrayAdapter<String> adapter=new ArrayAdapter<>(context,android.R.layout.simple_list_item_1,showstring);
                listView.setAdapter(adapter);
                AlertDialog.Builder builder= new AlertDialog.Builder(context);
                builder.setTitle("移动主机");
                builder.setMessage("移动"+choose+"到...");
                builder.setView(listView);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newproject=(String)listView.getSelectedItem();
                        if(newproject.length()==0){
                            Toast.makeText(context,"更改失败，输入工程名不能为空",Toast.LENGTH_LONG).show();
                            return;
                        }
                        if(newproject.equals("0")){
                            Toast.makeText(context,"更改失败，输入工程名不能为0",Toast.LENGTH_LONG).show();
                            return;
                        }
                        if(newproject.length()>10){
                            Toast.makeText(context,"更改失败，输入工程名过长",Toast.LENGTH_LONG).show();
                            return;
                        }
                        if(listView.getSelectedItemId()==0){
                            newproject="0";
                        }
                        movclient(choose,newproject);
                    }});
                builder.setNeutralButton("新建工程",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        newproject(choose,messagerom.getProjectlist().get(groupPosition));
                    }});
                builder.setCancelable(true);
                builder.setNegativeButton("取消",null);
                builder.show();
            }else if(childPosition==-1){
                if(groupPosition==0) {
                    return true;
                }
                choose=messagerom.getProjectlist().get(groupPosition).toString();
                final EditText input = new EditText(context);
                AlertDialog.Builder builder= new AlertDialog.Builder(context);
                input.setHint(choose);
                input.setGravity(1);
                input.setBackgroundResource(R.drawable.yuajiaotxqian);
                input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
                builder.setTitle("工程重命名");
                builder.setMessage("请输入1-10位的新工程名");
                builder.setView(input);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newproject=input.getText().toString().trim();
                        if(newproject.length()==0){
                            Toast.makeText(context,"更改失败，输入工程名不能为空",Toast.LENGTH_LONG).show();
                            return;
                        }
                        if(newproject.equals("0")){
                            Toast.makeText(context,"更改失败，输入工程名不能为0",Toast.LENGTH_LONG).show();
                            return;
                        }
                        if(newproject.length()>10){
                            Toast.makeText(context,"更改失败，输入工程名过长",Toast.LENGTH_LONG).show();
                            return;
                        }
                        if(groupstring.contains(newproject)){
                            Toast.makeText(context,"更改失败，存在同名工程",Toast.LENGTH_LONG).show();
                            return;
                        }
                        changeproject(newproject,choose);
                    }
                });
                builder.setCancelable(true);
                builder.setNegativeButton("取消",null);
                builder.show();
                }
            return true;
        }
    };

    //新建工程
    private void newproject(final String choose,String oldproject){
        final EditText input = new EditText(context);
        AlertDialog.Builder builder= new AlertDialog.Builder(context);
        input.setHint(oldproject);
        input.setGravity(1);
        input.setBackgroundResource(R.drawable.yuajiaotxqian);
        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        builder.setTitle("移动 "+choose+" 到新的工程");
        builder.setMessage("请输入1-10位的新工程名");
        builder.setView(input);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newproject=input.getText().toString().trim();
                if(newproject.length()==0){
                    Toast.makeText(context,"创建失败，输入工程名不能为空",Toast.LENGTH_LONG).show();
                    return;
                }
                if(newproject.equals("0")){
                    Toast.makeText(context,"创建失败，输入工程名不能为0",Toast.LENGTH_LONG).show();
                    return;
                }
                if(newproject.length()>10){
                    Toast.makeText(context,"创建失败，输入工程名过长",Toast.LENGTH_LONG).show();
                    return;
                }
                if(groupstring.contains(newproject)){
                    Toast.makeText(context,"创建失败，存在同名工程",Toast.LENGTH_LONG).show();
                    return;
                }
                movclient(choose,newproject);
            }});
        builder.setCancelable(true);
        builder.setNegativeButton("取消",null);
        builder.show();
    }

    private void changeproject(final String newprojectname,final String oldprojectname){
        if(busy){   //判断忙
            Toast.makeText(context,"网络忙碌，请稍后再试！",Toast.LENGTH_SHORT).show();
            return;
        }else{busy=true;}//置忙
        ring.setVisibility(View.VISIBLE);
        final mysocket socket = new mysocket();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    for(int ct=0;ct<2;ct++){
                        socket.changeproject(messagerom.username,oldprojectname,newprojectname,messagerom.userlp);//传递带密码的删除主机
                        do{Thread.sleep(100);}while (socket.getbusy());
                        if(socket.getpsw().equals("true")){
                            if(socket.getresmsg().equals("true")) {
                                messagerom.sevenewprojectname(oldprojectname, newprojectname);
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
                            updategroup();
                            Toast.makeText(context,"更改成功",Toast.LENGTH_SHORT).show();
                        }else if(psw.equals("未登录")){
                            Toast.makeText(myactivity, psw, Toast.LENGTH_SHORT).show();
                            app.toload(context);
                        }else{
                            Toast.makeText(context,psw,Toast.LENGTH_SHORT).show();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                        app.shownet(expandableListView);
                        busy=false;
                    }
                });
            }}).start();
    }

    //移动控制器
    private void movclient(final String clientname,final String newprojectname){
        if(busy){   //判断忙
            Toast.makeText(context,"网络忙碌，请稍后再试！",Toast.LENGTH_SHORT).show();
            return;
        }else{busy=true;}//置忙
        ring.setVisibility(View.VISIBLE);
        final mysocket socket = new mysocket();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    for(int ct=0;ct<1;ct++){
                        socket.movClient(messagerom.username,clientname,newprojectname, messagerom.userlp);//移动主机
                        do{Thread.sleep(100);}while (socket.getbusy());
                        if(socket.getpsw().equals("true")){
                            if(socket.getresmsg().equals("true")) {
                                messagerom.sevemovclient(clientname, newprojectname);
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
                        String psw = socket.getpsw();
                        if(psw.equals("true")){
                            Toast.makeText(myactivity, "移动成功", Toast.LENGTH_SHORT).show();
                            updategroup();
                        } else {
                            Toast.makeText(context,psw,Toast.LENGTH_SHORT).show();
                            if (psw.equals("未登录")){
                                app.toload(context);
                            }
                        }
                        swipeRefreshLayout.setRefreshing(false);
                        app.shownet(expandableListView);
                        busy=false;
                    }
                });
            }}).start();
    }
    public void Instantiation(){
        /**
         * 下拉刷新
         */
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark);//箭头颜色
        swipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
        swipeRefreshLayout.setProgressBackgroundColor(R.color.colorPrimary);//圆圈颜色
        swipeRefreshLayout.setProgressViewEndTarget(true, 200);//圆圈高度
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                reloadall();//加载列表
            }
        });
    }
    private Boolean busy=false;
    public void reloadall(){
        if(busy){
            Toast.makeText(context,"网络忙碌，请重试！",Toast.LENGTH_SHORT).show();
            return;
        }else{busy=true;}   //因为有多个操作，未全部完成不允许重建
        swipeRefreshLayout.setRefreshing(true);
        final mysocket socket = new mysocket();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    for(int ct=0;ct<2;ct++){
                        socket.reloadClient(messagerom.username,messagerom.userlp);
                        do{Thread.sleep(100);}while (socket.getbusy());
                        if(socket.getpsw().equals("true")){
                            messagerom.jxclients(socket.getresmsg());
                            break;
                        }
                    }
                    if(socket.getpsw().equals("true")) {
                        for (int ct = 0; ct < 2; ct++) {
                            socket.getOnclient(messagerom.username, messagerom.userlp);
                            do {Thread.sleep(100);} while (socket.getbusy());
                            if (socket.getpsw().equals("true")) {
                                messagerom.seveOnclient(socket.getresmsg());
                                break;
                            }
                        }
                    }
                }catch (Exception e){
                    socket.setpsw("加载异常");
                }
                myactivity.runOnUiThread(new Runnable(){
                    @Override
                    public void run(){
                        String psw = socket.getpsw();
                        if(psw.equals("true")){
                            updategroup();
                            sendnews(); //发送handle到消息红点
                        }else {
                            Toast.makeText(myactivity, psw, Toast.LENGTH_SHORT).show();
                            if (psw.equals("未登录")) {
                                app.toload(context);
                            }
                        }
                        swipeRefreshLayout.setRefreshing(false);
                        app.shownet(expandableListView);
                        busy=false; //清除忙
                    }
                });
            }
        }).start();
    }

    private int cout = 0;
    private void sendnews(){
        cout = app.getcout();//刷新次数计数
        cout++;
        app.setcout(cout);
        int cs = messagerom.getClients().size();
        int oc = messagerom.getOnlineclient().size();
        if(cs>oc){  //如果存在不在线，则发送显示红点
            app.setRbnew2(1);
            Handler mHandler = app.getHandler();
            Message message = Message.obtain();
            message.arg1 = cout;
            message.what = 1;
            mHandler.sendMessage(message);
        }
    }
    //显示网络异常，显示通知
   /* private void shownews() {
        FragmentManager fmanager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fmanager.beginTransaction();

        if(messagerom.getcknet()){
            if (fgnews != null) {
                fragmentTransaction.hide(fgnews);//在线隐藏网络异常通知
                fragmentTransaction.commit(); // 提交
            }
            return;
        }
        else {
            if (fgnews == null) {
                fgnews = new news();
                fragmentTransaction.add(R.id.fl_news, fgnews);//断网显示网络异常通知
            } else {
                fragmentTransaction.show(fgnews);
            }
            fragmentTransaction.commit(); // 提交
        }
    }*/
}