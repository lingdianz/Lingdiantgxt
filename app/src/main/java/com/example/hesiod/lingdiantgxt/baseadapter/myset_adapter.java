package com.example.hesiod.lingdiantgxt.baseadapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hesiod.lingdiantgxt.R;

import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hesiod.lingdiantgxt.R;
import com.example.hesiod.lingdiantgxt.baseadapter.Msg;

import java.util.List;

/**
 * Created by Hesiod on 2019/9/15.
 */

public class myset_adapter extends RecyclerView.Adapter<myset_adapter.ViewHolder>{

    private List<myset> mysetlist;
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView setimg1;
        TextView setname2;
        TextView setnews3;
        ImageView imgnews4;
        TextView setgo5;
        Intent intent;
        View setview;

        public ViewHolder(View view){
            super(view);
            setview=view;
            setimg1=(ImageView) view.findViewById(R.id.imgphoto1);
            setname2=(TextView)view.findViewById(R.id.tvname2);
            setnews3=(TextView)view.findViewById(R.id.tvnews3);
            imgnews4=(ImageView) view.findViewById(R.id.imgnews4);
            setgo5=(TextView)view.findViewById(R.id.tvgo5);
        }
    }
    public myset_adapter(List<myset> mysetlist){
        this.mysetlist=mysetlist;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        myset msg=mysetlist.get(position);
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
    }
    @Override
    public int getItemCount() {
        return mysetlist.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.mysetmune,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.setview.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int position=holder.getAdapterPosition();
               myset set=mysetlist.get(position);
                Intent intent=set.getintent();
                v.getContext().startActivity(intent);
            }
        });
        return holder;
    }
}
