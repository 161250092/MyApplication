package com.example.kongmin.view.mainPage;


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

import com.example.kongmin.managetask.dotask.MyTaskListActivity;
import com.example.kongmin.myapplication.R;
import com.example.kongmin.pojo.User;
import com.example.kongmin.util.MyApplication;
import com.example.kongmin.view.login.LoginActivity;
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

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.publish_tab, container, false);
        userId_text = view.findViewById(R.id.user_id);
        userAccount_text = view.findViewById(R.id.user_account);
        my_task_line = view.findViewById(R.id.my_task_line);
        my_logout = view.findViewById(R.id.my_logout);
        loadingDialog = new LoadingDialog(getActivity());

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
        myApplication.userLogout();
        Intent intent = new Intent(this.getActivity(),LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);;
        startActivity(intent);
    }

}
