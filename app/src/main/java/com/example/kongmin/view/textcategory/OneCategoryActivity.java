package com.example.kongmin.view.textcategory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.kongmin.Constant.Constant;
import com.example.kongmin.myapplication.R;
import com.example.kongmin.view.textcategory.util.ListDropDownAdapter;
import com.example.kongmin.util.*;
import android.widget.LinearLayout;

import java.util.*;

/**
 *
 * 单分类做任务界面
 * Created by kongmin
 * 2018.12.29
 */

public class OneCategoryActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private OneCategoryAdapter mOneCategoryAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    //导航栏
    private MyTabIndicator mTabIndicator;

    //初始化数据
    private List<OneCategoryFragment> list = new ArrayList<OneCategoryFragment>();
    private List<String> titles = new ArrayList<>();

    private LinearLayout extractlinear;





    //标签ID
    //private int labelid;
    //标签名称
    //private String labelname;
    private final SerializableMap labelMap = new SerializableMap();
    private Map<String,Integer> labelmap = new LinkedHashMap<String,Integer>();
    //文件内容
    //private String content;
    //内容ID
    //private int contentid;
    //段落在文本中的索引
    //private int contentindex;

    //private List<String> contents = new ArrayList<String>();
    //private List<Integer> contentids = new ArrayList<Integer>();
    //private List<Integer> contentindexs = new ArrayList<Integer>();

    //private int fragmentsize;





    //标签ID
    private int labelid;
    //标签名称
    private String labelname;
    //private final SerializableMap labelMap = new SerializableMap();
    //private Map<String,Integer> labelmap = new LinkedHashMap<String,Integer>();
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
    //选中的fileID
    private int btnfileid = -1;
    //选中的文件状态
    private String filestatus="";
    //某一个段落的状态
    private String parastatusstr;
    private List<String> parastatus= new ArrayList<String>();


    //和选择弹出框相关的
    @InjectView(R.id.dropDownMenu) DropDownMenu mDropDownMenu;
    private String headers[] = {"文件列表", "完成状态", "确定"};
    private List<View> popupViews = new ArrayList<>();


    private ListDropDownAdapter fileAdapter;
    private ListDropDownAdapter statusAdapter;
    private ListDropDownAdapter btnAdapter;

    private String status[] = {"全部", "进行中"};
    private String confirm[] = {"确定"};

    private int constellationPosition = 0;

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
    //private ArrayList<Integer> seledparalabelid = new ArrayList<Integer>();

    private MyApplication mApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_category_tab);
        // 获取整个应用的Application对象
        // 在不同的Activity中获取的对象是同一个
        mApplication = (MyApplication)getApplication();

        mTabIndicator = (MyTabIndicator) findViewById(R.id.mTabIndicator);
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);
        //和选择弹出框相关的
        ButterKnife.inject(this);
        initView();

        // 获取整个应用的Application对象
        // 在不同的Activity中获取的对象是同一个
        mApplication = (MyApplication)getApplication();
        userId = mApplication.getLoginUserId();
        Log.e("params---->", "Post方式请求成功，OneCategoryActivity的userID--->" + userId);

        /*for(int i=0;i<7;i++){
            titles.add("标题:" + i+1);
            OneCategoryFragment f1 = OneCategoryFragment.newInstance(i+3);
            list.add(f1);

        }*/

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
        docId = fileid.get(0);
        //传送过来的做任务的标签内容
        inststrMap = (SerializableMap) tdbundle.get("instlabel");
        inststrmap = inststrMap.getMap();
        for(String labelname : inststrmap.keySet()){
            Log.e("ExtractActivity---->", "GET方式请求成功，标签--->" + labelname+inststrmap.get(labelname));
        }

        LinearLayout extractlinear = findViewById(R.id.extractlinear);
        if(typename.equals("dotask")){
            extractlinear.setVisibility(View.GONE);
        }else{
            extractlinear.setVisibility(View.VISIBLE);
        }


        new Thread(runnable).start();
        try {
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        initFragment(inststrMap);


        /*for(int i=0;i<fragmentsize;i++){
            //导航栏加标题
            titles.add("第:"+contentindexs.get(i)+"段");
            //titles.add("第:"+(i+1)+"段");
            OneCategoryFragment f1 = OneCategoryFragment.newInstance(i);
            list.add(f1);
            Bundle bundle = new Bundle();
            //传递lebel数据
            bundle.putInt("fragmentindex",i);
            bundle.putInt("taskid", taskid);
            //将map数据添加到封装的myMap中
            labelMap.setMap(labelmap);
            bundle.putSerializable("lebelmap", labelMap);
            bundle.putInt("contentid" + i, contentids.get(i));
            bundle.putInt("contentindex" + i, contentindexs.get(i));
            bundle.putString("content" + i, contents.get(i));
            //todo 获取用户ID
            bundle.putInt("userid", 1);
            list.get(i).setArguments(bundle);
        }*/
        /*PlaceholderFragment f1 = PlaceholderFragment.newInstance(3);
        list.add(f1);
        PlaceholderFragment f2 = PlaceholderFragment.newInstance(5);
        list.add(f2);
        PlaceholderFragment f3 = PlaceholderFragment.newInstance(7);
        list.add(f3);*/


        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        //mOneCategoryAdapter = new OneCategoryAdapter(getSupportFragmentManager(),list);


        /*for(int i = 0; i < 3; i ++){
            titles.add("标题:" + i+1);
        }*/

        // Set up the ViewPager with the sections adapter.
        //mViewPager = (ViewPager) findViewById(R.id.container);
        //mViewPager.setAdapter(mOneCategoryAdapter);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/



        //mTabIndicator.setTitles(titles);
        mTabIndicator.setViewPager(mViewPager, 0);
        //mTabIndicator.setTitles(titles, 3);//可以设置默认选中的title

        mTabIndicator.setOnPageChangeListener(new MyTabIndicator.PageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Toast.makeText(OneCategoryActivity.this, "选择了：" + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public void initFragment(SerializableMap inststrMap){
        titles.clear();
        list.clear();
        for(int i=0;i<fragmentsize;i++){
            //导航栏加标题
            titles.add("第:"+contentindexs.get(i)+"段");
            //titles.add("第:"+(i+1)+"段");
            OneCategoryFragment f1 = OneCategoryFragment.newInstance(i);
            list.add(f1);
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
                //todo
                label_idList.setList(label_ids);
                bundle.putSerializable("label_ids",label_idList);
            }
            //todo 获取用户ID
            bundle.putInt("userid", 1);
            list.get(i).setArguments(bundle);
        }
        mOneCategoryAdapter = new OneCategoryAdapter(getSupportFragmentManager(),list);
        mOneCategoryAdapter.notifyDataSetChanged();
        mViewPager.setAdapter(mOneCategoryAdapter);
        mTabIndicator.setTitles(titles);
    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // TODO: http request.
            /*// 在这里进行 http request.网络请求相关操作
            String requestUrl = "http://172.20.10.5:8080/label/getLabelByTask";
            String paramUrl = "?taskid=1";
            String label = HttpUtil.requestGet(requestUrl,paramUrl);
            //Log.e("ExtractActivity---->", "GET方式请求成功，result--->" + label);
            String requestUrl2 = "http://172.20.10.5:8080/content/getContent";
            String paramUrl2 = "?docId=1";
            String docontent = HttpUtil.requestGet(requestUrl2,paramUrl2);
            //Log.e("ExtractActivity---->", "GET方式请求成功，result2--->" + docontent);

            //对标签进行解析，首先把字符串转成JSONArray对象
            JSONObject jsonObject = JSONObject.parseObject(label);
            JSONArray jsonArray = (JSONArray)jsonObject.get("label");
            if(jsonArray.size()>0){
                for(int i=0;i<jsonArray.size();i++){
                    //遍历jsonarray数组，把每一个对象转成json对象
                    JSONObject job = jsonArray.getJSONObject(i);
                    //得到每个对象中的属性值
                    if(job.get("lid")!=null) {
                        labelid = (Integer)job.get("lid");
                        Log.e("DotaskOneCategory---->", "activity中的labelID--->" + labelid);
                    }
                    if(job.get("labelname")!=null) {
                        labelname = (String)job.get("labelname").toString();
                        Log.e("DotaskOneCategory---->", "activity中的labelname--->" + labelname);
                        labelmap.put(labelname,labelid);
                    }

                }
            }
            //对文件内容进行解析，首先把字符串转成JSONArray对象
            JSONObject jsonObject2 = JSONObject.parseObject(docontent);
            JSONArray jsonArray2 = (JSONArray)jsonObject2.get("data");
            fragmentsize = jsonArray2.size();
            if(jsonArray2.size()>0){
                for(int i=0;i<jsonArray2.size();i++){
                    //遍历jsonarray数组，把每一个对象转成json对象
                    JSONObject job = jsonArray2.getJSONObject(i);
                    //得到每个对象中的属性值
                    if(job.get("cid")!=null) {
                        contentid = (Integer)job.get("cid");
                        contentids.add(contentid);
                        Log.e("DotaskOneCategory---->", "activity中的contentID--->" + contentid);
                    }
                    if(job.get("paracontent")!=null) {
                        content = (String)job.get("paracontent").toString();
                        contents.add(content);
                        Log.e("DotaskOneCategory---->", "activity中的content具体内容--->" + content);
                    }
                    if(job.get("paraindex")!=null) {
                        contentindex = (Integer)job.get("paraindex");
                        contentindexs.add(contentindex);
                        Log.e("DotaskOneCategory---->", "activity中的content具体内容--->" + contentindex);
                    }
                }
            }*/

            String requestUrl = Constant.classifyfileUrl;
            //String paramUrl = "?docId="+docId+"&status="+docStatus+"&taskId="+taskid;
            String paramUrl;
            if(typename.equals("dotask")){
                paramUrl = "?docId="+docId+"&status="+docStatus+"&taskId="+taskid+"&userId="+userId;
            }else{
                paramUrl = "?docId="+docId+"&status="+docStatus+"&taskId="+taskid+"&userId="+userId;
                Log.e("ExtractActivity---->", "GET方式请求成功，result2---> 查看我做的任务");
            }
            String docontent = HttpUtil.requestGet(requestUrl,paramUrl);

            //对文件内容进行解析，首先把字符串转成JSONArray对象
            JSONObject jsonObject = JSONObject.parseObject(docontent);
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
                    //得到每个对象中的属性值
                    /*if(job.get("cid")!=null) {
                        contentid = (Integer)job.get("cid");
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
                    }*/
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
                    //index_begins.clear();
                    //index_ends.clear();
                    //label_ids.clear();
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
        }
    };


    //和选择弹出框相关的
    private void initView() {

        //init file menu
        final ListView fileView = new ListView(this);
        fileAdapter = new ListDropDownAdapter(this, filename);
        fileView.setDividerHeight(0);
        fileView.setAdapter(fileAdapter);

        //init status menu
        final ListView statusView = new ListView(this);
        statusView.setDividerHeight(0);
        statusAdapter = new ListDropDownAdapter(this, Arrays.asList(status));
        statusView.setAdapter(statusAdapter);

        //init 确定 menu
        final ListView btnView = new ListView(this);
        btnView.setDividerHeight(0);
        btnAdapter = new ListDropDownAdapter(this, Arrays.asList(confirm));
        btnView.setAdapter(btnAdapter);

        //init popupViews
        popupViews.add(fileView);
        popupViews.add(statusView);
        popupViews.add(btnView);

        //add item click event
        fileView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fileAdapter.setCheckItem(position);
                //mDropDownMenu.setTabText(position == 0 ? headers[0] : filename.get(position));
                mDropDownMenu.setTabText(filename.get(position));
                //if(position!=0) {
                btnfileid = fileid.get(position);
                //}
                mDropDownMenu.closeMenu();
            }
        });

        statusView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                statusAdapter.setCheckItem(position);
                //mDropDownMenu.setTabText(position == 0 ? headers[1] : status[position]);
                mDropDownMenu.setTabText(status[position]);
                //if(position!=0) {
                filestatus = status[position];
                //}
                mDropDownMenu.closeMenu();
            }
        });

        btnView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                label_ids.clear();
                if(btnfileid!=-1 && filestatus.equals("")){
                    //选了文件没选状态
                    if(btnfileid!=fileid.get(0)) {
                        docId = btnfileid;
                        //请求数据
                        new Thread(runnable).start();
                        try {
                            Thread.sleep(1000);
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                        initFragment(inststrMap);
                    }
                }else if(btnfileid==-1 && !filestatus.equals("")){
                    //选了状态没选文件
                    if(!filestatus.equals("全部")){
                        docStatus = filestatus;
                        //请求数据
                        new Thread(runnable).start();
                        try {
                            Thread.sleep(1000);
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                        initFragment(inststrMap);
                    }
                }else if(btnfileid!=-1 && !filestatus.equals("")){
                    Toast.makeText(OneCategoryActivity.this, "选择了文件：" + btnfileid+filestatus, Toast.LENGTH_SHORT).show();
                    //如果文件和文件状态都没有选择默认值
                    //if(!filestatus.equals("全部")){
                    docId = btnfileid;
                    docStatus = filestatus;
                    //请求数据
                    new Thread(runnable).start();
                    try {
                        Thread.sleep(1000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    initFragment(inststrMap);
                    //}
                }
                mDropDownMenu.closeMenu();
            }
        });

        //init context view
        TextView contentView = new TextView(this);
        contentView.setHeight(0);
        /*contentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        contentView.setText("内容显示区域");
        contentView.setGravity(Gravity.CENTER);
        contentView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);*/
        //TextView contentView = findViewById(R.id.textview);

        //init dropdownview
        mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, contentView);
    }

    @Override
    public void onBackPressed() {
        //退出activity前关闭菜单
        if (mDropDownMenu.isShowing()) {
            mDropDownMenu.closeMenu();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_text_category_tab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
