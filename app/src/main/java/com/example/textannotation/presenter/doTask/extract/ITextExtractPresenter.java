package com.example.textannotation.presenter.doTask.extract;

import com.alibaba.fastjson.JSONObject;
import com.example.textannotation.pojo.extract.DoExtractionData;



public interface ITextExtractPresenter {


    /**
     * 对View层的接口
     */
    public void loadDataAndInitView(int taskId,int docId,int userId);
    public void getLastTask(int taskId,int pid,int userId);
    public void getNextTask(int taskId,int pid,int userId);
    public void saveAnnotationInfo(DoExtractionData doExtractionData);


    /**
     * 对Model的接口
     */
    public void initViewCallBack(JSONObject jsonObject);
    public void updateViewCallBack(JSONObject jsonObject);
    public void submitCallBack(JSONObject jsonObject);


}
