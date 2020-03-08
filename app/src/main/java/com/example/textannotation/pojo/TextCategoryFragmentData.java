package com.example.textannotation.pojo;

import com.example.textannotation.util.SerializableMap;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class TextCategoryFragmentData {

    //和设置标签相关的
    //标签名称
    private ArrayList<String> mLabelNames = new ArrayList<String>();
    //标签名称
    private ArrayList<Integer> mLabelId = new ArrayList<Integer>();

    private int taskId;
    //做任务页面 查看做任务页面
    private String typename;

    private SerializableMap labelMap;
    private Map<String,Integer> hashmap = new LinkedHashMap<>();

    //任务在全部文本中的索引
    private int contentId;
    private int contentIndex;

    private String content;
    private int docId;
    private int userId;

    public TextCategoryFragmentData() {
    }

    public ArrayList<String> getmLabelNames() {
        return mLabelNames;
    }

    public void setmLabelNames(ArrayList<String> mLabelNames) {
        this.mLabelNames = mLabelNames;
    }

    public ArrayList<Integer> getmLabelId() {
        return mLabelId;
    }

    public void setmLabelId(ArrayList<Integer> mLabelId) {
        this.mLabelId = mLabelId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public SerializableMap getLabelMap() {
        return labelMap;
    }

    public void setLabelMap(SerializableMap labelMap) {
        this.labelMap = labelMap;
    }

    public Map<String, Integer> getHashmap() {
        return hashmap;
    }

    public void setHashmap(Map<String, Integer> hashmap) {
        this.hashmap = hashmap;
    }

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public int getContentIndex() {
        return contentIndex;
    }

    public void setContentIndex(int contentIndex) {
        this.contentIndex = contentIndex;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getDocId() {
        return docId;
    }

    public void setDocId(int docId) {
        this.docId = docId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
