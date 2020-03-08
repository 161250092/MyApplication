package com.example.textannotation.pojo;

public class TaskInfo {
    private String title;   //标题
    private String content; //完成状态
    private String times;   //创建时间
    private String type;   //标注类型
    private String description; //描述
    private Integer viewnum; //浏览数
    private Integer attendnum; //参与数
    private int ids;        //id

    public TaskInfo(int ids, String ti, String con, String type , String time){
        this.ids = ids;
        this.type=type;
        this.title=ti;
        this.content=con;
        this.times=time;
    }

    public TaskInfo(String title, String content, String times, String type, String description, Integer viewnum, Integer attendnum, int ids) {
        this.title = title;
        this.content = content;
        this.times = times;
        this.type = type;
        this.description = description;
        this.viewnum = viewnum;
        this.attendnum = attendnum;
        this.ids = ids;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getViewnum() {
        return viewnum;
    }

    public void setViewnum(Integer viewnum) {
        this.viewnum = viewnum;
    }

    public Integer getAttendnum() {
        return attendnum;
    }

    public void setAttendnum(Integer attendnum) {
        this.attendnum = attendnum;
    }

    public void setIds(int ids) {
        this.ids = ids;
    }

    public TaskInfo(String ti, int id, String con , String time){
        this.ids=id;
        this.title=ti;
        this.content=con;
        this.times=time;
    }

    public TaskInfo(String ti, String con, String time){
        this.title=ti;
        this.content=con;
        this.times=time;
    }

    public TaskInfo(int i, String ti, String time){
        this.ids=i;
        this.title=ti;
        this.times=time;
    }

    public TaskInfo(String ti, String con){
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
