package com.example.kongmin.view.iCallBack;

import com.example.kongmin.pojo.MarkCategory1;

import java.util.ArrayList;

public interface ListRenderCallBack {

    public void initListView(ArrayList<MarkCategory1> markCategory1s);

    public void notifyDataChanged();
}
