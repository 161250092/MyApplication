package com.example.kongmin.view.sort;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import java.util.List;
import java.util.ArrayList;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kongmin.myapplication.R;

public class DotaskOneSortActivity extends AppCompatActivity {

    private RecyclerView rv;
    //private RecycleViewAdapter adapter;
    private DtSortAdapter adapter;
    //private List<Message> list;
    private List<DataBean> list;
    private TextView edit;
    private TextView save;

    private int docId;
    //要排序的item的个数
    private int listsize;
    private int instanceid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dotask_one_sort);

        rv= (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));

        initData();

        adapter=new DtSortAdapter(list);
        rv.setAdapter(adapter);

        //edit = (TextView) findViewById(R.id.edit);
        save = (TextView) findViewById(R.id.save);

        initmotivation();







    }

    private void initData() {
        list = new ArrayList<>();
        String[] arrays=getResources().getStringArray(R.array.news);
        for (String array : arrays) {
            DataBean bean=new DataBean();
            bean.setText(array);
            list.add(bean);
        }
    }
    private void initmotivation() {

        adapter.setOnItemClickListener(new DtSortAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(DotaskOneSortActivity.this,"您点击了"+position+"行",Toast.LENGTH_SHORT).show();
            }
        });

        //先实例化Callback
         final SimpleItemTouchHelperCallback callback = new SimpleItemTouchHelperCallback(adapter);
        //用Callback构造ItemtouchHelper
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        //调用ItemTouchHelper的attachToRecyclerView方法建立联系
        touchHelper.attachToRecyclerView(rv);


        /*edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.setSort(true);
            }
        });*/

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.notifyDataSetChanged();
            }
        });

    }

}
