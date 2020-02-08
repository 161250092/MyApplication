package com.example.kongmin.model;

import android.view.LayoutInflater;

import com.example.kongmin.presenter.MyCallBack;
import com.example.kongmin.view.dotask.MyListView;
import com.example.kongmin.view.rebuild.ListRenderCallBack;

import java.util.zip.Inflater;

public interface ITaskModel {

    public void getTaskByPage(int page, int limit, MyCallBack myCallBack, ListRenderCallBack listRenderCallBack);

    public void reLoadTaskList(MyCallBack myCallBack,ListRenderCallBack listRenderCallBack);

    public void getFirstPage(ListRenderCallBack listRenderCallBack);

    public void getNextPage(ListRenderCallBack listRenderCallBack);
}
