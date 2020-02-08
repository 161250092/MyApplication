package com.example.kongmin.presenter;

import com.example.kongmin.view.rebuild.ListRenderCallBack;

public interface ITaskListPresenter {
    public void getTaskByPage(int page, int limit, MyCallBack myCallBack,ListRenderCallBack listRenderCallBack);

    public void reLoadTaskList(MyCallBack myCallBack,ListRenderCallBack listRenderCallBack);
}
