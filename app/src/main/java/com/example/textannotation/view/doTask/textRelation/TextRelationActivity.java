package com.example.textannotation.view.doTask.textRelation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.example.textannotation.myapplication.R;
import com.example.textannotation.util.MyApplication;
import com.example.textannotation.util.SerializableMap;
import com.example.textannotation.view.common.MyTabIndicator;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.LoadingPopupView;
import com.lxj.xpopup.interfaces.OnConfirmListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * 文本关系
 * update by mwx
 * 2020.2.10
 */

public class TextRelationActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

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


    public void showNotice(String title,String msg){
        new XPopup.Builder(this).asConfirm(title, msg, new OnConfirmListener() {
            @Override
            public void onConfirm() {

            }
        }).show();
    }

    public void showCompletedNotice(String title,String msg){
        new XPopup.Builder(this).asConfirm(title, msg, new OnConfirmListener() {
            @Override
            public void onConfirm() {
                TextRelationActivity.this.finish();
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
                currentFragment.getNextTask();
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
        initTitle();
    }


    private void initTitle(){
        titles.add("选择文本间的关系");
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

            case R.id.before: {
                int fragmentIndex = mViewPager.getCurrentItem();
                PlaceholderFragment currentFragment  = (PlaceholderFragment)mSectionsPagerAdapter.getItem(fragmentIndex);
                showLoading("获取上一个任务");
                currentFragment.getLastTask();
                return false;
            }

            case R.id.next: {
                int fragmentIndex = mViewPager.getCurrentItem();
                PlaceholderFragment currentFragment  = (PlaceholderFragment)mSectionsPagerAdapter.getItem(fragmentIndex);
                showLoading("获取下一个任务");
                currentFragment.getNextTask();
                return false;
            }


        }

        return true;
    }
}
