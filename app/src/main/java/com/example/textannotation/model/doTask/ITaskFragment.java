package com.example.textannotation.model.doTask;

public interface ITaskFragment {
    //提交标签信息
    public void saveAnnotationInfo();
    //初始化，获取当前应做任务
    public void getCurrentTaskParagraph();
    //做下一个任务
    public void doNextTask();
    //跳过当前任务
    public void passCurrentTask();
    //提交任务错误信息
    public void submitErrors(String text);
    //与其他人的标注信息比较
    public void compareOthersTextAnnotation();
}
