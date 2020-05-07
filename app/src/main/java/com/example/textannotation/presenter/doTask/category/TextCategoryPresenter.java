package com.example.textannotation.presenter.doTask.category;

import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.textannotation.model.doTask.category.ITextCategoryFragmentModel;
import com.example.textannotation.model.doTask.category.TextCategoryFragmentModel;
import com.example.textannotation.view.doTask.textCategory.ITextCategoryView;


import java.util.List;

public class TextCategoryPresenter implements ITextCategoryPresenter  {

    private ITextCategoryView iTextCategoryView;
    private ITextCategoryFragmentModel iTextCategoryFragmentModel;

    public TextCategoryPresenter(ITextCategoryView iTextCategoryView){
        this.iTextCategoryView = iTextCategoryView;
        iTextCategoryFragmentModel = new TextCategoryFragmentModel(this);
    }

    /**
     * 向model层请求数据
     * @param taskId
     * @param docId
     * @param userId
     */
    @Override
    public void loadDataAndInitView(int taskId, int docId, int userId) {
        iTextCategoryFragmentModel.getFirstTaskParagraph(taskId,docId,userId);
    }

    @Override
    public void getLastTask(int taskId, int pid, int userId) {
        iTextCategoryFragmentModel.getLastTask(taskId,pid,userId);
    }

    @Override
    public void getNextTask(int taskId, int pid, int userId) {
        Log.e("presenter","getNext");
        iTextCategoryFragmentModel.getNextTask(taskId,pid,userId);
    }


    @Override
    public void saveAnnotationInfo(List<Integer> labelids, int pid, int taskId, int docId, int userId) {
        if (labelids.size() == 0) {
            iTextCategoryView.showExceptionInfo("未选择标签");
            return;
        }
        iTextCategoryFragmentModel.saveAnnotationInfo(labelids,pid,taskId,docId,userId);
    }



    /**
     * call back to view
     * @param jsonObject
     */
    @Override
    public void initViewCallBack(JSONObject jsonObject) {

        Log.e("presenter",jsonObject.toJSONString());
        if (jsonObject == null) {
            iTextCategoryView.showExceptionInfo("网络异常");
            return;
        }
        Log.e("presenter",jsonObject.toJSONString());
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        if (jsonArray == null ||jsonArray.size() == 0){
            iTextCategoryView.showCompletedInfo("任务已经完成");
        }
        JSONObject data = (JSONObject)jsonArray.get(0);
        if (data == null){
            iTextCategoryView.showCompletedInfo("任务已经完成");
            return;
        }
        Log.e("presenter",data.toJSONString());
        iTextCategoryView.initView(data);
    }

    @Override
    public void updateViewCallBack(JSONObject jsonObject) {

        if (jsonObject == null) {
            iTextCategoryView.showExceptionInfo("网络异常");
            return;
        }
        else if (jsonObject.getInteger("code") == 5000){
            iTextCategoryView.showSimpleInfo("当前任务未提交");
            return;
        }
        else if (jsonObject.getInteger("code") == -1){
            iTextCategoryView.showSimpleInfo("已经是第一个任务");
            return;
        }
        else if (jsonObject.getJSONObject("data") == null){
            Log.e("presenter UPDATE",jsonObject.toJSONString());
            iTextCategoryView.showCompletedInfo("任务已完成");
            return;
        }

        iTextCategoryView.updateContent(jsonObject);
    }

    @Override
    public void submitCallBack(JSONObject jsonObject) {
        if (jsonObject.getInteger("status") == 0){
            iTextCategoryView.showSubmitInfo("提交成功，请进入下一个任务");
            return;
        }
        iTextCategoryView.showSubmitInfo(jsonObject.toJSONString());
    }


}
