package com.example.kongmin.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kongmin.myapplication.R;

import java.util.List;

public class MainPageGridViewAdapter extends BaseAdapter {
    List<String> mList;
    Context mContext;
    public MainPageGridViewAdapter(List<String> list, Context context){
        this.mList = list;
        this.mContext = context;
    }
    @Override
    public int getCount() {
        return mList.size();
    }
    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService((Context.LAYOUT_INFLATER_SERVICE));
        convertView = layoutInflater.inflate(R.layout.item_main_page_gridview,null);
        TextView textView = convertView.findViewById(R.id.textview_item);
        textView.setText(mList.get(position));
        return convertView;
    }
}
