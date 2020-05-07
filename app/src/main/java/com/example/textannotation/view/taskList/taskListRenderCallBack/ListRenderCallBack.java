package com.example.textannotation.view.taskList.taskListRenderCallBack;

import com.example.textannotation.pojo.task.TaskInfo;

import java.util.ArrayList;

public interface ListRenderCallBack {

    public void initListView(ArrayList<TaskInfo> taskInfos);

    public void notifyDataChanged();
}
