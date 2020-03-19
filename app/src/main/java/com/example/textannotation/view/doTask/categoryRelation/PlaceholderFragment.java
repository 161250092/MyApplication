package com.example.textannotation.view.doTask.categoryRelation;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.textannotation.Constant.Constant;
import com.example.textannotation.model.ITaskUpload;
import com.example.textannotation.model.doTask.ITaskFragment;
import com.example.textannotation.myapplication.R;
import com.example.textannotation.network.OkHttpUtil;
import com.example.textannotation.util.threadPool.ThreadPool;
import com.example.textannotation.view.lazyfragment.BaseLazyFragment;
import com.example.textannotation.util.FlowGroupView;
import com.example.textannotation.util.HttpUtil;
import com.example.textannotation.util.MyApplication;
import com.example.textannotation.util.SerializableMap;

import java.io.IOException;
import java.util.*;

import com.example.textannotation.view.incompletePart.util.ExpandableTextView;
import com.google.gson.Gson;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 *
 * 文本关系 fragment
 */

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends BaseLazyFragment implements ITaskFragment {
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

    private List<TextView> instanceLabelViews = new ArrayList<>();
    private List<TextView> item1LabelViews = new ArrayList<>();
    private List<TextView> item2LabelViews = new ArrayList<>();

    //和设置instance标签相关的
    //instance标签数组
    private ArrayList<String> instancelabels = new ArrayList<>();
    private ArrayList<String> item1labels = new ArrayList<>();
    private ArrayList<String> item2labels = new ArrayList<>();

    private ArrayList<Integer> instancelabelid = new ArrayList<>();
    private ArrayList<Integer> item1labelid = new ArrayList<>();
    private ArrayList<Integer> item2labelid = new ArrayList<>();

    //选中的instlabel对应的TextView
    //选中的instlabel对应的名称
    private List<String> checkedinst = new ArrayList<String>();
    //选中的instlabel对应的ID
    private List<Integer> instlabelids = new ArrayList<>();

    //选中的item1label对应的TextView
    //选中的item1label对应的名称
    private List<String> checkeditem1 = new ArrayList<String>();
    //选中的item1label对应的ID
    private List<Integer> item1labelids = new ArrayList<>();

    //选中的item2label对应的TextView
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
    private int itemid1;
    private int itemid2;
    private String itemcontent1;
    private String itemcontent2;

    //任务ID
    private int taskid;

    private int docid;


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






    private void addTextView(int type,String str,FlowGroupView labelview,final LinkedHashMap<String,Integer> hashmap,final List<String> checked,final List<Integer> labelid) {
        TextView child = new TextView(getContext());
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
        params.setMargins(15, 15, 15, 15);

        child.setLayoutParams(params);
        child.setText(str);
        child.setPadding(20,5,20,5);
        child.setBackgroundResource(R.drawable.line_rect_huise);
        child.setTextColor(Color.BLACK);
        initEvents(child,hashmap,checked,labelid);
        //监听
        labelview.addView(child);

        switch (type){
            case 0:
                instanceLabelViews.add(child);
                break;
            case 1:
                item1LabelViews.add(child);
                break;
            case 2:
                item2LabelViews.add(child);
                break;
        }

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


    //设置加载动画
    @Override
    public void loadDataStart() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                fragmentlayout.setVisibility(View.VISIBLE);

                Bundle bundle = getArguments();
                taskid =  bundle.getInt("taskid");
                docid = bundle.getInt("docid");
                userId = bundle.getInt("userid");

                instlabelMap = (SerializableMap)bundle.get("instancelebelmap");
                instlabelmap = (LinkedHashMap<String, Integer>) instlabelMap.getMap();
                for(String labelname:instlabelmap.keySet()){
                    int labelid = instlabelmap.get(labelname);
                    //给标签数组赋值
                    instancelabels.add(labelname);
                    instancelabelid.add(labelid);
                }

                item1labelMap = (SerializableMap)bundle.get("item1labelmap");
                item1labelmap = (LinkedHashMap<String, Integer>)item1labelMap.getMap();
                for(String labelname:item1labelmap.keySet()){
                    int labelid = item1labelmap.get(labelname);
                    item1labels.add(labelname);
                    item1labelid.add(labelid);
                }
                item2labelMap = (SerializableMap)bundle.get("item2labelmap");
                item2labelmap = (LinkedHashMap<String, Integer>)item2labelMap.getMap();
                for(String labelname:item2labelmap.keySet()){
                    int labelid = item2labelmap.get(labelname);
                    item2labels.add(labelname);
                    item2labelid.add(labelid);
                }

                String paramUrl = "?docId="+docid+"&taskId="+taskid+"&userId="+userId;
                Log.e("mwx",Constant.getRelationCurrentTask+paramUrl);
                OkHttpUtil.sendGetRequest(Constant.getRelationCurrentTask+paramUrl, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        JSONObject data = JSONObject.parseObject(result);


                        instanceid = data.getInteger("instid");

                        itemid1 = data.getInteger("item1Id");
                        itemid2 = data.getInteger("item2Id");

                        itemcontent1 = data.getString("item1Content");
                        itemcontent2 = data.getString("item2Content");

                        initView();
                    }
                });

            }
        });


    }

    private void initView(){
        mHandler.post(new Runnable() {
            @Override
            public void run() {

                //先隐藏标记item按钮

                for (int i = 0; i < instancelabels.size(); i++) {
                    //addTextView(instancelabels.get(i),instlabelview,instlabelmap,checkedinst,instlabelids);
                    addTextView(0,instancelabels.get(i),instlabelview,instlabelmap,checkedinst,instlabelids);
                }
                //给item1的label赋值
                for (int i = 0; i < item1labels.size(); i++) {
                    //addTextView(item1labels.get(i),item1labelview,item1labelmap,checkeditem1,item1labelids);
                    addTextView(1,item1labels.get(i),item1labelview,item1labelmap,checkeditem1,item1labelids);
                }
                //给item1的label赋值
                for (int i = 0; i < item2labels.size(); i++) {
                    //addTextView(item2labels.get(i),item2labelview,item2labelmap,checkeditem2,item2labelids);
                    addTextView(2,item2labels.get(i),item2labelview,item2labelmap,checkeditem2,item2labelids);
                }

                Log.e("mwx",instancelabels.size()+" "+item1labels.size()+" "+item2labels.size());
                item1content.setText(itemcontent1);
                item2content.setText(itemcontent2);

                //先隐藏标记item按钮
                labelitembtn.setVisibility(View.GONE);
                mPb.setVisibility(View.GONE);
            }
        });

    }




    @Override
    protected View initRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "initRootView");
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
        ((TextView) view.findViewById(R.id.sample1).findViewById(R.id.title)).setText("part one");
        //第二个item的标题
        ((TextView) view.findViewById(R.id.sample2).findViewById(R.id.title)).setText("part two");
        //item1的内容
        item1content = (ExpandableTextView) view.findViewById(R.id.sample1).findViewById(R.id.expand_text_view);
        //item2的内容
        item2content = (ExpandableTextView) view.findViewById(R.id.sample2).findViewById(R.id.expand_text_view);

        //标记item按钮
        labelitembtn = (TextView) view.findViewById(R.id.labelitem);

        Log.e("params---->", "Post方式请求成功，userID--->" + userId);

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

        OkHttpUtil.sendPostRequest(Constant.relationdotaskUrl, getRequestBody(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                showInfo(result);
            }
        });
    }
    private RequestBody getRequestBody(){

        String instanceLabels = new Gson().toJson(instlabelids).replace("[","").replace("]","");

        String item1Labels = new Gson().toJson(item1labelids).replace("[","").replace("]","");

        String item2Labels = new Gson().toJson(item2labelids).replace("[","").replace("]","");

        Log.e("instanceLabels",instanceLabels);


        Log.e("item1Id",itemid1+"");
        Log.e("item1Labels",item1Labels);

        Log.e("item2Id",itemid2+"");
        Log.e("item2Labels",item2Labels);

        RequestBody requestBody = new FormBody.Builder()
                .add("taskId",taskid+"")
                .add("docId",docid+"")
                .add("instanceId",instanceid+"")
                .add("instanceLabels",instanceLabels)
                .add("item1Id",itemid1+"")
                .add("item1Labels",item1Labels)
                .add("item2Id",itemid2+"")
                .add("item2Labels",item2Labels)
                .add("userId",userId+"")
                .build();
        return requestBody;
    }

    public void showInfo(final String msg){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ((TextCategoryTabActivity)getActivity()).uploadInfo(msg);
            }
        });
    }

    @Override
    public void getCurrentTaskParagraph() {

    }

    @Override
    public void doNextTask() {
        checkedinst.clear();
        instlabelids.clear();

        checkeditem1.clear();
        item1labelids.clear();

        checkeditem2.clear();
        item2labelids.clear();


        String requestUrl = Constant.getRelationCurrentTask;
        String paramUrl = "?docId="+docid+"&taskId="+taskid+"&userId="+userId;
        Log.e("mwx",requestUrl + paramUrl);
        OkHttpUtil.sendGetRequest(requestUrl + paramUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                JSONObject data = JSONObject.parseObject(result);

                instanceid = data.getInteger("instid");
                itemid1 = data.getInteger("item1Id");
                itemid2 = data.getInteger("item2Id");

                itemcontent1 = data.getString("item1Content");
                itemcontent2 = data.getString("item2Content");

                Log.e("isntanceId",instanceid+"");
                updateContent();
            }
        });
    }

    private void updateContent(){

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        mHandler.post(new Runnable() {
            @Override
            public void run() {
                initLabelText();
                ((TextCategoryTabActivity)getActivity()).hideLoading();
                item1content.setText(itemcontent1);
                item2content.setText(itemcontent2);
            }
        });
    }

    private void initLabelText(){
        for (TextView textView:instanceLabelViews) {
            initEvents(textView,instlabelmap,checkedinst,instlabelids);
            textView.setBackgroundResource(R.drawable.line_rect_huise);
            textView.setTextColor(Color.BLACK);
        }

        for (TextView textView: item1LabelViews){
            initEvents(textView,item1labelmap,checkeditem1,item1labelids);
            textView.setBackgroundResource(R.drawable.line_rect_huise);
            textView.setTextColor(Color.BLACK);
        }

        for (TextView textView: item2LabelViews){
            initEvents(textView,item2labelmap,checkeditem2,item2labelids);
            textView.setBackgroundResource(R.drawable.line_rect_huise);
            textView.setTextColor(Color.BLACK);
        }



    }

    @Override
    public void passCurrentTask() {
        checkedinst.clear();
        instlabelids.clear();

        checkeditem1.clear();
        item1labelids.clear();

        checkeditem2.clear();
        item2labelids.clear();


        String requestUrl = Constant.passRelationCurrentTask;
        String paramUrl = "?docId="+docid+"&instanceId="+instanceid+"&taskId="+taskid+"&userId="+userId;
        Log.e("mwx",requestUrl + paramUrl);
        OkHttpUtil.sendGetRequest(requestUrl + paramUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                JSONObject data = JSONObject.parseObject(result);

                instanceid = data.getInteger("instid");
                itemid1 = data.getInteger("item1Id");
                itemid2 = data.getInteger("item2Id");

                itemcontent1 = data.getString("item1Content");
                itemcontent2 = data.getString("item2Content");
                updateContent();
            }
        });
    }

    @Override
    public void submitErrors(String text) {

            String requestUrl = Constant.submitErrorUrl;
            String params = "?docId="+docid+"&paraId="+instanceid+"&msg="+text+"&taskId="+taskid+"&userId="+userId;
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

    @Override
    public void compareOthersTextAnnotation() {

        OkHttpUtil.sendPostRequest(Constant.compareRelationAnnotation, getRequestBody(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                showInfo("比较信息",result);
            }
        });
    }


    public void showInfo(final String title, final String msg){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ((TextCategoryTabActivity)getActivity()).showNotice(title,msg);
            }
        });
    }
}