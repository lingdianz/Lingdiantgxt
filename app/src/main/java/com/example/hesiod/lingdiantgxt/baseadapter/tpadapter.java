package com.example.hesiod.lingdiantgxt.baseadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hesiod.lingdiantgxt.R;
import com.example.hesiod.lingdiantgxt.myJavaBean.myBitmap;

import java.util.List;

/**
 * Created by Hesiod on 2019/12/20.
 */

public class tpadapter extends ArrayAdapter<myBitmap> {
    private int resourceId;
    public tpadapter(Context context, int tvid, List<myBitmap> objects){
        super(context,tvid,objects);
        resourceId=tvid;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        myBitmap bitmap = getItem(position);
        View view;
        tpadapter.ViewHolder holder;
        if(convertView==null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            holder = new ViewHolder();
            holder.tvtitle = (TextView)view.findViewById(R.id.tvtitle);
            holder.tvsummary = (TextView)view.findViewById(R.id.tvsummary);
            holder.imageView = (ImageView)view.findViewById(R.id.imageview);
            view.setTag(holder);
        }else{
            view=convertView;
            holder=(ViewHolder)view.getTag();
        }
        holder.tvtitle.setText(bitmap.getTitle());
        holder.tvsummary.setText(bitmap.getSummary());
        holder.imageView.setImageBitmap(bitmap.getBitmap());
        return view;
    }
    class ViewHolder{
        TextView tvtitle;
        TextView tvsummary;
        ImageView imageView;
    }
}
