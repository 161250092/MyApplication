package com.example.kongmin.view.doTask.oneCategory;

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
import com.example.kongmin.Constant.Constant;
import com.example.kongmin.myapplication.R;
import com.example.kongmin.network.OkHttpUtil;
import com.example.kongmin.view.textcategory.MyTabIndicator;
import com.example.kongmin.util.*;
import com.lxj.xpopup.XPopup;
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


    private final SerializableMap labelMap = new SerializableMap();
    private Map<String,Integer> labelmap = new LinkedHashMap<String,Integer>();

    //标签ID
    private int labelid;
    //标签名称
    private String labelname;

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

    //任务ID
    private int taskid;
    //是做任务页面还是查看做任务页面
    private String typename;
    private int docId;
    //文件状态默认是全部
    private String docStatus = "全部";
    //todo 设置userId
    private int userId;

    //已经标注了的标签
    private SerializableList label_idList = new SerializableList();
    private ArrayList<Integer> label_ids = new ArrayList<Integer>();

    private Map<Integer,Integer> seledlabelid = new HashMap<>();

    private MyApplication mApplication;

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
        mTabIndicator.setOnPageChangeListener(new MyTabIndicator.PageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
               // Toast.makeText(OneCategoryActivity.this, "选择了：" + position, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public void initFragment(SerializableMap inststrMap){
        titles.clear();
        fragment_list.clear();
        for(int i=0;i<fragmentsize;i++){
            //导航栏加标题
            titles.add("第"+contentindexs.get(i)+"段");
            OneCategoryFragment f1 = OneCategoryFragment.newInstance(i);
            fragment_list.add(f1);
            Bundle bundle = new Bundle();
            //传递lebel数据
            bundle.putInt("fragmentindex",i);
            bundle.putInt("taskid", taskid);
            bundle.putInt("docid", docId);
            //是做任务页面还是查看做任务页面
            bundle.putString("type",typename);
            //将map数据添加到封装的myMap中
            //labelMap.setMap(labelmap);
            //bundle.putSerializable("lebelmap", labelMap);
            bundle.putSerializable("lebelmap", inststrMap);
            String contentidstr = "contentid" + i;
            Log.e("DotaskExtract---->", "activity中的contentidstr--->" + contentidstr + "------" + contentids.get(i));
            bundle.putInt("contentid" + i, contentids.get(i));
            bundle.putInt("contentindex" + i, contentindexs.get(i));
            bundle.putString("content" + i, contents.get(i));

            ArrayList<Integer> seledparalabelid = new ArrayList<Integer>();
            //如果该段已经排好序
            if(seledlabelid.containsKey(contentids.get(i))){
                bundle.putString("issorted"+i,"true");
                seledparalabelid.add(seledlabelid.get(contentids.get(i)));
                Log.e("DotaskExtract---->", "进来排好序");
            }else{
                bundle.putString("issorted"+i,"false");
                Log.e("DotaskExtract---->", "进来未排序");
            }
            bundle.putIntegerArrayList("seledinstid"+i,seledparalabelid);

            bundle.putString("parastatus"+ i, parastatus.get(i));
            if(parastatus.get(i).equals("已完成")){
                label_idList.setList(label_ids);
                bundle.putSerializable("label_ids",label_idList);
            }
            //todo 获取用户ID
            bundle.putInt("userid", userId);
            fragment_list.get(i).setArguments(bundle);
        }
        mOneCategoryAdapter = new OneCategoryAdapter(getSupportFragmentManager(),fragment_list);
        mOneCategoryAdapter.notifyDataSetChanged();
        mViewPager.setAdapter(mOneCategoryAdapter);
        mTabIndicator.setTitles(titles);
    }

    public void getFileContent(){
        String requestUrl = Constant.classifyfileUrl;
        String paramUrl;
        if(typename.equals("dotask")){
            paramUrl = "?docId="+docId+"&status="+docStatus+"&taskId="+taskid+"&userId="+userId;
        }else{
            paramUrl = "?docId="+docId+"&status="+docStatus+"&taskId="+taskid+"&userId="+userId;
            Log.e("ExtractActivity---->", "GET方式请求成功，result2---> 查看我做的任务");
        }

        OkHttpUtil.sendGetRequest(requestUrl+paramUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(OneCategoryActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String docontent = response.body().string();
                JSONObject jsonObject = JSONObject.parseObject(docontent);
                JSONArray jsonArray = (JSONArray)jsonObject.get("data");
                fragmentsize = jsonArray.size();
                //清空content
                contentids.clear();
                contents.clear();
                contentindexs.clear();

                if( jsonArray.size()>0 ){
                    for(int i=0;i<jsonArray.size();i++){
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
                            //不是已完成任务的状态就是空
                            parastatus.add("");
                        }

                        //已经做了的部分
                        JSONArray alreadyDone = (JSONArray)job.get("alreadyDone");

                        if(alreadyDone!=null && alreadyDone.size()>0) {
                            for (int j = 0; j < alreadyDone.size(); j++) {
                                //遍历jsonarray数组，把每一个对象转成json对象
                                JSONObject done = alreadyDone.getJSONObject(j);
                                //得到每个对象中的属性值
                                if (done.get("label_id") != null) {
                                    int label_id = (Integer) done.get("label_id");
                                    seledlabelid.put(contentid,label_id);
                                    Log.e("DotaskExtract---->", "activity中的contentid,label_id--->" + contentid+"--"+label_id + "------");
                                }
                            }
                        }
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initFragment(inststrMap);
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
                OneCategoryFragment currentFragment  = (OneCategoryFragment)mOneCategoryAdapter.getItem(fragmentIndex);
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
                                        Toast.makeText(OneCategoryActivity.this,"切换到" + text,Toast.LENGTH_SHORT).show();
                                        docId = mFileIds[position];
                                        getFileContent();
                                    }
                                })
                        .show();

                return false;
            }

            case R.id.settings:{

                new XPopup.Builder(this)
                        .asBottomList("设置",new String[]{"显示全部","只显示未完成","结束该段","结束该文档"} ,
                                new OnSelectListener() {
                                    @Override
                                    public void onSelect(int position, String text) {
                                        if (position == 0) {
                                            docStatus = status[0];
                                            Toast.makeText(OneCategoryActivity.this, "显示全部段落",Toast.LENGTH_SHORT).show();
                                            getFileContent();
                                        }

                                        else if (position == 1){
                                            docStatus = status[1];
                                            Toast.makeText(OneCategoryActivity.this, "只显示未完成段落"+text,Toast.LENGTH_SHORT).show();
                                            getFileContent();
                                        }

                                        else if (position == 2) {
                                            int fragmentIndex = mViewPager.getCurrentItem();
                                            OneCategoryFragment currentFragment  = (OneCategoryFragment)mOneCategoryAdapter.getItem(fragmentIndex);
                                            currentFragment.completeCon();

                                        } else{
                                            int fragmentIndex = mViewPager.getCurrentItem();
                                            OneCategoryFragment currentFragment  = (OneCategoryFragment)mOneCategoryAdapter.getItem(fragmentIndex);
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
