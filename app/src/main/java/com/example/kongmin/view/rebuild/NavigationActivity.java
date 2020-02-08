package com.example.kongmin.view.rebuild;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;


import com.example.kongmin.myapplication.R;

public class NavigationActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        drawerLayout = findViewById(R.id.dl_activity_main);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar !=  null){
            //设置按钮
            actionBar.setDisplayHomeAsUpEnabled(true);
            //更换按钮图标（默认是返回的箭头）
        }
 }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //该按钮的Id已经在android的R文件中定义，为 ：android.R.id.home
        if(item.getItemId() == android.R.id.home){
            //弹出DrawerLayout菜单，参数为弹出的方式
            drawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

}
