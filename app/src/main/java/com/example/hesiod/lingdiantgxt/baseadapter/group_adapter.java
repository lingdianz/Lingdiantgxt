package com.example.hesiod.lingdiantgxt.baseadapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hesiod.lingdiantgxt.R;
import com.example.hesiod.lingdiantgxt.saoyisao;
import com.example.hesiod.lingdiantgxt.myJavaBean.ce_groups;
import com.example.hesiod.lingdiantgxt.timer;

import java.util.List;


/**
 * Created by Hesiod on 2019/11/1.
 */

public class group_adapter extends ArrayAdapter<ce_groups> {
    private int resourceId;
    private Activity myactivity;

    public group_adapter(Context context,int textid,List<ce_groups> groups,Activity myactivity){
        super(context,textid,groups);
        this.resourceId=textid;
        this.myactivity=myactivity;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        final ce_groups group = getItem(position);
        View view;
        if(convertView==null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        } else{
            view = convertView;
        }
        CardView cardView=(CardView)view.findViewById(R.id.cardview);
       // TextView tvbright=(TextView)view.findViewById(R.id.tvbright);
        TextView tvdrivername=(TextView)view.findViewById(R.id.tvgroup);
        ImageView imgtimer = (ImageView)view.findViewById(R.id.imgtimer);
        ImageView imgset = (ImageView)view.findViewById(R.id.imgset);

        tvdrivername.setText(group.getGroupname());
        //tvbright.setText(group.getBrightness1());
        if(group.getIstimer().equals("0")) {
            imgtimer.setImageResource(R.mipmap.noisedis);
        }else{
            imgtimer.setImageResource(R.mipmap.noise);
        }
        imgset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("添加新驱动");
                builder.setMessage("添加新驱动到分组“"+group.getGroupname()+"”");
                builder.setPositiveButton("前往添加", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getContext(),saoyisao.class);
                        intent.putExtra("dowhat","adddriver");
                        intent.putExtra("what",group.getClient()+":"+group.getGroupname());
                        myactivity.startActivityForResult(intent,1);
                    }});
                builder.setNeutralButton("取消",null);
                builder.show();
            }
        });
        imgtimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"设置定时器",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(),timer.class);
                intent.putExtra("type","settimer");
                intent.putExtra("shebeiname",group.getGroupname());
                myactivity.startActivityForResult(intent,1);
                myactivity.overridePendingTransition(R.anim.show,0);
            }
        });
        return view;
    }

    //点击事件回调函数
    /*private MyItemClickListener listener;
    public void setOnItemClickAddListener(MyItemClickListener listener) {
        this.listener = listener;
    }
    public interface MyItemClickListener {
        void onItemClickAdd(Intent intent);
    }*/
}
