package com.example.kongmin.model;

import com.example.kongmin.presenter.MyCallBack;

public interface IRegisterModel {

    public void registerNewUser(String username, String password, String email, MyCallBack myCallBack);
}
