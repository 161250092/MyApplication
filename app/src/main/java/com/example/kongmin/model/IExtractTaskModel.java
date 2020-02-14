package com.example.kongmin.model;

import com.example.kongmin.view.rebuild.InitViewCallBack;

public interface IExtractTaskModel {

    public void getFileContent(int docId, String docStatus, int userId, int taskid, InitViewCallBack initViewCallBack);

}
