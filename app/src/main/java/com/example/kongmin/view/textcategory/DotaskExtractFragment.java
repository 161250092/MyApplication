package com.example.kongmin.view.textcategory;

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
import com.example.kongmin.Constant.Constant;
import com.example.kongmin.myapplication.R;
import com.example.kongmin.view.lazyfragment.BaseLazyFragment;
import com.example.kongmin.util.*;

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

/**
 * A placeholder fragment containing a simple view.
 */
public class DotaskExtractFragment extends BaseLazyFragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_CONTENT_NUMBER = "content_number";

    //设置加载动画
    //private TextView mTextView;
    private ProgressBar mPb;
    private Handler mHandler = new Handler();
    private String mData;

    private LinearLayout fragmentlayout;
    //private LinearLayout btnbutton;

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


    //private List<TextView> labellist = new ArrayList<TextView>();

    //private static String label;
    //private static String content;

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

    public DotaskExtractFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */

    //新建Fragment
    public static DotaskExtractFragment newInstance(int sectionNumber) {
        DotaskExtractFragment fragment = new DotaskExtractFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        //args.putInt(ARG_CONTENT_NUMBER, contentid);
        fragment.setArguments(args);
        return fragment;
    }


   /* @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dotask_extract, container, false);
        //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        TextView textView2 = (TextView) rootView.findViewById(R.id.section_label2);
        //textView2.setText(getString(R.string.section_format,getArguments().getInt(ARG_CONTENT_NUMBER)));
        TextView textView3 = (TextView) rootView.findViewById(R.id.complete);
        initData(textView2,textView3);
        //和设置标签相关的
        labelview =  rootView.findViewById(R.id.flowgroupview2);
        //每次加载之前需要清空一下，否则会有数据重复加载的问题
        names.clear();
        setData();
        for (int i = 0; i < names.size(); i++) {
            addTextView(names.get(i));
        }
        return rootView;
    }*/

    public void initData(TextView textView2,TextView textView3,final String selectlabel){
        /*textView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(getContext(), "点击了textView1", Toast.LENGTH_SHORT).show();
                return true;
            }
        });*/
        /*textView2.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(getContext(), "点击了textView2", Toast.LENGTH_SHORT).show();
                return true;
            }
        });*/
        textView3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                /*for(int i=0;i<checked.size();i++){
                    str+=checked.get(i);
                }
                checked.clear();*/
                Toast.makeText(getContext(), "选中了"+selectlabel+" ", Toast.LENGTH_SHORT).show();
            }
        });
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
                    //tv.setBackgroundResource(R.drawable.red_sold_round_sel);
                    //tv.setTextColor(Color.WHITE);
                    checked.add(tv.getText().toString());
                    //Toast.makeText(getContext(), tv.getText().toString()+"选中了", Toast.LENGTH_SHORT).show();
                    button = true;
                }else{
                    //tv.setBackgroundResource(R.drawable.line_rect_huise);
                    //tv.setTextColor(Color.BLACK);
                    //names.put(tv.getText().toString(),false);
                    checked.remove(tv.getText().toString());
                    //Toast.makeText(getContext(), tv.getText().toString()+"取消选中了", Toast.LENGTH_SHORT).show();
                    button = false;
                }
                for(int i=0;i<checked.size();i++){
                    selectlabel+=checked.get(i);
                }
                //initData(doccontent,completecon,selectlabel);

            }
        });
    }

    /*private void setData(){
        names.add("降龙十八掌");
        names.add("黯然销魂掌");
        names.add("左右互搏术");
        names.add("七十二路空明拳");
        names.add("小无相功");
        names.add("拈花指");
        names.add("打狗棍法");
        names.add("蛤蟆功");
        names.add("九阴白骨爪");
        names.add("一招半式闯江湖");
        names.add("醉拳");
        names.add("龙蛇虎豹");
        names.add("葵花宝典");
        names.add("吸星大法");
        names.add("如来神掌警示牌");
    }*/

    /*Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            label = data.getString("label");
            Log.i("myloglebel", "请求结果为-->" + label);
            label = data.getString("content");
            Log.i("mylogcontent", "请求结果为-->" + content);
            // TODO
            // UI界面的更新等相关操作
        }
    };*/

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
                    //btnbutton.setVisibility(View.VISIBLE);
                    //每次加载之前需要清空一下，否则会有数据重复加载的问题
                    names.clear();

                    Bundle bundle =getArguments();
                    /*String message = null;
                    if(bundle!=null){
                        message = bundle.getString("toFragment");
                    }
                    Log.e("DotaskExtract---->", "GET方式请求成功，toFragment--->" + message);*/
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

                    if(colorlist!=null && colorlist.size()>0){
                        for(int i=0;i<colorlist.size();i++){
                            Log.e("DotaskExtract---->", "GET方式请求成功，colorlist--->" + colorlist);
                            Log.e("DotaskExtract---->", "GET方式请求成功，beginlist--->" + beginlist);
                            Log.e("DotaskExtract---->", "GET方式请求成功，endlist--->" + endlist);
                            Log.e("DotaskExtract---->", "GET方式请求成功，content--->" + labelidlist);
                        }
                    }

                    Log.e("DotaskExtract---->", "GET方式请求成功，taskid--->" + taskid);
                    Log.e("DotaskExtract---->", "GET方式请求成功，contentid--->" + contentid);
                    Log.e("DotaskExtract---->", "GET方式请求成功，contentindex--->" + contentindex);
                    Log.e("DotaskExtract---->", "GET方式请求成功，content--->" + content);
                    Log.e("DotaskExtract---->", "GET方式请求成功，userid--->" + userid);

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
        //Log.i(TAG,"selectionStart="+selectionStart+",selectionEnd="+selectionEnd);
        //截取选中的文本
        //String txt = content.getText().toString();
        String txt = doccontent.getText().toString();
        substring = txt.substring(selectionStart, selectionEnd);
        dotaskcolor = colormap.get(labelid);
        //Log.i(TAG,"substring="+substring);
       /* SpannableStringBuilder spanText=new SpannableStringBuilder(content.getText().toString());
        spanText.setSpan(new ClickableSpan() {

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.RED);       //设置文件颜色
                ds.setUnderlineText(true);      //设置下划线
            }

            @Override
            public void onClick(View view) {

                final Snackbar snackbar = Snackbar.make(content,"别点我",Snackbar.LENGTH_LONG);
                snackbar.show();
                snackbar.setAction("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
            }
        }, selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);*/
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
        //Toast.makeText(getContext(), "点击了标注"+labelid+label+"selectionStart="+selectionStart+ ",selectionEnd="+selectionEnd+"substring="+substring, Toast.LENGTH_SHORT).show();
        lid = labelid;
        new Thread(runnable).start();
    }
    //页面加载时就初始化已经做过的任务
    public void inittaskextract(ArrayList<String> colors,ArrayList<Integer> index_begins,ArrayList<Integer> index_ends,final ArrayList<Integer> label_ids){
           //截取选中的文本
           String txt = doccontent.getText().toString();
           for(int i=0;i<index_begins.size();i++){
               //获取选中的起始位置和结束位置
               selectionStart = index_begins.get(i);
               selectionEnd = index_ends.get(i);
               //Log.e("params---->", "Post方式请求成功，selectionStart--->" + selectionStart);
               //Log.e("params---->", "Post方式请求成功，selectionStart--->" + selectionEnd);
               //Log.e("params---->", "Post方式请求成功，selectionStart--->" + txt);
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

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // TODO: http request.
            //String requestUrl = "http://10.0.2.2:8080/dotask/addDoTask?";
            //String requestUrl = "http://172.20.10.5:8080/dotask/addDoTask";
            String requestUrl = Constant.extradotaskUrl;
            //要传递的参数
            //int taskId,int docId,int paraId,int labelId,int indexBegin,int indexEnd，
            //String params ="?taskId="+taskid+"&contentid="+contentid+"&labelId="+lid+"&conbegin="+selectionStart+"&conend="+selectionEnd;
            String params ="?taskId="+taskid+"&docId="+docid+"&paraId="+contentid+"&labelId="+lid+"&indexBegin="+selectionStart+"&indexEnd="+selectionEnd+"&userId="+userId;
            Log.e("params---->", "Post方式请求成功，params--->" + params);
            Log.e("taskid---->", "Post方式请求成功，addtaskresult--->" + taskid);
            String result = HttpUtil.requestPost(requestUrl,params);
            Log.e("listview---->", "Post方式请求成功，result--->" + result);
        }
    };


    /**
     * 网络操作相关的子线程
     */
    /*Runnable networkTask = new Runnable() {
        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
            Message msg = new Message();
            Bundle data = new Bundle();
            String requestUrl = "http://172.20.10.5:8080/label/getLabelByTask";
            String paramUrl = "?taskid=1";
            String label = HttpUtil.requestGet(requestUrl,paramUrl);
            Log.e("DotaskExtract---->", "GET方式请求成功，result--->" + label);
            String requestUrl2 = "http://172.20.10.5:8080/content/getContent";
            String paramUrl2 = "?docId=1";
            String content = HttpUtil.requestGet(requestUrl2,paramUrl2);
            Log.e("DotaskExtract2---->", "GET方式请求成功，result2--->" + content);
            data.putString("label", label);
            data.putString("content", content);
            msg.setData(data);
            mHandler.sendMessage(msg);
        }
    };*/

    @Override
    protected View initRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "initRootView");
        //return inflater.inflate(R.layout.fragment_tab, container, false);
        // 开启一个子线程，进行网络操作，等待有返回结果，使用handler通知UI
        //new Thread(networkTask).start();
        return inflater.inflate(R.layout.fragment_dotask_extract,container,false);

    }



    @Override
    protected void findViewById(View view) {
        /*mTextView = view.findViewById(R.id.section_label);
        mPb = view.findViewById(R.id.pb);
        if (mLoadDataFinished) {
            // 一般情况下这时候数据请求都还没完成, 所以不会进这个if
            mTextView.setVisibility(View.VISIBLE);
            mTextView.setText(mData);
            mPb.setVisibility(View.GONE);
        }*/

         fragmentlayout = (LinearLayout)view.findViewById(R.id.section_label);
         //btnbutton = (LinearLayout)view.findViewById(R.id.btnbutton);
         //存放content的textview
         doccontent = (TextView) view.findViewById(R.id.doccontent);
         completecon = (TextView) view.findViewById(R.id.completecon);
         completedoc = (TextView) view.findViewById(R.id.completedoc);
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
            // 一般情况下这时候数据请求都还没完成, 所以不会进这个if
             fragmentlayout.setVisibility(View.VISIBLE);
             //btnbutton.setVisibility(View.VISIBLE);
            //mTextView.setText(mData);
            mPb.setVisibility(View.GONE);
         }
         initBtn();
    }

    public void initBtn(){
        completecon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(pararunnable).start();
            }
        });
        completedoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(docrunnable).start();
            }
        });
    }

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

    //和菜单有关的
    private class MyActionModeCallback implements ActionMode.Callback {
        private Menu mMenu;
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            MenuInflater menuInflater = actionMode.getMenuInflater();
            menu.clear();
            for(Integer labelid:hashmaplabel.keySet()){
                menu.add(Menu.NONE, Menu.FIRST + labelid, Menu.FIRST + labelid,hashmaplabel.get(labelid));
            }
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            //菜单创建完成以后获取到其对象，便于后续操作
            this.mMenu=menu;
            return true;
        }


        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            for(Integer labelid:hashmaplabel.keySet()){
                if(menuItem.getItemId()==Menu.FIRST + labelid){
                    String label =  hashmaplabel.get(labelid);
                    dotaskextract(labelid,label);
                    break;
                }
            }
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {

        }
    }
}