package com.example.hesiod.lingdiantgxt;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hesiod.lingdiantgxt.activity.BaseAcvtivity;
import com.example.hesiod.lingdiantgxt.activity.messagerom;
import com.example.hesiod.lingdiantgxt.baseadapter.mylist_adapter;
import com.example.hesiod.lingdiantgxt.baseadapter.myset;
import com.example.hesiod.lingdiantgxt.fragment_java.myuser3;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hesiod on 2019/9/12.
 */

public class usermessage extends BaseAcvtivity {
    private Button btnback;
    private ListView lvmyset;

    private List<myset> mysetlist =new ArrayList<>();

    private link app;
    private messagerom messagerom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usermessage);
        app=(link)getApplication();
        messagerom=app.getmessagerom();
    }
    @Override
    protected void onStart(){
        super.onStart();
        initview();
    }

    private void initview(){
        btnback=(Button)findViewById(R.id.btnreturn);
        btnback.setOnClickListener(new mClick());
        lvmyset =(ListView)findViewById(R.id.lvusermessage);

        initlist();
        mylist_adapter adapter=new mylist_adapter(usermessage.this,R.layout.mysetmune,mysetlist);
        lvmyset.setAdapter(adapter);
        lvmyset.setOnItemClickListener(new iClick());
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

    public class iClick implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            final myset msg= mysetlist.get(position);
                    if(msg.gettype()==myset.TYPE_SHOW){return;}

            final EditText input = new EditText(usermessage.this);
            input.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
            input.setHint("password");
            input.setGravity(1);
            input.setBackgroundResource(R.drawable.yuajiaotxqian);
            input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
            final String choose=msg.getstrsetname();

                AlertDialog.Builder builder= new AlertDialog.Builder(usermessage.this);
                builder.setTitle("更改"+choose);
                    builder.setMessage("需要权限，请输入验证密码");
                    builder.setView(input);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String inputstr=input.getText().toString();
                        String psw=messagerom.getPassword();
                        if(inputstr.isEmpty()){return;}
                        if(!inputstr.equals(psw)){
                            Toast.makeText(usermessage.this,"验证密码错误",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(choose.equals("密码")){
                            Intent intent=new Intent(usermessage.this,setpassword.class);
                            intent.putExtra("news","密码");//将data信息用putExtra（）存进intent
                            intent.putExtra("max","16");//将data信息用putExtra（）存进intent
                            startActivity(intent);
                        }else{
                            //密码核对正确跳转
                            Intent intent=msg.getintent();
                            if(intent==null){return;}
                            startActivity(intent);
                        }

                    }
                });
                builder.setNegativeButton("取消",null);
                builder.show();

        }
    }

    private void initlist() {
        String name1=messagerom.getName();
        String username1=messagerom.getUsername();
        if(!mysetlist.isEmpty()){mysetlist.clear();}
        Intent intent;
        intent=new Intent(usermessage.this,setmessage.class);
        intent.putExtra("news","用户名");
        intent.putExtra("max","16");
        myset name = new myset(R.mipmap.logo, "用户名", name1, myset.TYPE_MENU, intent);
        mysetlist.add(name);
        intent = null;
        myset zhanghao = new myset(R.mipmap.logo, "账号", username1, myset.TYPE_SHOW, intent);
        mysetlist.add(zhanghao);
        intent=new Intent(usermessage.this,setmessage.class);
        intent.putExtra("news","密码");//将data信息用putExtra（）存进intent
        intent.putExtra("max","16");//将data信息用putExtra（）存进intent
        myset mima = new myset(R.mipmap.logo, "密码", "", myset.TYPE_MENU, intent);
        mysetlist.add(mima);
    }
}
