package com.example.kongmin.model;

import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.kongmin.Constant.Constant;
import com.example.kongmin.network.OkHttpUtil;
import com.example.kongmin.pojo.MarkCategory1;
import com.example.kongmin.view.iCallBack.ListRenderCallBack;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MyTaskModel implements  IMyTaskModel {

    private ArrayList<MarkCategory1> markCategory1s;

    public MyTaskModel() {
        markCategory1s = new ArrayList<>();
    }

    @Override
    public void getMyDoingTasks(final ListRenderCallBack listRenderCallBack, int userId) {
        getTasks(listRenderCallBack,userId,false);
    }

    @Override
    public void getMyCompletedTasks(ListRenderCallBack listRenderCallBack,int userId) {
            getTasks(listRenderCallBack,userId,true);
    }

    private void getTasks(final ListRenderCallBack listRenderCallBack, int userId,boolean isCompleted){
        markCategory1s = new ArrayList<>();
        String requestUrl = Constant.selectmydotaskUrl;
        String dtstatus;
        if (!isCompleted)
            dtstatus = "进行中";
        else
            dtstatus = "已完成";

        int page = 1;
        int limit = Integer.MAX_VALUE;
        String params = "?dtstatus="+dtstatus+"&page="+page+"&limit="+limit+"&userId="+userId;

        OkHttpUtil.sendGetRequest(requestUrl + params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                int  id = 0;
                String title = "";
                String createtime = "";
                String taskcompstatus = "";
                String typename="";
                JSONObject jsonObject = JSONObject.parseObject(result);
                //把字符串转成JSONArray对象
                JSONArray jsonArray = (JSONArray)jsonObject.get("data");
                if(jsonArray.size()>0){
                    for(int i=0;i<jsonArray.size();i++){
                        //遍历jsonarray数组，把每一个对象转成json对象
                        JSONObject job = jsonArray.getJSONObject(i);
                        //得到每个对象中的属性值
                        //Log.e("size---->", "Post方式请求成功，size--->" + jsonArray.size());
                        if(job.get("taskId")!=null) {
                            //Log.e("tid---->", "Post方式请求成功，tid--->" + job.get("tid").toString());
                            id =Integer.valueOf(job.get("taskId").toString());
                        }
                        if(job.get("title")!=null) {
                            //Log.e("title---->", "Post方式请求成功，title--->" + job.get("title").toString());
                            title = job.get("title").toString();
                        }
                        if(job.get("typeName")!=null) {
                            //Log.e("title---->", "Post方式请求成功，title--->" + job.get("title").toString());
                            typename = job.get("typeName").toString();
                        }
                        if(job.get("dotime")!=null) {
                            //Log.e("createtime---->", "Post方式请求成功，createtime--->" + job.get("createtime").toString());
                            createtime = job.get("dotime").toString();
                        }
                        if(job.get("dpercent")!=null) {
                            taskcompstatus = job.get("dpercent").toString();
                        }
                        MarkCategory1 cun1 = new MarkCategory1(id,title,taskcompstatus,typename,createtime);;
                        markCategory1s.add(cun1);
                        title = "";
                        createtime = "";
                        taskcompstatus = "";
                        typename="";
                    }
                }
                Integer num = (Integer)jsonObject.get("count");
                Log.e("mwx",markCategory1s.size()+"");
                listRenderCallBack.initListView(markCategory1s);
            }
        });

    }

}
