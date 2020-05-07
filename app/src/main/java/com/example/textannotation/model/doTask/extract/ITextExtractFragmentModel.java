package com.example.textannotation.model.doTask.extract;

import com.example.textannotation.pojo.extract.DoExtractionData;

import okhttp3.RequestBody;

public interface ITextExtractFragmentModel {

    public void getFirstTaskParagraph(int taskId,int docId,int userId);
    public void getLastTask(int taskId,int pid,int userId);
    public void getNextTask(int taskId,int pid,int userId);
    public void saveAnnotationInfo(DoExtractionData doExtractionData);

}
