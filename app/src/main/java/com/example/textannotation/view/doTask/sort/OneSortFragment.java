package com.example.textannotation.view.doTask.sort;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.*;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.textannotation.Constant.Constant;
import com.example.textannotation.model.ITaskUpload;
import com.example.textannotation.model.doTask.ITaskFragment;
import com.example.textannotation.myapplication.R;
import com.example.textannotation.network.OkHttpUtil;
import com.example.textannotation.util.threadPool.ThreadPool;
import com.example.textannotation.view.doTask.ResolveHttpResponse;
import com.example.textannotation.view.doTask.extractText.DoTaskExtractActivity;
import com.example.textannotation.view.lazyfragment.BaseLazyFragment;
import com.example.textannotation.util.HttpUtil;
import com.example.textannotation.util.MyApplication;
import com.example.textannotation.util.SerializableSortMap;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.*;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 *
 * 单个文本排序界面
 * Created by kongmin
 * 2018.12.29
*/

public class OneSortFragment extends BaseLazyFragment implements ITaskFragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_CONTENT_NUMBER = "content_number";

    //设置加载动画
    private ProgressBar mPb;
    private Handler mHandler = new Handler();
    private String mData;

    private LinearLayout fragmentlayout;

    private int taskid;
    //是做任务页面还是查看做任务页面
    private String typename;
    private int docid;

    private RecyclerView rv;
    private DtSortAdapter adapter;
    private List<DataBean> list = new ArrayList<>();
    private SimpleItemTouchHelperCallback callback;



    private int instanceId;
    private int userid;

    private List<Integer> swap = new ArrayList<>();
    private Map<Integer,Integer> map = new TreeMap<>();

    //做任务要使用的数据
    private int instanceid;
    private int instanceindex;

    //每一个fragment对应的item的数据
    //list
    private ArrayList<Integer> itemids = new ArrayList<>();
    private ArrayList<String> itemcontents = new ArrayList<>();
    private ArrayList<Integer> itemindexs = new ArrayList<>();

    private static int sectionnumber;

    private MyApplication mApplication;
    private int userId;

    public OneSortFragment() {
    }


    //新建Fragment
    public static OneSortFragment newInstance(int sectionNumber) {
        OneSortFragment fragment = new OneSortFragment();
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
                Bundle bundle = getArguments();
                taskid = bundle.getInt("taskid");
                userid = bundle.getInt("userid");
                docid = bundle.getInt("docid");

                final String requestUrl = Constant.DotaskOneSortGetCurrentTaskUrl;
                String paramUrl = "?docId="+docid+"&taskId="+taskid+"&userId="+userid;
                Log.e("mwx",requestUrl + paramUrl);
                OkHttpUtil.sendGetRequest(requestUrl + paramUrl, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        String result = response.body().string();
                        JSONObject instanceItemjson = JSONObject.parseObject(result);
                        JSONObject instanceItem= (JSONObject)instanceItemjson.get("data");

                        itemids.clear();
                        itemcontents.clear();
                        itemindexs.clear();
                        instanceId = instanceItem.getInteger("instid");

                        JSONArray listItem = instanceItem.getJSONArray("itemList");
                        Log.e("mwx",listItem.toJSONString());
                        for (int i = 0 ;i < listItem.size() ; i++) {
                            JSONObject jsonObject = listItem.getJSONObject(i);
                            int id = jsonObject.getInteger("itid");
                            int index = jsonObject.getInteger("itemindex");
                            String content = jsonObject.getString("itemcontent");
                            itemcontents.add(content);
                            itemindexs.add(index);
                            itemids.add(id);
                        }
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
                initData();
                LinearLayoutManager lr=new LinearLayoutManager(getActivity());
                lr.setOrientation(OrientationHelper.VERTICAL);
                rv.setLayoutManager(lr);

                adapter = new DtSortAdapter(list);
                rv.setAdapter(adapter);
                mPb.setVisibility(View.GONE);
                initmotivation();
            }
        });

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

        adapter.setOnItemClickListener(new DtSortAdapter.OnItemClickListener() {
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
    public void saveAnnotationInfo() {
        OkHttpUtil.sendPostRequest(Constant.DotaskOneSortUrl, getRequestBody(), new Callback() {
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

    @Override
    public void getCurrentTaskParagraph() {

    }

    @Override
    public void doNextTask() {
        String requestUrl = Constant.DotaskOneSortGetCurrentTaskUrl;
        String paramUrl = "?docId="+docid+"&taskId="+taskid+"&userId="+userid;
        Log.e("mwx",requestUrl + paramUrl);
        OkHttpUtil.sendGetRequest(requestUrl + paramUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();
                JSONObject instanceItemjson = JSONObject.parseObject(result);
                JSONObject instanceItem= (JSONObject)instanceItemjson.get("data");

                itemids.clear();
                itemcontents.clear();
                itemindexs.clear();
                instanceId = instanceItem.getInteger("instid");

                JSONArray listItem = instanceItem.getJSONArray("itemList");
                Log.e("mwx",listItem.toJSONString());
                for (int i = 0 ;i < listItem.size() ; i++) {
                    JSONObject jsonObject = listItem.getJSONObject(i);
                    int id = jsonObject.getInteger("itid");
                    int index = jsonObject.getInteger("itemindex");
                    String content = jsonObject.getString("itemcontent");
                    itemcontents.add(content);
                    itemindexs.add(index);
                    itemids.add(id);
                }
                updateContent();
            }
        });
    }

    @Override
    public void passCurrentTask() {
        String requestUrl = Constant.DotaskOneSortPassCurrentTaskUrl;
        String paramUrl = "?docId="+docid+"&instanceId="+instanceId+"&taskId="+taskid+"&userId="+userid;
        Log.e("mwx",requestUrl + paramUrl);
        OkHttpUtil.sendGetRequest(requestUrl + paramUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();
                JSONObject instanceItemjson = JSONObject.parseObject(result);
                JSONObject instanceItem= (JSONObject)instanceItemjson.get("data");

                itemids.clear();
                itemcontents.clear();
                itemindexs.clear();
                instanceId = instanceItem.getInteger("instid");

                JSONArray listItem = instanceItem.getJSONArray("itemList");
                Log.e("mwx",listItem.toJSONString());
                for (int i = 0 ;i < listItem.size() ; i++) {
                    JSONObject jsonObject = listItem.getJSONObject(i);
                    int id = jsonObject.getInteger("itid");
                    int index = jsonObject.getInteger("itemindex");
                    String content = jsonObject.getString("itemcontent");
                    itemcontents.add(content);
                    itemindexs.add(index);
                    itemids.add(id);
                }
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
                initData();
                adapter.notifyDataSetChanged();
                ((DtOneSortActivity)getActivity()).hideLoading();
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

        OkHttpUtil.sendPostRequest(Constant.DotaskOneSortPassCompareUrl, getRequestBody(), new Callback() {
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
                .add("taskId",taskid+"")
                .add("instanceId",instanceId+"")
                .add("itemIds", itemIds)
                .add("newIndex",newIndex)
                .add("userId",userid+"")
                .build();

        Log.e("mwx",itemIds);
        Log.e("mwx",newIndex);
        return requestBody;
    }

    public void showInfo(final String title, final String msg){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ((DtOneSortActivity)getActivity()).showNotice(title,msg);
            }
        });
    }

    public void showInfo(final String msg){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ((DtOneSortActivity)getActivity()).uploadInfo(msg);
            }
        });
    }
}

