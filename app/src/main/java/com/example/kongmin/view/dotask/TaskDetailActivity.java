package com.example.kongmin.view.dotask;

import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.kongmin.Constant.Constant;
import com.example.kongmin.view.DoTask2Activity;
import com.example.kongmin.myapplication.R;
import com.example.kongmin.view.ShowFileAdapter;
import com.example.kongmin.view.sort.DtOneSortActivity;
import com.example.kongmin.view.textcategory.DotaskExtractActivity;
import com.example.kongmin.pojo.ShowFile;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View;
import android.content.Intent;
import com.example.kongmin.util.*;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.view.View.OnClickListener;
import com.example.kongmin.view.textcategory.OneCategoryActivity;
import com.example.kongmin.view.textcategory.TextCategoryTabActivity;
import com.hb.dialog.dialog.LoadingDialog;

/**
 * Created by kongmin on 2019/02/11.
 * 单个任务详情界面
 */
public class TaskDetailActivity extends AppCompatActivity {

    private TextView title;
    private TextView type;
    private TextView deadline;
    private TextView createtime;
    private TextView pubUser;
    private TextView content;
    private TextView dotask;
    //和设置标签相关的
    private FlowGroupView instancelabel;
    private FlowGroupView item1label;
    private FlowGroupView item2label;

    private LinearLayout instlv;
    private LinearLayout item1lv;
    private LinearLayout item2lv;
    private LinearLayout llybuttom;

    private int taskid;
    private String titlestr;
    private String tasktypestr;
    private String deadlinestr;
    private String createtimestr;
    private String descriptionstr;
    private String pubUserNamestr;

    private Button showFileListButton;

    private List<TextView> instlabellist = new ArrayList<>();
    private List<TextView> item1labellist = new ArrayList<>();
    private List<TextView> item2labellist = new ArrayList<>();

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

    private int tasktype = 0;
    private int userid = 0;

    //和文件相关的
    private ListView lv;
    private LayoutInflater inflater;
    private ArrayList<ShowFile> array = new ArrayList<ShowFile>();

    //做任务相关的文件
    private int fileidstr;
    private String filenamestr;

    //信息抽取标签颜色
    private ArrayList<String> colors = new ArrayList<>();
    private SerializableSortMap colorsMap = new SerializableSortMap();
    private Map<Integer,String> colormap = new LinkedHashMap<Integer,String>();

    private LoadingDialog loadingDialog;

    private static final String tag = "TaskDetailActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_deatil);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        title=(TextView) findViewById(R.id.tasktitle);
        type=(TextView) findViewById(R.id.type);
        deadline=(TextView) findViewById(R.id.deadline);
        createtime=(TextView) findViewById(R.id.createtime);
        pubUser=(TextView) findViewById(R.id.puser);
        content=(TextView) findViewById(R.id.taskcontent);

        instancelabel = (FlowGroupView)findViewById(R.id.instancelabel);
        item1label = (FlowGroupView)findViewById(R.id.item1label);
        item2label = (FlowGroupView)findViewById(R.id.item2label);

        instlv = (LinearLayout)findViewById(R.id.instlv);
        item1lv = (LinearLayout)findViewById(R.id.item1lv);
        item2lv = (LinearLayout)findViewById(R.id.item2lv);
        llybuttom = (LinearLayout)findViewById(R.id.llybuttom);
        llybuttom.setVisibility(View.GONE);
        //做任务
        dotask=(TextView) findViewById(R.id.dotask);

        showFileListButton = findViewById(R.id.show_file_list);

        loadingDialog = new LoadingDialog(this);

        //接收从上一个页面传递进来的参数
        Intent intent = getIntent();
        taskid = intent.getIntExtra("ids",0);
        tasktype = intent.getIntExtra("type",0);
        //Toast.makeText(getApplicationContext(), "提示:"+taskid+" "+tasktype, Toast.LENGTH_SHORT).show();
        //如果任务类型是信息抽取或文本分类，隐藏item
        if(tasktype==1 || tasktype==2){
            item1lv.setVisibility(View.GONE);
            item2lv.setVisibility(View.GONE);
        }else if(tasktype==4 || tasktype==5 || tasktype==6){
        //如果任务类型是文本配对或文本排序或文本类比排序，隐藏instance和item
            instlv.setVisibility(View.GONE);
            item1lv.setVisibility(View.GONE);
            item2lv.setVisibility(View.GONE);
        }
        //如果任务类型是文本关系，则都显示


        new Thread(runnable).start();
        try {
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        title.setText(titlestr);
        type.setText(tasktypestr);
        deadline.setText(deadlinestr);
        createtime.setText(createtimestr);
        pubUser.setText(pubUserNamestr);
        content.setText(descriptionstr);
        //content.setText("标注出表格中对应的信息的值\n值\n值\n值\n值\n值\n" +
        //        "值\n值\n值\n值\n值\n值\n值\n值\n值\n值\n值\n值\n值\n值\n值\n值\n值\n值\n");1
        lv=(ListView) findViewById(R.id.lv_bwlList);
        inflater=getLayoutInflater();


        ShowFileAdapter adapter=new ShowFileAdapter(inflater,array);
        lv.setAdapter(adapter);
        //setListViewHeightBasedOnChildren(lv);

       dotask.setOnClickListener(new OnClickListener() {

           @Override
           public void onClick(View v) {
               if(tasktype==1) {
                   Intent intent = new Intent(TaskDetailActivity.this, DotaskExtractActivity.class);
                   intent.putExtra("taskid", taskid);
                   intent.putExtra("type", "dotask");
                   //intent.putExtra("fileid", fileidstr);
                   //intent.putExtra("filename", filenamestr);
                   //如果任务类型是信息抽取，发送instance标签
                   Bundle bundle = new Bundle();
                   //将map数据添加到封装的myMap中
                   fileMap.setMap(filemap);
                   inststrMap.setMap(inststrmap);
                   bundle.putSerializable("filemap", fileMap);
                   bundle.putSerializable("instlabel",inststrMap);
                   bundle.putStringArrayList("colors",colors);
                   colorsMap.setMap(colormap);
                   bundle.putSerializable("colormap",colorsMap);
                   intent.putExtras(bundle);
                   Log.e(tag,"----------------");
                   startActivity(intent);

               }else if(tasktype==2){
                   Intent intent = new Intent(TaskDetailActivity.this, OneCategoryActivity.class);
                   intent.putExtra("taskid", taskid);
                   intent.putExtra("type", "dotask");
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
                   Intent intent = new Intent(TaskDetailActivity.this, TextCategoryTabActivity.class);
                   //如果任务类型是文本关系，发送instance，item1和item2标签
                   intent.putExtra("taskid", taskid);
                   intent.putExtra("type", "dotask");
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
                   Intent intent = new Intent(TaskDetailActivity.this, DoTask2Activity.class);
                   intent.putExtra("taskid", taskid);
                   intent.putExtra("type", "dotask");
                   intent.putExtra("tasktype",tasktypestr);
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
                   Intent intent = new Intent(TaskDetailActivity.this, DtOneSortActivity.class);
                   intent.putExtra("taskid", taskid);
                   intent.putExtra("type", "dotask");
                   Bundle bundle = new Bundle();
                   //将map数据添加到封装的myMap中
                   fileMap.setMap(filemap);
                   bundle.putSerializable("filemap", fileMap);
                   intent.putExtras(bundle);
                   startActivity(intent);
               }
              }
         });



        /*
         * 点击listView里面的item,用来修改日记
         */
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                //Intent intent=new Intent(getApplicationContext(), FileDetailActivity.class);
                loadingDialog.setMessage("loading");
                loadingDialog.show();
                Intent intent=new Intent(getApplicationContext(), FileContentActivity.class);
                intent.putExtra("tasktype",tasktype);
                intent.putExtra("ids",array.get(position).getFileid());
                intent.putExtra("filename",array.get(position).getName());
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadingDialog.dismiss();
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
            //String requestUrl = "http://10.0.2.2:8080/task/selectTaskDocumentByTaskid?";
            //String requestUrl = "http://172.20.10.5:8080/task/selectTaskDocumentByTaskid?";
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
                if(jsonObject!=null){
                    if (jsonObject.getString("title") != null) {
                        titlestr = jsonObject.getString("title");
                    }
                    if (jsonObject.getString("description") != null) {
                        descriptionstr = jsonObject.getString("description");
                    }
                    if (jsonObject.getString("typeName") != null) {
                        tasktypestr = jsonObject.getString("typeName");
                    }
                    if (jsonObject.getString("deadline") != null) {
                        deadlinestr = jsonObject.getString("deadline");
                    }
                    if (jsonObject.getString("createtime") != null) {
                        createtimestr = jsonObject.getString("createtime");
                    }
                    if (jsonObject.getString("taskcompstatus") != null) {
                        //createtimestr = jsonObject.getString("taskcompstatus");
                    }
                    if (jsonObject.getString("userId") != null) {
                        //createtimestr = jsonObject.getString("userId");
                        userid = jsonObject.getInteger("userId");
                        Log.e("listview---->", "Get方式请求成功，userid--->" + userid);
                    }
                    if (jsonObject.getString("pubUserName") != null) {
                        pubUserNamestr = jsonObject.getString("pubUserName");
                    }
                }
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
                            //ShowFile showFile1 = new ShowFile(id, filename + filetype, filesize + "kb", R.drawable.file);
                            ShowFile showFile1 = new ShowFile(id, filename, filesize + "b", R.drawable.file);
                            array.add(showFile1);
                            fileidstr = id;
                            //filenamestr = filename + filetype;
                            filenamestr = filename;
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
                                    addinstTextView(labelname);
                                }else if(labeltype.equals("item1")){
                                    item1strmap.put(labelname,labelid);
                                    additem1TextView(labelname);
                                }else{
                                    item2strmap.put(labelname,labelid);
                                    additem2TextView(labelname);
                                }
                            }else {
                                //信息抽取或者文本分类的标签
                                if (instlabel.get("labelname") != null) {
                                    //Log.e("did---->", "Post方式请求成功，did--->" + job.get("did").toString());
                                    Integer labelid = (Integer) instlabel.get("lid");
                                    String labelname = (String) instlabel.get("labelname");
                                    inststrmap.put(labelname,labelid);
                                    if(instlabel.get("color")!=null){
                                        String color = (String)instlabel.get("color");
                                        colors.add(color);
                                        colormap.put(labelid,color);
                                    }
                                    addinstTextView(labelname);
                                }
                            }
                        }
                    }
                }

            }

        }
    };

    public void showTaskList(View view){
        String buttonStr = showFileListButton.getText().toString();
        if ( buttonStr.equals("展示文件") ) {
            llybuttom.setVisibility(View.VISIBLE);
            showFileListButton.setText("隐藏文件");
        }
        else {
            showFileListButton.setText("展示文件");
            llybuttom.setVisibility(View.GONE);
        }
    }

    //和设置标签相关的
    /**
     * instence动态添加布局
     * @param str
     */
    private void addinstTextView(String str) {
        TextView child = new TextView(TaskDetailActivity.this);
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
        params.setMargins(15, 15, 15, 15);
        child.setLayoutParams(params);
        child.setBackgroundResource(R.drawable.line_rect_huise);
        child.setText(str);
        child.setPadding(20,5,20,5);
        child.setTextColor(Color.BLACK);
        instlabellist.add(child);
        instancelabel.addView(child);
    }
    /**
     * item1动态添加布局
     * @param str
     */
    private void additem1TextView(String str) {
        TextView child = new TextView(TaskDetailActivity.this);
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
        params.setMargins(15, 15, 15, 15);
        child.setLayoutParams(params);
        child.setBackgroundResource(R.drawable.line_rect_huise);
        child.setText(str);
        child.setPadding(20,5,20,5);
        child.setTextColor(Color.BLACK);
        item1labellist.add(child);
        item1label.addView(child);
    }
    /**
     * item2动态添加布局
     * @param str
     */
    private void additem2TextView(String str) {
        TextView child = new TextView(TaskDetailActivity.this);
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
        params.setMargins(15, 15, 15, 15);
        child.setLayoutParams(params);
        child.setBackgroundResource(R.drawable.line_rect_huise);
        child.setText(str);
        child.setPadding(20,5,20,5);
        child.setTextColor(Color.BLACK);
        item2labellist.add(child);
        item2label.addView(child);
    }

    /**动态改变listView的高度*/
    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
            //totalHeight += 80;
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
            //params.height = 80 * (listAdapter.getCount() - 1);
            //params.height = 80 * (listAdapter.getCount());
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        ((MarginLayoutParams) params).setMargins(0, 0, 0, 0);
        listView.setLayoutParams(params);

    }
}
