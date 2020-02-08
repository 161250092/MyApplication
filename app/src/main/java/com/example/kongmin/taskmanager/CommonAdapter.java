package com.example.kongmin.taskmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import java.util.ArrayList;
import java.util.List;
import android.util.SparseArray;
import android.widget.TextView;
import android.widget.Checkable;
import android.widget.RatingBar;
import android.widget.ProgressBar;
import android.graphics.Typeface;
import android.text.util.Linkify;
import android.view.animation.AlphaAnimation;
import android.os.Build;
import android.graphics.drawable.Drawable;
import android.graphics.Bitmap;

public abstract class CommonAdapter<T> extends BaseAdapter {
    protected Context mContext;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;
    private int layoutId;


    public CommonAdapter(Context context, List<T> datas, int layoutId) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mDatas = datas;
        this.layoutId = layoutId;
    }


    @Override
    public int getCount() {
        return this.mDatas != null?this.mDatas.size():0;
    }


    @Override
    public T getItem(int position) {
        return this.mDatas.get(position);
    }


    @Override
    public long getItemId(int position) {
        return (long)position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.get(this.mContext, convertView, parent, this.layoutId, position);
        this.convert(holder, this.getItem(position), position);
        return holder.getConvertView();
    }


    public abstract void convert(ViewHolder var1, T var2, int var3);


    public void setDatas(List<T> list) {
        if(this.mDatas != null) {
            if(null != list) {
                ArrayList temp = new ArrayList();
                temp.addAll(list);
                this.mDatas.clear();
                this.mDatas.addAll(temp);
            } else {
                this.mDatas.clear();
            }
        } else {
            this.mDatas = list;
        }


        this.notifyDataSetChanged();
    }


    public void remove(int i) {
        if(null != this.mDatas && this.mDatas.size() > i && i > -1) {
            this.mDatas.remove(i);
            this.notifyDataSetChanged();
        }


    }


    public void addDatas(List<T> list) {
        if(null != list) {
            ArrayList temp = new ArrayList();
            temp.addAll(list);
            if(this.mDatas != null) {
                this.mDatas.addAll(temp);
            } else {
                this.mDatas = temp;
            }


            this.notifyDataSetChanged();
        }


    }


    public List<T> getDatas() {
        return this.mDatas;
    }
}


//6.我们来看下自己写的ViewHolder

/**
 * 创 建 者：下一页5（轻飞扬）
 * 创建时间：2018/6/27.12:53
 * 个人小站：http://wap.yhsh.ai(已挂)
 * 最新小站：http://www.iyhsh.icoc.in
 * 联系作者：企鹅 13343401268(请用手机QQ添加)
 * 博客地址：http://blog.csdn.net/xiayiye5
 * 空间名称：SwipeDelMenuLayout
 * 项目包名：voicenotes.listviewdelete
 */
/*public class ViewHolder {


    private SparseArray<View> mViews;
    private int mPosition;
    private View mConvertView;
    private Context mContext;
    private int mLayoutId;


    public ViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
        this.mContext = context;
        this.mLayoutId = layoutId;
        this.mPosition = position;
        this.mViews = new SparseArray();
        this.mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        this.mConvertView.setTag(this);
    }


    public static ViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
        if(convertView == null) {
            return new ViewHolder(context, parent, layoutId, position);
        } else {
            ViewHolder holder = (ViewHolder)convertView.getTag();
            holder.mPosition = position;
            return holder;
        }
    }


    public int getPosition() {
        return this.mPosition;
    }


    public int getLayoutId() {
        return this.mLayoutId;
    }


    public <T extends View> T getView(int viewId) {
        View view = (View)this.mViews.get(viewId);
        if(view == null) {
            view = this.mConvertView.findViewById(viewId);
            this.mViews.put(viewId, view);
        }


        return (T) view;
    }


    public View getConvertView() {
        return this.mConvertView;
    }


    public ViewHolder setSelected(int viewId, boolean flag) {
        View v = this.getView(viewId);
        v.setSelected(flag);
        return this;
    }


    public ViewHolder setText(int viewId, String text) {
        TextView tv = (TextView)this.getView(viewId);
        tv.setText(text);
        return this;
    }


    public ViewHolder setImageResource(int viewId, int resId) {
        ImageView view = (ImageView)this.getView(viewId);
        view.setImageResource(resId);
        return this;
    }


    public ViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView view = (ImageView)this.getView(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }


    public ViewHolder setImageDrawable(int viewId, Drawable drawable) {
        ImageView view = (ImageView)this.getView(viewId);
        view.setImageDrawable(drawable);
        return this;
    }


    public ViewHolder setBackgroundColor(int viewId, int color) {
        View view = this.getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }


    public ViewHolder setBackgroundRes(int viewId, int backgroundRes) {
        View view = this.getView(viewId);
        view.setBackgroundResource(backgroundRes);
        return this;
    }


    public ViewHolder setTextColor(int viewId, int textColor) {
        TextView view = (TextView)this.getView(viewId);
        view.setTextColor(textColor);
        return this;
    }


    public ViewHolder setTextColorRes(int viewId, int textColorRes) {
        TextView view = (TextView)this.getView(viewId);
        view.setTextColor(this.mContext.getResources().getColor(textColorRes));
        return this;
    }


    @SuppressLint({"NewApi"})
    public ViewHolder setAlpha(int viewId, float value) {
        if(Build.VERSION.SDK_INT >= 11) {
            this.getView(viewId).setAlpha(value);
        } else {
            AlphaAnimation alpha = new AlphaAnimation(value, value);
            alpha.setDuration(0L);
            alpha.setFillAfter(true);
            this.getView(viewId).startAnimation(alpha);
        }


        return this;
    }


    public ViewHolder setVisible(int viewId, boolean visible) {
        View view = this.getView(viewId);
        view.setVisibility(visible?0:8);
        return this;
    }


    public ViewHolder linkify(int viewId) {
        TextView view = (TextView)this.getView(viewId);
        Linkify.addLinks(view, 15);
        return this;
    }


    public ViewHolder setTypeface(Typeface typeface, int... viewIds) {
        int[] var3 = viewIds;
        int var4 = viewIds.length;


        for(int var5 = 0; var5 < var4; ++var5) {
            int viewId = var3[var5];
            TextView view = (TextView)this.getView(viewId);
            view.setTypeface(typeface);
            view.setPaintFlags(view.getPaintFlags() | 128);
        }


        return this;
    }


    public ViewHolder setProgress(int viewId, int progress) {
        ProgressBar view = (ProgressBar)this.getView(viewId);
        view.setProgress(progress);
        return this;
    }


    public ViewHolder setProgress(int viewId, int progress, int max) {
        ProgressBar view = (ProgressBar)this.getView(viewId);
        view.setMax(max);
        view.setProgress(progress);
        return this;
    }


    public ViewHolder setMax(int viewId, int max) {
        ProgressBar view = (ProgressBar)this.getView(viewId);
        view.setMax(max);
        return this;
    }


    public ViewHolder setRating(int viewId, float rating) {
        RatingBar view = (RatingBar)this.getView(viewId);
        view.setRating(rating);
        return this;
    }


    public ViewHolder setRating(int viewId, float rating, int max) {
        RatingBar view = (RatingBar)this.getView(viewId);
        view.setMax(max);
        view.setRating(rating);
        return this;
    }


    public ViewHolder setTag(int viewId, Object tag) {
        View view = this.getView(viewId);
        view.setTag(tag);
        return this;
    }


    public ViewHolder setTag(int viewId, int key, Object tag) {
        View view = this.getView(viewId);
        view.setTag(key, tag);
        return this;
    }


    public ViewHolder setChecked(int viewId, boolean checked) {
        Checkable view = (Checkable)this.getView(viewId);
        view.setChecked(checked);
        return this;
    }


    public ViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = this.getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }


    public ViewHolder setOnTouchListener(int viewId, View.OnTouchListener listener) {
        View view = this.getView(viewId);
        view.setOnTouchListener(listener);
        return this;
    }


    public ViewHolder setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
        View view = this.getView(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }
}*/
