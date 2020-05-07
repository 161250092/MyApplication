package com.example.textannotation.view.doTask.textExtract;

import com.alibaba.fastjson.JSONObject;

public interface ITextExtractView {
    public void initView(int pid,String content);
    public void updateContent(int pid,String content);


    public void showSubmitInfo(final String msg);
    public void showCompletedInfo( final String msg);
    public void showExceptionInfo(String msg);
    public void showSimpleInfo(final String msg);

}
