package com.example.textannotation.presenter.doTask.sort;

import com.alibaba.fastjson.JSONObject;

import okhttp3.RequestBody;

public interface ITextSortPresenter {
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
