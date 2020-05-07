package com.example.textannotation.view.myTasks;

import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.textannotation.myapplication.R;
import com.example.textannotation.view.common.MyTabIndicator;

import java.util.ArrayList;
import java.util.List;

public class MyTaskListActivity extends AppCompatActivity {

    private MyTaskListAdapter myTaskListAdapter;

    private ViewPager mViewPager;

    //导航栏
    private MyTabIndicator mTabIndicator;

    //初始化数据
    private List<MyTaskListFragment> list = new ArrayList<MyTaskListFragment>();
    private List<String> titles = new ArrayList<>();

    private String typename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_dotask_list);

        initActionBar();
        mTabIndicator = (MyTabIndicator) findViewById(R.id.mTabIndicator);
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);



        final MyTaskListFragment myTaskListFragment = new MyTaskListFragment();
        list.add(myTaskListFragment);
        Bundle bundle1 = new Bundle();
        typename = "正在进行";
        bundle1.putString("type",typename);
        list.get(0).setArguments(bundle1);


        MyTaskListFragment myDonetaskListFragment = new MyTaskListFragment();
        list.add(myDonetaskListFragment);
        Bundle bundle2 = new Bundle();
        typename = "已完成";
        bundle2.putString("type",typename);
        list.get(1).setArguments(bundle2);

        myTaskListAdapter = new MyTaskListAdapter(getSupportFragmentManager(),list);

        titles.add("正在进行");
        titles.add("已完成");

        mViewPager.setAdapter(myTaskListAdapter);

        mTabIndicator.setTitles(titles);
        mTabIndicator.setViewPager(mViewPager, 0);

        mTabIndicator.setOnPageChangeListener(new MyTabIndicator.PageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public void initActionBar(){
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //Enable自定义的View
            actionBar.setCustomView(R.layout.action_bar_my_task);  //绑定自定义的布局：
        }

        ImageView main_icon = actionBar.getCustomView().findViewById(R.id.back_icon);
        main_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyTaskListActivity.this.finish();
            }
        });

    }


}
