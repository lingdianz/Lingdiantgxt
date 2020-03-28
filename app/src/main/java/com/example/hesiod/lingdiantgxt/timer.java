package com.example.hesiod.lingdiantgxt;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hesiod.lingdiantgxt.activity.BaseAcvtivity;
import com.example.hesiod.lingdiantgxt.activity.ckdriver;
import com.example.hesiod.lingdiantgxt.activity.messagerom;
import com.example.hesiod.lingdiantgxt.activity.mysocket;
import com.example.hesiod.lingdiantgxt.baseadapter.myset;
import com.example.hesiod.lingdiantgxt.baseadapter.timer_adapter;
import com.example.hesiod.lingdiantgxt.baseadapter.work_adapter;
import com.example.hesiod.lingdiantgxt.myJavaBean.ce_drivers;
import com.example.hesiod.lingdiantgxt.myJavaBean.ce_groups;

import java.util.ArrayList;
import java.util.List;

import static com.example.hesiod.lingdiantgxt.tiaoguang.getdrivertype;

public class timer extends BaseAcvtivity {

    private String type="",shebeiname = "";
    private ce_groups groupobj;

    private link app;
    private messagerom messagerom;

    private Button btnsure;
    private TextView tvname,tvstristimer,tvbn,tvvtg,tvcr,tvwd,tvtype,tvsetvtg,tvsetcr;
    private ImageView imgtimer;
    private ListView lvtimer;
    private ProgressBar ring;
    private TextView tvtitle;
    private RelativeLayout layoutset;
    private LinearLayout layoutdata,layoutnew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer);
        app = (link)getApplication();
        messagerom = app.getmessagerom();
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        shebeiname = intent.getStringExtra("shebeiname");
        initview();
    }

    private void initview(){
        layoutnew = (LinearLayout)findViewById(R.id.layoutnew);
        tvtype = (TextView) findViewById(R.id.tvtype);
        tvsetcr=(TextView)findViewById(R.id.tvsetcr);
        tvsetvtg=(TextView)findViewById(R.id.tvsetvtg);
        tvvtg = (TextView)findViewById(R.id.tvvtg);
        tvcr = (TextView)findViewById(R.id.tvcr);
        tvbn = (TextView)findViewById(R.id.tvbn);
        tvwd = (TextView)findViewById(R.id.tvwd);
        tvtitle = (TextView)findViewById(R.id.tvtitle);
        ring = (ProgressBar)findViewById(R.id.ring);
        lvtimer = (ListView)findViewById(R.id.lvtimer);
        tvname = (TextView)findViewById(R.id.tvname);
        btnsure = (Button)findViewById(R.id.btnsure);
        imgtimer = (ImageView)findViewById(R.id.imgtimer);
        layoutdata = (LinearLayout)findViewById(R.id.layoutdata);
        layoutset = (RelativeLayout)findViewById(R.id.layoutset);
        tvstristimer = (TextView)findViewById(R.id.tvstristimer);
        imgtimer.setOnClickListener(new mClick());
        btnsure.setOnClickListener(new mClick());

        setview();
    }
    private Boolean istimer=false;
    private class mClick implements View.OnClickListener{
        @Override
        public void onClick(View v){
            switch (v.getId()){
                case R.id.btnsure:
                    List<timerobj> tlist = new ArrayList<>(timerlist);
                    if(istimer) {   //总定时开启时，判断定时设置是否合法
                        for (int i = tlist.size()-1; i >=0 ; i--) {
                            if(!tlist.get(i).istimer){
                                tlist.remove(i);
                            }
                        }
                        if(tlist.size()>=2){        //定时器至少开启两个
                            for(int j=0;j<tlist.size();j++){
                                for(int a=j;a<tlist.size();a++){    //判断是否有重复时间
                                    if(a!=j){
                                        if(tlist.get(j).shi==tlist.get(a).shi && tlist.get(j).fen == tlist.get(a).fen){
                                            Toast.makeText(timer.this,"无效设置，定时器"+(j+1)+"和"+(a+1)+"时间重复定义！",Toast.LENGTH_LONG).show();
                                            return;
                                        }
                                    }
                                }
                                if(tlist.size()>=2){     //判断相邻时间亮度不相等
                                    int a = litle(tlist,j);
                                    if(tlist.get(j).bright==tlist.get(a).bright){
                                        Toast.makeText(timer.this,"无效设置，定时器"+(j+1)+"和"+(a+1)+"相邻亮度重复定义！",Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                }
                            }
                            settimer(timerlist);
                        }else{
                            Toast.makeText(timer.this,"无效设置，定时器至少要开启两个！",Toast.LENGTH_LONG).show();
                            return;
                        }
                    }else{      //总定时关闭时，可以忽略其他定时设置
                        settimer(timerlist);
                    }
                    break;
                case R.id.imgtimer:
                    istimer = !istimer;
                    if(istimer){
                        tvstristimer.setText("定时器/开启");
                        tvstristimer.setTextColor(getResources().getColor(R.color.blue3));
                        imgtimer.setImageResource(R.mipmap.noise);
                        lvtimer.setVisibility(View.VISIBLE);
                    }else{
                        tvstristimer.setText("定时器/关闭");
                        tvstristimer.setTextColor(getResources().getColor(R.color.gray));
                        imgtimer.setImageResource(R.mipmap.noisedis);
                        lvtimer.setVisibility(View.GONE);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    //返回自己前面的定时，最前时返回最后,条件是所有时间不能相等
    private int litle(List<timerobj> tlist,int j){
        if(tlist.size()<2){
            return j;
        }
        int a=j;
        for(int k=0;k<tlist.size();k++){
            if(tofen(tlist.get(k))>tofen(tlist.get(a))){a=k;}
        }
        int tbj=1;
        int tnow=tofen(tlist.get(j));
        for(int i=0;i<tlist.size();i++){
            if(i==j){continue;}
            int ti = tofen(tlist.get(i));
            if(ti!=0 && tnow > ti && ti>=tbj){
                a = i;
                tbj = ti;
            }
        }
        return a;
    }
    private int tofen(timerobj time){
        return time.shi*60+time.fen+1;
    }

    private Boolean busy=false;
    //发送定时设置
    private void settimer(final List<timerobj> tlist){
        if(busy){   //判断忙
            Toast.makeText(timer.this,"网络忙碌，请稍后再试！",Toast.LENGTH_SHORT).show();
            return;
        }else{busy=true;}//置忙
        ring.setVisibility(View.VISIBLE);
        final mysocket socket = new mysocket();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    List<String> tmsglist = gettmsglist(tlist);
                    for(int a=0;a<2;a++) {
                        socket.setGrouptimer(tmsglist);
                        do{Thread.sleep(100);}while (socket.getbusy());
                        if(socket.getpsw().equals("true")){
                            if(socket.getresmsg().equals("true")){
                                //messagerom.seveGrouptime(tmsglist);
                                break;
                            }else{
                                socket.setpsw("数据错误");
                            }
                        }}
                }catch (Exception e){
                    socket.setpsw("网络异常");
                }
                timer.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ring.setVisibility(View.GONE);
                        String psw=socket.getpsw();
                        if(psw.equals("true")) {
                            Toast.makeText(timer.this,"发送成功",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(timer.this, psw, Toast.LENGTH_SHORT).show();
                            if(psw.equals("未登录")){
                                app.toload(timer.this);
                                return;
                            }
                        }
                        app.shownet(tvname);
                        busy=false;
                    }
                });
            }}).start();
    }

    //设置定时器,访问格式：changetimer#cmd#username#cmd#clientname#cmd#groupname#cmd#
    // istimer#cmd#b1#cmd#b2#cmd#b3#cmd#b4#cmd#t1#cmd#t2#cmd#t3#cmd#t4#cmd#userlp#news#
    private List<String> gettmsglist(List<timerobj> tlist){
        List<String> tmsglist = new ArrayList<>();
        tmsglist.add(messagerom.username);
        tmsglist.add(clientname);
        tmsglist.add(shebeiname);
        tmsglist.add(istimer ? "1" : "0");
        for(int i=0;i<tlist.size();i++){
            tmsglist.add(""+tlist.get(i).bright);
        }
        for(int i=0;i<tlist.size();i++){
            tmsglist.add(tlist.get(i).istimer ? ""+tofen(tlist.get(i)) : "0");
        }
        tmsglist.add(messagerom.userlp);
        return tmsglist;
    }

    private void setview()
    {
        tvname.setText(shebeiname+" ");
        switch (type){
            case "driver":
                tvtitle.setText("定时模式");
                ce_drivers driverobj = new ce_drivers();
                for(int ct=0;ct<messagerom.getDrivers().size();ct++){
                    if(messagerom.getDrivers().get(ct).getDrivername().equals(shebeiname)){
                        driverobj = messagerom.getDrivers().get(ct);
                        String group = driverobj.getGroupname();
                        for(int ck=0;ck<messagerom.getGroups().size();ck++){
                            if(messagerom.getGroups().get(ck).getGroupname().equals(group)){
                                groupobj = messagerom.getGroups().get(ck);
                                break;
                            }
                        }
                    }
                }
                layoutset.setVisibility(View.GONE);
                layoutdata.setVisibility(View.VISIBLE);
                String str = driverobj.getVoltage()+"V";
                tvvtg.setText(str);
                str = driverobj.getCurrent()+"A";
                tvcr.setText(str);
                str = driverobj.getCelcius()+"℃";
                tvwd.setText(str);
                str = driverobj.getBrightness()+"G";
                tvbn.setText(str);
                tvtype.setText(getdrivertype(driverobj.getType())); //获取型号
                str = driverobj.getSetvoltage()+"V";
                tvsetvtg.setText(str);
                str = driverobj.getSetcurrent()+"A";
                tvsetcr.setText(str);
                layoutnew.setVisibility(View.VISIBLE);
                showtimer(groupobj);
                ckdriverdata(driverobj);
                break;
            case "group":
                layoutnew.setVisibility(View.GONE);//  不显示信息
                tvtitle.setText("定时模式");
                layoutdata.setVisibility(View.GONE);
                layoutset.setVisibility(View.GONE);
                for(int ct = 0;ct<messagerom.getGroups().size();ct++){
                    if(messagerom.getGroups().get(ct).getGroupname().equals(shebeiname)){
                        showtimer(messagerom.getGroups().get(ct));
                        break;
                    }
                }
                break;
            case "settimer":
                layoutnew.setVisibility(View.GONE); //  不显示信息
                layoutset.setVisibility(View.VISIBLE);
                layoutdata.setVisibility(View.GONE);
                tvtitle.setText("设置定时器");
                for(int ct = 0;ct<messagerom.getGroups().size();ct++){
                    if(messagerom.getGroups().get(ct).getGroupname().equals(shebeiname)){
                        showsettimer(messagerom.getGroups().get(ct));
                        clientname = messagerom.getGroups().get(ct).getClient();
                        break;
                    }
                }

                break;
            default:
                finish();
                break;
        }
    }

    private void ckdriverdata(ce_drivers driverobj){
        String str;
        str = driverobj.getVoltage()+"V";
        tvvtg.setText(str);
        str = driverobj.getCurrent()+"A";
        tvcr.setText(str);
        str = driverobj.getCelcius()+"℃";
        tvwd.setText(str);
        str= driverobj.getBrightness().equals("104") ?  "离线": driverobj.getBrightness() + "G";
        tvbn.setText(str);

//        public static int DRIVER_OK = 1;//通过
//        public static int DRIVER_U_BAD = 21;//电压坏
//        public static int DRIVER_I_BAD = 22;//电流坏
//        public static int DRIVER_C_BAD = 23;//温度坏
//        public static int DRIVER_LOST = 3;//离线
//        public static int DRIVER_I_LOST = 32;//电流过低
//        public static int DRIVER_U_EER = 41;//电压警报
//        public static int DRIVER_I_EER = 42;//电流警报
//        public static int DRIVER_G_EER = 43;//亮度警报
//        public static int DRIVER_D_EER = 44;//数据错误警报
//        public static int DRIVER_C_EER = 45;//温度警报

        switch (ckdriver.ckdriverpsw(messagerom.getGroups(),driverobj)){
            case 1:
                break;
            case 21:
                tvvtg.setBackgroundResource(R.drawable.yuan_bad);
                break;
            case 22:
                tvcr.setBackgroundResource(R.drawable.yuan_bad);
                break;
            case 23:
                tvwd.setBackgroundResource(R.drawable.yuan_bad);
                break;
            case 3:
                tvwd.setBackgroundResource(R.drawable.yuan_lost);
                tvvtg.setBackgroundResource(R.drawable.yuan_lost);
                tvcr.setBackgroundResource(R.drawable.yuan_lost);
                tvbn.setBackgroundResource(R.drawable.yuan_lost);
                break;
            case 32:
                tvcr.setBackgroundResource(R.drawable.yuan_lost);
                break;
            case 41:
                tvvtg.setBackgroundResource(R.drawable.yuan_err);
                break;
            case 42:
                tvcr.setBackgroundResource(R.drawable.yuan_err);
                break;
            case 43:
                tvbn.setBackgroundResource(R.drawable.yuan_err);
                break;
            case 44:
                tvbn.setBackgroundResource(R.drawable.yuan_err);
                tvbn.setText("err");
                break;
            case 45:
                tvwd.setBackgroundResource(R.drawable.yuan_err);
                break;
            default:
                break;
        }
    }
    private String clientname="";
    //显示设置界面
    private void showsettimer(ce_groups group){
        if(group == null){
            Toast.makeText(timer.this,"数据错误，请返回刷新重试！",Toast.LENGTH_SHORT).show();
            finish();
        }else{
            if(group.getIstimer().equals("0")){
                istimer=false;
                tvstristimer.setText("定时器/关闭");
                tvstristimer.setTextColor(getResources().getColor(R.color.gray));
                imgtimer.setImageResource(R.mipmap.noisedis);
                lvtimer.setVisibility(View.GONE);
            }else {
                istimer = true;
                tvstristimer.setText("定时器/开启");
                tvstristimer.setTextColor(getResources().getColor(R.color.blue3));
                imgtimer.setImageResource(R.mipmap.noise);
                lvtimer.setVisibility(View.VISIBLE);
            }

            //tvname,cbtime,spshi,spfen,spbright
            //定时1： 开  12:00 亮度：100
            this.timerlist =gettimelist(group);
            final timer_adapter adapter = new timer_adapter(timer.this,R.layout.timer_item,timerlist,timer.this, myset.TYPE_MENU);
            lvtimer.setAdapter(adapter);
            adapter.setOnItemClickListener(new timer_adapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int name, int vid, int sele) {
                    seleview(adapter,view,name,vid,sele);
                }

                @Override
                public void onItemChange(int name) {
                    checkchange(name,adapter);
                }
            });
        }
    }

    private List<timerobj> gettimelist(ce_groups group){
        List<timerobj> timerlist1 = new ArrayList<>();
        timerobj timer1 = new timerobj();
        timer1.name=0;timer1.istimer=!(intshi(group.getTime1(),0)==0);timer1.shi=intshi(group.getTime1(),1);
        timer1.fen=intshi(group.getTime1(),2);timer1.bright=intbright(group.getBrightness1());
        timerlist1.add(timer1);
        timerobj timer2 = new timerobj();
        timer2.name=1;timer2.istimer=!(intshi(group.getTime2(),0)==0);timer2.shi=intshi(group.getTime2(),1);
        timer2.fen=intshi(group.getTime2(),2);timer2.bright=intbright(group.getBrightness2());
        timerlist1.add(timer2);
        timerobj timer3 = new timerobj();
        timer3.name=2;timer3.istimer=!(intshi(group.getTime3(),0)==0);timer3.shi=intshi(group.getTime3(),1);
        timer3.fen=intshi(group.getTime3(),2);timer3.bright=intbright(group.getBrightness3());
        timerlist1.add(timer3);
        timerobj timer4 = new timerobj();
        timer4.name=3;timer4.istimer=!(intshi(group.getTime4(),0)==0);timer4.shi=intshi(group.getTime4(),1);
        timer4.fen=intshi(group.getTime4(),2);timer4.bright=intbright(group.getBrightness4());
        timerlist1.add(timer4);
        return timerlist1;
    }

    private int intshi(String zfen,int sf){ //sf为0取开关，sf为1取时，sf为2时取分
        int fen=0;
        try {
            fen = Integer.parseInt(zfen);
        }catch (Exception e){
            return 0;
        }
        if(fen==0){
            return 0;
        }else{
            switch (sf){
                case 0:
                    return 1;
                case 1:
                    return (fen-1)/60;
                case 2:
                    return (fen-1)%60;
                default:
                    return 0;
            }
        }
    }
    private int intbright(String bright){
        try{
            return Integer.parseInt(bright);
        }catch (Exception e){
            return 0;
        }
    }
    public class timerobj{
        public int name=0;
        public Boolean istimer=true;
        public int shi = 10;
        public int fen = 30;
        public int bright = 100;
    }
    private List<timerobj> timerlist = new ArrayList<>();

    //显示不能设置的定时数据
    private void showtimer(ce_groups groupobj){
        if(groupobj == null){
            Toast.makeText(timer.this,"数据错误，请返回刷新重试！",Toast.LENGTH_SHORT).show();
            finish();
        }else{
            if(groupobj.getIstimer().equals("0")){
                Toast.makeText(timer.this,"数据错误，请返回刷新重试！",Toast.LENGTH_SHORT).show();
                finish();
            }else{
                lvtimer.setVisibility(View.VISIBLE);
//                List<String> timelist = new ArrayList<>();
//                String time1 ="定时1：" + gettimestr(groupobj.getTime1())+"   亮度：" +groupobj.getBrightness1();
//                timelist.add(time1);
//                String time2 ="定时2：" + gettimestr(groupobj.getTime2())+"   亮度：" +groupobj.getBrightness2();
//                timelist.add(time2);
//                String time3 ="定时3：" + gettimestr(groupobj.getTime3())+"   亮度：" +groupobj.getBrightness3();
//                timelist.add(time3);
//                String time4 ="定时4：" + gettimestr(groupobj.getTime4())+"   亮度：" +groupobj.getBrightness4();
//                timelist.add(time4);
//                ArrayAdapter<String> adapter = new ArrayAdapter<>(timer.this,android.R.layout.simple_list_item_1,timelist);
//                lvtimer.setAdapter(adapter);
                List<timerobj> timelist = gettimelist(groupobj);
                timer_adapter adapter = new timer_adapter(timer.this,R.layout.timer_item,timelist,timer.this,myset.TYPE_SHOW);
                lvtimer.setAdapter(adapter);
            }
        }
    }

    //分组转换为24小时制时间显示
    private String gettimestr(String str){
        if(str.equals("0")){
            return "关";
        }else {
            int zfen = 0;
            try{
                zfen = Integer.parseInt(str);
            }catch (Exception e){
                return "err";
            }
            if(0 < zfen || zfen <= 1440){   //正确的范围为（1-1440）
                zfen --;        //因为时间显示习惯为00:00到23:59，所以要切换为（0-1439）
                String shi = ""+zfen/60;
                if(shi.length()==1){
                    shi = "0"+shi;
                }
                String fen = ""+zfen % 60;
                if(fen.length()==1){
                    fen = "0"+fen;
                }
                return shi + ":"+fen;
            }else{
                return "关";
            }
        }
    }

    private void seleview(final timer_adapter timeradapter,View view,final int name,final int vid,int sele){
        final View popupview=getLayoutInflater().inflate(R.layout.poplist,null,false);
        final ListView lvpoplist = (ListView) popupview.findViewById(R.id.lvpoplist);

        final List<String> setstring = new ArrayList<>();
        int maxid = vid == 0 ? 23 : (vid==1 ? 59 : 100);
        for(int i = 0;i <= maxid;i++){
            setstring.add(strtwo(i));
        }
        final work_adapter adapter1=new work_adapter(timer.this,R.layout.work_item,setstring);
        lvpoplist.setAdapter(adapter1);
        lvpoplist.setSelection(sele);   //跳到选择的行
        final PopupWindow popwindow = new PopupWindow(popupview, (int)(50*app.getDensity()), (int)(120*app.getDensity()));   //创建PopupWindow对象，指定宽度和高度
        popwindow.setAnimationStyle(R.style.popup_window_anim);     //设置动画
        popwindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#008030")));    //设置背景颜色
        popwindow.setFocusable(true);   //设置可以获取焦点
        popwindow.setOutsideTouchable(true);    //设置可以触摸弹出框以外的区域
        popwindow.update(); //更新popupwindow的状态
        popwindow.showAsDropDown(view,0,(int)(-120*app.getDensity()));

        lvpoplist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int timeid = name;
                switch (vid){
                    case 0: //0为时
                        popwindow.dismiss();
                        timerlist.get(timeid).shi=position;
                        timeradapter.notifyDataSetChanged();
                        break;
                    case 1: //1为分
                        popwindow.dismiss();
                        timerlist.get(timeid).fen=position;
                        timeradapter.notifyDataSetChanged();
                        break;
                    case 2: //2为亮度
                        popwindow.dismiss();
                        timerlist.get(timeid).bright=position;
                        timeradapter.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
            }
        });
    }
    private void checkchange(int name,timer_adapter timeradapter){
        Boolean istime = !timerlist.get(name).istimer;
        timerlist.get(name).istimer = istime;
        timeradapter.notifyDataSetChanged();
    }

    //转两位显示
    private String strtwo(int i){
        String str = ""+i;
        if(str.length()==1){
            return "0" + str;
        } else{
            return str;
        }
    }
}
