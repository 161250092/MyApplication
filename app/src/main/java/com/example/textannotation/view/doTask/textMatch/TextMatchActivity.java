package com.example.textannotation.view.doTask.textMatch;

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

/*
 * 文本配对做任务新页面
 * update by mwx
 * 2020.3.16
 * */
public class TextMatchActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    private TextMatchAdapter mTextMatchAdapter;

    private ViewPager mViewPager;
    //导航栏
    private MyTabIndicator mTabIndicator;

    //初始化数据
    public List<TextTextMatchFragment> fragment_list = new ArrayList<>();
    private List<String> titles = new ArrayList<>();

    //传送过来的做任务的文件内容
    private Map<String,Integer> filemap = new LinkedHashMap<String,Integer>();

    private List<Integer> fileid = new ArrayList<Integer>();
    private List<String> filename = new ArrayList<String>();
    private String[] mFileNames;
    private Integer[] mFileIds;
    //任务ID
    private int taskid;
    //是做任务页面还是查看做任务页面
    private String typename;
    private int docId;
    //文件状态默认是全部
    private String tasktype;

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
                hideLoading();
            }
        }).show();
    }
    public void uploadInfo(String msg){
        new XPopup.Builder(this).asConfirm("提交情况",msg , new OnConfirmListener() {
            @Override
            public void onConfirm() {
                int fragmentIndex = mViewPager.getCurrentItem();
                TextTextMatchFragment currentFragment  = (TextTextMatchFragment) mTextMatchAdapter.getItem(fragmentIndex);
                showLoading("获取下一个任务");
                currentFragment.getNextTask();
            }
        }).show();

    }
    public void showCompletedInfo(){
        new XPopup.Builder(this).asConfirm("","任务已经全部完成" , new OnConfirmListener() {
            @Override
            public void onConfirm() {
                TextMatchActivity.this.finish();
                hideLoading();
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
        navigation = (BottomNavigationView) findViewById(R.id.tools);
        navigation.setOnNavigationItemSelectedListener(this);
        loadingPopupView = new XPopup.Builder(this).asLoading("loading");
        initTitle();
    }

    private void initTitle(){
        titles.add("匹配上下文本");
        mTabIndicator.setTitles(titles);
        mTabIndicator.setViewPager(mViewPager, 0);
    }

    public void initWorkPlaceFragment(){
        titles.clear();
        fragment_list.clear();
        Log.e("mwx","init fragment");

        TextTextMatchFragment f1 = TextTextMatchFragment.newInstance(0);
        fragment_list.add(f1);
        Bundle bundle = new Bundle();
        //传递lebel数据
        bundle.putInt("taskid", taskid);
        bundle.putInt("docid", docId);
        bundle.putInt("userid", userId);
        bundle.putString("tasktype",tasktype);
        fragment_list.get(0).setArguments(bundle);

        mTextMatchAdapter = new TextMatchAdapter(getSupportFragmentManager(),fragment_list);
        mTextMatchAdapter.notifyDataSetChanged();
        mViewPager.setAdapter(mTextMatchAdapter);
        Log.e("mwx","init fragment end");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){
            case R.id.upload_paragraph: {
                int fragmentIndex = mViewPager.getCurrentItem();
                Log.e("mwx",fragmentIndex+"");
                TextTextMatchFragment currentFragment  = (TextTextMatchFragment) mTextMatchAdapter.getItem(fragmentIndex);
                currentFragment.saveAnnotationInfo();
                return  true;
            }

            case R.id.before:{
                int fragmentIndex = mViewPager.getCurrentItem();
                TextTextMatchFragment currentFragment  = (TextTextMatchFragment) mTextMatchAdapter.getItem(fragmentIndex);
                showLoading("获取上一个任务");
                currentFragment.getLastTask();
                return false;
            }

            case R.id.next: {
                int fragmentIndex = mViewPager.getCurrentItem();
                TextTextMatchFragment currentFragment  = (TextTextMatchFragment) mTextMatchAdapter.getItem(fragmentIndex);
                showLoading("获取下一个任务");
                currentFragment.getNextTask();
                return false;
            }
        }

        return true;
    }
}