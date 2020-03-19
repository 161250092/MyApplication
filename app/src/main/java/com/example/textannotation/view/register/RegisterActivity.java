package com.example.textannotation.view.register;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.example.textannotation.model.RegisterModel;
import com.example.textannotation.myapplication.R;
import com.example.textannotation.presenter.IRegisterPresenter;
import com.example.textannotation.presenter.MyCallBack;
import com.example.textannotation.presenter.impl.RegisterPresenter;
import com.example.textannotation.view.login.LoginActivity;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;

/**
 * Created by kongmin on 2019/02/11.
 * 注册界面
 */
public class RegisterActivity extends AppCompatActivity implements MyCallBack {

    private EditText username;
    private EditText email;
    private EditText password;
    private EditText passwordconfirm;
    private Button btnsure;

    private String namestr;
    private String emailstr;
    private String passwordstr;
    private String passwordcstr;

    private IRegisterPresenter iRegisterPresenter;

    private void initPresenter(){
        iRegisterPresenter = new RegisterPresenter(new RegisterModel());
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    public void init(){
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        passwordconfirm = findViewById(R.id.passwordconfirm);
        btnsure =  findViewById(R.id.btn_sure);
        initPresenter();

        btnsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                namestr = username.getText().toString();
                emailstr = email.getText().toString();
                passwordstr = password.getText().toString();
                passwordcstr = passwordconfirm.getText().toString();

                if(namestr.isEmpty() || emailstr.isEmpty() || passwordstr.isEmpty() || passwordcstr.isEmpty()){
                    Toast.makeText(RegisterActivity.this,"用户名、邮箱或密码为空，请仔细检查并重新填写！",Toast.LENGTH_LONG).show();
                }else if(!passwordstr.equals(passwordcstr)){
                    Toast.makeText(RegisterActivity.this,"密码和确认密码不匹配，请仔细检查并重新填写密码！",Toast.LENGTH_LONG).show();
                }else{
                   iRegisterPresenter.registerNewUser(namestr,passwordcstr,emailstr,RegisterActivity.this);
                }
            }
        });
    }

    @Override
    public void onSuccess() {
        new XPopup.Builder(this).asConfirm("","注册成功，请登录", new OnConfirmListener() {
            @Override
            public void onConfirm() {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }).show();


    }

    @Override
    public void onFailure(String msg) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RegisterActivity.this,"网络不佳，请确认网络连接情况",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
