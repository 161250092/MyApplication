package com.example.textannotation.view.doTask.textRelation;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.textannotation.myapplication.R;
import com.example.textannotation.presenter.doTask.relation.ITextRelationPresenter;
import com.example.textannotation.presenter.doTask.relation.TextRelationPresenter;
import com.example.textannotation.view.common.FlowGroupView;
import com.example.textannotation.util.SerializableMap;
import com.example.textannotation.view.doTask.IMenuAction;
import com.example.textannotation.view.doTask.textRelation.component.ExpandableTextView;
import com.example.textannotation.view.common.BaseLazyFragment;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 *
 * 文本关系 fragment
 */

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends BaseLazyFragment implements IMenuAction,ITextRelationView {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_CONTENT_NUMBER = "content_number";
    //设置加载动画
    private ProgressBar mPb;
    //handler创建于主线程，可用来更新视图
    private Handler mHandler = new Handler();
    //进度条加载完之后加载数据的view
    private LinearLayout fragmentlayout;
    //instance的标签
    private FlowGroupView instlabelview;
    //item1的标签
    private FlowGroupView item1labelview;
    //item2的标签
    private FlowGroupView item2labelview;

    //item1的内容
    private ExpandableTextView item1content;
    //item2的内容
    private ExpandableTextView item2content;

    private List<TextView> instanceLabelViews = new ArrayList<>();
    private List<TextView> item1LabelViews = new ArrayList<>();
    private List<TextView> item2LabelViews = new ArrayList<>();

    //标签数组 name
    private ArrayList<String> instancelabels = new ArrayList<>();
    private ArrayList<String> item1labels = new ArrayList<>();
    private ArrayList<String> item2labels = new ArrayList<>();
    //标签数组 id
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

    //bundle传递标签数据
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

    private ITextRelationPresenter iTextRelationPresenter;

    public PlaceholderFragment() {


    }
    //新建Fragment
    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    //设置加载动画
    @Override
    public void loadDataStart() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                fragmentlayout.setVisibility(View.VISIBLE);
            }
        });
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
            Log.e(labelname,labelid+"");
        }
        item2labelMap = (SerializableMap)bundle.get("item2labelmap");
        item2labelmap = (LinkedHashMap<String, Integer>)item2labelMap.getMap();
        for(String labelname:item2labelmap.keySet()){
            int labelid = item2labelmap.get(labelname);
            item2labels.add(labelname);
            item2labelid.add(labelid);
            Log.e(labelname,labelid+"");
        }
        Log.e(TAG,"init presenter");
        iTextRelationPresenter = new TextRelationPresenter(this);
        iTextRelationPresenter.loadDataAndInitView(taskid,docid,userId);

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


        //第一个item的标题
        ((TextView) view.findViewById(R.id.sample1).findViewById(R.id.title)).setText("(1)");
        //第二个item的标题
        ((TextView) view.findViewById(R.id.sample2).findViewById(R.id.title)).setText("(2)");
        //item1的内容
        item1content = (ExpandableTextView) view.findViewById(R.id.sample1).findViewById(R.id.expand_text_view);
        //item2的内容
        item2content = (ExpandableTextView) view.findViewById(R.id.sample2).findViewById(R.id.expand_text_view);



        Log.e("params---->", "Post方式请求成功，userID--->" + userId);

        mPb = view.findViewById(R.id.pb);
        if (mLoadDataFinished) {
            // 一般情况下这时候数据请求都还没完成, 所以不会进这个if
            fragmentlayout.setVisibility(View.VISIBLE);
            //mTextView.setText(mData);
            mPb.setVisibility(View.GONE);
        }


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
    public void getLastTask() {
        iTextRelationPresenter.getLastTask(taskid,instanceid,userId);
    }

    @Override
    public void getNextTask() {
        iTextRelationPresenter.getNextTask(taskid,instanceid,userId);
    }

    @Override
    public void saveAnnotationInfo() {
        iTextRelationPresenter.saveAnnotationInfo(getRequestBody());
    }

    private RequestBody getRequestBody(){
        if(instlabelids.size() == 0 || item1labelids.size() == 0 || item2labelids.size()==0){
            return null;
        }
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

    @Override
    public void initView(final int instanceId, final int itid1, final String content1, final int itid2, final String content2) {
        Log.e(TAG,"initView");
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                        instanceid = instanceId;
                        itemid1 = itid1;
                        itemid2 =itid2;
                        itemcontent1 = content1;
                        itemcontent2 = content2;
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

                mPb.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void updateContent(final int instanceId, final int itid1, final String content1, final int itid2, final String content2) {
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                instanceid = instanceId;
                itemid1 = itid1;
                itemid2 = itid2;
                itemcontent1 = content1;
                itemcontent2 = content2;
                Log.e("isntanceId",instanceid+"");
                initLabelText();
                ((TextRelationActivity)getActivity()).hideLoading();
                item1content.setText(itemcontent1);
                item2content.setText(itemcontent2);
            }
        });
    }

    @Override
    public void showExceptionInfo(String msg) {
        showInfo("",msg);
    }

    @Override
    public void showSubmitInfo(final String msg) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ((TextRelationActivity)getActivity()).uploadInfo(msg);
            }
        });
    }

    @Override
    public void showSimpleInfo(String msg) {
        showInfo("",msg);

    }
    @Override
    public void showCompletedInfo(final String msg) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ((TextRelationActivity)getActivity()).showCompletedNotice("",msg);
            }
        });
    }

    public void showInfo(final String title, final String msg){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ((TextRelationActivity)getActivity()).showNotice(title,msg);
                ((TextRelationActivity)getActivity()).hideLoading();
            }
        });
    }
}