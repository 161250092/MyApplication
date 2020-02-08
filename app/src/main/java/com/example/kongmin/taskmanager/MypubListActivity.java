package com.example.kongmin.taskmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.kongmin.myapplication.R;
import android.widget.ListView;
import android.view.Window;
import android.view.View;
import android.widget.Toast;
import java.util.List;
import java.util.ArrayList;

public class MypubListActivity extends AppCompatActivity {
    private static final String TAG = "zxt";
    private ListView mLv;
    private List<SwipeBean> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypub_list);
        mLv = (ListView) findViewById(R.id.test);

        initDatas();
        mLv.setAdapter(new CommonAdapter<SwipeBean>(this, mDatas, R.layout./*item_swipe_menu*/item_cst_swipe) {
            @Override
            public void convert(final ViewHolder holder, SwipeBean swipeBean, final int position) {
                //((SwipeMenuLayout)holder.getConvertView()).setIos(false);//这句话关掉IOS阻塞式交互效果
                holder.setText(R.id.content, swipeBean.name);
                holder.setOnClickListener(R.id.content, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MypubListActivity.this, "position:" + position, Toast.LENGTH_SHORT).show();
                    }
                });

                holder.setOnClickListener(R.id.btnDelete, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MypubListActivity.this, "删除:" + position, Toast.LENGTH_SHORT).show();
                        //在ListView里，点击侧滑菜单上的选项时，如果想让擦花菜单同时关闭，调用这句话
                        ((SwipeMenuLayout) holder.getConvertView()).quickClose();
                        mDatas.remove(position);
                        notifyDataSetChanged();
                    }
                });
            }
        });
    }

    private void initDatas() {
        mDatas = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mDatas.add(new SwipeBean("" + i));
        }
    }

}