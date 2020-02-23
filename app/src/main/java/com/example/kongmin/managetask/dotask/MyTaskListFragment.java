package com.example.kongmin.managetask.dotask;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kongmin.Constant.Constant;
import com.example.kongmin.model.IMyTaskModel;
import com.example.kongmin.model.MyTaskModel;
import com.example.kongmin.view.adapter.TaskListItemAdapter;
import com.example.kongmin.myapplication.R;
import com.example.kongmin.view.taskDetails.TaskDetailActivity;
import com.example.kongmin.pojo.MarkCategory1;
import com.example.kongmin.util.MyApplication;
import com.example.kongmin.view.iCallBack.ListRenderCallBack;
import com.hb.dialog.dialog.LoadingDialog;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;

/**
 * update by mwx on 2020/02/20.
 * 做任务模块查看所有发布的任务列表界面
 */
public class MyTaskListFragment extends Fragment implements ListRenderCallBack {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private View mRootView;
    //初始化数据

    private ListView lv;
    private LayoutInflater inflater;
    private TaskListItemAdapter adapter;
    private TextView noTasks;

    int tasktype = 0;

    private String typename;

    private MyApplication mApplication;
    private int userId;

    private IMyTaskModel myTaskModel;

    public MyTaskListFragment() {

    }



    public static MyTaskListFragment newInstance(String param1, String param2) {
        MyTaskListFragment fragment = new MyTaskListFragment();
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
        mRootView = inflater.inflate(R.layout.fragment_mydoingtask_list, container, false);
        findViewById(mRootView);
        myTaskModel = new MyTaskModel();

        RefreshLayout refreshLayout = (RefreshLayout) mRootView.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(1000/*,false*/);//传入false表示刷新失败

                if (typename.equals(Constant.UNCOMPLETED))
                    myTaskModel.getMyDoingTasks(MyTaskListFragment.this,userId);
                else
                    myTaskModel.getMyCompletedTasks(MyTaskListFragment.this,userId);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(1000/*,false*/);//传入false表示加载失败
            }
        });

        return mRootView;
    }

    protected void findViewById(View view) {
        lv =    (ListView) view.findViewById(R.id.lv_bwlList);
        noTasks = (TextView) view.findViewById(R.id.no_tasks);

        inflater = getLayoutInflater();
        mApplication = (MyApplication)getActivity().getApplication();
        userId = mApplication.getLoginUserId();
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle bundle =getArguments();
        typename = bundle.getString("type");
        if (typename.equals(Constant.UNCOMPLETED))
            myTaskModel.getMyDoingTasks(this,userId);
        else
            myTaskModel.getMyCompletedTasks(this,userId);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private LoadingDialog loadingDialog;


    @Override
    public void initListView(final ArrayList<MarkCategory1> markCategory1s) {
        loadingDialog = new LoadingDialog(getActivity());
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("mwx","runOnUI"+markCategory1s.size()+"");
                adapter = new TaskListItemAdapter(inflater,markCategory1s);
                lv.setAdapter(adapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // TODO Auto-generated method stub
                        Intent intent=new Intent(getContext(),TaskDetailActivity.class);

                        intent.putExtra("ids",markCategory1s.get(position).getIds());
                        String typename = markCategory1s.get(position).getType();
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
                        loadingDialog = new LoadingDialog(getActivity());
                        loadingDialog.setMessage("loading");
                        loadingDialog.show();
                        intent.putExtra("type",tasktype);
                        startActivity(intent);
                    }
                });

                if (markCategory1s.size() == 0)
                {
                    lv.setVisibility(View.GONE);
                    noTasks.setVisibility(View.VISIBLE);
                }

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
            }
        });
    }


}
