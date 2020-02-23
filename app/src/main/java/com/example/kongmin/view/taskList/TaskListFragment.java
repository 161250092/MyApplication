package com.example.kongmin.view.taskList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.kongmin.Constant.Constant;
import com.example.kongmin.model.ITaskModel;
import com.example.kongmin.model.TaskModel;
import com.example.kongmin.myapplication.R;
import com.example.kongmin.pojo.MarkCategory1;
import com.example.kongmin.view.adapter.TaskListItemAdapter;
import com.example.kongmin.view.taskDetails.TaskDetailActivity;
import com.example.kongmin.view.iCallBack.ListRenderCallBack;
import com.hb.dialog.dialog.LoadingDialog;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;

public class TaskListFragment extends Fragment implements ListRenderCallBack {

    private ListView lv;
    private TaskListItemAdapter adapter;
    private LayoutInflater inflater;

    private int taskType  = 0;

    private View mRootView;


    //加载页数
    private int mPageIndex = Constant.PageIndex;
    //每页加载数量
    private int mPageCapacity = Constant.PageCapacity;
    private ITaskModel taskModel;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_task_demo, container, false);
        lv = mRootView.findViewById(R.id.lv);
        this.inflater=getLayoutInflater();
        initPresenter();

        RefreshLayout refreshLayout = (RefreshLayout) mRootView.findViewById(R.id.refreshLayout);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(1000/*,false*/);//传入false表示刷新失败
                getTaskListInfo();

            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(1000/*,false*/);//传入false表示加载失败
                taskModel.getNextPage(TaskListFragment.this);
            }
        });

        return mRootView;
    }

    LoadingDialog loadingDialog;

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
        Log.e("getFirstpage"," ");
        taskModel.getFirstPage(this);
    }


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
                        intent.putExtra("ids",markCategory1s.get(position).getIds());
                        String typename = markCategory1s.get(position).getType();
                        Log.e("TASKMODEL",typename);
                        if (typename.equals("信息抽取")){//浅粉色
                            taskType = 1;
                        }else if (typename.equals("文本分类")){//浅黄色
                            taskType = 2;
                        }else if (typename.equals("文本关系")){//浅绿色
                            taskType = 3;
                        }else if (typename.contains("文本配对")){//浅蓝色
                            taskType = 4;
                        }else if (typename.equals("文本排序")){//浅紫色
                            taskType = 5;
                        }else if (typename.equals("文本类比排序")){//浅橘色
                            taskType = 6;
                        }
                        intent.putExtra("type",taskType);
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
    public void notifyDataChanged() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (loadingDialog != null)
            loadingDialog.dismiss();
    }

}
