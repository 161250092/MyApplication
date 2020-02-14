package com.example.kongmin.model;

import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.kongmin.network.OkHttpUtil;
import com.example.kongmin.network.Url;
import com.example.kongmin.view.rebuild.InitViewCallBack;
import com.example.kongmin.view.textcategory.DotaskExtractActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ExtractTaskModel implements  IExtractTaskModel {

    private List<String> contents = new ArrayList<String>();
    private List<Integer> contentids = new ArrayList<Integer>();
    private List<Integer> contentindexs = new ArrayList<Integer>();

    //某一个段落的状态
    private List<String> parastatus= new ArrayList<String>();


    //已经标注了的标签
    private ArrayList<Integer> index_begins = new ArrayList<Integer>();
    private ArrayList<Integer> index_ends = new ArrayList<Integer>();
    private ArrayList<Integer> label_ids = new ArrayList<Integer>();



    @Override
    public void getFileContent(int docId, String docStatus, int userId, int taskid,final InitViewCallBack initViewCallBack) {
        OkHttpUtil.sendGetRequest(Url.extradotaskUrl+"?docId="+docId+"&status="+docStatus+"&taskId="+taskid+"&userId="+userId, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(final Call call, Response response) throws IOException {
                String temp = response.body().string();
                JSONObject jsonObject = (JSONObject) JSON.parse(temp);

                JSONArray jsonArray = (JSONArray)jsonObject.get("data");
                int fragmentsize = jsonArray.size();
                //清空content
                contentids.clear();
                contents.clear();
                contentindexs.clear();
                if(jsonArray!=null && jsonArray.size()>0){
                    for(int i=0;i<jsonArray.size();i++){
                        //遍历jsonarray数组，把每一个对象转成json对象
                        JSONObject job = jsonArray.getJSONObject(i);
                        if(job.get("pid")!=null) {
                            int contentid = (Integer)job.get("pid");
                            contentids.add(contentid);
                            Log.e("DotaskExtract---->", "activity中的contentID--->" + contentid);
                        }
                        if(job.get("paracontent")!=null) {
                            String content = (String)job.get("paracontent").toString();
                            contents.add(content);
                            Log.e("DotaskExtract---->", "activity中的content具体内容--->" + content);
                        }
                        if(job.get("paraindex")!=null) {
                            int contentindex = (Integer)job.get("paraindex");
                            contentindexs.add(contentindex);
                            Log.e("DotaskExtract---->", "activity中的content具体内容--->" + contentindex);
                        }
                        if(job.get("dtstatus")!=null) {
                            String parastatusstr = (String)job.get("dtstatus");
                            parastatus.add(parastatusstr);
                            Log.e("DotaskExtract---->", "activity中的contentparastatus-->" + parastatusstr);
                        }else{
                            //不是已完成任务的状态就是空
                            parastatus.add("");
                        }

                        //已经做了的部分
                        JSONArray alreadyDone = (JSONArray)job.get("alreadyDone");

                        ArrayList<String> colorlist = new ArrayList<String>();
                        ArrayList<Integer> beginlist = new ArrayList<Integer>();
                        ArrayList<Integer> endlist = new ArrayList<Integer>();
                        ArrayList<Integer> labelidlist = new ArrayList<Integer>();
                        if(alreadyDone!=null && alreadyDone.size()>0) {
                            for (int j = 0; j < alreadyDone.size(); j++) {
                                //遍历jsonarray数组，把每一个对象转成json对象
                                JSONObject done = alreadyDone.getJSONObject(j);
                                //得到每个对象中的属性值
                                if (done.get("color") != null) {
                                    String color = (String) done.get("color");
                                    colorlist.add(color);
                                }
                                if (done.get("index_begin") != null) {
                                    int index_begin = (Integer) done.get("index_begin");
                                    index_begins.add(index_begin);
                                    beginlist.add(index_begin);
                                }
                                if (done.get("index_end") != null) {
                                    int index_end = (Integer) done.get("index_end");
                                    index_ends.add(index_end);
                                    endlist.add(index_end);
                                    //下载文件对应的抽取内容
                                    //downloadcontent = content.substring(index_begin,index_end);
                                }
                                if (done.get("label_id") != null) {
                                    int label_id = (Integer) done.get("label_id");
                                    label_ids.add(label_id);
                                    labelidlist.add(label_id);
                                    //下载文件对应的标签名称
                                    //downloadlabelname = downloadlabel.get(label_id);
                                    //Log.e("DotaskExtract---->", "activity中的label_id--->" + label_ids.get(j) + "------");
                                }
                                //singlelinecontent = downloadfilename+"\t"+downloadlabelname+"\t"+downloadcontent+"\n";
                               // downloadfilecontent.append(singlelinecontent);
                            }
                            //colorhashMap.put(contentid,colorlist);
                           // beginhashMap.put(contentid,beginlist);
                           // endhashMap.put(contentid,endlist);
                            //labelidhashMap.put(contentid,labelidlist);

                        }
                        initViewCallBack.initView(fragmentsize);
                    }
                }
            }
        });


    }


}
