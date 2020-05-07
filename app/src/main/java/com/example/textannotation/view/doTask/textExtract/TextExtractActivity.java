package com.example.textannotation.view.doTask.textExtract;

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
import com.example.textannotation.util.SerializableSortMap;
import com.example.textannotation.view.common.MyTabIndicator;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.LoadingPopupView;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.OnSelectListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
/**
 *
 * 信息抽取做任务界面
 * Created by kongmin
 * 2018.12.29
 */
public class TextExtractActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private TextExtractAdapter mTextExtractAdapter;
    private ViewPager mViewPager;
    //导航栏
    private MyTabIndicator mTabIndicator;

    //初始化数据
    private List<TextExtractFragment> fragment_list = new ArrayList<TextExtractFragment>();
    private List<String> titles = new ArrayList<>();


    //传送过来的做任务的标签内容
    private  SerializableMap inststrMap;
    private Map<String,Integer> inststrmap = new LinkedHashMap<String,Integer>();

    private String[] mLabelNames;
    //任务ID
    private int taskid;
    //是做任务页面还是查看做任务页面
    private String typename;
    private int userId;

    private Map<Integer,String> downloadlabel = new LinkedHashMap<Integer,String>();
    private MyApplication mApplication;

    //信息抽取标签颜色
    private ArrayList<String> colors = new ArrayList<>();
    private SerializableSortMap colorsMap;
    private Map<Integer,String> colormap = new LinkedHashMap<Integer,String>();

    private ArrayList<String> relationList;

    private LoadingPopupView loadingPopupView;
    public void showLoading(String msg){
        loadingPopupView.setTitle(msg);
        loadingPopupView.show();
    }

    public void hideLoading(){
        loadingPopupView.dismiss();
    }

    //显示匹配确认框
    public void showConfirmView(final String selectedMsg, final TextExtractFragment currentFragment){

        new XPopup.Builder(this).asConfirm("选中内容",selectedMsg, new OnConfirmListener() {
            @Override
            public void onConfirm() {
                showLabelSelections(selectedMsg,currentFragment);
            }
        }).show();
    }
    //显示标签选项
    public void showLabelSelections(final String content,final TextExtractFragment currentFragment){

        new XPopup.Builder(TextExtractActivity.this).asCenterList("请在下列标签选择", mLabelNames,
                new OnSelectListener() {
                    @Override
                    public void onSelect(int position, String text) {
                        String  labelName = mLabelNames[position];
                        int labelId = inststrmap.get(labelName);
                        currentFragment.addTag(content,labelId,labelName);
                    }
                }).show();

    }

    public void showSubmitInfo(final String msg){
        new XPopup.Builder(this).asConfirm("提交结果",msg, new OnConfirmListener() {
            @Override
            public void onConfirm() {
                int fragmentIndex = mViewPager.getCurrentItem();
                TextExtractFragment currentFragment  = (TextExtractFragment) mTextExtractAdapter.getItem(fragmentIndex);
                showLoading("获取下一个任务");
                currentFragment.getNextTask();
            }
        }).show();
    }

    public void showTaskCompletedInfo(){
        new XPopup.Builder(this).asConfirm("任务已经完成","感谢您的参与", new OnConfirmListener() {
            @Override
            public void onConfirm() {
                TextExtractActivity.this.finish();
            }
        }).show();
    }

    public void showNotice(final String msg){
        new XPopup.Builder(this).asConfirm("",msg, new OnConfirmListener() {
            @Override
            public void onConfirm() {
                hideLoading();
            }
        }).show();
    }

    //关系标签列表弹窗
    public void showRelationList(){
        String[] relationTabs;
        if (relationList!= null && relationList.size()>0){
            relationTabs = relationList.toArray(new String[relationList.size()]);
        }else {
            return;
        }

        new XPopup.Builder(this).asCenterList("关系标签",relationTabs,new  OnSelectListener()  {

            @Override
            public void onSelect(int position, String text) {
                int fragmentIndex = mViewPager.getCurrentItem();
                final TextExtractFragment currentFragment  = (TextExtractFragment) mTextExtractAdapter.getItem(fragmentIndex);
                currentFragment.addRelation(text);
            }
        }).show();

    }

    //现存的实体关系列表
    public void showEntityRelationList(String[] entity_relation_list){
        new XPopup.Builder(this).asCenterList("实体关系列表",entity_relation_list,new  OnSelectListener(){
            @Override
            public void onSelect(int position, String text) {
                int fragmentIndex = mViewPager.getCurrentItem();
                final TextExtractFragment currentFragment  = (TextExtractFragment) mTextExtractAdapter.getItem(fragmentIndex);
                currentFragment.deleteEntityRelation(position);
            }
        }).show();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dotask_extract);
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
        typename = intent.getStringExtra("type");
        Bundle tdbundle = intent.getExtras();;
        //传送过来的做任务的标签内容
        relationList = tdbundle.getStringArrayList("relationList");
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

        initFragment();
        mTabIndicator.setViewPager(mViewPager, 0);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.tools);
        navigation.setOnNavigationItemSelectedListener(this);
        loadingPopupView = new XPopup.Builder(this).asLoading("loading");
    }

    //fragment初始化
    public void initFragment(){
        titles.clear();
        fragment_list.clear();

        titles.add("长按文字区域选取文本");
        TextExtractFragment f1 = TextExtractFragment.newInstance(0);
        fragment_list.add(f1);
        Bundle bundle = new Bundle();
        //传递数据
        bundle.putInt("taskid", taskid);
        //是做任务页面还是查看做任务页面
        bundle.putString("type",typename);

        bundle.putSerializable("lebelmap", inststrMap);
        bundle.putSerializable("colormap",colorsMap);
        bundle.putStringArrayList("colors",colors);
        bundle.putStringArrayList("relationList",relationList);

        bundle.putInt("userid", userId);
        fragment_list.get(0).setArguments(bundle);
        Log.e("mwx","3");

        mTextExtractAdapter = new TextExtractAdapter(getSupportFragmentManager(),fragment_list);
        mTextExtractAdapter.notifyDataSetChanged();
        mViewPager.setAdapter(mTextExtractAdapter);
        mTabIndicator.setTitles(titles);

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){

            case R.id.pair:{
                int fragmentIndex = mViewPager.getCurrentItem();
                final TextExtractFragment currentFragment  = (TextExtractFragment) mTextExtractAdapter.getItem(fragmentIndex);
                String selectedContent = currentFragment.extractText();

                if (selectedContent != null && !selectedContent.equals(""))
                    showConfirmView(selectedContent,currentFragment);

                return  true;
            }

            case R.id.clear:{
                int fragmentIndex = mViewPager.getCurrentItem();
                final TextExtractFragment currentFragment  = (TextExtractFragment) mTextExtractAdapter.getItem(fragmentIndex);
                currentFragment.cancelRelationAnnotation();
                return true;
            }

            case R.id.upload: {
                int fragmentIndex = mViewPager.getCurrentItem();
                final TextExtractFragment currentFragment  = (TextExtractFragment) mTextExtractAdapter.getItem(fragmentIndex);
                currentFragment.saveAnnotationInfo();
                return  true;
            }

            case R.id.before: {
                int fragmentIndex = mViewPager.getCurrentItem();
                TextExtractFragment currentFragment  = (TextExtractFragment) mTextExtractAdapter.getItem(fragmentIndex);
                showLoading("获取上一个任务");
                currentFragment.getLastTask();
                return false;
            }

            case R.id.next: {
                int fragmentIndex = mViewPager.getCurrentItem();
                TextExtractFragment currentFragment  = (TextExtractFragment) mTextExtractAdapter.getItem(fragmentIndex);
                showLoading("获取下一个任务");
                currentFragment.getNextTask();
                return false;
            }

        }

        return true;
    }

}
