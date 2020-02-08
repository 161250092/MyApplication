package com.example.kongmin.view.rebuild;

import com.example.kongmin.pojo.MarkCategory1;

import java.util.ArrayList;

public interface ListRenderCallBack {

    public void initListView(ArrayList<MarkCategory1> markCategory1s);

    public void notifyDataChanged();
}
