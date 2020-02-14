package com.example.kongmin.view;

import android.content.Intent;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.*;

import com.example.kongmin.Constant.Constant;
import com.example.kongmin.myapplication.R;
import com.example.kongmin.network.OkHttpUtil;
import com.example.kongmin.view.textcategory.DropDownMenu;
import com.example.kongmin.view.textcategory.FragmentMessgeI;
import com.example.kongmin.view.textcategory.MatchCategoryAdapter;
import com.example.kongmin.view.textcategory.MatchCategoryFragment;
import com.example.kongmin.view.textcategory.MyTabIndicator;
import com.example.kongmin.view.textcategory.util.ListDropDownAdapter;
import com.example.kongmin.util.HttpUtil;
import com.example.kongmin.util.MyApplication;
import com.example.kongmin.util.SerializableMap;

/*
 * 文本配对做任务新页面
 * update by mwx
 * 2020.2.10
 * */
public class DoTask2Activity extends AppCompatActivity implements FragmentMessgeI {


    private MatchCategoryAdapter mMatchCategoryAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    //导航栏
    private MyTabIndicator mTabIndicator;

    //初始化数据
    public List<MatchCategoryFragment> list = new ArrayList<>();
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
    //private int taskid;
    private int fragmentsize;


    //传送过来的做任务的文件内容
    private Map<String,Integer> filemap = new LinkedHashMap<String,Integer>();

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
    @InjectView(R.id.dropDownMenu)
    DropDownMenu mDropDownMenu;
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

    private String tasktype;
    private int selectedaboveitemid;

    private int doneaitemid;
    private int donebitemid;

    private List<Integer> doneinstids = new ArrayList<>();
    private List<Integer> doneaitemids = new ArrayList<>();
    private List<Integer> donebitemids = new ArrayList<>();

    private ArrayList<Integer> chuansdoneaitemids = new ArrayList<>();
    private ArrayList<Integer> chuansdonebitemids = new ArrayList<>();

    private MyApplication mApplication;
    private int userId;

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
        initView();

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

        LinearLayout extractlinear = findViewById(R.id.extractlinear);
        if(typename.equals("dotask")){
            extractlinear.setVisibility(View.GONE);
        }else{
            extractlinear.setVisibility(View.VISIBLE);
        }
        getFileContent();

        mTabIndicator.setViewPager(mViewPager, 0);
        mTabIndicator.setOnPageChangeListener(new MyTabIndicator.PageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Toast.makeText(DoTask2Activity.this, "选择了：" + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    public void initFragment(){
        titles.clear();
        list.clear();
        for(int i=0;i<fragmentsize;i++){
            //导航栏加标题
            titles.add("第"+instanceindexs.get(i)+"段");
            MatchCategoryFragment f1 = MatchCategoryFragment.newInstance(i);
            list.add(f1);
            Bundle bundle = new Bundle();
            //传递lebel数据
            bundle.putInt("fragmentindex",i);
            bundle.putInt("taskid", taskid);
            bundle.putInt("docid", docId);
            //是做任务页面还是查看做任务页面
            bundle.putString("type",typename);
            bundle.putInt("instanceid" + i,instanceids.get(i));
            bundle.putInt("instanceindex" + i,instanceindexs.get(i));
            bundle.putString("tasktype",tasktype);
            ArrayList<Integer> clistitemids1 = new ArrayList<>();
            ArrayList<String> clitemcontents1 = new ArrayList<>();
            ArrayList<Integer> clistindexs1 = new ArrayList<>();
            ArrayList<Integer> clitemindexs1 = new ArrayList<>();
            //list2
            ArrayList<Integer> clistitemids2 = new ArrayList<>();
            ArrayList<String> clitemcontents2 = new ArrayList<>();
            ArrayList<Integer> clistindexs2 = new ArrayList<>();
            ArrayList<Integer> clitemindexs2 = new ArrayList<>();

            //和item相关的参数
            for(int j=0;j<iteminstids.size();j++){
                if(iteminstids.get(j).equals(instanceids.get(i))){
                    if(listindexs.get(j)==1){
                        clistitemids1.add(listitemids.get(j));
                        clitemcontents1.add(litemcontents.get(j));
                        clistindexs1.add(listindexs.get(j));
                        clitemindexs1.add(litemindexs.get(j));
                    }else{
                        clistitemids2.add(listitemids.get(j));
                        clitemcontents2.add(litemcontents.get(j));
                        clistindexs2.add(listindexs.get(j));
                        clitemindexs2.add(litemindexs.get(j));
                    }
                }
            }

            for(int j=0;j<doneinstids.size();j++){
                if(doneinstids.get(j).equals(instanceids.get(i))){
                    chuansdoneaitemids.add(doneaitemids.get(j));
                    chuansdonebitemids.add(donebitemids.get(j));
                }
            }

            bundle.putIntegerArrayList("itemid1p"+i, clistitemids1);
            bundle.putStringArrayList("itemcon1p"+i,clitemcontents1);
            bundle.putIntegerArrayList("itemlist1p"+i, clistindexs1);
            bundle.putIntegerArrayList("iteminst1p"+i, clitemindexs1);

            bundle.putIntegerArrayList("itemid2p"+i, clistitemids2);
            bundle.putStringArrayList("itemcon2p"+i,clitemcontents2);
            bundle.putIntegerArrayList("itemlist2p"+i, clistindexs2);
            bundle.putIntegerArrayList("iteminst2p"+i, clitemindexs2);

            bundle.putIntegerArrayList("chuansdoneaitemids"+i, chuansdoneaitemids);
            bundle.putIntegerArrayList("chuansdonebitemids"+i, chuansdonebitemids);


            Log.e("DoTask2Activity---->", "GET方式请求成功，listitemid1--->" + "itemid1p"+i+"——--"+clistitemids1);
            Log.e("DoTask2Activity---->", "GET方式请求成功，listitemid2--->" + "itemid2p"+i+"——--"+clistitemids2);
            Log.e("DoTask2Activity---->", "GET方式请求成功，itemcontent1--->" +"itemcon1p"+i+"----"+ clitemcontents1);
            Log.e("DoTask2Activity---->", "GET方式请求成功，itemcontent2--->" +"itemcon2p"+i+"----"+ clitemcontents2);

            Log.e("DoTask2Activity---->", "GET方式请求成功，listindex1--->" + "itemlist1p"+i+"----"+ clistindexs1);
            Log.e("DoTask2Activity---->", "GET方式请求成功，listindex2--->" + "itemlist2p"+i+"----"+clistindexs2);
            Log.e("DoTask2Activity---->", "GET方式请求成功，litemindex1--->" + "iteminst1p"+i+"----"+clitemindexs1);
            Log.e("DoTask2Activity---->", "GET方式请求成功，litemindex2--->" + "iteminst1p"+i+"----"+clitemindexs2);

            Log.e("DoTask2Activity---->", "GET方式请求成功，litemindex2--->" + "chuansdoneaitemids"+i+"----"+chuansdoneaitemids);
            Log.e("DoTask2Activity---->", "GET方式请求成功，litemindex2--->" + "chuansdonebitemids"+i+"----"+chuansdonebitemids);

            //todo 获取用户ID
            bundle.putInt("userid", 1);
            list.get(i).setArguments(bundle);
        }
        mMatchCategoryAdapter = new MatchCategoryAdapter(getSupportFragmentManager(),list);
        mMatchCategoryAdapter.notifyDataSetChanged();
        mViewPager.setAdapter(mMatchCategoryAdapter);
        mTabIndicator.setTitles(titles);
    }


    public void getFileContent(){
        String requestUrl = Constant.pairingfileUrl;
        String paramUrl;
        if(typename.equals("dotask")){
            paramUrl = "?docId="+docId+"&status="+docStatus+"&taskId="+taskid+"&userId="+userId;
        }else{
            paramUrl = "?docId="+docId+"&status="+docStatus+"&taskId="+taskid+"&userId="+userId;
            Log.e("ExtractActivity---->", "GET方式请求成功，result2---> 查看我做的任务");
        }

        OkHttpUtil.sendGetRequest(requestUrl + paramUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String listitem = response.body().string();
                JSONObject instanceItemjson = JSONObject.parseObject(listitem);
                JSONArray instanceItemArray = (JSONArray)instanceItemjson.get("instanceItem");
                fragmentsize = instanceItemArray.size();
                instanceids.clear();
                instanceindexs.clear();
                listitemids.clear();
                iteminstids.clear();
                litemcontents.clear();
                listindexs.clear();
                litemindexs.clear();
                if(instanceItemArray.size()>0){
                    for(int i=0;i<instanceItemArray.size();i++){
                        //遍历jsonarray数组，把每一个对象转成json对象
                        JSONObject job = instanceItemArray.getJSONObject(i);
                        //得到每个对象中的属性值
                        if(job.get("instid")!=null) {
                            //instance
                            instanceid = (Integer)job.get("instid");
                            instanceids.add(instanceid);
                            Log.e("DoTask2Activity---->", "activity中的instanceid--->" + instanceid);
                        }
                        if(job.get("instindex")!=null) {
                            //instance
                            String instindexstr = (String)job.get("instindex").toString();
                            instanceindex = Integer.valueOf(instindexstr);
                            instanceindexs.add(instanceindex);
                            Log.e("DoTask2Activity---->", "activity中的instanceindex--->" + instanceindex);
                        }
                        if(job.get("listitems")!=null) {
                            //instance
                            JSONArray itemList = (JSONArray)job.get("listitems");
                            for(int j=0;j<itemList.size();j++){
                                JSONObject item = itemList.getJSONObject(j);
                                if(item.get("ltid")!=null) {
                                    //instance
                                    listitemid = (Integer)item.get("ltid");
                                    listitemids.add(listitemid);
                                    iteminstids.add(instanceid);
                                    Log.e("DoTask2Activity---->", "activity中的listitemid--->" + listitemid);
                                }
                                if(item.get("litemcontent")!=null) {
                                    //instance
                                    litemcontent = (String)item.get("litemcontent").toString();
                                    litemcontents.add(litemcontent);
                                    Log.e("DoTask2Activity---->", "activity中的litemcontent--->" + litemcontent);
                                }
                                if(item.get("listIndex")!=null) {
                                    //instance
                                    String listindexstr = item.get("listIndex").toString();
                                    listindex = Integer.valueOf(listindexstr);
                                    listindexs.add(listindex);
                                    Log.e("DoTask2Activity---->", "activity中的listindex--->" + listindex);
                                }
                                if(item.get("litemindex")!=null) {
                                    //instance
                                    String litemindexstr = item.get("litemindex").toString();
                                    litemindex = Integer.valueOf(litemindexstr);
                                    litemindexs.add(litemindex);
                                    Log.e("DoTask2Activity---->", "activity中的litemindex--->" + litemindex);
                                }
                            }
                        }
                        //已经做过的部分
                        if(job.get("alreadyDone")!=null) {
                            JSONArray alreadyDonelArray = (JSONArray)job.get("alreadyDone");
                            if(alreadyDonelArray!=null&&alreadyDonelArray.size()>0){
                                for(int j=0;j<alreadyDonelArray.size();j++){
                                    //遍历jsonarray数组，把每一个对象转成json对象
                                    JSONObject alreadyDone = alreadyDonelArray.getJSONObject(j);
                                    //得到每个对象中的属性值
                                    if(alreadyDone.get("aLitemid")!=null) {
                                        //instance
                                        doneaitemid = (Integer)alreadyDone.get("aLitemid");
                                        doneinstids.add(instanceid);
                                        doneaitemids.add(doneaitemid);
                                        Log.e("DotaskOneCategory---->", "activity中的doneaitemid--->" + doneaitemid);
                                    }
                                    if(alreadyDone.get("bLitemid")!=null) {
                                        //instance
                                        donebitemid = (Integer)alreadyDone.get("bLitemid");
                                        donebitemids.add(donebitemid);
                                        Log.e("DotaskOneCategory---->", "activity中的donebitemid--->" + donebitemid);
                                    }
                                }
                            }
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
                if(btnfileid!=-1 && filestatus.equals("")){
                    //选了文件没选状态
                    if(btnfileid!=fileid.get(0)) {
                        docId = btnfileid;
                        //请求数据
                        getFileContent();
                    }
                }else if(btnfileid==-1 && !filestatus.equals("")){
                    //选了状态没选文件
                    if(!filestatus.equals("全部")){
                        docStatus = filestatus;
                        //请求数据
                        getFileContent();
                    }
                }else if(btnfileid!=-1 && !filestatus.equals("")){
                    Toast.makeText(DoTask2Activity.this, "选择了文件：" + btnfileid+filestatus, Toast.LENGTH_SHORT).show();
                    //如果文件和文件状态都没有选择默认值
                    //if(!filestatus.equals("全部")){
                    docId = btnfileid;
                    docStatus = filestatus;
                    //请求数据
                    getFileContent();
                    initFragment();
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

    //选中上面的item
    @Override
    public void transferString(int fragmentid,int itemid) {
            MatchCategoryFragment fragment = (MatchCategoryFragment) list.get(fragmentid);
            fragment.SetText(itemid);
            selectedaboveitemid = itemid;
    }

    //选中下面的item
    @Override
    public void transferStringblowxuanz(int fragmentid,int itemid) {
        MatchCategoryFragment fragment = (MatchCategoryFragment) list.get(fragmentid);
        fragment.SetTextblow(selectedaboveitemid,itemid);
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