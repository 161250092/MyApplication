package com.example.kongmin.model;

import com.example.kongmin.presenter.MyCallBack;
import com.example.kongmin.view.iCallBack.ListRenderCallBack;

/**
 * 获取我的任务
 */
public interface IMyTaskModel {

    public void getMyDoingTasks(ListRenderCallBack listRenderCallBack,int userId);

    public void getMyCompletedTasks(ListRenderCallBack listRenderCallBack,int userId);

}
