package com.example.kongmin.view.textcategory;

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
import android.widget.Toast;
import com.example.kongmin.myapplication.R;
import com.example.kongmin.view.lazyfragment.BaseLazyFragment;
import android.content.Context;

/**
 *
 * 做任务单个分类界面
 * Created by kongmin
 * 2018.12.29
 */

/**
 * A placeholder fragment containing a simple view.
 */
public class MatchCategoryAboveFragment extends BaseLazyFragment {
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

    private FragmentMessgeI callback;

    public boolean isselected = false;

    private int paragraphindex;

    private String doneabovecolor;

    public MatchCategoryAboveFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */

    //新建Fragment
    public static MatchCategoryAboveFragment newInstance(int sectionNumber) {
        MatchCategoryAboveFragment fragment = new MatchCategoryAboveFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_match_category_above, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        TextView textView2 = (TextView) rootView.findViewById(R.id.section_label2);
        //textView2.setText(getString(R.string.section_format,getArguments().getInt(ARG_CONTENT_NUMBER)));
        TextView textView3 = (TextView) rootView.findViewById(R.id.complete);
        initData(textView,textView2,textView3);

        return rootView;
    }*/

    public void initData(TextView itemcontent,final TextView selectbtn){
        itemcontent.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(getContext(), "选中了textView1", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        selectbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //if (isselected == false) {

                    callback.transferString(paragraphindex,listitemid);

                    MatchCategoryFragment matchCategoryFragment = (MatchCategoryFragment)MatchCategoryAboveFragment.this.getParentFragment();
                    //MatchCategoryFragment matchCategoryFragment = (MatchCategoryFragment)mainActivity.list.get(paragraphindex);
                    matchCategoryFragment.SetText(listitemid);
                    if(isselected == false) {
                        selectbtn.setBackgroundResource(R.drawable.red_sold_round_sel);
                        selectbtn.setTextColor(Color.WHITE);
                    }
                    isselected = true;
                    Toast.makeText(getContext(), "点击了页" +listitemid, Toast.LENGTH_SHORT).show();

                    //DoTask2Activity mainActivity = (DoTask2Activity) getActivity();

                    //MatchCategoryFragment matchCategoryFragment = (MatchCategoryFragment)MatchCategoryAboveFragment.this.getParentFragment();
                    //MatchCategoryFragment matchCategoryFragment = (MatchCategoryFragment)mainActivity.list.get(paragraphindex);
                    //matchCategoryFragment.SetText(sectionnumber);
                    /*for(int i=0;i<matchCategoryFragment.list1.size();i++){
                        MatchCategoryAboveFragment threeFragment = (MatchCategoryAboveFragment) matchCategoryFragment.list1.get(i);
                        //threeFragment.SetText(1);
                        threeFragment.selectbtn.setBackgroundResource(R.drawable.line_rect_huise);
                        threeFragment.selectbtn.setTextColor(Color.BLACK);
                        threeFragment.isselected = false;
                    }*/


                //}else{
                    /*selectbtn.setBackgroundResource(R.drawable.line_rect_huise);
                    selectbtn.setTextColor(Color.BLACK);
                    isselected = false;
                    Toast.makeText(getContext(), "取消点击了" + listitemid, Toast.LENGTH_SHORT).show();*/
                //}

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

                    Log.e("CategoryAbove---->", "GET方式请求成功，instanceid--->"+instanceid);
                    Log.e("CategoryAbove---->", "GET方式请求成功，listitemid--->"+listitemid);
                    Log.e("CategoryAbove---->", "GET方式请求成功，listitemcon--->"+listitemcon);
                    Log.e("CategoryAbove---->", "GET方式请求成功，listindexs--->"+listindexs);
                    Log.e("CategoryAbove---->", "GET方式请求成功，litemindexs--->"+litemindexs);

                    if(typename.equals("mypub")){
                        selectbtn.setVisibility(View.GONE);
                    }else{
                        selectbtn.setVisibility(View.VISIBLE);
                    }


                    initData(itemcontent,selectbtn);
                    itemcontent.setText(listitemcon);


                    //已经标注的item的button加上颜色
                    if(doneabovecolor!=null){
                        SetText(doneabovecolor);
                    }

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
        return inflater.inflate(R.layout.fragment_match_category_above,container,false);

    }

    @Override
    protected void findViewById(View view) {

        fragmentlayout = (LinearLayout)view.findViewById(R.id.section_label);
        //存放content的textview
        itemcontent = (TextView) view.findViewById(R.id.section_label2);
        selectbtn = (TextView) view.findViewById(R.id.complete);

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
        callback = (FragmentMessgeI) context;
    }

    public void SetText(String colorstr){
        /*selectbtn.setBackgroundResource(R.drawable.line_rect_huise);
        selectbtn.setTextColor(Color.BLACK);
        isselected = false;*/
        selectbtn.setBackgroundColor(Color.parseColor(colorstr));
        selectbtn.setTextColor(Color.BLACK);
    }

}