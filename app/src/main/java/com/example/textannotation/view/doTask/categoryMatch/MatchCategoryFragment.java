package com.example.textannotation.view.doTask.categoryMatch;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.*;
import android.widget.*;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.textannotation.Constant.Constant;

import android.support.v4.view.ViewPager;

import com.example.textannotation.model.doTask.ITaskFragment;
import com.example.textannotation.myapplication.R;
import com.example.textannotation.network.OkHttpUtil;
import com.example.textannotation.view.lazyfragment.BaseLazyFragment;
import com.example.textannotation.util.MyApplication;
import com.example.textannotation.view.commonVIew.MyTabIndicator;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/*
 * 文本配对
 * update by mwx 2020.2.16
 */

public class MatchCategoryFragment extends BaseLazyFragment implements IMatchCategory,ITaskFragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_CONTENT_NUMBER = "content_number";


    private MatchCategoryAboveAdapter mMatchCategoryAdapter1;
    private MatchCategoryBelowAdapter mMatchCategoryAdapter2;

    private ViewPager mViewPager1;
    private ViewPager mViewPager2;
    //导航栏
    private MyTabIndicator mTabIndicator1;
    private MyTabIndicator mTabIndicator2;
    //初始化数据
    public List<MatchCategoryAboveFragment> list1 = new ArrayList<>();
    private List<String> titles1 = new ArrayList<>();

    private List<MatchCategoryBelowFragment> list2 = new ArrayList<>();
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



    //任务ID
    private int taskid;
    //是做任务页面还是查看做任务页面
    private String typename;
    private int docId;

    private static int sectionnumber;


    private ArrayList<Integer> selectedAboveItemIds = new ArrayList<Integer>();
    private ArrayList<Integer> selectedBelowItemIds = new ArrayList<Integer>();

    private String tasktype;


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
                    taskid = bundle.getInt("taskid");
                    userId = bundle.getInt("userid");
                    docId = bundle.getInt("docid");
                    tasktype = bundle.getString("tasktype");

                    final String requestUrl = Constant.getPairCurrentTask;
                    String paramUrl = "?docId="+docId+"&taskId="+taskid+"&userId="+userId;

                    Log.e("mwz",requestUrl+paramUrl);
                    OkHttpUtil.sendGetRequest(requestUrl + paramUrl, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String result = response.body().string();
                            Log.e("mwx",result);

                            JSONObject data = JSONObject.parseObject(result);
                            JSONObject instanceItem = data.getJSONObject("instanceItem");

                            instanceid = instanceItem.getInteger("instid");
                            JSONArray listItems = instanceItem.getJSONArray("listitems");

                            for (int i = 0; i < listItems.size() ; i++){
                                JSONObject job = listItems.getJSONObject(i);

                                int ltid = job.getInteger("ltid");
                                String litemcontent = job.getString("litemcontent");
                                int litemindex = job.getInteger("litemindex");
                                //决定是属于哪一个部分 1是上部，2是下部
                                int listIndex = job.getInteger("listIndex");

                                if (listIndex == 1){
                                    clistitemids1.add(ltid);
                                    clitemcontents1.add(litemcontent);
                                    clistindexs1.add(listIndex);
                                    clitemindexs1.add(litemindex);
                                }

                                if (listIndex == 2){
                                    clistitemids2.add(ltid);
                                    clitemcontents2.add(litemcontent);
                                    clistindexs2.add(listIndex);
                                    clitemindexs2.add(litemindex);
                                }
                            }

                            initView();
                        }
                    });
                }
            }
        }, 200);

    }

     public void initView(){

        mHandler.post(new Runnable() {
            @Override
            public void run() {
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

                    bundle.putInt("userid", userId);
                    list1.get(i).setArguments(bundle);
                }

                for(int i=0;i<clistitemids2.size();i++){
                    int itemnum = clitemindexs2.get(i);
                    titles2.add("第"+itemnum+"部分");
                    MatchCategoryBelowFragment m1 = MatchCategoryBelowFragment.newInstance(i);
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

                    bundle.putInt("userid", userId);
                    list2.get(i).setArguments(bundle);
                }


                mMatchCategoryAdapter1 = new MatchCategoryAboveAdapter(getChildFragmentManager(),list1);
                mMatchCategoryAdapter2 = new MatchCategoryBelowAdapter(getChildFragmentManager(),list2);

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
                        updateBtnStatus();
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
                        // Log.e("mwx","选择了：" + position + "index: "+getBelowFragmentIndex() + "itemId: "+ getBelowFragmentItemId());
                        updateBtnStatus();
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
                mPb.setVisibility(View.GONE);
            }
        });

    }

    @Override
    protected View initRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "initRootView");
        return inflater.inflate(R.layout.fragment_match_category,container,false);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
    //选择上面的一个item

    public int getAboveFragmentIndex(){
        int fragmentIndex = mViewPager1.getCurrentItem();
        return fragmentIndex;
    }

    @Override
    public int getAboveItemId() {
        int fragmentIndex = mViewPager1.getCurrentItem();
        MatchCategoryAboveFragment matchCategoryAboveFragment = (MatchCategoryAboveFragment)mMatchCategoryAdapter1.getItem(fragmentIndex);
        return matchCategoryAboveFragment.getListitemid();
    }
    @Override
    public int getBelowItemId() {
        int fragmentIndex = mViewPager2.getCurrentItem();
        MatchCategoryBelowFragment matchCategoryBelowFragment = (MatchCategoryBelowFragment)mMatchCategoryAdapter2.getItem(fragmentIndex);
        return matchCategoryBelowFragment.getListitemid();
    }


    @Override
    public void addMatchInfoInAboveFragment(int belowIndex){
        int fragmentIndex = mViewPager1.getCurrentItem();
        MatchCategoryAboveFragment matchCategoryAboveFragment = (MatchCategoryAboveFragment)mMatchCategoryAdapter1.getItem(fragmentIndex);
        matchCategoryAboveFragment.updateAddMatchInfo(belowIndex);

    }

    @Override
    public void minusMatchInfoInAboveFragment(int belowIndex){
        Log.e("mwx","minus belowIndex  " + belowIndex);
        int fragmentIndex = mViewPager1.getCurrentItem();
        MatchCategoryAboveFragment matchCategoryAboveFragment = (MatchCategoryAboveFragment)mMatchCategoryAdapter1.getItem(fragmentIndex);
        matchCategoryAboveFragment.updateMinusMatchInfo(belowIndex);
    }

    @Override
    public void addMatchPair(int aboveItemId, int belowItemId) {
        selectedAboveItemIds.add(aboveItemId);
        selectedBelowItemIds.add(belowItemId);
    }

    @Override
    public void deleteMatchPair(int aboveItemId, int belowItemId) {
        for (int i = 0; i < selectedAboveItemIds.size() ; i++){
            if (selectedAboveItemIds.get(i) == aboveItemId && selectedBelowItemIds.get(i) == belowItemId){
                selectedAboveItemIds.remove(i);
                selectedBelowItemIds.remove(i);
                break;
            }
        }

    }

    @Override
    public void updateBtnStatus() {
        int aboveItemId = getAboveItemId();
        int belowItemId = getBelowItemId();

        for (int i = 0; i < selectedAboveItemIds.size(); i++){
            if ( selectedAboveItemIds.get(i) == aboveItemId && selectedBelowItemIds.get(i) == belowItemId){
                int fragmentIndex = mViewPager2.getCurrentItem();
                MatchCategoryBelowFragment matchCategoryBelowFragment = (MatchCategoryBelowFragment)mMatchCategoryAdapter2.getItem(fragmentIndex);
                matchCategoryBelowFragment.showCancelPairBtn();
                return;
            }
        }

        int fragmentIndex = mViewPager2.getCurrentItem();
        MatchCategoryBelowFragment matchCategoryBelowFragment = (MatchCategoryBelowFragment)mMatchCategoryAdapter2.getItem(fragmentIndex);
        matchCategoryBelowFragment.showPairBtn();
    }


    public int getBelowFragmentIndex(){
        return mViewPager2.getCurrentItem();
    }

    public int getBelowFragmentItemId(){
        int fragmentIndex = mViewPager2.getCurrentItem();
        MatchCategoryBelowFragment matchCategoryBelowFragment = (MatchCategoryBelowFragment)mMatchCategoryAdapter2.getItem(fragmentIndex);
        return matchCategoryBelowFragment.getListitemid();
    }


    @Override
    protected void findViewById(View view) {
        //进度条加载完之后加载数据的view
        fragmentlayout = (LinearLayout)view.findViewById(R.id.above);
        mTabIndicator1 = (MyTabIndicator) view.findViewById(R.id.mTabIndicator1);
        mViewPager1 = (ViewPager) view.findViewById(R.id.mViewPager1);
        mTabIndicator2 = (MyTabIndicator) view.findViewById(R.id.mTabIndicator2);
        mViewPager2 = (ViewPager) view.findViewById(R.id.mViewPager2);


        mPb = view.findViewById(R.id.pb);
        if (mLoadDataFinished) {
            // 一般情况下这时候数据请求都还没完成, 所以不会进这个if
            fragmentlayout.setVisibility(View.VISIBLE);
            //mTextView.setText(mData);
            mPb.setVisibility(View.GONE);
        }
        Log.e("mwx","fragment  findViewById");
    }



    private RequestBody getRequestBody(){
        Log.e("mwx","execute this");

        String item1Labels = new Gson().toJson(selectedAboveItemIds).replace("[","").replace("]","");
        String item2Labels = new Gson().toJson(selectedBelowItemIds).replace("[","").replace("]","");

        Log.e("item1Labels",item1Labels);
        Log.e("item2Labels",item2Labels);
        tasktype = tasktype.replace("#","%23");
        Log.e("tasktype",tasktype);

        Log.e("mwx",item1Labels+"&"+item2Labels+"&"+taskid+"&"+docId+"&"+tasktype+"&"+userId);

        RequestBody requestBody = new FormBody.Builder()
                .add("taskId",taskid+"")
                .add("docId",docId+"")
                .add("instanceId",instanceid+"")
                .add("aListitemId",item1Labels)
                .add("blowitemidstr",item2Labels)
                .add("taskType",tasktype)
                .add("userId",userId+"")
                .build();
        return requestBody;
    }

    @Override
    public void saveAnnotationInfo() {

        String requestUrl = Constant.pairingfileUrl;

        OkHttpUtil.sendPostRequest(requestUrl, getRequestBody(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String temp = response.body().string();
                Log.e("http response",temp);
                showInfo(temp);
            }
        });

    }
    public void showInfo(final String msg){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ((MatchCategoryActivity)getActivity()).uploadInfo(msg);
            }
        });
    }

    @Override
    public void getCurrentTaskParagraph() {

    }

    @Override
    public void doNextTask() {
        clistitemids1.clear();
        clitemcontents1.clear();
        clistindexs1.clear();
        clitemindexs1.clear();

        clistitemids2.clear();
        clitemcontents2.clear();
        clistindexs2.clear();
        clitemindexs2.clear();

        selectedAboveItemIds.clear(); ;
        selectedBelowItemIds.clear();
        final String requestUrl = Constant.getPairCurrentTask;
        String paramUrl = "?docId="+docId+"&taskId="+taskid+"&userId="+userId;

        Log.e("mwz",requestUrl+paramUrl);
        OkHttpUtil.sendGetRequest(requestUrl + paramUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.e("mwx",result);

                JSONObject data = JSONObject.parseObject(result);
                JSONObject instanceItem = data.getJSONObject("instanceItem");

                instanceid = instanceItem.getInteger("instid");
                JSONArray listItems = instanceItem.getJSONArray("listitems");

                for (int i = 0; i < listItems.size() ; i++){
                    JSONObject job = listItems.getJSONObject(i);

                    int ltid = job.getInteger("ltid");
                    String litemcontent = job.getString("litemcontent");
                    int litemindex = job.getInteger("litemindex");
                    //决定是属于哪一个部分 1是上部，2是下部
                    int listIndex = job.getInteger("listIndex");

                    if (listIndex == 1){
                        clistitemids1.add(ltid);
                        clitemcontents1.add(litemcontent);
                        clistindexs1.add(listIndex);
                        clitemindexs1.add(litemindex);
                    }

                    if (listIndex == 2){
                        clistitemids2.add(ltid);
                        clitemcontents2.add(litemcontent);
                        clistindexs2.add(listIndex);
                        clitemindexs2.add(litemindex);
                    }
                }

                updateView();
            }
        });

    }

    private void updateView(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                list1.clear();
                list2.clear();
                titles1.clear();
                titles2.clear();

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

                    bundle.putInt("userid", userId);
                    list1.get(i).setArguments(bundle);
                }

                for(int i=0;i<clistitemids2.size();i++){
                    int itemnum = clitemindexs2.get(i);
                    titles2.add("第"+itemnum+"部分");
                    MatchCategoryBelowFragment m1 = MatchCategoryBelowFragment.newInstance(i);
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

                    bundle.putInt("userid", userId);
                    list2.get(i).setArguments(bundle);
                }


                mMatchCategoryAdapter1 = new MatchCategoryAboveAdapter(getChildFragmentManager(),list1);
                mMatchCategoryAdapter2 = new MatchCategoryBelowAdapter(getChildFragmentManager(),list2);

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
                        // Log.e("mwx","选择了：" + position + " index: "+getAboveFragmentIndex() + " itemId: "+ getAboveItemId());
                        updateBtnStatus();
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
                        // Log.e("mwx","选择了：" + position + "index: "+getBelowFragmentIndex() + "itemId: "+ getBelowFragmentItemId());
                        updateBtnStatus();
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });

                mPb.setVisibility(View.GONE);
                ((MatchCategoryActivity)getActivity()).hideLoading();
            }
        });


    }

    @Override
    public void passCurrentTask() {
        clistitemids1.clear();
        clitemcontents1.clear();
        clistindexs1.clear();
        clitemindexs1.clear();

        clistitemids2.clear();
        clitemcontents2.clear();
        clistindexs2.clear();
        clitemindexs2.clear();

        selectedAboveItemIds.clear(); ;
        selectedBelowItemIds.clear();
        final String requestUrl = Constant.passPairCurrentTask;
        String paramUrl = "?docId="+docId+"&instanceId="+instanceid+"&taskId="+taskid+"&userId="+userId;

        Log.e("mwz",requestUrl+paramUrl);
        OkHttpUtil.sendGetRequest(requestUrl + paramUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.e("mwx",result);

                JSONObject data = JSONObject.parseObject(result);
                JSONObject instanceItem = data.getJSONObject("instanceItem");

                instanceid = instanceItem.getInteger("instid");
                JSONArray listItems = instanceItem.getJSONArray("listitems");

                for (int i = 0; i < listItems.size() ; i++){
                    JSONObject job = listItems.getJSONObject(i);

                    int ltid = job.getInteger("ltid");
                    String litemcontent = job.getString("litemcontent");
                    int litemindex = job.getInteger("litemindex");
                    //决定是属于哪一个部分 1是上部，2是下部
                    int listIndex = job.getInteger("listIndex");

                    if (listIndex == 1){
                        clistitemids1.add(ltid);
                        clitemcontents1.add(litemcontent);
                        clistindexs1.add(listIndex);
                        clitemindexs1.add(litemindex);
                    }

                    if (listIndex == 2){
                        clistitemids2.add(ltid);
                        clitemcontents2.add(litemcontent);
                        clistindexs2.add(listIndex);
                        clitemindexs2.add(litemindex);
                    }
                }

                updateView();
            }
        });
    }

    @Override
    public void submitErrors(String text) {
        String requestUrl = Constant.submitErrorUrl;
        String params = "?docId="+docId+"&paraId="+instanceid+"&msg="+text+"&taskId="+taskid+"&userId="+userId;
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


        OkHttpUtil.sendPostRequest(Constant.comparePairAnnotation, getRequestBody(), new Callback() {
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
                ((MatchCategoryActivity)getActivity()).showNotice(title,msg);
            }
        });
    }



}