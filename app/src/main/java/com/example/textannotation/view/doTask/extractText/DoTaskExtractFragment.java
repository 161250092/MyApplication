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
import com.example.textannotation.model.doTask.ITaskFragment;
import com.example.textannotation.myapplication.R;
import com.example.textannotation.network.OkHttpUtil;
import com.example.textannotation.util.threadPool.ThreadPool;
import com.example.textannotation.view.doTask.ResolveHttpResponse;
import com.example.textannotation.view.lazyfragment.BaseLazyFragment;
import com.example.textannotation.util.*;

import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.text.Spanned;

import java.io.IOException;
import java.util.*;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 *
 * 信息抽取界面
 * Created by kongmin
 * 2018.12.29
 */

public class DoTaskExtractFragment extends BaseLazyFragment implements ITaskFragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    //设置加载动画
    //private TextView mTextView;
    private ProgressBar mPb;
    private Handler mHandler = new Handler();
    private String mData;

    private LinearLayout fragmentlayout;

    //段落内容
    private TextView doccontent;

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

    private String content;
    private int userid;

    private int docid;

    private int pid;

    //设置样式的时候用的
    private SpannableStringBuilder spannableString;
    private Map<Integer,String> hashmaplabel = new LinkedHashMap<Integer,String>();

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
       // loadData();
        getCurrentTask();
    }

    private void getCurrentTask(){

        mHandler.post(new Runnable() {
            @Override
            public void run() {

                fragmentlayout.setVisibility(View.VISIBLE);

                names.clear();

                Bundle bundle = getArguments();


                taskid =  bundle.getInt("taskid");
                docid = bundle.getInt("docid");
                typename = bundle.getString("type");


                userid = bundle.getInt("userid");


                //任务标签
                labelMap = (SerializableMap)bundle.get("lebelmap");
                hashmap = labelMap.getMap();

                colors = bundle.getStringArrayList("colors");
                colorsMap = (SerializableSortMap) bundle.get("colormap");
                colormap = colorsMap.getMap();

                for(String labelname:hashmap.keySet()){
                    int labelid = hashmap.get(labelname);
                    //给标签数组赋值
                    names.add(labelname);
                    hashmaplabel.put(labelid,labelname);
                    Log.e("DotaskExtract---->", "GET方式请求成功，labelname+labelid--->" + labelname+labelid);
                }

                String param = "?docId="+docid+"&taskId="+taskid+"&userId="+userid;
                Log.e("Extract",Constant.getNextExtractTask + param);
                OkHttpUtil.sendGetRequest(Constant.getNextExtractTask + param, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        Log.e("extract",result);
                        JSONObject jsonObject = JSONObject.parseObject(result);
                        JSONObject  data = jsonObject.getJSONObject("data");
                        pid = data.getInteger("pid");
                        content = data.getString("paracontent");
                        initView();
                    }
                });
            }
        });

    }




    public void initView(){
        mHandler.post(new Runnable() {
            @Override
            public void run() {

                doccontent.setText(content);
                doccontent.setCustomSelectionActionModeCallback(new MyActionModeCallback());
                spannableString = new SpannableStringBuilder();
                spannableString.append(doccontent.getText().toString());

                for (int i = 0; i < names.size(); i++) {
                    addTextView(names.get(i));
                }
                mPb.setVisibility(View.GONE);
            }
        });


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
        Log.e("text",txt);


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
            }
        };

        spannableString.setSpan(clickableSpan, selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        //spannableString.removeSpan(clickableSpan);
        doccontent.setText(spannableString);
        Log.e("text",spannableString.toString());
        doccontent.setMovementMethod(LinkMovementMethod.getInstance());
        //给选中的标签设置颜色
        for(int i=0;i<labellist.size();i++){
            TextView textView = labellist.get(i);
            if(textView.getText().toString().equals(label)){
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

         doccontent = (TextView) view.findViewById(R.id.doccontent);

         //和设置标签相关的
         labelview = (FlowGroupView)view.findViewById(R.id.flowgroupview2);

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
            String params ="?taskId="+taskid+"&docId="+docid+"&paraId="+pid+"&labelId="+lid+"&indexBegin="+selectionStart+"&indexEnd="+selectionEnd+"&userId="+userId;
            Log.e("params---->", "Post方式请求成功，params--->" + params);

            String result = HttpUtil.requestPost(requestUrl,params);
            ResolveHttpResponse.showHttpResponse(result,getActivity());
        }
    };

    private void updateContent() {
        // sleep 1 second in case loading info disappear too fast
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                initLabelText();
                ((DoTaskExtractActivity)getActivity()).hideLoading();
                doccontent.setText(content);
                spannableString = new SpannableStringBuilder();
                spannableString.append(doccontent.getText().toString());
            }
        });
    }



    private void initLabelText(){
        for (TextView textView:labellist) {
            initEvents(textView);
            textView.setBackgroundResource(R.drawable.line_rect_huise);
            textView.setTextColor(Color.BLACK);
        }
    }

    private Runnable passCurrentTaskParagraph = new Runnable(){

        @Override
        public void run() {
            String requestUrl = Constant.passCurrentExtractTask;
            String params = "?docId="+docid+"&paraId="+pid+"&taskId="+taskid+"&userId="+userId;
            String result = HttpUtil.requestGet(requestUrl,params);
            Log.e("OneCategory",requestUrl+params);
            Log.e("OneCategory", result);
            JSONObject jsonObject = JSONObject.parseObject(result);
            JSONObject  data = jsonObject.getJSONObject("data");
            pid = data.getInteger("pid");
            content = data.getString("paracontent");
            updateContent();
        }
    };


    private Runnable getNextTaskParagraph = new Runnable(){
        @Override
        public void run() {
            final String requestUrl = Constant.getNextExtractTask;
            String params = "?docId="+docid+"&taskId="+taskid+"&userId="+userId;
            OkHttpUtil.sendGetRequest(requestUrl + params, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    JSONObject jsonObject = JSONObject.parseObject(result);
                    JSONObject  data = jsonObject.getJSONObject("data");
                    if (data == null)
                        return;

                    pid = data.getInteger("pid");
                    content = data.getString("paracontent");
                    updateContent();
                }
            });
        }
    };






    @Override
    public void saveAnnotationInfo() {
        ThreadPool.fixedThreadPool().submit(runnable);
    }

    @Override
    public void getCurrentTaskParagraph() {

    }

    @Override
    public void doNextTask() {
        ThreadPool.fixedThreadPool().submit(getNextTaskParagraph);
    }

    @Override
    public void passCurrentTask() {
        ThreadPool.fixedThreadPool().submit(passCurrentTaskParagraph);
    }

    private String errorInfo = "";
    private Runnable submitErr = new Runnable() {

        @Override
        public void run() {
            final String requestUrl = Constant.submitErrorUrl;
            String params = "?docId="+docid+"&paraId="+pid+"&msg="+errorInfo+"&taskId="+taskid+"&userId="+userId;
            String result = HttpUtil.requestGet(requestUrl,params);

            OkHttpUtil.sendGetRequest(requestUrl + params, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.e("OneCategory", response.body().string());
                }
            });

        }
    };

    @Override
    public void submitErrors(String text) {
        errorInfo = text;
        ThreadPool.fixedThreadPool().submit(submitErr);
    }

    @Override
    public void compareOthersTextAnnotation() {
        ThreadPool.fixedThreadPool().submit(compareOthersLabels);
    }
    private Runnable compareOthersLabels = new Runnable() {

        @Override
        public void run() {

            String requestUrl = Constant.compareWithOtherExtractAnnotation;
            String params = "?docId="+docid+"&taskId="+taskid+"&paraId="+pid+"&userId="+userId;
            Log.e("OneCategory",requestUrl+params);
            String result = HttpUtil.requestPost(requestUrl,params);
            Log.e("OneCategory", result);
            showCompareInfo(result);
        }
    };

    public void showCompareInfo( final String msg){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ((DoTaskExtractActivity)getActivity()).showOthersAnnotation(msg);
            }
        });
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