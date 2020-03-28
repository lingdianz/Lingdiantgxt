package com.example.hesiod.lingdiantgxt.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.example.hesiod.lingdiantgxt.link;
import com.example.hesiod.lingdiantgxt.timer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

public class mysocket{
    private Socket socket=null;
    private DataOutputStream dos = null;
    private DataInputStream dis = null;
    private String hdmsg="",resmsg="",psw="1",order="";
    private Boolean busy=false;
    private List<String> msglist=new ArrayList<>();

    public Boolean getbusy(){return busy;}
    public void clrbusy(){this.busy=false;}
    public void setbusy(){this.busy=true;}
    public String getpsw(){return psw;}
    public void setpsw(String psw){this.psw=psw;}
    public String getresmsg(){return resmsg;}

    //1、登录，返回true或false、异常消息
    public Boolean Login(String username1, String password1, String userlp1){
        if(!msglist.isEmpty()){msglist.clear();}
        msglist.add(username1);
        msglist.add(password1);
        msglist.add(userlp1);
        order="login";
        new DownloadTask().execute();
        return true;
    }
    //加载最新app，"getvesion#cmd#android#cmd#vesion#news#"
    public void getvesions(String what) {
        if(!msglist.isEmpty()){msglist.clear();}
        msglist.add("android");
        msglist.add(what);
        order="getvesion";
        new DownloadTask().execute();
    }

    //注册
    public void Side(String name1,String pass1,String phone1) {
        //注册账号，register#cmd#name#cmd#password#cmd#phonenumber#news#
        if(!msglist.isEmpty()){msglist.clear();}
        msglist.add(name1);
        msglist.add(pass1);
        msglist.add(phone1);
        order="register";
        new DownloadTask().execute();
    }

    //注册
    public void getMima(String username1,String phone1) {
        //注册账号，getpassword#cmd#username#cmd#phonenumber#news#
        if(!msglist.isEmpty()){msglist.clear();}
        msglist.add(username1);
        msglist.add(phone1);
        order="getpassword";
        new DownloadTask().execute();
    }
    //获取用户名，"getname#cmd#username#cmd#userlp#news#"
    public void reloadName(String username1 ,String userlp1) {
        if(!msglist.isEmpty()){msglist.clear();}
        msglist.add(username1);
        msglist.add(userlp1);
        order="getname";
        new DownloadTask().execute();
    }
    //加载控制器
    public  void reloadClient(String username1,String userlp1) {
        //获取控制器列表，"getclients#cmd#username#cmd#userlp#news#"
        if (!msglist.isEmpty()) { msglist.clear();}
        msglist.add(username1);
        msglist.add(userlp1);
        order = "getclients";
        new DownloadTask().execute();
    }
    //获取在线控制器列表，getonclient#cmd#500010001#cmd#testuserlp#news#
    public void getOnclient(String username1,String userlp1){
        if(!msglist.isEmpty()){msglist.clear();}
        msglist.add(username1);
        msglist.add(userlp1);
        order="getonclient";
        new DownloadTask().execute();
    }
    //子操作，移动控制器，登录后可执行，成功返回success
    public void movClient(String username,String clientname,String projectname,String userlp) {      //movclient#cmd#username#cmd#clientname#cmd#project#cmd#userlp#news#
        if (!msglist.isEmpty()) {
            msglist.clear();
        }
        msglist.add(username);
        msglist.add(clientname);
        msglist.add(projectname);
        msglist.add(userlp);
        order = "movclient";
        new DownloadTask().execute();
    }
    //工程改名
    public void changeproject(String username,String oldpname,String newpname,String userlp) {
        //changeproject#cmd#username#cmd#oldprojectname#cmd#newprojectname#cmd#userlp#news#
        if (!msglist.isEmpty()) {
            msglist.clear();
        }
        msglist.add(username);
        msglist.add(oldpname);
        msglist.add(newpname);
        msglist.add(userlp);
        order = "changeproject";
        new DownloadTask().execute();
    }
    //添加控制器，addclient#cmd#username#cmd#projectname#cmd#clientname#cmd#id#cmd#userlp#news#
    public void addClient(String username,String project1,String client1,String id1,String userlp){
        if (!msglist.isEmpty()) {
            msglist.clear();
        }
        msglist.add(username);
        msglist.add(project1);
        msglist.add(client1);
        msglist.add(id1);
        msglist.add(userlp);
        order = "addclient";
        new DownloadTask().execute();
    }
    //添加驱动，adddriver#cmd#username#cmd#client#cmd#groupname#cmd#drivername#cmd#id#cmd#userlp#news#
    public void addDriver(String username,String client1, String group1, String driver1, String id1,String userlp){
        if (!msglist.isEmpty()) {
            msglist.clear();
        }
        msglist.add(username);
        msglist.add(client1);
        msglist.add(group1);
        msglist.add(driver1);
        msglist.add(id1);
        msglist.add(userlp);
        order = "adddriver";
        new DownloadTask().execute();
    }
    //获取分组
    public void loadGroup(String username,String client1,String userlp){
        //获取分组列表，getgroups#cmd#username#cmd#clientname#cmd#testuserlp#news#
        if (!msglist.isEmpty()) { msglist.clear();}
        msglist.add(username);
        msglist.add(client1);
        msglist.add(userlp);
        order = "getgroups";
        new DownloadTask().execute();
    }

    //获取驱动
    public void loadDriver(String username,String client1,String userlp) {
        //获取驱动列表，getdrivers#cmd#username#cmd#clientname#cmd#userlp#news#
        if (!msglist.isEmpty()) { msglist.clear();}
        msglist.add(username);
        msglist.add(client1);
        msglist.add(userlp);
        order = "getdrivers";
        new DownloadTask().execute();
    }
    //更改账号密码，    "setbright#cmd#username#cmd#clientname#cmd#drivername#cmd#bright#cmd#userlp#news#"
    public void setDriverBright(String username,String client1,String driver1, String bright1,String userlp){
        if(!msglist.isEmpty()){msglist.clear();}
        msglist.add(username);
        msglist.add(client1);
        msglist.add(driver1);
        msglist.add(bright1);
        msglist.add(userlp);
        order="setbright";
        new DownloadTask().execute();
    }
    //更改账号密码，    "setgroupbright#cmd#username#cmd#clientname#cmd#groupname#cmd#bright#cmd#userlp#news#"
    public void setGroupBright(String username,String client1,String group1, String bright1,String userlp){
        if(!msglist.isEmpty()){msglist.clear();}
        msglist.add(username);
        msglist.add(client1);
        msglist.add(group1);
        msglist.add(bright1);
        msglist.add(userlp);
        order="setgroupbright";
        new DownloadTask().execute();
    }
    //更改用户昵称，changename#cmd#username#cmd#name#cmd#userlp#news
    public void setName(String username,String name1,String userlp){
        if(!msglist.isEmpty()){msglist.clear();}
        msglist.add(username);
        msglist.add(name1);
        msglist.add(userlp);
        order="changename";
        new DownloadTask().execute();
    }

    //更改账号密码，    "changepassword#cmd#username#cmd#password#cmd#password1#cmd#userlp#news#"
    public void setPassword(String username,String password,String password1,String userlp){
        if(!msglist.isEmpty()){msglist.clear();}
        msglist.add(username);
        msglist.add(password);
        msglist.add(password1);
        msglist.add(userlp);
        order="changepassword";
        new DownloadTask().execute();
    }
    //注销登录,offuser#cmd#username#cmd#userlp#news#
    public void offLogin(String username,String userlp) {
        if(!msglist.isEmpty()){msglist.clear();}
        msglist.add(username);
        msglist.add(userlp);
        order="offuser";
        new DownloadTask().execute();
    }
    //删除分组，delgroup#cmd#username#cmd#clientname#cmd#groupname#cmd#userlp#news#
    public void delGroup(String username,String clientname,String groupname,String userlp) {
        if(!msglist.isEmpty()){msglist.clear();}
        msglist.add(username);
        msglist.add(clientname);
        msglist.add(groupname);
        msglist.add(userlp);
        order="delgroup";
        new DownloadTask().execute();
    }

    //重命名分组，changegroup#cmd#username#cmd#clientname#cmd#oldgroupname#cmd#newgroupname#cmd#userlp#news#
    public void changeGroupname(String username,String clientname,String oldgroupname,String groupname,String userlp) {
        if(!msglist.isEmpty()){msglist.clear();}
        msglist.add(username);
        msglist.add(clientname);
        msglist.add(oldgroupname);
        msglist.add(groupname);
        msglist.add(userlp);
        order="changegroup";
        new DownloadTask().execute();
    }

    //重命名分组，addgroup#cmd#username#cmd#clientname#cmd#groupname#cmd#userlp#news#
    public void addGroup(String username,String clientname,String groupname,String userlp) {
        if(!msglist.isEmpty()){msglist.clear();}
        msglist.add(username);
        msglist.add(clientname);
        msglist.add(groupname);
        msglist.add(userlp);
        order="addgroup";
        new DownloadTask().execute();
    }
    //移动驱动分组，movdriver#cmd#username#cmd#clientname#cmd#newgroupname#cmd#drivername#cmd#userlp#news#
    public void amovDriver(String username,String clientname,String groupname,String drivername,String userlp) {
        if(!msglist.isEmpty()){msglist.clear();}
        msglist.add(username);
        msglist.add(clientname);
        msglist.add(groupname);
        msglist.add(drivername);
        msglist.add(userlp);
        order="movdriver";
        new DownloadTask().execute();
    }
    //移除驱动，deldriver#cmd#username#cmd#clientname#cmd#drivername#cmd#userlp#news#
    public void delDriver(String username,String clientname,String drivername,String userlp) {
        if(!msglist.isEmpty()){msglist.clear();}
        msglist.add(username);
        msglist.add(clientname);
        msglist.add(drivername);
        msglist.add(userlp);
        order="deldriver";
        new DownloadTask().execute();
    }
    //移除控制器，delclient#cmd#username#cmd#clientname#cmd#userlp#news#
    public void delClient(String username,String clientname,String userlp){
        if(!msglist.isEmpty()){msglist.clear();}
        msglist.add(username);
        msglist.add(clientname);
        msglist.add(userlp);
        order="delclient";
        new DownloadTask().execute();
    }
    //替换控制器：replaceclient#cmd#username#cmd#oldclient#cmd#newclient#cmd#userlp#news#
    public void replaceClient(String username,String oldclient,String newclient,String userlp){
        if(!msglist.isEmpty()){msglist.clear();}
        msglist.add(username);
        msglist.add(oldclient);
        msglist.add(newclient);
        msglist.add(userlp);
        order="replaceclient";
        new DownloadTask().execute();
    }
    //设置控制器地图位置，经度纬度,changelocation#cmd#username#cmd#clientname#cmd#locationstr#cmd#userlp#news#"
    public void changeLocation(String username,String clientname,String location,String userlp){
        if(!msglist.isEmpty()){msglist.clear();}
        msglist.add(username);
        msglist.add(clientname);
        msglist.add(location);
        msglist.add(userlp);
        order="changelocation";
        new DownloadTask().execute();
    }


    //设置定时器,访问格式：changetimer#cmd#username#cmd#clientname#cmd#groupname#cmd#
    // istimer#cmd#b1#cmd#b2#cmd#b3#cmd#b4#cmd#t1#cmd#t2#cmd#t3#cmd#t4#cmd#userlp#news#
    public void setGrouptimer(List<String> msglist1){
        this.msglist = msglist1;
        order="changetimer";
        new DownloadTask().execute();
    }


    private class DownloadTask extends AsyncTask<Void,Integer,Boolean> {
        @Override
        protected void onPreExecute(){
            busy = true;
        }
        @Override
        protected Boolean doInBackground(Void...params){
            //1----------连接
            try {
                socket = new Socket();
                SocketAddress socAddress = new InetSocketAddress(messagerom.ADDRESS, messagerom.PORT);
                socket.connect(socAddress, 2000);
                socket.setSoTimeout(5000);

                dos = new DataOutputStream(socket.getOutputStream());
                dis = new DataInputStream(socket.getInputStream());
            } catch (Exception e) {
                psw="未连接";
                return false;
            }
            StringBuilder msg=new StringBuilder();
            while (msglist.size()>0){
                msg.append("#cmd#");
                msg.append(msglist.get(0));
                msglist.remove(0);
            }
            //2----------发送
            hdmsg = order+msg+"#news#";     //命令字加信息加结束码
            try {
                dos.write(hdmsg.getBytes());
                dos.flush();
            }
            catch (Exception e) {
                psw = "发送失败";
                return false;
            }
            //3-----------接收
            try {
                //创建一个缓冲字节数
                int cout=0;
                resmsg = "";
                do {
                int r = dis.available();
                    while (r == 0) {
                        //Thread.sleep(10);
                        r = dis.available();
                        Thread.sleep(1);
                        cout++;
                        if (cout > 100) {
                            psw = "连接超时";
                            return false;
                        }
                    }
                    byte[] b = new byte[r];
                    dis.read(b);
                    String strSRecMsg = new String(b, "utf-8");
                    resmsg = resmsg + strSRecMsg.trim();
                }while (!resmsg.contains("#news#"));
                psw="true";
            }  catch (Exception e) {
                psw="接收异常";e.printStackTrace();
                return false;
            }
            return true;
        }
        @Override
        protected void onPostExecute(Boolean result){
            if (dos!= null) {
                try {dos.close();dos = null;}
                catch(IOException e){}
            }
            if (dis!= null) {
                try {dis.close();dis = null;}
                catch(IOException e){}
            }
            if (socket != null) {
                try {socket.close();socket = null;}
                catch (IOException e) {}
            } //重新初始化socket

            if(psw.equals("true")) {        //当接收到
                if(resmsg.length()>0 && resmsg.contains("#news#")){
                    String[] cutres = resmsg.split("#news#");
                    int position = cutres.length;
                    if (position > 0) {
                        for (int i = 0; i < position; i++) {
                            String[] cutmsg = cutres[i].split("#cmd#");
                            if (cutmsg.length == 2) {
                                if (cutmsg[0].equals(order)) {
                                    if (cutmsg[1].length() > 0) {
                                        resmsg = cutmsg[1];
                                        if (resmsg.equals("false")) {
                                            psw = "操作失败";
                                        }else if (resmsg.equals("nologin")) {
                                            psw = "未登录";
                                        }else{
                                            psw = "true"; hdmsg=""; busy=false;//复位
                                            return;
                                        }
                                        break;
                                    }else{psw = "接收错误";}
                                }else{psw = "接收错误";}
                            }else{psw = "接收错误";}
                        }
                    } else {
                        psw = "接收错误";
                    }
                }else{psw = "接收错误";}
            }
            hdmsg="";busy=false;//复位
        }
    }

}
