package com.example.hesiod.lingdiantgxt.myJavaBean;

//import org.litepal.crud.DataSupport;

/**
 * Created by Hesiod on 2019/9/20.
 */

public class ce_groups{
    private String id="0",username="0",brightness1="100",brightness2="100",brightness3="100",brightness4="100";
    private String istimer="0";
    private String time1="0";
    private String time2="0";
    private String time3="0";
    private String time4="0";
    private String client="0";
    private String groupname="0";
    private String setbright="100";

    public String getSetbright(){return setbright;}
    public void setSetbright(String setbright){this.setbright=setbright;}
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getBrightness4() {
        return brightness4;
    }

    public String getTime3() {
        return time3;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getTime4() {

        return time4;
    }

    public void setTime4(String time4) {
        this.time4 = time4;
    }

    public void setTime3(String time3) {
        this.time3 = time3;
    }

    public String getTime2() {

        return time2;
    }

    public void setTime2(String time2) {
        this.time2 = time2;
    }

    public String getTime1() {
        return time1;

    }

    public void setTime1(String time1) {
        this.time1 = time1;
    }

    public String getIstimer() {
        return istimer;
    }

    public void setIstimer(String istimer) {
        this.istimer = istimer;
    }

    public void setBrightness4(String brightness4) {
        this.brightness4 = brightness4;
    }

    public String getBrightness2() {
        return brightness2;

    }

    public String getBrightness3() {
        return brightness3;
    }

    public void setBrightness3(String brightness3) {
        this.brightness3 = brightness3;
    }

    public void setBrightness2(String brightness2) {
        this.brightness2 = brightness2;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBrightness1() {
        return brightness1;
    }

    public void setBrightness1(String brightness1) {
        this.brightness1 = brightness1;
    }
}