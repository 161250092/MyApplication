package com.example.textannotation.presenter.doTask.sort;

import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.textannotation.model.doTask.sort.ITextSortFragmentModel;
import com.example.textannotation.model.doTask.sort.TextSortFragmentModel;
import com.example.textannotation.view.doTask.textSort.ITextSortView;

import okhttp3.RequestBody;

public class TextSortPresenter implements ITextSortPresenter{

    private ITextSortView iTextSortView;

    private ITextSortFragmentModel iTextSortFragmentModel;

    public TextSortPresenter(ITextSortView iTextSortView) {
        this.iTextSortView = iTextSortView;
        iTextSortFragmentModel = new TextSortFragmentModel(this);
    }

    @Override
    public void loadDataAndInitView(int taskId, int docId, int userId) {
        iTextSortFragmentModel.getFirstTaskParagraph(taskId,docId,userId);
    }

    @Override
    public void getLastTask(int taskId, int pid, int userId) {
        iTextSortFragmentModel.getLastTask(taskId,pid,userId);
    }

    @Override
    public void getNextTask(int taskId, int pid, int userId) {
        iTextSortFragmentModel.getNextTask(taskId,pid,userId);
    }

    @Override
    public void saveAnnotationInfo(RequestBody requestBody) {
        iTextSortFragmentModel.saveAnnotationInfo(requestBody);
    }

    @Override
    public void initViewCallBack(JSONObject jsonObject) {
        Log.e("presenter init view",jsonObject.toJSONString());

        if (jsonObject == null ) {
            iTextSortView.showExceptionInfo("网络异常");
            return;
        }

        if (jsonObject.getInteger("code") == -1){
            iTextSortView.showExceptionInfo("查询失败");
            return;
        }

        Log.e("presenter","1");
        JSONObject instanceItemJSON = (JSONObject) jsonObject.getJSONArray("instanceItem").get(0);
        if (instanceItemJSON == null){
            iTextSortView.showCompletedInfo("已完成");
            return;
        }

        int instanceId = instanceItemJSON.getInteger("instid");
        JSONArray listItem = instanceItemJSON.getJSONArray("itemList");
        iTextSortView.initView(instanceId,listItem);
    }

    @Override
    public void updateViewCallBack(JSONObject jsonObject) {
        Log.e("presenter update view",jsonObject.toJSONString());
        if (jsonObject == null) {
            iTextSortView.showExceptionInfo("网络异常");
            return;
        }
        else if (jsonObject.getInteger("code") == 5000){
            iTextSortView.showSimpleInfo("当前任务未提交");
            return;
        }
        else if (jsonObject.getInteger("code") == -1){
            iTextSortView.showSimpleInfo(jsonObject.toJSONString());
            return;
        }
        JSONArray itemList = jsonObject.getJSONObject("data").getJSONArray("itemList");
        if (itemList == null ||itemList.get(0) == null){
            iTextSortView.showCompletedInfo("已经完成");
            return;
        }
        int instanceId = jsonObject.getJSONObject("data").getInteger("instid");
        iTextSortView.updateContent(instanceId,itemList);
    }

    @Override
    public void submitCallBack(JSONObject jsonObject) {
        Log.e("presenter submit view",jsonObject.toJSONString());
        iTextSortView.showSubmitInfo(jsonObject.toJSONString());
    }
}
