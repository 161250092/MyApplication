package com.example.textannotation.util;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.textannotation.myapplication.R;

import java.util.ArrayList;

public class InitLabel {

    private static Flowlayout instancelabel;
    private static Flowlayout item1label;
    private static Flowlayout item2label;

    private static ArrayList<TagItem> mAddTagsinstance = new ArrayList<TagItem>();
    private static EditText inputLabelinstance;
    private static String[] mTextStrinstance = {};
    private static ArrayList<String> listinstance = new ArrayList<String>();

    private static ArrayList<TagItem> mAddTagsitem1 = new ArrayList<TagItem>();
    private static EditText inputLabelitem1;
    private static String[] mTextStritem1 = {};
    private static ArrayList<String> listitem1 = new ArrayList<String>();

    private static ArrayList<TagItem> mAddTagsitem2 = new ArrayList<TagItem>();
    private static EditText inputLabelitem2;
    private static String[] mTextStritem2 = {};
    private static ArrayList<String> listitem2 = new ArrayList<String>();


    //设置和instance标签相关的
    public static void initInstanceList() {
        for(int i=0;i<mTextStrinstance.length;i++){
            listinstance.add(mTextStrinstance[i]);
        }
    }

    public static void initInstanceBtnListener() {
        inputLabelinstance.setImeOptions(EditorInfo.IME_ACTION_DONE);
        inputLabelinstance.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v,int actionId,KeyEvent event){
                if(actionId == EditorInfo.IME_ACTION_SEND || actionId == EditorInfo.IME_ACTION_DONE
                || actionId == EditorInfo.IME_ACTION_NEXT || (event != null && KeyEvent.KEYCODE_ENTER
                == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                String label = inputLabelinstance.getText().toString().trim();
                String[] newStr = new String[instancelabel.getChildCount()];
                /**获取子view的数量并添加进去*/
                if(label!=null&&!label.equals("")){
                    for(int m = 0;m < instancelabel.getChildCount()-1;m++){
                        //根据当前位置查找到当前textView中标签内容
                        newStr[m] =((TextView)instancelabel.getChildAt(m).findViewById(R.id.text)).getText().toString();
                    }
                    listinstance.add(label);
                    initInstanceLayout(listinstance);
                    inputLabelinstance.setText("");
                    inputLabelinstance.requestFocus();
                }
                return true;
            }
                return false;
            }
        });
    }
    public static void initInstanceLayout(final ArrayList<String> arr) {

        instancelabel.removeAllViewsInLayout();
        /*** 创建 textView数组*/
        final TextView[] textViews = new TextView[arr.size()];
        final ImageView[] icons = new ImageView[arr.size()];
        for (int i = 0; i < arr.size(); i++) {
            final int pos = i;
            final View view = (View) LayoutInflater.from(CommentApplication.getContext().getApplicationContext())
                  .inflate(R.layout.text_view, instancelabel, false);
            final TextView text = (TextView) view.findViewById(R.id.text);
            //查找到当前textView
            final ImageView icon = (ImageView) view.findViewById(R.id.delete_icon);
            //查找到当前删除小图标
            // 将已有标签设置成可选标签
            text.setText(listinstance.get(i));
            /**将当前textView赋值给textView数组*/
            textViews[i] = text;
            icons[i] = icon;
            //设置单击事件：
            icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //遍历  图标  删除 当前  被点击项
                    for(int j = 0; j < icons.length;j++){
                        if(icon.equals(icons[j])){
                            //获取当前点击删除图标的位置：
                            instancelabel.removeViewAt(j);
                            listinstance.remove(j);
                            initInstanceLayout(listinstance);
                        }
                    }
                }
            });
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    text.setActivated(!text.isActivated());
                    //true是激活的
                    if (text.isActivated()) {
                        boolean bResult = doInstanceAddText(listinstance.get(pos),false,pos);
                        text.setActivated(bResult);
                        //遍历数据将图标设置为可见：
                        for(int j = 0;j< textViews.length;j++){
                            if(text.equals(textViews[j])){
                                //非当前textView
                                icons[j].setVisibility(View.VISIBLE);
                            }
                        }
                    }else{
                        for(int j = 0;j< textViews.length;j++){
                            icons[j].setVisibility(View.GONE);
                        }
                    }
                    /**遍历textView满足已经被选中并且不是当前对象的textView则置为不选
                     */
                    for(int j = 0;j< textViews.length;j++){
                        if(!text.equals(textViews[j])){
                            //非当前textView
                            textViews[j].setActivated(false);
                            //true是激活的
                            icons[j].setVisibility(View.GONE);
                        }
                    }
                }
            });
            instancelabel.addView(view);
        }
        instancelabel.addView(inputLabelinstance);
    }

    // 标签索引文本
    public static int idxInstanceTextTag(String text) {
        int mTagCnt = mAddTagsinstance.size();
        //添加标签的条数
        for (int i = 0; i < mTagCnt; i++) {
            TagItem item = mAddTagsinstance.get(i);
            if (text.equals(item.tagText)) {
                return i;
            }
        }
        return -1;
    }

    // 标签添加文本状态
    public static boolean doInstanceAddText(final String str, boolean bCustom, int idx) {
        int tempIdx = idxInstanceTextTag(str);
        if (tempIdx >= 0) {
            TagItem item = mAddTagsinstance.get(tempIdx);
            item.tagCustomEdit = false;
            item.idx = tempIdx;
            return true;
        }
        int tagCnt = mAddTagsinstance.size();
        //添加标签的条数
        TagItem item = new TagItem();
        item.tagText = str;
        item.tagCustomEdit = bCustom;
        item.idx = idx;
        mAddTagsinstance.add(item);
        tagCnt++;
        return true;
    }


    //设置和item1标签相关的
    public static void inititem1List() {
        for(int i=0;i<mTextStritem1.length;i++){
            listitem1.add(mTextStritem1[i]);
        }
    }
    public static void inititem1BtnListener() {
        inputLabelitem1.setImeOptions(EditorInfo.IME_ACTION_DONE);
        inputLabelitem1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v,int actionId,KeyEvent event){
                if(actionId == EditorInfo.IME_ACTION_SEND || actionId == EditorInfo.IME_ACTION_DONE
                || actionId == EditorInfo.IME_ACTION_NEXT || (event != null && KeyEvent.KEYCODE_ENTER
                == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                String label = inputLabelitem1.getText().toString().trim();
                String[] newStr = new String[item1label.getChildCount()];
                /**获取子view的数量并添加进去*/
                if(label!=null&&!label.equals("")){
                    for(int m = 0;m < item1label.getChildCount()-1;m++){
                        //根据当前位置查找到当前textView中标签内容
                        newStr[m] =((TextView)item1label.getChildAt(m).findViewById(R.id.text)).getText().toString();
                    }
                    listitem1.add(label);
                    inititem1Layout(listitem1);
                    inputLabelitem1.setText("");
                    inputLabelitem1.requestFocus();
                }
                return true;
            }
                return false;
            }
        });
    }
    public static void inititem1Layout(final ArrayList<String> arr) {
        item1label.removeAllViewsInLayout();
        /**创建textView数组*/
        final TextView[] textViews = new TextView[arr.size()];
        final ImageView[] icons = new ImageView[arr.size()];
        for (int i = 0; i < arr.size(); i++) {
            final int pos = i;
            final View view = (View) LayoutInflater.from(CommentApplication.getContext().getApplicationContext())
                  .inflate(R.layout.text_view,item1label,false);
            final TextView text = (TextView) view.findViewById(R.id.text);
            //查找到当前textView
            final ImageView icon = (ImageView) view.findViewById(R.id.delete_icon);
            //查找到当前删除小图标
            //将已有标签设置成可选标签
            text.setText(listitem1.get(i));
            /**将当前textView赋值给textView数组*/
            textViews[i] = text;
            icons[i] = icon;
            //设置单击事件：
            icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //遍历图标删除当前被点击项
                    for(int j = 0; j < icons.length;j++){
                        if(icon.equals(icons[j])){
                            //获取当前点击删除图标的位置：
                            item1label.removeViewAt(j);
                            listitem1.remove(j);
                            inititem1Layout(listitem1);
                        }
                    }
                }
            });
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    text.setActivated(!text.isActivated());
                    //true是激活的
                    if (text.isActivated()) {
                        boolean bResult = doitem1AddText(listitem1.get(pos),false, pos);
                        text.setActivated(bResult);
                        //遍历数据将图标设置为可见：
                        for(int j = 0;j< textViews.length;j++){
                            if(text.equals(textViews[j])){
                                //非当前textView
                                icons[j].setVisibility(View.VISIBLE);
                            }
                        }
                    }else{
                        for(int j = 0;j< textViews.length;j++){
                            icons[j].setVisibility(View.GONE);
                        }
                    }
                    /**遍历textView满足已经被选中并且不是当前对象的textView则置为不选*/
                    for(int j = 0;j< textViews.length;j++){
                        if(!text.equals(textViews[j])){
                            //非当前textView
                            textViews[j].setActivated(false);
                            //true是激活的
                            icons[j].setVisibility(View.GONE);
                        }
                    }
                }
            });
            item1label.addView(view);
        }
        item1label.addView(inputLabelitem1);
    }

    //标签索引文本
    public static int idxitem1TextTag(String text) {
        int mTagCnt = mAddTagsitem1.size();
        //添加标签的条数
        for (int i = 0; i < mTagCnt; i++) {
            TagItem item = mAddTagsitem1.get(i);
            if (text.equals(item.tagText)) {
                return i;
            }
        }
        return -1;
    }

    // 标签添加文本状态
    public static boolean doitem1AddText(final String str,boolean bCustom,int idx) {
        int tempIdx = idxitem1TextTag(str);
        if (tempIdx >= 0) {
            TagItem item = mAddTagsitem1.get(tempIdx);
            item.tagCustomEdit = false;
            item.idx = tempIdx;
            return true;
        }
        int tagCnt = mAddTagsitem1.size();
        //添加标签的条数
        TagItem item = new TagItem();
        item.tagText = str;
        item.tagCustomEdit = bCustom;
        item.idx = idx;
        mAddTagsitem1.add(item);
        tagCnt++;
        return true;
    }


    //设置和item2标签相关的
    private static void inititem2List() {
        for(int i=0;i<mTextStritem2.length;i++){
            listitem2.add(mTextStritem2[i]);
        }
    }

    public static void inititem2BtnListener() {
        inputLabelitem2.setImeOptions(EditorInfo.IME_ACTION_DONE);
        inputLabelitem2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
                if (actionId == EditorInfo.IME_ACTION_SEND || actionId == EditorInfo.IME_ACTION_DONE
                 || actionId == EditorInfo.IME_ACTION_NEXT || (event != null && KeyEvent.KEYCODE_ENTER
                 == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                String label = inputLabelitem2.getText().toString().trim();
                String[] newStr = new String[item2label.getChildCount()];
                /**获取子view的数量并添加进去*/
                if(label!=null&&!label.equals("")){
                    for(int m = 0;m < item2label.getChildCount()-1;m++){
                        //根据当前位置查找到当前textView中标签内容
                        newStr[m] =((TextView)item2label.getChildAt(m).findViewById(R.id.text)).getText().toString();
                    }
                    listitem2.add(label);
                    inititem2Layout(listitem2);
                    inputLabelitem2.setText("");
                    inputLabelitem2.requestFocus();
                }
                return true;
            }
                return false;
            }
        });
    }
    public static void inititem2Layout(final ArrayList<String> arr) {
        item2label.removeAllViewsInLayout();
        /**创建textView数组*/
        final TextView[] textViews = new TextView[arr.size()];
        final ImageView[] icons = new ImageView[arr.size()];
        for (int i = 0; i < arr.size(); i++) {
            final int pos = i;
            final View view = (View) LayoutInflater.from(CommentApplication.getContext().getApplicationContext())
                  .inflate(R.layout.text_view, item2label,false);
            final TextView text = (TextView) view.findViewById(R.id.text);
            //查找到当前textView
            final ImageView icon = (ImageView) view.findViewById(R.id.delete_icon);
            //查找到当前删除小图标
            // 将已有标签设置成可选标签
            text.setText(listitem2.get(i));
            /**将当前textView赋值给textView数组*/
            textViews[i] = text;
            icons[i] = icon;
            //设置单击事件：
            icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //遍历图标删除当前被点击项
                    for(int j = 0; j < icons.length;j++){
                        if(icon.equals(icons[j])){
                            //获取当前点击删除图标的位置：
                            item2label.removeViewAt(j);
                            listitem2.remove(j);
                            inititem2Layout(listitem2);
                        }
                    }
                }
            });
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    text.setActivated(!text.isActivated());
                    //true是激活的
                    if (text.isActivated()) {
                        boolean bResult = doitem2AddText(listitem2.get(pos),false,pos);
                        text.setActivated(bResult);
                        //遍历数据将图标设置为可见：
                        for(int j = 0;j< textViews.length;j++){
                            if(text.equals(textViews[j])){
                                //非当前textView
                                icons[j].setVisibility(View.VISIBLE);
                            }
                        }
                    }else{
                        for(int j = 0;j< textViews.length;j++){
                            icons[j].setVisibility(View.GONE);
                        }
                    }

                    /**遍历textView满足已经被选中并且不是当前对象的textView则置为不选*/
                    for(int j = 0;j< textViews.length;j++){
                        if(!text.equals(textViews[j])){
                            //非当前textView
                            textViews[j].setActivated(false);
                            //true是激活的
                            icons[j].setVisibility(View.GONE);
                        }
                    }
                }
            });
            item2label.addView(view);
        }
        item2label.addView(inputLabelitem2);
    }
    // 标签索引文本
    public static int idxitem2TextTag(String text) {
        int mTagCnt = mAddTagsitem2.size();
        //添加标签的条数
        for (int i = 0; i < mTagCnt; i++) {
            TagItem item = mAddTagsitem2.get(i);
            if (text.equals(item.tagText)) {
                return i;
            }
        }
        return -1;
    }
    // 标签添加文本状态
    public static boolean doitem2AddText(final String str,boolean bCustom,int idx) {
        int tempIdx = idxitem2TextTag(str);
        if (tempIdx >= 0) {
            TagItem item = mAddTagsitem2.get(tempIdx);
            item.tagCustomEdit = false;
            item.idx = tempIdx;
            return true;
        }
        int tagCnt = mAddTagsitem2.size();
        //添加标签的条数
        TagItem item = new TagItem();
        item.tagText = str;
        item.tagCustomEdit = bCustom;
        item.idx = idx;
        mAddTagsitem2.add(item);
        tagCnt++;
        return true;
    }
}
