package com.example.textannotation.view.doTask.textRelation;

import com.alibaba.fastjson.JSONObject;

public interface ITextRelationView {

    public void initView(int instanceId,int itid1,String itemContent1,int itid2,String itemContent2);
    public void updateContent(int instanceId,int itid1,String itemContent1,int itid2,String itemContent2);


    public void showExceptionInfo(String msg);
    public void showSubmitInfo(final String msg);
    public void showSimpleInfo(final String msg);
    public void showCompletedInfo( final String msg);
}
