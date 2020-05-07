package com.example.textannotation.model.doTask.relation;

import java.util.List;

import okhttp3.RequestBody;

public interface ITextRelationFragmentModel {


    public void getFirstTaskParagraph(int taskId,int docId,int userId);
    public void getLastTask(int taskId,int pid,int userId);
    public void getNextTask(int taskId,int pid,int userId);
    public void saveAnnotationInfo(RequestBody requestBody);

}
