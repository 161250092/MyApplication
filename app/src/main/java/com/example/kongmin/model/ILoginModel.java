package com.example.kongmin.model;

import com.example.kongmin.presenter.MyCallBack;

public interface ILoginModel {
    //登录：发送网络请求 验证账号信息
    public void login(String user, String pwd, MyCallBack myCallBack);

    public void login(MyCallBack myCallBack);
}
