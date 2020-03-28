package com.example.hesiod.lingdiantgxt.baseadapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hesiod.lingdiantgxt.R;

import java.util.List;

/**
 * Created by Hesiod on 2019/9/18.
 */

public class mylist_adapter extends ArrayAdapter<myset>{

    private int resourceId;

    public mylist_adapter(Context context,int tvid, List<myset> objects){
        super(context,tvid,objects);
        resourceId=tvid;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        myset msg = getItem(position);
        View view;
        ViewHolder holder;
        if(convertView==null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            holder = new ViewHolder();
            holder.setimg1=(ImageView)view.findViewById(R.id.imgphoto1);
            holder.setname2=(TextView)view.findViewById(R.id.tvname2);
            holder.setnews3=(TextView)view.findViewById(R.id.tvnews3);
            holder.imgnews4=(ImageView)view.findViewById(R.id.imgnews4);
            holder.setgo5=(TextView)view.findViewById(R.id.tvgo5);
            view.setTag(holder);
        }else{
            view=convertView;
            holder=(ViewHolder)view.getTag();
        }
        if(msg.gettype()==myset.TYPE_SHOW){
            holder.imgnews4.setVisibility(View.GONE);
            holder.setgo5.setVisibility(View.GONE);
            holder.setimg1.setImageResource(msg.getimg());
            holder.setname2.setText(msg.getstrsetname());
            holder.setnews3.setText(msg.getstrnews());
        }else if(msg.gettype()==myset.TYPE_MENU){
            holder.imgnews4.setVisibility(View.GONE);
            holder.setgo5.setVisibility(View.VISIBLE);
            holder.setimg1.setImageResource(msg.getimg());
            holder.setname2.setText(msg.getstrsetname());
            holder.setnews3.setText(msg.getstrnews());
        }else if(msg.gettype()==myset.TYPE_NEWS) {
            holder.imgnews4.setVisibility(View.VISIBLE);
            holder.setgo5.setVisibility(View.VISIBLE);
            holder.setimg1.setImageResource(msg.getimg());
            holder.setname2.setText(msg.getstrsetname());
            holder.setnews3.setText(msg.getstrnews());
        }
        return view;
    }
    class ViewHolder{
        ImageView setimg1;
        TextView setname2;
        TextView setnews3;
        ImageView imgnews4;
        TextView setgo5;
    }
}
