package com.example.portfolioapp.Models;

public class Comments {

    String comment,dp,timestamp,uid,uname,pid;

    public Comments() {
    }

    public Comments(String comment, String dp, String timestamp, String uid, String uname,String pid) {
        this.comment = comment;
        this.dp = dp;
        this.timestamp = timestamp;
        this.uid = uid;
        this.uname = uname;
        this.pid = pid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }
}
