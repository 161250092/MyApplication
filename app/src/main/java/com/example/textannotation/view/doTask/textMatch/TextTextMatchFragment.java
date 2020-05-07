package com.example.textannotation.view.doTask.textMatch;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.textannotation.myapplication.R;
import com.example.textannotation.presenter.doTask.match.ITextMatchPresenter;
import com.example.textannotation.presenter.doTask.match.TextMatchPresenter;
import com.example.textannotation.view.common.MyTabIndicator;
import com.example.textannotation.view.doTask.IMenuAction;
import com.example.textannotation.view.doTask.textMatch.section.TextMatchAboveAdapter;
import com.example.textannotation.view.doTask.textMatch.section.TextMatchAboveFragment;
import com.example.textannotation.view.doTask.textMatch.section.TextMatchBelowAdapter;
import com.example.textannotation.view.doTask.textMatch.section.TextMatchBelowFragment;
import com.example.textannotation.view.common.BaseLazyFragment;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/*
 * 文本配对
 * update by mwx 2020.2.16
 */

public class TextTextMatchFragment extends BaseLazyFragment implements ITextMatch,IMenuAction,ITextMatchView {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_CONTENT_NUMBER = "content_number";


    private TextMatchAboveAdapter mMatchCategoryAdapter1;
    private TextMatchBelowAdapter mMatchCategoryAdapter2;

    private ViewPager mViewPager1;
    private ViewPager mViewPager2;
    //导航栏
    private MyTabIndicator mTabIndicator1;
    private MyTabIndicator mTabIndicator2;
    //初始化数据
    public List<TextMatchAboveFragment> list1 = new ArrayList<>();
    private List<String> titles1 = new ArrayList<>();

    private List<TextMatchBelowFragment> list2 = new ArrayList<>();
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

    private int userId;

    private ITextMatchPresenter iTextMatchPresenter;

    public TextTextMatchFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */

    //新建Fragment
    public static TextTextMatchFragment newInstance(int sectionNumber) {
        TextTextMatchFragment fragment = new TextTextMatchFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    //设置加载动画



    @Override
    public void loadDataStart() {
        Bundle bundle =getArguments();
        taskid = bundle.getInt("taskid");
        userId = bundle.getInt("userid");
        docId = bundle.getInt("docid");
        tasktype = bundle.getString("tasktype");

        iTextMatchPresenter = new TextMatchPresenter(this);
        iTextMatchPresenter.loadDataAndInitView(taskid,0,userId);



        Log.d(TAG, "loadDataStart");
        // 模拟请求数据
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mLoadDataFinished = true;
                if (mViewInflateFinished) {
                    fragmentlayout.setVisibility(View.VISIBLE);
                }
            }
        }, 200);

    }



    @Override
    protected View initRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "initRootView");
        return inflater.inflate(R.layout.fragment_match_category,container,false);

    }


    //选择上面的一个item
    public int getAboveFragmentIndex(){
        int fragmentIndex = mViewPager1.getCurrentItem();
        return fragmentIndex;
    }

    @Override
    public int getAboveItemId() {
        int fragmentIndex = mViewPager1.getCurrentItem();
        TextMatchAboveFragment textMatchAboveFragment = (TextMatchAboveFragment)mMatchCategoryAdapter1.getItem(fragmentIndex);
        return textMatchAboveFragment.getListitemid();
    }
    @Override
    public int getBelowItemId() {
        int fragmentIndex = mViewPager2.getCurrentItem();
        TextMatchBelowFragment textMatchBelowFragment = (TextMatchBelowFragment)mMatchCategoryAdapter2.getItem(fragmentIndex);
        return textMatchBelowFragment.getListitemid();
    }


    @Override
    public void addMatchInfoInAboveFragment(int belowIndex){
        int fragmentIndex = mViewPager1.getCurrentItem();
        TextMatchAboveFragment textMatchAboveFragment = (TextMatchAboveFragment)mMatchCategoryAdapter1.getItem(fragmentIndex);
        textMatchAboveFragment.updateAddMatchInfo(belowIndex);

    }

    @Override
    public void minusMatchInfoInAboveFragment(int belowIndex){
        Log.e("mwx","minus belowIndex  " + belowIndex);
        int fragmentIndex = mViewPager1.getCurrentItem();
        TextMatchAboveFragment textMatchAboveFragment = (TextMatchAboveFragment)mMatchCategoryAdapter1.getItem(fragmentIndex);
        textMatchAboveFragment.updateMinusMatchInfo(belowIndex);
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
                TextMatchBelowFragment textMatchBelowFragment = (TextMatchBelowFragment)mMatchCategoryAdapter2.getItem(fragmentIndex);
                textMatchBelowFragment.showCancelPairBtn();
                return;
            }
        }

        int fragmentIndex = mViewPager2.getCurrentItem();
        TextMatchBelowFragment textMatchBelowFragment = (TextMatchBelowFragment)mMatchCategoryAdapter2.getItem(fragmentIndex);
        textMatchBelowFragment.showPairBtn();
    }


    public int getBelowFragmentIndex(){
        return mViewPager2.getCurrentItem();
    }

    public int getBelowFragmentItemId(){
        int fragmentIndex = mViewPager2.getCurrentItem();
        TextMatchBelowFragment textMatchBelowFragment = (TextMatchBelowFragment)mMatchCategoryAdapter2.getItem(fragmentIndex);
        return textMatchBelowFragment.getListitemid();
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


    private HashMap<String,Object> getRequestParam(){
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("taskId",taskid);
        hashMap.put("docId",docId);
        hashMap.put("instanceId",instanceid);
        hashMap.put("aListitemId",selectedAboveItemIds);
        hashMap.put("blowitemidstr",selectedBelowItemIds);
        hashMap.put("taskType",tasktype);
        hashMap.put("userId",userId);
        return hashMap;
    }


    private RequestBody getRequestBody(){
        Log.e("mwx","execute this");

        String item1Labels = new Gson().toJson(selectedAboveItemIds).replace("[","").replace("]","");
        String item2Labels = new Gson().toJson(selectedBelowItemIds).replace("[","").replace("]","");

        Log.e("item1Labels",item1Labels);
        Log.e("item2Labels",item2Labels);
        tasktype = tasktype.replace("#","%23");
        Log.e("tasktype",tasktype);

        Log.e("mwx",item1Labels+"&"+item2Labels+"&"+taskid+"&"+"instanceId="+instanceid+"&"+docId+"&"+tasktype+"&"+userId);

        RequestBody requestBody = new FormBody.Builder()
                .add("taskId",taskid+"")
                .add("docId",docId+"")
                .add("instanceId",instanceid+"")
                .add("aListitemId",item1Labels)
                .add("bListitemId",item2Labels)
                .add("taskType",tasktype)
                .add("userId",userId+"")
                .build();
        return requestBody;
    }

    @Override
    public void getLastTask() {
        iTextMatchPresenter.getLastTask(taskid,instanceid,userId);
    }

    @Override
    public void getNextTask() {
        iTextMatchPresenter.getNextTask(taskid,instanceid,userId);
    }

    @Override
    public void saveAnnotationInfo() {
        iTextMatchPresenter.saveAnnotationInfo(getRequestBody());
    }





    private void updateTwoViewPages(){
        try {
            Thread.sleep(500);
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
                    TextMatchAboveFragment m1 = TextMatchAboveFragment.newInstance(i);
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
                    TextMatchBelowFragment m1 = TextMatchBelowFragment.newInstance(i);
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


                mMatchCategoryAdapter1 = new TextMatchAboveAdapter(getChildFragmentManager(),list1);
                mMatchCategoryAdapter2 = new TextMatchBelowAdapter(getChildFragmentManager(),list2);

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
                ((TextMatchActivity)getActivity()).hideLoading();
            }
        });
    }


    private void initData(){
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
    }

    @Override
    public void initView(JSONObject instanceItem) {
        initData();
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
        initTwoViewPagers();
    }

    public void initTwoViewPagers(){

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<clistitemids1.size();i++){
                    //导航栏加标题
                    titles1.add("第"+clitemindexs1.get(i)+"部分");
                    TextMatchAboveFragment m1 = TextMatchAboveFragment.newInstance(i);
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
                    TextMatchBelowFragment m1 = TextMatchBelowFragment.newInstance(i);
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


                mMatchCategoryAdapter1 = new TextMatchAboveAdapter(getChildFragmentManager(),list1);
                mMatchCategoryAdapter2 = new TextMatchBelowAdapter(getChildFragmentManager(),list2);

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
    public void updateContent(JSONObject data) {
        initData();
        instanceid = data.getInteger("instid");
        JSONArray listItems = data.getJSONArray("listitems");

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
        updateTwoViewPages();
    }

    @Override
    public void showExceptionInfo(final String msg) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ((TextMatchActivity)getActivity()).showNotice("",msg);
            }
        });
    }

    @Override
    public void showSubmitInfo(final String msg) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ((TextMatchActivity)getActivity()).uploadInfo(msg);
            }
        });
    }

    @Override
    public void showSimpleInfo(final String msg) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ((TextMatchActivity)getActivity()).showNotice("",msg);
            }
        });
    }

    @Override
    public void showCompletedInfo(String msg) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ((TextMatchActivity)getActivity()).showCompletedInfo();
            }
        });
    }
}