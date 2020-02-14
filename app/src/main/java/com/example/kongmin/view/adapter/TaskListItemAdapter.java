package com.example.kongmin.view.adapter;

import android.widget.BaseAdapter;
import android.widget.TextView;
import android.view.LayoutInflater;
import java.util.ArrayList;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.Color;

import com.example.kongmin.myapplication.R;
import com.example.kongmin.pojo.MarkCategory1;


public class TaskListItemAdapter extends BaseAdapter{
        LayoutInflater inflater;
        ArrayList<MarkCategory1> array;

        public TaskListItemAdapter(LayoutInflater inf, ArrayList<MarkCategory1> arry){
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
                convertView=inflater.inflate(R.layout.edit_text_list_view, null);
                //注意导包，别导系统包
                vh.tv1=(TextView) convertView.findViewById(R.id.textView1);
                vh.tv2=(TextView) convertView.findViewById(R.id.textView2);
                vh.tv3=(TextView) convertView.findViewById(R.id.textView3);
                vh.tv4=(TextView) convertView.findViewById(R.id.textView4);
                convertView.setTag(vh);
            }
            vh=(ViewHolder) convertView.getTag();
            vh.tv1.setText(array.get(position).getTitle());
            vh.tv2.setText(array.get(position).getContent());
            vh.tv3.setText(array.get(position).getType());
            vh.tv4.setText(array.get(position).getTimes());
            //设置item的不同背景颜色
            String typename = array.get(position).getType();
            if (typename.equals("信息抽取")){//浅粉色
                //vh.tv1.setTextColor(Color.parseColor("#FDF5E6"));
                convertView.setBackgroundColor(Color.parseColor("#FFF5EE"));
            }else if (typename.equals("文本分类")){//浅黄色
                //vh.tv1.setTextColor(Color.parseColor("#FFF5EE"));
                convertView.setBackgroundColor(Color.parseColor("#fffafe"));
            }else if (typename.equals("文本关系")){//浅绿色
                //vh.tv1.setTextColor(Color.parseColor("#F0FFF0"));
                convertView.setBackgroundColor(Color.parseColor("#fafff0"));
            }else if (typename.contains("文本配对")){//浅蓝色
                //vh.tv1.setTextColor(Color.parseColor("#F5FFFA"));
                convertView.setBackgroundColor(Color.parseColor("#e1f2fe"));
            }else if (typename.equals("文本排序")){//浅紫色
                //vh.tv1.setTextColor(Color.parseColor("#F8F8FF"));
                convertView.setBackgroundColor(Color.parseColor("#f9f0ff"));
            }else if (typename.equals("文本类比排序")){//浅橘色
                //vh.tv1.setTextColor(Color.parseColor("#F5F5F5"));
                convertView.setBackgroundColor(Color.parseColor("#ffded2"));
            }
            return convertView;
        }
        class ViewHolder{
            TextView tv1,tv2,tv3,tv4;
        }


}
