package com.example.textannotation.view.iCallBack;

import com.example.textannotation.pojo.TaskInfo;

import java.util.ArrayList;

public interface ListRenderCallBack {

    public void initListView(ArrayList<TaskInfo> taskInfos);

    public void notifyDataChanged();
}
