package com.example.textannotation.model.doTask.extract;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.example.textannotation.constant.Constant;
import com.example.textannotation.network.OkHttpUtil;
import com.example.textannotation.pojo.extract.DoExtractionData;
import com.example.textannotation.presenter.doTask.extract.ITextExtractPresenter;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class TextExtractFragmentModel implements ITextExtractFragmentModel {

    private ITextExtractPresenter iTextExtractPresenter;

    public TextExtractFragmentModel(ITextExtractPresenter iTextExtractPresenter) {
        this.iTextExtractPresenter = iTextExtractPresenter;
    }

    @Override
    public void getFirstTaskParagraph(int taskId, int docId, int userId) {
        String paramUrl = "?taskId="+taskId+"&userId="+userId;
        Log.e("model",Constant.extradotaskUrl+paramUrl);

        OkHttpUtil.sendGetRequest(Constant.extradotaskUrl+paramUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                iTextExtractPresenter.initViewCallBack(null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.e("extract",result);
                JSONObject data = JSONObject.parseObject(result);
                iTextExtractPresenter.initViewCallBack(data);
            }
        });


    }

    @Override
    public void getLastTask(int taskId, int pid, int userId) {
        final String requestUrl = Constant.GetLastExtractTask;
        String params = "?taskId="+taskId+"&subtaskId="+pid+"&userId="+userId;
        Log.e("model",requestUrl+params);
        OkHttpUtil.sendGetRequest(requestUrl + params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                iTextExtractPresenter.updateViewCallBack(null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String temp = response.body().string();
                Log.e("tag",temp);
                JSONObject data = JSONObject.parseObject(temp);
                iTextExtractPresenter.updateViewCallBack(data);
            }
        });
    }

    @Override
    public void getNextTask(int taskId, int pid, int userId) {
        final String requestUrl = Constant.GetNextExtractTask;
        String params = "?taskId="+taskId+"&subtaskId="+pid+"&userId="+userId;
        Log.e("model",requestUrl+params);
        OkHttpUtil.sendGetRequest(requestUrl + params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                iTextExtractPresenter.updateViewCallBack(null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String temp = response.body().string();
                Log.e("tag",temp);
                JSONObject data = JSONObject.parseObject(temp);
                iTextExtractPresenter.updateViewCallBack(data);
            }
        });
    }

    @Override
    public void saveAnnotationInfo(DoExtractionData doExtractionData) {

        Log.e("model",doExtractionData.getEntities().size()+" ");
        OkHttpUtil.sendPostRequest(Constant.extradotaskUrl, doExtractionData, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                JSONObject data = JSONObject.parseObject(result);
                iTextExtractPresenter.submitCallBack(data);
            }
        });
    }


}
