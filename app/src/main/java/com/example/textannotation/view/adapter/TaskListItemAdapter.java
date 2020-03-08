package com.example.textannotation.view.adapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.view.LayoutInflater;
import java.util.ArrayList;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.Color;

import com.example.textannotation.myapplication.R;
import com.example.textannotation.pojo.TaskInfo;


public class TaskListItemAdapter extends BaseAdapter{
        LayoutInflater inflater;
        ArrayList<TaskInfo> array;

        public TaskListItemAdapter(LayoutInflater inf, ArrayList<TaskInfo> arry){
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
            if(convertView == null){
                vh = new ViewHolder();
                convertView = inflater.inflate(R.layout.item_task, null);
                vh.title   =  (TextView)convertView.findViewById(R.id.title);
                vh.typeName   =  (TextView)convertView.findViewById(R.id.typeName);
                vh.taskcompstatus   =  (TextView)convertView.findViewById(R.id.taskcompstatus);
                vh.description   =  (TextView)convertView.findViewById(R.id.description);
                vh.viewnum   =  (TextView)convertView.findViewById(R.id.viewnum);
                vh.attendnum   =  (TextView)convertView.findViewById(R.id.attendnum);
                vh.createtime   =  (TextView)convertView.findViewById(R.id.createtime);
                convertView.setTag(vh);
            }
            vh = (ViewHolder) convertView.getTag();


            vh.title.setText(array.get(position).getTitle());
            vh.typeName.setText(array.get(position).getType());

            if (!array.get(position).getContent().equals("进行中"))
                vh.taskcompstatus.setText(array.get(position).getContent());
            else
                vh.taskcompstatus.setVisibility(View.INVISIBLE);

            vh.description.setText(array.get(position).getDescription());

            int attendnum = array.get(position).getAttendnum();
            int viewnum = array.get(position).getViewnum();
            vh.attendnum.setText(attendnum+" 参与者");
            vh.viewnum.setText(viewnum+" 浏览者");

            vh.createtime.setText(array.get(position).getTimes());
            //设置item的不同背景颜色
            String typename = array.get(position).getType();

            if (typename.equals("信息抽取")){//浅粉色
                //vh.tv1.setTextColor(Color.parseColor("#FDF5E6"));
                vh.typeName.setTextColor(Color.parseColor("#ef0206"));
            }else if (typename.equals("文本分类")){//浅黄色
                //vh.tv1.setTextColor(Color.parseColor("#FFF5EE"));
                vh.typeName.setTextColor(Color.parseColor("#a7c62a"));
            }else if (typename.equals("文本关系")){//浅绿色
                //vh.tv1.setTextColor(Color.parseColor("#F0FFF0"));
                vh.typeName.setTextColor(Color.parseColor("#029790"));
            }else if (typename.contains("文本配对")){//浅蓝色
                //vh.tv1.setTextColor(Color.parseColor("#F5FFFA"));
                vh.typeName.setTextColor(Color.parseColor("#f47615"));
            }else if (typename.equals("文本排序")){//浅紫色
                //vh.tv1.setTextColor(Color.parseColor("#F8F8FF"));
                vh.typeName.setTextColor(Color.parseColor("#1bb100"));
            }else if (typename.equals("文本类比排序")){//浅橘色
                //vh.tv1.setTextColor(Color.parseColor("#F5F5F5"));
                vh.typeName.setTextColor(Color.parseColor("#030386"));
            }


            return convertView;
        }


        class ViewHolder{
            TextView title,typeName,taskcompstatus,description,viewnum,attendnum,createtime;
        }


}
