package com.example.hesiod.lingdiantgxt.baseadapter;

import android.content.Intent;
import android.media.Image;
import android.widget.ImageView;

/**
 * Created by Hesiod on 2019/9/15.
 */

public class myset {

    public static final int TYPE_SHOW=0;//显示，>不显示，点击无跳转
    public static final int TYPE_MENU=1;//> 按钮显示
    public static final int TYPE_NEWS=2;//显示更新小红点
    private String strsetname,strnews;
    private int img;
    private int type;
    private Intent intent;
    public myset(int img, String strsetname,String strnews, int type,Intent intent){
        this.img=img;
        this.strsetname=strsetname;
        this.strnews=strnews;
        this.type=type;
        this.intent=intent;
    }
    public int getimg(){
        return img;
    }
    public String getstrsetname(){
        return strsetname;
    }
    public String getstrnews(){
        return strnews;
    }
    public int gettype(){
        return type;
    }
    public Intent getintent(){return intent;}
}
