package com.example.hesiod.lingdiantgxt.baseadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.hesiod.lingdiantgxt.R;

import java.util.List;


/**
 * Created by Hesiod on 2019/11/1.
 */

public class work_adapter extends ArrayAdapter<String> {
    private int resourceId;

    public work_adapter(Context context,int textid,List<String> groups){
        super(context,textid,groups);
        this.resourceId=textid;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        final String group = getItem(position);
        View view;
        if(convertView==null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        } else{
            view = convertView;
        }
        TextView tvdrivername=(TextView) view.findViewById(R.id.tvwork);
        tvdrivername.setText(group);
        return view;
    }
}
