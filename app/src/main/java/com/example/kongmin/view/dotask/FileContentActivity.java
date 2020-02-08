package com.example.kongmin.view.dotask;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.kongmin.Constant.Constant;
import com.example.kongmin.myapplication.R;
import com.example.kongmin.util.HttpUtil;
import com.example.kongmin.view.adapter.FileContentGridViewAdapter;
import com.example.kongmin.view.adapter.MainPageGridViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class FileContentActivity extends AppCompatActivity {

    private int fileid;
    private String filename;
    private TextView filenametext;
    //private TextView contentview;
    private String filecontent;

    private TextView cancel;

    private String requestUrl;
    private int tasktype;



    private List<String> paragraphist = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_content);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //contentview =  findViewById(R.id.filecontent);
        filenametext = findViewById(R.id.filetitle);

        Intent intent = getIntent();
        tasktype = intent.getIntExtra("tasktype",0);
        fileid = intent.getIntExtra("ids",0);
        filename = intent.getStringExtra("filename");
        //给文件名赋值
        filenametext.setText(filename);
        new Thread(runnable).start();
        try {
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        GridView gridView = findViewById(R.id.paragraph_list);
        gridView.setAdapter(new FileContentGridViewAdapter(paragraphist,this));
        //contentview.setText(filecontent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }




    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // TODO: http request.
            //String requestUrl = "http://10.0.2.2:8080/content/getContent?";
            if(tasktype==1 || tasktype==2){
                requestUrl = Constant.extractfilecontentUrl;
            }else if(tasktype==4){
                requestUrl = Constant.matchfilecontentUrl;
            }else{
                requestUrl = Constant.sortfilecontentUrl;
            }
            //要传递的数
            String params ="?docId="+fileid;
            Log.e("docId---->", "Post方式请求成功，result--->" + fileid);
            String result = HttpUtil.requestGet(requestUrl,params);
            Log.e("listview---->", "Post方式请求成功，result--->" + result);
            JSONObject jsonObject= (JSONObject) JSON.parse(result);
            StringBuffer sb = new StringBuffer();
            if (jsonObject.getString("data")!=null) {
                Log.e("data---->", "Post方式请求成功，data--->" + jsonObject.getString("data"));
                // 首先把字符串转成 JSONArray 对象
                JSONArray jsonArray = (JSONArray) JSON.parseArray(jsonObject.getString("data"));
                if(jsonArray.size()>0){
                    for(int i=0;i<jsonArray.size();i++){
                        //遍历jsonarray数组，把每一个对象转成json对象
                        JSONObject job = jsonArray.getJSONObject(i);
                        //得到每个对象中的属性值
                        Log.e("size---->", "Post方式请求成功，size--->" + jsonArray.size());
                        if(tasktype==1 || tasktype==2) {
                            if (job.get("paracontent") != null) {
                                Log.e("paracontent---->", "Post方式请求成功，paracontent--->" + job.get("paracontent").toString());
                                sb.append(job.get("paracontent").toString() + "\n---------------------------------------------------\n");
                                paragraphist.add(job.get("paracontent").toString());
                            }
                        }else if(tasktype==4){
                            if (job.get("litemcontent") != null) {
                                sb.append(job.get("litemcontent").toString() + "\n---------------------------------------------------\n");
                                paragraphist.add(job.get("litemcontent").toString());
                            }
                        }else{
                            if (job.get("itemcontent") != null) {
                                sb.append(job.get("itemcontent").toString() + "\n---------------------------------------------------\n");
                                paragraphist.add(job.get("itemcontent").toString());
                            }
                        }

                    }
                }
            }
            filecontent = sb.toString();
        }
    };
}
