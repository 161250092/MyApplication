package com.example.kongmin.view.rebuild;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.kongmin.myapplication.R;
import com.example.kongmin.view.adapter.MainPageGridViewAdapter;
import com.hb.dialog.dialog.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

public class MainPageFragment extends Fragment {

    LoadingDialog loadingDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mRootView;
        mRootView = inflater.inflate(R.layout.fragment_main_page, container, false);

        loadingDialog = new LoadingDialog(getActivity());

        GridView gridView = mRootView.findViewById(R.id.gridview);
        final List<String> list = new ArrayList<>();
        list.add("我的任务");
        list.add("任务列表");
        for(int i = 3;i <= 6;i++){
            list.add("选项"+i);
        }
        gridView.setAdapter(new MainPageGridViewAdapter(list,getActivity()));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1){
                    loadingDialog = new LoadingDialog(getActivity());
                    loadingDialog.setMessage("loading");
                    loadingDialog.show();
                    Intent intent = new Intent(getActivity(),
                            TaskListActivity.class);
                    startActivity(intent);
                }
            }
        });
        return mRootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (loadingDialog != null)
            loadingDialog.dismiss();
    }
}
