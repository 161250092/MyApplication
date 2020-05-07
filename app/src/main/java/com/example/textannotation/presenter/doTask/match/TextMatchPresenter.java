package com.example.textannotation.presenter.doTask.match;


import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.example.textannotation.model.doTask.match.ITextMatchFragmentModel;
import com.example.textannotation.model.doTask.match.TextMatchFragmentModel;
import com.example.textannotation.view.doTask.textMatch.ITextMatchView;

import okhttp3.RequestBody;

public class TextMatchPresenter implements ITextMatchPresenter{

    private ITextMatchView iTextMatchView;
    private ITextMatchFragmentModel iTextMatchFragmentModel;

    public TextMatchPresenter(ITextMatchView iTextMatchView) {
        this.iTextMatchView = iTextMatchView;
        iTextMatchFragmentModel = new TextMatchFragmentModel(this);
    }

    @Override
    public void loadDataAndInitView(int taskId, int docId, int userId) {
         iTextMatchFragmentModel.getFirstTaskParagraph(taskId,docId,userId);
    }

    @Override
    public void getLastTask(int taskId, int pid, int userId) {
        iTextMatchFragmentModel.getLastTask(taskId,pid,userId);
    }

    @Override
    public void getNextTask(int taskId, int pid, int userId) {
        iTextMatchFragmentModel.getNextTask(taskId,pid,userId);
    }

    @Override
    public void saveAnnotationInfo(RequestBody requestBody) {
        iTextMatchFragmentModel.saveAnnotationInfo(requestBody);
    }

    @Override
    public void initViewCallBack(JSONObject jsonObject) {

        if (jsonObject == null ) {
            iTextMatchView.showExceptionInfo("网络异常");
            return;
        }


        if (jsonObject.getInteger("code") == -1){
            iTextMatchView.showExceptionInfo("查询失败");
            return;
        }

        Log.e("presenter","1");
        JSONObject instanceItemJSON = (JSONObject) jsonObject.getJSONArray("instanceItem").get(0);
        if (instanceItemJSON == null){
            iTextMatchView.showCompletedInfo("已完成");
            return;
        }
        iTextMatchView.initView(instanceItemJSON);

    }

    @Override
    public void updateViewCallBack(JSONObject jsonObject) {
        Log.e("update view ",jsonObject.toJSONString());

        if (jsonObject == null) {
            iTextMatchView.showExceptionInfo("网络异常");
            return;
        }

        else if (jsonObject.getInteger("code") == 5000){
            iTextMatchView.showSimpleInfo("当前任务未提交");
            return;
        }

        else if (jsonObject.getInteger("code") == -1){
            iTextMatchView.showSimpleInfo("已经是第一个任务了");
            return;
        }
        else if (jsonObject.getInteger("code") == 5001){
            iTextMatchView.showCompletedInfo("任务已经完成");
            return;
        }

        JSONObject data = jsonObject.getJSONObject("data");
        iTextMatchView.updateContent(data);
    }

    @Override
    public void submitCallBack(JSONObject jsonObject) {
//        if (jsonObject.getInteger("status") == 0){
//            iTextMatchView.showSubmitInfo("已提交，请进行下一个任务");
//            return;
//        }
        iTextMatchView.showSubmitInfo(jsonObject.toJSONString());
    }
}
