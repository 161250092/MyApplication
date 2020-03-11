package com.example.textannotation.util;

import android.app.Application;
import com.example.textannotation.pojo.User;

/**
 * 可以比作运行时session,保留用户ID和账户
 */
public class MyApplication extends Application {

    public String appVersion = "v1.0";

    //当前登录用户
    private static User loginUser = new User();

    public User geLogintUser(){
        return loginUser;
    }

    public int getLoginUserId(){
        return loginUser.getUserId();
    }

    public String getLoginUserName(){
        return loginUser.getUserName();
    }

    public void userLogin(User user){
        loginUser.setUserId(user.getUserId());
        loginUser.setUserName(user.getUserName());
    }

    public void userLogout(){
        loginUser = new User();
    }
}