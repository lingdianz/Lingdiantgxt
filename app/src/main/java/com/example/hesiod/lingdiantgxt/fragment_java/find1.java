package com.example.hesiod.lingdiantgxt.fragment_java;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.hesiod.lingdiantgxt.R;
import com.example.hesiod.lingdiantgxt.activity.messagerom;
import com.example.hesiod.lingdiantgxt.activity.myHttpuilt;
import com.example.hesiod.lingdiantgxt.link;
import com.example.hesiod.lingdiantgxt.myJavaBean.TestBean;
import com.example.hesiod.lingdiantgxt.webshow;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by dm on 16-3-29.
 * 第一个页面
 */
public class find1 extends Fragment {
    private link app;
    private messagerom messagerom;
    View view;
    Activity activity;
    Button btntest,btngo;
    TextView tvtest;
    ImageView imgtest;
    ProgressBar ring;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_find1, container, false);
        activity = getActivity();
        app=(link)activity.getApplication();
        messagerom=app.getmessagerom();
        initview();
        return view;
    }
    private void initview(){
        btntest = (Button)view.findViewById(R.id.btntest);
        btngo = (Button)view.findViewById(R.id.btngo);
        tvtest = (TextView) view.findViewById(R.id.tvtest);
        imgtest = (ImageView) view.findViewById(R.id.imgtest);
        ring =(ProgressBar)view.findViewById(R.id.ring);
        tvtest.setMovementMethod(ScrollingMovementMethod.getInstance());
        btntest.setOnClickListener(new mClick());
        btngo.setOnClickListener(new mClick());
    }

    public class mClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btntest:
                    if(ring.getVisibility()==View.VISIBLE){
                        return;
                    }else{
                        ring.setVisibility(View.VISIBLE);
                        String urltq="https://free-api.heweather.net/s6/weather/now?key="+messagerom.weatherKEY+"&location=北京";
                        String urlqd = "https://www.kuaidi100.com/query?type=yunda&postid=4301155281141";
                        String urltb = "https://suggest.taobao.com/sug?code=utf-8&q=电脑&callback=cb";
                        getTB(urlqd);
                    }
                    break;
                case R.id.btngo:
                    Intent intent = new Intent(getContext(), webshow.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    }

    private void getTB(String url){
        new myHttpuilt().getWebData(url, myHttpuilt.TYPE_JSON, new myHttpuilt.CallBack() {
            @Override
            public void OKres(myHttpuilt.uiltData uiltdata) {
                ring.setVisibility(View.GONE);
                tvtest.setText(JxjsonQD(uiltdata.resmsg));
            }
            @Override
            public void ERRres(myHttpuilt.uiltData uiltdata) {
                ring.setVisibility(View.GONE);
                tvtest.setText(JxjsonQD(uiltdata.errmsg));
            }
        });
    }

    private String JxjsonQD(String jmsg){
        Gson gson = new Gson();
        StringBuilder sbmsg = new StringBuilder("");
        TestBean.TestQD testQD = gson.fromJson(jmsg,TestBean.TestQD.class);
        sbmsg.append("message:").append(testQD.getMessage()).append("\r\n");
        sbmsg.append("运单号:").append(testQD.getNu()).append("\r\n");
        sbmsg.append("查询：").append(testQD.getIscheck()).append("\r\n");
        sbmsg.append("status:").append(testQD.getStatus()).append("\r\n");
        sbmsg.append("conditon:").append(testQD.getCondition()).append("\r\n");
        sbmsg.append("state:").append(testQD.getState()).append("\r\n");
        List<TestBean.TestQD.Data> dataList = testQD.getData();
        for(int at = 0;at<dataList.size();at++){
            sbmsg.append(at+"、");
            sbmsg.append("开始时间：").append(dataList.get(at).getTime()).append("\r\n");
            sbmsg.append("状态：").append(dataList.get(at).getContext()).append("\r\n");
            sbmsg.append("最后时间：").append(dataList.get(at).getFtime()).append("\r\n\n");
        }
        return sbmsg.toString();
    }

    private String JxjsonTB(String jmsg) {
        return jmsg;
    }
}