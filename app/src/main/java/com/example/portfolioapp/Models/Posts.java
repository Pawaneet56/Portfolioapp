package com.example.portfolioapp.Models;

public class Posts {

    String FullName,Id,Detail,ProjectName,PostImage,UserImage,pid,pTime;

    public Posts() {
    }

    public Posts(String fullName, String id, String detail, String projectName, String postImage, String userImage, String pid, String pTime) {
        FullName = fullName;
        Id = id;
        Detail = detail;
        ProjectName = projectName;
        PostImage = postImage;
        UserImage = userImage;
        this.pid = pid;
        this.pTime = pTime;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getDetail() {
        return Detail;
    }

    public void setDetail(String detail) {
        Detail = detail;
    }

    public String getProjectName() {
        return ProjectName;
    }

    public void setProjectName(String projectName) {
        ProjectName = projectName;
    }

    public String getPostImage() {
        return PostImage;
    }

    public void setPostImage(String postImage) {
        PostImage = postImage;
    }

    public String getUserImage() {
        return UserImage;
    }

    public void setUserImage(String userImage) {
        UserImage = userImage;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getpTime() {
        return pTime;
    }

    public void setpTime(String pTime) {
        this.pTime = pTime;
    }
}
