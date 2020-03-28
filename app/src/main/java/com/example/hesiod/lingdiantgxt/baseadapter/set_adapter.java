package com.example.hesiod.lingdiantgxt.baseadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.hesiod.lingdiantgxt.R;


/**
 * Created by Hesiod on 2019/11/1.
 */

public class set_adapter extends ArrayAdapter<String> {
    private int resourceId;
    private TextView tvset;
    private View v5dp;

    public set_adapter(Context context,int textid,String[] groups){
        super(context,textid,groups);
        this.resourceId=textid;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        final String setstr = getItem(position);
        View view;
        if(convertView==null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        } else{
            view = convertView;
        }
        tvset = (TextView)view.findViewById(R.id.tvset);
        tvset.setText(setstr);
        if(setstr.contains("删除")){
            tvset.setTextColor(getContext().getResources().getColor(R.color.red3));
        }else{
            tvset.setTextColor(getContext().getResources().getColor(R.color.black));
        }
        return view;
    }
}