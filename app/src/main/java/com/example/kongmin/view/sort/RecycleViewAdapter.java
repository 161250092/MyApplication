package com.example.kongmin.view.sort;

import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.View;
import java.util.List;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import com.example.kongmin.myapplication.R;
import java.util.Collections;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder> implements ItemTouchHelperAdapter{

    private List<Message> list;

    public interface OnItemClickListener {
        void onClick(int position);
    }
    private OnItemClickListener mOnItemClickListener;

    public RecycleViewAdapter(List<Message> list) {
        this.list = list;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
            this.mOnItemClickListener=onItemClickListener;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_username, tv_time, tv_message;
        private ImageView iv_icon;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_username = (TextView) itemView.findViewById(R.id.tv_username);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_message = (TextView) itemView.findViewById(R.id.tv_message);
            iv_icon = (ImageView) itemView.findViewById(R.id.iv_icon);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dotask_one_sort_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder,final int position) {
        Message msg = list.get(position);
        holder.tv_username.setText(msg.getUsername());
        holder.tv_time.setText(msg.getTime());
        holder.tv_message.setText(msg.getMessage());
        holder.iv_icon.setBackgroundResource(msg.getImg_id());

        if( mOnItemClickListener!= null){
            holder.itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onItemDismiss(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onItemMove(int from, int to) {
        //Collections.swap(list, from, to);
        //notifyItemMoved(from, to);
        if(from < to) {
            //从上往下拖动，每滑动一个item，都将list中的item向下交换，向上滑同理。
            for (int i = from; i < to; i++) {
                Collections.swap(list, i, i + 1);//交换数据源两个数据的位置
            }
        } else {
            for (int i = from; i > to; i--) {
                Collections.swap(list, i, i - 1);//交换数据源两个数据的位置
            }
        }
        //更新视图
        notifyItemMoved(from, to);
    }
}

