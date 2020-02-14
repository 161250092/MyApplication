package com.example.kongmin.view;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kongmin.managetask.SimpleActivity;
import com.example.kongmin.managetask.dotask.MyDotaskListActivity;
import com.example.kongmin.myapplication.R;
import com.example.kongmin.pojo.User;
import com.example.kongmin.util.MyApplication;
import com.example.kongmin.view.rebuild.MainActivity;

public class ManagerFragment extends Fragment implements AdapterView.OnItemClickListener{
    @Nullable
    private String[] data = {"我参与的", "个人信息管理","登出"};
    MyApplication myApplication;

    TextView userId_text;
    TextView userAccount_text;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.publish_tab, container, false);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.list_item_add,data);

        ListView listview =(ListView) view.findViewById(R.id.list_view);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(this);

        userId_text = view.findViewById(R.id.user_id);
        userAccount_text = view.findViewById(R.id.user_account);

        myApplication = ((MainActivity)getActivity()).getMyApplication();

        User user = myApplication.geLogintUser();
        Log.e("ManagerFragment",user.getUserName());
        Log.e("ManagerFragment",user.getUserId()+"");
        userId_text.setText(new StringBuilder().append("当前id  ").append(user.getUserId()).append("").toString());
        userAccount_text.setText(new StringBuilder().append("账号  ").append(user.getUserName()).toString());
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position==0){
            Intent intent =new Intent(getActivity(),MyDotaskListActivity.class);
            //启动
            startActivity(intent);
        }else if(position==1){

        }
    }
}
