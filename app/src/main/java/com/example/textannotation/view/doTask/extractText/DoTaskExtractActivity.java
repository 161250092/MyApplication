package com.example.textannotation.view.doTask.extractText;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.textannotation.myapplication.R;
import com.example.textannotation.network.OkHttpUtil;
import com.example.textannotation.network.Url;
import com.example.textannotation.util.MyApplication;
import com.example.textannotation.util.SerializableMap;
import com.example.textannotation.util.SerializableSortMap;
import com.example.textannotation.view.commonVIew.MyTabIndicator;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.BottomListPopupView;
import com.lxj.xpopup.impl.LoadingPopupView;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
import com.lxj.xpopup.interfaces.OnSelectListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
/**
 *
 * 信息抽取做任务界面
 * Created by kongmin
 * 2018.12.29
 */
public class DoTaskExtractActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    private DoTaskExtractAdapter mDoTaskExtractAdapter;

    private ViewPager mViewPager;
    //导航栏
    private MyTabIndicator mTabIndicator;

    //初始化数据
    private List<DoTaskExtractFragment> fragment_list = new ArrayList<DoTaskExtractFragment>();
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

    private String[] mLabelNames;

    //某一个段落的状态
    private String parastatusstr;
    private List<String> parastatus= new ArrayList<String>();

    private String status[] = {"全部", "进行中"};
    //任务ID
    private int taskid;
    //是做任务页面还是查看做任务页面
    private String typename;
    private int docId;
    //文件状态默认是全部
    private String docStatus = "全部";

    private int userId;

    private Map<Integer,String> downloadlabel = new LinkedHashMap<Integer,String>();

    private MyApplication mApplication;

    //信息抽取标签颜色
    private ArrayList<String> colors = new ArrayList<>();
    private SerializableSortMap colorsMap;
    private Map<Integer,String> colormap = new LinkedHashMap<Integer,String>();

    private HashMap<Integer,ArrayList<String>> colorhashMap = new HashMap<>();
    private HashMap<Integer,ArrayList<Integer>> beginhashMap = new HashMap<>();
    private HashMap<Integer,ArrayList<Integer>> endhashMap = new HashMap<>();
    private HashMap<Integer,ArrayList<Integer>> labelidhashMap = new HashMap<>();

    private LoadingPopupView loadingPopupView;
    public void showLoading(String msg){
        loadingPopupView.setTitle(msg);
        loadingPopupView.show();
    }

    public void hideLoading(){
        loadingPopupView.dismiss();
    }

    //显示匹配确认框
    public void showConfirmView(String selectedMsg,final DoTaskExtractFragment currentFragment){

        new XPopup.Builder(this).asConfirm("选中内容",selectedMsg, new OnConfirmListener() {
            @Override
            public void onConfirm() {
                showLabelSelections(currentFragment);
            }
        }).show();
    }
    //显示标签选项
    public void showLabelSelections(final DoTaskExtractFragment currentFragment){

        new XPopup.Builder(DoTaskExtractActivity.this).asCenterList("请在下列标签选择", mLabelNames,
                new OnSelectListener() {
                    @Override
                    public void onSelect(int position, String text) {
                        String  labelName = mLabelNames[position];
                        int labelId = inststrmap.get(labelName);
                        currentFragment.addTag(labelId,labelName);
                    }
                }).show();
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
                DoTaskExtractFragment currentFragment  = (DoTaskExtractFragment)mDoTaskExtractAdapter.getItem(fragmentIndex);
                currentFragment.submitErrors(text);
            }
        }).show();
    }

    private void getOthersAnnotation(){
        int fragmentIndex = mViewPager.getCurrentItem();
        DoTaskExtractFragment currentFragment  = (DoTaskExtractFragment)mDoTaskExtractAdapter.getItem(fragmentIndex);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dotask_extract);
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
        Bundle tdbundle = intent.getExtras();
        //传送过来的做任务的文件内容
        SerializableMap fileMap = (SerializableMap) tdbundle.get("filemap");
        filemap = fileMap.getMap();
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
            downloadlabel.put(inststrmap.get(labelname),labelname);
            Log.e("ExtractActivity---->", "GET方式请求成功，标签--->" + labelname+inststrmap.get(labelname));
        }
        mLabelNames = inststrmap.keySet().toArray(new String[inststrmap.keySet().size()]);

        if(typename.equals("dotask")) {
            colors = tdbundle.getStringArrayList("colors");
            colorsMap = (SerializableSortMap) tdbundle.get("colormap");
            colormap = colorsMap.getMap();
        }

        getFileContent();
        mTabIndicator.setViewPager(mViewPager, 0);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.tools);
        navigation.setOnNavigationItemSelectedListener(this);

        loadingPopupView = new XPopup.Builder(this).asLoading("loading");
        initBottomListPopupView();

    }

    //新版本初始化
    public void initFragment(){

        titles.clear();
        fragment_list.clear();

        titles.add("当前任务");
        DoTaskExtractFragment f1 = DoTaskExtractFragment.newInstance(0);
        fragment_list.add(f1);
        Bundle bundle = new Bundle();
        //传递数据
        bundle.putInt("taskid", taskid);
        bundle.putInt("docid", docId);
        //是做任务页面还是查看做任务页面
        bundle.putString("type",typename);

        bundle.putSerializable("lebelmap", inststrMap);
        bundle.putSerializable("colormap",colorsMap);
        bundle.putStringArrayList("colors",colors);

        bundle.putInt("userid", userId);
        fragment_list.get(0).setArguments(bundle);
        Log.e("mwx","3");

        mDoTaskExtractAdapter = new DoTaskExtractAdapter(getSupportFragmentManager(),fragment_list);
        mDoTaskExtractAdapter.notifyDataSetChanged();
        mViewPager.setAdapter(mDoTaskExtractAdapter);
        mTabIndicator.setTitles(titles);

        Log.e("mwx","4");
    }

    //老版本初始化
    public void initFragment(SerializableMap inststrMap){
        titles.clear();
        fragment_list.clear();
        for(int i=0;i<fragmentsize;i++){
            //导航栏加标题
            titles.add("任务"+contentindexs.get(i));
            //titles.add("第:"+(i+1)+"段");
            DoTaskExtractFragment f1 = DoTaskExtractFragment.newInstance(i);
            fragment_list.add(f1);
            Bundle bundle = new Bundle();
            //传递数据
            bundle.putInt("fragmentindex",i);
            bundle.putInt("taskid", taskid);
            bundle.putInt("docid", docId);
            //是做任务页面还是查看做任务页面
            bundle.putString("type",typename);
            //将map数据添加到封装的myMap中
            //labelMap.setMap(labelmap);
            //bundle.putSerializable("lebelmap", labelMap);
            bundle.putSerializable("lebelmap", inststrMap);
            bundle.putSerializable("colormap",colorsMap);
            bundle.putStringArrayList("colors",colors);
            String contentidstr = "contentid" + i;
            Log.e("DotaskExtract---->", "activity中的contentidstr--->" + contentidstr + "------" + contentids.get(i));
            bundle.putInt("contentid" + i, contentids.get(i));
            bundle.putInt("contentindex" + i, contentindexs.get(i));
            bundle.putString("content" + i, contents.get(i));
            bundle.putString("parastatus"+ i, parastatus.get(i));

            for(int contentid:colorhashMap.keySet()){
                if(contentid==contentids.get(i)){
                    bundle.putStringArrayList("colorlist",colorhashMap.get(contentid));
                    bundle.putIntegerArrayList("beginlist",beginhashMap.get(contentid));
                    bundle.putIntegerArrayList("endlist",endhashMap.get(contentid));
                    bundle.putIntegerArrayList("labelidlist",labelidhashMap.get(contentid));
                }
            }
            bundle.putInt("userid", userId);
            fragment_list.get(i).setArguments(bundle);
        }

        mDoTaskExtractAdapter = new DoTaskExtractAdapter(getSupportFragmentManager(),fragment_list);
        mDoTaskExtractAdapter.notifyDataSetChanged();
        mViewPager.setAdapter(mDoTaskExtractAdapter);
        mTabIndicator.setTitles(titles);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.tools);
        navigation.setOnNavigationItemSelectedListener(this);
    }

    private void getFileContent(){
        Log.e("mwx",Url.extradotaskUrl+"?docId="+docId+"&status="+docStatus+"&taskId="+taskid+"&userId="+userId);

        OkHttpUtil.sendGetRequest(Url.extradotaskUrl+"?docId="+docId+"&status="+docStatus+"&taskId="+taskid+"&userId="+userId, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DoTaskExtractActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onResponse(final Call call, Response response) throws IOException {
                String temp = response.body().string();
                Log.e("mwx",temp);
                JSONObject jsonObject = (JSONObject) JSON.parse(temp);

                JSONArray jsonArray = (JSONArray)jsonObject.get("data");

                fragmentsize = jsonArray.size();
                //清空content
                contentids.clear();
                contents.clear();
                contentindexs.clear();
                if(jsonArray!=null && jsonArray.size()>0){
                    for(int i=0;i<jsonArray.size();i++){
                        //遍历jsonarray数组，把每一个对象转成json对象
                        JSONObject job = jsonArray.getJSONObject(i);
                        if(job.get("pid")!=null) {
                            contentid = (Integer)job.get("pid");
                            contentids.add(contentid);
                            Log.e("DotaskExtract---->", "activity中的contentID--->" + contentid);
                        }
                        if(job.get("paracontent")!=null) {
                            content = (String)job.get("paracontent").toString();
                            contents.add(content);
                            Log.e("DotaskExtract---->", "activity中的content具体内容--->" + content);
                        }
                        if(job.get("paraindex")!=null) {
                            contentindex = (Integer)job.get("paraindex");
                            contentindexs.add(contentindex);
                            Log.e("DotaskExtract---->", "activity中的content具体内容--->" + contentindex);
                        }
                        if(job.get("dtstatus")!=null) {
                            parastatusstr = (String)job.get("dtstatus");
                            parastatus.add(parastatusstr);
                            Log.e("DotaskExtract---->", "activity中的contentparastatus-->" + parastatusstr);
                        }else{
                            parastatus.add("");
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
                final DoTaskExtractFragment currentFragment  = (DoTaskExtractFragment) mDoTaskExtractAdapter.getItem(fragmentIndex);
                String selectedContent = currentFragment.getSelectedContent();
                showConfirmView(selectedContent,currentFragment);
                return  true;
            }

            case R.id.next: {
                int fragmentIndex = mViewPager.getCurrentItem();
                DoTaskExtractFragment currentFragment  = (DoTaskExtractFragment) mDoTaskExtractAdapter.getItem(fragmentIndex);
                showLoading("获取下一个任务");
                currentFragment.doNextTask();
                return false;
            }

            case R.id.pass_task:{
                int fragmentIndex = mViewPager.getCurrentItem();
                DoTaskExtractFragment currentFragment  = (DoTaskExtractFragment) mDoTaskExtractAdapter.getItem(fragmentIndex);
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
                                        Toast.makeText(DoTaskExtractActivity.this,"切换到" + text,Toast.LENGTH_SHORT).show();
                                        docId = mFileIds[position];
                                        getFileContent();
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
