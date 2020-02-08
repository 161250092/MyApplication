package com.example.kongmin.view.sort;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.kongmin.myapplication.R;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import java.util.ArrayList;

public class SortActivity extends AppCompatActivity {

    RecyclerView main_rv;
    ExpandAdapter adapter;

    ArrayList<DataBean> models;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort);

        models=new ArrayList<>();
        String[] arrays=getResources().getStringArray(R.array.news);
        for (String array : arrays) {
            DataBean bean=new DataBean();
            bean.setText(array);
            models.add(bean);
        }

        main_rv= (RecyclerView) findViewById(R.id.main_rv);
        main_rv.setHasFixedSize(true);
        main_rv.setLayoutManager(new LinearLayoutManager(this));
        adapter=new ExpandAdapter(this, models);
        main_rv.setAdapter(adapter);

    }
}
