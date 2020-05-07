package com.example.textannotation.view.doTask.textMatch.section;

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
import com.example.textannotation.view.common.BaseLazyFragment;
import android.content.Context;

import java.util.ArrayList;

/**
 *
 * 文本配对 待配对部分
 * update by mwx
 *
 */

public class TextMatchAboveFragment extends BaseLazyFragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_CONTENT_NUMBER = "content_number";

    //设置加载动画
    private ProgressBar mPb;
    private Handler mHandler = new Handler();
    private String mData;


    private LinearLayout fragmentlayout;
    private TextView itemcontent;

    private TextView matchItems;

    private int taskid;
    private int userid;
    //是做任务页面还是查看做任务页面
    private String typename;

    private int instanceid;
    private int listitemid;
    private String listitemcon;
    private int listindexs;
    private int litemindexs;

    private static int sectionnumber;

    public boolean isselected = false;

    private int paragraphindex;

    private String doneabovecolor;

    private ArrayList<Integer> mBelowMatchItems;

    public TextMatchAboveFragment() {
    }


    //新建Fragment
    public static TextMatchAboveFragment newInstance(int sectionNumber) {
        TextMatchAboveFragment fragment = new TextMatchAboveFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
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

                    Bundle bundle =getArguments();
                    sectionnumber = bundle.getInt("fragmentindex");
                    taskid =  bundle.getInt("taskid");
                    userid = bundle.getInt("userid");
                    typename = bundle.getString("type");
                    instanceid =  bundle.getInt("instanceid");

                    listitemid = bundle.getInt("listitemid1"+sectionnumber);
                    listitemcon = bundle.getString("listitemcon1"+sectionnumber);
                    listindexs =  bundle.getInt("listindexs1"+sectionnumber);
                    litemindexs = bundle.getInt("litemindexs1"+sectionnumber);
                    paragraphindex = bundle.getInt("paragraphindex");

                    doneabovecolor = bundle.getString("chuansacolor"+sectionnumber);


                    itemcontent.setText(listitemcon);

                    mPb.setVisibility(View.GONE);
                }
            }
        }, 200);

    }

    @Override
    protected View initRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_match_category_above,container,false);
    }

    @Override
    protected void findViewById(View view) {

        fragmentlayout = (LinearLayout)view.findViewById(R.id.section_label);

        //存放content的textview
        itemcontent = (TextView) view.findViewById(R.id.section_label2);

        matchItems = (TextView)view.findViewById(R.id.match_items);

        mBelowMatchItems = new ArrayList<>();

        mPb = view.findViewById(R.id.pb);
        if (mLoadDataFinished) {
            // 一般情况下这时候数据请求都还没完成, 所以不会进这个if
            fragmentlayout.setVisibility(View.VISIBLE);
            //mTextView.setText(mData);
            mPb.setVisibility(View.GONE);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }


    public int getListitemid(){
        return listitemid;
    }

    public static final String  hintTextBegin = "已配对:";

    public void updateAddMatchInfo(int belowIndex){
        mBelowMatchItems.add(belowIndex);
        matchItems.setVisibility(View.VISIBLE);
        StringBuffer stringBuffer = new StringBuffer(hintTextBegin);
        for (Integer i : mBelowMatchItems){
            stringBuffer.append( (i+1) + "、");
        }
        stringBuffer.deleteCharAt(stringBuffer.length() -1);
        stringBuffer.append("部分");
        matchItems.setText(stringBuffer.toString());
    }

    public void updateMinusMatchInfo(int belowIndex){
        mBelowMatchItems.remove( (Integer) belowIndex);
        if (mBelowMatchItems.size() == 0)
            matchItems.setVisibility(View.GONE);

        else {
            StringBuffer stringBuffer = new StringBuffer(hintTextBegin);
            for (Integer i : mBelowMatchItems){
                stringBuffer.append( (i+1) +"、");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() -1);
            stringBuffer.append("部分");
            matchItems.setText(stringBuffer.toString());
        }
    }


}