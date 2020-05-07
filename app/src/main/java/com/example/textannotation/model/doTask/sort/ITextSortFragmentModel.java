package com.example.textannotation.model.doTask.sort;

import okhttp3.RequestBody;

public interface ITextSortFragmentModel {

    public void getFirstTaskParagraph(int taskId,int docId,int userId);
    public void getLastTask(int taskId,int pid,int userId);
    public void getNextTask(int taskId,int pid,int userId);
    public void saveAnnotationInfo(RequestBody requestBody);

}
