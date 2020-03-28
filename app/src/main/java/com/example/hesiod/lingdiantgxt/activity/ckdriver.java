package com.example.hesiod.lingdiantgxt.activity;

import android.annotation.SuppressLint;

import com.example.hesiod.lingdiantgxt.myJavaBean.ce_drivers;
import com.example.hesiod.lingdiantgxt.myJavaBean.ce_groups;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Hesiod on 2019/11/11.
 */

public class ckdriver {

    public static int DRIVER_OK = 1;//通过
    public static int DRIVER_U_BAD = 21;//电压坏
    public static int DRIVER_I_BAD = 22;//电流坏
    public static int DRIVER_C_BAD = 23;//温度坏
    public static int DRIVER_LOST = 3;//离线
    public static int DRIVER_I_LOST = 32;//电流过低
    public static int DRIVER_U_EER = 41;//电压警报
    public static int DRIVER_I_EER = 42;//电流警报
    public static int DRIVER_G_EER = 43;//亮度警报
    public static int DRIVER_D_EER = 44;//数据错误警报
    public static int DRIVER_C_EER = 45;//温度警报
    public static int ckdriverpsw(List<ce_groups> groups, ce_drivers driver) {
        float setvtg = 0, setcr = 0, setbn = 0, setwd = 0, nowvtg = 0, nowcr = 0, nowbn = 0, nowwd = 0;
        ce_groups group = new ce_groups();
        if (driver.getIstimer()) {        //如果在定时模式
            for (int ct = 0; ct < groups.size(); ct++) {
                if (driver.getGroupname().equals(groups.get(ct).getGroupname())) {
                    group = groups.get(ct);
                    break;
                }
            }
            if (group == null || group.getIstimer().equals("0")) {          //在数据异常在手动调节模式
                setbn = Integer.parseInt(driver.getSetbrightness());
            } else {
                setbn = timebright(group);      //定时模式取样
                if (setbn == -1) {      //如果取样失败
                    setbn = Integer.parseInt(driver.getSetbrightness());
                }
            }
        } else {             //在手动调节模式
            setbn = Integer.parseInt(driver.getSetbrightness());
        }
        try {
            setvtg = Float.parseFloat(driver.getSetvoltage());
            setcr = Float.parseFloat(driver.getSetcurrent());
            setwd = 100;

            nowvtg = Float.parseFloat(driver.getVoltage());
            nowcr = Float.parseFloat(driver.getCurrent());
            nowbn = Integer.parseInt(driver.getBrightness());
            nowwd = Integer.parseInt(driver.getCelcius());
        } catch (Exception e) {
            return DRIVER_D_EER;    //如果数据错误，则所有数据将不可参考，等级1
        }
        if (nowbn == 104) {
            return DRIVER_LOST;     //如果被控制器找不到，则认为离线，其他数据也不可参考，等级2
        }
        if (driver.getType().equals("0")) {      //电压值恒定机型，取样
            if (nowvtg > setvtg * 1.2 || nowvtg < setvtg * 0.8) {       //恒压的驱动以电压为先参考，超范围即为坏，等级3
                return DRIVER_U_BAD;
            }
            if (nowcr > setcr * 1) {        //电流超出范围即视为非法使用，超范围即为坏，等级3
                return DRIVER_I_BAD;
            }
            if (nowwd > setwd * 1.1) {        //温度超出范围即视为，超范围即为坏，等级3
                return DRIVER_C_BAD;
            }
            if (nowvtg > setvtg * 1.1 || nowvtg < setvtg * 0.9) {   //电压偏离一点，可能为误差，警示4
                return DRIVER_U_EER;
            }
            if (nowcr < setcr * 0.1) {          //电流超低，可能为脱线，也可能被设置为关，都为灰色警示，客户自行可以判断4
                return DRIVER_I_LOST;
            }
            if (nowcr > setcr * 0.9) {            //电流大于安全使用范围，警示提示4
                return DRIVER_I_EER;
            }
        } else {
            if (nowvtg > setvtg * 1.3 || nowvtg < setvtg * 0.4) {       //非恒压的驱动以电压为先参考，范围较宽，超范围即为坏，等级3
                return DRIVER_U_BAD;
            }
            if (nowcr > setcr * 1.1) {        //电流超出范围即视为非法使用，超范围即为坏，等级3
                return DRIVER_I_BAD;
            }
            if (nowwd > setwd * 1.1) {        //温度超出范围即视为，超范围即为坏，等级3
                return DRIVER_C_BAD;
            }
            if (nowvtg > setvtg * 1.1 || nowvtg < setvtg * 0.5) {   //非恒压设备电压电压偏离可以大点，可能为误差，警示4
                return DRIVER_U_EER;
            }
            if (nowcr < setcr * 0.1) {          //电流超低，可能为脱线，也可能被设置为关，都为灰色警示，客户自行可以判断4
                return DRIVER_I_LOST;
            }
        }
        if (nowwd > setwd * 1) {                  //温度警报，等级4
            return DRIVER_C_EER;
        }

        if (nowbn != setbn) {                       //比较亮度是否相等，等级4
            return DRIVER_G_EER;
        }

        return DRIVER_OK;
    }

    //获取当前时间定时器亮度
    public static int timebright(ce_groups group){
        if(group.getIstimer().equals("0")){
            return -1;
        }
        try{
            int timenow = getnowtime();         //时间格式一天为（1-1440）分
            if(timenow < 1 || 1440 < timenow){ return -1;}
            int t1 = Integer.parseInt(group.getTime1());  //时间格式一天为（1-1440）分
            int t2 = Integer.parseInt(group.getTime2());
            int t3 = Integer.parseInt(group.getTime3());
            int t4 = Integer.parseInt(group.getTime4());
            int b1 = Integer.parseInt(group.getBrightness1());
            int b2 = Integer.parseInt(group.getBrightness2());
            int b3 = Integer.parseInt(group.getBrightness3());
            int b4 = Integer.parseInt(group.getBrightness4());

            int couttime=counttime(t1,t2,t3,t4);
            if(couttime<2){
                return -1;
            }else{
                int timebright=-1;
                int tmax = 0;
                if(tmax<t1){ tmax = t1; timebright=b1; }
                if(tmax<t2){ tmax = t2;  timebright=b2; }
                if(tmax<t3){ tmax = t3;  timebright=b3; }
                if(tmax<t4){ tmax = t4;  timebright=b4; }     //先取最大值，当timenow在以下没有得到值时，其为最小，取t最大值

                int timebj=1;
                if(t1!=0 && timenow >= t1 && timebj <= t1 ){  timebj = t1; timebright = b1; }
                if(t2!=0 && timenow >= t2 && timebj <= t2 ){  timebj = t2; timebright = b2; }
                if(t3!=0 && timenow >= t3 && timebj <= t3 ){  timebj = t3; timebright = b3; }
                if(t3!=0 && timenow >= t4 && timebj <= t4 ){  timebj = t4; timebright = b4; }
                return timebright;
            }
        }catch (Exception e){
            return -1;
        }
    }

    //正确的定时时间为（1-1440）,此外都被认为定时器关闭,一般为0
    private static int counttime(int t1,int t2,int t3,int t4){  //计数有几个有效定时器
        int counttime=0;
        if(1<= t1 && t1<=1440){counttime++;}
        if(1<= t2 && t2<=1440){counttime++;}
        if(1<= t3 && t3<=1440){counttime++;}
        if(1<= t4 && t4<=1440){counttime++;}
        return counttime;
    }

    //获取系统时间并转为1分到1440分，即00:01分到24:00，(1-1440)范围
    private static int getnowtime(){
        try {
            String stime = getNowTime();
            String[] cuttime = stime.split(":");
            int shi = Integer.parseInt(cuttime[0]);
            int fen = Integer.parseInt(cuttime[1]);
            return shi * 60 + fen +1 ;
        }catch (Exception e){
            return -1;
        }
    }
    //获取当前系统时间，时分秒
    @SuppressLint("SimpleDateFormat")
    public static String getNowTime() {
        SimpleDateFormat s_format = new SimpleDateFormat("HH:mm:ss");
        return s_format.format(new Date());
    }

}
