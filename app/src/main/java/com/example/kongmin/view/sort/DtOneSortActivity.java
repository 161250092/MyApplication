package com.example.kongmin.view.sort;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.kongmin.Constant.Constant;
import com.example.kongmin.myapplication.R;
import com.example.kongmin.view.textcategory.DropDownMenu;
import com.example.kongmin.view.textcategory.MyTabIndicator;
import com.example.kongmin.view.textcategory.util.ListDropDownAdapter;
import com.example.kongmin.util.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;


public class DtOneSortActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private OneSortAdapter mOneSortAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    //导航栏
    private MyTabIndicator mTabIndicator;

    //初始化数据
    private List<OneSortFragment> list = new ArrayList<OneSortFragment>();
    private List<String> titles = new ArrayList<>();

    //任务ID
    //private int taskid;
    //private int docId;

    //private int fragmentsize;

    //发送http请求获取到的数据
    //每个fragment显示一个instance
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

    private List<Integer> fileid = new ArrayList<Integer>();
    private List<String> filename = new ArrayList<String>();
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
    @InjectView(R.id.dropDownMenu)
    DropDownMenu mDropDownMenu;
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



    private TextView downloadextract;

    private String filePath = "/sdcard/xinjian/";

    private String filenameTemp = filePath+ "信息抽取"+ ".txt";

    private StringBuffer downloadfilecontent = new StringBuffer();
    private String singlelinecontent;

    private String downloadfilename;
    private String downloadcontent;

    private MyApplication mApplication;
    //todo 设置userId
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dt_one_sort);
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
                    Toast.makeText(DtOneSortActivity.this,"点击了下载按钮",Toast.LENGTH_LONG).show();
                }
            });
        }


        initFragment();

        /*for(int i=0;i<fragmentsize;i++){
            //导航栏加标题
            titles.add("第"+instanceindexs.get(i)+"段");
            //titles.add("第:"+(i+1)+"段");
            OneSortFragment f1 = OneSortFragment.newInstance(i);
            list.add(f1);

            Bundle bundle = new Bundle();
            //传递lebel数据
            bundle.putInt("fragmentindex",i);
            //todo 设置taskid
            taskid = 12;
            bundle.putInt("taskid", taskid);

            bundle.putInt("instanceid" + i,instanceids.get(i));
            bundle.putInt("instanceindex" + i,instanceindexs.get(i));

            ArrayList<Integer> citemids = new ArrayList<>();
            ArrayList<String> citemcontents = new ArrayList<>();
            ArrayList<Integer> citemindexs = new ArrayList<>();

            //和item相关的参数
            for(int j=0;j<iteminstids.size();j++){
                if(iteminstids.get(j).equals(instanceids.get(i))){
                        citemids.add(itemids.get(j));
                        citemcontents.add(itemcontents.get(j));
                        citemindexs.add(itemindexs.get(j));
                }
            }
            bundle.putIntegerArrayList("itemidp"+i,citemids);
            bundle.putStringArrayList("itemconp"+i,citemcontents);
            bundle.putIntegerArrayList("itemindexp"+i,citemindexs);

            Log.e("DtOneSortActivity---->", "GET方式请求成功，itemid--->"+citemids);
            Log.e("DtOneSortActivity---->", "GET方式请求成功，itemcontent--->"+citemcontents);
            Log.e("DtOneSortActivity---->", "GET方式请求成功，itemidex--->"+citemindexs);

            //todo 获取用户ID
            bundle.putInt("userid", 1);
            list.get(i).setArguments(bundle);
        }*/

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        //先注释掉
        //mOneSortAdapter = new OneSortAdapter(getSupportFragmentManager(),list);


        // Set up the ViewPager with the sections adapter.
        //mViewPager = (ViewPager) findViewById(R.id.container);
        //先注释掉
        //mViewPager.setAdapter(mOneSortAdapter);

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

        mTabIndicator.setOnPageChangeListener(new MyTabIndicator.PageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                Toast.makeText(DtOneSortActivity.this, "选择了：" + position, Toast.LENGTH_SHORT).show();
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





    public void initFragment(){
        titles.clear();
        list.clear();
        for(int i=0;i<fragmentsize;i++){
            //导航栏加标题
            titles.add("第:"+instanceindexs.get(i)+"段");
            OneSortFragment f1 = OneSortFragment.newInstance(i);
            list.add(f1);
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
            //sortMap.setMap(sortmap);
            //bundle.putSerializable("sortmap"+i, sortMap);


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
            if(parastatus.get(i).equals("已完成")){
               /*for(int j=0;j<index_begins.size();j++){
                    Log.e("DotaskExtract---->", "activity中的index_beginsindex_ends--->" + index_begins.get(j) + "------" );
                }
                //把标注好的标签信息传过去
                label_idList.setList(label_ids);
                bundle.putSerializable("label_ids",label_idList);*/
            }

            //todo 获取用户ID
            bundle.putInt("userid", 1);
            list.get(i).setArguments(bundle);
        }
        mOneSortAdapter = new OneSortAdapter(getSupportFragmentManager(),list);
        mOneSortAdapter.notifyDataSetChanged();
        mViewPager.setAdapter(mOneSortAdapter);
        mTabIndicator.setTitles(titles);
    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // TODO: http request.
            String requestUrl = Constant.DotaskOneSortRequestUrl;
            //String paramUrl = "?docId="+docId+"&status="+docStatus+"&taskId="+taskid;
            String paramUrl;
            if(typename.equals("dotask")){
                paramUrl = "?docId="+docId+"&status="+docStatus+"&taskId="+taskid+"&userId="+userId;
            }else{
                paramUrl = "?docId="+docId+"&status="+docStatus+"&taskId="+taskid+"&userId="+userId;
                Log.e("ExtractActivity---->", "GET方式请求成功，result2---> 查看我做的任务");
            }
            String result = HttpUtil.requestGet(requestUrl,paramUrl);
            Log.e("DotaskOneSort---->", "Post方式请求成功，result--->" + result);
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
                    //sortedindex.clear();
                    //sorteditemId.clear();
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

                            Log.e("DoTask2Activity---->", "activity中的sorteditemIdmap--->" + sortedindexmap.get(35));
                            Log.e("DoTask2Activity---->", "activity中的sorteditemIdmap--->" + sorteditemIdmap.get(35));
                        }
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
                        initFragment();
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
                        initFragment();
                    }
                }else if(btnfileid!=-1 && !filestatus.equals("")){
                    Toast.makeText(DtOneSortActivity.this, "选择了文件：" + btnfileid+filestatus, Toast.LENGTH_SHORT).show();
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
                    initFragment();
                    //}
                }
                mDropDownMenu.closeMenu();
            }
        });

        //init context view
        TextView contentView = new TextView(this);
        contentView.setHeight(0);


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
