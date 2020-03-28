package com.example.hesiod.lingdiantgxt.fragment_java;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.example.hesiod.lingdiantgxt.R;
import com.example.hesiod.lingdiantgxt.appset;
import com.example.hesiod.lingdiantgxt.baseadapter.mylist_adapter;
import com.example.hesiod.lingdiantgxt.baseadapter.myset;
import com.example.hesiod.lingdiantgxt.control_room;
import com.example.hesiod.lingdiantgxt.kefu;
import com.example.hesiod.lingdiantgxt.link;
import com.example.hesiod.lingdiantgxt.activity.messagerom;
import com.example.hesiod.lingdiantgxt.usermessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dm on 16-3-29.
 * 第一个页面
 */
public class news2 extends Fragment {
    private View view;
    private ListView lvnews;
    private List<myset> mynewslist = new ArrayList<>();

    private Activity myactivity;
    private Context context;
    private link app;
    private messagerom messagerom;


    private LocationClient mylocationclient = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news2, container, false); // 先解析file.xml布局，得到一个view
        myactivity=getActivity();
        context = getContext();
        app = (link)myactivity.getApplication();
        messagerom = app.getmessagerom();
        initview();
        return view;
    }

    private void initview(){
        lvnews = (ListView)view.findViewById(R.id.lvnews);

        mylist_adapter adapter=new mylist_adapter(context,R.layout.mysetmune,mynewslist);
        lvnews.setAdapter(adapter);
        lvnews.setOnItemClickListener(new mItemClick());

        initlist();
    }

    private void initlist(){
        Intent intent;
        for(int ct = 0;ct < messagerom.getClients().size();ct++)
        {
            String clientname = messagerom.getClients().get(ct).getClientname();
            if(!messagerom.getOnlineclient().contains(clientname))
            {
                app.setRbnew2(1);
                intent=new Intent(getContext(),control_room.class);
                intent.putExtra("shebeiname",clientname);
                myset list1=new myset(R.mipmap.dengpao0,clientname,"离线",myset.TYPE_NEWS,intent);
                mynewslist.add(list1);
            }
        }
    }

    private class mItemClick implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
            myset msg= mynewslist.get(position);
            Intent intent=msg.getintent();
            if(intent==null){return;}
            startActivity(intent);
        }
    }
}