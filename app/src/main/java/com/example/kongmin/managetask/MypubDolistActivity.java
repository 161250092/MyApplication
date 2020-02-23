package com.example.kongmin.managetask;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.kongmin.Constant.Constant;
import com.example.kongmin.view.doTask.categoryMatch.MatchCategoryActivity;
import com.example.kongmin.view.adapter.TaskListItemAdapter;
import com.example.kongmin.myapplication.R;
import com.example.kongmin.view.doTask.sort.DtOneSortActivity;
import com.example.kongmin.view.doTask.oneCategory.OneCategoryActivity;
import com.example.kongmin.view.doTask.categoryRelation.TextCategoryTabActivity;
import com.example.kongmin.pojo.MarkCategory1;
import com.example.kongmin.util.HttpUtil;
import com.example.kongmin.view.doTask.extractText.DoTaskExtractActivity;
import com.example.kongmin.util.SerializableMap;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/*
 * 我发布的任务做任务用户列表页面
 */

public class MypubDolistActivity extends AppCompatActivity {

    private TaskListItemAdapter mAdapter;
    private SwipeMenuListView mListView;

    private LayoutInflater inflater;
    private ArrayList<MarkCategory1> array = new ArrayList<MarkCategory1>();

    private int taskid;
    private int tasktype;




    //传送给做任务的文件内容
    private final SerializableMap fileMap = new SerializableMap();
    private Map<String,Integer> filemap = new LinkedHashMap<String,Integer>();
    //传送给做任务的标签内容
    private final SerializableMap inststrMap = new SerializableMap();
    private Map<String,Integer> inststrmap = new LinkedHashMap<String,Integer>();
    private final SerializableMap item1strMap = new SerializableMap();
    private Map<String,Integer> item1strmap = new LinkedHashMap<String,Integer>();
    private final SerializableMap item2strMap = new SerializableMap();
    private Map<String,Integer> item2strmap = new LinkedHashMap<String,Integer>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypub_dolist);

        mListView = (SwipeMenuListView) findViewById(R.id.listView);

        //接收从上一个页面传递进来的参数
        Intent intent = getIntent();
        taskid = intent.getIntExtra("taskid",0);
        tasktype = intent.getIntExtra("tasktype",0);
        new Thread(runnable).start();
        try {
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        inflater=getLayoutInflater();

        mAdapter = new TaskListItemAdapter(inflater,array);
        mListView.setAdapter(mAdapter);

        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(dp2px(90));
                // set item title
                openItem.setTitle("Open");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        mListView.setMenuCreator(creator);

        // step 2. listener item click event
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                //ApplicationInfo item = mAppList.get(position);
                MarkCategory1 item = array.get(position);
                switch (index) {
                    case 0:
                        // open
                        //open(item);
                        break;
                    case 1:
                        // delete
//					delete(item);
                        //mAppList.remove(position);
                        array.remove(position);
                        mAdapter.notifyDataSetChanged();
                        break;
                }
                return false;
            }
        });

        // set SwipeListener
        mListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });

        // set MenuStateChangeListener
        mListView.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
            @Override
            public void onMenuOpen(int position) {
            }

            @Override
            public void onMenuClose(int position) {
            }
        });

        // other setting
//		listView.setCloseInterpolator(new BounceInterpolator());

        // test item long click
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                Toast.makeText(getApplicationContext(), position + " long click", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), position + " long click", Toast.LENGTH_SHORT).show();

                new Thread(runnable3).start();
                try {
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }


                //Intent intent=new Intent(MypubDolistActivity.this,DotaskExtractActivity.class);
                /*intent.putExtra("ids",array.get(position).getIds());
                String typename = array.get(position).getType();*/
                String type = "mypub";
                //todo position从1开始
                //intent.putExtra("taskid",array.get(position).getIds());
                //intent.putExtra("type",type);
                //docId=8&status=全部&taskId=6&userId=1
                //startActivity(intent);

                if(tasktype==1) {
                    Intent intent = new Intent(MypubDolistActivity.this, DoTaskExtractActivity.class);
                    intent.putExtra("taskid", taskid);
                    intent.putExtra("type", "mypub");
                    //intent.putExtra("fileid", fileidstr);
                    //intent.putExtra("filename", filenamestr);
                    //如果任务类型是信息抽取，发送instance标签
                    Bundle bundle = new Bundle();
                    //将map数据添加到封装的myMap中
                    fileMap.setMap(filemap);
                    inststrMap.setMap(inststrmap);
                    bundle.putSerializable("filemap", fileMap);
                    bundle.putSerializable("instlabel",inststrMap);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else if(tasktype==2){
                    Intent intent = new Intent(MypubDolistActivity.this, OneCategoryActivity.class);
                    intent.putExtra("taskid", taskid);
                    intent.putExtra("type", "mypub");
                    //如果任务类型是文本分类，发送instance标签
                    Bundle bundle = new Bundle();
                    //将map数据添加到封装的myMap中
                    fileMap.setMap(filemap);
                    inststrMap.setMap(inststrmap);
                    bundle.putSerializable("filemap", fileMap);
                    bundle.putSerializable("instlabel",inststrMap);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else if(tasktype==3){
                    Intent intent = new Intent(MypubDolistActivity.this, TextCategoryTabActivity.class);
                    //如果任务类型是文本关系，发送instance，item1和item2标签
                    intent.putExtra("taskid", taskid);
                    intent.putExtra("type", "mypub");
                    //intent.putExtra("fileid", fileidstr);
                    //intent.putExtra("filename", filenamestr);
                    //如果任务类型是信息抽取，发送instance标签
                    Bundle bundle = new Bundle();
                    //将map数据添加到封装的myMap中
                    fileMap.setMap(filemap);
                    inststrMap.setMap(inststrmap);
                    item1strMap.setMap(item1strmap);
                    item2strMap.setMap(item2strmap);
                    bundle.putSerializable("filemap", fileMap);
                    bundle.putSerializable("instlabel",inststrMap);
                    bundle.putSerializable("item1label",item1strMap);
                    bundle.putSerializable("item2label",item2strMap);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else if(tasktype==4){
                    //Intent intent = new Intent(TaskDetailActivity.this, MatchCategoryActivity.class);
                    //如果任务类型是文本配对，则不发送标签
                    Intent intent = new Intent(MypubDolistActivity.this, MatchCategoryActivity.class);
                    intent.putExtra("taskid", taskid);
                    intent.putExtra("type", "mypub");
                    Bundle bundle = new Bundle();
                    //将map数据添加到封装的myMap中
                    fileMap.setMap(filemap);
                    bundle.putSerializable("filemap", fileMap);
                    intent.putExtras(bundle);
                    //intent.putExtra("fileid", fileidstr);
                    //intent.putExtra("filename", filenamestr);
                    startActivity(intent);
                }else{
                    //如果任务类型是文本排序或文本类比排序，则不发送标签
                    Intent intent = new Intent(MypubDolistActivity.this, DtOneSortActivity.class);
                    intent.putExtra("taskid", taskid);
                    intent.putExtra("type", "mypub");
                    Bundle bundle = new Bundle();
                    //将map数据添加到封装的myMap中
                    fileMap.setMap(filemap);
                    bundle.putSerializable("filemap", fileMap);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }






            }
        });

    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // TODO: http request.
            String requestUrl = Constant.TMshowmypubdtlistUrl;
            String params = "?taskId="+taskid+"&page=1&limit=100";
            String result = HttpUtil.requestGet(requestUrl,params);
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
                    //todo 做任务用户名称
                    if(job.get("title")!=null) {
                        //Log.e("title---->", "Post方式请求成功，title--->" + job.get("title").toString());
                        title = job.get("title").toString();
                    }
                    if(job.get("dpercent")!=null) {
                        //Log.e("title---->", "Post方式请求成功，title--->" + job.get("title").toString());
                        typename = job.get("dpercent").toString();
                    }
                    if(job.get("dotime")!=null) {
                        //Log.e("createtime---->", "Post方式请求成功，createtime--->" + job.get("createtime").toString());
                        createtime = job.get("dotime").toString();
                    }
                    if(job.get("dstatus")!=null) {
                        //Log.e("taskcompstatus---->", "Post方式请求成功，taskcompstatus--->" + job.get("taskcompstatus").toString());
                        taskcompstatus = job.get("dstatus").toString();
                    }

                    //用户、状态、进度、时间
                    //MarkCategory1 cun1 = new MarkCategory1(id,title,taskcompstatus,typename,createtime);
                    MarkCategory1 cun1 = new MarkCategory1(id,"test",taskcompstatus,typename,createtime);
                    array.add(cun1);
                    title = "";
                    createtime = "";
                    taskcompstatus = "";
                    typename="";
                }
            }
            Integer num = (Integer)jsonObject.get("count");
            Log.e("count---->", "Post方式请求成功，count--->" + num);
        }
    };


    private Runnable runnable3 = new Runnable() {
        @Override
        public void run() {
            // TODO: http request.
            String requestUrl = Constant.taskdetailUrl;
            //要传递的数
            String params ="?tid="+taskid+"&typeId="+tasktype;
            Log.e("taskid---->", "Post方式请求成功，result--->" + taskid);
            String result = HttpUtil.requestGet(requestUrl,params);
            Log.e("listview---->", "Post方式请求成功，result--->" + result);
            JSONObject jsonObject1= (JSONObject) JSON.parse(result);
            //把字符串转成JSONArray对象
            if(jsonObject1!=null) {
                JSONObject jsonObject = (JSONObject) jsonObject1.get("data");
                int id = 0;
                String filename = "";
                String filetype = "";
                int filesize = 0;
                //首先把字符串转成JSONArray对象
                //JSONArray jsonArray = (JSONArray) JSON.parseArray(jsonObject.getString("documentList"));
                JSONArray jsonArray = (JSONArray)jsonObject.get("documentList");
                if (jsonArray!=null && jsonArray.size() > 0) {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        //遍历jsonarray数组，把每一个对象转成json对象
                        JSONObject job = jsonArray.getJSONObject(i);
                        //得到每个对象中的属性值
                        Log.e("size---->", "Post方式请求成功，size--->" + jsonArray.size());
                        if(job!=null) {
                            if (job.get("did") != null) {
                                //Log.e("did---->", "Post方式请求成功，did--->" + job.get("did").toString());
                                id = (Integer) job.get("did");
                            }
                            if (job.get("filename") != null) {
                                //Log.e("filename---->", "Post方式请求成功，filename--->" + job.get("filename").toString());
                                filename = job.get("filename").toString();
                                filemap.put(filename,id);
                            }
                            if (job.get("filetype") != null) {
                                //Log.e("filetype---->", "Post方式请求成功，filetype--->" + job.get("filetype").toString());
                                filetype = job.get("filetype").toString();
                            }
                            if (job.get("filesize") != null) {
                                //Log.e("filesize---->", "Post方式请求成功，filesize--->" + job.get("filesize").toString());
                                filesize = (Integer) job.get("filesize");
                            }
                        }

                        id = 0;
                        filename = "";
                        filetype = "";
                        filesize = 0;
                    }
                }

                JSONArray instlabelArray = (JSONArray)jsonObject.get("labelList");
                if (instlabelArray!=null && instlabelArray.size() > 0) {
                    for (int i = 0; i < instlabelArray.size(); i++) {
                        //遍历jsonarray数组，把每一个对象转成json对象
                        JSONObject instlabel = instlabelArray.getJSONObject(i);
                        //得到每个对象中的属性值
                        Log.e("size---->", "Post方式请求成功，size--->" + instlabelArray.size());
                        if(instlabel!=null) {
                            if(instlabel.containsKey("labeltype")){
                                String labeltype = (String) instlabel.get("labeltype");
                                Integer labelid = (Integer) instlabel.get("lid");
                                String labelname = (String) instlabel.get("labelname");
                                if(labeltype.equals("instance")){
                                    inststrmap.put(labelname,labelid);
                                }else if(labeltype.equals("item1")){
                                    item1strmap.put(labelname,labelid);
                                }else{
                                    item2strmap.put(labelname,labelid);
                                }
                            }else {
                                //信息抽取或者文本分类的标签
                                if (instlabel.get("labelname") != null) {
                                    //Log.e("did---->", "Post方式请求成功，did--->" + job.get("did").toString());
                                    Integer labelid = (Integer) instlabel.get("lid");
                                    String labelname = (String) instlabel.get("labelname");
                                    inststrmap.put(labelname,labelid);
                                }
                            }
                        }
                    }
                }

            }

        }
    };











    private void delete(ApplicationInfo item) {
        // delete app
        try {
            Intent intent = new Intent(Intent.ACTION_DELETE);
            intent.setData(Uri.fromParts("package", item.packageName, null));
            startActivity(intent);
        } catch (Exception e) {
        }
    }

    private void open(ApplicationInfo item) {
        // open app
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(item.packageName);
        List<ResolveInfo> resolveInfoList = getPackageManager()
                .queryIntentActivities(resolveIntent, 0);
        if (resolveInfoList != null && resolveInfoList.size() > 0) {
            ResolveInfo resolveInfo = resolveInfoList.get(0);
            String activityPackageName = resolveInfo.activityInfo.packageName;
            String className = resolveInfo.activityInfo.name;

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName componentName = new ComponentName(
                    activityPackageName, className);

            intent.setComponent(componentName);
            startActivity(intent);
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_left) {
            mListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
            return true;
        }
        if (id == R.id.action_right) {
            mListView.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
