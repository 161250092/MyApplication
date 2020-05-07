package com.example.textannotation.presenter.doTask.relation;

import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.textannotation.model.doTask.relation.ITextRelationFragmentModel;
import com.example.textannotation.model.doTask.relation.TextRelationFragmentModel;
import com.example.textannotation.view.doTask.textRelation.ITextRelationView;

import okhttp3.RequestBody;

public class TextRelationPresenter implements ITextRelationPresenter {
    private ITextRelationView iTextRelationView;

    private ITextRelationFragmentModel iTextRelationFragmentModel;

    public TextRelationPresenter(ITextRelationView iTextRelationView) {
        this.iTextRelationView = iTextRelationView;
        iTextRelationFragmentModel = new TextRelationFragmentModel(this);
    }

    @Override
    public void loadDataAndInitView(int taskId, int docId, int userId) {
        iTextRelationFragmentModel.getFirstTaskParagraph(taskId,docId,userId);
    }

    @Override
    public void getLastTask(int taskId, int pid, int userId) {
        iTextRelationFragmentModel.getLastTask(taskId,pid,userId);
    }

    @Override
    public void getNextTask(int taskId, int pid, int userId) {
        iTextRelationFragmentModel.getNextTask(taskId,pid,userId);
    }


    @Override
    public void saveAnnotationInfo(RequestBody requestBody) {
        if (requestBody == null) {
            iTextRelationView.showExceptionInfo("请选择标签");
            return;
        }
        iTextRelationFragmentModel.saveAnnotationInfo(requestBody);
    }



    @Override
    public void initViewCallBack(JSONObject jsonObject) {

        if (jsonObject == null ) {
            iTextRelationView.showExceptionInfo("网络异常");
            return;
        }

        if (jsonObject.getInteger("code") == -1){
            iTextRelationView.showExceptionInfo("查询失败");
            return;
        }

        Log.e("presenter","1");
        JSONObject instanceItemJSON = (JSONObject) jsonObject.getJSONArray("instanceItem").get(0);
        if (instanceItemJSON == null){
            iTextRelationView.showCompletedInfo("已完成");
            return;
        }

        int instid = instanceItemJSON.getInteger("instid");
        Log.e("presenter","2");
        JSONObject item1 = (JSONObject)instanceItemJSON.getJSONArray("itemList").get(0);
        JSONObject item2 = (JSONObject)instanceItemJSON.getJSONArray("itemList").get(1);
        Log.e("presenter","3");
        int itid1 = item1.getInteger("itid");
        int itid2 = item2.getInteger("itid");
        String content1 = item1.getString("itemcontent");
        String content2 = item2.getString("itemcontent");
        Log.e("presenter","4");
        iTextRelationView.initView(instid,itid1,content1,itid2,content2);
    }

    @Override
    public void updateViewCallBack(JSONObject jsonObject) {
        if (jsonObject == null) {
            iTextRelationView.showExceptionInfo("网络异常");
            return;
        }
        else if (jsonObject.getInteger("code") == 5000){
            iTextRelationView.showSimpleInfo("当前任务未提交");
            return;
        }
        else if (jsonObject.getInteger("code") == -1){
            iTextRelationView.showSimpleInfo("已经是第一个任务了");
            return;
        }
        JSONArray itemList = jsonObject.getJSONObject("data").getJSONArray("itemList");
        if (itemList == null ||itemList.get(0) == null){
            iTextRelationView.showCompletedInfo("已经完成");
            return;
        }
        jsonObject = jsonObject.getJSONObject("data");
        int instanceId = jsonObject.getInteger("instid");
        JSONObject item1 = (JSONObject) itemList.get(0);
        JSONObject item2 = (JSONObject) itemList.get(1);
        int itid1 = item1.getInteger("itid");
        int itid2 = item2.getInteger("itid");
        String content1 = item1.getString("itemcontent");
        String content2 = item2.getString("itemcontent");
        iTextRelationView.updateContent(instanceId,itid1,content1,itid2,content2);
    }

    @Override
    public void submitCallBack(JSONObject jsonObject) {
        if(jsonObject.getInteger("status") == 0){
            iTextRelationView.showSubmitInfo("已提交，请进行下一个任务");
            return;
        }

        iTextRelationView.showSubmitInfo(jsonObject.toJSONString());
    }


}
