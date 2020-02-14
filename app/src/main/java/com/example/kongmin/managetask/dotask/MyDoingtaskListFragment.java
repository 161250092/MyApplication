package com.example.kongmin.managetask.dotask;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.kongmin.Constant.Constant;
import com.example.kongmin.network.OkHttpUtil;
import com.example.kongmin.view.adapter.TaskListItemAdapter;
import com.example.kongmin.myapplication.R;
import com.example.kongmin.view.dotask.MyListView;
import com.example.kongmin.view.dotask.TaskDetailActivity;
import com.example.kongmin.pojo.MarkCategory1;
import com.example.kongmin.util.HttpUtil;
import com.example.kongmin.util.MyApplication;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DtListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DtListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

/**
 * Created by kongmin on 2019/02/11.
 * 做任务模块查看所有发布的任务列表界面
 */
public class MyDoingtaskListFragment extends Fragment implements MyListView.LoadListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View mRootView;
    //初始化数据
    //private ListView lv;
    private MyListView lv;
    private LayoutInflater inflater;
    private ArrayList<MarkCategory1> array = new ArrayList<MarkCategory1>();
    private ArrayList<MarkCategory1> loadarray = new ArrayList<MarkCategory1>();
    private TaskListItemAdapter adapter;
    private int loadnum = 10;
    private int num = 0;
    private int formalnum = 0;
    int tasktype = 0;

    private String dtstatus;

    private String typename;


    private OnFragmentInteractionListener mListener;

    private MyApplication mApplication;
    private int userId;

    public MyDoingtaskListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DtListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyDoingtaskListFragment newInstance(String param1, String param2) {
        MyDoingtaskListFragment fragment = new MyDoingtaskListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_dt_list, container, false);
        mRootView = inflater.inflate(R.layout.fragment_mydoingtask_list, container, false);

        Bundle bundle =getArguments();
                    /*String message = null;
                    if(bundle!=null){
                        message = bundle.getString("toFragment");
                    }
                    Log.e("DotaskExtract---->", "GET方式请求成功，toFragment--->" + message);*/
        typename = bundle.getString("type");


        findViewById(mRootView);
        return mRootView;

    }

    protected void findViewById(View view) {
        //lv=(ListView) view.findViewById(R.id.lv_bwlList);
        lv =(MyListView)view.findViewById(R.id.lv_bwlList);
        lv.setInterface(this);
        inflater=getLayoutInflater();

        // 获取整个应用的Application对象
        // 在不同的Activity中获取的对象是同一个
        mApplication = (MyApplication)getActivity().getApplication();
        userId = mApplication.getLoginUserId();

        getFileContent();
        try {
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        num = loadnum >  array.size() ? array.size() : loadnum;
        formalnum = num;
        for (int i=0;i<num;i++){
            loadarray.add(array.get(i));
        }

        adapter=new TaskListItemAdapter(inflater,loadarray);
        lv.setAdapter(adapter);
        /*
         * 点击listView里面的item,用来查看任务详情
         */
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                Intent intent=new Intent(getContext(),TaskDetailActivity.class);
                /*intent.putExtra("ids",array.get(position).getIds());
                String typename = array.get(position).getType();*/
                //todo position从1开始
                intent.putExtra("ids",array.get(position-1).getIds());
                String typename = array.get(position-1).getType();
                if (typename.equals("信息抽取")){//浅粉色
                   tasktype = 1;
                }else if (typename.equals("文本分类")){//浅黄色
                    tasktype = 2;
                }else if (typename.equals("文本关系")){//浅绿色
                    tasktype = 3;
                }else if (typename.contains("文本配对")){//浅蓝色
                    tasktype = 4;
                }else if (typename.equals("文本排序")){//浅紫色
                    tasktype = 5;
                }else if (typename.equals("类比排序")){//浅橘色
                    tasktype = 6;
                }
                intent.putExtra("type",tasktype);
                startActivity(intent);
            }
        });

    }

    //下拉刷新
    @Override
    public void onLoad() {
        //设置三秒延迟模仿延时获取数据
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //加载数据
                /*for (int j=1;j<11;j++){
                    list.add(j);
                }*/
                for (int i=num;i<num+loadnum&&i<array.size();i++){
                    loadarray.add(array.get(i));
                }
                //更新数据
                adapter.notifyDataSetChanged();
                num = num+loadnum;
                //加载完毕
                lv.loadComplete();

            }
        },3000);


    }
    //上拉加载
    @Override
    public void pullLoad() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /*list.clear();
                for (int i=1;i<20;i++){
                    list.add(i+1);
                }*/
                loadarray.clear();
                for (int i=0;i<formalnum;i++){
                    loadarray.add(array.get(i));
                }
                num = formalnum;
                adapter.notifyDataSetChanged();
                lv.loadComplete();

            }
        },2000);

    }

    public void getFileContent(){
        String requestUrl = Constant.selectmydotaskUrl;
        //String params = "";
        if(typename.equals("正在进行")){
            dtstatus = "进行中";
        }else{
            dtstatus = "已完成";
        }
        int page = 1;
        int limit = Integer.MAX_VALUE;
        String params = "?dtstatus="+dtstatus+"&page="+page+"&limit="+limit+"&userId="+userId;

        OkHttpUtil.sendGetRequest(requestUrl + params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                int  id = 0;
                String title = "";
                String createtime = "";
                String taskcompstatus = "";
                String typename="";
                JSONObject jsonObject = JSONObject.parseObject(result);
                //把字符串转成JSONArray对象
                JSONArray jsonArray = (JSONArray)jsonObject.get("data");
                if(jsonArray.size()>0){
                    for(int i=0;i<jsonArray.size();i++){
                        //遍历jsonarray数组，把每一个对象转成json对象
                        JSONObject job = jsonArray.getJSONObject(i);
                        //得到每个对象中的属性值
                        //Log.e("size---->", "Post方式请求成功，size--->" + jsonArray.size());
                        if(job.get("taskId")!=null) {
                            //Log.e("tid---->", "Post方式请求成功，tid--->" + job.get("tid").toString());
                            id =Integer.valueOf(job.get("taskId").toString());
                        }
                        if(job.get("title")!=null) {
                            //Log.e("title---->", "Post方式请求成功，title--->" + job.get("title").toString());
                            title = job.get("title").toString();
                        }
                        if(job.get("typeName")!=null) {
                            //Log.e("title---->", "Post方式请求成功，title--->" + job.get("title").toString());
                            typename = job.get("typeName").toString();
                        }
                        if(job.get("dotime")!=null) {
                            //Log.e("createtime---->", "Post方式请求成功，createtime--->" + job.get("createtime").toString());
                            createtime = job.get("dotime").toString();
                        }
                        if(job.get("dpercent")!=null) {
                            //Log.e("taskcompstatus---->", "Post方式请求成功，taskcompstatus--->" + job.get("taskcompstatus").toString());
                            taskcompstatus = job.get("dpercent").toString();
                        }
                        MarkCategory1 cun1 = new MarkCategory1(id,title,taskcompstatus,typename,createtime);;
                        array.add(cun1);
                        title = "";
                        createtime = "";
                        taskcompstatus = "";
                        typename="";
                    }
                }
                Integer num = (Integer)jsonObject.get("count");
                Log.e("count---->", "Post方式请求成功，count--->" + num);

            }
        });
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // TODO: http request.
            //String requestUrl = "http://172.20.10.5:8080/task/selectTaskByType";
            String requestUrl = Constant.selectmydotaskUrl;
            //String params = "";
            if(typename.equals("正在进行")){
                dtstatus = "进行中";
            }else{
                dtstatus = "已完成";
            }
            int page = 1;
            int limit = Integer.MAX_VALUE;
            String params = "?dtstatus="+dtstatus+"&page="+page+"&limit="+limit+"&userId="+userId;
            String result = HttpUtil.requestGet(requestUrl,params);
            int  id = 0;
            String title = "";
            String createtime = "";
            String taskcompstatus = "";
            String typename="";
            JSONObject jsonObject = JSONObject.parseObject(result);
            //把字符串转成JSONArray对象
            JSONArray jsonArray = (JSONArray)jsonObject.get("data");
            if(jsonArray.size()>0){
                for(int i=0;i<jsonArray.size();i++){
                    //遍历jsonarray数组，把每一个对象转成json对象
                    JSONObject job = jsonArray.getJSONObject(i);
                    //得到每个对象中的属性值
                    //Log.e("size---->", "Post方式请求成功，size--->" + jsonArray.size());
                    if(job.get("taskId")!=null) {
                        //Log.e("tid---->", "Post方式请求成功，tid--->" + job.get("tid").toString());
                        id =Integer.valueOf(job.get("taskId").toString());
                    }
                    if(job.get("title")!=null) {
                        //Log.e("title---->", "Post方式请求成功，title--->" + job.get("title").toString());
                        title = job.get("title").toString();
                    }
                    if(job.get("typeName")!=null) {
                        //Log.e("title---->", "Post方式请求成功，title--->" + job.get("title").toString());
                        typename = job.get("typeName").toString();
                    }
                    if(job.get("dotime")!=null) {
                        //Log.e("createtime---->", "Post方式请求成功，createtime--->" + job.get("createtime").toString());
                        createtime = job.get("dotime").toString();
                    }
                    if(job.get("dpercent")!=null) {
                        //Log.e("taskcompstatus---->", "Post方式请求成功，taskcompstatus--->" + job.get("taskcompstatus").toString());
                        taskcompstatus = job.get("dpercent").toString();
                    }
                    MarkCategory1 cun1 = new MarkCategory1(id,title,taskcompstatus,typename,createtime);;
                    array.add(cun1);
                    title = "";
                    createtime = "";
                    taskcompstatus = "";
                    typename="";
                }
            }
            Integer num = (Integer)jsonObject.get("count");
            Log.e("count---->", "Post方式请求成功，count--->" + num);
        }
    };


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
