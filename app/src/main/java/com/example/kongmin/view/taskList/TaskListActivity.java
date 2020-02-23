package com.example.kongmin.view.taskList;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.kongmin.myapplication.R;

public class TaskListActivity extends AppCompatActivity {


    //任务列表清单
    private TaskListFragment taskListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        initFragment();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initFragment()
    {
       // dtListFragment = new DtListFragment();
       // getSupportFragmentManager().beginTransaction().replace(R.id.task_list_fragment,dtListFragment).show(dtListFragment).commit();
        taskListFragment = new TaskListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.task_list_fragment, taskListFragment).show(taskListFragment).commit();
    }


    @Override
    public void onRestart(){
        super.onRestart();
        taskListFragment = null;
        taskListFragment = new TaskListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.task_list_fragment, taskListFragment).show(taskListFragment).commit();
    }


}
