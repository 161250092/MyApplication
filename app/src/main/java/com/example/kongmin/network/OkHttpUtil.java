package com.example.kongmin.network;

import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OkHttpUtil {


    private static OkHttpClient mOkHttpClient;

    static {
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10,TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS)
                .build();
    }

    //get 请求
    public static void sendGetRequest(String address, Callback callback){
        Request request = new Request.Builder().url(address).build();
        mOkHttpClient.newCall(request).enqueue(callback);
    }

    //post 请求
    public static void sendPostRequest(String address, RequestBody requestBody, Callback callback){
        Request request = new Request.Builder()
                .url(address)
                .post(requestBody)
                .build();
        mOkHttpClient.newCall(request).enqueue(callback);
    }




}
