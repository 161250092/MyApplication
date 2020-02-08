package com.example.kongmin.model;


import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.kongmin.network.OkHttpUtil;
import com.example.kongmin.network.Url;
import com.example.kongmin.pojo.UserEntity;
import com.example.kongmin.presenter.MyCallBack;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterModel implements IRegisterModel {

    public static final MediaType JSONType = MediaType.parse("application/json");
    @Override
    public void registerNewUser(String username, String password, String email, final MyCallBack myCallBack) {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(email);
        userEntity.setUsername(username);
        userEntity.setPassword(password);
        userEntity.setBirthday("2000-01-01 12:00:00");
        userEntity.setSex("unknown");
        userEntity.setJob("unknown");

        RequestBody requestBody = RequestBody.create(JSONType, new Gson().toJson(userEntity));
        //RequestBody.
        OkHttpUtil.sendPostRequest(Url.userRegisterUrl, requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                myCallBack.onFailure("注册时网络异常");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String loginstr = response.body().string();
                Log.d("Register_Model", "string from server: " + loginstr + "----test");
                JSONObject jsonObject= (JSONObject) JSON.parse(loginstr);
                Integer data = (Integer) jsonObject.getInteger("status");
                if(data.toString().equals("200")){
                    myCallBack.onSuccess();
                }else{
                    //该邮箱已经被注册("1003")或注册失败("1004")
                    String loginres = jsonObject.getString("msg");
                    myCallBack.onFailure(loginres);
                }
            }
        });
    }


}
