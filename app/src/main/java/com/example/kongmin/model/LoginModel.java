package com.example.kongmin.model;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.kongmin.network.OkHttpUtil;
import com.example.kongmin.network.Url;
import com.example.kongmin.pojo.User;
import com.example.kongmin.presenter.MyCallBack;
import com.example.kongmin.util.MyApplication;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginModel implements ILoginModel {
    private MyApplication mApplication;
    private int userId = -1;
    private String userName = "unknown";
    private String password = "unknown";


    /**存储账号和密码
     * todo: 密码加密
     */
    private String USERNAME_DATABASE = "username";
    private SharedPreferences mUserNameData;

    private String PASSWORD_DATABASE = "password";
    private SharedPreferences mPwdData;

    public LoginModel(MyApplication mApplication,Activity activity){
      this.mApplication = mApplication;
      this.mUserNameData = activity.getSharedPreferences(USERNAME_DATABASE,activity.MODE_PRIVATE);
      this.mPwdData = activity.getSharedPreferences(PASSWORD_DATABASE, activity.MODE_PRIVATE);
    }

    @Override
    public void login(String user, String pwd, final MyCallBack callBack) {
        Log.e("LOGIN_MODEL", "do login "+user+" "+pwd);
        this.userName = user;
        this.password = pwd;

        RequestBody requestBody = new FormBody.Builder()
                .add("username", user)
                .add("password", pwd)
                .build();

        OkHttpUtil.sendPostRequest(Url.userloginUrl, requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onFailure("login fail");
            }

            @Override
            public void onResponse(final Call call, Response response) throws IOException {

                String loginstr = response.body().string();
                Log.e("LOGIN_MODEL", "string from server: " + loginstr + "----test");
                JSONObject jsonObject= (JSONObject)JSON.parse(loginstr);
                Integer data = (Integer) jsonObject.getInteger("status");

                if(data.toString().equals("200")){
                    JSONObject userdata= (JSONObject)jsonObject.get("data");
                    if (userdata.get("userId") != null){
                        userId = (Integer)userdata.get("userId");
                        Log.e("LOGIN_MODEL", "userId--->" + userId+"  "+ userName);
                    }
                    LoginModel.this.storeLoginInfo(userId,userName,password);
                    callBack.onSuccess();
                }else{
                   callBack.onFailure("登录失败");
                }
            }
        });

    }

    @Override
    public void login(MyCallBack myCallBack) {
        userName = mUserNameData.getString(USERNAME_DATABASE,"unknown");
        password = mPwdData.getString(PASSWORD_DATABASE,"unknown");
        if (userName.equals("unknown")&&userName.equals(password))
           // myCallBack.onFailure("请登录");
            return;
        else
            this.login(userName,password,myCallBack);
    }


    public void storeLoginInfo(int userId,String userName,String password){
        User user = new User();
        user.setUserId(userId);
        user.setUserName(userName);
        // 将登录用户信息保存到Application对象中
        mApplication.userLogin(user);

        //将数据存入本地
        SharedPreferences.Editor userNameEditor = mUserNameData.edit();
        userNameEditor.putString("username",userName);
        userNameEditor.apply();

        SharedPreferences.Editor passwordEditor = mPwdData.edit();
        passwordEditor.putString("password",password);
        passwordEditor.apply();
    }




}
