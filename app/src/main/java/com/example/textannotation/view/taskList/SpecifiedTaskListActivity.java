package com.example.textannotation.view.taskList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.textannotation.Constant.Constant;
import com.example.textannotation.model.ISpecifiedTypeTaskModel;

import com.example.textannotation.model.SpecifiedTypeTaskModel;

import com.example.textannotation.myapplication.R;
import com.example.textannotation.pojo.TaskInfo;
import com.example.textannotation.presenter.MyCallBack;
import com.example.textannotation.view.adapter.TaskListItemAdapter;
import com.example.textannotation.view.iCallBack.ListRenderCallBack;
import com.example.textannotation.view.taskDetails.TaskDetailActivity;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;

public class SpecifiedTaskListActivity extends AppCompatActivity  implements ListRenderCallBack,MyCallBack {

    ListView mTaskList;

    private ISpecifiedTypeTaskModel taskModel;

    RefreshLayout refreshLayout;
    private LayoutInflater inflater;
    private TaskListItemAdapter adapter;

    private String selectedTaskType = Constant.taskType[0];
    private int taskType = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_task);
        initActionBar();

        mTaskList = findViewById(R.id.lv);
        refreshLayout = findViewById(R.id.refreshLayout);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(800/*,false*/);//传入false表示刷新失败
                taskModel.getSpecifiedTasks(SpecifiedTaskListActivity.this,selectedTaskType,SpecifiedTaskListActivity.this);

            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(800/*,false*/);//传入false表示加载失败
            }
        });

        taskModel = new SpecifiedTypeTaskModel();
        inflater = getLayoutInflater();

        taskModel.getOriginSpecifiedTasks(SpecifiedTaskListActivity.this,selectedTaskType,SpecifiedTaskListActivity.this);

    }

    private void initActionBar(){
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //Enable自定义的View
            actionBar.setCustomView(R.layout.action_bar_task_list);  //绑定自定义的布局：
        }

        ImageView return_icon = actionBar.getCustomView().findViewById(R.id.back_icon);
        return_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpecifiedTaskListActivity.this.finish();
            }
        });

        ImageView setting_icon = actionBar.getCustomView().findViewById(R.id.settings);
        setting_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new XPopup.Builder(SpecifiedTaskListActivity.this).asCenterList("类型",Constant.taskType,new OnSelectListener(){
                    @Override
                    public void onSelect(int position, String text) {
                        selectedTaskType = Constant.taskType[position];
                        Log.e("mwx",selectedTaskType);
                        taskModel.getSpecifiedTasks(SpecifiedTaskListActivity.this,selectedTaskType,SpecifiedTaskListActivity.this);
                    }
                } ).show();
            }
        });


    }



    @Override
    public void initListView(final ArrayList<TaskInfo> taskInfos) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter = new TaskListItemAdapter(inflater, taskInfos);
                mTaskList.setAdapter(adapter);
                mTaskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent=new Intent(SpecifiedTaskListActivity.this,TaskDetailActivity.class);
                        intent.putExtra("ids", taskInfos.get(position).getIds());
                        String typename = taskInfos.get(position).getType();
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
                        startActivity(intent);
                    }
                });
            }
        });
    }


    @Override
    public void notifyDataChanged() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    public void onSuccess() {

    }

    @Override
    public void onFailure(String msg) {

    }
}
