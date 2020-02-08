package com.example.kongmin.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ImageView;

import com.example.kongmin.myapplication.R;
import com.example.kongmin.pojo.ShowFile;

import java.util.ArrayList;

public class ShowFileAdapter extends BaseAdapter {
    LayoutInflater inflater;
    ArrayList<ShowFile> array;

    public ShowFileAdapter(LayoutInflater inf,ArrayList<ShowFile> arry){
        this.inflater=inf;
        this.array=arry;
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int position) {
        return array.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if(convertView==null){
            vh=new ViewHolder();
            convertView=inflater.inflate(R.layout.edit_text_show_file, null);//注意导包，别导系统包
            vh.tv1=(ImageView) convertView.findViewById(R.id.file_image);
            vh.tv2=(TextView) convertView.findViewById(R.id.name);
            vh.tv3=(TextView) convertView.findViewById(R.id.mess);
            convertView.setTag(vh);
        }
        vh=(ViewHolder) convertView.getTag();
        vh.tv1.setImageResource(array.get(position).getImageId());
        vh.tv2.setText(array.get(position).getName());
        vh.tv3.setText(array.get(position).getSize());
        return convertView;
    }
    class ViewHolder{
        ImageView tv1;
        TextView tv2,tv3;
    }

   /* private final int resourceId;

    public EdittextAdapter(Context context, int textViewResourceId, List<MarkCategory1> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MarkCategory1 fruit = (MarkCategory1) getItem(position); // 获取当前项的Fruit实例
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);//实例化一个对象
        TextView tv1=(TextView) view.findViewById(R.id.textView1);
        TextView tv2=(TextView) view.findViewById(R.id.textView2);
        tv1.setText(fruit.getTitle());
        tv2.setText(fruit.getTimes());
        return view;
    }*/


}

