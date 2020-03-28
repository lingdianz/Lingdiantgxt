package com.example.hesiod.lingdiantgxt.myJavaBean;

//import org.litepal.crud.DataSupport;

/**
 * Created by Hesiod on 2019/9/20.
 */

public class ce_drivers{
    private String id="0",username="0",type="0",brightness="0",setbrightness="0",client="0",groupname="0",drivername="0";
    private String voltage="0",current="0",setvoltage="0",setcurrent="0",celcius = "25";
    private Boolean istimer=false;

    public String getCelcius(){return celcius;}
    public void setCelcius(String celcius){this.celcius=celcius;}
    public Boolean getIstimer(){return istimer;}
    public void setIstimer(Boolean istimer){this.istimer=istimer; }
    public String getUsername() {
        return username;
    }

    public String getGroupname() {
        return groupname;
    }

    public String getSetcurrent() {
        return setcurrent;
    }

    public void setSetcurrent(String setcurrent) {
        this.setcurrent = setcurrent;
    }

    public String getSetvoltage() {
        return setvoltage;
    }

    public void setSetvoltage(String setvoltage) {
        this.setvoltage = setvoltage;
    }

    public String getCurrent() {

        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getVoltage() {

        return voltage;
    }

    public void setVoltage(String voltage) {
        this.voltage = voltage;
    }

    public String getDrivername() {
        return drivername;
    }

    public void setDrivername(String drivername) {
        this.drivername = drivername;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getBrightness() {
        return brightness;

    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getSetbrightness() {
        return setbrightness;
    }

    public void setSetbrightness(String setbrightness) {
        this.setbrightness = setbrightness;
    }

    public void setBrightness(String brightness) {
        this.brightness = brightness;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;

    }

    public void setId(String id) {
        this.id = id;
    }
}
