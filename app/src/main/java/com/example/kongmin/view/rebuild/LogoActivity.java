package com.example.kongmin.view.rebuild;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.content.Intent;

import com.example.kongmin.model.LoginModel;
import com.example.kongmin.myapplication.R;
import com.example.kongmin.presenter.impl.LoginPresenter;
import com.example.kongmin.presenter.MyCallBack;

import com.example.kongmin.util.MyApplication;
import com.example.kongmin.view.EditTextClearTools;
import com.hb.dialog.dialog.LoadingDialog;

/**
 * reviewed by mwx  on 2020/01/15.
 * 登录界面
 */
public class LogoActivity extends AppCompatActivity implements MyCallBack {

    private EditText etUserName;
    private EditText etUserPassword;
    private ImageView unameClear;
    private ImageView pwdClear;
    private Button btnLogin;
    private TextView user_register;
    private String userName;
    private String userPassword;

    private MyApplication mApplication;

    private static final int request_success = 1;
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,
    Manifest.permission.GET_ACCOUNTS,Manifest.permission.READ_CONTACTS,Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS};

    private LoginPresenter loginPresenter;

    public LoginPresenter initPresenter() {
        return new LoginPresenter(new LoginModel(mApplication,this));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_logo_activity);
        // 获取整个应用的Application对象,在不同的Activity中获取的对象是同一个
        mApplication = (MyApplication)getApplication();
        //初始化P层和M层和视图组件
        init();

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        loadingDialog.dismiss();
    }

    private void init(){
        //init presenter
        this.loginPresenter = initPresenter();
        //init view
        etUserName = (EditText) findViewById(R.id.et_userName);
        etUserPassword = (EditText) findViewById(R.id.et_password);
        unameClear = (ImageView) findViewById(R.id.iv_unameClear);
        pwdClear = (ImageView) findViewById(R.id.iv_pwdClear);
        user_register = (TextView) findViewById(R.id.user_register);
        //清除用户名和密码的方法
        EditTextClearTools.addClearListener(etUserName,unameClear);
        EditTextClearTools.addClearListener(etUserPassword,pwdClear);

        //加载弹窗
        loadingDialog = new LoadingDialog(this);
        //点击用户注册按钮
        user_register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 给btnLogin添加点击响应事件
                Intent intent = new Intent(LogoActivity.this, RegisterActivity.class);
                //启动
                startActivity(intent);
            }
        });

        btnLogin=(Button)findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = etUserName.getText().toString();
                userPassword = etUserPassword.getText().toString();
                if (userName.equals("")||userPassword.equals("")) {
                    //Toast.makeText(LogoActivity.this,"账号或密码为空",Toast.LENGTH_SHORT).show();
                    loading();
                    test();
                    return;
                }
                Toast.makeText(LogoActivity.this,"登录中....",Toast.LENGTH_SHORT).show();
                loginPresenter.login(userName,userPassword,LogoActivity.this);
            }
        });

    }

    private  void test(){
        Intent intent = new Intent(LogoActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private LoadingDialog loadingDialog;
    private void loading(){
        loadingDialog.setMessage("loading");
        loadingDialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //检查权限
        this.checkPermissions();
    }

    @Override
    public void onResume() {
        super.onResume();
       loginPresenter.login(this);
    }



    /**
     * 动态检查权限
     */
    private void checkPermissions(){
        for (String permission: permissions){
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{permission},
                        request_success);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case request_success: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //授权成功，直接操作
                    this.checkPermissions();
                } else {
                    //禁止授权
                }
            }
        }
    }

    @Override
    public void onSuccess() {
        Intent intent = new Intent(LogoActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onFailure(String msg) {
        Toast.makeText(LogoActivity.this,msg,Toast.LENGTH_SHORT).show();
    }
}