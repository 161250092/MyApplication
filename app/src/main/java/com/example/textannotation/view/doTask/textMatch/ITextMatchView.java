package com.example.textannotation.view.doTask.textMatch;

import com.alibaba.fastjson.JSONObject;

public interface ITextMatchView {


    public void initView(JSONObject jsonObject);
    public void updateContent(JSONObject jsonObject);


    public void showExceptionInfo(String msg);
    public void showSubmitInfo(final String msg);
    public void showSimpleInfo(final String msg);
    public void showCompletedInfo( final String msg);


}
