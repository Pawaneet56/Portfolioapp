package com.example.portfolioapp.Models;

public class Apply {
    String Fullname,applierid,dp,pid,time,skills;

    public Apply(){
    }

    public Apply(String Fullname, String applierid,String dp,String pid,String time,String skills){
        this.Fullname = Fullname;
        this.applierid = applierid;
        this.dp = dp;
        this.pid = pid;
        this.time=time;
        this.skills=skills;
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

    public String getdp() {
        return dp;
    }

    public void setdp(String dp) {
        this.dp = dp;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
public String gettime(){
        return time;
}
public String getSkills(){
        return this.skills;
}
public void settime(String time){
        this.time=time;
}

}




