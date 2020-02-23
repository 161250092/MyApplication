package com.example.kongmin.view.doTask.categoryMatch;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
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

import java.io.IOException;
import java.util.*;

import com.example.kongmin.Constant.Constant;
import com.example.kongmin.myapplication.R;
import com.example.kongmin.network.OkHttpUtil;
import com.example.kongmin.view.textcategory.MyTabIndicator;
import com.example.kongmin.util.MyApplication;
import com.example.kongmin.util.SerializableMap;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;

/*
 * 文本配对做任务新页面
 * update by mwx
 * 2020.2.10
 * */
public class MatchCategoryActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private MatchCategoryAdapter mMatchCategoryAdapter;

    private ViewPager mViewPager;

    //导航栏
    private MyTabIndicator mTabIndicator;

    //初始化数据
    public List<MatchCategoryFragment> fragment_list = new ArrayList<>();
    private List<String> titles = new ArrayList<>();

    //发送http请求获取到的数据
    //每个fragment显示一个instance
    private int instanceid;
    //每一个instance在一个文本里面的位置
    private int instanceindex;
    //itemid
    private int listitemid;
    //item的内容
    private String litemcontent;
    //每一个item在第几个list
    private int listindex;
    //每一个item在instance中的位置
    private int litemindex;

    //做任务要使用的数据
    private List<Integer> instanceids = new ArrayList<>();
    private List<Integer> instanceindexs = new ArrayList<>();

    private List<Integer> iteminstids = new ArrayList<>();
    private List<Integer> listitemids = new ArrayList<>();
    private List<String> litemcontents = new ArrayList<>();
    private List<Integer> listindexs = new ArrayList<>();
    private List<Integer> litemindexs = new ArrayList<>();

    //任务ID
    private int fragmentsize;

    //传送过来的做任务的文件内容
    private Map<String,Integer> filemap = new LinkedHashMap<String,Integer>();

    private List<Integer> fileid = new ArrayList<Integer>();
    private List<String> filename = new ArrayList<String>();
    private String[] mFileNames;
    private Integer[] mFileIds;

    private String status[] = {"全部", "进行中"};

    //任务ID
    private int taskid;
    //是做任务页面还是查看做任务页面
    private String typename;
    private int docId;
    //文件状态默认是全部
    private String docStatus = "全部";

    private String tasktype;
    private int selectedaboveitemid;

    private int doneaitemid;
    private int donebitemid;

    private List<Integer> doneinstids = new ArrayList<>();
    private List<Integer> doneaitemids = new ArrayList<>();
    private List<Integer> donebitemids = new ArrayList<>();

    private ArrayList<Integer> chuansdoneaitemids = new ArrayList<>();
    private ArrayList<Integer> chuansdonebitemids = new ArrayList<>();

    private MyApplication mApplication;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_task2);

        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

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
        tasktype = intent.getStringExtra("tasktype");
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
        mFileNames = filename.toArray(new String[filename.size()]) ;
        mFileIds = fileid.toArray(new Integer[fileid.size()]);

        LinearLayout extractlinear = findViewById(R.id.extractlinear);
        if(typename.equals("dotask")){
            extractlinear.setVisibility(View.GONE);
        }else{
            extractlinear.setVisibility(View.VISIBLE);
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

    public void initFragment(){
        titles.clear();
        fragment_list.clear();
        Log.e("mwx","init fragment");
        for(int i=0;i<fragmentsize;i++){
            //导航栏加标题
            titles.add("第"+instanceindexs.get(i)+"段");
            MatchCategoryFragment f1 = MatchCategoryFragment.newInstance(i);
            fragment_list.add(f1);
            Bundle bundle = new Bundle();
            //传递lebel数据
            bundle.putInt("fragmentindex",i);
            bundle.putInt("taskid", taskid);
            bundle.putInt("docid", docId);
            //是做任务页面还是查看做任务页面
            bundle.putString("type",typename);
            bundle.putInt("instanceid" + i,instanceids.get(i));
            bundle.putInt("instanceindex" + i,instanceindexs.get(i));
            bundle.putString("tasktype",tasktype);
            ArrayList<Integer> clistitemids1 = new ArrayList<>();
            ArrayList<String> clitemcontents1 = new ArrayList<>();
            ArrayList<Integer> clistindexs1 = new ArrayList<>();
            ArrayList<Integer> clitemindexs1 = new ArrayList<>();
            //list2
            ArrayList<Integer> clistitemids2 = new ArrayList<>();
            ArrayList<String> clitemcontents2 = new ArrayList<>();
            ArrayList<Integer> clistindexs2 = new ArrayList<>();
            ArrayList<Integer> clitemindexs2 = new ArrayList<>();

            //和item相关的参数
            for(int j=0;j<iteminstids.size();j++){
                if(iteminstids.get(j).equals(instanceids.get(i))){
                    if(listindexs.get(j)==1){
                        clistitemids1.add(listitemids.get(j));
                        clitemcontents1.add(litemcontents.get(j));
                        clistindexs1.add(listindexs.get(j));
                        clitemindexs1.add(litemindexs.get(j));
                    }else{
                        clistitemids2.add(listitemids.get(j));
                        clitemcontents2.add(litemcontents.get(j));
                        clistindexs2.add(listindexs.get(j));
                        clitemindexs2.add(litemindexs.get(j));
                    }
                }
            }

            for(int j=0;j<doneinstids.size();j++){
                if(doneinstids.get(j).equals(instanceids.get(i))){
                    chuansdoneaitemids.add(doneaitemids.get(j));
                    chuansdonebitemids.add(donebitemids.get(j));
                }
            }

            bundle.putIntegerArrayList("itemid1p"+i, clistitemids1);
            bundle.putStringArrayList("itemcon1p"+i,clitemcontents1);
            bundle.putIntegerArrayList("itemlist1p"+i, clistindexs1);
            bundle.putIntegerArrayList("iteminst1p"+i, clitemindexs1);

            bundle.putIntegerArrayList("itemid2p"+i, clistitemids2);
            bundle.putStringArrayList("itemcon2p"+i,clitemcontents2);
            bundle.putIntegerArrayList("itemlist2p"+i, clistindexs2);
            bundle.putIntegerArrayList("iteminst2p"+i, clitemindexs2);

            bundle.putIntegerArrayList("chuansdoneaitemids"+i, chuansdoneaitemids);
            bundle.putIntegerArrayList("chuansdonebitemids"+i, chuansdonebitemids);

            bundle.putInt("userid", userId);
            fragment_list.get(i).setArguments(bundle);
        }
        mMatchCategoryAdapter = new MatchCategoryAdapter(getSupportFragmentManager(),fragment_list);
        mMatchCategoryAdapter.notifyDataSetChanged();
        mViewPager.setAdapter(mMatchCategoryAdapter);
        mTabIndicator.setTitles(titles);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.tools);
        navigation.setOnNavigationItemSelectedListener(this);

        Log.e("mwx","init fragment end");
    }


    public void getFileContent(){
        String requestUrl = Constant.pairingfileUrl;
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
                String listitem = response.body().string();
                Log.e("mwx",listitem);
                JSONObject instanceItemjson = JSONObject.parseObject(listitem);
                JSONArray instanceItemArray = (JSONArray)instanceItemjson.get("instanceItem");
                fragmentsize = instanceItemArray.size();
                instanceids.clear();
                instanceindexs.clear();
                listitemids.clear();
                iteminstids.clear();
                litemcontents.clear();
                listindexs.clear();
                litemindexs.clear();
                if(instanceItemArray.size()>0){
                    for(int i=0;i<instanceItemArray.size();i++){
                        //遍历jsonarray数组，把每一个对象转成json对象
                        JSONObject job = instanceItemArray.getJSONObject(i);
                        //得到每个对象中的属性值
                        if(job.get("instid")!=null) {
                            //instance
                            instanceid = (Integer)job.get("instid");
                            instanceids.add(instanceid);
                            Log.e("DoTask2Activity---->", "activity中的instanceid--->" + instanceid);
                        }
                        if(job.get("instindex")!=null) {
                            //instance
                            String instindexstr = (String)job.get("instindex").toString();
                            instanceindex = Integer.valueOf(instindexstr);
                            instanceindexs.add(instanceindex);
                            Log.e("DoTask2Activity---->", "activity中的instanceindex--->" + instanceindex);
                        }
                        if(job.get("listitems")!=null) {
                            //instance
                            JSONArray itemList = (JSONArray)job.get("listitems");
                            for(int j=0;j<itemList.size();j++){
                                JSONObject item = itemList.getJSONObject(j);
                                if(item.get("ltid")!=null) {
                                    //instance
                                    listitemid = (Integer)item.get("ltid");
                                    listitemids.add(listitemid);
                                    iteminstids.add(instanceid);
                                    Log.e("DoTask2Activity---->", "activity中的listitemid--->" + listitemid);
                                }
                                if(item.get("litemcontent")!=null) {
                                    //instance
                                    litemcontent = (String)item.get("litemcontent").toString();
                                    litemcontents.add(litemcontent);
                                    Log.e("DoTask2Activity---->", "activity中的litemcontent--->" + litemcontent);
                                }
                                if(item.get("listIndex")!=null) {
                                    //instance
                                    String listindexstr = item.get("listIndex").toString();
                                    listindex = Integer.valueOf(listindexstr);
                                    listindexs.add(listindex);
                                    Log.e("DoTask2Activity---->", "activity中的listindex--->" + listindex);
                                }
                                if(item.get("litemindex")!=null) {
                                    //instance
                                    String litemindexstr = item.get("litemindex").toString();
                                    litemindex = Integer.valueOf(litemindexstr);
                                    litemindexs.add(litemindex);
                                    Log.e("DoTask2Activity---->", "activity中的litemindex--->" + litemindex);
                                }
                            }
                        }

                        //已经做过的部分
                        if(job.get("alreadyDone")!=null) {
                            JSONArray alreadyDonelArray = (JSONArray)job.get("alreadyDone");
                            if(alreadyDonelArray!=null&&alreadyDonelArray.size()>0){
                                for(int j=0;j<alreadyDonelArray.size();j++){
                                    //遍历jsonarray数组，把每一个对象转成json对象
                                    JSONObject alreadyDone = alreadyDonelArray.getJSONObject(j);
                                    //得到每个对象中的属性值
                                    if(alreadyDone.get("aLitemid")!=null) {
                                        //instance
                                        doneaitemid = (Integer)alreadyDone.get("aLitemid");
                                        doneinstids.add(instanceid);
                                        doneaitemids.add(doneaitemid);
                                        Log.e("DotaskOneCategory---->", "activity中的doneaitemid--->" + doneaitemid);
                                    }
                                    if(alreadyDone.get("bLitemid")!=null) {
                                        //instance
                                        donebitemid = (Integer)alreadyDone.get("bLitemid");
                                        donebitemids.add(donebitemid);
                                        Log.e("DotaskOneCategory---->", "activity中的donebitemid--->" + donebitemid);
                                    }
                                }
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
                MatchCategoryFragment currentFragment  = (MatchCategoryFragment)mMatchCategoryAdapter.getItem(fragmentIndex);
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
                                        Toast.makeText(MatchCategoryActivity.this,"切换到" + text,Toast.LENGTH_SHORT).show();
                                        docId = mFileIds[position];
                                        getFileContent();
                                    }
                                })
                        .show();

                return false;
            }

            case R.id.settings:{

                new XPopup.Builder(this)
                        .asBottomList("设置",new String[]{"显示全部段落","只显示未完成段落","结束该段","结束该文档"} ,
                                new OnSelectListener() {
                                    @Override
                                    public void onSelect(int position, String text) {

                                        if (position == 0) {
                                            docStatus = status[0];
                                            Toast.makeText(MatchCategoryActivity.this, "显示全部段落",Toast.LENGTH_SHORT).show();
                                            getFileContent();
                                        }

                                        else if (position == 1){
                                            docStatus = status[1];
                                            Toast.makeText(MatchCategoryActivity.this, "只显示未完成段落"+text,Toast.LENGTH_SHORT).show();
                                            getFileContent();
                                        }
                                        else if (position == 2) {
                                            int fragmentIndex = mViewPager.getCurrentItem();
                                            MatchCategoryFragment currentFragment  = (MatchCategoryFragment)mMatchCategoryAdapter.getItem(fragmentIndex);
                                            currentFragment.completeCon();

                                        } else{
                                            int fragmentIndex = mViewPager.getCurrentItem();
                                            MatchCategoryFragment currentFragment  = (MatchCategoryFragment)mMatchCategoryAdapter.getItem(fragmentIndex);
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