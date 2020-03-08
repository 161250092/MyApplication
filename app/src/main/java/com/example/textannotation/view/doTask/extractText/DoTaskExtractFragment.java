package com.example.textannotation.view.doTask.extractText;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.*;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.textannotation.Constant.Constant;
import com.example.textannotation.model.ITaskUpload;
import com.example.textannotation.myapplication.R;
import com.example.textannotation.util.threadPool.ThreadPool;
import com.example.textannotation.view.lazyfragment.BaseLazyFragment;
import com.example.textannotation.util.*;

import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.text.Spanned;

import java.util.*;


/**
 *
 * 信息抽取界面
 * Created by kongmin
 * 2018.12.29
 */

public class DoTaskExtractFragment extends BaseLazyFragment implements ITaskUpload {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    //设置加载动画
    //private TextView mTextView;
    private ProgressBar mPb;
    private Handler mHandler = new Handler();
    private String mData;

    private LinearLayout fragmentlayout;


    //段落内容
    private TextView doccontent;
    //完成本段按钮
    private TextView completecon;
    //完成本文档按钮
    private TextView completedoc;
    //按钮的LinearLayout
    private LinearLayout extractlinear;

    //和设置标签相关的
    private ArrayList<String> names = new ArrayList<String>();
    private FlowGroupView labelview;
    private List<TextView> labellist = new ArrayList<>();

    private static ArrayList<String> checked = new ArrayList<String>();

    private int taskid;
    //是做任务页面还是查看做任务页面
    private String typename;
    private SerializableMap labelMap;
    private Map<String,Integer> hashmap = new LinkedHashMap<>();
    private int contentid;
    //段落在文本中的索引
    private int contentindex;
    private String content;
    private int userid;

    private int docid;

    private static int sectionnumber;

    //设置样式的时候用的
    private SpannableStringBuilder spannableString;
    private Map<Integer,String> hashmapcontent = new HashMap<Integer,String>();
    private Map<Integer,String> hashmaplabel = new LinkedHashMap<Integer,String>();

    //传送过来的段落的状态
    private String parastatus;

    //随机生成的颜色
    private String colorstr;

    private MyApplication mApplication;
    private int userId;

    //信息抽取标签颜色
    private ArrayList<String> colors = new ArrayList<>();
    private HashMap<Integer,String> hashcolors = new HashMap<>();
    private String color;
    private String dotaskcolor;
    private int index;

    private SerializableSortMap colorsMap;
    private Map<Integer,String> colormap = new LinkedHashMap<Integer,String>();

    ArrayList<String> colorlist = new ArrayList<String>();
    ArrayList<Integer> beginlist = new ArrayList<Integer>();
    ArrayList<Integer> endlist = new ArrayList<Integer>();
    ArrayList<Integer> labelidlist = new ArrayList<Integer>();

    public DoTaskExtractFragment() {
    }


    //新建Fragment
    public static DoTaskExtractFragment newInstance(int sectionNumber) {
        DoTaskExtractFragment fragment = new DoTaskExtractFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    //和设置标签相关的
    /**
     * 动态添加布局
     * @param str
     */
    private void addTextView(String str) {
        TextView child = new TextView(getContext());
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
        params.setMargins(15, 15, 15, 15);
        child.setLayoutParams(params);
        child.setBackgroundResource(R.drawable.line_rect_huise);
        child.setText(str);
        child.setPadding(20,5,20,5);
        child.setTextColor(Color.BLACK);
        labellist.add(child);
        initEvents(child);
        labelview.addView(child);
    }
    /**
     * 为每个view 添加点击事件
     */
    private void initEvents(final TextView tv){
        tv.setOnClickListener(new View.OnClickListener() {
            boolean button = false;
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String selectlabel = "";
                if(button==false) {
                    checked.add(tv.getText().toString());
                    button = true;
                }else{
                    checked.remove(tv.getText().toString());
                    button = false;
                }
                for(int i=0;i<checked.size();i++){
                    selectlabel+=checked.get(i);
                }
            }
        });
    }

    //设置加载动画
    @Override
    public void loadDataStart() {
        Log.d(TAG, "loadDataStart");

        // 模拟请求数据
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mData = "这是加载下来的数据";
                // 一旦获取到数据, 就应该立刻标记数据加载完成
                mLoadDataFinished = true;
                if (mViewInflateFinished) {
                    fragmentlayout.setVisibility(View.VISIBLE);
                    names.clear();
                    Bundle bundle =getArguments();

                    sectionnumber = bundle.getInt("fragmentindex");
                    taskid =  bundle.getInt("taskid");
                    docid = bundle.getInt("docid");
                    typename = bundle.getString("type");
                    //任务标签
                    labelMap = (SerializableMap)bundle.get("lebelmap");
                    hashmap = labelMap.getMap();
                    if(typename.equals("dotask")) {
                        colors = bundle.getStringArrayList("colors");
                        colorsMap = (SerializableSortMap) bundle.get("colormap");
                        colormap = colorsMap.getMap();
                    }else{
                        //把文本设置为只读属性
                        doccontent.setTextColor(Color.BLACK);
                        doccontent.setEnabled(false);
                    }
                    for(String labelname:hashmap.keySet()){
                        int labelid = hashmap.get(labelname);
                        //给标签数组赋值
                        names.add(labelname);
                        hashmaplabel.put(labelid,labelname);
                        Log.e("DotaskExtract---->", "GET方式请求成功，labelname+labelid--->" + labelname+labelid);
                    }
                    for(int labelid:colormap.keySet()){
                        Log.e("DotaskExtract---->", "GET方式请求成功，labelname+colormap--->" + labelid+colormap.get(labelid));
                    }
                    //文本ID
                    String contentidstr = "contentid"+sectionnumber;
                    contentid = bundle.getInt(contentidstr);
                    Log.e("DotaskExtract---->", "fragment中的contentidstr--->" + contentidstr);
                    contentindex = bundle.getInt("contentindex"+sectionnumber);
                    Log.e("DotaskExtract---->", "fragment中的contentindex--->" + contentindex);
                    content = bundle.getString("content"+sectionnumber);
                    userid = bundle.getInt("userid");
                    //段落的状态
                    parastatus = bundle.getString("parastatus"+sectionnumber);


                    colorlist = bundle.getStringArrayList("colorlist");
                    beginlist = bundle.getIntegerArrayList("beginlist");
                    endlist = bundle.getIntegerArrayList("endlist");
                    labelidlist = bundle.getIntegerArrayList("labelidlist");


                    /*mTextView.setText(mData);
                    mTextView.setText("这是改变后的数据");*/
                    doccontent.setText(content);
                    doccontent.setCustomSelectionActionModeCallback(new MyActionModeCallback());
                    spannableString = new SpannableStringBuilder();
                    spannableString.append(doccontent.getText().toString());

                    if(typename.equals("mypub")){
                        extractlinear.setVisibility(View.GONE);
                    }else{
                        extractlinear.setVisibility(View.VISIBLE);
                    }

                    //setData();
                    for (int i = 0; i < names.size(); i++) {
                        addTextView(names.get(i));
                    }

                    //如果时已完成的，显示标注的信息
                    if(parastatus.equals("已完成")) {
                        inittaskextract(colorlist,beginlist,endlist,labelidlist);
                    }
                    mPb.setVisibility(View.GONE);
                }
            }
        }, 500);

    }

    /**
     * 获取十六进制的颜色代码.例如  "#6E36B4" , For HTML ,
     * @return String
     */
    public static String getRandColorCode(){
        String r,g,b;
        Random random = new Random();
        r = Integer.toHexString(random.nextInt(256)).toUpperCase();
        g = Integer.toHexString(random.nextInt(256)).toUpperCase();
        b = Integer.toHexString(random.nextInt(256)).toUpperCase();

        r = r.length()==1 ? "0" + r : r ;
        g = g.length()==1 ? "0" + g : g ;
        b = b.length()==1 ? "0" + b : b ;

        return r+g+b;
    }


    private int selectionStart = 0;
    private int selectionEnd = 0;
    private String substring ="";
    private int lid = 0;


    public void dotaskextract(final int labelid, String label){
        //获取选中的起始位置和结束位置
        selectionStart = doccontent.getSelectionStart();
        selectionEnd = doccontent.getSelectionEnd();

        String txt = doccontent.getText().toString();
        substring = txt.substring(selectionStart, selectionEnd);
        dotaskcolor = colormap.get(labelid);

        ClickableSpan clickableSpan = new ClickableSpan() {

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                dotaskcolor = colormap.get(labelid);
                //设置选中的文本的颜色
                //ds.setColor(Color.RED);
                ds.setColor(Color.parseColor(dotaskcolor));
                //设置下划线
                ds.setUnderlineText(true);
            }

            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(), "请不要点我+substring="+substring, Toast.LENGTH_SHORT).show();
                //spannableString.setSpan(clickableSpannoline, selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            }
        };
        spannableString.setSpan(clickableSpan, selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        //spannableString.removeSpan(clickableSpan);
        doccontent.setText(spannableString);
        doccontent.setMovementMethod(LinkMovementMethod.getInstance());
        //给选中的标签设置颜色
        for(int i=0;i<labellist.size();i++){
            TextView textView = labellist.get(i);
            if(textView.getText().toString().equals(label)){
                //textView.setBackgroundResource(R.drawable.red_sold_round_sel);
                textView.setBackgroundColor(Color.parseColor(dotaskcolor));
                textView.setTextColor(Color.WHITE);
                Log.e("params---->", "Post方式请求成功，dotaskcolor--->" + dotaskcolor);
                break;
            }
        }
        lid = labelid;
        ThreadPool.fixedThreadPool().submit(runnable);
    }
    //页面加载时就初始化已经做过的任务
    public void inittaskextract(ArrayList<String> colors,ArrayList<Integer> index_begins,ArrayList<Integer> index_ends,final ArrayList<Integer> label_ids){
           //截取选中的文本
           String txt = doccontent.getText().toString();
           for(int i=0;i<index_begins.size();i++){
               //获取选中的起始位置和结束位置
               selectionStart = index_begins.get(i);
               selectionEnd = index_ends.get(i);

               substring = txt.substring(selectionStart, selectionEnd);
               final String labelname = hashmaplabel.get(label_ids.get(i));
               //随机生成一个颜色
               colorstr = "#"+getRandColorCode();
               index = i;
               color = colormap.get(label_ids.get(index));
               Log.e("params---->", "Post方式请求成功，hashcolors-----hashcolors--->" + color);
               ClickableSpan clickableSpan = new ClickableSpan() {
                   @Override
                   public void updateDrawState(TextPaint ds) {
                       super.updateDrawState(ds);
                       //设置文件颜色
                       //ds.setColor(Color.RED);
                       String colorcon = colorstr;
                       Log.e("params---->", "Post方式请求成功，colorstr--->" + colorcon);
                       //ds.setColor(Color.parseColor(colorcon));
                       color = colormap.get(label_ids.get(index));
                       ds.setColor(Color.parseColor(color));
                       Log.e("params---->", "Post方式请求成功，colorstrstrtt--->" +index + color);
                       //设置下划线
                       ds.setUnderlineText(true);
                   }
                   @Override
                   public void onClick(View view) {
                       //Toast.makeText(getContext(), "请不要点我+substring="+substring, Toast.LENGTH_SHORT).show();
                       Toast.makeText(getContext(),labelname, Toast.LENGTH_SHORT).show();
                       //spannableString.setSpan(clickableSpannoline, selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                   }
               };
               //给选中的文本设置颜色
               //spannableString.setSpan(clickableSpan,selectionStart,selectionEnd,Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

               //spannableString.setSpan(new ForegroundColorSpan(Color.parseColor(color)), selectionStart, selectionEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
               spannableString.setSpan(new ForegroundColorSpan(Color.parseColor(colors.get(i))), selectionStart, selectionEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

               //spannableString.setSpan(clickableSpan,selectionStart,selectionEnd,Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
               doccontent.setText(spannableString);
               doccontent.setMovementMethod(LinkMovementMethod.getInstance());
               //给选中的标签设置颜色
               for(int j=0;j<labellist.size();j++){
                   TextView textView = labellist.get(j);
                   //Log.e("params---->", "Post方式请求成功，hashmaplabel--->" + hashmaplabel.get(label_ids.get(i)));
                   if(textView.getText().toString().equals(hashmaplabel.get(label_ids.get(i)))){
                       textView.setBackgroundResource(R.drawable.red_sold_round_sel);
                       //设置成和文本一样的颜色
                       Log.e("params---->", "Post方式请求成功，colorstrparams--->" + colorstr);
                       //textView.setBackgroundColor(Color.parseColor(colorstr));
                       //textView.setBackgroundColor(Color.parseColor(colormap.get(label_ids.get(i))));
                       textView.setBackgroundColor(Color.parseColor(colors.get(i)));
                       //textView.setBackgroundColor(Color.parseColor("#FFFF33"));
                       textView.setTextColor(Color.WHITE);
                       break;
                   }
               }
           }
    }



    @Override
    protected View initRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "initRootView");
        return inflater.inflate(R.layout.fragment_dotask_extract,container,false);

    }



    @Override
    protected void findViewById(View view) {
         fragmentlayout = (LinearLayout)view.findViewById(R.id.section_label);
         //btnbutton = (LinearLayout)view.findViewById(R.id.btnbutton);
         //存放content的textview
         doccontent = (TextView) view.findViewById(R.id.doccontent);
       //  completecon = (TextView) view.findViewById(R.id.completecon);
       //  completedoc = (TextView) view.findViewById(R.id.completedoc);
         extractlinear = (LinearLayout) view.findViewById(R.id.extractlinear);
         //和设置标签相关的
         labelview = (FlowGroupView)view.findViewById(R.id.flowgroupview2);

        // 获取整个应用的Application对象
        // 在不同的Activity中获取的对象是同一个
        mApplication = (MyApplication)getActivity().getApplication();
        userId = mApplication.getLoginUserId();
        Log.e("params---->", "Post方式请求成功，userID--->" + userId);

         mPb = view.findViewById(R.id.pb);
         if (mLoadDataFinished) {
             fragmentlayout.setVisibility(View.VISIBLE);
            mPb.setVisibility(View.GONE);
         }

    }



    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            String requestUrl = Constant.extradotaskUrl;
            String params ="?taskId="+taskid+"&docId="+docid+"&paraId="+contentid+"&labelId="+lid+"&indexBegin="+selectionStart+"&indexEnd="+selectionEnd+"&userId="+userId;
            Log.e("params---->", "Post方式请求成功，params--->" + params);
            Log.e("taskid---->", "Post方式请求成功，addtaskresult--->" + taskid);
            String result = HttpUtil.requestPost(requestUrl,params);
            Log.e("listview---->", "Post方式请求成功，result--->" + result);
        }
    };


    private Runnable pararunnable = new Runnable() {
        @Override
        public void run() {
            // TODO: http request.
            String requestUrl = Constant.extradtparaUrl;;
            //要传递的参数
            String params ="?taskId="+taskid+"&docId="+docid+"&paraId="+contentid+"&userId="+userId;
            Log.e("params---->", "Post方式请求成功，pararunnable--->" + params);
            Log.e("taskid---->", "Post方式请求成功，pararunnable--->" + taskid);
            String result = HttpUtil.requestPost(requestUrl,params);
            Log.e("listview---->", "Post方式请求成功，pararunnable--->" + result);
            //等待请求结束
            JSONObject jsonObject= (JSONObject) JSON.parse(result);
            Integer data = (Integer) jsonObject.getInteger("status");
            if(data.toString().equals("0")){
                String loginres = jsonObject.getString("msg");
                Looper.prepare();
                Toast.makeText(getContext(),loginres,Toast.LENGTH_LONG).show();
                Looper.loop();
            }else{
                String loginres = jsonObject.getString("msg");
                Looper.prepare();
                Toast.makeText(getContext(),loginres,Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }
    };

    private Runnable docrunnable = new Runnable() {
        @Override
        public void run() {
            // TODO: http request.
            String requestUrl = Constant.extradtdocUrl;
            //要传递的参数
            String params ="?taskId="+taskid+"&docId="+docid+"&userId="+userId;
            Log.e("params---->", "Post方式请求成功，docrunnable--->" + params);
            Log.e("taskid---->", "Post方式请求成功，docrunnable--->" + taskid);
            String result = HttpUtil.requestPost(requestUrl,params);
            Log.e("listview---->", "Post方式请求成功，docrunnable--->" + result);
            //等待请求结束
            JSONObject jsonObject= (JSONObject) JSON.parse(result);
            Integer data = (Integer) jsonObject.getInteger("status");
            if(data.toString().equals("0")){
                String loginres = jsonObject.getString("msg");
                Looper.prepare();
                Toast.makeText(getContext(),loginres,Toast.LENGTH_LONG).show();
                Looper.loop();
            }else{
                //上传的文件不符合要求
                //4011,"msg":"该文件你的段落还没有全部完成"
                String loginres = jsonObject.getString("msg");
                Looper.prepare();
                Toast.makeText(getContext(),loginres,Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }
    };

    @Override
    public void saveIns() {
        ThreadPool.fixedThreadPool().submit(runnable);
    }

    @Override
    public void completeCon() {
        ThreadPool.fixedThreadPool().submit(pararunnable);

    }

    @Override
    public void completeDoc() {
        ThreadPool.fixedThreadPool().submit(docrunnable);
    }


    private class MyActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

        }
    }

    /**
     * 添加标签
     * @param labelId
     * @param labelName
     */
    public void addTag(int labelId, String labelName){
        selectionStart = doccontent.getSelectionStart();
        selectionEnd = doccontent.getSelectionEnd();

        if (selectionStart >= selectionEnd || selectionEnd == 0 )
            Toast.makeText(getActivity(),"请先选择文本信息",Toast.LENGTH_SHORT).show();
        else
            dotaskextract(labelId,labelName);
    }


    public String getSelectedContent(){
        selectionStart = doccontent.getSelectionStart();
        selectionEnd = doccontent.getSelectionEnd();

        return doccontent.getText().toString().substring(selectionStart,selectionEnd);
    }

}