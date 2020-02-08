package com.example.kongmin.pojo;

import java.util.ArrayList;
/**
 * 试卷
 */
public class Page {
    //问卷id
    private String pageId;
    //问卷状态
    private String status;
    //问卷主题
    private String title;
    //题目
    private ArrayList<Question> quesitions;

    public ArrayList<Question> getQuesitions() {
        return quesitions;
    }

    public void setQuesitions(ArrayList<Question> quesitions) {
        this.quesitions = quesitions;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
