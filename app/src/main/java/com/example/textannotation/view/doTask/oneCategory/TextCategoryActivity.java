package com.example.textannotation.view.doTask.oneCategory;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.textannotation.myapplication.R;
import com.example.textannotation.pojo.FileInfo;
import com.example.textannotation.util.MyApplication;
import com.example.textannotation.util.SerializableMap;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class TextCategoryActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener   {

    private MyApplication mApplication;
    private int userId;

    private String status[] = {"全部", "进行中"};
    private String docStatus = status[0];

    //任务ID
    private int taskid;
    //做任务页面 or 查看做任务页面
    private String typename;
    //文件 Id
    private int docId;

    //文件内容
    FileInfo mFileInfo;

    //标签内容
    private  SerializableMap mLabelMap;
    private Map<String,Integer> mLabel_name_id_map = new LinkedHashMap<String,Integer>();


    private OneCategoryAdapter mOneCategoryAdapter;

    private ViewPager mViewPager;

    private List<TextCategoryFragment> fragment_list = new ArrayList<>();


    /**
     * 文本分类活动加载顺序
     * 1.加载布局，隐藏actionbar
     * 2.获取登录用户信息
     * 3.解析bundle
     * 4.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_category);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }


        mApplication = (MyApplication)getApplication();
        userId = mApplication.getLoginUserId();

        resolveBundle();


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.tools);
        navigation.setOnNavigationItemSelectedListener(this);

    }

    private void resolveBundle(){

        //接收从上一个页面传递进来的参数
        Intent intent = getIntent();
        taskid = intent.getIntExtra("taskid",0);
        typename = intent.getStringExtra("type");
        Bundle tdbundle = intent.getExtras();
        //传送过来的做任务的文件内容
        SerializableMap fileMap = (SerializableMap) tdbundle.get("filemap");
        mFileInfo  = new FileInfo(fileMap);
        docId = mFileInfo.getmFileIds()[0];

        mLabelMap = (SerializableMap) tdbundle.get("instlabel");
        mLabel_name_id_map = mLabelMap.getMap();



    }







    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
}
