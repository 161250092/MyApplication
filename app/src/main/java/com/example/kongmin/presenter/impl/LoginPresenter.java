package com.example.kongmin.presenter.impl;

import com.example.kongmin.model.LoginModel;
import com.example.kongmin.presenter.ILoginPresenter;
import com.example.kongmin.presenter.MyCallBack;

public class LoginPresenter implements ILoginPresenter {
    private LoginModel loginModel;


    public LoginPresenter(LoginModel loginModel){
        this.loginModel = loginModel;
    }

    @Override
    public void login(String username, String password,MyCallBack myCallBack) {
        loginModel.login(username,password,myCallBack);}


    @Override
    public void login(MyCallBack myCallBack) {
        loginModel.login(myCallBack);
    }
}
