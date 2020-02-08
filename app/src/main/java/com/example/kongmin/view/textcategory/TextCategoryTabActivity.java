package com.example.kongmin.view.textcategory;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.*;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.kongmin.Constant.Constant;
import com.example.kongmin.myapplication.R;
import com.example.kongmin.view.textcategory.util.ListDropDownAdapter;
import com.example.kongmin.util.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * 做任务两个分类界面
 * Created by kongmin
 * 2018.12.29
 */

public class TextCategoryTabActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    //导航栏
    private MyTabIndicator mTabIndicator;

    //初始化数据
    private List<PlaceholderFragment> list = new ArrayList<PlaceholderFragment>();
    private List<String> titles = new ArrayList<>();


    //发送http请求获取到的数据
    //每个fragment显示一个instance
    private int instanceid;
    //每一个instance在一个文本里面的位置
    private int instanceindex;
    //每一个instance要标注几个label
    private int instlabelnum;
    //itemid
    private int itemid;
    //item的内容
    private String itemcontent;
    //每一个item在instance中的位置
    private int itemindex;
    //每一个item要标注几个label
    private int itemlabelnum;

    private int instancelabelid;
    private String instlabelname;
    private int item1labelid;
    private String item1labelname;
    private int item2labelid;
    private String item2labelname;

    //做任务要使用的数据
    private final SerializableMap instlabelMap = new SerializableMap();
    private Map<String,Integer> instlabelmap = new LinkedHashMap<>();
    private final SerializableMap item1labelMap = new SerializableMap();
    private Map<String,Integer>  item1labelmap = new LinkedHashMap<>();
    private final SerializableMap item2labelMap = new SerializableMap();
    private Map<String,Integer> item2labelmap = new LinkedHashMap<>();

    private List<Integer> instanceids = new ArrayList<Integer>();
    private List<Integer> instanceindexs = new ArrayList<Integer>();

    private List<Integer> itemids = new ArrayList<Integer>();
    private List<String> itemcontents = new ArrayList<String>();
    private List<Integer> itemindexs = new ArrayList<Integer>();
    private List<Integer> itemlabelnums = new ArrayList<Integer>();

    private List<Integer> instlabelnums = new ArrayList<Integer>();
    //任务ID
    //private int taskid;
    private int fragmentsize;



    //传送过来的做任务的文件内容
    private Map<String,Integer> filemap = new LinkedHashMap<String,Integer>();
    //传送过来的做任务的标签内容
    private  SerializableMap inststrMap;
    private Map<String,Integer> inststrmap = new LinkedHashMap<String,Integer>();

    private List<Integer> fileid = new ArrayList<Integer>();
    private List<String> filename = new ArrayList<String>();
    //选中的fileID
    private int btnfileid = -1;
    //选中的文件状态
    private String filestatus="";
    //某一个段落的状态
    private String parastatusstr;
    private List<String> parastatus= new ArrayList<String>();


    //和选择弹出框相关的
    @InjectView(R.id.dropDownMenu) DropDownMenu mDropDownMenu;
    private String headers[] = {"文件列表", "完成状态", "确定"};
    private List<View> popupViews = new ArrayList<>();


    private ListDropDownAdapter fileAdapter;
    private ListDropDownAdapter statusAdapter;
    private ListDropDownAdapter btnAdapter;

    private String status[] = {"全部", "进行中"};
    private String confirm[] = {"确定"};

    private int constellationPosition = 0;

    //任务ID
    private int taskid;
    //是做任务页面还是查看做任务页面
    private String typename;
    private int docId;
    //文件状态默认是全部
    private String docStatus = "全部";


    //已经标注了的标签
    private SerializableList index_beginList = new SerializableList();
    private ArrayList<Integer> index_begins = new ArrayList<Integer>();
    private SerializableList index_endList = new SerializableList();
    private ArrayList<Integer> index_ends = new ArrayList<Integer>();
    private SerializableList label_idList = new SerializableList();
    private ArrayList<Integer> label_ids = new ArrayList<Integer>();

    private List<Integer> seleinstlid = new ArrayList<>();
    //private List<Integer> seleinstlabelid = new ArrayList<>();
    //private List<Integer> seleitem1labelid = new ArrayList<>();
    //private List<Integer> seleitem2labelid = new ArrayList<>();

    private Map<Integer,List<Integer>> seledinstidmap = new HashMap<>();
    private Map<Integer,List<Integer>> seledinstmap = new HashMap<>();
    private Map<Integer,List<Integer>> seleditem1map = new HashMap<>();
    private Map<Integer,List<Integer>> seleditem2map = new HashMap<>();




    //传送过来的item1做任务的标签内容
    private  SerializableMap item1strMap;
    private Map<String,Integer> item1strmap = new LinkedHashMap<String,Integer>();
    //传送过来的item2做任务的标签内容
    private  SerializableMap item2strMap;
    private Map<String,Integer> item2strmap = new LinkedHashMap<String,Integer>();

    private TextView downloadextract;

    private String filePath = "/sdcard/xinjian/";

    private String filenameTemp = filePath+ "信息抽取"+ ".txt";

    private StringBuffer downloadfilecontent = new StringBuffer();

    private String singleinstcontent;
    private String singleitem1content;
    private String singleitem2content;
    private String singlelinecontent;

    private String instlabeltype = "instance";
    private String item1labeltype = "item1";
    private String item2labeltype = "item2";
    private String downloadfilename;
    private String downitem1content;
    private String downitem2content;

    private Map<Integer,String> downinstlabel = new LinkedHashMap<Integer,String>();
    private Map<Integer,String> downitem1label = new LinkedHashMap<Integer,String>();
    private Map<Integer,String> downitem2label = new LinkedHashMap<Integer,String>();

    private MyApplication mApplication;
    private int userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_category_tab);
        // 获取整个应用的Application对象
        // 在不同的Activity中获取的对象是同一个
        mApplication = (MyApplication)getApplication();

        //获取读写权限
        requestReadExternalPermission();
        requestWriteExternalPermission();

        mTabIndicator = (MyTabIndicator) findViewById(R.id.mTabIndicator);
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);
        //和选择弹出框相关的
        ButterKnife.inject(this);
        initView();

        // 获取整个应用的Application对象
        // 在不同的Activity中获取的对象是同一个
        mApplication = (MyApplication)getApplication();
        userId = mApplication.getLoginUserId();

        //接收从上一个页面传递进来的参数
        Intent intent = getIntent();
        taskid = intent.getIntExtra("taskid",0);
        typename = intent.getStringExtra("type");
        Bundle tdbundle = intent.getExtras();
        //传送过来的做任务的文件内容
        SerializableMap fileMap = (SerializableMap) tdbundle.get("filemap");
        filemap =fileMap.getMap();
        for(String labelname : filemap.keySet()){
            fileid.add(filemap.get(labelname));
            filename.add(labelname);
            Log.e("ExtractActivity---->", "GET方式请求成功，labelname--->" + labelname+filemap.get(labelname));
        }
        docId = fileid.get(0);
        downloadfilename = filename.get(0);
        //传送过来的做任务的标签内容
        inststrMap = (SerializableMap) tdbundle.get("instlabel");
        inststrmap = inststrMap.getMap();
        for(String labelname : inststrmap.keySet()){
            downinstlabel.put(inststrmap.get(labelname),labelname);
            Log.e("ExtractActivity---->", "GET方式请求成功，inst标签--->" + labelname+inststrmap.get(labelname));
        }

        item1strMap = (SerializableMap) tdbundle.get("item1label");
        item1strmap = item1strMap.getMap();
        for(String labelname : item1strmap.keySet()){
            downitem1label.put(item1strmap.get(labelname),labelname);
            Log.e("ExtractActivity---->", "GET方式请求成功，item1标签--->" + labelname+item1strmap.get(labelname));
        }


        item2strMap = (SerializableMap) tdbundle.get("item2label");
        item2strmap = item2strMap.getMap();
        for(String labelname : item2strmap.keySet()){
            downitem2label.put(item2strmap.get(labelname),labelname);
            Log.e("ExtractActivity---->", "GET方式请求成功，item2标签--->" + labelname+item2strmap.get(labelname));
        }

        LinearLayout extractlinear = findViewById(R.id.extractlinear);
        if(typename.equals("dotask")){
            extractlinear.setVisibility(View.GONE);
        }else{
            extractlinear.setVisibility(View.VISIBLE);
        }


        new Thread(runnable).start();
        try {
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        //下载文档按钮
        downloadextract = (TextView)findViewById(R.id.downloadextract);
        //是做任务页面还是查看做任务页面
        if(typename.equals("dotask")){
            downloadextract.setVisibility(View.GONE);
        }else{
            downloadextract.setVisibility(View.VISIBLE);
            downloadextract.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        CreateText();
                        //print("写入文件中的内容\n写入文件中的内容");
                        print(downloadfilecontent.toString());
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    Toast.makeText(TextCategoryTabActivity.this,"点击了下载按钮",Toast.LENGTH_LONG).show();
                }
            });
        }


        initFragment(inststrMap);

        /*for(int i=0;i<fragmentsize;i++){
            //导航栏加标题
            titles.add("第"+instanceindexs.get(i)+"段");
            //titles.add("第:"+(i+1)+"段");
            PlaceholderFragment f1 = PlaceholderFragment.newInstance(i);
            list.add(f1);
            Bundle bundle = new Bundle();
            //传递lebel数据
            bundle.putInt("fragmentindex",i);
            //todo 设置taskid
            taskid = 1;
            bundle.putInt("taskid", taskid);

            instlabelMap.setMap(instlabelmap);
            bundle.putSerializable("instancelebelmap", instlabelMap);
            item1labelMap.setMap(item1labelmap);
            bundle.putSerializable("item1labelmap", item1labelMap);
            item2labelMap.setMap(item2labelmap);
            bundle.putSerializable("item2labelmap", item2labelMap);
            bundle.putInt("instanceid" + i, instanceids.get(i));
            bundle.putInt("instanceindex" + i, instanceindexs.get(i));
            //和item相关的参数
            bundle.putInt("itemid1page" + i, itemids.get(2*i));
            bundle.putInt("itemid2page" + i, itemids.get(2*i+1));
            bundle.putString("itemcontent1page" + i,itemcontents.get(2*i));
            bundle.putString("itemcontent2page" + i,itemcontents.get(2*i+1));
            bundle.putInt("itemindex1page" + i, itemindexs.get(2*i));
            bundle.putInt("itemindex2page" + i, itemindexs.get(2*i+1));

            bundle.putInt("instlabelnum",instlabelnums.get(0));
            bundle.putInt("item1labelnum",itemlabelnums.get(0));
            bundle.putInt("item2labelnum",itemlabelnums.get(1));

            //todo 获取用户ID
            bundle.putInt("userid", 1);
            list.get(i).setArguments(bundle);
        }*/


        /*PlaceholderFragment f1 = PlaceholderFragment.newInstance(3);
        list.add(f1);
        PlaceholderFragment f2 = PlaceholderFragment.newInstance(5);
        list.add(f2);
        PlaceholderFragment f3 = PlaceholderFragment.newInstance(7);
        list.add(f3);*/


        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        //先注释掉
        //mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),list);


        /*for(int i = 0; i < 3; i ++){
            titles.add("标题:" + i+1);
        }*/

        // Set up the ViewPager with the sections adapter.
        //mViewPager = (ViewPager) findViewById(R.id.container);
        //先注释掉
        //mViewPager.setAdapter(mSectionsPagerAdapter);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/


        //先注释掉
        //mTabIndicator.setTitles(titles);
        mTabIndicator.setViewPager(mViewPager, 0);
        //mTabIndicator.setTitles(titles, 3);//可以设置默认选中的title

        mTabIndicator.setOnPageChangeListener(new MyTabIndicator.PageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Toast.makeText(TextCategoryTabActivity.this, "选择了：" + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }



    @SuppressLint("NewApi")
    private void requestReadExternalPermission() {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.d("DotaskExtract---->", "READ permission IS NOT granted...");
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        } else {
            Log.d("DotaskExtract---->", "READ permission is granted...");
        }
    }

    @SuppressLint("NewApi")
    private void requestWriteExternalPermission() {
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.d("DotaskExtract---->", "WRITE permission IS NOT granted...");
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        } else {
            Log.d("DotaskExtract---->", "WRITE permission is granted...");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 0){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("DotaskExtract---->","获取到了权限");
            } else{
                // 没有获取到权限，做特殊处理
                Log.e("DotaskExtract---->","没有获取到权限");
                Toast.makeText(TextCategoryTabActivity.this,"没有获取读取手机权限，请到应用中心手动打开该权限",Toast.LENGTH_SHORT).show();
            }
        }
    }

    //创建文件夹及文件
    public void CreateText() throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                //按照指定的路径创建文件夹
                file.mkdirs();
                Log.e("DotaskExtract---->", "activity中的filepath新建成功--->" + file.getAbsolutePath() + "------" );
            } catch (Exception e) {
                // TODO: handle exception
            }
        }else{
            Log.e("DotaskExtract---->", "activity中的filepath已经存在--->" + file.getAbsolutePath() + "------" );
        }
        String filename = downloadfilename.substring(0,downloadfilename.lastIndexOf("."));
        filenameTemp = filePath+filename+".txt";
        File dir = new File(filenameTemp);
        if (!dir.exists()){
            try {
                //在指定的文件夹中创建文件
                dir.createNewFile();
                Log.e("DotaskExtract---->", "activity中的filename新建成功--->" + dir.getName() + "------" );
            } catch (Exception e) {
                Log.e("DotaskExtract---->", "activity中的filename新建失败--->" + "------" );
            }
        }

    }

    //向已创建的文件中写入数据
    public void print(String str) {
        FileWriter fw = null;
        BufferedWriter bw = null;
        String datetime = "";
        try {
            SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd" + " "+"hh:mm:ss");
            datetime = tempDate.format(new java.util.Date()).toString();
            fw = new FileWriter(filenameTemp,true);
            //创建FileWriter对象，用来写入字符流
            bw = new BufferedWriter(fw); // 将缓冲对文件的输出
            //String myreadline = datetime + "[]" + str;
            String myreadline = str;
            bw.write(myreadline + "\n"); // 写入文件
            bw.newLine();
            bw.flush(); // 刷新该流的缓冲
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            try {
                bw.close();
                fw.close();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
            }
        }
    }


    public void initFragment(SerializableMap inststrMap){
        titles.clear();
        list.clear();
        for(int i=0;i<fragmentsize;i++){
            //导航栏加标题
            titles.add("第"+instanceindexs.get(i)+"段");
            PlaceholderFragment f1 = PlaceholderFragment.newInstance(i);
            list.add(f1);
            Bundle bundle = new Bundle();
            //传递lebel数据
            bundle.putInt("fragmentindex",i);
            bundle.putInt("taskid", taskid);
            bundle.putInt("docid", docId);
            //是做任务页面还是查看做任务页面
            bundle.putString("type",typename);

            instlabelMap.setMap(instlabelmap);
            bundle.putSerializable("instancelebelmap", instlabelMap);
            item1labelMap.setMap(item1labelmap);
            bundle.putSerializable("item1labelmap", item1labelMap);
            item2labelMap.setMap(item2labelmap);
            bundle.putSerializable("item2labelmap", item2labelMap);
            bundle.putInt("instanceid" + i, instanceids.get(i));
            bundle.putInt("instanceindex" + i, instanceindexs.get(i));
            //和item相关的参数
            bundle.putInt("itemid1page" + i, itemids.get(2*i));
            bundle.putInt("itemid2page" + i, itemids.get(2*i+1));
            bundle.putString("itemcontent1page" + i,itemcontents.get(2*i));
            bundle.putString("itemcontent2page" + i,itemcontents.get(2*i+1));
            bundle.putInt("itemindex1page" + i, itemindexs.get(2*i));
            bundle.putInt("itemindex2page" + i, itemindexs.get(2*i+1));

            bundle.putInt("instlabelnum",instlabelnums.get(0));
            bundle.putInt("item1labelnum",itemlabelnums.get(0));
            bundle.putInt("item2labelnum",itemlabelnums.get(1));


            //如果该段已经排好序
            if(seledinstmap.containsKey(instanceids.get(i))){
                bundle.putString("issorted"+i,"true");
                //sortedindexmap.get(instanceids.get(i));
                bundle.putIntegerArrayList("seledinstid"+i,(ArrayList)seledinstidmap.get(instanceids.get(i)));
                bundle.putIntegerArrayList("seledinst"+i,(ArrayList)seledinstmap.get(instanceids.get(i)));
                bundle.putIntegerArrayList("seleditem1"+i,(ArrayList)seleditem1map.get(instanceids.get(i)));
                bundle.putIntegerArrayList("seleditem2"+i,(ArrayList)seleditem2map.get(instanceids.get(i)));
                Log.e("DtOneSortActivity---->", "GET方式请求成功,issorted+i--->"+"issorted"+i+"-----"+instanceids.get(i)+"----"+seledinstmap.get(instanceids.get(i)));
            }else{
                bundle.putString("issorted"+i,"false");
            }




            bundle.putString("parastatus"+ i, parastatus.get(i));
            if(parastatus.get(i).equals("已完成")){
                for(int j=0;j<index_begins.size();j++){
                    Log.e("DotaskExtract---->", "activity中的index_beginsindex_ends--->" + index_begins.get(j) + "------" );
                }
                //把标注好的标签信息传过去
               /* index_beginList.setList(index_begins);
                bundle.putSerializable("index_begins",index_beginList);
                index_endList.setList(index_ends);
                bundle.putSerializable("index_ends",index_endList);
                label_idList.setList(label_ids);
                bundle.putSerializable("label_ids",label_idList);*/
            }


            //todo 获取用户ID
            bundle.putInt("userid", 1);
            list.get(i).setArguments(bundle);

        }
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),list);
        mSectionsPagerAdapter.notifyDataSetChanged();
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mTabIndicator.setTitles(titles);
    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // TODO: http request.
            // 在这里进行 http request.网络请求相关操作
            //String requestUrl3 = "http://172.20.10.5:8080/instance/getInstanceItem";
            //String paramUrl3 = "?docId=1";
            String requestUrl = Constant.relationfileUrl;
            //String paramUrl = "?docId="+docId+"&status="+docStatus+"&taskId="+taskid;
            String paramUrl;
            if(typename.equals("dotask")){
                paramUrl = "?docId="+docId+"&status="+docStatus+"&taskId="+taskid+"&userId="+userId;
            }else{
                paramUrl = "?docId="+docId+"&status="+docStatus+"&taskId="+taskid+"&userId="+userId;
                Log.e("ExtractActivity---->", "GET方式请求成功，result2---> 查看我做的任务");
            }
            String instanceItem = HttpUtil.requestGet(requestUrl,paramUrl);

            Log.e("ExtractActivity---->", "GET方式请求成功，InstanceItem--->" + instanceItem);
            JSONObject instanceItemjson = JSONObject.parseObject(instanceItem);
            JSONArray instanceItemArray = (JSONArray)instanceItemjson.get("instanceItem");
            JSONArray instanceLabelArray = (JSONArray)instanceItemjson.get("instanceLabel");
            JSONArray item1LabelArray = (JSONArray)instanceItemjson.get("item1Label");
            JSONArray item2LabelArray = (JSONArray)instanceItemjson.get("item2Label");
            fragmentsize = instanceItemArray.size();
            //清空content
            instanceids.clear();
            instanceindexs.clear();
            instlabelnums.clear();
            parastatus.clear();
            itemids.clear();
            itemcontents.clear();
            itemindexs.clear();
            itemlabelnums.clear();
            if(instanceItemArray.size()>0){
                for(int i=0;i<instanceItemArray.size();i++){
                    //遍历jsonarray数组，把每一个对象转成json对象
                    JSONObject job = instanceItemArray.getJSONObject(i);
                    //得到每个对象中的属性值
                    if(job.get("instid")!=null) {
                        //instance
                        instanceid = (Integer)job.get("instid");
                        instanceids.add(instanceid);
                        Log.e("DotaskOneCategory---->", "activity中的instanceid--->" + instanceid);
                    }
                    if(job.get("instindex")!=null) {
                        //instance
                        String instanceindexstr = (String)job.get("instindex").toString();
                        instanceindex = Integer.valueOf(instanceindexstr);
                        instanceindexs.add(instanceindex);
                        Log.e("DotaskOneCategory---->", "activity中的instanceindex--->" + instanceindex);
                    }
                    if(job.get("labelnum")!=null) {
                        //instance
                        instlabelnum = (Integer)job.get("labelnum");
                        //todo instance的label应该是统一的，一个文本只用设置一个就可以了
                        instlabelnums.add(instlabelnum);
                        Log.e("DotaskOneCategory---->", "activity中的instlabelnum--->" + instlabelnum);
                    }
                    if(job.get("dtstatus")!=null) {
                        parastatusstr = (String)job.get("dtstatus");
                        parastatus.add(parastatusstr);
                        Log.e("DotaskExtract---->", "activity中的contentparastatus-->" + parastatusstr);
                    }else{
                        //不是已完成任务的状态就是空
                        parastatus.add("");
                    }
                    HashMap<Integer,String> hashMap = new HashMap<Integer,String>();
                    if(job.get("itemList")!=null) {
                        //instance
                        JSONArray itemList = (JSONArray)job.get("itemList");
                        for(int j=0;j<itemList.size();j++){
                            JSONObject item = itemList.getJSONObject(j);
                            if(item.get("itid")!=null) {
                                //instance
                                itemid = (Integer)item.get("itid");
                                itemids.add(itemid);
                                Log.e("DotaskOneCategory---->", "activity中的itemid--->" + itemid);
                            }
                            if(item.get("itemcontent")!=null) {
                                //instance
                                itemcontent = (String)item.get("itemcontent").toString();
                                itemcontents.add(itemcontent);
                                Log.e("DotaskOneCategory---->", "activity中的itemcontent--->" + itemcontent);
                            }
                            if(item.get("itemindex")!=null) {
                                //instance
                                String itemindexstr = item.get("itemindex").toString();
                                itemindex = Integer.valueOf(itemindexstr);
                                itemindexs.add(itemindex);
                                hashMap.put(itemindex,itemcontent);
                                Log.e("DotaskOneCategory---->", "activity中的itemindex--->" + itemindex);
                            }
                            if(item.get("labelnum")!=null) {
                                //instance
                                itemlabelnum = (Integer)item.get("labelnum");
                                //todo item的label应该是统一的，一个文本只用设置一个就可以了
                                itemlabelnums.add(itemlabelnum);
                                Log.e("DotaskOneCategory---->", "activity中的itemlabelnum--->" + itemlabelnum);
                            }
                        }
                    }
                    List<Integer> seleinstlabelid = new ArrayList<>();
                    List<Integer> seleitem1labelid = new ArrayList<>();
                    List<Integer> seleitem2labelid = new ArrayList<>();
                    if(job.get("alreadyDone")!=null) {

                        StringBuffer downinstlabelname = new StringBuffer();
                        StringBuffer downitem1labelname = new StringBuffer();
                        StringBuffer downitem2labelname = new StringBuffer();

                        //instance
                        JSONArray alreadyDonelArray = (JSONArray)job.get("alreadyDone");
                        if(alreadyDonelArray.size()>0) {
                            for (int j = 0; j < alreadyDonelArray.size(); j++) {
                                JSONObject item = alreadyDonelArray.getJSONObject(j);
                                if (item.get("labeltype") != null) {
                                    String labeltype = (String) item.get("labeltype");
                                    int labelId = (Integer) item.get("labelId");
                                    seleinstlid.add(instanceid);
                                    if(labeltype.equals("instance")){
                                        seleinstlabelid.add(labelId);
                                        downinstlabelname.append(downinstlabel.get(labelId)+"、");
                                    }else if(labeltype.equals("item1")){
                                        seleitem1labelid.add(labelId);
                                        downitem1labelname.append(downitem1label.get(labelId)+"、");
                                    }else{
                                        seleitem2labelid.add(labelId);
                                        downitem2labelname.append(downitem2label.get(labelId)+"、");
                                    }
                                }
                            }
                            seledinstidmap.put(instanceid,seleinstlid);
                            seledinstmap.put(instanceid,seleinstlabelid);
                            seleditem1map.put(instanceid,seleitem1labelid);
                            seleditem2map.put(instanceid,seleitem2labelid);

                            singleinstcontent = downloadfilename+"\t"+instlabeltype+"\t"+downinstlabelname+"\n"+hashMap.get(1)+"\n~~~~~~~~~~~\n"+hashMap.get(2)+"\n";
                            singleitem1content = downloadfilename+"\t"+item1labeltype+"\t"+downitem1labelname+"\n"+hashMap.get(1)+"\n";
                            singleitem2content = downloadfilename+"\t"+item2labeltype+"\t"+downitem2labelname+"\n"+hashMap.get(2)+"\n";
                            singlelinecontent = singleinstcontent+"-------------\n"+singleitem1content+"-------------\n"+singleitem2content;
                            downloadfilecontent.append("*************\n"+singlelinecontent+"\n*************");

                        }
                  }


                }
            }
            if(instanceLabelArray.size()>0){
                for(int i=0;i<instanceLabelArray.size();i++){
                    //遍历jsonarray数组，把每一个对象转成json对象
                    JSONObject job = instanceLabelArray.getJSONObject(i);
                    //得到每个对象中的属性值
                    if(job.get("lid")!=null) {
                        //instance
                        instancelabelid = (Integer)job.get("lid");
                        Log.e("DotaskOneCategory---->", "activity中的instancelabelid--->" + instancelabelid);
                    }
                    if(job.get("labelname")!=null) {
                        //instance
                        instlabelname = (String)job.get("labelname").toString();
                        instlabelmap.put(instlabelname,instancelabelid);
                        Log.e("DotaskOneCategory---->", "activity中的instlebelname--->" + instlabelname);
                    }
                }
            }

            if(item1LabelArray.size()>0){
                for(int i=0;i<item1LabelArray.size();i++){
                    //遍历jsonarray数组，把每一个对象转成json对象
                    JSONObject job = item1LabelArray.getJSONObject(i);
                    //得到每个对象中的属性值
                    if(job.get("lid")!=null) {
                        //instance
                        item1labelid = (Integer)job.get("lid");
                        Log.e("DotaskOneCategory---->", "activity中的item1labelid--->" + item1labelid);
                    }
                    if(job.get("labelname")!=null) {
                        //instance
                        item1labelname = (String)job.get("labelname").toString();
                        item1labelmap.put(item1labelname,item1labelid);
                        Log.e("DotaskOneCategory---->", "activity中的item1lebelname--->" + item1labelname);
                    }
                }
            }

            if(item2LabelArray.size()>0){
                for(int i=0;i<item2LabelArray.size();i++){
                    //遍历jsonarray数组，把每一个对象转成json对象
                    JSONObject job = item2LabelArray.getJSONObject(i);
                    //得到每个对象中的属性值
                    if(job.get("lid")!=null) {
                        //instance
                        item2labelid = (Integer)job.get("lid");
                        Log.e("DotaskOneCategory---->", "activity中的item2labelid--->" + item2labelid);
                    }
                    if(job.get("labelname")!=null) {
                        //instance
                        item2labelname = (String)job.get("labelname").toString();
                        item2labelmap.put(item2labelname,item2labelid);
                        Log.e("DotaskOneCategory---->", "activity中的item2lebelname--->" + item2labelname);
                    }
                }
            }

        }
    };


    //和选择弹出框相关的
    private void initView() {

        //init file menu
        final ListView fileView = new ListView(this);
        fileAdapter = new ListDropDownAdapter(this, filename);
        fileView.setDividerHeight(0);
        fileView.setAdapter(fileAdapter);

        //init status menu
        final ListView statusView = new ListView(this);
        statusView.setDividerHeight(0);
        statusAdapter = new ListDropDownAdapter(this, Arrays.asList(status));
        statusView.setAdapter(statusAdapter);

        //init 确定 menu
        final ListView btnView = new ListView(this);
        btnView.setDividerHeight(0);
        btnAdapter = new ListDropDownAdapter(this, Arrays.asList(confirm));
        btnView.setAdapter(btnAdapter);

        //init popupViews
        popupViews.add(fileView);
        popupViews.add(statusView);
        popupViews.add(btnView);

        //add item click event
        fileView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fileAdapter.setCheckItem(position);
                //mDropDownMenu.setTabText(position == 0 ? headers[0] : filename.get(position));
                mDropDownMenu.setTabText(filename.get(position));
                //if(position!=0) {
                btnfileid = fileid.get(position);
                //}
                mDropDownMenu.closeMenu();
            }
        });

        statusView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                statusAdapter.setCheckItem(position);
                //mDropDownMenu.setTabText(position == 0 ? headers[1] : status[position]);
                mDropDownMenu.setTabText(status[position]);
                //if(position!=0) {
                filestatus = status[position];
                //}
                mDropDownMenu.closeMenu();
            }
        });

        btnView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                index_begins.clear();
                index_ends.clear();
                label_ids.clear();
                if(btnfileid!=-1 && filestatus.equals("")){
                    //选了文件没选状态
                    if(btnfileid!=fileid.get(0)) {
                        docId = btnfileid;

                        //设置下载文件的文件名称
                        for(int i=0;i<fileid.size();i++){
                            if(docId==fileid.get(i)){
                                downloadfilename = filename.get(i);
                                break;
                            }
                        }

                        //请求数据
                        new Thread(runnable).start();
                        try {
                            Thread.sleep(1000);
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                        initFragment(inststrMap);
                    }
                }else if(btnfileid==-1 && !filestatus.equals("")){
                    //选了状态没选文件
                    if(!filestatus.equals("全部")){
                        docStatus = filestatus;
                        //请求数据
                        new Thread(runnable).start();
                        try {
                            Thread.sleep(1000);
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                        initFragment(inststrMap);
                    }
                }else if(btnfileid!=-1 && !filestatus.equals("")){
                    Toast.makeText(TextCategoryTabActivity.this, "选择了文件：" + btnfileid+filestatus, Toast.LENGTH_SHORT).show();
                    //如果文件和文件状态都没有选择默认值
                    //if(!filestatus.equals("全部")){
                    docId = btnfileid;

                    //设置下载文件的文件名称
                    for(int i=0;i<fileid.size();i++){
                        if(docId==fileid.get(i)){
                            downloadfilename = filename.get(i);
                            break;
                        }
                    }

                    docStatus = filestatus;
                    //请求数据
                    new Thread(runnable).start();
                    try {
                        Thread.sleep(1000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    initFragment(inststrMap);
                    //}
                }
                mDropDownMenu.closeMenu();
            }
        });

        //init context view
        TextView contentView = new TextView(this);
        contentView.setHeight(0);
        /*contentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        contentView.setText("内容显示区域");
        contentView.setGravity(Gravity.CENTER);
        contentView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);*/
        //TextView contentView = findViewById(R.id.textview);

        //init dropdownview
        mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, contentView);
    }

    @Override
    public void onBackPressed() {
        //退出activity前关闭菜单
        if (mDropDownMenu.isShowing()) {
            mDropDownMenu.closeMenu();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_text_category_tab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
