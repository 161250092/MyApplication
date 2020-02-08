package com.example.kongmin.view.textcategory;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.example.kongmin.myapplication.R;

import java.util.ArrayList;
import java.util.List;
/*
* 文本配对做任务旧页面
* */
public class MatchCategoryActivity extends AppCompatActivity{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    //private SectionsPagerAdapter mSectionsPagerAdapter;

    private MatchCategoryAboveAdapter mMatchCategoryAdapter1;
    private MatchCategoryBlowAdapter mMatchCategoryAdapter2;
    //private MatchCategoryAdapter mMatchCategoryAdapter;



    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager1;
    private ViewPager mViewPager2;
    //private ViewPager mViewPager;

    //导航栏
    private MyTabIndicator mTabIndicator1;
    private MyTabIndicator mTabIndicator2;
    //private MyTabIndicator mTabIndicator2;
    //初始化数据
    private List<MatchCategoryAboveFragment> list1 = new ArrayList<MatchCategoryAboveFragment>();
    //private List<PlaceholderFragment> list1 = new ArrayList<PlaceholderFragment>();
    private List<String> titles1 = new ArrayList<>();

    private List<MatchCategoryBlowFragment> list2 = new ArrayList<MatchCategoryBlowFragment>();
    private List<String> titles2 = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_category);

        mTabIndicator1 = (MyTabIndicator) findViewById(R.id.mTabIndicator1);
        mViewPager1 = (ViewPager) findViewById(R.id.mViewPager1);
        mTabIndicator2 = (MyTabIndicator) findViewById(R.id.mTabIndicator2);
        mViewPager2 = (ViewPager) findViewById(R.id.mViewPager2);


        for(int i=0;i<7;i++){
            titles1.add("标题:" + i+1);
            MatchCategoryAboveFragment m1 = MatchCategoryAboveFragment.newInstance(i+3);

            list1.add(m1);
            /*PlaceholderFragment f1 =  PlaceholderFragment.newInstance(i+3);
            list1.add(f1);*/
            titles2.add("标题:" + i+1);
            MatchCategoryBlowFragment m2 = MatchCategoryBlowFragment.newInstance(i+3);
            list2.add(m2);


        }

        /*PlaceholderFragment f1 = PlaceholderFragment.newInstance(3);
        list.add(f1);
        PlaceholderFragment f2 = PlaceholderFragment.newInstance(5);
        list.add(f2);
        PlaceholderFragment f3 = PlaceholderFragment.newInstance(7);
        list.add(f3);*/


        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mMatchCategoryAdapter1 = new MatchCategoryAboveAdapter(getSupportFragmentManager(),list1);
        //mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),list1);
        mMatchCategoryAdapter2 = new MatchCategoryBlowAdapter(getSupportFragmentManager(),list2);


        /*for(int i = 0; i < 3; i ++){
            titles.add("标题:" + i+1);
        }*/

        // Set up the ViewPager with the sections adapter.
        //mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager1.setAdapter(mMatchCategoryAdapter1);
        //mViewPager1.setAdapter(mSectionsPagerAdapter);
        mViewPager2.setAdapter(mMatchCategoryAdapter2);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/



        mTabIndicator1.setTitles(titles1);
        mTabIndicator1.setViewPager(mViewPager1, 0);
        //mTabIndicator.setTitles(titles, 3);//可以设置默认选中的title

        mTabIndicator1.setOnPageChangeListener(new MyTabIndicator.PageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Toast.makeText(MatchCategoryActivity.this, "选择了：" + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        mTabIndicator2.setTitles(titles2);
        mTabIndicator2.setViewPager(mViewPager2, 0);
        //mTabIndicator.setTitles(titles, 3);//可以设置默认选中的title

        mTabIndicator2.setOnPageChangeListener(new MyTabIndicator.PageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Toast.makeText(MatchCategoryActivity.this, "选择了：" + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_text_category_tab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}


