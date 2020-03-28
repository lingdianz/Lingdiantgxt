package com.example.hesiod.lingdiantgxt;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.Snackbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.example.hesiod.lingdiantgxt.activity.MyCrashHandler;
import com.example.hesiod.lingdiantgxt.activity.messagerom;
import com.example.hesiod.lingdiantgxt.baseadapter.ActivityCollector;
import com.example.hesiod.lingdiantgxt.baseadapter.myset;

//import org.litepal.LitePal;

import java.io.File;

import interfaces.heweather.com.interfacesmodule.view.HeConfig;

public class link extends Application {

    private  boolean ckload=false;
    private messagerom messagerom;
    private String[] kflist={"客服a01","客服a02","客服a03","客服a04","客服a05","客服a06",
            "技术b01","技术b02","技术b03","技术b04"};

    private Handler handler = new Handler(Looper.getMainLooper());

    private static Context context;
    private Boolean cknet=true;
    private int rbnew0 = 0,rbnew1 = 0,rbnew2 = 0,rbnew3 = 0;    //首页的消息提示红点
    private String newvesion="";
    private int newapp= myset.TYPE_MENU;
    private float density=1;

    //下面两方法是配置数据库的Litepal全局context;
    @Override
    public void onCreate(){
        Application application = this;
        context=getApplicationContext();
        //LitePal.initialize(context);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);

        //和风天气,Android SDK key:
        HeConfig.init("HE1911201332221225", "e91230221cd04c03b45987c5311d2621");
        HeConfig.switchToFreeServerNode();

        MyCrashHandler myCrashHandler = MyCrashHandler.getInstance();
        myCrashHandler.InitMyCrashHandler(application);
    }

    public float getDensity(){return density;}
    public void setDensity(float density){this.density=density;}

    public int getRbnew0(){return rbnew0;}
    public int getRbnew1(){return rbnew1;}
    public int getRbnew2(){return rbnew2;}
    public int getRbnew3(){return rbnew3;}
    public void setRbnew0(int rbnew){this.rbnew0 = rbnew;}
    public void setRbnew1(int rbnew){this.rbnew1 = rbnew;}
    public void setRbnew2(int rbnew){this.rbnew2 = rbnew;}  //消息
    public void setRbnew3(int rbnew){this.rbnew3 = rbnew;}
    //get方法
    public static Context getContext(){
        return context;
    }
    public messagerom getmessagerom(){return this.messagerom;}
    public boolean getckload (){return ckload;}
    public String[] getkflist(){return kflist;}
    public boolean getcknet (){return cknet;}
    public void setcknet(Boolean cknet){this.cknet=cknet;}
    private int cout=0;
    public int getcout(){return cout;}
    public void setcout(int cout){this.cout=cout;}
    public String getNewvesion(){return newvesion;}
    public void setNewvesion(String newvesion){this.newvesion = newvesion;}
    public int getNewapp(){return newapp;}
    public void setNewapp(int newapp){this.newapp = newapp;}
    // set方法
    public void delmessagerom(){this.messagerom=null;}
    public void setmessagerom(){this.messagerom=new messagerom(context);}
    public Handler getHandler(){return handler;}
    public void setHandler(Handler handler) {
        if (this.handler != null) {
            this.handler = null;
        }
        this.handler = handler;
    }


    //handler到消息小红点显示
    private void handlertonews(){

    }

    //获取版本号
    public String getVersion() {
        PackageManager manager = context.getPackageManager();
        String version; int code = 1;String name = "1.0";
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            code = info.versionCode;
            name = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        version = code+"."+name;
        return version;
    }


    //跳转登录
    public void toload(Context context){
        cleanInternalCache(context);//清除缓存
        ActivityCollector.finishAll();
        Intent intent=new Intent(context,load.class);
        context.startActivity(intent);
    }
    //跳转登录
    public void toload1(Context context){
        cleanInternalCache(context);//清除缓存
        ActivityCollector.finishAll();
        Intent intent=new Intent(context,load.class);
        context.startActivity(intent);
    }
    //退出程序
    public void exitapp(Context context){
        cleanInternalCache(context);//清除缓存
        ActivityCollector.finishAll();//退出所有活动
    }
    /** * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache) * * @param context */
    public static void cleanInternalCache(Context context) {
        deleteFilesByDirectory(context,context.getCacheDir());
    }
    /** * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 * * @param directory */
    private static void deleteFilesByDirectory(Context context , File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }

    public void shownet(View view){
        if(getcknet()){return;}
        Snackbar.make(view,"老板，您网络开小差咯！",Snackbar.LENGTH_LONG).setAction("网络设置",new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(context,"请打开网络连接！",Toast.LENGTH_LONG).show();
            }
        }).show();
    }

    public void getdensity(){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        //int width = dm.widthPixels;         // 屏幕宽度（像素）
        //int height = dm.heightPixels;       // 屏幕高度（像素）
        float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
        //int densityDpi = dm.densityDpi;     // 屏幕密度dpi（120 / 160 / 240）
        setDensity(density);
    }
}

