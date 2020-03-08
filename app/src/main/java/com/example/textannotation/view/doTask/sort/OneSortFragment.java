package com.example.textannotation.view.doTask.sort;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.*;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.textannotation.Constant.Constant;
import com.example.textannotation.model.ITaskUpload;
import com.example.textannotation.myapplication.R;
import com.example.textannotation.util.threadPool.ThreadPool;
import com.example.textannotation.view.doTask.ResolveHttpResponse;
import com.example.textannotation.view.lazyfragment.BaseLazyFragment;
import com.example.textannotation.util.HttpUtil;
import com.example.textannotation.util.MyApplication;
import com.example.textannotation.util.SerializableSortMap;

import java.util.*;
/**
 *
 * 单个文本排序界面
 * Created by kongmin
 * 2018.12.29
*/

public class OneSortFragment extends BaseLazyFragment implements ITaskUpload {

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
    private List<DataBean> list;
    private SimpleItemTouchHelperCallback callback;


    private String issorted;
    //已经排好序的做任务的结果
    private ArrayList<Integer> sortedindex = new ArrayList<Integer>();
    private ArrayList<Integer> sorteditemId = new ArrayList<Integer>();

    private SerializableSortMap sortMap;

    private Map<Integer,String> sortmap = new HashMap<>();

    private Map<Integer,Integer> idindex = new TreeMap<>();



    private int docId;
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

                    //获取传过来的数据
                    Bundle bundle = getArguments();
                    sectionnumber = bundle.getInt("fragmentindex");
                    taskid = bundle.getInt("taskid");
                    userid = bundle.getInt("userid");
                    docid = bundle.getInt("docid");
                    typename = bundle.getString("type");
                    instanceid = bundle.getInt("instanceid"+sectionnumber);
                    instanceindex = bundle.getInt("instanceindex"+sectionnumber);
                    Log.e("MatchCategory---->", "GET方式请求成功，sectionnumber--->" + sectionnumber);

                    itemids =  bundle.getIntegerArrayList("itemidp"+sectionnumber);
                    itemcontents = bundle.getStringArrayList("itemconp"+sectionnumber);
                    itemindexs = bundle.getIntegerArrayList("itemindexp"+sectionnumber);

                    Log.e("MatchCategory---->", "GET方式请求成功，instanceid--->" + instanceid);
                    Log.e("MatchCategory---->", "GET方式请求成功，instanceindex--->" + instanceindex);
                    Log.e("MatchCategory---->", "GET方式请求成功，itemids--->" + itemids);
                    Log.e("MatchCategory---->", "GET方式请求成功，itemcontents--->" + itemcontents);
                    Log.e("MatchCategory---->", "GET方式请求成功，itemindexs--->" + itemindexs);

                    for(int i=0;i<itemids.size();i++){
                        sortmap.put(itemids.get(i),itemcontents.get(i));
                    }


                    issorted = bundle.getString("issorted"+sectionnumber);
                    if(issorted.equals("true")){
                        //todo 存在的itemId才加进去
                        sortedindex = bundle.getIntegerArrayList("sortedindex"+sectionnumber);
                        sorteditemId = bundle.getIntegerArrayList("sorteditemId"+sectionnumber);
                        for(int i=0;i<sortedindex.size();i++){
                            if(sortmap.containsKey(sorteditemId.get(i))) {
                                idindex.put(sortedindex.get(i), sorteditemId.get(i));
                                Log.e("DotaskExtract---->", "GET方式请求成功，issorted.equals(true)--->" + sortedindex.get(i) + "-----" + sorteditemId.get(i));
                            }
                        }
                        initsortedData();
                        Log.e("DotaskExtract---->", "GET方式请求成功，issorted.equals(\"true\")--->" + "标注了排序的");
                    }else{
                        initData();
                    }

                    adapter=new DtSortAdapter(list);
                    rv.setAdapter(adapter);
                    mPb.setVisibility(View.GONE);
                    initmotivation();
                }
            }
        }, 300);

    }

    private void initData() {
        list = new ArrayList<>();
        //String[] arrays=getResources().getStringArray(R.array.news);
        for(int i=0;i<itemcontents.size();i++){
            DataBean bean=new DataBean();
            bean.setText(itemcontents.get(i));
            //bean.setItemid(itemindexs.get(i));
            bean.setItemid(itemids.get(i));
            list.add(bean);
        }
    }
    public void initsortedData(){
        list = new ArrayList<>();
        for(Integer sortedindex:idindex.keySet()){
            int sorteditemId = idindex.get(sortedindex);
            DataBean bean=new DataBean();
            bean.setItemid(sorteditemId);
            bean.setText(sortmap.get(sorteditemId));
            //bean.setItemid(itemindexs.get(i));
            list.add(bean);
            Log.e("DotaskExtract---->", "GET方式请求成功，sorteditemId+itemcontent--->" + sorteditemId+sortmap.get(sorteditemId));
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

    private Runnable saveinst = new Runnable() {
        @Override
        public void run() {
            String requestUrl = Constant.DotaskOneSortUrl;
            //要传递的参数
            Collections.sort(itemids);
            StringBuffer itemidstr = new StringBuffer();
            for(int i=0;i<itemids.size()-1;i++){
                itemidstr.append(itemids.get(i)+",");
            }
            itemidstr.append(itemids.get(itemids.size()-1));
            //排完序之后的新的顺序

            for (int i=0;i<adapter.getItemCount();i++){
                List<DataBean> itemlist = adapter.getItemList();
                map.put(itemlist.get(i).getItemid(),i);
                Log.e("params---->", "Post方式请求成功，itemlist.get(i).getItemid()--->" + itemlist.get(i).getItemid());
            }
            swap.clear();
            for(Integer key:map.keySet()){
                swap.add(map.get(key));
                Log.e("params---->", "Post方式请求成功，itemlist.get(i).getItemid()--->" + key+"-----"+map.get(key));
            }
            StringBuffer itemindexstr = new StringBuffer();
            for(int i=0;i<swap.size()-1;i++){
                itemindexstr.append(swap.get(i)+",");
            }
            itemindexstr.append(swap.get(swap.size()-1));

            String params ="?taskId="+taskid+"&docId="+docid+"&instanceId="+instanceid+"&itemIds="+itemidstr+"&newIndex="+itemindexstr+"&userId="+userId;
            Log.e("params---->", "Post方式请求成功，params--->" + params);
            String result = HttpUtil.requestPost(requestUrl,params);
            Log.e("listview---->", "Post方式请求成功，result--->" + result);
            ResolveHttpResponse.showHttpResponse(result,getContext());
        }
    };


    private Runnable completeinst = new Runnable() {
        @Override
        public void run() {

            String requestUrl = Constant.DtOneSortcominstUrl;
            //要传递的参数
            String params ="?taskId="+taskid+"&docId="+docid+"&instanceId="+instanceid+"&userId="+userId;
            Log.e("params---->", "Post方式请求成功，pararunnable--->" + params);
            Log.e("taskid---->", "Post方式请求成功，pararunnable--->" + taskid);
            String result = HttpUtil.requestPost(requestUrl,params);
            Log.e("listview---->", "Post方式请求成功，pararunnable--->" + result);
            //等待请求结束
            ResolveHttpResponse.showHttpResponse(result,getContext());

        }
    };

    private Runnable completedoc = new Runnable() {
        @Override
        public void run() {
            String requestUrl = Constant.DtOneSortcomdocUrl;
            //要传递的参数
            String params ="?taskId="+taskid+"&docId="+docid+"&userId="+userId;
            Log.e("params---->", "Post方式请求成功，docrunnable--->" + params);
            Log.e("taskid---->", "Post方式请求成功，docrunnable--->" + taskid);
            String result = HttpUtil.requestPost(requestUrl,params);
            Log.e("listview---->", "Post方式请求成功，docrunnable--->" + result);
            //等待请求结束
            ResolveHttpResponse.showHttpResponse(result,getContext());

        }
    };


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
    public void saveIns() {
        ThreadPool.fixedThreadPool().submit(saveinst);
    }

    @Override
    public void completeCon() {
        ThreadPool.fixedThreadPool().submit(completeinst);
    }

    @Override
    public void completeDoc() {
        ThreadPool.fixedThreadPool().submit(completedoc);
    }
}

