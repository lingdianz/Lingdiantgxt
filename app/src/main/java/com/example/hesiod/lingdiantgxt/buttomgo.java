package com.example.hesiod.lingdiantgxt;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hesiod.lingdiantgxt.activity.BaseAcvtivity;
import com.example.hesiod.lingdiantgxt.activity.messagerom;
import com.example.hesiod.lingdiantgxt.baseadapter.work_adapter;
import com.example.hesiod.lingdiantgxt.fragment_java.group0;
import com.example.hesiod.lingdiantgxt.fragment_java.news2;
import com.example.hesiod.lingdiantgxt.fragment_java.find1;
import com.example.hesiod.lingdiantgxt.fragment_java.myuser3;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页面内容
 * Created by dm on 16-1-19.
 */
public class buttomgo extends BaseAcvtivity {
    // 初始化顶部栏显示
    // 定义4个Fragment对象
    private group0 fg0;
    private find1 fg1;
    private news2 fg2;
    private myuser3 fg3;

    // 帧布局对象，用来存放Fragment对象
    private Fragment news;
    private TextView tvmulu;
    private Button imgbtset,test1;

    private RadioButton  rb0;
    private RadioButton  rb1;
    private RadioButton  rb2;
    private RadioButton  rb3;
    private RadioGroup   rg0;
    private FragmentManager fragmentManager;
    private ImageView rbnew0;
    private ImageView rbnew1;
    private ImageView rbnew2;
    private ImageView rbnew3;

    private ListView lvpoplist;

    private link app;
    private messagerom messagerom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buttomgo);
        app=(link)getApplication();
        messagerom=app.getmessagerom();
        fragmentManager = getSupportFragmentManager();
        GBcknet();
        initView(); // 初始化界面控件
    }
    /**
     * 初始化页面
     */
    private void initView() {
        imgbtset=(Button)findViewById(R.id.firstset);
        imgbtset.setOnClickListener(new buttomgo.mClick());
        //test1=(Button)findViewById(R.id.test1);
//        test1.setOnClickListener(new buttomgo.mClick());

        tvmulu=(TextView)findViewById(R.id.tvmulu);
        rg0=(RadioGroup)findViewById(R.id.rg_group);

        rb0=(RadioButton)findViewById(R.id.rb_0 );
        rb0.setOnClickListener(new buttomgo.mClick());
        rb1=(RadioButton)findViewById(R.id.rb_1 );
        rb1.setOnClickListener(new buttomgo.mClick());
        rb2=(RadioButton)findViewById(R.id.rb_2 );
        rb2.setOnClickListener(new buttomgo.mClick());
        rb3=(RadioButton)findViewById(R.id.rb_3 );
        rb3.setOnClickListener(new buttomgo.mClick());
        rbnew0 = (ImageView)findViewById(R.id.rbnew0);
        rbnew1 = (ImageView)findViewById(R.id.rbnew1);
        rbnew2 = (ImageView)findViewById(R.id.rbnew2);
        rbnew3 = (ImageView)findViewById(R.id.rbnew3);

        setRadioButtonImg();
        if(messagerom.getChoose() == 0){
            setChioceItem(0); // 初始化页面加载时显示第一个选项卡
        }
        if(messagerom.getChoose() == 1){
            setChioceItem(1); // 初始化页面加载时显示第一个选项卡
        }
        if(messagerom.getChoose() == 2){
            setChioceItem(2); // 初始化页面加载时显示第一个选项卡
        }
        if(messagerom.getChoose() == 3){
            setChioceItem(3); // 初始化页面加载时显示第一个选项卡
        }
        initrbnew();
        sethanlernews();
    }
    //设置底部导航按钮图片布局等。。。
    private void setRadioButtonImg() {
        int ds =(int)(70 * app.getDensity()/2);   //根据屏幕密度，适配
        Drawable First1 = getResources().getDrawable(R.drawable.tiaoguangimg);
        First1.setBounds(0, 0,ds ,ds);//参数从左到右依次是距左右边距离，距上下边距离，图片长度,图片宽度
        rb0.setCompoundDrawables(null, First1, null, null);//mainData.rgMainListenbook是控件id
        Drawable Second = getResources().getDrawable(R.drawable.fujinimg);
        Second.setBounds(0,0,ds ,ds);
        rb1.setCompoundDrawables(null, Second, null, null);
        Drawable Three = getResources().getDrawable(R.drawable.fenzuimg);
        Three.setBounds(0, 0,ds ,ds);
        rb2.setCompoundDrawables(null, Three, null, null);
        Drawable Four = getResources().getDrawable(R.drawable.wodeimg);
        Four.setBounds(0, 0,ds ,ds);
        rb3.setCompoundDrawables(null, Four, null, null);
    }

    private void sethanlernews(){
        Handler mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==1){
                    initrbnew();
                }
            }
        };
        app.setHandler(mHandler);
    }
    //判断显示消息提示小红点
    private void initrbnew(){
        if(app.getRbnew0()==0) { rbnew0.setVisibility(View.INVISIBLE);}else{ rbnew0.setVisibility(View.VISIBLE);}
        if(app.getRbnew1()==0) { rbnew1.setVisibility(View.INVISIBLE);}else{ rbnew1.setVisibility(View.VISIBLE);}
        if(app.getRbnew2()==0) { rbnew2.setVisibility(View.INVISIBLE);}else{ rbnew2.setVisibility(View.VISIBLE);}
        if(app.getRbnew3()==0) { rbnew3.setVisibility(View.INVISIBLE);}else{ rbnew3.setVisibility(View.VISIBLE);}
    }
    public class mClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rb_0 :
                    messagerom.setChoose(0);   //写选择0
                    app.setRbnew0(0);   //清0红点0
                    rbnew0.setVisibility(View.INVISIBLE);   //隐藏红点0
                    setChioceItem(0);
                    break;
                case R.id.rb_1 :
                    messagerom.setChoose(1);   //写选择1
                    app.setRbnew1(0);   //清0红点1
                    rbnew1.setVisibility(View.INVISIBLE);   //隐藏红点1
                    setChioceItem(1);
                    break;
                case R.id.rb_2 :
                    messagerom.setChoose(2);   //写选择2
                    app.setRbnew2(0);   //清0红点2
                    rbnew2.setVisibility(View.INVISIBLE);   //隐藏红点2
                    setChioceItem(2);
                    break;
                case R.id.rb_3 :
                    messagerom.setChoose(3);   //写选择3
                    app.setRbnew3(0);   //清0红点3
                    rbnew3.setVisibility(View.INVISIBLE);   //隐藏红点3
                    setChioceItem(3);
                    break;

                case R.id.firstset:
                    View popupview=getLayoutInflater().inflate(R.layout.poplist,null,false);

                    lvpoplist = (ListView) popupview.findViewById(R.id.lvpoplist);

                    List<String> datas = new ArrayList<>();
                    datas.add("添加设备");
                    datas.add("扫一扫");
                    datas.add("地图");
                    work_adapter adapter=new work_adapter(buttomgo.this,R.layout.work_item,datas);
                    lvpoplist.setAdapter(adapter);
                    PopupWindow popwindow = new PopupWindow(popupview, (int)(120*app.getDensity()), (int)((120+3)*app.getDensity()));//创建PopupWindow对象，指定宽度和高度
                    popwindow.setAnimationStyle(R.style.popup_window_anim);//设置动画
                    popwindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#9595ff")));//设置背景颜色
                    popwindow.setFocusable(true);//设置可以获取焦点
                    popwindow.setOutsideTouchable(true); //设置可以触摸弹出框以外的区域
                    popwindow.update(); //更新popupwindow的状态
                    popwindow.showAsDropDown(findViewById(R.id.firstset),0,10);//以下拉的方式显示，并且可以设置显示的位置
                    lisentlistview(popwindow);
                    break;
            default:
                break;
            }
        }
    }

    private void lisentlistview(final PopupWindow popwindow) {
        lvpoplist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String popname = lvpoplist.getItemAtPosition(position).toString();
                Intent intent;
                switch (position) {
                    case 0:
                        Toast.makeText(buttomgo.this, popname, Toast.LENGTH_SHORT).show();
                        intent = new Intent(buttomgo.this, saoyisao.class);
                        intent.putExtra("dowhat","addclient");
                        intent.putExtra("what","choose");
                        startActivityForResult(intent,1);
                        popwindow.dismiss();
                        break;
                    case 1:
                        Toast.makeText(buttomgo.this, popname, Toast.LENGTH_SHORT).show();
                        intent = new Intent(buttomgo.this, saoyisao.class);
                        intent.putExtra("dowhat","saoyisao");
                        intent.putExtra("what","saoyisao");
                        startActivityForResult(intent,1);
                        popwindow.dismiss();
                        break;
                    case 2:
                        Toast.makeText(buttomgo.this, popname, Toast.LENGTH_SHORT).show();
                        intent = new Intent(buttomgo.this, clientmap.class);
                        intent.putExtra("shebeiname","所有控制器");
                        startActivity(intent);
                        popwindow.dismiss();
                        break;
                    default:
                        break;
                }
            }
        });
    }
    /**
     * 设置点击选项卡的事件处理
     *
     * @param index 选项卡的标号：0, 1, 2, 3
     */

    private void setChioceItem(int index) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideFragments(fragmentTransaction);// 清空, 重置选项, 隐藏所有Fragment
        switch (index) {
            case 0:
            if (fg0 == null) {
                fg0 = new group0();
                fragmentTransaction.add(R.id.fl_layout, fg0);

            } else {
                fragmentTransaction.show(fg0);
            }
                tvmulu.setText("设备");
                break;
            case 1:
                if (fg1 == null) {
                    fg1 = new find1();
                    fragmentTransaction.add(R.id.fl_layout, fg1);
                } else {
                    fragmentTransaction.show(fg1);
                }
                tvmulu.setText("发现");
                break;
            case 2:
                if (fg2 == null) {
                    fg2 = new news2();
                    fragmentTransaction.add(R.id.fl_layout, fg2);
                } else {
                    fragmentTransaction.show(fg2);
                }
                tvmulu.setText("消息");
                break;
            case 3:
                if (fg3 == null) {
                    fg3 = new myuser3();
                    fragmentTransaction.add(R.id.fl_layout, fg3);
                } else {
                    fragmentTransaction.show(fg3);
                }
                tvmulu.setText("我");
                break;
            default:
                break;
        }
        fragmentTransaction.commit(); // 提交
    }

    //隐藏Fragment
    private void hideFragments(FragmentTransaction fragmentTransaction) {
        if (fg0 != null) {
            fragmentTransaction.hide(fg0);
        }
        if (fg1 != null) {
            fragmentTransaction.hide(fg1);
        }
        if (fg2 != null) {
            fragmentTransaction.hide(fg2);
        }
        if (fg3 != null) {
            fragmentTransaction.hide(fg3);
        }
    }

    //第一次点击事件发生的时间
    private long mExitTime;
     //点击两次返回退出app
     @Override
     public boolean onKeyDown(int keyCode, KeyEvent event) {
     if (keyCode == KeyEvent.KEYCODE_BACK) {
         if ((System.currentTimeMillis() - mExitTime) > 2000) {

                Toast.makeText(this, "再按一次退出APP", Toast.LENGTH_SHORT).show();
                //System.currentTimeMillis()系统当前时间
                mExitTime = System.currentTimeMillis();
                } else {
                finish();
                }
            return true;
            }
        return super.onKeyDown(keyCode, event);
     }

    private IntentFilter intentFilter;
    private NetworkChangeReceiver networkChangeReceiver;
    private void GBcknet(){         //开启广播监听网络连接
        intentFilter =new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver=new buttomgo.NetworkChangeReceiver();
        this.registerReceiver(networkChangeReceiver,intentFilter);
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        this.unregisterReceiver(networkChangeReceiver);//在活动销毁时，关闭广播
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == 1) {
            fg0.reloadall();    //添加设备返回后刷新一下
        }
    }

    //监听连接网络改变的广播
    class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent){
            ConnectivityManager connectivityManager=(ConnectivityManager)
            buttomgo.this.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
            if(networkInfo!=null&&networkInfo.isAvailable()){
                app.setcknet(true);   //连接正常
            }else {
                app.setcknet(false); //连接断开
            }
        }
    }
}