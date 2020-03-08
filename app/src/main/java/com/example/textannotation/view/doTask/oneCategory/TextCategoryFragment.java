package com.example.textannotation.view.doTask.oneCategory;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.textannotation.Constant.Constant;
import com.example.textannotation.model.ITaskUpload;
import com.example.textannotation.model.doTask.ITaskFragment;
import com.example.textannotation.myapplication.R;

import com.example.textannotation.util.FlowGroupView;
import com.example.textannotation.util.HttpUtil;
import com.example.textannotation.util.MyApplication;
import com.example.textannotation.util.SerializableMap;
import com.example.textannotation.util.threadPool.ThreadPool;
import com.example.textannotation.view.lazyfragment.BaseLazyFragment;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TextCategoryFragment extends BaseLazyFragment implements ITaskFragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_CONTENT_NUMBER = "content_number";

    //设置加载动画
    private ProgressBar mPb;
    private Handler mHandler = new Handler();
    private String mData;

    private LinearLayout fragmentlayout;

    //段落内容
    private TextView paracontent;


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
    private Map<String, Integer> hashmap = new LinkedHashMap<>();
    private int contentid;
    //段落在文本中的索引
    private int contentindex;
    private String content;
    private int userid;


    private int docid;

    private static int sectionnumber;


    private String issorted;


    private MyApplication mApplication;
    private int userId;



    private Runnable savIns = new Runnable() {
        @Override
        public void run() {
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
            showHttpResponse(result);

        }
    };

    private void showHttpResponse(String str){
        JSONObject jsonObject= (JSONObject) JSON.parse(str);
        Integer data = (Integer) jsonObject.getInteger("status");
        if(data.toString().equals("0")){
            Looper.prepare();
            Toast.makeText(getContext(),Constant.SUBMIT_SUCCESS,Toast.LENGTH_LONG).show();
            Looper.loop();
        }else{
            Looper.prepare();
            Toast.makeText(getContext(),Constant.SUBMIT_FAIL,Toast.LENGTH_LONG).show();
            Looper.loop();
        }
    }

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
            showHttpResponse(result);
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
            showHttpResponse(result);
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

                    Bundle bundle = getArguments();
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
                    //文本ID
                    String contentidstr = "contentid"+sectionnumber;
                    contentid = bundle.getInt(contentidstr);
                    Log.e("DotaskExtract---->", "fragment中的contentidstr--->" + contentidstr);
                    contentindex = bundle.getInt("contentindex"+sectionnumber);
                    content = bundle.getString("content"+sectionnumber);
                    userid = bundle.getInt("userid");

                    issorted = bundle.getString("issorted"+sectionnumber);



                    paracontent.setText(content);
                    mPb.setVisibility(View.GONE);
                }
            }
        }, 300);

    }

    @Override
    protected View initRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_one_category,container,false);
    }



    @Override
    protected void findViewById(View view) {
        fragmentlayout = (LinearLayout)view.findViewById(R.id.section_label);
        //存放content的textview
        paracontent = (TextView) view.findViewById(R.id.section_label2);
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
    public void saveAnnotationInfo() {

    }

    @Override
    public void doNextTask() {

    }

    @Override
    public void passCurrentTask() {

    }

    @Override
    public void submitErrors() {

    }


}
