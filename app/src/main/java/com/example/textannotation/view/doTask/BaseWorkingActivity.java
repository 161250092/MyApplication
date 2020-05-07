package com.example.textannotation.view.doTask;

import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;

import com.example.textannotation.util.MyApplication;
import com.example.textannotation.util.SerializableMap;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseWorkingActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    //任务的文件内容
    protected Map<String,Integer> filemap = new LinkedHashMap<String,Integer>();
    protected List<Integer> fileid = new ArrayList<Integer>();
    protected List<String> filename = new ArrayList<String>();
    protected String[] mFileNames;
    protected Integer[] mFileIds;

    //传送过来的做任务的标签内容
    protected SerializableMap inststrMap;
    protected Map<String,Integer> inststrmap = new LinkedHashMap<String,Integer>();


    protected int taskid;
    protected int docId;
    protected int userId;

    protected MyApplication mApplication;


    public abstract void initFragment();

    public abstract void dealIntent();

}
