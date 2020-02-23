package com.example.kongmin.view.textcategory;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.kongmin.myapplication.R;
import com.example.kongmin.view.doTask.categoryRelation.SectionsPagerAdapter;
import com.example.kongmin.view.doTask.categoryRelation.PlaceholderFragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

public class CategoryTabActivity extends AppCompatActivity {

    private MyTabIndicator mTabIndicator;
    private ViewPager mViewPager;


    private SectionsPagerAdapter mSectionsPagerAdapter;
    private List<PlaceholderFragment> list = new ArrayList<PlaceholderFragment>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_tab);

        mTabIndicator = (MyTabIndicator) findViewById(R.id.mTabIndicator);
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);

        //初始化数据
        final List<String> titles = new ArrayList<>();
        final ArrayList<View> viewList = new ArrayList<View>();// 将要分页显示的View装入数组中


        for(int i = 0; i < 10; i ++){
            titles.add("标题:" + i);
            TextView textView = new TextView(this);
            textView.setText(titles.get(i));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT);
            textView.setGravity(Gravity.CENTER);
            textView.setLayoutParams(params);
            viewList.add(textView);
            PlaceholderFragment f1 = PlaceholderFragment.newInstance(i);
            list.add(f1);
            //viewList.add(f1.getView());
            Fragment fr = new Fragment();
        }


        PagerAdapter mAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return titles.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(viewList.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View v = viewList.get(position);
                ViewGroup parent = (ViewGroup) v.getParent();
                if (parent != null) {
                    parent.removeAllViews();
                }
                container.addView(v, 0);
                return viewList.get(position);
            }
        };

       /* PlaceholderFragment f1 = PlaceholderFragment.newInstance(3);
        list.add(f1);
        PlaceholderFragment f2 = PlaceholderFragment.newInstance(5);
        list.add(f2);
        PlaceholderFragment f3 = PlaceholderFragment.newInstance(7);
        list.add(f3);*/

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),list);

        //绑定监听
        mViewPager.setAdapter(mAdapter);
        //mViewPager.setAdapter(mSectionsPagerAdapter);
        //mViewPager.setCurrentItem(0);

        mTabIndicator.setTitles(titles);
        mTabIndicator.setViewPager(mViewPager, 0);
        //mTabIndicator.setTitles(titles, 3);//可以设置默认选中的title

        mTabIndicator.setOnPageChangeListener(new MyTabIndicator.PageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Toast.makeText(CategoryTabActivity.this, "选择了：" + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
}