package com.example.kongmin.presenter.impl;

import com.example.kongmin.model.IRegisterModel;
import com.example.kongmin.model.RegisterModel;
import com.example.kongmin.presenter.IRegisterPresenter;
import com.example.kongmin.presenter.MyCallBack;

public class RegisterPresenter implements IRegisterPresenter {

    IRegisterModel iRegisterModel;

    public RegisterPresenter(RegisterModel registerModel){
        iRegisterModel =  registerModel;
    }

    @Override
    public void registerNewUser(String username, String password, String email, MyCallBack myCallBack) {
        iRegisterModel.registerNewUser(username,password,email,myCallBack);
    }
}
