package com.example.kongmin.view.rebuild;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.SearchView;

import com.example.kongmin.myapplication.R;
import com.example.kongmin.util.MyApplication;
import com.example.kongmin.view.ManagerFragment;
import com.example.kongmin.view.PublishFragment;
import com.example.kongmin.view.dotask.DtListFragment;

/**
 * update by mwx on 2020/01/18.
 * Tab首页
 */

public class MainActivity extends AppCompatActivity {

    private MainPageFragment mainPageFragment;
    private ManagerFragment managerFragment;
    private Fragment[] fragments;
    //用于记录上个选择的Fragment
    private int lastfragment;
    private BottomNavigationView bottomNavigationView;

    MyApplication myApplication;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getSupportActionBar() != null){
//            getSupportActionBar().hide();
//        }
        setContentView(R.layout.activity_main);

        myApplication = (MyApplication)getApplication();
        initActionBar();
        initFragment();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(changeFragment);
    }
    private void initActionBar(){
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main_page, menu);
        MenuItem searchItem = menu.findItem(R.id.search_line);

        SearchView sv = (SearchView) MenuItemCompat.getActionView(searchItem);
        //设置监听
        if (sv != null)
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        Log.e("MainActivity","end");
        return super.onCreateOptionsMenu(menu);
    }

    public MyApplication getMyApplication(){
        return myApplication;
    }

}
