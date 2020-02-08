package com.example.kongmin.managetask;

import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.kongmin.Constant.Constant;
import com.example.kongmin.myapplication.R;
import com.example.kongmin.view.adapter.TaskListItemAdapter;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.TypedValue;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.kongmin.pojo.MarkCategory1;
import com.example.kongmin.util.HttpUtil;
import com.example.kongmin.util.MyApplication;
import android.app.AlertDialog;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface;


import java.util.ArrayList;
import java.util.List;

/**
 * SwipeMenuListView
 * Created by baoyz on 15/6/29.
 * 我发布的任务列表页面
 */

public class SimpleActivity extends AppCompatActivity  {

    //private List<ApplicationInfo> mAppList;
    //private AppAdapter mAdapter;
    private TaskListItemAdapter mAdapter;
    private SwipeMenuListView mListView;

    private LayoutInflater inflater;
    private ArrayList<MarkCategory1> array = new ArrayList<MarkCategory1>();

    private int tasktype = 1;

    private MyApplication mApplication;
    private int userId;

    private int taskId;
    private int typeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);

        // 获取整个应用的Application对象
        // 在不同的Activity中获取的对象是同一个
        mApplication = (MyApplication)getApplication();
        // 获取整个应用的Application对象
        // 在不同的Activity中获取的对象是同一个
        mApplication = (MyApplication)getApplication();
        userId = mApplication.getLoginUserId();

        //mAppList = getPackageManager().getInstalledApplications(0);

        mListView = (SwipeMenuListView) findViewById(R.id.listView);

        new Thread(runnable).start();
        try {
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        //mAdapter = new AppAdapter();
        inflater=getLayoutInflater();
        /*MarkCategory1 cun1 = new MarkCategory1(11,"mingchne","opp","ohppp","opnkmkk");
        MarkCategory1 cun2 = new MarkCategory1(11,"mingchne","opp","ohppp","opnkmkk");
        MarkCategory1 cun3 = new MarkCategory1(11,"mingchne","opp","ohppp","opnkmkk");
        MarkCategory1 cun4 = new MarkCategory1(11,"mingchne","opp","ohppp","opnkmkk");
        array.add(cun1);
        array.add(cun2);
        array.add(cun3);
        array.add(cun4);*/

        mAdapter = new TaskListItemAdapter(inflater,array);
        mListView.setAdapter(mAdapter);

        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(dp2px(90));
                // set item title
                openItem.setTitle("Open");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        mListView.setMenuCreator(creator);

        // step 2. listener item click event
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                //ApplicationInfo item = mAppList.get(position);
                MarkCategory1 item = array.get(position);
                switch (index) {
                    case 0:
                        // open
                        //open(item);
                        break;
                    case 1:
                        // delete
                        //delete(item);
                        //deletetask(position);
                        //mAppList.remove(position);
                        //todo position从1开始
                        array.remove(position);
                        mAdapter.notifyDataSetChanged();
                        break;
                }
                return false;
            }
        });

        // set SwipeListener
        mListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });

        // set MenuStateChangeListener
        mListView.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
            @Override
            public void onMenuOpen(int position) {
            }

            @Override
            public void onMenuClose(int position) {
            }
        });

        // other setting
//		listView.setCloseInterpolator(new BounceInterpolator());

        // test item long click
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                Toast.makeText(getApplicationContext(), position + " long click", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), position + " long click", Toast.LENGTH_SHORT).show();
                //Intent intent=new Intent(SimpleActivity.this,MypubDolistActivity.class);
                /*intent.putExtra("ids",array.get(position).getIds());
                String typename = array.get(position).getType();*/
                String typename = array.get(position).getType();

                //todo position从1开始
                //intent.putExtra("taskid",array.get(position).getIds());

                if (typename.equals("信息抽取")){//浅粉色
                    Intent intent=new Intent(SimpleActivity.this,MypubDolistActivity.class);
                    //todo position从1开始
                    intent.putExtra("tasktype",1);
                    intent.putExtra("taskid",array.get(position).getIds());
                    startActivity(intent);
                }else if (typename.equals("文本分类")){//浅黄色
                    Intent intent=new Intent(SimpleActivity.this,MypubDolistActivity.class);
                    //todo position从1开始
                    intent.putExtra("tasktype",2);
                    intent.putExtra("taskid",array.get(position).getIds());
                    startActivity(intent);
                }else if (typename.equals("文本关系")){//浅绿色
                    Intent intent=new Intent(SimpleActivity.this,MypubDolistActivity.class);
                    //todo position从1开始
                    intent.putExtra("tasktype",3);
                    intent.putExtra("taskid",array.get(position).getIds());
                    startActivity(intent);
                }else if (typename.contains("文本配对")){//浅蓝色
                    Intent intent=new Intent(SimpleActivity.this,MypubDolistActivity.class);
                    //todo position从1开始
                    intent.putExtra("tasktype",4);
                    intent.putExtra("taskid",array.get(position).getIds());
                    startActivity(intent);
                }else if (typename.equals("文本排序")){//浅紫色
                    Intent intent=new Intent(SimpleActivity.this,MypubDolistActivity.class);
                    //todo position从1开始
                    intent.putExtra("tasktype",5);
                    intent.putExtra("taskid",array.get(position).getIds());
                    startActivity(intent);
                }else if (typename.equals("文本类比排序")){//浅橘色
                    Intent intent=new Intent(SimpleActivity.this,MypubDolistActivity.class);
                    //todo position从1开始
                    intent.putExtra("tasktype",6);
                    intent.putExtra("taskid",array.get(position).getIds());
                    startActivity(intent);
                }
                //startActivity(intent);
            }
        });

    }



    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // TODO: http request.
            String requestUrl = Constant.TMshowmypublistUrl;
            int page = 1;
            int limit = Integer.MAX_VALUE;
            String params = "?page="+page+"&limit="+limit+"&userId="+userId;
            String result = HttpUtil.requestGet(requestUrl,params);
            int  id = 0;
            String title = "";
            String createtime = "";
            String taskcompstatus = "";
            String typename="";
            JSONObject jsonObject = JSONObject.parseObject(result);
            //把字符串转成JSONArray对象
            JSONArray jsonArray = (JSONArray)jsonObject.get("data");
            if(jsonArray.size()>0){
                for(int i=0;i<jsonArray.size();i++){
                    //遍历jsonarray数组，把每一个对象转成json对象
                    JSONObject job = jsonArray.getJSONObject(i);
                    //得到每个对象中的属性值
                    //Log.e("size---->", "Post方式请求成功，size--->" + jsonArray.size());
                    if(job.get("tid")!=null) {
                        //Log.e("tid---->", "Post方式请求成功，tid--->" + job.get("tid").toString());
                        id =Integer.valueOf(job.get("tid").toString());
                    }
                    if(job.get("title")!=null) {
                        //Log.e("title---->", "Post方式请求成功，title--->" + job.get("title").toString());
                        title = job.get("title").toString();
                    }
                    if(job.get("typeName")!=null) {
                        //Log.e("title---->", "Post方式请求成功，title--->" + job.get("title").toString());
                        typename = job.get("typeName").toString();
                    }
                    if(job.get("createtime")!=null) {
                        //Log.e("createtime---->", "Post方式请求成功，createtime--->" + job.get("createtime").toString());
                        createtime = job.get("createtime").toString();
                    }
                    if(job.get("taskcompstatus")!=null) {
                        //Log.e("taskcompstatus---->", "Post方式请求成功，taskcompstatus--->" + job.get("taskcompstatus").toString());
                        taskcompstatus = job.get("taskcompstatus").toString();
                    }
                    MarkCategory1 cun1 = new MarkCategory1(id,title,taskcompstatus,typename,createtime);;
                    array.add(cun1);
                    title = "";
                    createtime = "";
                    taskcompstatus = "";
                    typename="";
                }
            }
            Integer num = (Integer)jsonObject.get("count");
            Log.e("count---->", "Post方式请求成功，count--->" + num);
        }
    };


    public void deletetask(final int position){
        // 创建构建器
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 设置参数
        builder.setTitle("请做出选择").setIcon(R.drawable.ic_launcher)
                .setMessage("确定删除任务？")
                .setPositiveButton("是", new OnClickListener() {// 积极
                    @Override
                    public void onClick(DialogInterface dialog,int which) {
                        //todo position从1开始
                        taskId =  array.get(position).getIds();
                        String typename = array.get(position).getType();
                        int index = position;
                        //todo position从1开始
                        //intent.putExtra("taskid",array.get(position).getIds());
                        if (typename.equals("信息抽取")){//浅粉色
                            typeId = 1;
                        }else if (typename.equals("文本分类")){//浅黄色
                            typeId = 2;
                        }else if (typename.equals("文本关系")){//浅绿色
                            typeId = 3;
                        }else if (typename.contains("文本配对")){//浅蓝色
                            typeId = 4;
                        }else if (typename.equals("文本排序")){//浅紫色
                            typeId = 5;
                        }else if (typename.equals("文本类比排序")){//浅橘色
                            typeId = 6;
                        }
                        Log.e("tag", "position-->>" + position);
                        //new Thread(deleterunnable).start();
                        Toast.makeText(SimpleActivity.this, "ddd"+index+taskId+typename, Toast.LENGTH_LONG).show();
                        //Toast.makeText(SimpleActivity.this, position, Toast.LENGTH_LONG).show();
                        // TODO Auto-generated method stub
                    }
                }).setNegativeButton("否", new OnClickListener() {// 消极
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                Toast.makeText(SimpleActivity.this, "取消删除", Toast.LENGTH_LONG).show();
            }
        });
                /* .setNeutralButton("不知道", new OnClickListener() {// 中间级
                     @Override
                      public void onClick(DialogInterface dialog, int which) {
                         // TODO Auto-generated method stub
                         Toast.makeText(SimpleActivity.this, "快睁开眼瞅瞅", Toast.LENGTH_LONG).show();
                }
                });*/
        builder.create().show();
    }

    private Runnable deleterunnable = new Runnable() {
        @Override
        public void run() {
            // TODO: http request.
            String requestUrl = Constant.deletetaskUrl;
            requestUrl = requestUrl+"/"+taskId+"/"+typeId;
            String params="";
            Log.e("tag", "addtaskresultnew-->>" + requestUrl);
            String result = HttpUtil.requestDelete(requestUrl,params);
            Log.e("tag", "addtaskresultnew-->>" + result);
            JSONObject jsonObject= (JSONObject) JSON.parse(result);
            String data = (String) jsonObject.getString("msg");
            Integer status = (Integer) jsonObject.getInteger("code");
            if(data.equals("success")||status==0){
                Looper.prepare();
                Toast.makeText(SimpleActivity.this, "删除成功", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }
    };




    private void delete(ApplicationInfo item) {
        // delete app
        try {
            Intent intent = new Intent(Intent.ACTION_DELETE);
            intent.setData(Uri.fromParts("package", item.packageName, null));
            startActivity(intent);
        } catch (Exception e) {
        }
    }

    private void open(ApplicationInfo item) {
        // open app
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(item.packageName);
        List<ResolveInfo> resolveInfoList = getPackageManager()
                .queryIntentActivities(resolveIntent, 0);
        if (resolveInfoList != null && resolveInfoList.size() > 0) {
            ResolveInfo resolveInfo = resolveInfoList.get(0);
            String activityPackageName = resolveInfo.activityInfo.packageName;
            String className = resolveInfo.activityInfo.name;

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName componentName = new ComponentName(
                    activityPackageName, className);

            intent.setComponent(componentName);
            startActivity(intent);
        }
    }

    /*class AppAdapter extends BaseSwipListAdapter {

        @Override
        public int getCount() {
            return mAppList.size();
        }

        @Override
        public ApplicationInfo getItem(int position) {
            return mAppList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(),
                        R.layout.item_list_app, null);
                new ViewHolder(convertView);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            ApplicationInfo item = getItem(position);
            holder.iv_icon.setImageDrawable(item.loadIcon(getPackageManager()));
            holder.tv_name.setText(item.loadLabel(getPackageManager()));
            holder.iv_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(SimpleActivity.this, "iv_icon_click", Toast.LENGTH_SHORT).show();
                }
            });
            holder.tv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(SimpleActivity.this,"iv_icon_click",Toast.LENGTH_SHORT).show();
                }
            });
            return convertView;
        }

        class ViewHolder {
            ImageView iv_icon;
            TextView tv_name;

            public ViewHolder(View view) {
                iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
                tv_name = (TextView) view.findViewById(R.id.tv_name);
                view.setTag(this);
            }
        }

        @Override
        public boolean getSwipEnableByPosition(int position) {
            if(position % 2 == 0){
                return false;
            }
            return true;
        }
    }*/

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_left) {
            mListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
            return true;
        }
        if (id == R.id.action_right) {
            mListView.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}