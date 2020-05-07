package com.example.textannotation.model.doTask.match;

import okhttp3.RequestBody;

public interface ITextMatchFragmentModel {

    public void getFirstTaskParagraph(int taskId,int docId,int userId);
    public void getLastTask(int taskId,int pid,int userId);
    public void getNextTask(int taskId,int pid,int userId);
    public void saveAnnotationInfo(RequestBody requestBody);


}
