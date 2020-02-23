package com.example.kongmin.view.taskDetails;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.kongmin.Constant.Constant;
import com.example.kongmin.myapplication.R;
import com.example.kongmin.network.OkHttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FileContentActivity extends AppCompatActivity {

    private int fileid;
    private String filename;

    private TextView filenametext;
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

        filenametext = findViewById(R.id.filetitle);

        Intent intent = getIntent();
        tasktype = intent.getIntExtra("tasktype",0);
        fileid = intent.getIntExtra("ids",0);
        filename = intent.getStringExtra("filename");
        //给文件名赋值
        filenametext.setText(filename);

        getFileContent();

    }

    private void initList(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.list_item_add,paragraphist);
        ListView listView = findViewById(R.id.paragraph_list);
        listView.setAdapter(adapter);
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


    private void getFileContent(){
        if(tasktype==1 || tasktype==2){
            requestUrl = Constant.extractfilecontentUrl;
        }else if(tasktype==4){
            requestUrl = Constant.matchfilecontentUrl;
        }else{
            requestUrl = Constant.sortfilecontentUrl;
        }
        //要传递的数
        String params ="?docId="+fileid;
        OkHttpUtil.sendGetRequest(requestUrl + params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JSONObject jsonObject= (JSONObject) JSON.parse(response.body().string());
                StringBuffer sb = new StringBuffer();
                if (jsonObject.getString("data")!=null) {
                    Log.e("data---->", "Post方式请求成功，data--->" + jsonObject.getString("data"));
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

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initList();
                    }
                });
            }
        });

    }




}
