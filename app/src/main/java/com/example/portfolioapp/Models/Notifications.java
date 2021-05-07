package com.example.portfolioapp.Models;

public class Notifications {

    String pid,timestamp,puid,notification,suid,sname,semail,simage,type;

    public Notifications(String pid, String timestamp, String puid, String notification, String suid, String sname, String semail, String simage,String type) {
        this.pid = pid;
        this.timestamp = timestamp;
        this.puid = puid;
        this.notification = notification;
        this.suid = suid;
        this.sname = sname;
        this.semail = semail;
        this.simage = simage;
        this.type = type;
    }

    public Notifications() {
    }


    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPuid() {
        return puid;
    }

    public void setPuid(String puid) {
        this.puid = puid;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getSuid() {
        return suid;
    }

    public void setSuid(String suid) {
        this.suid = suid;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getSemail() {
        return semail;
    }

    public void setSemail(String semail) {
        this.semail = semail;
    }

    public String getSimage() {
        return simage;
    }

    public void setSimage(String simage) {
        this.simage = simage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
