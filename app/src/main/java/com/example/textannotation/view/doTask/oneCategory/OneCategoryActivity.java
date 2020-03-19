package com.example.textannotation.view.doTask.oneCategory;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import android.widget.Toast;
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
import com.lxj.xpopup.impl.BottomListPopupView;
import com.lxj.xpopup.impl.LoadingPopupView;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
import com.lxj.xpopup.interfaces.OnSelectListener;

import java.io.IOException;
import java.util.*;

/**
 *
 * 单分类做任务界面
 * Created by kongmin
 * 2018.12.29
 */

public class OneCategoryActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private OneCategoryAdapter mOneCategoryAdapter;

    private ViewPager mViewPager;

    //导航栏
    private MyTabIndicator mTabIndicator;

    //初始化数据
    private List<OneCategoryFragment> fragment_list = new ArrayList<OneCategoryFragment>();
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

    //某一个段落的状态
    private String parastatusstr;
    private List<String> parastatus= new ArrayList<String>();

    private String status[] = {"全部", "进行中"};
    //默认文件状态
    private String docStatus = status[1];
    //任务ID
    private int taskid;
    //是做任务页面还是查看做任务页面
    private String typename;
    private int docId;


    private int userId;

    //已经标注了的标签
    private SerializableList label_idList = new SerializableList();
    private ArrayList<Integer> label_ids = new ArrayList<Integer>();

    private Map<Integer,Integer> seledlabelid = new HashMap<>();

    private MyApplication mApplication;

    /**
     * 已完成标注的部分 ，key是contentid, value是对应的label id
     */
    private Map<Integer,Set<Integer>> completedLabels = new HashMap<>();

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
        //和选择弹出框相关的
        ButterKnife.inject(this);
        // 获取整个应用的Application对象
        // 在不同的Activity中获取的对象是同一个
        mApplication = (MyApplication)getApplication();
        userId = mApplication.getLoginUserId();
        Log.e("params---->", "Post方式请求成功，OneCategoryActivity的userID--->" + userId);

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
        //传送过来的做任务的标签内容
        inststrMap = (SerializableMap) tdbundle.get("instlabel");
        inststrmap = inststrMap.getMap();
        for(String labelname : inststrmap.keySet()){
            Log.e("ExtractActivity---->", "GET方式请求成功，标签--->" + labelname+inststrmap.get(labelname));
        }

        getFileContent();
        mTabIndicator.setViewPager(mViewPager, 0);

        loadingPopupView = new XPopup.Builder(this).asLoading("loading");
        initBottomListPopupView();
    }

    //新版本， 只加载一个FRAGMENT
    public void initFragment(){
        titles.clear();
        fragment_list.clear();
        titles.add("常规任务");
        OneCategoryFragment f1 = OneCategoryFragment.newInstance(0);
        fragment_list.add(f1);
        Bundle bundle = new Bundle();
        //传递lebel数据
        bundle.putInt("taskid", taskid);
        bundle.putInt("docid", docId);
        bundle.putInt("userid",userId);
        bundle.putSerializable("lebelmap",inststrMap);
        fragment_list.get(0).setArguments(bundle);

        mOneCategoryAdapter = new OneCategoryAdapter(getSupportFragmentManager(),fragment_list);
        mOneCategoryAdapter.notifyDataSetChanged();
        mViewPager.setAdapter(mOneCategoryAdapter);
        mTabIndicator.setTitles(titles);
    }

    public void getFileContent(){
        initFragment();
    }


    private void submitError( ){
        new XPopup.Builder(this).asInputConfirm("提交错误信息","具体信息", new OnInputConfirmListener() {
            @Override
            public void onConfirm(String text) {
                int fragmentIndex = mViewPager.getCurrentItem();
                OneCategoryFragment currentFragment  = (OneCategoryFragment)mOneCategoryAdapter.getItem(fragmentIndex);
                currentFragment.submitErrors(text);
            }
        }).show();
    }

    private void getOthersAnnotation(){
        int fragmentIndex = mViewPager.getCurrentItem();
        OneCategoryFragment currentFragment  = (OneCategoryFragment)mOneCategoryAdapter.getItem(fragmentIndex);
        currentFragment.compareOthersTextAnnotation();
    }

    public void showOthersAnnotation(String msg){
        new XPopup.Builder(this).asConfirm("同任务比较", msg, new OnConfirmListener() {
        @Override
        public void onConfirm() {
        }
        }).show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
        switch (menuItem.getItemId()) {
            case R.id.upload_paragraph: {
                int fragmentIndex = mViewPager.getCurrentItem();
                Log.e("mwx", fragmentIndex + "");
                OneCategoryFragment currentFragment = (OneCategoryFragment) mOneCategoryAdapter.getItem(fragmentIndex);
                currentFragment.saveAnnotationInfo();
                return true;
            }

            case R.id.next: {
                int fragmentIndex = mViewPager.getCurrentItem();
                OneCategoryFragment currentFragment  = (OneCategoryFragment)mOneCategoryAdapter.getItem(fragmentIndex);
                showLoading("获取下一个任务");
                currentFragment.doNextTask();
                return false;
            }

            case R.id.pass_task: {
                int fragmentIndex = mViewPager.getCurrentItem();
                OneCategoryFragment currentFragment  = (OneCategoryFragment)mOneCategoryAdapter.getItem(fragmentIndex);
                showLoading("跳过当前任务");
                currentFragment.passCurrentTask();
                return false;
            }

            case R.id.file_list:{
                new XPopup.Builder(this)
                        .asBottomList("选择文件", mFileNames,
                                new OnSelectListener() {
                                    @Override
                                    public void onSelect(int position, String text) {
                                        Toast.makeText(OneCategoryActivity.this,"切换到" + text,Toast.LENGTH_SHORT).show();
                                        docId = mFileIds[position];
                                        getFileContent();
                                    }
                                })
                        .show();

                return false;
            }


            case R.id.settings :{
                settingView.show();
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

    public void uploadInfo(String msg){
        new XPopup.Builder(this).asConfirm("提交情况",msg , new OnConfirmListener() {
            @Override
            public void onConfirm() {
                int fragmentIndex = mViewPager.getCurrentItem();
                OneCategoryFragment currentFragment  = (OneCategoryFragment)mOneCategoryAdapter.getItem(fragmentIndex);
                showLoading("获取下一个任务");
                currentFragment.doNextTask();
            }
        }).show();

    }



}
