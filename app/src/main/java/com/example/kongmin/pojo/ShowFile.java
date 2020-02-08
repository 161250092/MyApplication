package com.example.kongmin.pojo;

public class ShowFile {

    private int fileid;
    private String name;
    private String size;
    private int imageId;

    public ShowFile(int fileid,String name, String size, int imageId) {
        this.fileid = fileid;
        this.name = name;
        this.size = size;
        this.imageId = imageId;
    }
    public ShowFile(String name, String size, int imageId) {
        this.name = name;
        this.size = size;
        this.imageId = imageId;
    }

    public int getFileid() {
        return fileid;
    }

    public void setFileid(int fileid) {
        this.fileid = fileid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
