package com.example.textannotation.view.doTask.oneCategory;

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
import com.example.textannotation.Constant.Constant;
import com.example.textannotation.model.ITaskUpload;
import com.example.textannotation.model.doTask.ITaskFragment;
import com.example.textannotation.myapplication.R;
import com.example.textannotation.network.OkHttpUtil;
import com.example.textannotation.network.Url;
import com.example.textannotation.util.threadPool.ThreadPool;
import com.example.textannotation.view.lazyfragment.BaseLazyFragment;
import com.example.textannotation.util.FlowGroupView;
import com.example.textannotation.util.HttpUtil;
import com.example.textannotation.util.MyApplication;
import com.example.textannotation.util.SerializableMap;

import java.io.IOException;
import java.util.*;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 *
 * 做任务单个分类界面
 * Created by kongmin
 * 2018.12.29
 */


public class OneCategoryFragment extends BaseLazyFragment implements ITaskFragment {
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

    int pid;

    public OneCategoryFragment() {
    }


    //新建Fragment
    public static OneCategoryFragment newInstance(int sectionNumber) {
        OneCategoryFragment fragment = new OneCategoryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    //设置加载动画
    @Override
    public void loadDataStart() {
        //oldVersionLoadData();
        getCurrentTaskParagraph();
    }

    private void initView(){
        Log.e("OneCategory","initView");
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Log.e("OneCategory","run3");

                for (int i = 0; i < names.size(); i++) {
                    addTextView(namelabelid.get(i), names.get(i), seledparalabelid);
                }
                fragmentlayout.setVisibility(View.VISIBLE);
                paracontent.setText(content);
                mPb.setVisibility(View.GONE);
            }
        });


    }

    private void updateContent() {
        // sleep 1 second in case loading info disappear too fast
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                initLabelText();
                ((OneCategoryActivity)getActivity()).hideLoading();
                paracontent.setText(content);
            }
        });
    }

    private Runnable getCurrentTaskParagraph = new Runnable(){
        @Override
        public void run() {
            labelids.clear();
            names.clear();
            Bundle bundle = getArguments();

            Log.e("OneCategory","run2");
            taskid = bundle.getInt("taskid");
            docid = bundle.getInt("docid");
            userId = bundle.getInt("userid");
            labelMap = (SerializableMap) bundle.get("lebelmap");
            hashmap = labelMap.getMap();
            for (String labelname : hashmap.keySet()) {
                int labelid = hashmap.get(labelname);
                //给标签数组赋值
                names.add(labelname);
                namelabelid.add(labelid);
                Log.e("DotaskExtract---->", "GET方式请求成功，labelname+labelid--->" + labelname + labelid);
            }

            Log.e("OneCategory","run3");

            String requestUrl = Constant.getNextClassifyTask;
            String params = "?docId="+docid+"&taskId="+taskid+"&userId="+userId;
            String result = HttpUtil.requestPost(requestUrl,params);
            Log.e("OneCategory",requestUrl+params);
            Log.e("OneCategory", result);

            JSONObject jsonObject = JSONObject.parseObject(result);
            JSONObject  data = jsonObject.getJSONObject("data");
            pid = data.getInteger("pid");
            content = data.getString("paracontent");
            Log.e("One",content);
            initView();
        }
    };

    private Runnable doNextTask = new Runnable(){
        @Override
        public void run() {
            labelids.clear();
            names.clear();
            Bundle bundle = getArguments();

            taskid = bundle.getInt("taskid");
            docid = bundle.getInt("docid");
            userId = bundle.getInt("userid");
            labelMap = (SerializableMap) bundle.get("lebelmap");
            hashmap = labelMap.getMap();
            for (String labelname : hashmap.keySet()) {
                int labelid = hashmap.get(labelname);
                //给标签数组赋值
                names.add(labelname);
                namelabelid.add(labelid);
                Log.e("DotaskExtract---->", "GET方式请求成功，labelname+labelid--->" + labelname + labelid);
            }

            String requestUrl = Constant.getNextClassifyTask;
            String params = "?docId="+docid+"&taskId="+taskid+"&userId="+userId;
            String result = HttpUtil.requestPost(requestUrl,params);
            Log.e("OneCategory",requestUrl+params);
            Log.e("OneCategory", result);

            JSONObject jsonObject = JSONObject.parseObject(result);
            JSONObject  data = jsonObject.getJSONObject("data");
            pid = data.getInteger("pid");
            content = data.getString("paracontent");
            Log.e("One",pid+"");
            updateContent();
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

    private void initLabelText(){
        for (TextView textView:labellist) {
            initEvents(textView);
            textView.setBackgroundResource(R.drawable.line_rect_huise);
            textView.setTextColor(Color.BLACK);
        }
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
                    button = false;
                }
                for(int i=0;i<checked.size();i++){
                    selectlabel+=checked.get(i);
                }

            }
        });
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
            String params ="?taskId="+taskid+"&docId="+docid+"&paraId="+pid+"&labelId="+labelstr+"&userId="+userId;
            Log.e("mwx",requestUrl+params);
            String result = HttpUtil.requestPost(requestUrl,params);
            Log.e("mwx", "result--->" + result);
            showHttpResponse(result);
        }
    };

    private Runnable passCurrentTask = new Runnable() {
        @Override
        public void run() {
            String requestUrl = Constant.passCurrentTask;
            String params = "?docId="+docid+"&paraId="+pid+"&taskId="+taskid+"&userId="+userId;
            String result = HttpUtil.requestGet(requestUrl,params);
            Log.e("OneCategory",requestUrl+params);
            Log.e("OneCategory", result);

            JSONObject jsonObject = JSONObject.parseObject(result);
            JSONObject  data = jsonObject.getJSONObject("data");
            pid = data.getInteger("pid");
            content = data.getString("paracontent");
            Log.e("One",pid+"");
            updateContent();
        }
    };


    private Runnable submitErr = new Runnable() {

        @Override
        public void run() {
            final String requestUrl = Constant.submitErrorUrl;
            String params = "?docId="+docid+"&paraId="+pid+"&msg="+errorInfo+"&taskId="+taskid+"&userId="+userId;
            String result = HttpUtil.requestGet(requestUrl,params);

            OkHttpUtil.sendGetRequest(requestUrl + params, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.e("OneCategory", response.body().string());
                }
            });

        }
    };


    private Runnable compareOthersLabels = new Runnable() {

        @Override
        public void run() {
            if (labelids.size() <= 0)
                return;

            StringBuffer labelstr = new StringBuffer();
            for(int i=0;i<labelids.size()-1;i++){
                labelstr.append(labelids.get(i)+",");
            }
            labelstr.append(labelids.get(labelids.size()-1));

            String requestUrl = Constant.compareWithOtherAnnotation;
            String params = "?docId="+docid+"&taskId="+taskid+"&paraId="+pid+"&labelId="+labelstr+"&userId="+userId;
            Log.e("OneCategory",requestUrl+params);
            String result = HttpUtil.requestPost(requestUrl,params);
            Log.e("OneCategory", result);
            showCompareInfo(result);
        }
    };

    public void showCompareInfo( final String msg){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ((OneCategoryActivity)getActivity()).showOthersAnnotation(msg);
            }
        });
    }




    @Override
    public void saveAnnotationInfo() {
        ThreadPool.fixedThreadPool().submit(savIns);
    }

    @Override
    public void getCurrentTaskParagraph() {
        ThreadPool.fixedThreadPool().submit(getCurrentTaskParagraph);
    }

    @Override
    public void doNextTask() {
        ThreadPool.fixedThreadPool().submit(doNextTask);
    }

    @Override
    public void passCurrentTask() {
        ThreadPool.fixedThreadPool().submit(passCurrentTask);
    }


    private String errorInfo = "";

    @Override
    public void submitErrors(String text) {
        errorInfo = text;
        ThreadPool.fixedThreadPool().submit(submitErr);

    }

    @Override
    public void compareOthersTextAnnotation() {
        ThreadPool.fixedThreadPool().submit(compareOthersLabels);
    }



    private void oldVersionLoadData(){
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
                    taskid = bundle.getInt("taskid");
                    docid = bundle.getInt("docid");
                    typename = bundle.getString("type");
                    //任务标签
                    labelMap = (SerializableMap) bundle.get("lebelmap");
                    hashmap = labelMap.getMap();
                    for (String labelname : hashmap.keySet()) {
                        int labelid = hashmap.get(labelname);
                        //给标签数组赋值
                        names.add(labelname);
                        namelabelid.add(labelid);
                        Log.e("DotaskExtract---->", "GET方式请求成功，labelname+labelid--->" + labelname + labelid);
                    }
                    //段落的状态
                    parastatus = bundle.getString("parastatus" + sectionnumber);
                    //文本ID
                    String contentidstr = "contentid" + sectionnumber;
                    contentid = bundle.getInt(contentidstr);
                    Log.e("DotaskExtract---->", "fragment中的contentidstr--->" + contentidstr);
                    contentindex = bundle.getInt("contentindex" + sectionnumber);
                    content = bundle.getString("content" + sectionnumber);
                    userid = bundle.getInt("userid");

                    issorted = bundle.getString("issorted" + sectionnumber);
                    if (issorted.equals("true")) {
                        seledparalabelid = bundle.getIntegerArrayList("seledinstid" + sectionnumber);
                        Log.e("DotaskExtract---->", "GET方式请求成功，seledparalabelid--->" + seledparalabelid);
                    }

                    //setData();
                    //给instance的label赋值
                    for (int i = 0; i < names.size(); i++) {
                        addTextView(namelabelid.get(i), names.get(i), seledparalabelid);
                    }

                    Log.e("DotaskExtract---->", "GET方式请求成功，taskid--->" + taskid);
                    Log.e("DotaskExtract---->", "GET方式请求成功，contentid--->" + contentid);
                    Log.e("DotaskExtract---->", "GET方式请求成功，contentindex--->" + contentindex);
                    Log.e("DotaskExtract---->", "GET方式请求成功，content--->" + content);
                    Log.e("DotaskExtract---->", "GET方式请求成功，userid--->" + userid);


                    paracontent.setText(content);
                    mPb.setVisibility(View.GONE);

                }
            }
        }, 300);

    }


}