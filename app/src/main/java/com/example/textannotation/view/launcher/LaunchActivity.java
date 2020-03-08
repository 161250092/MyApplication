package com.example.textannotation.view.launcher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.example.textannotation.model.LoginModel;
import com.example.textannotation.myapplication.R;
import com.example.textannotation.presenter.MyCallBack;
import com.example.textannotation.presenter.impl.LoginPresenter;
import com.example.textannotation.util.MyApplication;
import com.example.textannotation.view.login.LoginActivity;
import com.example.textannotation.view.mainPage.MainActivity;


public class LaunchActivity extends Activity implements MyCallBack {

    private static final int START_COUNTING = 1;
    private static final int COUNT_NUMBER = 2;

    private TextView mCount;

    private LoginPresenter loginPresenter;
    private MyApplication mApplication;
    private MyHandler mHandler = new MyHandler();

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_launcher);
        mApplication = (MyApplication)getApplication();
        this.loginPresenter = initPresenter();
        mCount = findViewById(R.id.count);


    }

    @Override
    protected void onResume() {
        super.onResume();
        Message msg = mHandler.obtainMessage();
        msg.what = START_COUNTING;
        msg.obj = COUNT_NUMBER;
        mHandler.sendMessageDelayed(msg, 0);


    }



    public LoginPresenter initPresenter() {
        return new LoginPresenter(new LoginModel(mApplication,this));
    }

    @Override
    public void onSuccess() {
        Intent intent = new Intent(LaunchActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onFailure(String msg) {
        Intent intent = new Intent(LaunchActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case START_COUNTING:
                    int count = (int) msg.obj;
                    mCount.setText(count + "秒后跳转");
                    if (count > 0) {
                        Message msg1 = obtainMessage();
                        msg1.what = START_COUNTING;
                        msg1.obj = count - 1;
                        sendMessageDelayed(msg1, 1000);
                    } else if (count == 0 ) {
                        loginPresenter.login(LaunchActivity.this);
                    }
                    break;
                default:
                    break;
            }
        }
    };

}
