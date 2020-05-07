package com.example.textannotation.model.doTask.match;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.example.textannotation.constant.Constant;
import com.example.textannotation.model.doTask.sort.ITextSortFragmentModel;
import com.example.textannotation.network.OkHttpUtil;
import com.example.textannotation.presenter.doTask.match.ITextMatchPresenter;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TextMatchFragmentModel implements ITextMatchFragmentModel {


    private ITextMatchPresenter iTextMatchPresenter;

    public TextMatchFragmentModel(ITextMatchPresenter iTextMatchPresenter) {
        this.iTextMatchPresenter = iTextMatchPresenter;
    }


    @Override
    public void getFirstTaskParagraph(int taskId, int docId, int userId) {
        String paramUrl = "?taskId="+taskId+"&userId="+userId;
        Log.e("model",Constant.pairingfileUrl+paramUrl);

        OkHttpUtil.sendGetRequest(Constant.pairingfileUrl+paramUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                iTextMatchPresenter.initViewCallBack(null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.e("relation",result);
                JSONObject data = JSONObject.parseObject(result);
                iTextMatchPresenter.initViewCallBack(data);
            }
        });
    }

    @Override
    public void getLastTask(int taskId, int pid, int userId) {
        String paramUrl = "?taskId="+taskId+"&subtaskId="+pid+"&userId="+userId;
        Log.e("model",Constant.lastPairTask+paramUrl);

        OkHttpUtil.sendGetRequest(Constant.lastPairTask+paramUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                iTextMatchPresenter.updateViewCallBack(null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.e("match",result);
                JSONObject data = JSONObject.parseObject(result);
                iTextMatchPresenter.updateViewCallBack(data);
            }
        });


    }

    @Override
    public void getNextTask(int taskId, int pid, int userId) {
        String paramUrl = "?taskId="+taskId+"&subtaskId="+pid+"&userId="+userId;
        Log.e("model",Constant.nextPairTask+paramUrl);

        OkHttpUtil.sendGetRequest(Constant.nextPairTask+paramUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                iTextMatchPresenter.updateViewCallBack(null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.e("match",result);
                JSONObject data = JSONObject.parseObject(result);
                iTextMatchPresenter.updateViewCallBack(data);
            }
        });

    }

    @Override
    public void saveAnnotationInfo(RequestBody requestBody) {
        OkHttpUtil.sendPostRequest(Constant.pairingfileUrl, requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                JSONObject data = JSONObject.parseObject(result);
                iTextMatchPresenter.submitCallBack(data);
            }
        });
    }


}
