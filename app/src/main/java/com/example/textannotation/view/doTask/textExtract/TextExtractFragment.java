package com.example.textannotation.view.doTask.textExtract;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.textannotation.myapplication.R;
import com.example.textannotation.pojo.extract.DoExtractionData;
import com.example.textannotation.pojo.extract.Entity;
import com.example.textannotation.pojo.extract.Relation;
import com.example.textannotation.presenter.doTask.extract.ITextExtractPresenter;
import com.example.textannotation.presenter.doTask.extract.TextExtractPresenter;
import com.example.textannotation.view.common.FlowGroupView;
import com.example.textannotation.util.SerializableMap;
import com.example.textannotation.util.SerializableSortMap;
import com.example.textannotation.view.common.BaseLazyFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * 信息抽取界面
 * update by mwx
 * 点击 span 一下表示选中,如果这是第一个选中的，则存储。第二个选中另一个span，弹窗选择关系。第二次选择上一个，则删除第一个span。
 */

public class TextExtractFragment extends BaseLazyFragment implements ITextExtractMenu,ITextExtractView {

    private static final String ARG_SECTION_NUMBER = "section_number";
    //设置加载动画
    private ProgressBar mPb;
    private Handler mHandler = new Handler();
    private LinearLayout fragmentlayout;
    //段落内容
    private TextView doccontent;
    //实体标签
    private ArrayList<String> names = new ArrayList<String>();
    private FlowGroupView labelview;
    private List<TextView> labellist = new ArrayList<>();

    //关系标签
    private  ArrayList<String> relationTabList = new ArrayList<>();
    private FlowGroupView relationViewGroup;
    private List<TextView> relationViews = new ArrayList<>();


    private int taskid;
    //是做任务页面还是查看做任务页面
    private String typename;
    private SerializableMap labelMap;
    private Map<String,Integer> hashmap = new LinkedHashMap<>();
    private HashMap<String,ClickableSpan> selectedTextSpan = new HashMap<>();

    private String content;
    private int userid;
    private int pid;

    //设置样式的时候用的
    private SpannableStringBuilder spannableString;
    private Map<Integer,String> hashmaplabel = new LinkedHashMap<Integer,String>();

    //信息抽取标签颜色
    private ArrayList<String> colors = new ArrayList<>();
    private String dotaskcolor;
    private SerializableSortMap colorsMap;
    private Map<Integer,String> colormap = new LinkedHashMap<Integer,String>();

    //待发送的实体和关系数据
    private List<Entity> entities = new ArrayList<>();
    private List<Relation> relations = new ArrayList<>();
    private Relation relation_temp;
    //封装标注数据
    private DoExtractionData doExtractionData;

    private ITextExtractPresenter iTextExtractPresenter;


    //新建Fragment
    public static TextExtractFragment newInstance(int sectionNumber) {
        TextExtractFragment fragment = new TextExtractFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    //设置实体标签
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
        labelview.addView(child);
    }
    //设置关系标签
    private void addRelationTabView(String str){
        TextView child = new TextView(getContext());
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
        params.setMargins(15, 15, 15, 15);
        child.setLayoutParams(params);
        child.setBackgroundResource(R.drawable.line_rect_huise);
        child.setText(str);
        child.setPadding(20,5,20,5);
        child.setTextColor(Color.BLACK);
        relationViews.add(child);
        relationViewGroup.addView(child);
    }

    //设置加载动画
    @Override
    public void loadDataStart() {
        Bundle bundle = getArguments();
        taskid =  bundle.getInt("taskid");
        typename = bundle.getString("type");
        userid = bundle.getInt("userid");
        //任务标签
        labelMap = (SerializableMap)bundle.get("lebelmap");
        hashmap = labelMap.getMap();

        colors = bundle.getStringArrayList("colors");
        colorsMap = (SerializableSortMap) bundle.get("colormap");
        colormap = colorsMap.getMap();
        relationTabList = bundle.getStringArrayList("relationList");

        for (String s:relationTabList){
            Log.e("fragment",s);
        }

        for(String labelname:hashmap.keySet()){
            int labelid = hashmap.get(labelname);
            names.add(labelname);
            hashmaplabel.put(labelid,labelname);
            Log.e("fragment", "GET方式请求成功，labelname+labelid--->" + labelname+labelid);
        }
        doExtractionData = new DoExtractionData();
        doExtractionData.setTaskId(taskid);
        doExtractionData.setUserId(userid);
        relation_temp = new Relation();
        iTextExtractPresenter = new TextExtractPresenter(this);
        iTextExtractPresenter.loadDataAndInitView(taskid,0,userid);

    }

    private int selectionStart = 0;
    private int selectionEnd = 0;
    private int lid = 0;


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
        relationViewGroup = (FlowGroupView)view.findViewById(R.id.relationTab);

         mPb = view.findViewById(R.id.pb);
         if (mLoadDataFinished) {
             fragmentlayout.setVisibility(View.VISIBLE);
            mPb.setVisibility(View.GONE);
         }

    }


    private void initLabelText(){
        for (TextView textView:labellist) {
            textView.setBackgroundResource(R.drawable.line_rect_huise);
            textView.setTextColor(Color.BLACK);
        }
    }

    @Override
    public void getLastTask() {
        iTextExtractPresenter.getLastTask(taskid,pid,userid);
    }

    @Override
    public void getNextTask() {
        iTextExtractPresenter.getNextTask(taskid,pid,userid);
    }

    @Override
    public void saveAnnotationInfo() {
        if(entities.size()==0)
            return;

        doExtractionData.setSubtaskId(pid);
        doExtractionData.setEntities(entities);
        doExtractionData.setRelations(relations);
        iTextExtractPresenter.saveAnnotationInfo(doExtractionData);
    }

    @Override
    public String extractText() {
        selectionStart = doccontent.getSelectionStart();
        selectionEnd = doccontent.getSelectionEnd();
        if (selectionStart >= selectionEnd || selectionEnd == 0 ) {
            Toast.makeText(getActivity(), "请先选择文本信息", Toast.LENGTH_SHORT).show();
            return null;
        }
        return doccontent.getText().toString().substring(selectionStart,selectionEnd);
    }

    @Override
    public void cancelRelationAnnotation() {
        if (relations == null ||relations.size()==0)
            return;

        String[] entity_relation_list = new String[relations.size()];
        for (int i = 0; i < relations.size(); i++){
            entity_relation_list[i] = relations.get(i).getHeadEntity()+"  "+relations.get(i).getRelation()+"  "+relations.get(i).getTailEntity();
        }
        ((TextExtractActivity)getActivity()).showEntityRelationList(entity_relation_list);
    }

    public void cancelAnnotation(int selectionStart,int selectionEnd){
        relation_temp.setHeadEntity(null);
        for(int i = 0; i < entities.size(); i++){
            Entity entity = entities.get(i);
            if (entity.getStartIndex() == selectionStart && entity.getEndIndex() == selectionEnd){
                entities.remove(i);
                ClickableSpan clickableSpan = selectedTextSpan.get(selectionStart+"&"+selectionEnd);
                spannableString.removeSpan(clickableSpan);
                doccontent.setText(spannableString);
                break;
            }
        }
    }

    public void deleteEntityRelation(int index){
        Log.e("fragment",index+"");
        relations.remove(index);
    }

    @Override
    public void initView(int pid, final String content) {
        Log.e("view","init view");
        this.pid = pid;
        this.content = content;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                fragmentlayout.setVisibility(View.VISIBLE);
                doccontent.setText(content);
                spannableString = new SpannableStringBuilder();
                spannableString.append(doccontent.getText().toString());
                for (int i = 0; i < names.size(); i++) {
                    addTextView(names.get(i));
                }
                if(relationTabList != null && relationTabList.size() >0 ){
                    for (String tabName:relationTabList)
                     addRelationTabView(tabName);
                }
                else {
                    relationViewGroup.setVisibility(View.GONE);
                }

                mPb.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void updateContent(int pid, final String content) {

        Log.e("extract view","1");

        this.pid = pid;
        this.content = content;
        entities.clear();
        relations.clear();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        Log.e("extract view","2");

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                initLabelText();
                ((TextExtractActivity)getActivity()).hideLoading();
                doccontent.setText(content);
                spannableString = new SpannableStringBuilder();
                spannableString.append(doccontent.getText().toString());
            }
        });
    }

    @Override
    public void showSubmitInfo(final String msg) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ((TextExtractActivity)getActivity()).showSubmitInfo(msg);
            }
        });
    }

    @Override
    public void showCompletedInfo(String msg) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ((TextExtractActivity)getActivity()).showTaskCompletedInfo();
            }
        });
    }

    @Override
    public void showExceptionInfo(final String msg) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ((TextExtractActivity)getActivity()).showNotice(msg);
            }
        });
    }

    @Override
    public void showSimpleInfo(final String msg) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ((TextExtractActivity)getActivity()).showNotice(msg);
            }
        });
    }


    /**
     * ui变动核心方法
     * 通过选择框选定文本，将相应文本显示出来
     */
    public void addTag(final String selectedText, final int labelid, final String labelName){
        String txt = doccontent.getText().toString();
        Log.e("text",txt);
        Log.e("text",selectionStart+" "+selectionEnd);

        dotaskcolor = colormap.get(labelid);
        ClickableSpan clickableSpan = new ClickableSpan() {
            final int start = selectionStart;
            final int end = selectionEnd;
            final String text = selectedText;
            int clickCount = 0;

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                dotaskcolor = colormap.get(labelid);
                ds.setColor(Color.parseColor(dotaskcolor));
                //设置下划线
                if (clickCount%2 == 0){
                    ds.setUnderlineText(false);
                } else {
                    ds.setUnderlineText(true);
                }

            }
            @Override
            public void onClick(View view) {
                clickCount++;
                /**
                 * 点击两次取消实体标注，
                 * 点击一次选中当前实体，两个实体被选中则进行关系标签选择
                 */
                if (clickCount == 2) {
                    cancelAnnotation(start,end);
                    Log.e("tag", "remove "+start+" "+end);
                }else if (clickCount == 1){
                    dealEntityClickEvent(text,start,end);
                }
                Log.e("tag", ""+clickCount);
            }
        };

        spannableString.setSpan(clickableSpan, selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        selectedTextSpan.put(selectionStart+"&"+selectionEnd,clickableSpan);

        doccontent.setText(spannableString);
        doccontent.setMovementMethod(LinkMovementMethod.getInstance());
        //给选中的标签设置颜色
        for(int i=0;i<labellist.size();i++){
            TextView textView = labellist.get(i);
            if(textView.getText().toString().equals(labelName)){
                textView.setBackgroundColor(Color.parseColor(dotaskcolor));
                textView.setTextColor(Color.WHITE);
                Log.e("params---->", "Post方式请求成功，dotaskcolor--->" + dotaskcolor);
                break;
            }
        }
        Entity entity = new Entity();
        entity.setStartIndex(selectionStart);
        entity.setEndIndex(selectionEnd);
        entity.setEntity(selectedText);
        entity.setEntityName(labelName);
        entities.add(entity);

        selectionStart = 0;
        selectionEnd = 0;
        lid = 0;
    }

    //点击一次选中当前实体，两个实体被选中则进行关系标签选择
    private void dealEntityClickEvent(String text,int startIndex,int endIndex){
        doccontent.setText(spannableString);
        //点第一个实体，则将其加入head
        if (relation_temp.getHeadEntity() == null || relation_temp.getHeadEntity().equals("")){
            relation_temp.setHeadEntity(text);
        } //点第二个实体，则将其加入tail
        else if (relation_temp.getHeadEntity() != null && !relation_temp.getHeadEntity().equals("")){
            relation_temp.setTailEntity(text);
            ((TextExtractActivity)getActivity()).showRelationList();
        }
    }

    public void addRelation(String relation){

        Relation relation1 = new Relation();
        relation1.setRelation(relation);
        relation1.setHeadEntity(relation_temp.getHeadEntity());
        relation1.setTailEntity(relation_temp.getTailEntity());
        relations.add(relation1);

        Log.e("fragment",relation1.getRelation()+" "+relation1.getHeadEntity()+" "+relation1.getTailEntity());
        relation_temp.setHeadEntity(null);
        relation_temp.setTailEntity(null);

    }




}