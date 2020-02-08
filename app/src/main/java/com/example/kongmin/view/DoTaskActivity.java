package com.example.kongmin.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.kongmin.myapplication.R;
import com.example.kongmin.util.HttpUtil;
import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;
import android.text.TextPaint;
import android.graphics.Color;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;

import java.util.List;

import com.example.kongmin.pojo.Content;
import java.util.Map;
import java.util.HashMap;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */

//文本的做任务界面
public class DoTaskActivity extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    private TextView title;
    private TextView content;
    private int taskid;
    private int fileid;
    private String filename;
    private String filecontent;
    private SpannableStringBuilder spannableString;

    private LinearLayout ll;
    private List<Content> contentList;//放置标题的集合

    int id = 0;
    String text = "";
    private View view;
    LayoutInflater inflater;

    Map<Integer,String> hashmapcontent = new HashMap<Integer,String>();
    Map<Integer,String> hashmaplabel = new HashMap<Integer,String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_do_task);

        mVisible = true;

        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.doccontent);
        //mContentView = findViewById(R.id.lay);



        //view = inflater.inflate(R.layout.do_task_listview, null);//注意导包，别导系统包


        //title.setId(Integer.valueOf(9));

        String[] data = { "标签", "分类", "两个文本", "多文本"};

       /* ArrayAdapter<String> adapter = new ArrayAdapter<String>(DoTaskActivity.this,R.layout.do_task_listview,data);
        ListView lv =(ListView) findViewById(R.id.lv_bwlList);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), "position"+position, Toast.LENGTH_SHORT).show();

            }
        });*/

        //View layout = LayoutInflater.from(getApplicationContext()).inflate(R.layout.do_task_listview,null);//布局打气筒获取单行对象
        //TextView name = (TextView) layout.findViewById(R.id.name);
        //content = (TextView) layout.findViewById(R.id.text2);
        //ll = (LinearLayout) findViewById(R.id.lay);

        /* int i = 9;
        //把数据显示至屏幕
        for (String p : data) {
            //1.集合中每有一条元素，就new一个textView
            TextView tv = new TextView(this);
            //2.把信息设置为文本框的内容
            tv.setText(p);
            text = tv.getText().toString();
            tv.setId(Integer.valueOf(i));
            i++;
            id = tv.getId();
            tv.setTextSize(20);
            tv.setKeepScreenOn(true);
            tv.setTextIsSelectable(true);
            //3.把textView设置为线性布局的子节点
            ll.addView(tv);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), text+id, Toast.LENGTH_SHORT).show();
                }
            });
        }*/




        title =  findViewById(R.id.doctitle);
        content = findViewById(R.id.doccontent);

        //content = view.findViewById(R.id.text2);
       /* content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), content.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });*/

        //接收从上一个页面传递进来的参数
        Intent intent = getIntent();
        taskid = intent.getIntExtra("taskid",0);
        fileid = intent.getIntExtra("fileid",0);
        filename = intent.getStringExtra("filename");
        Toast.makeText(getApplicationContext(), "提示：点击窗口外部关闭窗口！"+filename,
                Toast.LENGTH_SHORT).show();

        //给文件名赋值
        title.setText(filename);


        // Set up the user interaction to manually show or hide the system UI.
       /* mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });
        mControlsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });*/

        new Thread(runnable).start();
        //new Thread(runnable2).start();
        try {
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        content.setText(filecontent);
        content.setCustomSelectionActionModeCallback(new MyActionModeCallback());

        //initView();
        //initData();

        spannableString = new SpannableStringBuilder();
        spannableString.append(content.getText().toString());

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        //findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);
    }




   /* private void initView() {
        //要添加view的容器
        ll = (LinearLayout) findViewById(R.id.lay);
        contentList = new ArrayList<Content>();
    }*/

    /**
     * 处理数据,可以是服务器请求过来的,也可以是本地的
     */
    private void initData() {
        for (Integer contentid:hashmapcontent.keySet()) {
            Content content = new Content(contentid,hashmapcontent.get(contentid));
            contentList.add(content);
        }
        //数据拿到之后去根据数据去动态添加View
        addView();
    }

    /**
     * 动态添加的具体实现
     */
    private void addView() {
        //ivList集合有几个元素就添加几个
        for (int i = 0; i < contentList.size(); i++) {
            //首先引入要添加的View
            View view = View.inflate(this, R.layout.do_task_content, null);
            //找到里面需要动态改变的控件
            //ImageView ivLogo = (ImageView) view.findViewById(R.id.iv_logo);
            //TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
             content = (TextView) view.findViewById(R.id.doccontent);
            //给控件赋值
            //ivLogo.setImageResource(ivList.get(i));
            content.setText(contentList.get(i).getContentname());
            //content.setText(filecontent);
            content.setCustomSelectionActionModeCallback(new MyActionModeCallback());

            /*
            动态给每个View设置margin,也可以在xml里面设置,xml里面设置后每个view之间的间距都是一样的
            动态设置可以给每个view之间的间距设置的不一样 params.setMargins(int left, int top, int right, int bottom);
             */
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
//                    (LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
//            view.setLayoutParams(params);
//            params.setMargins(24,0,24,0);
//            view.setTag(i);
            //设置每个View的点击事件
            final int finalI = i;//由于OnClick里面拿不到i,所以需要重新赋值给一个final对象
            content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(DoTaskActivity.this, "点击了"+contentList.get(finalI).getContentname(), Toast.LENGTH_SHORT).show();
                }
            });
            //把所有动态创建的view都添加到容器里面
            ll.addView(view);
        }

    }

    private int selectionStart = 0;
    private int selectionEnd = 0;
    private String substring ="";
    private int lid = 0;
    public void dodelete(int labelid,String label){
        //获取选中的起始位置
        selectionStart = content.getSelectionStart();
        selectionEnd = content.getSelectionEnd();
        //Log.i(TAG,"selectionStart="+selectionStart+",selectionEnd="+selectionEnd);
        //截取选中的文本
        //String txt = content.getText().toString();
        String txt = content.getText().toString();
        substring = txt.substring(selectionStart, selectionEnd);
        //Log.i(TAG,"substring="+substring);
       /* SpannableStringBuilder spanText=new SpannableStringBuilder(content.getText().toString());
        spanText.setSpan(new ClickableSpan() {

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.RED);       //设置文件颜色
                ds.setUnderlineText(true);      //设置下划线
            }

            @Override
            public void onClick(View view) {
                final Snackbar snackbar = Snackbar.make(content,"别点我",Snackbar.LENGTH_LONG);
                snackbar.show();
                snackbar.setAction("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
            }
        }, selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);*/
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DoTaskActivity.this, "请不要点我+substring="+substring, Toast.LENGTH_SHORT).show();
                spannableString.setSpan(clickableSpannoline, selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            }
        };
        spannableString.setSpan(clickableSpan, selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        //spannableString.removeSpan(clickableSpan);
        content.setText(spannableString);
        content.setMovementMethod(LinkMovementMethod.getInstance());

        Toast.makeText(getApplicationContext(), "点击了标注"+labelid+label+"selectionStart="+selectionStart+
                ",selectionEnd="+selectionEnd+"substring="+substring, Toast.LENGTH_SHORT).show();
        lid = labelid;
        new Thread(runnable3).start();


    }

    private Runnable runnable3 = new Runnable() {
        @Override
        public void run() {
            // TODO: http request.
            //String requestUrl = "http://10.0.2.2:8080/dotask/addDoTask?";
            String requestUrl = "http://172.20.10.5:8080/dotask/addDoTask?";
            //要传递的数
            String params ="taskid="+taskid+"&contentid=1"+"&labelId="+lid+"&conbegin="+selectionStart+"&conend="+selectionEnd;
            Log.e("params---->", "Post方式请求成功，params--->" + params);
            Log.e("taskid---->", "Post方式请求成功，addtaskresult--->" + taskid);
            String result = HttpUtil.requestPost(requestUrl,params);
            Log.e("listview---->", "Post方式请求成功，result--->" + result);
        }
    };









    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // TODO: http request.

            //String requestUrl = "http://10.0.2.2:8080/content/getContent?";
            String requestUrl = "http://172.20.10.5:8080/content/getContent?";
            //要传递的数
            String params ="docId="+fileid;
            Log.e("docId---->", "Post方式请求成功，result--->" + fileid);
            String result = HttpUtil.requestGet(requestUrl,params);
            Log.e("listview---->", "Post方式请求成功，result--->" + result);
            JSONObject jsonObject= (JSONObject) JSON.parse(result);
            StringBuffer sb = new StringBuffer();
            if (jsonObject.getString("data")!=null) {
                Log.e("data---->", "Post方式请求成功，data--->" + jsonObject.getString("data"));
                // 首先把字符串转成 JSONArray 对象
                JSONArray jsonArray = (JSONArray) JSON.parseArray(jsonObject.getString("data"));
                if(jsonArray.size()>0){
                    for(int i=0;i<jsonArray.size();i++){
                        //遍历jsonarray数组，把每一个对象转成json对象
                        JSONObject job = jsonArray.getJSONObject(i);
                        //得到每个对象中的属性值
                        Log.e("size---->", "Post方式请求成功，size--->" + jsonArray.size());
                        if(job.get("cid")!=null) {
                            Log.e("cid---->", "Post方式请求成功，cid--->" + job.get("cid").toString());
                        }
                        if(job.get("paracontent")!=null) {
                            Log.e("paracontent---->", "Post方式请求成功，paracontent--->" + job.get("paracontent").toString());
                            sb.append(job.get("paracontent").toString()+"\n");
                            hashmapcontent.put((Integer)job.get("cid"),job.get("paracontent").toString());
                        }
                    }
                }
            }
            filecontent = sb.toString();



            //第二个请求开始
            String requestUrl2 = "http://10.0.2.2:8080//label/getLabelByTask?";
            //要传递的数
            String params2 ="taskid="+taskid;
            Log.e("taskid---->", "Post方式请求成功，taskid--->" + taskid);
            String result2 = HttpUtil.requestGet(requestUrl2,params2);
            Log.e("listview---->", "Post方式请求成功，result2--->" + result2);
            JSONObject jsonObject2= (JSONObject) JSON.parse(result2);
            StringBuffer sb2 = new StringBuffer();
            if (jsonObject2.getString("label")!=null) {
                Log.e("label---->", "Post方式请求成功，label--->" + jsonObject2.getString("label"));
                // 首先把字符串转成 JSONArray 对象
                JSONArray jsonArray2 = (JSONArray) JSON.parseArray(jsonObject2.getString("label"));
                if (jsonArray2!=null && jsonArray2.size() > 0) {
                    for (int i = 0; i < jsonArray2.size(); i++) {
                        //遍历jsonarray数组，把每一个对象转成json对象
                        JSONObject job2= jsonArray2.getJSONObject(i);
                        //得到每个对象中的属性值
                        Log.e("size---->", "Post方式请求成功，size--->" + jsonArray2.size());
                        if (job2.get("lid") != null) {
                            Log.e("lid---->", "Post方式请求成功，lid--->" + job2.get("lid").toString());
                            sb2.append(job2.get("lid").toString() + "\n");

                        }
                        if (job2.get("labelname") != null) {
                                Log.e("labelname---->", "Post方式请求成功，labelname--->" + job2.get("labelname").toString());
                            sb2.append(job2.get("labelname").toString() + "\n");
                            hashmaplabel.put((Integer)job2.get("lid"),job2.get("labelname").toString());
                        }

                    }
                }
                //filecontent+= sb2.toString();
                //Toast.makeText(getApplicationContext(), ""+sb2.toString(),
                //        Toast.LENGTH_LONG).show();
            }
            //第二个请求结束
        }
    };



   /* private Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
            // TODO: http request.

        }
    };*/

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        //mHideHandler.removeCallbacks(mHideRunnable);
        //mHideHandler.postDelayed(mHideRunnable, delayMillis);
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }




    ClickableSpan clickableSpannoline = new ClickableSpan() {
        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            //ds.setColor(Color.RED);       //设置文件颜色
            //ds.setUnderlineText(true);      //设置下划线
            //ds.setColor(ds.linkColor);
            ds.setColor(Color.BLACK);
            ds.setUnderlineText(false);
        }
        @Override
        public void onClick(View view) {
        }
    };



    //和菜单有关的
    private class MyActionModeCallback implements ActionMode.Callback {
        private Menu mMenu;

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            MenuInflater menuInflater = actionMode.getMenuInflater();
            menu.clear();

            for(Integer labelid:hashmaplabel.keySet()){
                menu.add(Menu.NONE, Menu.FIRST + labelid, Menu.FIRST + labelid,hashmaplabel.get(labelid));
            }

            /*menu.add(Menu.NONE, Menu.FIRST + 1, 5, "删除").setIcon(

                    android.R.drawable.ic_menu_delete);
            menu.add(Menu.NONE, Menu.FIRST + 2, 2, "保存").setIcon(

                    android.R.drawable.ic_menu_edit);

            menu.add(Menu.NONE, Menu.FIRST + 3, 6, "帮助").setIcon(

                    android.R.drawable.ic_menu_help);

            menu.add(Menu.NONE, Menu.FIRST + 4, 1, "添加").setIcon(

                    android.R.drawable.ic_menu_add);

            menu.add(Menu.NONE, Menu.FIRST + 5, 4, "详细").setIcon(

                    android.R.drawable.ic_menu_info_details);

            menu.add(Menu.NONE, Menu.FIRST + 6, 3, "发送").setIcon(

                    android.R.drawable.ic_menu_send);*/
            //menuInflater.inflate(R.menu.textmenu,menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            //菜单创建完成以后获取到其对象，便于后续操作
            this.mMenu=menu;
            return true;
        }


        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            for(Integer labelid:hashmaplabel.keySet()){
                if(menuItem.getItemId()==Menu.FIRST + labelid){
                   String label =  hashmaplabel.get(labelid);
                    dodelete(labelid,label);
                    break;
                }
            }
            /*switch (menuItem.getItemId()){
                case Menu.FIRST + 1:
                    //全选
                    //mContentView.selectAll();
                    dodelete();
                    break;
                case R.id.it_copy:
                    //String selectText = getSelectText(SelectMode.COPY);
                    //setText(selectText)是为了后面的this.mMenu.close()起作用
                    //mTvSelect.setText(selectText);
                    Toast.makeText(getApplicationContext(), "选中的内容已复制到剪切板", Toast.LENGTH_SHORT).show();
                    this.mMenu.close();
                    break;
                case R.id.it_cut:
                    //剪切
                    //String txt = getSelectText(SelectMode.CUT);
                    //mTvSelect.setText(txt);
                    Toast.makeText(getApplicationContext(), "选中的内容已剪切到剪切板", Toast.LENGTH_SHORT).show();
                    this.mMenu.close();
                    break;

                case R.id.it_paste:
                    //获取剪切班管理者
                    //ClipboardManager cbs = (ClipboardManager) mContext.getSystemService(CLIPBOARD_SERVICE);
                    //if (cbs.hasPrimaryClip()){
                    //    mTvSelect.setText(cbs.getPrimaryClip().getItemAt(0).getText());
                    //}
                    this.mMenu.close();
                    break;
            }*/
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {

        }
    }



}
