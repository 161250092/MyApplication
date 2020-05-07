package com.example.textannotation.view.doTask.textCategory;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.*;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.textannotation.presenter.doTask.category.ITextCategoryPresenter;
import com.example.textannotation.presenter.doTask.category.TextCategoryPresenter;
import com.example.textannotation.view.doTask.IMenuAction;
import com.example.textannotation.myapplication.R;
import com.example.textannotation.view.common.BaseLazyFragment;
import com.example.textannotation.view.common.FlowGroupView;
import com.example.textannotation.util.SerializableMap;

import java.util.*;

/**
 * 做任务单个分类界面
 */
public class TextCategoryFragment extends BaseLazyFragment implements IMenuAction,ITextCategoryView {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_CONTENT_NUMBER = "content_number";

    private ProgressBar mPb;
    private Handler mHandler = new Handler();
    private LinearLayout fragmentlayout;
    private FlowGroupView labelview;
    //段落内容
    private TextView paracontent;
    //Intent中传递的labelName labelId
    private SerializableMap labelMap;
    private Map<String,Integer> hashmap = new LinkedHashMap<>();
    //标签名称
    private ArrayList<String> names = new ArrayList<String>();
    //标签名称
    private ArrayList<Integer> namelabelid = new ArrayList<Integer>();
    private List<TextView> labellist = new ArrayList<>();
    private List<String> checked = new ArrayList<String>();
    //选中的标签
    private List<Integer> labelids = new ArrayList<>();

    private String content;
    private int taskid;
    private int docid;
    private int userId;
    int pid;

    ITextCategoryPresenter iTextCategoryPresenter;

    public TextCategoryFragment() {
    }

    //新建Fragment
    public static TextCategoryFragment newInstance(int sectionNumber) {
        TextCategoryFragment fragment = new TextCategoryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    //设置加载动画
    @Override
    public void loadDataStart() {
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
        iTextCategoryPresenter = new TextCategoryPresenter(this);
        iTextCategoryPresenter.loadDataAndInitView(taskid,docid,userId);
    }

    public void initView( final JSONObject jsonObject){

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Log.e("OneCategory","run3");
                pid = jsonObject.getInteger("pid");
                content = jsonObject.getString("paracontent");
                Log.e("One",content);
                for (int i = 0; i < names.size(); i++) {
                    addTextView(names.get(i));
                }
                fragmentlayout.setVisibility(View.VISIBLE);
                paracontent.setText(content);
                mPb.setVisibility(View.GONE);
            }
        });


    }

    @Override
    public void updateContent(final JSONObject jsonObject) {
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                JSONObject  data = jsonObject.getJSONObject("data");
                pid = data.getInteger("pid");
                content = data.getString("paracontent");
                initLabelText();
                ((TextCategoryActivity)getActivity()).hideLoading();
                paracontent.setText(content);
            }
        });
    }

    @Override
    public void showExceptionInfo(final String msg) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(TextCategoryFragment.this.getActivity(),msg,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addTextView(String str ) {
        TextView child = new TextView(getContext());
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
        params.setMargins(15, 15, 15, 15);
        child.setLayoutParams(params);


        child.setBackgroundResource(R.drawable.line_rect_huise);
        child.setTextColor(Color.BLACK);

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

        mPb = view.findViewById(R.id.pb);
        if (mLoadDataFinished) {
            // 一般情况下这时候数据请求都还没完成, 所以不会进这个if
            fragmentlayout.setVisibility(View.VISIBLE);
            //mTextView.setText(mData);
            mPb.setVisibility(View.GONE);
        }
    }


    public void showSubmitInfo(final String msg){
        mHandler.post(new Runnable() {
            @Override
            public void run() {

                ((TextCategoryActivity)getActivity()).uploadInfo(msg);
            }
        });
    }

    @Override
    public void showSimpleInfo(final String msg){

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ((TextCategoryActivity)getActivity()).hideLoading();
                ((TextCategoryActivity)getActivity()).simpleInfo(msg);
            }
        });
    }

    public void showCompletedInfo( final String msg){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ((TextCategoryActivity)getActivity()).showCompletedTaskInfo();
            }
        });
    }


    @Override
    public void getLastTask() {
        labelids.clear();
        names.clear();
        iTextCategoryPresenter.getLastTask(taskid,pid,userId);
    }

    @Override
    public void getNextTask() {
        labelids.clear();
        names.clear();
        iTextCategoryPresenter.getNextTask(taskid,pid,userId);
    }

    @Override
    public void saveAnnotationInfo() {
        iTextCategoryPresenter.saveAnnotationInfo(labelids,pid,taskid,docid,userId);
    }

}