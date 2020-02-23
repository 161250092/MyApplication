package com.example.kongmin.view.doTask.sort;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kongmin.myapplication.R;

import java.util.Collections;
import java.util.List;

public class DtSortAdapter extends RecyclerView.Adapter<DtSortAdapter.MyViewHolder> implements ItemTouchHelperAdapter{

    private List<DataBean> list;

    public interface OnItemClickListener {
        void onClick(int position);
    }
    private OnItemClickListener mOnItemClickListener;

    public DtSortAdapter(List<DataBean> list) {
        this.list = list;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
        this.mOnItemClickListener=onItemClickListener;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        private ExpandableTextView expandable_text;

        public MyViewHolder(View itemView) {
            super(itemView);
            expandable_text= (ExpandableTextView) itemView.findViewById(R.id.expandable_text);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder,final int position) {

        holder.expandable_text.setText(list.get(position).getText(), list.get(position).isCollapsed());
        holder.expandable_text.setListener(new ExpandableTextView.OnExpandStateChangeListener() {
            @Override
            public void onExpandStateChanged(boolean isExpanded) {
                list.get(position).setCollapsed(isExpanded);
            }
        });

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
    public List getItemList(){
        return list;
    }
}
