package com.example.kongmin.model;

import com.example.kongmin.presenter.MyCallBack;
import com.example.kongmin.view.iCallBack.ListRenderCallBack;

public interface ITaskModel {

    public void getTaskByPage(int page, int limit, MyCallBack myCallBack, ListRenderCallBack listRenderCallBack);

    public void reLoadTaskList(MyCallBack myCallBack,ListRenderCallBack listRenderCallBack);

    public void getFirstPage(ListRenderCallBack listRenderCallBack);

    public void getNextPage(ListRenderCallBack listRenderCallBack);
}
