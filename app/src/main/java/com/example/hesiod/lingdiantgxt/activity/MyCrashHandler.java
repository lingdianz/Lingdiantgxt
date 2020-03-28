package com.example.hesiod.lingdiantgxt.activity;

/**
 * Created by Hesiod on 2019/12/21.
 */

import android.app.Application;
import android.content.Context;
import android.os.Looper;
import android.os.SystemClock;
import android.widget.Toast;

import com.example.hesiod.lingdiantgxt.link;

/**
 * 程序奔溃处理类
 * UncaughtExceptionHandler
 * @author myCrashHandler
 */
public class MyCrashHandler implements Thread.UncaughtExceptionHandler {
    private Application application;
    private Thread.UncaughtExceptionHandler myCrashThread;  //系统默认的UncaughtException处理类
    private static MyCrashHandler INSTANCE = new MyCrashHandler();
    private MyCrashHandler() {}
    /** 获取CrashHandler实例 ,单例模式 */
    public static MyCrashHandler getInstance() { return INSTANCE; }
    /**
     * 1、调用则初始化该处理器，开始监听程序奔溃事件
     * @param application Application
     */
    public void InitMyCrashHandler(Application application){
        this.application = application;
        myCrashThread = Thread.getDefaultUncaughtExceptionHandler();    //获取系统默认的UncaughtException处理器
        Thread.setDefaultUncaughtExceptionHandler(INSTANCE);    //设置该CrashThread类为程序的默认处理器
    }
    /**
     * 2、监听处，程序奔溃则触发以下函数
     * @param thread 线程
     * @param throwable 异常数据
     */
    @Override
    public void uncaughtException(Thread thread, final Throwable throwable) {
        /* if (!handleException(throwable) && myCrashThread != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            myCrashThread.uncaughtException(thread, throwable);
        }else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                //Log.e(TAG, "error : ", e);
            }*/
        handleException(throwable);

        //Toast.makeText(context, "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_LONG).show();
            //退出程序
        SystemClock.sleep(6000);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        //}
    }
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        //使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                try {
                    Looper.prepare();
                    Toast.makeText(application, "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_SHORT).show();
                    //Thread.sleep(3000);
                    Looper.loop();
                }catch (Exception e){}
            }
        }.start();
        //收集设备参数信息
        //collectDeviceInfo(mContext);
        //保存日志文件
        //saveCrashInfo2File(ex);
        return true;
    }
}
