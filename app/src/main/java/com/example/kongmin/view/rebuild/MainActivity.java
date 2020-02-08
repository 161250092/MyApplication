package com.example.kongmin.view.rebuild;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.example.kongmin.myapplication.R;
import com.example.kongmin.view.ManagerFragment;
import com.example.kongmin.view.PublishFragment;
import com.example.kongmin.view.dotask.DtListFragment;

/**
 * Tab首页
 * 每个Tab页有一个Fragment，
 * 分别为publishFragment,doFragment,managerFragment
 * Tab首页实现三个Fragment的切换
 * Created by kongmin on 2019/02/11.
 * Tab首页
 */

public class MainActivity extends AppCompatActivity {

    private MainPageFragment mainPageFragment;
    private ManagerFragment managerFragment;
    private Fragment[] fragments;
    //用于记录上个选择的Fragment
    private int lastfragment;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_main);

        initFragment();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        //navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setOnNavigationItemSelectedListener(changeFragment);
    }

    //初始化fragment和fragment数组
    private void initFragment()
    {
        mainPageFragment = new MainPageFragment();
        managerFragment = new ManagerFragment();
        fragments = new Fragment[]{mainPageFragment,managerFragment};
        lastfragment=0;
        getSupportFragmentManager().beginTransaction().replace(R.id.id_content,mainPageFragment).show(mainPageFragment).commit();
        bottomNavigationView=(BottomNavigationView)findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(changeFragment);
    }
    //判断选择的菜单
    private BottomNavigationView.OnNavigationItemSelectedListener changeFragment= new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item){
            switch (item.getItemId()){
                case R.id.navigation_main_page: {
                    if(lastfragment!=0) {
                        switchFragment(lastfragment,0);
                        lastfragment=0;
                    }
                    return true;
                }
                case R.id.navigation_notifications: {
                    if(lastfragment!=1) {
                        switchFragment(lastfragment,1);
                        lastfragment=1;
                    }
                    return true;
                }

            }
            return false;
        }
    };
    //切换Fragment
    private void switchFragment(int lastfragment,int index){
        FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
        //隐藏上个Fragment
        transaction.hide(fragments[lastfragment]);
        if(fragments[index].isAdded()==false) {
            transaction.add(R.id.id_content,fragments[index]);
        }
        transaction.show(fragments[index]).commitAllowingStateLoss();
    }

    public void jumpToTaskList(View view){
        Intent intent = new Intent(MainActivity.this, TaskListActivity.class);
        startActivity(intent);
    }
}
