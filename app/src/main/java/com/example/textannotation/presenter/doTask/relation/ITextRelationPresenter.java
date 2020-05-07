package com.example.textannotation.presenter.doTask.relation;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

import okhttp3.RequestBody;

public interface ITextRelationPresenter {

    /**
     * 对View层的接口
     */
    public void loadDataAndInitView(int taskId,int docId,int userId);
    public void getLastTask(int taskId,int pid,int userId);
    public void getNextTask(int taskId,int pid,int userId);
    public void saveAnnotationInfo(RequestBody requestBody);


    /**
     * 对Model的接口
     */
    public void initViewCallBack(JSONObject jsonObject);
    public void updateViewCallBack(JSONObject jsonObject);
    public void submitCallBack(JSONObject jsonObject);

}
