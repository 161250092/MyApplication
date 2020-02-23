package com.example.kongmin.presenter.impl;

import com.example.kongmin.model.ITaskModel;
import com.example.kongmin.model.TaskModel;
import com.example.kongmin.presenter.ITaskListPresenter;
import com.example.kongmin.presenter.MyCallBack;
import com.example.kongmin.view.iCallBack.ListRenderCallBack;

public class TaskListPresenter implements ITaskListPresenter {
    private ITaskModel iTaskModel;

    public TaskListPresenter(){
        iTaskModel = new TaskModel();
    }

    @Override
    public void getTaskByPage(int page, int limit, MyCallBack myCallBack,ListRenderCallBack listRenderCallBack) {
        iTaskModel.getTaskByPage(page,limit,myCallBack,listRenderCallBack);
    }

    @Override
    public void reLoadTaskList(MyCallBack myCallBack,ListRenderCallBack listRenderCallBack) {
        iTaskModel.reLoadTaskList(myCallBack,listRenderCallBack);
    }
}
