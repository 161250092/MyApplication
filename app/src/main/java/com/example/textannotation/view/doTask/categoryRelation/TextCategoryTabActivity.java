package com.example.textannotation.view.doTask.categoryRelation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import android.widget.*;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.textannotation.Constant.Constant;
import com.example.textannotation.myapplication.R;
import com.example.textannotation.network.OkHttpUtil;
import com.example.textannotation.view.commonVIew.MyTabIndicator;
import com.example.textannotation.util.*;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * 文本关系
 * update by mwx
 * 2020.2.10
 */

public class TextCategoryTabActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    //导航栏
    private MyTabIndicator mTabIndicator;
    //初始化数据
    private List<PlaceholderFragment> fragment_list = new ArrayList<PlaceholderFragment>();
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

    private String[] mFileNames;
    private Integer[] mFileIds;
    //选中的fileID
    private int btnfileid = -1;
    //选中的文件状态
    private String filestatus="";
    //某一个段落的状态
    private String parastatusstr;
    private List<String> parastatus= new ArrayList<String>();

    private String status[] = {"全部", "进行中"};

    //任务ID
    private int taskid;
    //是做任务页面还是查看做任务页面
    private String typename;
    private int docId;
    //文件状态默认是全部
    private String docStatus = "全部";

    private ArrayList<Integer> index_begins = new ArrayList<Integer>();

    private List<Integer> seleinstlid = new ArrayList<>();


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

    private Map<Integer,String> downinstlabel = new LinkedHashMap<Integer,String>();
    private Map<Integer,String> downitem1label = new LinkedHashMap<Integer,String>();
    private Map<Integer,String> downitem2label = new LinkedHashMap<Integer,String>();

    private MyApplication mApplication;
    private int userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_category_tab);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        //获取读写权限
        requestReadExternalPermission();
        requestWriteExternalPermission();

        mTabIndicator = (MyTabIndicator) findViewById(R.id.mTabIndicator);
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);
        //和选择弹出框相关的
        ButterKnife.inject(this);

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
        mFileNames = filename.toArray(new String[filename.size()]) ;
        mFileIds = fileid.toArray(new Integer[fileid.size()]);

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

        getFileContent();


        mTabIndicator.setViewPager(mViewPager, 0);
        //mTabIndicator.setTitles(titles, 3);//可以设置默认选中的title

        mTabIndicator.setOnPageChangeListener(new MyTabIndicator.PageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.tools);
        navigation.setOnNavigationItemSelectedListener(this);
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


    public void initFragment(SerializableMap inststrMap){
        titles.clear();
        fragment_list.clear();
        for(int i=0;i<fragmentsize;i++){
            //导航栏加标题
            titles.add("任务"+instanceindexs.get(i));
            PlaceholderFragment f1 = PlaceholderFragment.newInstance(i);
            fragment_list.add(f1);
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
            }

            //todo 获取用户ID
            bundle.putInt("userid", userId);
            fragment_list.get(i).setArguments(bundle);

        }
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),fragment_list);
        mSectionsPagerAdapter.notifyDataSetChanged();
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mTabIndicator.setTitles(titles);
    }

    public void getFileContent(){
        String requestUrl = Constant.relationfileUrl;
        String paramUrl;
        if(typename.equals("dotask")){
            paramUrl = "?docId="+docId+"&status="+docStatus+"&taskId="+taskid+"&userId="+userId;
        }else{
            paramUrl = "?docId="+docId+"&status="+docStatus+"&taskId="+taskid+"&userId="+userId;
            Log.e("ExtractActivity---->", "GET方式请求成功，result2---> 查看我做的任务");
        }
        Log.e("mwx",requestUrl + paramUrl);
        OkHttpUtil.sendGetRequest(requestUrl + paramUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String instanceItem = response.body().string();
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

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initFragment(inststrMap);
                    }
                });
            }
        });


    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){

            case R.id.upload_paragraph: {
                int fragmentIndex = mViewPager.getCurrentItem();
                Toast.makeText(this,"upload",Toast.LENGTH_SHORT).show();
                Log.e("mwx",fragmentIndex+"");
                PlaceholderFragment currentFragment  = (PlaceholderFragment)mSectionsPagerAdapter.getItem(fragmentIndex);
                currentFragment.saveIns();
                return  true;
            }

            case R.id.before: {
                int fragmentIndex = mViewPager.getCurrentItem();
                fragmentIndex =  (fragmentIndex == 0) ? fragment_list.size() - 1 : fragmentIndex - 1 ;
                mViewPager.setCurrentItem(fragmentIndex);
                return false;
            }

            case R.id.next: {
                int fragmentIndex = mViewPager.getCurrentItem();
                fragmentIndex = (fragmentIndex + 1) % fragment_list.size();
                mViewPager.setCurrentItem(fragmentIndex);
                return false;
            }

            case R.id.file_list:{
                new XPopup.Builder(this)
                        //.maxWidth(600)
                        .asBottomList("选择文件", mFileNames,
                                new OnSelectListener() {
                                    @Override
                                    public void onSelect(int position, String text) {
                                        Toast.makeText(TextCategoryTabActivity.this,"切换到" + text,Toast.LENGTH_SHORT).show();
                                        docId = mFileIds[position];
                                        getFileContent();
                                    }
                                })
                        .show();

                return false;
            }

            case R.id.settings:{

                new XPopup.Builder(this)
                        .asBottomList("设置",new String[]{"显示全部","只显示未完成","结束该段","结束该文档"} ,
                                new OnSelectListener() {
                                    @Override
                                    public void onSelect(int position, String text) {

                                        if (position == 0) {
                                            docStatus = status[0];
                                            Toast.makeText(TextCategoryTabActivity.this, "显示全部段落",Toast.LENGTH_SHORT).show();
                                            getFileContent();
                                        }

                                        else if (position == 1) {
                                            docStatus = status[1];
                                            Toast.makeText(TextCategoryTabActivity.this, "只显示未完成段落"+text,Toast.LENGTH_SHORT).show();
                                            getFileContent();
                                        }

                                        else if (position == 2) {
                                            int fragmentIndex = mViewPager.getCurrentItem();
                                            PlaceholderFragment currentFragment  = (PlaceholderFragment) mSectionsPagerAdapter.getItem(fragmentIndex);
                                            currentFragment.completeCon();

                                        } else{
                                            int fragmentIndex = mViewPager.getCurrentItem();
                                            PlaceholderFragment currentFragment  = (PlaceholderFragment) mSectionsPagerAdapter.getItem(fragmentIndex);
                                            currentFragment.completeDoc();
                                        }

                                    }
                                })
                        .show();
                return false;
            }

        }

        return true;
    }
}
