package com.example.kongmin.view.textcategory;

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
import com.example.kongmin.myapplication.R;

import android.support.v4.view.ViewPager;
import android.widget.Toast;
import com.example.kongmin.view.lazyfragment.BaseLazyFragment;
import com.example.kongmin.util.HttpUtil;
import com.example.kongmin.util.MyApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import java.util.Map;
import java.util.HashMap;

/**
 *
 * 做任务单个分类界面
 * Created by kongmin
 * 2018.12.29
 */

/**
 * A placeholder fragment containing a simple view.
 */
public class MatchCategoryFragment extends BaseLazyFragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_CONTENT_NUMBER = "content_number";


    private MatchCategoryAboveAdapter mMatchCategoryAdapter1;
    private MatchCategoryBlowAdapter mMatchCategoryAdapter2;

    private ViewPager mViewPager1;
    private ViewPager mViewPager2;
    //导航栏
    private MyTabIndicator mTabIndicator1;
    private MyTabIndicator mTabIndicator2;
    //初始化数据
    public List<MatchCategoryAboveFragment> list1 = new ArrayList<>();
    private List<String> titles1 = new ArrayList<>();

    private List<MatchCategoryBlowFragment> list2 = new ArrayList<>();
    private List<String> titles2 = new ArrayList<>();

    //设置加载动画
    //private TextView mTextView;
    private ProgressBar mPb;
    private Handler mHandler = new Handler();
    //布局中的控件
    //进度条加载完之后加载数据的view
    private LinearLayout fragmentlayout;


    //做任务要使用的数据
    private int instanceid;
    private int instanceindex;

    //每一个fragment对应的item的数据
    //list1
    private ArrayList<Integer> clistitemids1 = new ArrayList<>();
    private ArrayList<String> clitemcontents1 = new ArrayList<>();
    private ArrayList<Integer> clistindexs1 = new ArrayList<>();
    private ArrayList<Integer> clitemindexs1 = new ArrayList<>();
    //list2
    private ArrayList<Integer> clistitemids2 = new ArrayList<>();
    private ArrayList<String> clitemcontents2 = new ArrayList<>();
    private ArrayList<Integer> clistindexs2 = new ArrayList<>();
    private ArrayList<Integer> clitemindexs2 = new ArrayList<>();

    //已经做过的部分
    private ArrayList<Integer> chuansdoneaitemids = new ArrayList<>();
    private ArrayList<Integer> chuansdonebitemids = new ArrayList<>();


    //任务ID
    private int taskid;
    //是做任务页面还是查看做任务页面
    private String typename;
    private int docId;
    //userid
    private int userid;
    private static int sectionnumber;

    private FragmentMessgeI callback;

    //随机生成的颜色
    private String colorstr;

    private Map<Integer,String> hashmap = new HashMap<>();

    private int selectedinstanceid;
    private int selectedblowitemid;
    private int unselectedblowitemid;
    private ArrayList<Integer> selectedaboveitemids = new ArrayList<Integer>();
    private ArrayList<Integer> selectedblowitemids = new ArrayList<Integer>();

    private String tasktype;

    //提交本段按钮
    private TextView saveinst;
    //完成本段按钮
    private TextView completecon;
    //完成本文档按钮
    private TextView completedoc;

    private ArrayList<Integer> doneaboveitems = new ArrayList<Integer>();
    private ArrayList<Integer> donebolwitems = new ArrayList<Integer>();
    private ArrayList<String> donecolor = new ArrayList<String>();

    private String doneabovecolor;
    private String donebolwcolor;

    private MyApplication mApplication;
    private int userId;

    public MatchCategoryFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */

    //新建Fragment
    public static MatchCategoryFragment newInstance(int sectionNumber) {
        MatchCategoryFragment fragment = new MatchCategoryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    /* @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_match_category, container, false);
        mTabIndicator1 = (MyTabIndicator) rootView.findViewById(R.id.mTabIndicator1);
        mViewPager1 = (ViewPager) rootView.findViewById(R.id.mViewPager1);
        mTabIndicator2 = (MyTabIndicator) rootView.findViewById(R.id.mTabIndicator2);
        mViewPager2 = (ViewPager) rootView.findViewById(R.id.mViewPager2);
        //textView3.setBackgroundResource(R.drawable.red_sold_round_sel);
        //textView3.setTextColor(Color.WHITE);
        initData();
        return rootView;
    }*/

    //设置加载动画
    @Override
    public void loadDataStart() {
        Log.d(TAG, "loadDataStart");
        // 模拟请求数据
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 一旦获取到数据, 就应该立刻标记数据加载完成
                mLoadDataFinished = true;
                if (mViewInflateFinished) {
                    fragmentlayout.setVisibility(View.VISIBLE);

                    //获取传过来的数据
                    Bundle bundle =getArguments();
                    sectionnumber = bundle.getInt("fragmentindex");
                    taskid = bundle.getInt("taskid");
                    userid = bundle.getInt("userid");
                    //是做任务页面还是查看做任务页面
                    typename = bundle.getString("type");
                    docId = bundle.getInt("docid");
                    instanceid = bundle.getInt("instanceid"+sectionnumber);
                    instanceindex = bundle.getInt("instanceindex"+sectionnumber);
                    Log.e("MatchCategory---->", "GET方式请求成功，sectionnumber--->" + sectionnumber);

                    clistitemids1 =  bundle.getIntegerArrayList("itemid1p"+sectionnumber);
                    clitemcontents1 = bundle.getStringArrayList("itemcon1p"+sectionnumber);
                    clistindexs1 = bundle.getIntegerArrayList("itemlist1p"+sectionnumber);
                    clitemindexs1 = bundle.getIntegerArrayList("iteminst1p"+sectionnumber);

                    clistitemids2 =bundle.getIntegerArrayList("itemid2p"+sectionnumber);
                    clitemcontents2 = bundle.getStringArrayList("itemcon2p"+sectionnumber);
                    clistindexs2 = bundle.getIntegerArrayList("itemlist2p"+sectionnumber);
                    clitemindexs2 = bundle.getIntegerArrayList("iteminst2p"+sectionnumber);

                    chuansdoneaitemids = bundle.getIntegerArrayList("chuansdoneaitemids"+sectionnumber);
                    chuansdonebitemids = bundle.getIntegerArrayList("chuansdonebitemids"+sectionnumber);


                    tasktype = bundle.getString("tasktype");
                    Log.e("MatchCategory---->", "GET方式请求成功，instanceid--->" + instanceid);
                    Log.e("MatchCategory---->", "GET方式请求成功，instanceindex--->" + instanceindex);

                    Log.e("MatchCategory---->", "GET方式请求成功，listitemid1--->" + clistitemids1);
                    Log.e("MatchCategory---->", "GET方式请求成功，listitemid2--->" + clistitemids2);
                    Log.e("MatchCategory---->", "GET方式请求成功，itemcontent1--->" + clitemcontents1);
                    Log.e("MatchCategory---->", "GET方式请求成功，itemcontent2--->" + clitemcontents2);

                    Log.e("MatchCategory---->", "GET方式请求成功，listindex1--->" + clistindexs1);
                    Log.e("MatchCategory---->", "GET方式请求成功，listindex2--->" + clistindexs2);
                    Log.e("MatchCategory---->", "GET方式请求成功，litemindex1--->" + clitemindexs1);
                    Log.e("MatchCategory---->", "GET方式请求成功，litemindex2--->" + clitemindexs2);

                    Log.e("MatchCategory---->", "GET方式请求成功，chuansdoneaitemids--->" + chuansdoneaitemids);
                    Log.e("MatchCategory---->", "GET方式请求成功，chuansdonebitemids--->" + chuansdonebitemids);


                    for(int i=0;i<chuansdoneaitemids.size();i++) {
                        //selectedaboveitemids.add(chuansdoneaitemids.get(i));
                        //selectedblowitemids.add(chuansdonebitemids.get(i));
                        initDonebutton(chuansdoneaitemids.get(i), chuansdonebitemids.get(i));
                        Log.e("MatchCategory---->", "GET方式请求成功，进入循环了--->" + chuansdoneaitemids.get(i)+"---"+chuansdonebitemids.get(i));
                    }


                    //对控件进行赋值
                    initData();



                    //数据加载完毕设置进度条可见
                    mPb.setVisibility(View.GONE);
                }
            }
        }, 500);

    }

     public void initData(){
        /*for(int i=0;i<7;i++){
            titles1.add("标题:" + i+1);
            MatchCategoryAboveFragment m1 = MatchCategoryAboveFragment.newInstance(i+3);
            list1.add(m1);

            titles2.add("标题:" + i+1);
            MatchCategoryBlowFragment m2 = MatchCategoryBlowFragment.newInstance(i+3);
            list2.add(m2);
        }*/

        for(int i=0;i<clistitemids1.size();i++){
            //导航栏加标题
            titles1.add("第"+clitemindexs1.get(i)+"部分");
            MatchCategoryAboveFragment m1 = MatchCategoryAboveFragment.newInstance(i);
            list1.add(m1);

            Bundle bundle = new Bundle();
            //传递lebel数据
            bundle.putInt("fragmentindex",i);
            bundle.putInt("taskid", taskid);
            bundle.putInt("instanceid",instanceid);
            bundle.putString("type",typename);
            bundle.putInt("listitemid1" + i,clistitemids1.get(i));
            bundle.putString("listitemcon1" + i,clitemcontents1.get(i));
            bundle.putInt("listindexs1" + i,clistindexs1.get(i));
            bundle.putInt("litemindexs1" + i,clitemindexs1.get(i));
            bundle.putInt("paragraphindex",sectionnumber);
            for(int j=0;j<doneaboveitems.size();j++){
                Log.e("MatchCategory---->", "GET方式请求成功，进去了listitemid1--->"+doneaboveitems.get(j));
                if(doneaboveitems.get(j)==clistitemids1.get(i)){
                    doneabovecolor = donecolor.get(j);
                    bundle.putString("chuansacolor"+i,doneabovecolor);
                    break;
                }
            }

            Log.e("MatchCategory---->", "GET方式请求成功，listitemid1--->"+clistitemids1.get(i));
            Log.e("MatchCategory---->", "GET方式请求成功，listitemcon1--->"+clitemcontents1.get(i));
            Log.e("MatchCategory---->", "GET方式请求成功，listindexs1--->"+clistindexs1.get(i));
            Log.e("MatchCategory---->", "GET方式请求成功，litemindexs1--->"+clitemindexs1.get(i));

            Log.e("MatchCategory---->", "GET方式请求成功，doneabovecolor--->"+doneabovecolor);

            //todo 获取用户ID
            bundle.putInt("userid", 1);
            list1.get(i).setArguments(bundle);
        }

        for(int i=0;i<clistitemids2.size();i++){
            //导航栏加标题
            //int itemnum = clitemindexs2.get(i) - clistitemids1.size();
            int itemnum = clitemindexs2.get(i);
            titles2.add("第"+itemnum+"部分");
            MatchCategoryBlowFragment m1 = MatchCategoryBlowFragment.newInstance(i);
            list2.add(m1);

            Bundle bundle = new Bundle();
            //传递lebel数据
            bundle.putInt("fragmentindex",i);
            bundle.putInt("taskid", taskid);
            bundle.putInt("instanceid",instanceid);
            bundle.putString("type",typename);
            bundle.putInt("listitemid2" + i,clistitemids2.get(i));
            bundle.putString("listitemcon2" + i,clitemcontents2.get(i));
            bundle.putInt("listindexs2" + i,clistindexs2.get(i));
            bundle.putInt("litemindexs2" + i,clitemindexs2.get(i));
            bundle.putInt("paragraphindex",sectionnumber);

            ArrayList<String> donebolwcolors= new ArrayList<String>();
            //传递给下面的item删除时用的
            int saboveitemids = 0;
            for(int j=0;j<donebolwitems.size();j++){
                if(donebolwitems.get(j)==clistitemids2.get(i)){
                    donebolwcolor = donecolor.get(j);
                    saboveitemids = doneaboveitems.get(j);
                    donebolwcolors.add(donebolwcolor);
                }
            }
            if(donebolwcolors.size()!=0) {
                bundle.putStringArrayList("chuansbcolor"+i,donebolwcolors);
                bundle.putInt("saboveitemids"+i,saboveitemids);
            }

            Log.e("MatchCategory---->", "GET方式请求成功，listitemid2--->"+clistitemids2.get(i));
            Log.e("MatchCategory---->", "GET方式请求成功，listitemcon2--->"+clitemcontents2.get(i));
            Log.e("MatchCategory---->", "GET方式请求成功，listindexs2--->"+clistindexs2.get(i));
            Log.e("MatchCategory---->", "GET方式请求成功，litemindexs2--->"+clitemindexs2.get(i));
            Log.e("MatchCategory---->", "GET方式请求成功，donebolwcolors--->"+donebolwcolors);

            //todo 获取用户ID
            bundle.putInt("userid", 1);
            list2.get(i).setArguments(bundle);
        }


        mMatchCategoryAdapter1 = new MatchCategoryAboveAdapter(getChildFragmentManager(),list1);
        mMatchCategoryAdapter2 = new MatchCategoryBlowAdapter(getChildFragmentManager(),list2);

        mViewPager1.setAdapter(mMatchCategoryAdapter1);
        mViewPager2.setAdapter(mMatchCategoryAdapter2);


        mTabIndicator1.setTitles(titles1);
        mTabIndicator1.setViewPager(mViewPager1, 0);
        //mTabIndicator.setTitles(titles, 3);//可以设置默认选中的title

        mTabIndicator1.setOnPageChangeListener(new MyTabIndicator.PageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Toast.makeText(getContext(), "选择了：" + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        mTabIndicator2.setTitles(titles2);
        mTabIndicator2.setViewPager(mViewPager2, 0);
        //mTabIndicator.setTitles(titles, 3);//可以设置默认选中的title

        mTabIndicator2.setOnPageChangeListener(new MyTabIndicator.PageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Toast.makeText(getContext(), "选择了：" + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    protected View initRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "initRootView");
        //return inflater.inflate(R.layout.fragment_tab, container, false);
        // 开启一个子线程，进行网络操作，等待有返回结果，使用handler通知UI
        //new Thread(networkTask).start();
        return inflater.inflate(R.layout.fragment_match_category,container,false);

    }


    /**
     * 获取十六进制的颜色代码.例如  "#6E36B4" , For HTML ,
     * @return String
     */
    public static String getRandColorCode(){
        String r,g,b;
        Random random = new Random();
        r = Integer.toHexString(random.nextInt(256)).toUpperCase();
        g = Integer.toHexString(random.nextInt(256)).toUpperCase();
        b = Integer.toHexString(random.nextInt(256)).toUpperCase();

        r = r.length()==1 ? "0" + r : r ;
        g = g.length()==1 ? "0" + g : g ;
        b = b.length()==1 ? "0" + b : b ;

        return r+g+b;
    }

    public void initDonebutton(int selectedaboveitemid,int itemid){

        String colorstr2 = SetText(selectedaboveitemid);
        doneaboveitems.add(selectedaboveitemid);
        donebolwitems.add(itemid);
        donecolor.add(colorstr2);


        /*Log.e("DotaskOneCategory---->", "SetText-->" + selectedaboveitemid+"----"+selectedblowitemid);
        for(int j=0;j<clistitemids1.size();j++){
            if(clistitemids1.get(j)==selectedaboveitemid) {
                Log.e("DotaskOneCategory---->", "SetText2-->" + selectedaboveitemid+"----"+selectedblowitemid);
                MatchCategoryAboveFragment abovefragment = (MatchCategoryAboveFragment) list1.get(j);
                //abovefragment.SetText(colorstr);
                if(abovefragment.selectbtn!=null){
                    Log.e("DotaskOneCategory---->", "SetText3-->" + selectedaboveitemid+"----"+selectedblowitemid);
                    abovefragment.selectbtn.setBackgroundColor(Color.parseColor(colorstr));
                    abovefragment.selectbtn.setTextColor(Color.BLACK);
                }
            }
        }

        for(int j=0;j<clistitemids2.size();j++){
            if(clistitemids2.get(j)==itemid) {
                MatchCategoryBlowFragment fragment = (MatchCategoryBlowFragment) list2.get(j);
                //fragment.donebutton(colorstr);
                if(fragment.selectbtn!=null){
                    //fragment.selectbtn.setBackgroundColor(Color.parseColor(colorstr));
                    //fragment.selectbtn.setTextColor(Color.BLACK);

                    //fragment.SetText(colorstr);
                    final TextView tv = new TextView(getContext());
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
                    fragment.ll.addView(tv);
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SetTextblowunselected(selectedaboveitemid,itemid,tv);
                        }
                    });
                    break;
                }
                Log.e("DotaskOneCategory---->", "selectedblowitemid--->" + selectedaboveitemid+"----"+selectedblowitemid);
            }

        }*/

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (FragmentMessgeI) context;
    }
    //选择上面的一个item
    public String SetText(int itemid){

         if(hashmap.containsKey(itemid)){
             colorstr = hashmap.get(itemid);
         }else{
             //随机生成一个颜色
             colorstr = "#"+getRandColorCode();
             hashmap.put(itemid,colorstr);
         }
         return colorstr;
        /*for(int j=0;j<clistitemids1.size();j++){
            if(clistitemids1.get(j)!=itemid) {
                MatchCategoryAboveFragment fragment = (MatchCategoryAboveFragment) list1.get(j);
                //fragment.SetText(itemid);
                if(fragment.selectbtn!=null){
                    fragment.selectbtn.setBackgroundResource(R.drawable.line_rect_huise);
                    fragment.selectbtn.setTextColor(Color.BLACK);
                    fragment.isselected = false;
                    Log.e("MatchCategory---->", "取消点击了" + j + "----没电" + itemid);
                }
            }
        }*/

    }

    public void SetTextblow(final int selectedaboveitemid,final int itemid){

        for(int j=0;j<clistitemids1.size();j++){
            if(clistitemids1.get(j)==selectedaboveitemid) {
                MatchCategoryAboveFragment abovefragment = (MatchCategoryAboveFragment) list1.get(j);
                if(abovefragment.selectbtn!=null){
                    abovefragment.selectbtn.setBackgroundColor(Color.parseColor(colorstr));
                    abovefragment.selectbtn.setTextColor(Color.BLACK);
                }
            }
        }

        for(int j=0;j<clistitemids2.size();j++){
            if(clistitemids2.get(j)==itemid) {
                MatchCategoryBlowFragment fragment = (MatchCategoryBlowFragment) list2.get(j);
                if(fragment.selectbtn!=null){
                    //fragment.selectbtn.setBackgroundColor(Color.parseColor(colorstr));
                    //fragment.selectbtn.setTextColor(Color.BLACK);

                    //fragment.SetText(colorstr);
                    final TextView tv = new TextView(getContext());
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
                    fragment.ll.addView(tv);
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SetTextblowunselected(selectedaboveitemid,itemid,tv);
                        }
                    });

                }

                selectedblowitemid = itemid;
                selectedaboveitemids.add(selectedaboveitemid);
                selectedblowitemids.add(selectedblowitemid);
                Log.e("DotaskOneCategory---->", "selectedblowitemid--->" + selectedaboveitemid+"----"+selectedblowitemid);
            }

        }

    }
    //取消选中下面的item
    public void SetTextblowunselected(int selectedaboveitemid,int itemid,TextView tv){
        for(int j=0;j<clistitemids2.size();j++){
            if(clistitemids2.get(j)==itemid) {
                MatchCategoryBlowFragment fragment = (MatchCategoryBlowFragment) list2.get(j);
                if(fragment.selectbtn!=null){
                    fragment.ll.removeView(tv);
                    //fragment.selectbtn.setBackgroundResource(R.drawable.line_rect_huise);
                    //fragment.selectbtn.setTextColor(Color.BLACK);
                    unselectedblowitemid = itemid;
                    for(int i=0;i<selectedblowitemids.size();i++){
                        if(selectedblowitemids.get(i)==itemid && selectedaboveitemids.get(i)==selectedaboveitemid){
                            selectedaboveitemids.remove(selectedaboveitemids.get(i));
                            selectedblowitemids.remove(selectedblowitemids.get(i));
                            break;
                        }
                    }
                }
                Log.e("DotaskOneCategory---->", "unselectedblowitemid--->" + selectedaboveitemid+"----"+unselectedblowitemid);
            }
        }
    }

    //取消选中下面的item
    public void SetTextblowunselected(int selectedaboveitemid,int itemid){
        for(int j=0;j<clistitemids2.size();j++){
            if(clistitemids2.get(j)==itemid) {
                MatchCategoryBlowFragment fragment = (MatchCategoryBlowFragment) list2.get(j);
                if(fragment.selectbtn!=null){
                    unselectedblowitemid = itemid;
                    for(int i=0;i<selectedblowitemids.size();i++){
                        if(selectedblowitemids.get(i)==itemid && selectedaboveitemids.get(i)==selectedaboveitemid){
                            selectedaboveitemids.remove(selectedaboveitemids.get(i));
                            selectedblowitemids.remove(selectedblowitemids.get(i));
                            break;
                        }
                    }
                }
                Log.e("DotaskOneCategory---->", "unselectedblowitemid--->" + selectedaboveitemid+"----"+unselectedblowitemid);
            }
        }
    }




    @Override
    protected void findViewById(View view) {
        //进度条加载完之后加载数据的view
        fragmentlayout = (LinearLayout)view.findViewById(R.id.above);
        mTabIndicator1 = (MyTabIndicator) view.findViewById(R.id.mTabIndicator1);
        mViewPager1 = (ViewPager) view.findViewById(R.id.mViewPager1);
        mTabIndicator2 = (MyTabIndicator) view.findViewById(R.id.mTabIndicator2);
        mViewPager2 = (ViewPager) view.findViewById(R.id.mViewPager2);
        saveinst = (TextView) view.findViewById(R.id.complete);
        completecon = (TextView) view.findViewById(R.id.completecon);
        completedoc = (TextView) view.findViewById(R.id.completedoc);

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
        initBtn();
    }

    public void initBtn(){
        saveinst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(saveinstrunnable).start();
            }
        });
        completecon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(pararunnable).start();
            }
        });
        completedoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(docrunnable).start();
            }
        });
    }

    private Runnable saveinstrunnable = new Runnable() {
        @Override
        public void run() {
            // TODO: http request.
            String requestUrl = Constant.pairingfileUrl;

            StringBuffer aboveitemidstr = new StringBuffer();
            for(int i=0;i<selectedaboveitemids.size()-1;i++){
                aboveitemidstr.append(selectedaboveitemids.get(i)+",");
            }
            aboveitemidstr.append(selectedaboveitemids.get(selectedaboveitemids.size()-1));

            StringBuffer blowitemidstr = new StringBuffer();
            for(int i=0;i<selectedblowitemids.size()-1;i++){
                blowitemidstr.append(selectedblowitemids.get(i)+",");
            }
            blowitemidstr.append(selectedblowitemids.get(selectedblowitemids.size()-1));
            //要传递的参数
            // int taskId,int docId,int instanceId,int[] aListitemId, int[] bListitemId,String taskType
            tasktype = tasktype.replace("#","%23");
            String params ="?taskId="+taskid+"&docId="+docId+"&instanceId="+instanceid+"&aListitemId="+aboveitemidstr+"&bListitemId="+blowitemidstr+"&taskType="+tasktype+"&userId="+userId;
            Log.e("params---->", "Post方式请求成功，pararunnable--->" + params);
            Log.e("taskid---->", "Post方式请求成功，pararunnable--->" + taskid);
            String result = HttpUtil.requestPost(requestUrl,params);
            Log.e("listview---->", "Post方式请求成功，pararunnable--->" + result);
            //等待请求结束
            JSONObject jsonObject= (JSONObject) JSON.parse(result);
            Integer data = (Integer) jsonObject.getInteger("code");
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

    private Runnable pararunnable = new Runnable() {
        @Override
        public void run() {
            // TODO: http request.
            String requestUrl = Constant.DtOneSortcominstUrl;
            //要传递的参数
            String params ="?taskId="+taskid+"&docId="+docId+"&instanceId="+instanceid+"&userId="+userId;
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

    private Runnable docrunnable = new Runnable() {
        @Override
        public void run() {
            // TODO: http request.
            String requestUrl = Constant.DtOneSortcomdocUrl;
            //要传递的参数
            String params ="?taskId="+taskid+"&docId="+docId+"&userId="+userId;
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

}