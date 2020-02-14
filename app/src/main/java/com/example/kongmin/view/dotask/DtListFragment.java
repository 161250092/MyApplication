package com.example.kongmin.view.dotask;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.kongmin.Constant.Constant;
import com.example.kongmin.model.ITaskModel;
import com.example.kongmin.model.TaskModel;
import com.example.kongmin.myapplication.*;
import com.example.kongmin.pojo.MarkCategory1;
import com.example.kongmin.presenter.ITaskListPresenter;
import com.example.kongmin.presenter.MyCallBack;
import com.example.kongmin.presenter.impl.TaskListPresenter;
import com.example.kongmin.util.HttpUtil;
import com.example.kongmin.view.adapter.TaskListItemAdapter;
import com.example.kongmin.view.rebuild.ListRenderCallBack;
import com.example.kongmin.view.rebuild.LogoActivity;
import com.hb.dialog.dialog.LoadingDialog;

import android.os.Handler;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * reviwed by mwx on 2020/02/01.
 * 做任务模块查看所有发布的任务列表界面
 */
public class DtListFragment extends Fragment implements MyListView.LoadListener,MyCallBack,ListRenderCallBack {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    /**
     * 初始化
     */
    private View mRootView;
    private MyListView lv;
    private LayoutInflater inflater;
    private int taskType  = 0;
    private TaskListItemAdapter adapter;

    //加载页数
    private int mPageIndex = Constant.PageIndex;
    //每页加载数量
    private int mPageCapacity = Constant.PageCapacity;
    private ITaskModel taskModel;

    public DtListFragment() {
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
        mRootView = inflater.inflate(R.layout.fragment_dt_list, container, false);
        lv = mRootView.findViewById(R.id.task_List);
        lv.setInterface(this);
        this.inflater=getLayoutInflater();
        initPresenter();
        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getTaskListInfo();
    }

    private void initPresenter(){
        inflater = getLayoutInflater();
        taskModel = new TaskModel();
    }

    private void getTaskListInfo(){
        taskModel.getFirstPage(this);
    }
    //下拉刷新
    @Override
    public void onLoad() {
        Log.e("List_Fragment","onLoad");
        taskModel.getNextPage(this);
    }
    //上拉加载
    @Override
    public void pullLoad() {

    }

    private int tasktype = 0;

    LoadingDialog loadingDialog;

    @Override
    public void initListView(final ArrayList<MarkCategory1> markCategory1s) {
        loadingDialog = new LoadingDialog(getActivity());
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter=new TaskListItemAdapter(inflater,markCategory1s);
                lv.setAdapter(adapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent=new Intent(getContext(),TaskDetailActivity.class);
                        intent.putExtra("ids",markCategory1s.get(position-1).getIds());
                        String typename = markCategory1s.get(position-1).getType();
                        Log.e("TASKMODEL",typename);
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
                        loadingDialog = new LoadingDialog(getActivity());
                        loadingDialog.setMessage("loading");
                        loadingDialog.show();
                        startActivity(intent);
                    }
                });
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (loadingDialog != null)
            loadingDialog.dismiss();
    }
    @Override
    public void notifyDataChanged() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
                lv.loadComplete();
            }
        });

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onFailure(String msg) {
        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
    }

}