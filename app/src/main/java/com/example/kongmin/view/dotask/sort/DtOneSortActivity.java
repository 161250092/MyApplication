package com.example.kongmin.view.doTask.sort;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.kongmin.Constant.Constant;
import com.example.kongmin.myapplication.R;
import com.example.kongmin.network.OkHttpUtil;
import com.example.kongmin.view.textcategory.MyTabIndicator;
import com.example.kongmin.util.*;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 文本排序或文本类比排序
 */
public class DtOneSortActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private OneSortAdapter mOneSortAdapter;

    private ViewPager mViewPager;

    //导航栏
    private MyTabIndicator mTabIndicator;

    //初始化数据
    private List<OneSortFragment> fragment_list = new ArrayList<OneSortFragment>();
    private List<String> titles = new ArrayList<>();

    private int instanceid;
    //每一个instance在一个文本里面的位置
    private int instanceindex;
    //itemid
    private int itemid;
    //item的内容
    private String itemcontent;
    //每一个item在第几个list
    private int itemindex;

    //做任务要使用的数据
    private List<Integer> instanceids = new ArrayList<>();
    private List<Integer> instanceindexs = new ArrayList<>();

    private List<Integer> iteminstids = new ArrayList<>();
    private List<Integer> itemids = new ArrayList<>();
    private List<String> itemcontents = new ArrayList<>();
    private List<Integer> itemindexs = new ArrayList<>();

    private int fragmentsize;

    //传送过来的做任务的文件内容
    private Map<String,Integer> filemap = new LinkedHashMap<String,Integer>();

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

    //已经排好序的做任务的结果
    private ArrayList<Integer> sortedindex = new ArrayList<Integer>();
    private ArrayList<Integer> sorteditemId = new ArrayList<Integer>();

    private Map<Integer,List<Integer>> sortedindexmap = new HashMap<>();
    private Map<Integer,List<Integer>> sorteditemIdmap = new HashMap<>();

    //存放itemID和content
    private  SerializableSortMap sortMap  = new SerializableSortMap();;
    private Map<Integer,String> sortmap = new LinkedHashMap<Integer,String>();

    //和选择弹出框相关的
    private String status[] = {"全部", "进行中"};

    //任务ID
    private int taskid;
    //是做任务页面还是查看做任务页面
    private String typename;
    private int docId;
    //文件状态默认是全部
    private String docStatus = "全部";

    private TextView downloadextract;

    private String filePath = "/sdcard/xinjian/";

    private String filenameTemp = filePath+ "信息抽取"+ ".txt";

    private StringBuffer downloadfilecontent = new StringBuffer();
    private String singlelinecontent;

    private String downloadfilename;

    private MyApplication mApplication;
    //todo 设置userId
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dt_one_sort);

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

        getFileContent();

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
                    Toast.makeText(DtOneSortActivity.this,"点击了下载按钮",Toast.LENGTH_LONG).show();
                }
            });
        }

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
                Toast.makeText(DtOneSortActivity.this,"没有获取读取手机权限，请到应用中心手动打开该权限",Toast.LENGTH_SHORT).show();
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


    public void initFragment(){
        titles.clear();
        fragment_list.clear();
        for(int i=0;i<fragmentsize;i++){
            //导航栏加标题
            titles.add("第"+instanceindexs.get(i)+"段");
            OneSortFragment f1 = OneSortFragment.newInstance(i);
            fragment_list.add(f1);
            Bundle bundle = new Bundle();
            //传递数据
            bundle.putInt("fragmentindex",i);
            bundle.putInt("taskid", taskid);
            bundle.putInt("instanceid" + i,instanceids.get(i));
            bundle.putInt("instanceindex" + i,instanceindexs.get(i));
            bundle.putInt("docid", docId);
            //是做任务页面还是查看做任务页面
            bundle.putString("type",typename);
            ArrayList<Integer> citemids = new ArrayList<>();
            ArrayList<String> citemcontents = new ArrayList<>();
            ArrayList<Integer> citemindexs = new ArrayList<>();
            sortmap.clear();
            //和item相关的参数
            for(int j=0;j<iteminstids.size();j++){
                if(iteminstids.get(j).equals(instanceids.get(i))){
                    citemids.add(itemids.get(j));
                    citemcontents.add(itemcontents.get(j));
                    citemindexs.add(itemindexs.get(j));
                    //sortmap.put(itemids.get(j),itemcontents.get(j));
                }
            }

            bundle.putIntegerArrayList("itemidp"+i,citemids);
            bundle.putStringArrayList("itemconp"+i,citemcontents);
            bundle.putIntegerArrayList("itemindexp"+i,citemindexs);

            //如果该段已经排好序
            if(sortedindexmap.containsKey(instanceids.get(i))){
                bundle.putString("issorted"+i,"true");
                //sortedindexmap.get(instanceids.get(i));
                bundle.putIntegerArrayList("sortedindex"+i,(ArrayList)sortedindexmap.get(instanceids.get(i)));
                bundle.putIntegerArrayList("sorteditemId"+i,(ArrayList)sorteditemIdmap.get(instanceids.get(i)));
                Log.e("DtOneSortActivity---->", "GET方式请求成功,issorted+i--->"+"issorted"+i+"-----"+sortedindexmap.get(instanceids.get(i)) +sorteditemIdmap.get(35));
            }else{
                bundle.putString("issorted"+i,"false");
            }

            Log.e("DtOneSortActivity---->", "GET方式请求成功，itemid--->"+citemids);
            Log.e("DtOneSortActivity---->", "GET方式请求成功，itemcontent--->"+citemcontents);
            Log.e("DtOneSortActivity---->", "GET方式请求成功，itemidex--->"+citemindexs);

            bundle.putString("parastatus"+ i, parastatus.get(i));

            bundle.putInt("userid", userId);
            fragment_list.get(i).setArguments(bundle);
        }
        mOneSortAdapter = new OneSortAdapter(getSupportFragmentManager(),fragment_list);
        mOneSortAdapter.notifyDataSetChanged();
        mViewPager.setAdapter(mOneSortAdapter);
        mTabIndicator.setTitles(titles);
    }

    public void getFileContent(){
        final String requestUrl = Constant.DotaskOneSortRequestUrl;
        String paramUrl;
        if(typename.equals("dotask")){
            paramUrl = "?docId="+docId+"&status="+docStatus+"&taskId="+taskid+"&userId="+userId;
        }else{
            paramUrl = "?docId="+docId+"&status="+docStatus+"&taskId="+taskid+"&userId="+userId;
            Log.e("ExtractActivity---->", "GET方式请求成功，result2---> 查看我做的任务");
        }

        OkHttpUtil.sendGetRequest(requestUrl + paramUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();
                JSONObject instanceItemjson = JSONObject.parseObject(result);
                JSONArray instanceItemArray = (JSONArray)instanceItemjson.get("instanceItem");
                fragmentsize = instanceItemArray.size();
                //清空instance和item
                instanceids.clear();
                instanceindexs.clear();
                itemids.clear();
                iteminstids.clear();
                itemcontents.clear();
                itemindexs.clear();
                //sortedindexmap.clear();
                if(instanceItemArray!=null&&instanceItemArray.size()>0){
                    for(int i=0;i<instanceItemArray.size();i++){
                        //遍历jsonarray数组，把每一个对象转成json对象
                        JSONObject job = instanceItemArray.getJSONObject(i);
                        //得到每个对象中的属性值
                        if(job.get("instid")!=null) {
                            //instance
                            instanceid = (Integer)job.get("instid");
                            instanceids.add(instanceid);
                            Log.e("DtOneSortActivity---->", "activity中的instanceid--->" + instanceid);
                        }
                        if(job.get("instindex")!=null) {
                            //instance
                            instanceindex = (Integer) job.get("instindex");
                            instanceindexs.add(instanceindex);
                            Log.e("DtOneSortActivity---->", "activity中的instanceindex--->" + instanceindex);
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
                                    iteminstids.add(instanceid);
                                    Log.e("DtOneSortActivity---->", "activity中的listitemid--->" + itemid);
                                }
                                if(item.get("itemcontent")!=null) {
                                    //instance
                                    itemcontent = (String)item.get("itemcontent").toString();
                                    itemcontents.add(itemcontent);
                                    Log.e("DtOneSortActivity---->", "activity中的litemcontent--->" + itemcontent);
                                }
                                if(item.get("itemindex")!=null) {
                                    //instance
                                    String listindexstr = item.get("itemindex").toString();
                                    itemindex = Integer.valueOf(listindexstr);
                                    itemindexs.add(itemindex);
                                    hashMap.put(itemid,itemcontent);
                                    Log.e("DtOneSortActivity---->", "activity中的listindex--->" + itemindex);
                                }
                            }
                        }

                        TreeMap<Integer,String> treeMap = new  TreeMap<Integer,String>();
                        if(job.get("alreadyDone")!=null) {
                            //instance
                            JSONArray alreadyDonelArray = (JSONArray)job.get("alreadyDone");
                            if(alreadyDonelArray.size()>0) {
                                for (int j = 0; j < alreadyDonelArray.size(); j++) {
                                    JSONObject item = alreadyDonelArray.getJSONObject(j);
                                    int index = 0;
                                    if (item.get("newindex") != null) {
                                        index = (Integer) item.get("newindex");
                                        sortedindex.add(index);
                                        Log.e("DoTask2Activity---->", "activity中的newindex--->" + index);
                                    }
                                    if (item.get("itemId") != null) {
                                        int itemId = (Integer)item.get("itemId");
                                        sorteditemId.add(itemId);
                                        treeMap.put(index,hashMap.get(itemId));
                                        Log.e("DoTask2Activity---->", "activity中的itemId--->" + itemId);
                                    }
                                }

                                sortedindexmap.put(instanceid,sortedindex);
                                sorteditemIdmap.put(instanceid,sorteditemId);

                                for(int index:hashMap.keySet()){
                                    Log.e("DoTask2Activity---->", "hashMap--->" + index + hashMap.get(index));
                                }

                                for(int index:treeMap.keySet()){
                                    Log.e("DoTask2Activity---->", "treeMap--->" + index + treeMap.get(index));
                                }

                                StringBuffer itemcontent = new StringBuffer();
                                for(int index:treeMap.keySet()){
                                    itemcontent.append(downloadfilename+"\t"+(index+1)+"\t"+treeMap.get(index)+"\n");
                                }
                                singlelinecontent = itemcontent.toString()+"-----------------\n";
                                downloadfilecontent.append(singlelinecontent);

                            }
                        }
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initFragment();
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
                OneSortFragment currentFragment  = (OneSortFragment)mOneSortAdapter.getItem(fragmentIndex);
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
                                        Toast.makeText(DtOneSortActivity.this,"切换到" + text,Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(DtOneSortActivity.this, "只显示未完成段落",Toast.LENGTH_SHORT).show();
                                            getFileContent();
                                        }

                                        else if (position == 1){
                                            docStatus = status[1];
                                            Toast.makeText(DtOneSortActivity.this, "显示全部段落"+text,Toast.LENGTH_SHORT).show();
                                            getFileContent();
                                        }

                                        else if (position == 2) {
                                            int fragmentIndex = mViewPager.getCurrentItem();
                                            OneSortFragment currentFragment  = (OneSortFragment)mOneSortAdapter.getItem(fragmentIndex);
                                            currentFragment.completeCon();

                                        } else{
                                            int fragmentIndex = mViewPager.getCurrentItem();
                                            OneSortFragment currentFragment  = (OneSortFragment)mOneSortAdapter.getItem(fragmentIndex);
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
