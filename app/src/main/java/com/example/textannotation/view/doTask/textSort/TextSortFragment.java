package com.example.textannotation.view.doTask.textSort;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.textannotation.myapplication.R;
import com.example.textannotation.presenter.doTask.sort.ITextSortPresenter;
import com.example.textannotation.presenter.doTask.sort.TextSortPresenter;
import com.example.textannotation.util.MyApplication;
import com.example.textannotation.view.doTask.IMenuAction;
import com.example.textannotation.view.doTask.textSort.util.DataBean;
import com.example.textannotation.view.common.BaseLazyFragment;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 *
 * 文本排序界面
 * update by mwx
*/

public class TextSortFragment extends BaseLazyFragment implements IMenuAction,ITextSortView {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_CONTENT_NUMBER = "content_number";

    //设置加载动画
    private ProgressBar mPb;
    private Handler mHandler = new Handler();

    private LinearLayout fragmentlayout;

    private int taskId;
    private int docid;

    private RecyclerView rv;
    private SortAdapter adapter;
    private List<DataBean> list = new ArrayList<>();
    private SimpleItemTouchHelperCallback callback;

    private int instanceId;

    private List<Integer> swap = new ArrayList<>();
    private Map<Integer,Integer> map = new TreeMap<>();

    //每一个fragment对应的item的数据
    //list
    private ArrayList<Integer> itemids = new ArrayList<>();
    private ArrayList<String> itemcontents = new ArrayList<>();
    private ArrayList<Integer> itemindexs = new ArrayList<>();


    private MyApplication mApplication;
    private int userId;

    private ITextSortPresenter iTextSortPresenter;
    public TextSortFragment() {
    }


    //新建Fragment
    public static TextSortFragment newInstance(int sectionNumber) {
        TextSortFragment fragment = new TextSortFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        //args.putInt(ARG_CONTENT_NUMBER, contentid);
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
        taskId = bundle.getInt("taskid");
        userId = bundle.getInt("userid");
        iTextSortPresenter = new TextSortPresenter(this);
        iTextSortPresenter.loadDataAndInitView(taskId,docid,userId);
    }

    private void initData() {
        list.clear();
        //String[] arrays=getResources().getStringArray(R.array.news);
        for(int i=0;i<itemcontents.size();i++){
            DataBean bean=new DataBean();
            bean.setText(itemcontents.get(i));
            bean.setItemid(itemids.get(i));
            list.add(bean);
        }
    }

    private void initmotivation() {

        adapter.setOnItemClickListener(new SortAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(getContext(),"点击了"+position+"行 , itemId "+list.get(position).getItemid(),Toast.LENGTH_SHORT).show();
            }
        });
        //先实例化Callback
        callback = new SimpleItemTouchHelperCallback(adapter);
        //用Callback构造ItemtouchHelper
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        //调用ItemTouchHelper的attachToRecyclerView方法建立联系
        touchHelper.attachToRecyclerView(rv);
}


    @Override
    protected View initRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "initRootView");
        return inflater.inflate(R.layout.fragment_one_sort,container,false);
    }

    @Override
    protected void findViewById(View view) {
        fragmentlayout = (LinearLayout)view.findViewById(R.id.section_label);

        rv= (RecyclerView) view.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        // 获取整个应用的Application对象
        // 在不同的Activity中获取的对象是同一个
        mApplication = (MyApplication)getActivity().getApplication();
        userId = mApplication.getLoginUserId();
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
    public void getLastTask() {
        iTextSortPresenter.getLastTask(taskId,instanceId,userId);
    }

    @Override
    public void getNextTask() {
        iTextSortPresenter.getNextTask(taskId,instanceId,userId);
    }

    @Override
    public void saveAnnotationInfo() {

        iTextSortPresenter.saveAnnotationInfo(getRequestBody());
//        OkHttpUtil.sendPostRequest(Constant.DotaskOneSortUrl, getRequestBody(), new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String result = response.body().string();
//                showInfo(result);
//            }
//        });
    }


    private RequestBody getRequestBody(){
        Collections.sort(itemids);
        //排完序之后的新的顺序
        for (int i=0;i<adapter.getItemCount();i++){
            List<DataBean> itemlist = adapter.getItemList();
            map.put(itemlist.get(i).getItemid(),i+1);
        }
        swap.clear();
        for(Integer key:map.keySet()){
            swap.add(map.get(key));
        }

        String itemIds = new Gson().toJson(itemids).replace("[","").replace("]","");
        String newIndex = new Gson().toJson(swap).replace("[","").replace("]","");

        RequestBody requestBody = new FormBody.Builder()
                .add("docId", docid+"")
                .add("taskId",taskId+"")
                .add("instanceId",instanceId+"")
                .add("itemIds", itemIds)
                .add("newIndex",newIndex)
                .add("userId",userId+"")
                .build();

        Log.e("mwx",itemIds);
        Log.e("mwx",newIndex);
        return requestBody;
    }

    public void showInfo(final String msg){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ((TextSortActivity)getActivity()).uploadInfo(msg);
            }
        });
    }

    @Override
    public void initView(final int pid, final JSONArray listItem) {
        itemids.clear();
        itemcontents.clear();
        itemindexs.clear();
        instanceId = pid;
        for (int i = 0 ;i < listItem.size() ; i++) {
            JSONObject jsonObject = listItem.getJSONObject(i);
            int id = jsonObject.getInteger("itid");
            int index = jsonObject.getInteger("itemindex");
            String content = jsonObject.getString("itemcontent");
            itemcontents.add(content);
            itemindexs.add(index);
            itemids.add(id);
        }

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                initData();
                LinearLayoutManager lr=new LinearLayoutManager(getActivity());
                lr.setOrientation(OrientationHelper.VERTICAL);
                rv.setLayoutManager(lr);
                adapter = new SortAdapter(list);
                rv.setAdapter(adapter);
                mPb.setVisibility(View.GONE);
                initmotivation();
            }
        });
    }

    @Override
    public void updateContent(int pid, JSONArray listItem) {
        itemids.clear();
        itemcontents.clear();
        itemindexs.clear();
        instanceId = pid;
        for (int i = 0 ;i < listItem.size() ; i++) {
            JSONObject jsonObject = listItem.getJSONObject(i);
            int id = jsonObject.getInteger("itid");
            int index = jsonObject.getInteger("itemindex");
            String content = jsonObject.getString("itemcontent");
            itemcontents.add(content);
            itemindexs.add(index);
            itemids.add(id);
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                initData();
                adapter.notifyDataSetChanged();
                ((TextSortActivity)getActivity()).hideLoading();
            }
        });

    }

    @Override
    public void showExceptionInfo(final String msg) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    ((TextSortActivity)getActivity()).showNotice("",msg);
                    ((TextSortActivity)getActivity()).hideLoading();
                }
            });
    }

    @Override
    public void showSubmitInfo(final String msg) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ((TextSortActivity)getActivity()).uploadInfo(msg);
                ((TextSortActivity)getActivity()).hideLoading();
            }
        });
    }

    @Override
    public void showSimpleInfo(final String msg) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ((TextSortActivity)getActivity()).showNotice("",msg);
                ((TextSortActivity)getActivity()).hideLoading();
            }
        });
    }

    @Override
    public void showCompletedInfo(String msg) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ((TextSortActivity)getActivity()).showCompletedInfo();
                ((TextSortActivity)getActivity()).hideLoading();
            }
        });
    }
}

