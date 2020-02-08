package com.example.kongmin.view.textcategory;

public interface BaseView {
    //显示等待框
    void showLoading(String msg);
    //隐藏等待框
    void hideLoading();
    //错误信息
    void showErr(String err);


}
