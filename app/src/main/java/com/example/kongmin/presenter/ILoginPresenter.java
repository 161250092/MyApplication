package com.example.kongmin.presenter;

public interface ILoginPresenter {

    public void login(String username,String password,MyCallBack myCallBack);

    public void login(MyCallBack myCallBack);

}
