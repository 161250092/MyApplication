package com.example.kongmin.view;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.content.Context;
import com.example.kongmin.Constant.Constant;
import com.example.kongmin.myapplication.R;
import com.example.kongmin.view.publictask.AddListMatchCategoryActivity;
import com.example.kongmin.view.publictask.AddListTwoitemsActivity;
import com.example.kongmin.view.publictask.AddListOneSortActivity;
import com.example.kongmin.view.publictask.InfoExtraActivity;

/**
 * Created by kongmin on 2019/02/11.
 * 发任务界面
 */
public class PublishFragment extends Fragment implements AdapterView.OnItemClickListener{
    @Nullable
    private Context mContext;
    private String[] tasktype = Constant.taskType;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.publish_tab, container, false);
        this.mContext = getActivity();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.list_item_add,tasktype);
        ListView listview =(ListView) view.findViewById(R.id.list_view);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position==0||position==1){
            //发任务类型是信息抽取或文本分类,跳转到发任务界面
            //Intent intent =new Intent(getActivity(),EditTextListActivity.class);
            Intent intent =new Intent(getActivity(), InfoExtraActivity.class);
            intent.putExtra("tasktype",position);
            //启动
            startActivity(intent);
        }
        if(position==2){
            //发任务类型是文本关系,跳转到发任务界面
            Intent intent =new Intent(getActivity(), AddListTwoitemsActivity.class);
            //启动
            startActivity(intent);
        }
        if(position==3){
            //发任务类型是文本配对,跳转到发任务界面
            Intent intent =new Intent(getActivity(), AddListMatchCategoryActivity.class);
            //启动
            startActivity(intent);
        }
        //todo 两种排序方式要分开
        if(position==4||position==5){
            //发任务类型是文本排序或文本类比排序,跳转到发任务界面
            Intent intent =new Intent(getActivity(), AddListOneSortActivity.class);
            //启动
            startActivity(intent);
        }
    }
}
