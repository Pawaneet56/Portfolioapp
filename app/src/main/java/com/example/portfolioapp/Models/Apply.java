package com.example.portfolioapp.Models;

public class Apply {
    String Fullname,id,dp,pid,time,skills,status;

    public Apply(){
    }

    public Apply(String Fullname, String id,String dp,String pid,String time,String skills,String status){
        this.Fullname = Fullname;
        this.id = id;
        this.dp = dp;
        this.pid = pid;
        this.time=time;
        this.skills=skills;
        this.status=status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}




