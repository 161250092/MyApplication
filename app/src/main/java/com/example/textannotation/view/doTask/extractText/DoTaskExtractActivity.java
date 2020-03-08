package com.example.textannotation.view.doTask.extractText;

import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.*;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.textannotation.myapplication.R;
import com.example.textannotation.network.OkHttpUtil;
import com.example.textannotation.network.Url;
import com.example.textannotation.util.*;
import com.example.textannotation.view.commonVIew.MyTabIndicator;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;

import java.util.*;

import java.io.IOException;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.annotation.SuppressLint;
/**
 *
 * 信息抽取做任务界面
 * Created by kongmin
 * 2018.12.29
 */
public class DoTaskExtractActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    private DoTaskExtractAdapter mDoTaskExtractAdapter;


    private ViewPager mViewPager;

    //导航栏
    private MyTabIndicator mTabIndicator;

    //初始化数据
    private List<DoTaskExtractFragment> fragment_list = new ArrayList<DoTaskExtractFragment>();
    private List<String> titles = new ArrayList<>();

    //文件内容
    private String content;
    //内容ID
    private int contentid;
    //段落在文本中的索引
    private int contentindex;

    private List<String> contents = new ArrayList<String>();
    private List<Integer> contentids = new ArrayList<Integer>();
    private List<Integer> contentindexs = new ArrayList<Integer>();
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

    private String[] mLabelNames;

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
    //todo 设置userId
    private int userId;

    //已经标注了的标签
    private ArrayList<Integer> index_begins = new ArrayList<Integer>();

    private ArrayList<Integer> index_ends = new ArrayList<Integer>();

    private ArrayList<Integer> label_ids = new ArrayList<Integer>();

    private int index_begin;
    private int index_end;
    private int label_id;

    private TextView downloadextract;

    private String filePath = "/sdcard/xinjian/";

    private String filenameTemp = filePath+ "信息抽取"+ ".txt";

    private StringBuffer downloadfilecontent = new StringBuffer();
    private String singlelinecontent;

    private String downloadfilename;
    private String downloadlabelname;
    private String downloadcontent;

    private Map<Integer,String> downloadlabel = new LinkedHashMap<Integer,String>();

    private MyApplication mApplication;

    //信息抽取标签颜色
    private ArrayList<String> colors = new ArrayList<>();
    private SerializableSortMap colorsMap;
    private Map<Integer,String> colormap = new LinkedHashMap<Integer,String>();

    private HashMap<Integer,ArrayList<String>> colorhashMap = new HashMap<>();
    private HashMap<Integer,ArrayList<Integer>> beginhashMap = new HashMap<>();
    private HashMap<Integer,ArrayList<Integer>> endhashMap = new HashMap<>();
    private HashMap<Integer,ArrayList<Integer>> labelidhashMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dotask_extract);
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

        mApplication = (MyApplication)getApplication();
        userId = mApplication.getLoginUserId();

        //接收从上一个页面传递进来的参数
        Intent intent = getIntent();
        taskid = intent.getIntExtra("taskid",0);
        typename = intent.getStringExtra("type");
        Bundle tdbundle = intent.getExtras();
        //传送过来的做任务的文件内容
        SerializableMap fileMap = (SerializableMap) tdbundle.get("filemap");
        filemap = fileMap.getMap();
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
            downloadlabel.put(inststrmap.get(labelname),labelname);
            Log.e("ExtractActivity---->", "GET方式请求成功，标签--->" + labelname+inststrmap.get(labelname));
        }
        mLabelNames = inststrmap.keySet().toArray(new String[inststrmap.keySet().size()]);

        if(typename.equals("dotask")) {
            colors = tdbundle.getStringArrayList("colors");
            colorsMap = (SerializableSortMap) tdbundle.get("colormap");
            colormap = colorsMap.getMap();
        }

        getFileContent();

        mTabIndicator.setViewPager(mViewPager, 0);
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
                Toast.makeText(DoTaskExtractActivity.this,"没有获取读取手机权限，请到应用中心手动打开该权限",Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void initFragment(SerializableMap inststrMap){
        titles.clear();
        fragment_list.clear();
        for(int i=0;i<fragmentsize;i++){
            //导航栏加标题
            titles.add("任务"+contentindexs.get(i));
            //titles.add("第:"+(i+1)+"段");
            DoTaskExtractFragment f1 = DoTaskExtractFragment.newInstance(i);
            fragment_list.add(f1);
            Bundle bundle = new Bundle();
            //传递数据
            bundle.putInt("fragmentindex",i);
            bundle.putInt("taskid", taskid);
            bundle.putInt("docid", docId);
            //是做任务页面还是查看做任务页面
            bundle.putString("type",typename);
            //将map数据添加到封装的myMap中
            //labelMap.setMap(labelmap);
            //bundle.putSerializable("lebelmap", labelMap);
            bundle.putSerializable("lebelmap", inststrMap);
            bundle.putSerializable("colormap",colorsMap);
            bundle.putStringArrayList("colors",colors);
            String contentidstr = "contentid" + i;
            Log.e("DotaskExtract---->", "activity中的contentidstr--->" + contentidstr + "------" + contentids.get(i));
            bundle.putInt("contentid" + i, contentids.get(i));
            bundle.putInt("contentindex" + i, contentindexs.get(i));
            bundle.putString("content" + i, contents.get(i));
            bundle.putString("parastatus"+ i, parastatus.get(i));

            for(int contentid:colorhashMap.keySet()){
                if(contentid==contentids.get(i)){
                    bundle.putStringArrayList("colorlist",colorhashMap.get(contentid));
                    bundle.putIntegerArrayList("beginlist",beginhashMap.get(contentid));
                    bundle.putIntegerArrayList("endlist",endhashMap.get(contentid));
                    bundle.putIntegerArrayList("labelidlist",labelidhashMap.get(contentid));
                }
            }
            bundle.putInt("userid", userId);
            fragment_list.get(i).setArguments(bundle);
        }

        mDoTaskExtractAdapter = new DoTaskExtractAdapter(getSupportFragmentManager(),fragment_list);
        mDoTaskExtractAdapter.notifyDataSetChanged();
        mViewPager.setAdapter(mDoTaskExtractAdapter);
        mTabIndicator.setTitles(titles);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.tools);
        navigation.setOnNavigationItemSelectedListener(this);
    }

    private void getFileContent(){
        Log.e("mwx",Url.extradotaskUrl+"?docId="+docId+"&status="+docStatus+"&taskId="+taskid+"&userId="+userId);

        OkHttpUtil.sendGetRequest(Url.extradotaskUrl+"?docId="+docId+"&status="+docStatus+"&taskId="+taskid+"&userId="+userId, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DoTaskExtractActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onResponse(final Call call, Response response) throws IOException {
                String temp = response.body().string();
                Log.e("mwx",temp);
                JSONObject jsonObject = (JSONObject) JSON.parse(temp);

                JSONArray jsonArray = (JSONArray)jsonObject.get("data");
                fragmentsize = jsonArray.size();
                //清空content
                contentids.clear();
                contents.clear();
                contentindexs.clear();
                if(jsonArray!=null && jsonArray.size()>0){
                    for(int i=0;i<jsonArray.size();i++){
                        //遍历jsonarray数组，把每一个对象转成json对象
                        JSONObject job = jsonArray.getJSONObject(i);
                        if(job.get("pid")!=null) {
                            contentid = (Integer)job.get("pid");
                            contentids.add(contentid);
                            Log.e("DotaskExtract---->", "activity中的contentID--->" + contentid);
                        }
                        if(job.get("paracontent")!=null) {
                            content = (String)job.get("paracontent").toString();
                            contents.add(content);
                            Log.e("DotaskExtract---->", "activity中的content具体内容--->" + content);
                        }
                        if(job.get("paraindex")!=null) {
                            contentindex = (Integer)job.get("paraindex");
                            contentindexs.add(contentindex);
                            Log.e("DotaskExtract---->", "activity中的content具体内容--->" + contentindex);
                        }
                        if(job.get("dtstatus")!=null) {
                            parastatusstr = (String)job.get("dtstatus");
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
                                    //Log.e("DotaskExtract---->", "activity中的index_begins--->" + index_begins.get(j) + "------");
                                }
                                if (done.get("index_begin") != null) {
                                    index_begin = (Integer) done.get("index_begin");
                                    index_begins.add(index_begin);
                                    beginlist.add(index_begin);
                                    //Log.e("DotaskExtract---->", "activity中的index_begins--->" + index_begins.get(j) + "------");
                                }
                                if (done.get("index_end") != null) {
                                    index_end = (Integer) done.get("index_end");
                                    index_ends.add(index_end);
                                    endlist.add(index_end);
                                    //下载文件对应的抽取内容
                                    downloadcontent = content.substring(index_begin,index_end);
                                    //Log.e("DotaskExtract---->", "activity中的index_ends--->" + index_ends.get(j) + "------" );
                                }
                                if (done.get("label_id") != null) {
                                    label_id = (Integer) done.get("label_id");
                                    label_ids.add(label_id);
                                    labelidlist.add(label_id);
                                    //下载文件对应的标签名称
                                    downloadlabelname = downloadlabel.get(label_id);
                                    //Log.e("DotaskExtract---->", "activity中的label_id--->" + label_ids.get(j) + "------");
                                }
                                singlelinecontent = downloadfilename+"\t"+downloadlabelname+"\t"+downloadcontent+"\n";
                                downloadfilecontent.append(singlelinecontent);
                            }
                            colorhashMap.put(contentid,colorlist);
                            beginhashMap.put(contentid,beginlist);
                            endhashMap.put(contentid,endlist);
                            labelidhashMap.put(contentid,labelidlist);
                            for(int j=0;j<index_begins.size();j++){
                                Log.e("DotaskExtract---->", "activity中的index_beginsindex_ends--->" + index_begins.get(j) + "------" );
                            }
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
                final DoTaskExtractFragment currentFragment  = (DoTaskExtractFragment) mDoTaskExtractAdapter.getItem(fragmentIndex);

                String selectedContent = currentFragment.getSelectedContent();
                new XPopup.Builder(this).asCenterList("选中内容:"+selectedContent, mLabelNames,
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                Toast.makeText(DoTaskExtractActivity.this,mLabelNames[position]+" "+inststrmap.get(mLabelNames[position]),Toast.LENGTH_SHORT).show();
                                String  labelName = mLabelNames[position];
                                int labelId = inststrmap.get(labelName);
                                currentFragment.addTag(labelId,labelName);
                            }
                        }).show();
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
                                        Toast.makeText(DoTaskExtractActivity.this,"切换到" + text,Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(DoTaskExtractActivity.this, "显示全部段落",Toast.LENGTH_SHORT).show();
                                            getFileContent();

                                        }

                                        else if (position == 1){
                                            docStatus = status[1];
                                            Toast.makeText(DoTaskExtractActivity.this, "只显示未完成段落"+text,Toast.LENGTH_SHORT).show();
                                            getFileContent();
                                        }
                                        else if (position == 2) {
                                            int fragmentIndex = mViewPager.getCurrentItem();
                                            DoTaskExtractFragment currentFragment  = (DoTaskExtractFragment) mDoTaskExtractAdapter.getItem(fragmentIndex);
                                            currentFragment.completeCon();

                                        } else{
                                            int fragmentIndex = mViewPager.getCurrentItem();
                                            DoTaskExtractFragment currentFragment  = (DoTaskExtractFragment) mDoTaskExtractAdapter.getItem(fragmentIndex);
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
