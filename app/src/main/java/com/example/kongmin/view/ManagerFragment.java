package com.example.kongmin.view;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.kongmin.managetask.SimpleActivity;
import com.example.kongmin.managetask.dotask.MyDotaskListActivity;
import com.example.kongmin.myapplication.R;

public class ManagerFragment extends Fragment implements AdapterView.OnItemClickListener{
    @Nullable
    private String[] data = { "我发布的","我参与的", "个人信息管理"};
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.publish_tab, container, false);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.list_item_add,data);

        ListView listview =(ListView) view.findViewById(R.id.list_view);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(this);
        return view;
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //通过view获取其内部的组件，进而进行操作
        /*String text = (String) ((TextView)view.findViewById(R.id.text2)).getText();
        //大多数情况下，position和id相同，并且都从0开始
        String showText = "点击第" + position + "项，文本内容为：" + text + "，ID为：" + id;*/
        if(position==0){
        //给bnt1添加点击响应事件
        //Intent intent =new Intent(getActivity(),MypubListActivity.class);
            Intent intent =new Intent(getActivity(),SimpleActivity.class);
        //启动
        startActivity(intent);
        }else if(position==1){
            Intent intent =new Intent(getActivity(),MyDotaskListActivity.class);
            //启动
            startActivity(intent);
        }
        //Toast.makeText(mContext, showText, Toast.LENGTH_LONG).show();
    }
}
