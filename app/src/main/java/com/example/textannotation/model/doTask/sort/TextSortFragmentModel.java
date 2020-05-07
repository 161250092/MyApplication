package com.example.textannotation.model.doTask.sort;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.example.textannotation.constant.Constant;
import com.example.textannotation.network.OkHttpUtil;
import com.example.textannotation.presenter.doTask.sort.ITextSortPresenter;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TextSortFragmentModel implements ITextSortFragmentModel {

    private ITextSortPresenter iTextSortPresenter;

    public TextSortFragmentModel(ITextSortPresenter iTextSortPresenter) {
        this.iTextSortPresenter = iTextSortPresenter;
    }

    @Override
    public void getFirstTaskParagraph(int taskId, int docId, int userId) {
        String paramUrl = "?taskId="+taskId+"&userId="+userId;
        Log.e("model",Constant.DotaskOneSortRequestUrl+paramUrl);
        OkHttpUtil.sendGetRequest(Constant.DotaskOneSortRequestUrl+paramUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.e("relation",result);
                JSONObject data = JSONObject.parseObject(result);
                iTextSortPresenter.initViewCallBack(data);
            }
        });


    }

    @Override
    public void getLastTask(int taskId, int pid, int userId) {
        final String requestUrl = Constant.lastOneSortUrl;
        String params = "?taskId="+taskId+"&subtaskId="+pid+"&userId="+userId;
        Log.e("model",requestUrl+params);
        OkHttpUtil.sendGetRequest(requestUrl + params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                iTextSortPresenter.updateViewCallBack(null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String temp = response.body().string();
                Log.e("tag",temp);
                JSONObject data = JSONObject.parseObject(temp);
                iTextSortPresenter.updateViewCallBack(data);
            }
        });
    }

    @Override
    public void getNextTask(int taskId, int pid, int userId) {
        final String requestUrl = Constant.nextOneSortUrl;
        String params = "?taskId="+taskId+"&subtaskId="+pid+"&userId="+userId;
        Log.e("model",requestUrl+params);
        OkHttpUtil.sendGetRequest(requestUrl + params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                iTextSortPresenter.updateViewCallBack(null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String temp = response.body().string();
                Log.e("tag",temp);
                JSONObject data = JSONObject.parseObject(temp);
                iTextSortPresenter.updateViewCallBack(data);
            }
        });
    }

    @Override
    public void saveAnnotationInfo(RequestBody requestBody) {
        OkHttpUtil.sendPostRequest(Constant.DotaskOneSortUrl, requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                JSONObject data = JSONObject.parseObject(result);
                iTextSortPresenter.submitCallBack(data);
            }
        });

    }
}
