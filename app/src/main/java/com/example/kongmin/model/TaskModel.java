package com.example.kongmin.model;

import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.kongmin.Constant.Constant;
import com.example.kongmin.network.OkHttpUtil;
import com.example.kongmin.network.Url;

import com.example.kongmin.pojo.MarkCategory1;
import com.example.kongmin.presenter.MyCallBack;
import com.example.kongmin.view.rebuild.ListRenderCallBack;

import java.io.IOException;
import java.util.ArrayList;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class TaskModel implements  ITaskModel {

    private int taskNum = 0;
    private ArrayList<MarkCategory1> markCategory1s;

    public TaskModel(){
        this.markCategory1s = new ArrayList<>();
    }

    private boolean isTaskAllGot = false;
    @Override
    public void getTaskByPage(int page, int limit, final MyCallBack myCallBack, final ListRenderCallBack listRenderCallBack) {
        Log.e("TASKMODEL",page+" "+limit);
        if (isTaskAllGot)
            return;
        OkHttpUtil.sendGetRequest(Url.selectalltaskUrl+"?page="+page+"&limit="+limit, new Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
                myCallBack.onFailure("网络异常");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData = response.body().string();
                ArrayList<MarkCategory1> temp = TaskModel.this.resolveJSONData(jsonData);
                TaskModel.this.markCategory1s.addAll(temp);
                Log.e("TASKMODEL","size: "+markCategory1s.size());
                listRenderCallBack.notifyDataChanged();
               // listRenderCallBack.renderListView(TaskModel.this.markCategory1s);
                if (markCategory1s.size() >= taskNum)
                    isTaskAllGot = true;
            }
        });
    }

    @Override
    public void reLoadTaskList(MyCallBack myCallBack,ListRenderCallBack listRenderCallBack) {
        this.markCategory1s = new ArrayList<>();
        this.getTaskByPage(Constant.PageIndex,Constant.PageCapacity,myCallBack,listRenderCallBack);
    }

    private int mPageIndex = 1;
    @Override
    public void getFirstPage(final ListRenderCallBack listRenderCallBack) {
        mPageIndex = 1;
        OkHttpUtil.sendGetRequest(Url.selectalltaskUrl+"?page="+Constant.PageIndex+"&limit="+Constant.PageCapacity, new Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData = response.body().string();
                markCategory1s = new ArrayList<>();
                ArrayList<MarkCategory1> temp = resolveJSONData(jsonData);
                markCategory1s.addAll(temp);
                Log.e("TASKMODEL","size: "+markCategory1s.size());
                listRenderCallBack.initListView(markCategory1s);
            }
        });
    }


    @Override
    public void getNextPage(final ListRenderCallBack listRenderCallBack) {
        mPageIndex++;
        if (isTaskAllGot)
            return;
        OkHttpUtil.sendGetRequest(Url.selectalltaskUrl+"?page="+mPageIndex+"&limit="+Constant.PageCapacity, new Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData = response.body().string();
                ArrayList<MarkCategory1> temp = TaskModel.this.resolveJSONData(jsonData);
                markCategory1s.addAll(temp);
                Log.e("TASKMODEL","index "+mPageIndex+" size:"+markCategory1s.size());
                listRenderCallBack.notifyDataChanged();
                if (markCategory1s.size() >= taskNum)
                    isTaskAllGot = true;
            }
        });
    }

    //解析JSON数据
    private ArrayList<MarkCategory1> resolveJSONData(String jsonData){
        ArrayList<MarkCategory1> res = new ArrayList<>();
        int  id = 0;
        String title = "";
        String createtime = "";
        String taskcompstatus = "";
        String typename="";
        JSONObject jsonObject = JSONObject.parseObject(jsonData);
        taskNum = Integer.valueOf(jsonObject.get("count").toString());

        JSONArray jsonArray = (JSONArray)jsonObject.get("data");
        if(jsonArray!=null && jsonArray.size()>0){
            for(int i=0;i<jsonArray.size();i++){
                JSONObject job = jsonArray.getJSONObject(i);
                if(job.get("tid")!=null) {
                    id =Integer.valueOf(job.get("tid").toString());
                }
                if(job.get("title")!=null) {
                    title = job.get("title").toString();
                }
                if(job.get("typeName")!=null) {
                    typename = job.get("typeName").toString();
                }
                if(job.get("createtime")!=null) {
                    createtime = job.get("createtime").toString();
                }
                if(job.get("taskcompstatus")!=null) {
                    taskcompstatus = job.get("taskcompstatus").toString();
                }
                res.add(new MarkCategory1(id,title,taskcompstatus,typename,createtime));
            }
        }
        return res;
    }

}
