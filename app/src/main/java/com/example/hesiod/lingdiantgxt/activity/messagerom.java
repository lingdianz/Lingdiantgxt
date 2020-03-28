package com.example.hesiod.lingdiantgxt.activity;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.hesiod.lingdiantgxt.baseadapter.myset;
import com.example.hesiod.lingdiantgxt.myJavaBean.ce_clients;
import com.example.hesiod.lingdiantgxt.myJavaBean.ce_drivers;
import com.example.hesiod.lingdiantgxt.myJavaBean.ce_groups;

//import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

public class messagerom{
    public Context context;
    public messagerom(Context context){this.context=context;}

    public String username="",password="",name="",userlp="",phonenumber="";//账号信息
    private String vesionnews="",loadaddr="";   //app新版本信息
    private Boolean ubusy=false,isonline=false;
    private List<ce_clients> clients=new ArrayList<>();
    private List<ce_drivers> drivers = new ArrayList<>();
    private List<ce_groups> groups = new ArrayList<>();
    private List<List<ce_clients>> clientlist= new ArrayList<>();
    private List<List<ce_drivers>> driverlist = new ArrayList<>();
    private List<String> projectlist=new ArrayList<>();
    private List<String> linecountlist=new ArrayList<>();
    private List<String> onlinecountlist=new ArrayList<>();
    private List<String> onlineclientlist=new ArrayList<>();
    //private List<project_clients> clientslist=new ArrayList<>();
    private List<myset> newslist = new ArrayList<>();
    private int choose = 0;

    public void setChoose (int choose){this.choose=choose;}
    public int getChoose (){return this.choose;}
    public List<List<ce_clients>> getClientlist(){return clientlist;}
    public List<List<ce_drivers>> getDriverlist(){return driverlist;}
    public List<ce_clients> getClients(){return clients;}
    public List<ce_drivers> getDrivers(){return drivers;}
    public List<ce_groups> getGroups(){return groups;}
    public String getName(){return name;}
    public void sevename(String name){this.name=name;}
    public String getUsername(){return username;}
    public void seveUsername(String username){this.username=username;}
    public String getPassword(){return password;}
    public void sevePassword(String password){this.password=password;}
    public void seveUserlp(String userlp){this.userlp=userlp;}
    public Boolean getBusy(){return ubusy;}
    public List<String> getProjectlist(){return projectlist;}
    public List<String> getLinecountlist(){return linecountlist;}
    public List<String> getOnlineclient(){return onlineclientlist;}
    //public List<project_clients> getClientslist(){return clientslist;}
    public List<myset> getNewslist(){return newslist;}
    public void setNewslist(List<myset> newslist){this.newslist = newslist;}

    public Boolean jxclients(String resmsg){
        if(resmsg.length()>0&&!resmsg.equals("null")){
            //getclients#cmd#id#cmd#username#cmd#project#cmd#client#cmd#clientpass#cmd#location#get##news#
            String[] cutres=resmsg.split("#get#");
            int position=cutres.length;
            if(position>0){
                clients.clear();//当接收里有列表数据时，清空缓存器，重新加载，否则保留
                for(int i=0;i<position;i++){
                    String[] cutmsg=cutres[i].split("#amp#");
                    if(cutmsg.length==7){
                        ce_clients msg1=new ce_clients();
                        msg1.setId(cutmsg[0]);
                        msg1.setUsername(cutmsg[1]);
                        msg1.setProject(cutmsg[3]);
                        msg1.setClientname(cutmsg[4]);
                        msg1.setClientpass(cutmsg[5]);
                        msg1.setLocation(cutmsg[6]);
                        clients.add(msg1);
                    }}}
        }else if(resmsg.equals("null")){
            clients.clear();
        }
        reclientlist();
        return true;
    }

    private Boolean reclientlist(){
        if(!clientlist.isEmpty()){
            clientlist.clear();
        }
        if(!projectlist.isEmpty()){
            projectlist.clear();
        }
        projectlist.add("0");
        clientlist.add(clients);

        if(!clients.isEmpty()){
            List<ce_clients> clpro=new ArrayList<>(clients);
            List<String> pro = new ArrayList<>();
            String spro = "";
            for(int ct=0;ct< clpro.size();ct++){
                spro = clpro.get(ct).getProject();
                if(!spro.equals("0")){
                    if(projectlist.contains(spro)){
                        clientlist.get(projectlist.indexOf(spro)).add(clpro.get(ct));
                    }else{
                        projectlist.add(spro);
                        List<ce_clients> lpro1 = new ArrayList<>();
                        lpro1.add(clpro.get(ct));
                        clientlist.add(lpro1);
                    }
                }
            }
        }
        projectlist.set(0,"我的工程");
        return true;
    }
    //取消
    //保存替换控制器，就是复制他们的工程名和地址，服务器数据库移动了他们底下的分组与驱动
    /*public Boolean seveReplaceClient(String oldclient,String newclient){
        ce_clients oldclientobj = new ce_clients();
        ce_clients newclientobj =new ce_clients();
        for(int ct=0;ct<clients.size();ct++){
            if(clients.get(ct).getClientname().equals(oldclient)){
                oldclientobj = clients.get(ct);
            }
            if(clients.get(ct).getClientname().equals(newclient)){
                newclientobj = clients.get(ct);
            }
        }
        if(newclientobj != null && oldclientobj != null) {
            newclientobj.setProject(oldclientobj.getProject());
            newclientobj.setLocation(oldclientobj.getLocation());
        }
        return true;
    }*/
    //在新加载client后，重新解析列表
    private Boolean reList()
    {
        reclientlist();
        reonCountlist();
        return true;
    }

    public Boolean seveOnclient(String resmsg){
        if(!onlineclientlist.isEmpty()){
            onlineclientlist.clear();//当接收里有列表数据时，清空缓存器，重新加载，否则保留
        }
        if(resmsg.length()>0 && !resmsg.equals("null")){
            String[] cutres = resmsg.split("#get#");
            int position=cutres.length;
            if(position>0){
                for(int i=0;i<position;i++){
                    onlineclientlist.add(cutres[i]);
                }}}
        reonCountlist();//当新加载成功时，解析列表
        return true;
    }

    //5、在线计数，更新顺序为3
    private Boolean reonCountlist(){
        if(!linecountlist.isEmpty()){
            linecountlist.clear();
        }
        if(!clients.isEmpty()){
            for(int i=0;i<clientlist.size();i++){
                int oncount = 0;
                for (int a = 0; a < clientlist.get(i).size(); a++) {
                    if (onlineclientlist.contains(clientlist.get(i).get(a).getClientname())) {
                        oncount++;
                    }
                }
                linecountlist.add(oncount + "/" + clientlist.get(i).size());
            }
        }else {
            linecountlist.add("0/0");
        }
        return true;
    }

    //提取控制器内部的小分组
    public Boolean sevegroups(String resmsg){
        if(resmsg.length()>0 && !resmsg.equals("null")){
            groups.clear();//当接收里有列表数据时，清空缓存器，重新加载，否则保留
            String[] cutres=resmsg.split("#get#");
            int position=cutres.length;
            if(position>0){
                for(int i=0;i<position;i++){
                    String[] cutmsg=cutres[i].split("#amp#");
                    if(cutmsg.length==13){
                        //1#amp#500010001#amp#100#amp#50#amp#30#amp#0#amp#0
                        // #amp#18:00#amp#00:00#amp#06:00#amp#08:00#amp#c10010001#amp#group1
                        ce_groups msg1=new ce_groups();
                        msg1.setId(cutmsg[0]);
                        msg1.setUsername(cutmsg[1]);
                        msg1.setBrightness1(cutmsg[2]);
                        msg1.setBrightness2(cutmsg[3]);
                        msg1.setBrightness3(cutmsg[4]);
                        msg1.setBrightness4(cutmsg[5]);
                        msg1.setIstimer(cutmsg[6]);
                        msg1.setTime1(cutmsg[7]);
                        msg1.setTime2(cutmsg[8]);
                        msg1.setTime3(cutmsg[9]);
                        msg1.setTime4(cutmsg[10]);
                        msg1.setClient(cutmsg[11]);
                        msg1.setGroupname(cutmsg[12]);
                        groups.add(msg1);
                    }}}
        }else if(resmsg.equals("null")){
            groups.clear();
        }
        return true;
    }

    //提取驱动
    public Boolean seveDrivers(String resmsg){
        drivers.clear();//当接收里有列表数据时，清空缓存器，重新加载，否则保留
        if(resmsg.length()>0 && !resmsg.equals("null")){
                String[] cutres=resmsg.split("#get#");
                int position=cutres.length;
                if(position>0){
                    for(int i=0;i<position;i++){
                        String[] cutmsg=cutres[i].split("#amp#");
                        if(cutmsg.length==14){
                            //id,username,type,brightness,setbrightness,client,groupname,drivername;
                            //voltage,current,setvoltage,setcurrent;
                            ce_drivers msg1=new ce_drivers();
                            msg1.setId(cutmsg[0]);
                            msg1.setUsername(cutmsg[1]);
                            msg1.setType(cutmsg[2]);
                            msg1.setBrightness(cutmsg[4]);
                            msg1.setSetbrightness(cutmsg[5]);
                            msg1.setClient(cutmsg[6]);
                            msg1.setGroupname(cutmsg[7]);
                            msg1.setDrivername(cutmsg[8]);
                            msg1.setVoltage(cutmsg[9]);
                            msg1.setCurrent(cutmsg[10]);
                            msg1.setSetvoltage(cutmsg[11]);
                            msg1.setSetcurrent(cutmsg[12]);
                            msg1.setCelcius(cutmsg[13]);
                            drivers.add(msg1);
                        }}}
        }else if(resmsg.equals("null")){
            drivers.clear();
        }
        reDriverlist();
        return true;
    }

    //从全部控制器中，解析出列表
    private Boolean reDriverlist(){
        if(!driverlist.isEmpty()){
            driverlist.clear();
        }
        if(groups.size()==0){return true;}
        for(int j=0;j<groups.size();j++){      //
            String grn=groups.get(j).getGroupname();
            List<ce_drivers> drlist=new ArrayList<>();
            for(int i=0;i<drivers.size();i++){
                if(drivers.get(i).getGroupname().equals(grn)){
                    drlist.add(drivers.get(i));         //添加到列表

                    //同时查询定时器
                    if(groups.get(j).getIstimer().equals("0")){
                        drivers.get(i).setIstimer(false);
                    }else{
                        drivers.get(i).setIstimer(true);
                    }
                }
            }
            driverlist.add(drlist);
        }
        return true;
    }
    //保存移动后的控制器列表，并重新解析列表
    public Boolean sevedelclient(String clientname){
        for(int i=0;i<clients.size();i++){
            if(clients.get(i).getClientname().equals(clientname)){
                clients.remove(i);
                break;
            }
        }
        reList();//更改了client就得解析一下列表
        return true;
    }

    //保存新项目名
    public Boolean sevenewprojectname(String oldpname,String newpname){
        for(int ct=0;ct<clients.size();ct++){
            if(clients.get(ct).getProject().equals(oldpname)){
                clients.get(ct).setProject(newpname);
            }
        }
        reList();//更改了client就得解析一下列表
        return true;
    }

    //保存移动后的列表，并解析
    public Boolean sevemovclient(String clientname,String projectname){
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).getClientname().equals(clientname)) {
                clients.get(i).setProject(projectname);
            }
        }
        reList();//更改了client就得解析一下列表
        return true;
    }

    //保存设置后的亮度
    public Boolean seveDriverBright(String driver1,String bright1){
        if(drivers.size()>0){
            for(int ct=0;ct<drivers.size();ct++){
                if(driver1.contains(drivers.get(ct).getDrivername())){
                    drivers.get(ct).setSetbrightness(bright1);
                }
            }
        }else{
            return false;
        }
        reDriverlist();
        return true;
    }

    //保存移除后的分组
    public Boolean seveDelGroup(String groupname){
        for(int ct=0;ct<groups.size();ct++){
            if(groups.get(ct).getGroupname().equals(groupname)){
                groups.remove(ct);
            }
        }
        for(int ct=0;ct<drivers.size();ct++){
            if (drivers.get(ct).getGroupname().equals(groupname)) {
                drivers.get(ct).setGroupname("0");
            }
        }
        reDriverlist();
        return true;
    }

    //保存改名后的分组
    public Boolean seveChangeGroup (String oldgroupname,String newgroupname){
        for(int ct=0;ct<groups.size();ct++){
            if(groups.get(ct).getGroupname().equals(oldgroupname)){
                groups.get(ct).setGroupname(newgroupname);
            }
        }
        for(int ct=0;ct<drivers.size();ct++){
            if (drivers.get(ct).getGroupname().equals(oldgroupname)) {
                drivers.get(ct).setGroupname(newgroupname);
            }
        }
        reDriverlist();
        return true;
    }

    //保存添加分组
    public Boolean addGroup (String clientname,String newgroupname){
        ce_groups group = new ce_groups();
        group.setUsername(username);
        group.setClient(clientname);
        group.setGroupname(newgroupname);
        groups.add(group);
        reDriverlist();
        return true;
    }
    //移动驱动，改归属分组名
    public Boolean sevemovdriver(String drivername,String newgroupname){
        for(int ct=0;ct<drivers.size();ct++){
            if(drivers.get(ct).getDrivername().equals(drivername)){
                drivers.get(ct).setGroupname(newgroupname);
            }
        }
        reDriverlist();
        return true;
    }

    //移动驱动，改归属分组名
    public Boolean sevedeldriver(String drivername){
        for(int ct=0;ct<drivers.size();ct++){
            if(drivers.get(ct).getDrivername().equals(drivername)){
                drivers.remove(ct);
            }
        }
        reDriverlist();
        return true;
    }

    //保存地址，改保存地址
    public Boolean seveLocation(String clientname,String location){
        for(int ct = 0;ct<clients.size();ct++){
            if(clients.get(ct).getClientname().equals(clientname)){
                clients.get(ct).setLocation(location);
            }
        }
        return true;
    }

    //保存设置分组亮度
    /*public Boolean seveGroupbright(String groupname,String bright){
        for(int ct = 0;ct<drivers.size();ct++){
            if(drivers.get(ct).getGroupname().equals(groupname) && groupname.equals("0")){
                drivers.get(ct).setBrightness(bright);
            }
        }
        return true;
    }*/

    //保存定时设置

    //取消，因为返回后全部刷新，就没必要在操作一只了
    //添加驱动
  /*  public Boolean seveaddDriver(String client1,String group1,String driver1,String id1){
        Boolean isexit = false;
        for(int ct = 0;ct< drivers.size();ct++) {
            if (drivers.get(ct).getDrivername().equals(driver1)){
                drivers.get(ct).setClient(client1);
                drivers.get(ct).setGroupname(group1);
                drivers.get(ct).setId(id1);
                return true;    //存在则更改，完了返回
            }
        }   //不存在则添加
        ce_drivers driverobj = new ce_drivers();
        driverobj.setClient(client1);
        driverobj.setGroupname(group1);
        driverobj.setDrivername(driver1);
        driverobj.setId(id1);
        drivers.add(driverobj);
        reDriverlist();
        return true;
    }
*/
    //和风天气 Web API key：
    public static final String weatherKEY = "ce33151fc2314a0c94f93ba4821695d3";


    public static final String ADDRESS = "49.235.7.35";
    public static final int PORT = 17947;

    private String hdmsg="",resmsg="",psw="",order="";
    private Boolean busy=false;

    public Boolean getbusy(){return busy;}
    public void setbusy(){this.busy=true;}
    public void clrbusy(){this.busy=false;}
    public String getpsw(){return psw;}
    public void setpsw(String psw){this.psw=psw;}
    public String getresmsg(){return resmsg;}


    //保存账号信息---------------------------------------------------------
    //存储三个字符串，两个布尔
    private String fusername="",fpassword="",flprandom="";
    private Boolean isremenber=false,isautoload=false;
    public String getFusername(){return fusername;}
    public String getFpassword(){return fpassword;}
    public String getFlprandom(){return flprandom; }
    public Boolean getFisremenber(){return isremenber;}
    public Boolean getFisautoload(){return isautoload;}

    public Boolean readusermessage(Context context1, String file1) {
        SharedPreferences reader=context1.getSharedPreferences(file1,MODE_PRIVATE);
        fusername=reader.getString("username","");
        fpassword=reader.getString("password","");
        isremenber=reader.getBoolean("isremenber",false);
        isautoload=reader.getBoolean("isautoload",false);

        SharedPreferences readlp=context1.getSharedPreferences("lprandom",MODE_PRIVATE);
        flprandom=readlp.getString("lprandom","");
        return true;
    }
    public void seveusermessage(Context context1, String un1,String ps1,Boolean isr1,Boolean isa1){
        this.fpassword =ps1;
        this.fusername =un1;
        this.isremenber =isr1;
        this.isautoload =isa1;
        SharedPreferences.Editor editor=context1.getSharedPreferences("user1",MODE_PRIVATE).edit();
        editor.putString("username",un1);
        if(isr1) {
            editor.putString("password", ps1);
        }else{
            editor.putString("password","");
        }
        editor.putBoolean("isremenber",isr1);
        editor.putBoolean("isautoload",isa1);
        editor.apply();
    }

    public void sevelprandom(Context context){
        this.flprandom = ""+(new Random().nextFloat());
        SharedPreferences.Editor editor=context.getSharedPreferences("lprandom",MODE_PRIVATE).edit();
        editor.putString("lprandom",flprandom);
        editor.apply();
    }

    //保存账号信息--------------------


    public Boolean isFirstPermissionCamera(Context context1) {
        SharedPreferences reader=context1.getSharedPreferences("isfirstopen",MODE_PRIVATE);
        Boolean isfirstopen = reader.getBoolean("isfierstopenc",false);
        return isfirstopen;
    }
    public void noFirstPermissionCamera(Context context1){
        SharedPreferences.Editor editor=context1.getSharedPreferences("isfirstopen",MODE_PRIVATE).edit();
        editor.putBoolean("isfierstopenc",true);
        editor.apply();
    }
    public Boolean isFirstPermissionFile(Context context1){
        SharedPreferences reader=context1.getSharedPreferences("isfirstopen",MODE_PRIVATE);
        Boolean isfirstopen = reader.getBoolean("isfierstopenf",false);
        return isfirstopen;
    }
    public void noFirstPermissionFile(Context context1){
        SharedPreferences.Editor editor=context1.getSharedPreferences("isfirstopen",MODE_PRIVATE).edit();
        editor.putBoolean("isfierstopenf",true);
        editor.apply();
    }
}
