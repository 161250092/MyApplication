package com.example.textannotation.view.doTask.textSort;


import com.alibaba.fastjson.JSONArray;

public interface ITextSortView {

    public void initView(int instanceId, JSONArray jsonArray);
    public void updateContent(int instanceId, JSONArray jsonArray);


    public void showExceptionInfo(String msg);
    public void showSubmitInfo(final String msg);
    public void showSimpleInfo(final String msg);
    public void showCompletedInfo( final String msg);
}
