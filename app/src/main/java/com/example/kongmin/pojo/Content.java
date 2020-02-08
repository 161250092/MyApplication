package com.example.kongmin.pojo;

public class Content {
    private int contentid;
    private String contentname;

    public Content(int contentid, String contentname) {
        this.contentid = contentid;
        this.contentname = contentname;
    }

    public int getContentid() {
        return contentid;
    }

    public void setContentid(int contentid) {
        this.contentid = contentid;
    }

    public String getContentname() {
        return contentname;
    }

    public void setContentname(String contentname) {
        this.contentname = contentname;
    }
}
