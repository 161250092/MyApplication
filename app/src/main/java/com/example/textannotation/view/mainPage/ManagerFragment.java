package com.example.textannotation.view.mainPage;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.textannotation.model.login.LoginModel;
import com.example.textannotation.myapplication.R;
import com.example.textannotation.view.common.AvatarImageView;
import com.example.textannotation.view.myTasks.MyTaskListActivity;
import com.example.textannotation.pojo.login.User;
import com.example.textannotation.util.MyApplication;
import com.example.textannotation.view.login.LoginActivity;
import com.hb.dialog.dialog.LoadingDialog;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;

public class ManagerFragment extends Fragment {

    MyApplication myApplication;
    TextView userId_text;
    TextView userAccount_text;
    RelativeLayout my_task_line;
    RelativeLayout my_logout;

    LoadingDialog loadingDialog;

    LoginModel loginModel;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.publish_tab, container, false);
        userId_text = view.findViewById(R.id.user_id);
        userAccount_text = view.findViewById(R.id.user_account);
        my_task_line = view.findViewById(R.id.my_task_line);
        my_logout = view.findViewById(R.id.my_logout);
        loadingDialog = new LoadingDialog(getActivity());
        AvatarImageView imageView = view.findViewById(R.id.user_logo);
        Glide.with(this).load(R.drawable.view_02).into(imageView);

        my_task_line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.setMessage("loading");
                loadingDialog.show();
                Intent intent =new Intent(getActivity(),MyTaskListActivity.class);
                startActivity(intent);
            }
        });

        my_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new XPopup.Builder(getContext()).asConfirm("再次确认", "您确认要登出该账号吗",
                        new OnConfirmListener() {
                            @Override
                            public void onConfirm() {
                                    logout();
                            }
                        })
                        .show();
            }
        });



        myApplication = ((MainActivity)getActivity()).getMyApplication();
        User user = myApplication.geLogintUser();
        userId_text.setText(new StringBuilder().append("众包号   ").append(user.getUserId()).append("").toString());
        userAccount_text.setText(new StringBuilder().append(user.getUserName()).toString());
        loginModel = new LoginModel(myApplication,this.getActivity());


        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    public void onStop() {
        super.onStop();
        if (loadingDialog != null)
            loadingDialog.dismiss();
    }


    private void logout(){
        loginModel.logOff();
        Intent intent = new Intent(this.getActivity(),LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
