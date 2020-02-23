package com.example.kongmin.view.doTask.categoryRelation;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.kongmin.Constant.Constant;
import com.example.kongmin.model.ITaskUpload;
import com.example.kongmin.myapplication.R;
import com.example.kongmin.util.threadPool.ThreadPool;
import com.example.kongmin.view.lazyfragment.BaseLazyFragment;
import com.example.kongmin.util.FlowGroupView;
import com.example.kongmin.util.HttpUtil;
import com.example.kongmin.util.MyApplication;
import com.example.kongmin.util.SerializableMap;
import java.util.*;

import com.example.kongmin.view.textcategory.util.ExpandableTextView;

/**
 *
 * 做任务两个分类界面
 * Created by kongmin
 * 2018.12.29
 */

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends BaseLazyFragment implements ITaskUpload {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_CONTENT_NUMBER = "content_number";

    //设置加载动画
    //private TextView mTextView;
    private ProgressBar mPb;
    private Handler mHandler = new Handler();

    //布局中的控件
    //进度条加载完之后加载数据的view
    private LinearLayout fragmentlayout;
    //instance的标签
    private FlowGroupView instlabelview;
    //item1的标签
    private FlowGroupView item1labelview;
    //item2的标签
    private FlowGroupView item2labelview;
    //item的label对应的可隐藏与显示的view
    private LinearLayout itemlabelview;
    //item1的内容
    private ExpandableTextView item1content;
    //item2的内容
    private ExpandableTextView item2content;
    //标记label按钮
    private TextView labelitembtn;
    //完成一段instance的按钮
    private TextView savebtn;
    private TextView completeinst;
    private TextView completedoc;
    //按钮的LinearLayout
    private LinearLayout extractlinear;



    //和设置instance标签相关的
    //instance标签数组
    private ArrayList<String> instancelabels = new ArrayList<>();
    private ArrayList<String> item1labels = new ArrayList<>();
    private ArrayList<String> item2labels = new ArrayList<>();

    private ArrayList<Integer> instancelabelid = new ArrayList<>();
    private ArrayList<Integer> item1labelid = new ArrayList<>();
    private ArrayList<Integer> item2labelid = new ArrayList<>();

    //选中的instlabel对应的TextView
    //private List<TextView> bitinstlabel = new ArrayList<>();
    //选中的instlabel对应的名称
    private List<String> checkedinst = new ArrayList<String>();
    //选中的instlabel对应的ID
    private List<Integer> instlabelids = new ArrayList<>();

    //选中的item1label对应的TextView
    //private List<TextView> bititem1label = new ArrayList<>();
    //选中的item1label对应的名称
    private List<String> checkeditem1 = new ArrayList<String>();
    //选中的item1label对应的ID
    private List<Integer> item1labelids = new ArrayList<>();

    //选中的item2label对应的TextView
    //private List<TextView> bititem2label = new ArrayList<>();
    //选中的item2label对应的名称
    private List<String> checkeditem2 = new ArrayList<String>();
    //选中的item2label对应的ID
    private List<Integer> item2labelids = new ArrayList<>();


    //做任务要使用的数据
    private SerializableMap instlabelMap = new SerializableMap();
    private LinkedHashMap<String,Integer> instlabelmap = new LinkedHashMap<>();
    private SerializableMap item1labelMap = new SerializableMap();
    private LinkedHashMap<String,Integer>  item1labelmap = new LinkedHashMap<>();
    private SerializableMap item2labelMap = new SerializableMap();
    private LinkedHashMap<String,Integer> item2labelmap = new LinkedHashMap<>();

    private int instanceid;
    private int instanceindex;
    private int itemid1;
    private int itemid2;
    private String itemcontent1;
    private String itemcontent2;
    private int itemindex1;
    private int itemindex2;

    private int instlabelnum;
    private int item1labelnum;
    private int item2labelnum;

    //任务ID
    private int taskid;
    //是做任务页面还是查看做任务页面
    private String typename;
    private int docid;
    private static int sectionnumber;

    private String issorted;
    //已经排好序的做任务的结果
    private ArrayList<Integer> seledinstid = new ArrayList<Integer>();
    private ArrayList<Integer> seledinst = new ArrayList<Integer>();
    private ArrayList<Integer> seleditem1 = new ArrayList<Integer>();
    private ArrayList<Integer> seleditem2 = new ArrayList<Integer>();

    private ArrayList<Integer> delseledinst = new ArrayList<Integer>();
    private ArrayList<Integer> delseleditem1 = new ArrayList<Integer>();
    private ArrayList<Integer> delseleditem2 = new ArrayList<Integer>();

    private MyApplication mApplication;
    private int userId;



    public PlaceholderFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */

    //新建Fragment
    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }




    public void initData(TextView completebtn,final String selectlabel){
        //完成本段的按钮
        completebtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //Toast.makeText(getContext(), "选中了"+selectlabel+" ", Toast.LENGTH_SHORT).show();
                //发送做任务的请求
                new Thread(runnable3).start();
            }
        });
    }


    private Runnable runnable3 = new Runnable() {
        @Override
        public void run() {
            // TODO: http request.
            //String requestUrl = "http://172.20.10.5:8080/dotask/addInstanceItem";
            //todo 先把这个注释掉
            issorted = "true";
            String requestUrl = Constant.relationdotaskUrl;

            StringBuffer instlabelstr = new StringBuffer();
            for(int i=0;i<instlabelids.size()-1;i++){
                instlabelstr.append(instlabelids.get(i)+",");
            }
            instlabelstr.append(instlabelids.get(instlabelids.size()-1));

            StringBuffer item1labelstr = new StringBuffer();
            for(int i=0;i<item1labelids.size()-1;i++){
                item1labelstr.append(item1labelids.get(i)+",");
            }
            item1labelstr.append(item1labelids.get(item1labelids.size()-1));

            StringBuffer item2labelstr = new StringBuffer();
            for(int i=0;i<item2labelids.size()-1;i++){
                item2labelstr.append(item2labelids.get(i)+",");
            }
            item2labelstr.append(item2labelids.get(item2labelids.size()-1));

            //要传递的参数
            //String params ="?userId="+userid+"&taskId="+taskid+"&instanceId="+instanceid+"&instanceLabels="+instlabelstr+
            //       "&itemId1="+itemid1+"&item1Labels="+item1labelstr+"&itemId2="+itemid2+"&item2Labels="+item2labelstr;
            String params ="?taskId="+taskid+"&docId="+docid+"&instanceId="+instanceid+"&instanceLabels="+instlabelstr+
                          "&item1Id="+itemid1+"&item1Labels="+item1labelstr+"&item2Id="+itemid2+"&item2Labels="+item2labelstr+"&userId="+userId;
            Log.e("params---->", "Post方式请求成功，params--->" + params);
            String result = HttpUtil.requestPost(requestUrl,params);
            Log.e("Placeholder---->", "Post方式请求成功，result--->" + result);
        }
    };

    private Runnable completeinstrun = new Runnable() {
        @Override
        public void run() {
            // TODO: http request.
            issorted = "true";
            String requestUrl = Constant.relationdtparaUrl;
            //要传递的参数
            String params ="?taskId="+taskid+"&docId="+docid+"&instanceId="+instanceid+"&userId="+userId;
            Log.e("params---->", "Post方式请求成功，pararunnable--->" + params);
            Log.e("taskid---->", "Post方式请求成功，pararunnable--->" + taskid);
            String result = HttpUtil.requestPost(requestUrl,params);
            Log.e("listview---->", "Post方式请求成功，pararunnable--->" + result);
            //等待请求结束
            JSONObject jsonObject= (JSONObject) JSON.parse(result);
            Integer data = (Integer) jsonObject.getInteger("status");
            if(data.toString().equals("0")){
                String loginres = jsonObject.getString("msg");
                Looper.prepare();
                Toast.makeText(getContext(),loginres,Toast.LENGTH_LONG).show();
                Looper.loop();
            }else{
                String loginres = jsonObject.getString("msg");
                Looper.prepare();
                Toast.makeText(getContext(),loginres,Toast.LENGTH_LONG).show();
                Looper.loop();
            }

        }
    };

    private Runnable completedocrun = new Runnable() {
        @Override
        public void run() {
            // TODO: http request.
            String requestUrl = Constant.relationdtdocUrl;
            //要传递的参数
            String params ="?taskId="+taskid+"&docId="+docid+"&userId="+userId;
            Log.e("params---->", "Post方式请求成功，docrunnable--->" + params);
            Log.e("taskid---->", "Post方式请求成功，docrunnable--->" + taskid);
            String result = HttpUtil.requestPost(requestUrl,params);
            Log.e("listview---->", "Post方式请求成功，docrunnable--->" + result);
            //等待请求结束
            JSONObject jsonObject= (JSONObject) JSON.parse(result);
            Integer data = (Integer) jsonObject.getInteger("status");
            if(data.toString().equals("0")){
                String loginres = jsonObject.getString("msg");
                Looper.prepare();
                Toast.makeText(getContext(),loginres,Toast.LENGTH_LONG).show();
                Looper.loop();
            }else{
                //上传的文件不符合要求
                //4011,"msg":"该文件你的段落还没有全部完成"
                String loginres = jsonObject.getString("msg");
                Looper.prepare();
                Toast.makeText(getContext(),loginres,Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }
    };



    private void addTextView(int labelidd,String str,FlowGroupView labelview,final LinkedHashMap<String,Integer> hashmap,final List<String> checked,final List<Integer> labelid,final ArrayList<Integer> seledlabel) {
        TextView child = new TextView(getContext());
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
        params.setMargins(15, 15, 15, 15);
        child.setLayoutParams(params);
        if(seledlabel.contains(labelidd)){
            child.setBackgroundResource(R.drawable.red_sold_round_sel);
            child.setTextColor(Color.WHITE);
        }else{
            child.setBackgroundResource(R.drawable.line_rect_huise);
            child.setTextColor(Color.BLACK);
        }
        child.setText(str);
        child.setPadding(20,5,20,5);

        //labellist.add(child);
        initEvents(child,hashmap,checked,labelid);
        //监听
        labelview.addView(child);
    }

    private void initEvents(final TextView tv,final LinkedHashMap<String,Integer> hashmap,final List<String> checked,final List<Integer> labelids){
        tv.setOnClickListener(new View.OnClickListener() {
            boolean button = false;
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String selectlabel = "";
                if(button==false) {
                    tv.setBackgroundResource(R.drawable.red_sold_round_sel);
                    tv.setTextColor(Color.WHITE);
                    //names.put(tv.getText().toString(),true);
                    checked.add(tv.getText().toString());
                    for(String labelname:hashmap.keySet()){
                        int labelid = hashmap.get(labelname);
                        if(tv.getText().toString().equals(labelname)) {
                            labelids.add(labelid);
                            break;
                        }
                    }
                    Toast.makeText(getContext(), tv.getText().toString()+"选中了", Toast.LENGTH_SHORT).show();
                    button = true;
                }else{
                    tv.setBackgroundResource(R.drawable.line_rect_huise);
                    tv.setTextColor(Color.BLACK);
                    //names.put(tv.getText().toString(),false);
                    for(String labelname:hashmap.keySet()){
                        int labelid = hashmap.get(labelname);
                        if(tv.getText().toString().equals(labelname)) {
                            for(int i=0;i<labelids.size();i++){
                                if(labelids.get(i)==labelid){
                                    labelids.remove(i);
                                    break;
                                }
                            }
                        }
                    }
                    checked.remove(tv.getText().toString());
                    Toast.makeText(getContext(), tv.getText().toString()+"取消选中了", Toast.LENGTH_SHORT).show();
                    button = false;
                }
                for(int i=0;i<checked.size();i++){
                    selectlabel+=checked.get(i);
                }
                initData(savebtn,selectlabel);
            }
        });
    }


    //设置加载动画
    @Override
    public void loadDataStart() {
        Log.d(TAG, "loadDataStart");

        // 模拟请求数据
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //mData = "这是加载下来的数据";
                // 一旦获取到数据, 就应该立刻标记数据加载完成
                mLoadDataFinished = true;
                if (mViewInflateFinished) {
                    fragmentlayout.setVisibility(View.VISIBLE);

                    //每次加载之前需要清空一下，否则会有数据重复加载的问题
                    //names.clear();


                    //获取传过来的数据
                    Bundle bundle =getArguments();
                    sectionnumber = bundle.getInt("fragmentindex");
                    taskid =  bundle.getInt("taskid");
                    docid = bundle.getInt("docid");
                    typename = bundle.getString("type");
                    //任务标签
                    instlabelMap = (SerializableMap)bundle.get("instancelebelmap");
                    instlabelmap = (LinkedHashMap<String, Integer>) instlabelMap.getMap();
                    for(String labelname:instlabelmap.keySet()){
                        int labelid = instlabelmap.get(labelname);
                        //给标签数组赋值
                        instancelabels.add(labelname);
                        instancelabelid.add(labelid);
                        Log.e("DotaskExtract---->", "GET方式请求成功，instancelabels--->" + labelname+labelid);
                    }
                    item1labelMap = (SerializableMap)bundle.get("item1labelmap");
                    item1labelmap = (LinkedHashMap<String, Integer>)item1labelMap.getMap();
                    for(String labelname:item1labelmap.keySet()){
                        int labelid = item1labelmap.get(labelname);
                        //给标签数组赋值
                        item1labels.add(labelname);
                        item1labelid.add(labelid);
                        Log.e("DotaskExtract---->", "GET方式请求成功，item1labels--->" + labelname+labelid);
                    }
                    item2labelMap = (SerializableMap)bundle.get("item2labelmap");
                    item2labelmap = (LinkedHashMap<String, Integer>)item2labelMap.getMap();
                    for(String labelname:item2labelmap.keySet()){
                        int labelid = item2labelmap.get(labelname);
                        //给标签数组赋值
                        item2labels.add(labelname);
                        item2labelid.add(labelid);
                        Log.e("DotaskExtract---->", "GET方式请求成功，item2labels--->" + labelname+labelid);
                    }

                    instanceid = bundle.getInt("instanceid"+sectionnumber);
                    instanceindex = bundle.getInt("instanceindex"+sectionnumber);
                    //和item相关的参数
                    itemid1 = bundle.getInt("itemid1page"+sectionnumber);
                    itemid2 = bundle.getInt("itemid2page"+sectionnumber);
                    itemcontent1 = bundle.getString("itemcontent1page"+sectionnumber);
                    itemcontent2 = bundle.getString("itemcontent2page"+sectionnumber);
                    itemindex1 = bundle.getInt("itemindex1page"+sectionnumber);
                    itemindex2 = bundle.getInt("itemindex2page"+sectionnumber);
                    if(itemindex1==2){
                        itemcontent1 = bundle.getString("itemcontent2page"+sectionnumber);
                        itemcontent2 = bundle.getString("itemcontent1page"+sectionnumber);
                    }
                    instlabelnum = bundle.getInt("instlabelnum");
                    item1labelnum = bundle.getInt("item1labelnum");
                    item2labelnum = bundle.getInt("item2labelnum");

                    Log.e("DotaskExtract---->", "GET方式请求成功，instanceid--->" + instanceid);
                    Log.e("DotaskExtract---->", "GET方式请求成功，instanceindex--->" + instanceindex);
                    Log.e("DotaskExtract---->", "GET方式请求成功，itemid1--->" + itemid1);
                    Log.e("DotaskExtract---->", "GET方式请求成功，itemid2--->" + itemid2);
                    Log.e("DotaskExtract---->", "GET方式请求成功，itemcontent1--->" + itemcontent1);
                    Log.e("DotaskExtract---->", "GET方式请求成功，itemcontent2--->" + itemcontent2);

                    Log.e("DotaskExtract---->", "GET方式请求成功，itemindex1--->" + itemindex1);
                    Log.e("DotaskExtract---->", "GET方式请求成功，itemindex2--->" + itemindex2);
                    Log.e("DotaskExtract---->", "GET方式请求成功，instlabelnum--->" + instlabelnum);
                    Log.e("DotaskExtract---->", "GET方式请求成功，item1labelnum--->" + item1labelnum);
                    Log.e("DotaskExtract---->", "GET方式请求成功，item2labelnum--->" + item2labelnum);


                     //对控件进行赋值
                    /*mTextView.setText(mData);
                    mTextView.setText("这是改变后的数据");*/
                    //textView2.setText(content);

                    issorted = bundle.getString("issorted"+sectionnumber);
                    if(issorted.equals("true")){
                        //todo 存在的itemId才加进去
                        seledinstid = bundle.getIntegerArrayList("seledinstid"+sectionnumber);
                        seledinst = bundle.getIntegerArrayList("seledinst"+sectionnumber);
                        seleditem1 = bundle.getIntegerArrayList("seleditem1"+sectionnumber);
                        seleditem2 = bundle.getIntegerArrayList("seleditem2"+sectionnumber);
                        delseledinst = seledinst;
                        delseleditem1 = seleditem1;
                        delseleditem2 = seleditem2;

                        Log.e("DotaskExtract---->", "GET方式请求成功，seledinst--->" +seledinst);
                        Log.e("DotaskExtract---->", "GET方式请求成功，seleditem1--->" +seleditem1);
                        Log.e("DotaskExtract---->", "GET方式请求成功，seleditem2--->" +seleditem2);
                        Log.e("DotaskExtract---->", "GET方式请求成功，issorted.equals(\"true\")--->" + "标注了排序的");
                    }

                    if(typename.equals("mypub")){
                        extractlinear.setVisibility(View.GONE);
                    }else{
                        extractlinear.setVisibility(View.VISIBLE);
                    }


                    for (int i = 0; i < instancelabels.size(); i++) {
                        //addTextView(instancelabels.get(i),instlabelview,instlabelmap,checkedinst,instlabelids);
                        addTextView(instancelabelid.get(i),instancelabels.get(i),instlabelview,instlabelmap,checkedinst,instlabelids,delseledinst);
                    }
                    //给item1的label赋值
                    for (int i = 0; i < item1labels.size(); i++) {
                        //addTextView(item1labels.get(i),item1labelview,item1labelmap,checkeditem1,item1labelids);
                        addTextView(item1labelid.get(i),item1labels.get(i),item1labelview,item1labelmap,checkeditem1,item1labelids,seleditem1);
                    }
                    //给item1的label赋值
                    for (int i = 0; i < item2labels.size(); i++) {
                        //addTextView(item2labels.get(i),item2labelview,item2labelmap,checkeditem2,item2labelids);
                        addTextView(item2labelid.get(i),item2labels.get(i),item2labelview,item2labelmap,checkeditem2,item2labelids,seleditem2);
                    }


                    item1content.setText(itemcontent1);
                    item2content.setText(itemcontent2);

                    //先隐藏标记item按钮
                    labelitembtn.setVisibility(View.GONE);


                    mPb.setVisibility(View.GONE);
                }
            }
        }, 500);

    }

    @Override
    protected View initRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "initRootView");
        //return inflater.inflate(R.layout.fragment_tab, container, false);
        // 开启一个子线程，进行网络操作，等待有返回结果，使用handler通知UI
        //new Thread(networkTask).start();
        return inflater.inflate(R.layout.fragment_text_category,container,false);

    }



    @Override
    protected void findViewById(View view) {
        //进度条加载完之后加载数据的view
        fragmentlayout = (LinearLayout)view.findViewById(R.id.above);
        //instance的标签
        instlabelview = (FlowGroupView)view.findViewById(R.id.instancelabel);
        //item1的标签
        item1labelview = (FlowGroupView)view.findViewById(R.id.item1label);
        //item2的标签
        item2labelview = (FlowGroupView)view.findViewById(R.id.item2label);
        //item的label对应的可隐藏与显示的view
        itemlabelview = (LinearLayout) view.findViewById(R.id.itemlabel);


        //第一个item的标题
        ((TextView) view.findViewById(R.id.sample1).findViewById(R.id.title)).setText("Item 1");
        //第二个item的标题
        ((TextView) view.findViewById(R.id.sample2).findViewById(R.id.title)).setText("Item 2");
        //item1的内容
        item1content = (ExpandableTextView) view.findViewById(R.id.sample1).findViewById(R.id.expand_text_view);
        //item2的内容
        item2content = (ExpandableTextView) view.findViewById(R.id.sample2).findViewById(R.id.expand_text_view);

        //标记item按钮
        labelitembtn = (TextView) view.findViewById(R.id.labelitem);
        //完成按钮
        //completebtn = (TextView) view.findViewById(R.id.completeinst);
        savebtn = (TextView) view.findViewById(R.id.saveinst);
        completeinst = (TextView) view.findViewById(R.id.completeinst);
        completedoc = (TextView) view.findViewById(R.id.completedoc);
        extractlinear = (LinearLayout) view.findViewById(R.id.extractlinear);

        // 获取整个应用的Application对象
        // 在不同的Activity中获取的对象是同一个
        mApplication = (MyApplication)getActivity().getApplication();
        userId = mApplication.getLoginUserId();
        Log.e("params---->", "Post方式请求成功，userID--->" + userId);

        savebtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //Toast.makeText(getContext(), "选中了"+selectlabel+" ", Toast.LENGTH_SHORT).show();
                //发送做任务的请求
                new Thread(runnable3).start();
            }
        });

        //完成一段
        completeinst.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //Toast.makeText(getContext(), "选中了"+selectlabel+" ", Toast.LENGTH_SHORT).show();
                //发送做任务的请求
                new Thread(completeinstrun).start();
            }
        });
        //完成整篇文档
        completedoc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //Toast.makeText(getContext(), "选中了"+selectlabel+" ", Toast.LENGTH_SHORT).show();
                //发送做任务的请求
                new Thread(completedocrun).start();
            }
        });

        mPb = view.findViewById(R.id.pb);
        if (mLoadDataFinished) {
            // 一般情况下这时候数据请求都还没完成, 所以不会进这个if
            fragmentlayout.setVisibility(View.VISIBLE);
            //mTextView.setText(mData);
            mPb.setVisibility(View.GONE);
        }


    }

    @Override
    public void saveIns() {
        ThreadPool.fixedThreadPool().submit(runnable3);
    }

    @Override
    public void completeCon() {
        ThreadPool.fixedThreadPool().submit(completeinstrun);
    }

    @Override
    public void completeDoc() {
        ThreadPool.fixedThreadPool().submit(completedocrun);
    }
}