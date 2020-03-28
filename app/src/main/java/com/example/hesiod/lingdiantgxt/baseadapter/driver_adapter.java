package com.example.hesiod.lingdiantgxt.baseadapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hesiod.lingdiantgxt.R;
import com.example.hesiod.lingdiantgxt.activity.ckdriver;
import com.example.hesiod.lingdiantgxt.myJavaBean.ce_drivers;
import com.example.hesiod.lingdiantgxt.myJavaBean.ce_groups;
import com.example.hesiod.lingdiantgxt.tiaoguang;
import com.example.hesiod.lingdiantgxt.timer;

import java.util.List;


/**
 * Created by Hesiod on 2019/11/1.
 */

public class driver_adapter extends RecyclerView.Adapter<driver_adapter.ViewHolder> {
    private Context context;
    private List<ce_drivers> driverlist;
    private List<ce_groups> groups;
    private Activity myactivity;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView tvbright;
        TextView tvdrivername;
        ImageView imgtimer;

        public ViewHolder(View view){
            super(view);
            cardView=(CardView)view;
            tvbright=(TextView)view.findViewById(R.id.tvbright);
            tvdrivername=(TextView)view.findViewById(R.id.tvdriver);
            imgtimer = (ImageView)view.findViewById(R.id.imgtimer);
        }
    }
    public driver_adapter(List<ce_drivers> driverlist,List<ce_groups> groups,Activity myactivity){
        this.driverlist=driverlist;
        this.groups=groups;
        this.myactivity=myactivity;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        if(context==null){
            context=parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.driver_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if(driverlist.size()==0){ return; }
        final ce_drivers driver = driverlist.get(position);
        holder.tvdrivername.setText(driver.getDrivername());
        holder.tvbright.setText(driver.getBrightness());
        if(driver.getIstimer()){
            holder.imgtimer.setVisibility(View.VISIBLE);
        }else{
            holder.imgtimer.setVisibility(View.GONE);
        }

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

        switch (ckdriver.ckdriverpsw(groups,driver)){
            case 1:     //正常运行
                holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.white0));
                break;
            case 21:     //数据超常，坏
                holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.red5));
                break;
            case 22:    //数据超常，坏
                holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.red5));
                break;
            case 23:    //数据超常，坏
                holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.red5));
                break;
            case 3:     //驱动丢失
                holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.gray1));
                holder.tvbright.setText("离线");
                break;
            case 32:     //电流过低,可能离线，也可能负载过轻或空载
                holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.gray1));
                break;
            case 41:     //数据异常，警告
                holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.yellow3));
                break;
            case 42:     //数据异常，警告
                holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.yellow3));
                break;
            case 43:     //数据异常，警告
                holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.yellow3));
                break;
            case 44:     //数据异常，警告
                holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.yellow3));
                break;
            case 45:     //温度异常，警告
                holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.yellow3));
                break;
            default:
                break;
        }

        holder.cardView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent;
                        if(driver.getIstimer()){
                            intent=new Intent(context,timer.class);
                            intent.putExtra("type","driver");
                            intent.putExtra("shebeiname",driver.getDrivername());
                            context.startActivity(intent);
                            myactivity.overridePendingTransition(R.anim.show,0);
                        }else{
                            intent = new Intent(context, tiaoguang.class);  //跳到手动调光
                            intent.putExtra("shebeiname",driver.getDrivername());
                            context.startActivity(intent);
                            myactivity.overridePendingTransition(R.anim.show,0);
                        }

                    }
                }
        );
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.onItemClick(driver,v);     //回调函数把值返回给context
                return true;
            }
        });
    }
/*
    public static int DRIVER_OK = 1;
    public static int DRIVER_BAD = 2;
    public static int DRIVER_LOST = 3;
    public static int DRIVER_EER = 4;
    public int ckdriverpsw(ce_drivers driver){
        float setvtg=0,setcr=0,setbn=0,nowvtg=0,nowcr=0,nowbn=0;
        try {
            setvtg = Float.parseFloat(driver.getSetvoltage());
            setcr = Float.parseFloat(driver.getSetcurrent());
            setbn = Integer.parseInt(driver.getSetbrightness());

            nowvtg = Float.parseFloat(driver.getVoltage());
            nowcr = Float.parseFloat(driver.getCurrent());
            nowbn = Integer.parseInt(driver.getBrightness());
        }catch (Exception e){
            return DRIVER_EER;
        }
        if(nowbn==104){
            return DRIVER_LOST;
        }
        if(nowbn!=setbn){
            return DRIVER_EER;
        }
        if(driver.getType().equals("0")) {
            if (nowcr > setcr * 0.9) {
                return DRIVER_BAD;
            }
            if (nowvtg > setvtg * 1.2 || nowvtg < setvtg * 0.8) {
                return DRIVER_BAD;
            }
            if (nowvtg > setvtg * 1.1 || nowvtg < setvtg * 0.9) {
                return DRIVER_EER;
            }
            if (nowcr < setcr * 0.1) {
                return DRIVER_EER;
            }
        }else if(driver.getType().equals("1")) {
            if (nowcr > setcr * 1.2 || nowcr < setcr * 0.8) {
                return DRIVER_BAD;
            }
            if (nowvtg > setvtg * 1.2 || nowvtg < setvtg * 0.4) {
                return DRIVER_BAD;
            }
            if (nowvtg > setvtg * 1.1 || nowvtg < setvtg * 0.7) {
                return DRIVER_EER;
            }
            if (nowcr > setcr * 1.1 || nowcr < setcr * 0.9) {
                return DRIVER_EER;
            }
        }

        return DRIVER_OK;
    }
*/


    @Override
    public int getItemCount(){
        return driverlist.size();
    }

    //长按事件回调函数
    private MyItemClickListener listener;
    public void setOnLongItemClickListener(MyItemClickListener listener) {
        this.listener = listener;
    }
    public interface MyItemClickListener {
        void onItemClick(ce_drivers driver,View v);
    }
}
