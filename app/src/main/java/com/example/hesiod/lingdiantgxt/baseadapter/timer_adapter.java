package com.example.hesiod.lingdiantgxt.baseadapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.hesiod.lingdiantgxt.R;
import com.example.hesiod.lingdiantgxt.timer;

import java.util.List;


/**
 * Created by Hesiod on 2019/11/1.
 */

public class timer_adapter extends ArrayAdapter<timer.timerobj> {
    private int resourceId;
    private Activity myactivity;
    private TextView tvname;
    private CheckBox cbtime;;
    private TextView tvshi;
    private TextView tvfen;
    private TextView tvbright;
    private TextView tvdian;
    private int type;

    public timer_adapter(Context context,int textid,List<timer.timerobj> groups,Activity myactivity,int type){
        super(context,textid,groups);
        this.resourceId=textid;
        this.myactivity=myactivity;
        this.type = type;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        final timer.timerobj time = getItem(position);
        View view;
        if(convertView==null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        } else{
            view = convertView;
        }
        tvname = (TextView)view.findViewById(R.id.tvname);
        cbtime = (CheckBox)view.findViewById(R.id.cbtime);
        tvshi = (TextView)view.findViewById(R.id.tvshi);
        tvfen = (TextView)view.findViewById(R.id.tvfen);
        tvbright = (TextView)view.findViewById(R.id.tvbright);
        tvdian = (TextView)view.findViewById(R.id.tvdian);

        final int name = time.name;
        Boolean cb = time.istimer;
        final int shi = time.shi;
        final int fen = time.fen;
        final int bright = time.bright;

        if(type == myset.TYPE_SHOW){
            if(cb){
                tvname.setText(""+(name+1)+"、开");
                tvname.setTextColor(myactivity.getResources().getColor(R.color.blue5));
                tvshi.setBackgroundResource(R.color.blue3);
                tvfen.setBackgroundResource(R.color.blue3);
                tvbright.setBackgroundResource(R.color.blue3);
                tvdian.setBackgroundResource(R.color.blue3);
            }else{
                tvname.setText(""+(name+1)+"、关");
                tvname.setTextColor(myactivity.getResources().getColor(R.color.gray));
                tvshi.setBackgroundResource(R.color.gray);
                tvfen.setBackgroundResource(R.color.gray);
                tvbright.setBackgroundResource(R.color.gray);
                tvdian.setBackgroundResource(R.color.gray);
            }
            cbtime.setVisibility(View.GONE);
        }else{
            tvname.setText(""+(name+1)+"、");
            cbtime.setChecked(cb);
            if(cb){
                cbtime.setText("开");
                tvshi.setBackgroundResource(R.color.blue3);
                tvfen.setBackgroundResource(R.color.blue3);
                tvbright.setBackgroundResource(R.color.blue3);
                tvdian.setBackgroundResource(R.color.blue3);
            }else{
                cbtime.setText("关");
                tvshi.setBackgroundResource(R.color.gray);
                tvfen.setBackgroundResource(R.color.gray);
                tvbright.setBackgroundResource(R.color.gray);
                tvdian.setBackgroundResource(R.color.gray);
            }
        }
        tvshi.setText(strtwo(shi));
        tvfen.setText(strtwo(fen));
        tvbright.setText(""+bright);
        if(type != myset.TYPE_SHOW){
            cbtime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemChange(name);
                }
            });
            tvshi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {listener.onItemClick(v,name,0,shi);
                }
            });
            tvfen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {listener.onItemClick(v,name,1,fen);
                }
            });
            tvbright.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {listener.onItemClick(v,name,2,bright);
                }
            });
        }

        return view;
    }

    private String strtwo(int i){
        String str = ""+i;
        if(str.length()==1){
            return "0" + str;
        } else{
            return str;
        }
    }

    //点击事件回调函数
    private OnItemClickListener listener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    public interface OnItemClickListener {
        void onItemClick(View view,int name,int vid,int sele);
        void onItemChange(int name);
    }

}
