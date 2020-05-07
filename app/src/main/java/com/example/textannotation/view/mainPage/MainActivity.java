package com.example.textannotation.view.mainPage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;

import com.example.textannotation.myapplication.R;
import com.example.textannotation.util.MyApplication;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;

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

        setContentView(R.layout.activity_main);

        myApplication = (MyApplication)getApplication();
        initActionBar();
        initFragment();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(changeFragment);
    }
    private void initActionBar(){

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //Enable自定义的View
            actionBar.setCustomView(R.layout.action_bar);  //绑定自定义的布局：
        }

        ImageView search_icon = actionBar.getCustomView().findViewById(R.id.search_icon);
        search_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new XPopup.Builder(MainActivity.this).asInputConfirm("","请输入搜索内容",new OnInputConfirmListener(){
                    @Override
                    public void onConfirm(String text) {


                    }
                }).show();
            }
        });


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
    private BottomNavigationView.OnNavigationItemSelectedListener changeFragment = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item){
            switch (item.getItemId()){
                case R.id.navigation_main_page: {
                    if(lastfragment!=0) {
                        switchFragment(lastfragment,0);
//                        getSupportActionBar().show();
                        lastfragment=0;
                    }
                    return true;
                }
                case R.id.navigation_notifications: {
                    if(lastfragment!=1) {
                        switchFragment(lastfragment,1);
//                        getSupportActionBar().hide();
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

    public boolean  onKeyDown(int keyCode, KeyEvent event) {
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home.addCategory(Intent.CATEGORY_HOME);
        startActivity(home);
        return true;
    }

    public MyApplication getMyApplication(){
        return myApplication;
    }

}
