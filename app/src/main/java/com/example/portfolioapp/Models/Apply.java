package com.example.portfolioapp.Models;

public class Apply {
    String Fullname,applierid,dp,pid;

    public Apply(){
    }

    public Apply(String Fullname, String applierid,String dp,String pid){
        this.Fullname = Fullname;
        this.applierid = applierid;
        this.dp = dp;
        this.pid = pid;
    }

    public String getFullName() {
        return Fullname;
    }

    public void setFullName(String fullName) {
        this.Fullname = fullName;
    }

    public String getId() {
        return applierid;
    }

    public void setId(String applierid) {
        this.applierid = applierid;
    }

    public String getUserImage() {
        return dp;
    }

    public void setUserImage(String dp) {
        this.dp = dp;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }


}




