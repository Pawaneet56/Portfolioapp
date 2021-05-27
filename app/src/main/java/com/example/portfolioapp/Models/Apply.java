package com.example.portfolioapp.Models;

public class Apply {
    String Fullname,id,dp,pid,time,skills;

    public Apply(){
    }

    public Apply(String Fullname, String id,String dp,String pid,String time,String skills){
        this.Fullname = Fullname;
        this.id = id;
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
        return id;
    }

    public void setId(String id) {
        this.id = id;
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




