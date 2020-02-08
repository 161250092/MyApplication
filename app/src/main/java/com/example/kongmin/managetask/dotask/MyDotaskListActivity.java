package com.example.kongmin.managetask.dotask;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.kongmin.myapplication.R;
import com.example.kongmin.view.textcategory.MyTabIndicator;

import java.util.ArrayList;
import java.util.List;

public class MyDotaskListActivity extends AppCompatActivity {

    private MyDotaskListAdapter myDotaskListAdapter;

    private ViewPager mViewPager;

    //导航栏
    private MyTabIndicator mTabIndicator;

    //初始化数据
    private List<MyDoingtaskListFragment> list = new ArrayList<MyDoingtaskListFragment>();
    private List<String> titles = new ArrayList<>();

    private String typename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_dotask_list);

        mTabIndicator = (MyTabIndicator) findViewById(R.id.mTabIndicator);
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);


        MyDoingtaskListFragment myDoingtaskListFragment = new MyDoingtaskListFragment();
        list.add(myDoingtaskListFragment);

        Bundle bundle1 = new Bundle();
        //传递数据
        typename = "正在进行";
        //是做任务页面还是查看做任务页面
        bundle1.putString("type",typename);
        list.get(0).setArguments(bundle1);

        MyDoingtaskListFragment myDonetaskListFragment = new MyDoingtaskListFragment();
        list.add(myDonetaskListFragment);

        Bundle bundle2 = new Bundle();
        //传递数据
        typename = "已完成";
        //是做任务页面还是查看做任务页面
        bundle2.putString("type",typename);
        list.get(1).setArguments(bundle2);

        myDotaskListAdapter = new MyDotaskListAdapter(getSupportFragmentManager(),list);

        titles.add("正在进行");
        titles.add("已完成");

        mViewPager.setAdapter(myDotaskListAdapter);

        mTabIndicator.setTitles(titles);
        mTabIndicator.setViewPager(mViewPager, 0);

        mTabIndicator.setOnPageChangeListener(new MyTabIndicator.PageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                Toast.makeText(MyDotaskListActivity.this, "选择了：" + position, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

}
