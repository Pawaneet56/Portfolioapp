package com.example.portfolioapp.Models;

public class Posts {
    String Detail,FullName,ProjectName,PostImage,UserImage;

    public  Posts(){}

    public Posts(String detail, String fullName, String projectName, String postImage, String userImage) {
        Detail = detail;
        FullName = fullName;
        ProjectName = projectName;
        PostImage = postImage;
        UserImage = userImage;
    }

    public String getDetail() {
        return Detail;
    }

    public void setDetail(String detail) {
        Detail = detail;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
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
}
