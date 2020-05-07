package com.example.textannotation.view.doTask.textCategory;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.textannotation.myapplication.R;
import com.example.textannotation.view.common.MyTabIndicator;
import com.example.textannotation.util.*;
import com.example.textannotation.view.doTask.BaseWorkingActivity;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.LoadingPopupView;
import com.lxj.xpopup.interfaces.OnConfirmListener;

import java.util.*;

/**
 *
 * 分类任务界面
 * update by mwx
 * 2020.2.10
 */

public class TextCategoryActivity extends BaseWorkingActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private TextCategoryAdapter mTextCategoryAdapter;

    private ViewPager mViewPager;
    //导航栏
    private MyTabIndicator mTabIndicator;
    //初始化数据
    private List<TextCategoryFragment> fragment_list = new ArrayList<TextCategoryFragment>();
    private List<String> titles = new ArrayList<>();
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_category_tab);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.tools);
        navigation.setOnNavigationItemSelectedListener(this);

        mTabIndicator = (MyTabIndicator) findViewById(R.id.mTabIndicator);
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);
        // 获取整个应用的Application对象
        mApplication = (MyApplication)getApplication();
        userId = mApplication.getLoginUserId();

        dealIntent();
        initFragment();
        mTabIndicator.setViewPager(mViewPager, 0);
        loadingPopupView = new XPopup.Builder(this).asLoading("loading");
    }

    //初始化一个FRAGMENT，用以存放文本信息和标签
    public void initFragment(){
        titles.clear();
        fragment_list.clear();
        titles.add("根据文本选择标签");
        TextCategoryFragment f1 = TextCategoryFragment.newInstance(0);
        fragment_list.add(f1);
        Bundle bundle = new Bundle();
        //传递lebel数据
        bundle.putInt("taskid", taskid);
        bundle.putInt("docid", docId);
        bundle.putInt("userid",userId);
        bundle.putSerializable("lebelmap",inststrMap);
        fragment_list.get(0).setArguments(bundle);

        mTextCategoryAdapter = new TextCategoryAdapter(getSupportFragmentManager(),fragment_list);
        mTextCategoryAdapter.notifyDataSetChanged();
        mViewPager.setAdapter(mTextCategoryAdapter);
        mTabIndicator.setTitles(titles);
    }

    @Override
    public void dealIntent() {
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
        inststrmap = inststrMap.getMap();
        for(String labelname : inststrmap.keySet()){
            Log.e("ExtractActivity---->", "GET方式请求成功，标签--->" + labelname+inststrmap.get(labelname));
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
        switch (menuItem.getItemId()) {
            case R.id.upload_paragraph: {
                int fragmentIndex = mViewPager.getCurrentItem();
                Log.e("mwx", fragmentIndex + "");
                TextCategoryFragment currentFragment = (TextCategoryFragment) mTextCategoryAdapter.getItem(fragmentIndex);
                currentFragment.saveAnnotationInfo();
                return true;
            }

            case R.id.before:{
                int fragmentIndex = mViewPager.getCurrentItem();
                TextCategoryFragment currentFragment  = (TextCategoryFragment) mTextCategoryAdapter.getItem(fragmentIndex);
                showLoading("获取上一个任务");
                currentFragment.getLastTask();
                return false;
            }

            case R.id.next: {
                int fragmentIndex = mViewPager.getCurrentItem();
                TextCategoryFragment currentFragment  = (TextCategoryFragment) mTextCategoryAdapter.getItem(fragmentIndex);
                showLoading("获取下一个任务");
                currentFragment.getNextTask();
                return false;
            }
        }

        return  true;
    }


    private LoadingPopupView loadingPopupView;
    public void showLoading(String msg){
        loadingPopupView.setTitle(msg);
        loadingPopupView.show();
    }
    public void hideLoading(){
        loadingPopupView.dismiss();
    }


    public void uploadInfo(String msg){
        new XPopup.Builder(this).asConfirm("提交情况",msg , new OnConfirmListener() {
            @Override
            public void onConfirm() {
                int fragmentIndex = mViewPager.getCurrentItem();
                TextCategoryFragment currentFragment  = (TextCategoryFragment) mTextCategoryAdapter.getItem(fragmentIndex);
                showLoading("获取下一个任务");
                currentFragment.getNextTask();
            }
        }).show();
    }

    public void simpleInfo(String msg){
        new XPopup.Builder(this).asConfirm("",msg , new OnConfirmListener() {
            @Override
            public void onConfirm() {
            }
        }).show();
    }

    public void showCompletedTaskInfo(){
        new XPopup.Builder(this).asConfirm("无任务","该文档已经完成" , new OnConfirmListener() {
            @Override
            public void onConfirm() {
                TextCategoryActivity.this.finish();
            }
        }).show();

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                TextCategoryActivity.this.finish();
            }
        },2000)   ;

    }



}
