package com.example.hesiod.lingdiantgxt;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.hesiod.lingdiantgxt.activity.BaseAcvtivity;

public class kefu extends BaseAcvtivity {

    private Button btnreturn;
    private ListView lvkflistview;

    private link app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kefu);
        app=(link)getApplication();
        initview();
        lisentlistview();
    }
    private void initview(){
        btnreturn=(Button)findViewById(R.id.btnreturn);
        btnreturn.setOnClickListener(new mClick());
        lvkflistview = (ListView) findViewById(R.id.lvkefu);
        setlistview();
    }

    private void lisentlistview(){
        lvkflistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String kfname = lvkflistview.getItemAtPosition(position).toString();
                gotalk(kfname,"...在线");
        }
    });
        lvkflistview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view , final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(kefu.this);
                builder.setMessage("点击确定联系客服");
                builder.setTitle("提示");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //取消则什么都没做
                    }});
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        String kfname = lvkflistview.getItemAtPosition(position).toString();
                        gotalk(kfname,"...在线");
                            }
                        });
                builder.create().show();
                return true;
                }
        });
    }
    private void gotalk(String kefuname ,String line){
        Intent intent = new Intent(kefu.this, talk.class);
        intent.putExtra("kefu",kefuname);
        intent.putExtra("online",line);
        startActivity(intent);
    }
    public class mClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnreturn:
                    finish();
                    break;
                default:
                    break;
            }
        }
    }

    private void setlistview(){
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(kefu.this,android.R.layout.simple_list_item_1,app.getkflist());
        lvkflistview.setAdapter(adapter);
    }
}