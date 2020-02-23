package com.example.kongmin.view.taskDetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kongmin.myapplication.R;

import java.util.List;

public class FileContentGridViewAdapter extends BaseAdapter {

    List<String> mList;
    Context mContext;

    public FileContentGridViewAdapter(List<String> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
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
        convertView = layoutInflater.inflate(R.layout.item_file_content,null);
        TextView textView = convertView.findViewById(R.id.textview_item);
        textView.setText(mList.get(position));
        return convertView;
    }
}
