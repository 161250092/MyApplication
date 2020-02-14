package com.example.kongmin.view.textcategory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.kongmin.Constant.Constant;
import com.example.kongmin.myapplication.R;
import com.example.kongmin.view.rebuild.InitViewCallBack;
import com.example.kongmin.view.textcategory.util.ListDropDownAdapter;
import com.example.kongmin.network.OkHttpUtil;
import com.example.kongmin.network.Url;
import com.example.kongmin.util.*;

import java.util.*;

import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.text.SimpleDateFormat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.annotation.SuppressLint;
/**
 *
 * 信息抽取做任务界面
 * Created by kongmin
 * 2018.12.29
 */
public class DotaskExtractActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private DotaskExtractAdapter mDotaskExtractAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    //导航栏
    private MyTabIndicator mTabIndicator;

    //初始化数据
    private List<DotaskExtractFragment> list = new ArrayList<DotaskExtractFragment>();
    private List<String> titles = new ArrayList<>();

    //文件内容
    private String content;
    //内容ID
    private int contentid;
    //段落在文本中的索引
    private int contentindex;

    private List<String> contents = new ArrayList<String>();
    private List<Integer> contentids = new ArrayList<Integer>();
    private List<Integer> contentindexs = new ArrayList<Integer>();
    private int fragmentsize;

    //传送过来的做任务的文件内容
    private Map<String,Integer> filemap = new LinkedHashMap<String,Integer>();
    //传送过来的做任务的标签内容
    private  SerializableMap inststrMap;
    private Map<String,Integer> inststrmap = new LinkedHashMap<String,Integer>();


    private List<Integer> fileid = new ArrayList<Integer>();
    private List<String> filename = new ArrayList<String>();
    //选中的fileID
    private int btnfileid = -1;
    //选中的文件状态
    private String filestatus="";
    //某一个段落的状态
    private String parastatusstr;
    private List<String> parastatus= new ArrayList<String>();

    //和选择弹出框相关的
    @InjectView(R.id.dropDownMenu) DropDownMenu mDropDownMenu;
    private String headers[] = {"文件列表", "完成状态", "确定"};
    private List<View> popupViews = new ArrayList<>();

    private ListDropDownAdapter fileAdapter;
    private ListDropDownAdapter statusAdapter;
    private ListDropDownAdapter btnAdapter;
    private String status[] = {"全部", "进行中"};
    private String confirm[] = {"确定"};

    //任务ID
    private int taskid;
    //是做任务页面还是查看做任务页面
    private String typename;
    private int docId;
    //文件状态默认是全部
    private String docStatus = "全部";
    //todo 设置userId
    private int userId;

    //已经标注了的标签
    private ArrayList<Integer> index_begins = new ArrayList<Integer>();

    private ArrayList<Integer> index_ends = new ArrayList<Integer>();

    private ArrayList<Integer> label_ids = new ArrayList<Integer>();

    private int index_begin;
    private int index_end;
    private int label_id;

    private TextView downloadextract;

    private String filePath = "/sdcard/xinjian/";

    private String filenameTemp = filePath+ "信息抽取"+ ".txt";

    private StringBuffer downloadfilecontent = new StringBuffer();
    private String singlelinecontent;

    private String downloadfilename;
    private String downloadlabelname;
    private String downloadcontent;

    private Map<Integer,String> downloadlabel = new LinkedHashMap<Integer,String>();

    private MyApplication mApplication;

    //信息抽取标签颜色
    private ArrayList<String> colors = new ArrayList<>();
    private SerializableSortMap colorsMap;
    private Map<Integer,String> colormap = new LinkedHashMap<Integer,String>();

    private HashMap<Integer,ArrayList<String>> colorhashMap = new HashMap<>();
    private HashMap<Integer,ArrayList<Integer>> beginhashMap = new HashMap<>();
    private HashMap<Integer,ArrayList<Integer>> endhashMap = new HashMap<>();
    private HashMap<Integer,ArrayList<Integer>> labelidhashMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dotask_extract);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        //获取读写权限
        requestReadExternalPermission();
        requestWriteExternalPermission();

        mTabIndicator = (MyTabIndicator) findViewById(R.id.mTabIndicator);
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);
        //和选择弹出框相关的
        ButterKnife.inject(this);
        initView();

        // 获取整个应用的Application对象
        // 在不同的Activity中获取的对象是同一个
        mApplication = (MyApplication)getApplication();
        userId = mApplication.getLoginUserId();

        //接收从上一个页面传递进来的参数
        Intent intent = getIntent();
        taskid = intent.getIntExtra("taskid",0);
        typename = intent.getStringExtra("type");
        Bundle tdbundle = intent.getExtras();
        //传送过来的做任务的文件内容
        SerializableMap fileMap = (SerializableMap) tdbundle.get("filemap");
        filemap = fileMap.getMap();
        for(String labelname : filemap.keySet()){
            fileid.add(filemap.get(labelname));
            filename.add(labelname);
            Log.e("ExtractActivity---->", "GET方式请求成功，labelname--->" + labelname+filemap.get(labelname));
        }
        docId = fileid.get(0);
        downloadfilename = filename.get(0);
        //传送过来的做任务的标签内容
        inststrMap = (SerializableMap) tdbundle.get("instlabel");
        inststrmap = inststrMap.getMap();
        for(String labelname : inststrmap.keySet()){
            downloadlabel.put(inststrmap.get(labelname),labelname);
            Log.e("ExtractActivity---->", "GET方式请求成功，标签--->" + labelname+inststrmap.get(labelname));
        }
        if(typename.equals("dotask")) {
            colors = tdbundle.getStringArrayList("colors");
            colorsMap = (SerializableSortMap) tdbundle.get("colormap");
            colormap = colorsMap.getMap();
        }

        getFileContent();

        //下载文档按钮
        downloadextract = (TextView)findViewById(R.id.downloadextract);
        //是做任务页面还是查看做任务页面
        if(typename.equals("dotask")){
            downloadextract.setVisibility(View.GONE);
        }else{
            downloadextract.setVisibility(View.VISIBLE);
            downloadextract.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        CreateText();
                        //print("写入文件中的内容\n写入文件中的内容");
                        print(downloadfilecontent.toString());
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    Toast.makeText(DotaskExtractActivity.this,"点击了下载按钮",Toast.LENGTH_LONG).show();
                }
            });
        }



        mTabIndicator.setViewPager(mViewPager, 0);
        mTabIndicator.setOnPageChangeListener(new MyTabIndicator.PageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
               // Toast.makeText(DotaskExtractActivity.this, "第 " + position+" 段", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }



    @SuppressLint("NewApi")
    private void requestReadExternalPermission() {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.d("DotaskExtract---->", "READ permission IS NOT granted...");
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        } else {
            Log.d("DotaskExtract---->", "READ permission is granted...");
        }
    }

    @SuppressLint("NewApi")
    private void requestWriteExternalPermission() {
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.d("DotaskExtract---->", "WRITE permission IS NOT granted...");
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        } else {
            Log.d("DotaskExtract---->", "WRITE permission is granted...");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 0){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("DotaskExtract---->","获取到了权限");
            } else{
                // 没有获取到权限，做特殊处理
                Log.e("DotaskExtract---->","没有获取到权限");
                Toast.makeText(DotaskExtractActivity.this,"没有获取读取手机权限，请到应用中心手动打开该权限",Toast.LENGTH_SHORT).show();
            }
        }
    }

    //创建文件夹及文件
    public void CreateText() throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                //按照指定的路径创建文件夹
                file.mkdirs();
                Log.e("DotaskExtract---->", "activity中的filepath新建成功--->" + file.getAbsolutePath() + "------" );
            } catch (Exception e) {
                // TODO: handle exception
            }
        }else{
            Log.e("DotaskExtract---->", "activity中的filepath已经存在--->" + file.getAbsolutePath() + "------" );
        }
        String filename = downloadfilename.substring(0,downloadfilename.lastIndexOf("."));
        filenameTemp = filePath+filename+".txt";
        File dir = new File(filenameTemp);
        if (!dir.exists()){
            try {
                //在指定的文件夹中创建文件
                dir.createNewFile();
                Log.e("DotaskExtract---->", "activity中的filename新建成功--->" + dir.getName() + "------" );
            } catch (Exception e) {
                Log.e("DotaskExtract---->", "activity中的filename新建失败--->" + "------" );
            }
        }

    }

    //向已创建的文件中写入数据
    public void print(String str) {
        FileWriter fw = null;
        BufferedWriter bw = null;
        String datetime = "";
        try {
            SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd" + " "+"hh:mm:ss");
            datetime = tempDate.format(new java.util.Date()).toString();
            fw = new FileWriter(filenameTemp,true);
            //创建FileWriter对象，用来写入字符流
            bw = new BufferedWriter(fw); // 将缓冲对文件的输出
            //String myreadline = datetime + "[]" + str;
            String myreadline = str;
            bw.write(myreadline + "\n"); // 写入文件
            bw.newLine();
            bw.flush(); // 刷新该流的缓冲
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            try {
                bw.close();
                fw.close();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
            }
        }
    }

    public void initFragment(SerializableMap inststrMap){
        titles.clear();
        list.clear();
        for(int i=0;i<fragmentsize;i++){
            //导航栏加标题
            titles.add("第"+contentindexs.get(i)+"段");
            //titles.add("第:"+(i+1)+"段");
            DotaskExtractFragment f1 = DotaskExtractFragment.newInstance(i);
            list.add(f1);
            Bundle bundle = new Bundle();
            //传递数据
            bundle.putInt("fragmentindex",i);
            bundle.putInt("taskid", taskid);
            bundle.putInt("docid", docId);
            //是做任务页面还是查看做任务页面
            bundle.putString("type",typename);
            //将map数据添加到封装的myMap中
            //labelMap.setMap(labelmap);
            //bundle.putSerializable("lebelmap", labelMap);
            bundle.putSerializable("lebelmap", inststrMap);
            bundle.putSerializable("colormap",colorsMap);
            bundle.putStringArrayList("colors",colors);
            String contentidstr = "contentid" + i;
            Log.e("DotaskExtract---->", "activity中的contentidstr--->" + contentidstr + "------" + contentids.get(i));
            bundle.putInt("contentid" + i, contentids.get(i));
            bundle.putInt("contentindex" + i, contentindexs.get(i));
            bundle.putString("content" + i, contents.get(i));
            bundle.putString("parastatus"+ i, parastatus.get(i));

            for(int contentid:colorhashMap.keySet()){
                if(contentid==contentids.get(i)){
                    bundle.putStringArrayList("colorlist",colorhashMap.get(contentid));
                    bundle.putIntegerArrayList("beginlist",beginhashMap.get(contentid));
                    bundle.putIntegerArrayList("endlist",endhashMap.get(contentid));
                    bundle.putIntegerArrayList("labelidlist",labelidhashMap.get(contentid));
                }
            }
            //todo 获取用户ID
            bundle.putInt("userid", 1);
            list.get(i).setArguments(bundle);
        }
        mDotaskExtractAdapter = new DotaskExtractAdapter(getSupportFragmentManager(),list);
        mDotaskExtractAdapter.notifyDataSetChanged();
        mViewPager.setAdapter(mDotaskExtractAdapter);
        mTabIndicator.setTitles(titles);
    }

    private void getFileContent(){
        Log.e("mlgeb",Url.extradotaskUrl+"?docId="+docId+"&status="+docStatus+"&taskId="+taskid+"&userId="+userId);
        OkHttpUtil.sendGetRequest(Url.extradotaskUrl+"?docId="+docId+"&status="+docStatus+"&taskId="+taskid+"&userId="+userId, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DotaskExtractActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onResponse(final Call call, Response response) throws IOException {
                String temp = response.body().string();
                JSONObject jsonObject = (JSONObject) JSON.parse(temp);

                JSONArray jsonArray = (JSONArray)jsonObject.get("data");
                fragmentsize = jsonArray.size();
                //清空content
                contentids.clear();
                contents.clear();
                contentindexs.clear();
                if(jsonArray!=null && jsonArray.size()>0){
                    for(int i=0;i<jsonArray.size();i++){
                        //遍历jsonarray数组，把每一个对象转成json对象
                        JSONObject job = jsonArray.getJSONObject(i);
                        if(job.get("pid")!=null) {
                            contentid = (Integer)job.get("pid");
                            contentids.add(contentid);
                            Log.e("DotaskExtract---->", "activity中的contentID--->" + contentid);
                        }
                        if(job.get("paracontent")!=null) {
                            content = (String)job.get("paracontent").toString();
                            contents.add(content);
                            Log.e("DotaskExtract---->", "activity中的content具体内容--->" + content);
                        }
                        if(job.get("paraindex")!=null) {
                            contentindex = (Integer)job.get("paraindex");
                            contentindexs.add(contentindex);
                            Log.e("DotaskExtract---->", "activity中的content具体内容--->" + contentindex);
                        }
                        if(job.get("dtstatus")!=null) {
                            parastatusstr = (String)job.get("dtstatus");
                            parastatus.add(parastatusstr);
                            Log.e("DotaskExtract---->", "activity中的contentparastatus-->" + parastatusstr);
                        }else{
                            //不是已完成任务的状态就是空
                            parastatus.add("");
                        }

                        //已经做了的部分
                        JSONArray alreadyDone = (JSONArray)job.get("alreadyDone");

                        ArrayList<String> colorlist = new ArrayList<String>();
                        ArrayList<Integer> beginlist = new ArrayList<Integer>();
                        ArrayList<Integer> endlist = new ArrayList<Integer>();
                        ArrayList<Integer> labelidlist = new ArrayList<Integer>();
                        if(alreadyDone!=null && alreadyDone.size()>0) {
                            for (int j = 0; j < alreadyDone.size(); j++) {
                                //遍历jsonarray数组，把每一个对象转成json对象
                                JSONObject done = alreadyDone.getJSONObject(j);
                                //得到每个对象中的属性值
                                if (done.get("color") != null) {
                                    String color = (String) done.get("color");
                                    colorlist.add(color);
                                    //Log.e("DotaskExtract---->", "activity中的index_begins--->" + index_begins.get(j) + "------");
                                }
                                if (done.get("index_begin") != null) {
                                    index_begin = (Integer) done.get("index_begin");
                                    index_begins.add(index_begin);
                                    beginlist.add(index_begin);
                                    //Log.e("DotaskExtract---->", "activity中的index_begins--->" + index_begins.get(j) + "------");
                                }
                                if (done.get("index_end") != null) {
                                    index_end = (Integer) done.get("index_end");
                                    index_ends.add(index_end);
                                    endlist.add(index_end);
                                    //下载文件对应的抽取内容
                                    downloadcontent = content.substring(index_begin,index_end);
                                    //Log.e("DotaskExtract---->", "activity中的index_ends--->" + index_ends.get(j) + "------" );
                                }
                                if (done.get("label_id") != null) {
                                    label_id = (Integer) done.get("label_id");
                                    label_ids.add(label_id);
                                    labelidlist.add(label_id);
                                    //下载文件对应的标签名称
                                    downloadlabelname = downloadlabel.get(label_id);
                                    //Log.e("DotaskExtract---->", "activity中的label_id--->" + label_ids.get(j) + "------");
                                }
                                singlelinecontent = downloadfilename+"\t"+downloadlabelname+"\t"+downloadcontent+"\n";
                                downloadfilecontent.append(singlelinecontent);
                            }
                            colorhashMap.put(contentid,colorlist);
                            beginhashMap.put(contentid,beginlist);
                            endhashMap.put(contentid,endlist);
                            labelidhashMap.put(contentid,labelidlist);
                            for(int j=0;j<index_begins.size();j++){
                                Log.e("DotaskExtract---->", "activity中的index_beginsindex_ends--->" + index_begins.get(j) + "------" );
                            }
                        }
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initFragment(inststrMap);
                    }
                });
            }
        });

    }

    //和选择弹出框相关的
    private void initView() {
        //init file menu
        final ListView fileView = new ListView(this);
        fileAdapter = new ListDropDownAdapter(this, filename);
        fileView.setDividerHeight(0);
        fileView.setAdapter(fileAdapter);

        //init status menu
        final ListView statusView = new ListView(this);
        statusView.setDividerHeight(0);
        statusAdapter = new ListDropDownAdapter(this, Arrays.asList(status));
        statusView.setAdapter(statusAdapter);

        //init 确定 menu
        final ListView btnView = new ListView(this);
        btnView.setDividerHeight(0);
        btnAdapter = new ListDropDownAdapter(this, Arrays.asList(confirm));
        btnView.setAdapter(btnAdapter);

        //init popupViews
        popupViews.add(fileView);
        popupViews.add(statusView);
        popupViews.add(btnView);

        fileView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fileAdapter.setCheckItem(position);
                //mDropDownMenu.setTabText(position == 0 ? headers[0] : filename.get(position));
                mDropDownMenu.setTabText(filename.get(position));
                //if(position!=0) {
                btnfileid = fileid.get(position);
                //}
                mDropDownMenu.closeMenu();
            }
        });

        statusView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                statusAdapter.setCheckItem(position);
                //mDropDownMenu.setTabText(position == 0 ? headers[1] : status[position]);
                mDropDownMenu.setTabText(status[position]);
                //if(position!=0) {
                filestatus = status[position];
                //}
                mDropDownMenu.closeMenu();
            }
        });

        btnView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                index_begins.clear();
                index_ends.clear();
                label_ids.clear();
                if(btnfileid!=-1 && filestatus.equals("")){
                 //选了文件没选状态
                 if(btnfileid!=fileid.get(0)) {
                     docId = btnfileid;

                     //设置下载文件的文件名称
                     for(int i=0;i<fileid.size();i++){
                         if(docId==fileid.get(i)){
                             downloadfilename = filename.get(i);
                             break;
                         }
                      }
                     //请求数据
//                     new Thread(runnable).start();
//                     try {
//                         Thread.sleep(1000);
//                     }catch (InterruptedException e){
//                         e.printStackTrace();
//                     }
                     getFileContent();
 //                    initFragment(inststrMap);
                 }
                }else if(btnfileid==-1 && !filestatus.equals("")){
                    //选了状态没选文件
                    if(!filestatus.equals("全部")){
                        docStatus = filestatus;
                        //请求数据
                        getFileContent();

                    }
                }else if(btnfileid!=-1 && !filestatus.equals("")){
                    Toast.makeText(DotaskExtractActivity.this, "选择了文件：" + btnfileid+filestatus, Toast.LENGTH_SHORT).show();
                    //如果文件和文件状态都没有选择默认值
                    //if(!filestatus.equals("全部")){
                        docId = btnfileid;
                        //设置下载文件的文件名称
                        for(int i=0;i<fileid.size();i++){
                        if(docId==fileid.get(i)){
                            downloadfilename = filename.get(i);
                            break;
                        }
                        }

                        docStatus = filestatus;
                        //请求数据
                        getFileContent();
                    //}
                }
                mDropDownMenu.closeMenu();
            }
        });

        //init context view
        TextView contentView = new TextView(this);
        contentView.setHeight(0);
        mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, contentView);
    }

    @Override
    public void onBackPressed() {
        //退出activity前关闭菜单
        if (mDropDownMenu.isShowing()) {
            mDropDownMenu.closeMenu();
        } else {
            super.onBackPressed();
        }
    }

}
