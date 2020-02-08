package com.example.kongmin.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.kongmin.Constant.Constant;
import com.example.kongmin.myapplication.R;
import com.example.kongmin.pojo.ShowFile;
import com.example.kongmin.util.HttpUtil;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FileDetailActivity extends AppCompatActivity {
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

    private int fileid;
    private String filename;
    private TextView filenametext;
    private TextView contentview;
    private String filecontent;

    private int tasktype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_file_detail);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);

        contentview =  findViewById(R.id.fullscreen_content);
        filenametext = findViewById(R.id.filetitle);

        //接收从上一个页面传递进来的参数
        Intent intent = getIntent();
        tasktype = intent.getIntExtra("tasktype",0);
        fileid = intent.getIntExtra("ids",0);
        filename = intent.getStringExtra("filename");

        Toast.makeText(getApplicationContext(), "提示：点击窗口外部关闭窗口！"+fileid+filename, Toast.LENGTH_SHORT).show();

        //给文件名赋值
        filenametext.setText(filename);

        mContentView.setOnClickListener(new View.OnClickListener() {
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
        });

        new Thread(runnable).start();
        try {
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        contentview.setText(filecontent);

        // Set up the user interaction to manually show or hide the system UI.




        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        //findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);
    }



    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // TODO: http request.
            //String requestUrl = "http://10.0.2.2:8080/content/getContent?";
            String requestUrl = Constant.extractfilecontentUrl;
            //要传递的数
            String params ="?docId="+fileid;
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
                        if(job.get("paracontent")!=null) {
                            Log.e("paracontent---->", "Post方式请求成功，paracontent--->" + job.get("paracontent").toString());
                            sb.append(job.get("paracontent").toString()+"\n");
                        }
                    }
                }
            }
            filecontent = sb.toString();
        }
    };







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
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
