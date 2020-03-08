package com.example.textannotation.view.taskList;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.textannotation.myapplication.R;

public class TaskListActivity extends AppCompatActivity {


    //任务列表清单
    private TaskListFragment taskListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        initActionBar();
        initFragment();

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
                TaskListActivity.this.finish();
            }
        });
    }

    public void hideActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    public void showActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.show();
    }


    private void initFragment()
    {
        taskListFragment = new TaskListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.task_list_fragment, taskListFragment).show(taskListFragment).commit();
    }

//
    @Override
    public void onRestart() {
        super.onRestart();
        taskListFragment = null;
        taskListFragment = new TaskListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.task_list_fragment, taskListFragment).show(taskListFragment).commitAllowingStateLoss();

    }


}
