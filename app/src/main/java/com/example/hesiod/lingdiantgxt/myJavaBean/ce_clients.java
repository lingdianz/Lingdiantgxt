package com.example.hesiod.lingdiantgxt.myJavaBean;

//import org.litepal.crud.DataSupport;

/**
 * Created by Hesiod on 2019/9/20.
 */

public class ce_clients{
    private String id;
    private String username;
    private String project;
    private String clientname;
    private String clientpass;
    private String location;

    public String getProject() {
        return project;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getClientpass() {

        return clientpass;
    }

    public void setClientpass(String clientpass) {
        this.clientpass = clientpass;
    }

    public String getClientname() {
        return clientname;

    }

    public void setClientname(String clientname) {
        this.clientname = clientname;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getUsername() {
        return username;
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