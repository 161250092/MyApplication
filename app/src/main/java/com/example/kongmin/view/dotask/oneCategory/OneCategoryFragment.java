package com.example.kongmin.view.doTask.oneCategory;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.*;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
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

/**
 *
 * 做任务单个分类界面
 * Created by kongmin
 * 2018.12.29
 */

/**
 * A placeholder fragment containing a simple view.
 */
public class OneCategoryFragment extends BaseLazyFragment implements ITaskUpload {
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
    private String mData;

    private LinearLayout fragmentlayout;

    //段落内容
    private TextView paracontent;
    //保存本段做任务内容
    private TextView saveinst;
    //完成本段按钮
    private TextView completecon;
    //完成本文档按钮
    private TextView completedoc;
    //按钮的LinearLayout
    private LinearLayout extractlinear;

    //和设置标签相关的
    //标签名称
    private ArrayList<String> names = new ArrayList<String>();
    //标签名称
    private ArrayList<Integer> namelabelid = new ArrayList<Integer>();
    private FlowGroupView labelview;
    private List<TextView> labellist = new ArrayList<>();

    private List<String> checked = new ArrayList<String>();
    private List<Integer> labelids = new ArrayList<>();


    private int taskid;
    //是做任务页面还是查看做任务页面
    private String typename;
    private SerializableMap labelMap;
    private Map<String,Integer> hashmap = new LinkedHashMap<>();
    private int contentid;
    //段落在文本中的索引
    private int contentindex;
    private String content;
    private int userid;


    private int docid;

    private static int sectionnumber;

    //传送过来的段落的状态
    private String parastatus;
    private String issorted;
    //已经排好序的做任务的结果
    private ArrayList<Integer> seledparalabelid = new ArrayList<Integer>();

    private MyApplication mApplication;
    private int userId;


    public OneCategoryFragment() {
    }


    //新建Fragment
    public static OneCategoryFragment newInstance(int sectionNumber) {
        OneCategoryFragment fragment = new OneCategoryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        //args.putInt(ARG_CONTENT_NUMBER, contentid);
        fragment.setArguments(args);
        return fragment;
    }


    private Runnable savIns = new Runnable() {
        @Override
        public void run() {
            // TODO: http request.
            //String requestUrl = "http://10.0.2.2:8080/dotask/addDoTask?";
            //String requestUrl = "http://172.20.10.5:8080/dotask/addClassifyTask";
            String requestUrl = Constant.doclassifytaskUrl;
            StringBuffer labelstr = new StringBuffer();
            for(int i=0;i<labelids.size()-1;i++){
                labelstr.append(labelids.get(i)+",");
            }
            labelstr.append(labelids.get(labelids.size()-1));
            //要传递的参数
            String params ="?taskId="+taskid+"&docId="+docid+"&paraId="+contentid+"&labelId="+labelstr+"&userId="+userId;
            Log.e("params---->", "Post方式请求成功，params--->" + params);
            Log.e("taskid---->", "Post方式请求成功，addtaskresult--->" + taskid);
            String result = HttpUtil.requestPost(requestUrl,params);
            Log.e("listview---->", "Post方式请求成功，result--->" + result);
        }
    };

    private Runnable completeinstrun = new Runnable() {
        @Override
        public void run() {
            // TODO: http request.
            issorted = "true";
            String requestUrl = Constant.extradtparaUrl;
            //要传递的参数
            String params ="?taskId="+taskid+"&docId="+docid+"&paraId="+contentid+"&userId="+userId;
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
            String requestUrl = Constant.extradtdocUrl;
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


    private void addTextView(int labelid,String str,final ArrayList<Integer> seledlabel) {
        TextView child = new TextView(getContext());
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
        params.setMargins(15, 15, 15, 15);
        child.setLayoutParams(params);
        if(seledlabel.contains(labelid)){
            child.setBackgroundResource(R.drawable.red_sold_round_sel);
            child.setTextColor(Color.WHITE);
        }else{
            child.setBackgroundResource(R.drawable.line_rect_huise);
            child.setTextColor(Color.BLACK);
        }
        child.setText(str);
        child.setPadding(20,5,20,5);
        labellist.add(child);
        initEvents(child);//监听
        labelview.addView(child);
    }
    /**
     * 为每个view 添加点击事件
     */
    private void initEvents(final TextView tv){
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
                mData = "这是加载下来的数据";
                // 一旦获取到数据, 就应该立刻标记数据加载完成
                mLoadDataFinished = true;
                if (mViewInflateFinished) {
                    fragmentlayout.setVisibility(View.VISIBLE);

                    //每次加载之前需要清空一下，否则会有数据重复加载的问题
                    names.clear();

                    Bundle bundle =getArguments();
                    /*String message = null;
                    if(bundle!=null){
                        message = bundle.getString("toFragment");
                    }
                    Log.e("DotaskExtract---->", "GET方式请求成功，toFragment--->" + message);*/
                    sectionnumber = bundle.getInt("fragmentindex");
                    taskid =  bundle.getInt("taskid");
                    docid = bundle.getInt("docid");
                    typename = bundle.getString("type");
                    //任务标签
                    labelMap = (SerializableMap)bundle.get("lebelmap");
                    hashmap = labelMap.getMap();
                    for(String labelname:hashmap.keySet()){
                        int labelid = hashmap.get(labelname);
                        //给标签数组赋值
                        names.add(labelname);
                        namelabelid.add(labelid);
                        Log.e("DotaskExtract---->", "GET方式请求成功，labelname+labelid--->" + labelname+labelid);
                    }
                    //段落的状态
                    parastatus = bundle.getString("parastatus"+sectionnumber);
                    //文本ID
                    String contentidstr = "contentid"+sectionnumber;
                    contentid = bundle.getInt(contentidstr);
                    Log.e("DotaskExtract---->", "fragment中的contentidstr--->" + contentidstr);
                    contentindex = bundle.getInt("contentindex"+sectionnumber);
                    content = bundle.getString("content"+sectionnumber);
                    userid = bundle.getInt("userid");

                    issorted = bundle.getString("issorted"+sectionnumber);
                    if(issorted.equals("true")){
                        //todo 存在的itemId才加进去
                        seledparalabelid = bundle.getIntegerArrayList("seledinstid"+sectionnumber);
                        Log.e("DotaskExtract---->", "GET方式请求成功，seledparalabelid--->" + seledparalabelid);
                    }

                    if(typename.equals("mypub")){
                        extractlinear.setVisibility(View.GONE);
                    }else{
                        extractlinear.setVisibility(View.VISIBLE);
                    }

                    //setData();
                    //给instance的label赋值
                    for (int i = 0; i < names.size(); i++) {
                        //addTextView(instancelabels.get(i),instlabelview,instlabelmap,checkedinst,instlabelids);
                        addTextView(namelabelid.get(i),names.get(i),seledparalabelid);
                    }

                    Log.e("DotaskExtract---->", "GET方式请求成功，taskid--->" + taskid);
                    Log.e("DotaskExtract---->", "GET方式请求成功，contentid--->" + contentid);
                    Log.e("DotaskExtract---->", "GET方式请求成功，contentindex--->" + contentindex);
                    Log.e("DotaskExtract---->", "GET方式请求成功，content--->" + content);
                    Log.e("DotaskExtract---->", "GET方式请求成功，userid--->" + userid);

                    /*mTextView.setText(mData);
                    mTextView.setText("这是改变后的数据");*/
                    paracontent.setText(content);

                    //setData();
                    /*for (int i = 0; i < names.size(); i++) {
                        addTextView(names.get(i));
                    }*/
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
        return inflater.inflate(R.layout.fragment_one_category,container,false);
    }



    @Override
    protected void findViewById(View view) {


        fragmentlayout = (LinearLayout)view.findViewById(R.id.section_label);
        //存放content的textview
        paracontent = (TextView) view.findViewById(R.id.section_label2);
        saveinst = (TextView) view.findViewById(R.id.complete);
        completecon = (TextView) view.findViewById(R.id.completecon);
        completedoc = (TextView) view.findViewById(R.id.completedoc);
        extractlinear = (LinearLayout) view.findViewById(R.id.extractlinear);

        //和设置标签相关的
        labelview = (FlowGroupView)view.findViewById(R.id.flowgroupview2);

        // 获取整个应用的Application对象
        // 在不同的Activity中获取的对象是同一个
        mApplication = (MyApplication)getActivity().getApplication();
        userId = mApplication.getLoginUserId();

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
        ThreadPool.fixedThreadPool().submit(savIns);
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