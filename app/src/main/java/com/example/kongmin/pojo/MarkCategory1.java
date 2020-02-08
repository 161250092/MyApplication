package com.example.kongmin.pojo;

public class MarkCategory1 {
    private String title;   //标题
    private String content; //完成状态
    private String times;   //创建时间
    private String type;   //标注类型
    private int ids;        //id

    public MarkCategory1(int ids,String ti,String con,String type ,String time){
        this.ids = ids;
        this.type=type;
        this.title=ti;
        this.content=con;
        this.times=time;
    }

    public MarkCategory1(String ti,int id,String con ,String time){
        this.ids=id;
        this.title=ti;
        this.content=con;
        this.times=time;
    }

    public MarkCategory1(String ti,String con,String time){
        this.title=ti;
        this.content=con;
        this.times=time;
    }

    public MarkCategory1(int i,String ti,String time){
        this.ids=i;
        this.title=ti;
        this.times=time;
    }

    public MarkCategory1(String ti,String con){
        this.title=ti;
        this.content=con;
    }

    public int getIds() {
        return ids;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getTimes() {
        return times;
    }

    public String getType() { return type; }
}
