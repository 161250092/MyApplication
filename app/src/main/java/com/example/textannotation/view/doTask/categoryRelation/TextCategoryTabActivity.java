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
import com.example.textannotation.view.doTask.sort.OneSortFragment;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.BottomListPopupView;
import com.lxj.xpopup.impl.LoadingPopupView;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
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


    //传送过来的做任务的文件内容
    private Map<String,Integer> filemap = new LinkedHashMap<String,Integer>();
    //传送过来的做任务的标签内容
    private  SerializableMap inststrMap;

    private List<Integer> fileid = new ArrayList<Integer>();
    private List<String> filename = new ArrayList<String>();

    private String[] mFileNames;
    private Integer[] mFileIds;

    //任务ID
    private int taskid;

    private int docId;
    //文件状态默认是全部

    //传送过来的item1做任务的标签内容
    private  SerializableMap item1strMap;

    //传送过来的item2做任务的标签内容
    private  SerializableMap item2strMap;


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
                PlaceholderFragment currentFragment  = (PlaceholderFragment)mSectionsPagerAdapter.getItem(fragmentIndex);
                currentFragment.submitErrors(text);
            }
        }).show();
    }

    private void getOthersAnnotation(){
        int fragmentIndex = mViewPager.getCurrentItem();
        PlaceholderFragment currentFragment  = (PlaceholderFragment)mSectionsPagerAdapter.getItem(fragmentIndex);
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
                PlaceholderFragment currentFragment  = (PlaceholderFragment)mSectionsPagerAdapter.getItem(fragmentIndex);
                showLoading("获取下一个任务");
                currentFragment.doNextTask();
            }
        }).show();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_category_tab);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        mTabIndicator = (MyTabIndicator) findViewById(R.id.mTabIndicator);
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);

        // 获取整个应用的Application对象
        // 在不同的Activity中获取的对象是同一个
        mApplication = (MyApplication)getApplication();
        userId = mApplication.getLoginUserId();

        //接收从上一个页面传递进来的参数
        Intent intent = getIntent();
        taskid = intent.getIntExtra("taskid",0);
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
        //传送过来的做任务的标签内容
        inststrMap = (SerializableMap) tdbundle.get("instlabel");
        item1strMap = (SerializableMap) tdbundle.get("item1label");
        item2strMap = (SerializableMap) tdbundle.get("item2label");

        initFragment();

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


    public void initFragment(){
        Log.e("mwx","1");
        fragment_list.clear();
        PlaceholderFragment f1 = PlaceholderFragment.newInstance(0);
        fragment_list.add(f1);

        Bundle bundle = new Bundle();
        bundle.putInt("taskid", taskid);
        bundle.putInt("docid", docId);
        bundle.putInt("userid",userId);

        bundle.putSerializable("instancelebelmap", inststrMap);
        bundle.putSerializable("item1labelmap", item1strMap);
        bundle.putSerializable("item2labelmap", item2strMap);
        fragment_list.get(0).setArguments(bundle);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),fragment_list);
        mSectionsPagerAdapter.notifyDataSetChanged();
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){

            case R.id.upload_paragraph: {
                int fragmentIndex = mViewPager.getCurrentItem();
                PlaceholderFragment currentFragment  = (PlaceholderFragment)mSectionsPagerAdapter.getItem(fragmentIndex);
                currentFragment.saveAnnotationInfo();
                return  true;
            }

            case R.id.next: {
                int fragmentIndex = mViewPager.getCurrentItem();
                PlaceholderFragment currentFragment  = (PlaceholderFragment)mSectionsPagerAdapter.getItem(fragmentIndex);
                showLoading("获取下一个任务");
                currentFragment.doNextTask();
                return false;
            }

            case R.id.pass_task: {
                int fragmentIndex = mViewPager.getCurrentItem();
                PlaceholderFragment currentFragment  = (PlaceholderFragment)mSectionsPagerAdapter.getItem(fragmentIndex);
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
                                        Toast.makeText(TextCategoryTabActivity.this,"切换到" + text,Toast.LENGTH_SHORT).show();
                                        docId = mFileIds[position];
                                        initFragment();
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
