package com.example.kongmin.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;
import android.widget.AdapterView.OnItemClickListener;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.kongmin.myapplication.R;
import com.example.kongmin.view.adapter.TaskListItemAdapter;
import com.example.kongmin.view.taskDetails.TaskDetailActivity;
import com.example.kongmin.pojo.MarkCategory1;
import com.example.kongmin.util.HttpUtil;

public class DoTaskListActivity extends AppCompatActivity {

    ListView lv;
    LayoutInflater inflater;
    ArrayList<MarkCategory1> array = new ArrayList<MarkCategory1>();
    //MyDataBase mdb;

    private int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_task_list );
        lv=(ListView) findViewById(R.id.lv_bwlList);
        inflater=getLayoutInflater();

        /*initFruits(); // 初始化水果数据
        EdittextAdapter adapter = new EdittextAdapter(EditTextListActivity.this, R.layout.edit_text_list_view, array);
        ListView listView = (ListView) findViewById(R.id.lv_bwlList);
        listView.setAdapter(adapter);*/

        //mdb=new MyDataBase(this);
        //array=mdb.getArray();


        // 获取意图对象
        Intent intent = getIntent();
        //获取传递的值
        type = intent.getIntExtra("position",0);
        type = type+1;
        new Thread(runnable).start();
        try {
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }


        //标题状态类型时间
       /* MarkCategory1 cun1 = new MarkCategory1("标题1", "未完成","标签","2018-11-7 18:09:21");
        array.add(cun1);
        MarkCategory1 cun2 = new MarkCategory1("标题2", "未完成","标签","2018-11-7 18:09:21");
        array.add(cun2);
        MarkCategory1 cun3 = new MarkCategory1("标题3", "未完成","标签","2018-11-7 18:09:21");
        array.add(cun3);*/
        TaskListItemAdapter adapter=new TaskListItemAdapter(inflater,array);
        lv.setAdapter(adapter);
        /*
         * 点击listView里面的item,用来修改日记
         */
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Intent intent=new Intent(getApplicationContext(), TaskDetailActivity.class);
                intent.putExtra("ids",array.get(position).getIds());
                intent.putExtra("type",type);
                //Toast.makeText(getApplicationContext(), "提示：点击窗口外部关闭窗口！"+array.get(position).getIds()+array.get(position).getTitle(),
                //               Toast.LENGTH_SHORT).show();
                startActivity(intent);
                DoTaskListActivity.this.finish();
            }
        });

        /*
         * 长点后来判断是否删除数据
         */
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           final int position, long id) {
                //AlertDialog,来判断是否删除日记。
                new AlertDialog.Builder(DoTaskListActivity.this)
                        .setTitle("删除")
                        .setMessage("是否删除笔记")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //mdb.toDelete(array.get(position).getIds());
                                //array=mdb.getArray();
                                TaskListItemAdapter adapter=new TaskListItemAdapter(inflater,array);
                                lv.setAdapter(adapter);
                            }
                        })
                        .create().show();
                return true;
            }
        });
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // TODO: http request.
            //String requestUrl = "http://10.0.2.2:8080/task/selectAll";
            //String requestUrl = "http://10.0.2.2:8080/task/selectTaskByType";
            String requestUrl = "http://172.20.10.5:8080/task/selectTaskByType";
            String params = "?type="+type;
            String result = HttpUtil.requestPost(requestUrl,params);
            Log.e("listview---->", "Post方式请求成功，result--->" + result);
            int  id = 0;
            String title = "";
            String createtime = "";
            String taskcompstatus = "";

            // 首先把字符串转成 JSONArray 对象
            JSONArray jsonArray = (JSONArray) JSON.parseArray(result);
            if(jsonArray.size()>0){
                for(int i=0;i<jsonArray.size();i++){
                    //遍历jsonarray数组，把每一个对象转成json对象
                    JSONObject job = jsonArray.getJSONObject(i);
                    //得到每个对象中的属性值
                    Log.e("size---->", "Post方式请求成功，size--->" + jsonArray.size());
                    if(job.get("tid")!=null) {
                        //Log.e("tid---->", "Post方式请求成功，tid--->" + job.get("tid").toString());
                        id =Integer.valueOf(job.get("tid").toString());
                    }
                    if(job.get("title")!=null) {
                        //Log.e("title---->", "Post方式请求成功，title--->" + job.get("title").toString());
                        title = job.get("title").toString();
                    }
                    if(job.get("createtime")!=null) {
                        //Log.e("createtime---->", "Post方式请求成功，createtime--->" + job.get("createtime").toString());
                        createtime = job.get("createtime").toString();
                    }
                    if(job.get("taskcompstatus")!=null) {
                        //Log.e("taskcompstatus---->", "Post方式请求成功，taskcompstatus--->" + job.get("taskcompstatus").toString());
                        taskcompstatus = job.get("taskcompstatus").toString();
                    }
                    MarkCategory1 cun1;
                    if(type==1){
                        cun1 = new MarkCategory1(id,title, taskcompstatus,"标签",createtime);
                    }else if(type==2){
                        cun1 = new MarkCategory1(id,title, taskcompstatus,"分类",createtime);
                    }else if(type==3){
                        cun1 = new MarkCategory1(id,title, taskcompstatus,"两个文本",createtime);
                    }else{
                        cun1 = new MarkCategory1(id,title, taskcompstatus,"多文本",createtime);
                    }
                    array.add(cun1);
                    title = "";
                    createtime = "";
                    taskcompstatus = "";
                }


            }
        }
    };





    private void initFruits() {
        MarkCategory1 apple = new MarkCategory1("文本1", "2018-11-7 18:09:21","2018-11-7 18:09:21");
        array.add(apple);
        MarkCategory1 banana = new MarkCategory1("文本2", "2018-11-7 18:09:21","2018-11-7 18:09:22");
        array.add(banana);
        MarkCategory1 orange = new MarkCategory1("文本3", "2018-11-7 18:09:21","2018-11-7 18:09:23");
        array.add(orange);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.edit_text_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Intent intent=new Intent(getApplicationContext(),AddListActivity.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.item2:
                this.finish();
                break;
            default:
                break;
        }
        return true;
    }*/

}
