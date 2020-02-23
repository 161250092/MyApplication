package com.example.kongmin.view.doTask.categoryMatch;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.kongmin.myapplication.R;
import com.example.kongmin.view.lazyfragment.BaseLazyFragment;

import java.util.ArrayList;

/**
 *
 * 文本配对 配对选项
 * update by mwx
 */

/**
 * A placeholder fragment containing a simple view.
 */
public class MatchCategoryBelowFragment extends BaseLazyFragment {
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
    public TextView selectbtn;
    public TextView cancelBtn;

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

    public LinearLayout ll;

    private ArrayList<String> donebolwcolors= new ArrayList<String>();

    private int saboveitemids;

    private ArrayList<String> showedcolors= new ArrayList<String>();


    public MatchCategoryBelowFragment() {
    }


    //新建Fragment
    public static MatchCategoryBelowFragment newInstance(int sectionNumber) {
        MatchCategoryBelowFragment fragment = new MatchCategoryBelowFragment();
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

                    listitemid = bundle.getInt("listitemid2"+sectionnumber);
                    listitemcon = bundle.getString("listitemcon2"+sectionnumber);
                    listindexs =  bundle.getInt("listindexs2"+sectionnumber);
                    litemindexs = bundle.getInt("litemindexs2"+sectionnumber);
                    paragraphindex = bundle.getInt("paragraphindex");

                    donebolwcolors = bundle.getStringArrayList("chuansbcolor"+sectionnumber);
                    saboveitemids = bundle.getInt("saboveitemids"+sectionnumber);

                    selectbtn.setOnClickListener(new View.OnClickListener(){

                        @Override
                        public void onClick(View v) {
                            MatchCategoryFragment matchCategoryFragment  =(MatchCategoryFragment)getParentFragment();
                            if (matchCategoryFragment != null) {
                                int aboveItemId = matchCategoryFragment.getAboveItemId();
                                int belowItemId = getListitemid();

                                matchCategoryFragment.addMatchPair(aboveItemId,belowItemId);
                                selectbtn.setVisibility(View.GONE);
                                cancelBtn.setVisibility(View.VISIBLE);

                                int belowIndex = matchCategoryFragment.getBelowFragmentIndex();
                                matchCategoryFragment.addMatchInfoInAboveFragment(belowIndex);

                            }
                        }
                    });


                    cancelBtn.setOnClickListener(new View.OnClickListener(){

                        @Override
                        public void onClick(View v) {
                            MatchCategoryFragment matchCategoryFragment  =(MatchCategoryFragment)getParentFragment();
                            if (matchCategoryFragment != null) {
                                int aboveItemId = matchCategoryFragment.getAboveItemId();
                                int belowItemId = getListitemid();

                                matchCategoryFragment.deleteMatchPair(aboveItemId,belowItemId);
                                selectbtn.setVisibility(View.VISIBLE);
                                cancelBtn.setVisibility(View.GONE);

                                int belowIndex = matchCategoryFragment.getBelowFragmentIndex();
                                matchCategoryFragment.minusMatchInfoInAboveFragment(belowIndex);
                            }

                        }
                    });


                    //已经标注的item的button加上颜色
                    if(donebolwcolors!=null){
                        for(int i=0;i<donebolwcolors.size();i++){
                            if(showedcolors.contains(donebolwcolors.get(i))){

                            }else{
                                showedcolors.add(donebolwcolors.get(i));
                                donebutton(donebolwcolors.get(i));
                            }

                        }

                    }

                    mPb.setVisibility(View.GONE);
                }
            }
        }, 500);

    }

    @Override
    protected View initRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "initRootView");
        return inflater.inflate(R.layout.fragment_match_category_blow,container,false);

    }

    @Override
    protected void findViewById(View view) {

        fragmentlayout = (LinearLayout)view.findViewById(R.id.section_label);
        //存放content的textview
        itemcontent = (TextView) view.findViewById(R.id.section_label2);
        selectbtn = (TextView) view.findViewById(R.id.complete);
        cancelBtn = (TextView)view.findViewById(R.id.cancel_complete);
        ll = (LinearLayout) view.findViewById(R.id.section_label3);
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


    @Override
    public void onStart() {
       super.onStart();
    }


    public void donebutton(String colorstr){
        final TextView tv = new TextView(ll.getContext());
        //2.把信息设置为文本框的内容
        tv.setText("确定匹配");
        tv.setBackgroundColor(Color.parseColor(colorstr));
        tv.setTextColor(Color.BLACK);
        tv.setPadding(10,10,10,10);
        LinearLayout.LayoutParams LP_WW = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LP_WW.setMargins(0,0,10,5);
        LP_WW.gravity = Gravity.LEFT;
        tv.setLayoutParams(LP_WW);

        //3.把textView设置为线性布局的子节点
        ll.addView(tv);

    }

    public int getListitemid(){
        return listitemid;
    }

    public void showPairBtn(){
        selectbtn.setVisibility(View.VISIBLE);
        cancelBtn.setVisibility(View.GONE);
    }

    public void showCancelPairBtn(){
        selectbtn.setVisibility(View.GONE);
        cancelBtn.setVisibility(View.VISIBLE);
    }
}