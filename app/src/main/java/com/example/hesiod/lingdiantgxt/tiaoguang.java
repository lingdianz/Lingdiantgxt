package com.example.hesiod.lingdiantgxt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hesiod.lingdiantgxt.activity.BaseAcvtivity;
import com.example.hesiod.lingdiantgxt.activity.ckdriver;
import com.example.hesiod.lingdiantgxt.activity.messagerom;
import com.example.hesiod.lingdiantgxt.activity.mysocket;
import com.example.hesiod.lingdiantgxt.myJavaBean.ce_groups;
import com.example.hesiod.lingdiantgxt.myJavaBean.ce_drivers;

import static java.lang.Math.PI;
import static java.lang.Math.atan;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class tiaoguang extends BaseAcvtivity {

    private ce_drivers driverobj=new ce_drivers();
    private ce_drivers clientobj=new ce_drivers();
    private ce_groups groupobj=new ce_groups();
    private String shebeiname="",type="";
//    imgtimer,
    private TextView tvname,tvbright,tvtype,tvsetvtg,tvsetcr,tvfangwei,tvvtg,tvcr,tvbn,tvwd;
    private CardView cardsend,cardbright;
    private CardView cardview;
    private ImageView imgadd,imgdec,imgclose;
    private RelativeLayout layouttiaoguang,layoutbright;
    private ProgressBar ring;

    private link app;
    private messagerom messagerom;
    private ImageView imgpoint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tiaoguang);
        app=(link)getApplication();
        messagerom=app.getmessagerom();
        Intent intent=getIntent();
        shebeiname=intent.getStringExtra("shebeiname");
        initview();//用了shebeiname所以要放在接收shebeiname后面
    }

    private void initview() {
        ring = (ProgressBar)findViewById(R.id.ring);
        imgpoint=(ImageView)findViewById(R.id.imgpoint);
        tvname = (TextView) findViewById(R.id.tvname);
        layoutbright = (RelativeLayout)findViewById(R.id.layoutbright);
        tvfangwei = (TextView) findViewById(R.id.tvfangwei);
        tvbright=(TextView)findViewById(R.id.tvbright);
        tvtype = (TextView) findViewById(R.id.tvtype);
        tvsetcr=(TextView)findViewById(R.id.tvsetcr);
        tvsetvtg=(TextView)findViewById(R.id.tvsetvtg);
        tvvtg = (TextView)findViewById(R.id.tvvtg);
        tvcr = (TextView)findViewById(R.id.tvcr);
        tvwd = (TextView)findViewById(R.id.tvwd);
        tvbn = (TextView)findViewById(R.id.tvbn);
        cardsend = (CardView) findViewById(R.id.cardsend);
        cardbright = (CardView)findViewById(R.id.cardbright);
        //imgtimer = (ImageView) findViewById(R.id.imgtimer);
        imgclose = (ImageView)findViewById(R.id.imgclose);
        imgadd = (ImageView) findViewById(R.id.imgadd);
        imgdec = (ImageView) findViewById(R.id.imgdec);
        //imgtimer.setOnClickListener(new mClick());
        imgadd.setOnClickListener(new mClick());
        imgdec.setOnClickListener(new mClick());
        cardsend.setOnClickListener(new mClick());
        layoutbright.setOnTouchListener(new mTouch());
        imgclose.setOnClickListener(new mClick());

        cardview = (CardView) findViewById(R.id.cardview);
        layouttiaoguang=(RelativeLayout)findViewById(R.id.tiaoguang);

        pointx = imgpoint.getLeft();
        pointy = imgpoint.getTop();

        jiexidata();//初始化界面

    }
    private int pointx = 0;
    private int pointy = 0;

    //private static float pr =130f ;
    private void showpoint(int bright){
        if(bright>100){
            if(bright==104){
                tvbright.setText("离线");
            }
            bright=100;
        }
        String str = ""+bright;
        tvbright.setText(str);
        float pr = 160 / 2 * 5 / 6  * app.getDensity();     //160为cardbright的dp宽度，取半径，减圆环宽度的一半，最后转换成px单位，
        double A=bright/100f * 3 / 2 * PI;  //100f不可改，只有为f才能除出小数
        float py = pr * (float)cos(A);
        float px = pr * (float)sin(A);

        int left = pointx - (int)px;
        int top = pointy + (int)py;
        FrameLayout.LayoutParams params=(FrameLayout.LayoutParams)imgpoint.getLayoutParams();
        params.setMargins(left,top,0,0);
        imgpoint.setLayoutParams(params);
    }

    private void jiexidata()
    {
        String name1="",bright1="";
        //判断是否为控制器，解析。。。
        if(shebeiname.contains("c")){
            if(messagerom.getDrivers().size()==0){
                Toast.makeText(tiaoguang.this,"没有驱动，赶紧去添加驱动吧！",Toast.LENGTH_SHORT).show();
                finish();
            }
            type="client";
            name1=shebeiname+" ";
            tvname.setText(name1);
            showpoint(100);
            tvtype.setText("全部非定时驱动");
            tvsetvtg.setVisibility(View.GONE);
            tvsetcr.setVisibility(View.GONE);
            tvvtg.setVisibility(View.GONE);
            tvcr.setVisibility(View.GONE);
            tvwd.setVisibility(View.GONE);
            tvbn.setVisibility(View.GONE);
            //imgtimer.setVisibility(View.GONE);
            if(messagerom.getOnlineclient().contains(shebeiname)) {
                cardview.setCardBackgroundColor(getResources().getColor(R.color.blue0));
            }else{
                cardview.setCardBackgroundColor(getResources().getColor(R.color.gray));
            }
        }
        //判断是否为驱动，解析。。。
        else if(shebeiname.contains("d")){
            for(int ct=0;ct<messagerom.getDrivers().size();ct++){   //查找驱动数据
                if(shebeiname.equals(messagerom.getDrivers().get(ct).getDrivername())){
                    driverobj=messagerom.getDrivers().get(ct);
                    break;
                }
            }
            if(driverobj!=null){
                type="driver";
                name1=shebeiname+" ";
                tvname.setText(name1);
                if(driverobj.getIstimer()){
                    //imgtimer.setVisibility(View.VISIBLE);
                    for(int k=0;k<messagerom.getGroups().size();k++){
                        if(messagerom.getGroups().get(k).getGroupname().equals(driverobj.getGroupname())){
                            showpoint(ckdriver.timebright(messagerom.getGroups().get(k)));      //调用ckdriver里的查定时亮度的公共类
                        }
                    }
                }else{
                    //imgtimer.setVisibility(View.GONE);
                    bright1=driverobj.getSetbrightness();   //设置手动模式亮度的按钮，放置当前设置的亮度
                    int bright = Integer.parseInt(bright1);
                    showpoint(bright);
                }
                tvtype.setText(getdrivertype(driverobj.getType())); //获取型号
                String str = driverobj.getSetvoltage()+"V";
                tvsetvtg.setText(str);
                str = driverobj.getSetcurrent()+"A";
                tvsetcr.setText(str);
                ckdriverdata(driverobj);
                if(messagerom.getOnlineclient().contains(driverobj.getClient())) {
                    cardview.setCardBackgroundColor(getResources().getColor(R.color.blue0));
                }else{
                    cardview.setCardBackgroundColor(getResources().getColor(R.color.gray));
                }
            }else{
                Toast.makeText(tiaoguang.this,"抱歉，没有找到该设备，请刷新重试！",Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        //判断是否为分组，解析。。。
        else if(shebeiname.contains("#g#")){
            shebeiname = shebeiname.replace("#g#","");
            if(shebeiname.length()>6){
                tvname.setTextSize(10);
            }
            for(int ct=0;ct<messagerom.getGroups().size();ct++){
                if(shebeiname.contains(messagerom.getGroups().get(ct).getGroupname())){
                    groupobj=messagerom.getGroups().get(ct);
                    break;
                }
            }
            if(groupobj!=null){
                type="group";
                name1=shebeiname+" ";
                tvname.setText(name1);
                tvtype.setText("分组");
                tvsetvtg.setVisibility(View.GONE);
                tvsetcr.setVisibility(View.GONE);
                tvvtg.setVisibility(View.GONE);
                tvcr.setVisibility(View.GONE);
                tvwd.setVisibility(View.GONE);
                tvbn.setVisibility(View.GONE);
                if(messagerom.getOnlineclient().contains(groupobj.getClient())) {
                    cardview.setCardBackgroundColor(getResources().getColor(R.color.blue0));
                }else{
                    cardview.setCardBackgroundColor(getResources().getColor(R.color.gray));
                }
                //if(groupobj.getIstimer().equals("0")){  //判断分组是否为定时模式
                    //imgtimer.setVisibility(View.GONE);
                    int bright = Integer.parseInt(groupobj.getSetbright());
                    showpoint(bright);
                //}else{
                    //imgtimer.setVisibility(View.VISIBLE);
                    //int bright = ckdriver.timebright(groupobj);     //定时模式时，直接显示当前定时器亮度
                    //showpoint(bright);                              //调用ckdriver里的查定时亮度的公共类
                //}
            }else{
                Toast.makeText(tiaoguang.this,"抱歉，没有找到该分组，请刷新重试！",Toast.LENGTH_SHORT).show();
                finish();
            }
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

    public static String getdrivertype(String type){
        switch (type){
            case "0":
                return "恒压型";
            case "1":
                return "恒流型";
            default:
                return "其他型";
        }
    }

    private class mTouch implements View.OnTouchListener{
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (v.getId()) {
                case R.id.layoutbright:
                    switch (event.getAction()){
                        case MotionEvent.ACTION_MOVE:

                            float tx1 = event.getX();
                            float ty1 = event.getY();

                            int bright1 = getbright(tx1,ty1);
                            if(bright1>0){
                                if(bright1>100){bright1=100;}
                                showpoint(bright1);
                            }
                            return true;
                        default:
                            return true;
                    }
                default:
                    break;
            }
            return true;
        }
    }

    private int getbright(float tx,float ty){
        int bright=100;
        double B = -1f;      //tan（）正切值
        double A = 1.5f * (float)PI;     //弧度，PI=3.14，即为180度

        double px = cardbright.getWidth()/2;   //圆心x,单位为px
        double py = cardbright.getHeight()/2;  //圆形y，单位为px

        //tvfangwei.setText("px:"+px+"py:"+py + "tx:"+tx+"ty:"+ty);
        if(((tx-px)*(tx-px)+(ty-py)*(ty-py)) < (px*2/3 * px*2/3)){   //屏蔽环内圆区域,环宽占1/3
            return -1;
        }

        if(ty==py){     //正切底数不能为0，即除数不能为0，
            A = tx >= px ? PI * 3 / 2 : PI / 2;
        }else {
            B = (px - tx) / (ty - py);
            A = atan(B);
            if(ty < py){
                A = PI +A;
            }
        }
        if(A > 0){
            double fbright = (A+0.047) / (PI * 3/2);
            bright = (int) (fbright * 100f);
        }else{
            bright=-1;      //屏蔽第四象限
        }
        return bright;
    }

    private class mClick implements View.OnClickListener{
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.imgadd:
                String sbright1=tvbright.getText().toString().trim();
                int bright1=100;
                try{
                    bright1=Integer.parseInt(sbright1);
                    if(bright1<100){
                        bright1++;
                    }
                }catch (Exception e){}
                showpoint(bright1);
                break;
            case R.id.imgdec:
                String sbright2=tvbright.getText().toString().trim();
                int bright2=100;
                try{
                    bright2=Integer.parseInt(sbright2);
                    if(bright2>1){
                        bright2--;
                    }
                }catch (Exception e){}
                showpoint(bright2);
                break;
            case R.id.imgclose:
                if(tvbright.getText().toString().equals("0")){
                    int bright=100;
                    if(type.equals("driver")){
                        try{
                            bright = Integer.parseInt(driverobj.getBrightness());
                            if(bright==0){bright=100;}
                        }catch (Exception e){}
                    }
                    showpoint(bright);
                }else{
                    showpoint(0);
                }
                break;
            case R.id.cardsend:
                String sbright = tvbright.getText().toString().trim();
                switch (type){
                    case "driver":
                        String client = driverobj.getClient();
                        String driver = driverobj.getDrivername();
                        setbright(client,driver,sbright);
                        break;
                    case "client":
                        setgroupbright(shebeiname,"0",sbright);
                        break;
                    case "group":
                        String client1=groupobj.getClient();
                        String group = groupobj.getGroupname();
                        setgroupbright(client1,group,sbright);
                        break;
                    default:
                        break;
                    }
                break;
            default:
                break;
            }
        }
    }

    private void setgroupbright(final String client,final String group,final String sbright){
        if(busy){   //判断忙
            Toast.makeText(tiaoguang.this,"网络忙碌，请稍后再试！",Toast.LENGTH_SHORT).show();
            return;
        }else{busy=true;}//置忙
        ring.setVisibility(View.VISIBLE);
        final mysocket socket = new mysocket();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    for(int a=0;a<2;a++) {
                        socket.setGroupBright(messagerom.username,client,group,sbright,messagerom.userlp);
                        do{Thread.sleep(100);}while (socket.getbusy());
                        if(socket.getpsw().equals("true")){
                            if(socket.getresmsg().equals("true")){
                                //messagerom.seveGroupbright(group,sbright);    //因为改了，亮度没更新，没意义，所以不刷新了
                                break;
                            }else{
                                socket.setpsw("数据错误");
                            }
                        }}
                }catch (Exception e){
                    socket.setpsw("网络异常");
                }
                tiaoguang.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ring.setVisibility(View.GONE);
                        String psw=socket.getpsw();
                        if(psw.equals("true")) {
                            Toast.makeText(tiaoguang.this,"发送成功",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(tiaoguang.this, psw, Toast.LENGTH_SHORT).show();
                            if(psw.equals("未登录")){
                                app.toload(tiaoguang.this);
                                busy=false;
                                return;
                            }
                        }
                        app.shownet(cardview);
                        busy=false;
                    }
                });
            }}).start();
    }

    private Boolean busy=false;
    //登录
    private void setbright(final String client,final String driver,final String sbright) {
        if(busy){   //判断忙
            Toast.makeText(tiaoguang.this,"网络忙碌，请稍后再试！",Toast.LENGTH_SHORT).show();
            return;
        }else{busy=true;}//置忙
        ring.setVisibility(View.VISIBLE);
        final mysocket socket = new mysocket();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    for(int a=0;a<2;a++) {
                        socket.setDriverBright(messagerom.username,client,driver,sbright,messagerom.userlp);
                        do{Thread.sleep(100);}while (socket.getbusy());
                        if(socket.getpsw().equals("true")){
                            if(socket.getresmsg().equals("true")){
                                driverobj.setSetbrightness(sbright);
                                messagerom.seveDriverBright(driver,sbright);
                                break;
                            }else{
                                socket.setpsw("数据错误");
                            }
                        }}
                }catch (Exception e){
                    socket.setpsw("网络异常");
                }
                tiaoguang.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ring.setVisibility(View.GONE);
                        String psw=socket.getpsw();
                        if(psw.equals("true")) {
                            Toast.makeText(tiaoguang.this,"发送成功",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(tiaoguang.this, psw, Toast.LENGTH_SHORT).show();
                            if(psw.equals("未登录")){
                                app.toload(tiaoguang.this);
                                busy=false;
                                return;
                            }
                        }
                        app.shownet(cardview);
                        busy=false;
                    }
                });
            }}).start();
    }
}
