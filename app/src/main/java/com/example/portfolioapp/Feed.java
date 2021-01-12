package com.example.portfolioapp;

public class Feed {

    String uname,pname,description;
    int userpic,postpic;

    public Feed(String uname, String pname, String description, int userpic, int postpic) {
        this.uname = uname;
        this.pname = pname;
        this.description = description;
        this.userpic = userpic;
        this.postpic = postpic;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUserpic() {
        return userpic;
    }

    public void setUserpic(int userpic) {
        this.userpic = userpic;
    }

    public int getPostpic() {
        return postpic;
    }

    public void setPostpic(int postpic) {
        this.postpic = postpic;
    }
}
