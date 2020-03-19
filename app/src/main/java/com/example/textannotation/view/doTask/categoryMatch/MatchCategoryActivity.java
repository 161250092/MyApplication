package com.example.textannotation.view.doTask.categoryMatch;

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

import com.example.textannotation.Constant.Constant;
import com.example.textannotation.myapplication.R;
import com.example.textannotation.network.OkHttpUtil;
import com.example.textannotation.view.commonVIew.MyTabIndicator;
import com.example.textannotation.util.MyApplication;
import com.example.textannotation.util.SerializableMap;
import com.example.textannotation.view.doTask.categoryRelation.PlaceholderFragment;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.BottomListPopupView;
import com.lxj.xpopup.impl.LoadingPopupView;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
import com.lxj.xpopup.interfaces.OnSelectListener;

/*
 * 文本配对做任务新页面
 * update by mwx
 * 2020.3.16
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

    private int doneaitemid;
    private int donebitemid;

    private List<Integer> doneinstids = new ArrayList<>();
    private List<Integer> doneaitemids = new ArrayList<>();
    private List<Integer> donebitemids = new ArrayList<>();

    private ArrayList<Integer> chuansdoneaitemids = new ArrayList<>();
    private ArrayList<Integer> chuansdonebitemids = new ArrayList<>();

    private MyApplication mApplication;
    private int userId;

    BottomNavigationView navigation;

    private LoadingPopupView loadingPopupView;
    public void showLoading(String msg){
        loadingPopupView.setTitle(msg);
        loadingPopupView.show();
    }

    public void hideLoading(){
        loadingPopupView.dismiss();
    }


    private BottomListPopupView settingView;
    private void  initBottomListPopupView(){
        settingView = new XPopup.Builder(this)
                .asBottomList("设置", new String[]{"提交错误信息", "与其他人比较"}, new OnSelectListener() {
                    @Override
                    public void onSelect(int position, String text) {
                        switch (position){
                            case 0:
                                submitError();
                                break;
                            case 1:
                                getOthersAnnotation();
                                break;
                        }
                    }
                });
    }

    private void submitError( ){
        new XPopup.Builder(this).asInputConfirm("提交错误信息","具体信息", new OnInputConfirmListener() {
            @Override
            public void onConfirm(String text) {
                int fragmentIndex = mViewPager.getCurrentItem();
                MatchCategoryFragment currentFragment  = (MatchCategoryFragment)mMatchCategoryAdapter.getItem(fragmentIndex);
                currentFragment.submitErrors(text);
            }
        }).show();
    }

    private void getOthersAnnotation(){
        int fragmentIndex = mViewPager.getCurrentItem();
        MatchCategoryFragment currentFragment  = (MatchCategoryFragment)mMatchCategoryAdapter.getItem(fragmentIndex);
        currentFragment.compareOthersTextAnnotation();
    }

    public void showNotice(String title,String msg){
        new XPopup.Builder(this).asConfirm(title, msg, new OnConfirmListener() {
            @Override
            public void onConfirm() {

            }
        }).show();
    }

    public void uploadInfo(String msg){
        new XPopup.Builder(this).asConfirm("提交情况",msg , new OnConfirmListener() {
            @Override
            public void onConfirm() {
                int fragmentIndex = mViewPager.getCurrentItem();
                MatchCategoryFragment currentFragment  = (MatchCategoryFragment)mMatchCategoryAdapter.getItem(fragmentIndex);
                showLoading("获取下一个任务");
                currentFragment.doNextTask();
            }
        }).show();

    }

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

        initWorkPlaceFragment();

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

        navigation = (BottomNavigationView) findViewById(R.id.tools);
        navigation.setOnNavigationItemSelectedListener(this);
        loadingPopupView = new XPopup.Builder(this).asLoading("loading");
        initBottomListPopupView();
        initTitle();
    }
    private void initTitle(){
        titles.add("当前任务");
        mTabIndicator.setTitles(titles);
        mTabIndicator.setViewPager(mViewPager, 0);
    }

    public void initWorkPlaceFragment(){
        titles.clear();
        fragment_list.clear();
        Log.e("mwx","init fragment");

        MatchCategoryFragment f1 = MatchCategoryFragment.newInstance(0);
        fragment_list.add(f1);
        Bundle bundle = new Bundle();
        //传递lebel数据
        bundle.putInt("taskid", taskid);
        bundle.putInt("docid", docId);
        bundle.putInt("userid", userId);
        bundle.putString("tasktype",tasktype);
        fragment_list.get(0).setArguments(bundle);

        mMatchCategoryAdapter = new MatchCategoryAdapter(getSupportFragmentManager(),fragment_list);
        mMatchCategoryAdapter.notifyDataSetChanged();
        mViewPager.setAdapter(mMatchCategoryAdapter);
        Log.e("mwx","init fragment end");
    }




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){
            case R.id.upload_paragraph: {
                int fragmentIndex = mViewPager.getCurrentItem();
                Log.e("mwx",fragmentIndex+"");
                MatchCategoryFragment currentFragment  = (MatchCategoryFragment)mMatchCategoryAdapter.getItem(fragmentIndex);
                currentFragment.saveAnnotationInfo();
                return  true;
            }

            case R.id.next: {
                int fragmentIndex = mViewPager.getCurrentItem();
                MatchCategoryFragment currentFragment  = (MatchCategoryFragment)mMatchCategoryAdapter.getItem(fragmentIndex);
                showLoading("获取下一个任务");
                currentFragment.doNextTask();
                return false;
            }

            case R.id.pass_task: {
                int fragmentIndex = mViewPager.getCurrentItem();
                MatchCategoryFragment currentFragment  = (MatchCategoryFragment)mMatchCategoryAdapter.getItem(fragmentIndex);
                showLoading("跳过当前任务");
                currentFragment.passCurrentTask();
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
                                        initWorkPlaceFragment();
                                    }
                                })
                        .show();

                return false;
            }

            case R.id.settings:{

                settingView.show();
                return false;
            }
        }

        return true;
    }
}