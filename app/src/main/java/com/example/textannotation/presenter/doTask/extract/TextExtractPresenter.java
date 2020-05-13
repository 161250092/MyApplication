package com.example.textannotation.presenter.doTask.extract;

import android.support.v4.app.NavUtils;
import android.util.Log;


import com.alibaba.fastjson.JSONObject;
import com.example.textannotation.model.doTask.extract.ITextExtractFragmentModel;
import com.example.textannotation.model.doTask.extract.TextExtractFragmentModel;
import com.example.textannotation.pojo.extract.DoExtractionData;
import com.example.textannotation.view.doTask.textExtract.ITextExtractView;


public class TextExtractPresenter implements ITextExtractPresenter{

    private ITextExtractView iTextExtractView;

    private ITextExtractFragmentModel iTextExtractFragmentModel;

    public TextExtractPresenter(ITextExtractView iTextExtractView) {
        this.iTextExtractView = iTextExtractView;
        iTextExtractFragmentModel = new TextExtractFragmentModel(this);

    }

    @Override
    public void loadDataAndInitView(int taskId, int docId, int userId) {
        iTextExtractFragmentModel.getFirstTaskParagraph(taskId,docId,userId);
    }

    @Override
    public void getLastTask(int taskId, int pid, int userId) {
        iTextExtractFragmentModel.getLastTask(taskId,pid,userId);
    }

    @Override
    public void getNextTask(int taskId, int pid, int userId) {
        iTextExtractFragmentModel.getNextTask(taskId,pid,userId);
    }

    @Override
    public void saveAnnotationInfo(DoExtractionData doExtractionData) {
        iTextExtractFragmentModel.saveAnnotationInfo(doExtractionData);
    }




    @Override
    public void initViewCallBack(JSONObject jsonObject) {
        if (jsonObject == null ) {
            iTextExtractView.showExceptionInfo("网络异常");
            return;
        }

        if (jsonObject.getInteger("code") == -1){
            iTextExtractView.showExceptionInfo("查询失败");
            return;
        }

        JSONObject data = (JSONObject) jsonObject.getJSONArray("data").get(0);
        if (data == null){
            iTextExtractView.showCompletedInfo("已完成");
            return;
        }

        int pid = data.getInteger("pid");
        String content = data.getString("paracontent");
        iTextExtractView.initView(pid,content);
    }

    @Override
    public void updateViewCallBack(JSONObject jsonObject) {
        if (jsonObject == null) {
            iTextExtractView.showExceptionInfo("网络异常");
            return;
        }
        else if (jsonObject.getInteger("code") == null){
            iTextExtractView.showExceptionInfo("服务器异常");
            return;
        }


        else if (jsonObject.getInteger("code") == 5000){
            iTextExtractView.showSimpleInfo("当前任务未提交");
            return;
        }

        else if (jsonObject.getInteger("code") == -1){
            iTextExtractView.showSimpleInfo("已经是第一个任务");
            return;
        }
        else if (jsonObject.getInteger("code") == 5001){
            iTextExtractView.showCompletedInfo("任务已经完成");
            return;
        }

        JSONObject data = jsonObject.getJSONObject("data");
        int pid = data.getInteger("pid");
        String content = data.getString("paracontent");
        Log.e("presenter",pid +" "+content);

        iTextExtractView.updateContent(pid,content);
    }

    @Override
    public void submitCallBack(JSONObject jsonObject) {
        if (jsonObject.getInteger("status") != null && jsonObject.getInteger("status") == 0)
            iTextExtractView.showSubmitInfo("提交成功");
        else
            iTextExtractView.showSubmitInfo(jsonObject.toJSONString());
    }
}
