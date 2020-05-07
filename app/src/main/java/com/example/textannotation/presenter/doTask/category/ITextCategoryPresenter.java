package com.example.textannotation.presenter.doTask.category;

import com.alibaba.fastjson.JSONObject;


import java.util.List;

public interface ITextCategoryPresenter {

    /**
     * 对View层的接口
     */
    public void loadDataAndInitView(int taskId,int docId,int userId);
    public void getLastTask(int taskId,int pid,int userId);
    public void getNextTask(int taskId,int pid,int userId);
    public void saveAnnotationInfo(List<Integer> labelids, int pid, int taskId, int docId, int userId);


    /**
     * 对Model的接口
     */
    public void initViewCallBack(JSONObject jsonObject);
    public void updateViewCallBack(JSONObject jsonObject);
    public void submitCallBack(JSONObject jsonObject);




}
