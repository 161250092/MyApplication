package com.example.kongmin.view.publictask;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.*;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.kongmin.keyboard.EmojiKeyboard;
import com.example.kongmin.myapplication.*;
import com.example.kongmin.pojo.ShowFile;
import com.example.kongmin.util.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import com.example.kongmin.Constant.Constant;
import com.example.kongmin.view.BaseActivity;
import com.example.kongmin.view.EditTextListActivity;
import com.example.kongmin.view.FileSelectActivity;
import com.example.kongmin.view.rebuild.MainActivity;
import com.example.kongmin.view.ShowFileAdapter;

/**
 * Created by kongmin on 2019/02/11.
 * 文本关系类别标注发任务界面
 */
public class AddListTwoitemsActivity extends BaseActivity {
    //标签的个数不能为空要有默认值

    //选择文件的弹出框
    private EmojiKeyboard emojiKeyboard;
    private final String TAG = "AddListTwoitemsActivity";

    private EditText title;
    private TextView type;
    private EditText instancelabelnum;
    private EditText item1labelnum;
    private EditText item2labelnum;
    private EditText deadline;
    private EditText content;
    //返回按钮
    private TextView cancel;
    //保存按钮
    private TextView save;
    //选择文件按钮
    private ImageView add_file;

    //和设置标签相关的
    private Flowlayout instancelabel;
    private Flowlayout item1label;
    private Flowlayout item2label;

    private ArrayList<TagItem> mAddTagsinstance = new ArrayList<TagItem>();
    private EditText inputLabelinstance;
    private String[] mTextStrinstance = {"instance"};
    private ArrayList<String>  listinstance = new ArrayList<String>();

    private ArrayList<TagItem> mAddTagsitem1 = new ArrayList<TagItem>();
    private EditText inputLabelitem1;
    private String[] mTextStritem1 = {"item1"};
    private ArrayList<String>  listitem1 = new ArrayList<String>();

    private ArrayList<TagItem> mAddTagsitem2 = new ArrayList<TagItem>();
    private EditText inputLabelitem2;
    private String[] mTextStritem2 = {"item2"};
    private ArrayList<String>  listitem2 = new ArrayList<String>();


    //上传文件的路径
    private String picturePath = "";
    //上传文件的大小
    private String pictureSize = "";
    //上传文件路径数组
    private List<String> picturePaths = new ArrayList<String>();
    //上传文件返回值相关
    protected static int RESULT_LOAD_FILE = 1;
    //发送post请求携带的参数
    //private Map<String,String> fileparams = new HashMap<String,String>();
    private Map<String,Object> fileparams = new HashMap<String,Object>();
    //上传文件对应的文件列表
    private ListView listfile;
    private LayoutInflater inflater;
    //存放文件的数组
    private ArrayList<ShowFile> array = new ArrayList<ShowFile>();
    //上传文件列表的适配器
    private ShowFileAdapter adapter;

    //声明PopupWindow
    private PopupWindow popupWindow;
    //声明PopupWindow对应的视图
    private View popupView;
    //声明平移动画
    private TranslateAnimation animation;

    private MyApplication mApplication;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_add_list_twoitems);
        setContentView(R.layout.activity_twoitems_main);

        // 获取整个应用的Application对象
        // 在不同的Activity中获取的对象是同一个
        mApplication = (MyApplication)getApplication();
        // 获取整个应用的Application对象
        // 在不同的Activity中获取的对象是同一个
        mApplication = (MyApplication)getApplication();
        userId = mApplication.getLoginUserId();

        //设置Edittext添加文本时换行时ScrollView可以自动滑动
        initKeyBoardListener((ScrollView) findViewById(R.id.scrollView));
        initText();
        initView();
    }

    private void initText(){
        title = (EditText) findViewById(R.id.twoitemstitle);
        type = (TextView) findViewById(R.id.twoitemstype);
        instancelabelnum = (EditText) findViewById(R.id.instancenum);
        item1labelnum = (EditText) findViewById(R.id.itemnum1);
        item2labelnum = (EditText) findViewById(R.id.itemnum2);
        deadline = (EditText) findViewById(R.id.twoitemsdeadline);
        content = (EditText) findViewById(R.id.twoitemscontent);

        //返回和保存按钮
        cancel = (TextView) findViewById(R.id.twoitemscancel);
        save = (TextView) findViewById(R.id.twoitemssave);
        //上传文件列表
        listfile = (ListView) findViewById(R.id.filelist);
        inflater=getLayoutInflater();

        //和设置标签相关的
        instancelabel = (Flowlayout) findViewById(R.id.instanctag);
        item1label = (Flowlayout) findViewById(R.id.itemtag1);
        item2label = (Flowlayout) findViewById(R.id.itemtag2);

        inputLabelinstance = (EditText) LayoutInflater.from(this).inflate(R.layout.my_edit_text, null);
        inputLabelitem1 = (EditText) LayoutInflater.from(this).inflate(R.layout.my_edit_text, null);
        inputLabelitem2 = (EditText) LayoutInflater.from(this).inflate(R.layout.my_edit_text, null);

        initInstanceList();
        initInstanceLayout(listinstance);
        initInstanceBtnListener();

        inititem1List();
        inititem1Layout(listitem1);
        inititem1BtnListener();

        inititem2List();
        inititem2Layout(listitem2);
        inititem2BtnListener();

        type.setText("文本关系");
        //选择文件按钮
        add_file = (ImageView)findViewById(R.id.sc_fujianimg);
        add_file.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FileSelectActivity.class);
                startActivityForResult(intent, RESULT_LOAD_FILE);
            }
        });
        //保存按钮
        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                isSave();
            }
        });
        //返回按钮
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditTextListActivity.class);
                startActivity(intent);
            }
        });

    }

    private void initView() {
        ScrollView rv_messageList = (ScrollView)findViewById(R.id.scrollView);
        //任务内容编辑框
        EditText et_inputMessage = (EditText) findViewById(R.id.twoitemscontent);
        //打开文件选择
        ImageView iv_more = (ImageView) findViewById(R.id.iv_more);
        LinearLayout ll_rootEmojiPanel = (LinearLayout) findViewById(R.id.ll_rootEmojiPanel);
        emojiKeyboard = new EmojiKeyboard(this, et_inputMessage, ll_rootEmojiPanel, iv_more, rv_messageList);
        emojiKeyboard.setEmoticonPanelVisibilityChangeListener(new EmojiKeyboard.OnEmojiPanelVisibilityChangeListener() {
            @Override
            public void onShowEmojiPanel() {
                Log.e(TAG, "onShowEmojiPanel");
            }

            @Override
            public void onHideEmojiPanel() {
                Log.e(TAG, "onHideEmojiPanel");
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!emojiKeyboard.interceptBackPress()) {
            super.onBackPressed();
        }
    }

    //从选择文件界面返回过来的文件路径
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_FILE && resultCode == RESULT_LOAD_FILE && data != null) {
            picturePath = data.getStringExtra("path");
            pictureSize = String.valueOf(data.getLongExtra("length",0));
            Log.e("tag", "picturePath-->>" + picturePath);
            picturePaths.add(picturePath);
            int num = picturePath.lastIndexOf("/");
            String filename = picturePath.substring(num+1);
            //给文件列表赋值
            ShowFile showFile1 = new ShowFile(filename,pictureSize+"kb",R.drawable.icon_file);
            array.add(showFile1);
            //上传文件的列表
            adapter=new ShowFileAdapter(inflater,array);
            listfile.setAdapter(adapter);
            initListView();
        }
    }

    /*
     * 点击文件列表里面的文件，用来对文件进行操作
     */
    public void initListView(){
        listfile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub
            changeIcon(position,picturePath);
            lightoff();
        }
    });
    }
    /**
     * 弹出popupWindow选择对文件的操作
     */
    private void changeIcon(final int position, final String picturePath) {
        if (popupWindow == null) {
            popupView = View.inflate(this, R.layout.popup_file, null);
            // 参数2,3：指明popupwindow的宽度和高度
            popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);

            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    lighton();
                }
            });

            // 设置背景图片，必须设置不然动画没作用
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setFocusable(true);

            // 设置点击popupwindow外屏幕其它地方消失
            popupWindow.setOutsideTouchable(true);

            // 平移动画相对于手机屏幕的底部开始，X轴不变，Y轴从1变0
            animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0,
                    Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT, 0);
            animation.setInterpolator(new AccelerateInterpolator());
            animation.setDuration(200);

            popupView.findViewById(R.id.filedelete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 打开系统拍照程
                    /*Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(camera, CAMERA);*/
                    //filelinearlayout.removeView(view);
                    array.remove(position);
                    picturePaths.remove(position);
                    //ShowFileAdapter adapter=new ShowFileAdapter(inflater,array);
                    adapter.notifyDataSetChanged();
                    listfile.setAdapter(adapter);
                    popupWindow.dismiss();
                    lighton();
                }
            });
            popupView.findViewById(R.id.filescan).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 打开系统图库选择图片
                    /*Intent picture = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(picture, PICTURE);*/
                    //String popstr = readFile(picturePath);
                    //Log.d("popstr--->>>", popstr);
                    popupWindow.dismiss();
                    lighton();
                }
            });
            popupView.findViewById(R.id.filecancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 打开系统图库选择图片
                    /*Intent picture = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(picture, PICTURE);*/
                    popupWindow.dismiss();
                    lighton();
                }
            });
        }

        // 在点击之后设置popupwindow的销毁
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
            lighton();
        }

        // 设置popupWindow的显示位置，此处是在手机屏幕底部且水平居中的位置
        popupWindow.showAtLocation(AddListTwoitemsActivity.this.findViewById(R.id.twoitemscontent), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        popupView.startAnimation(animation);
    }

    /**
     * 设置手机屏幕亮度变暗
     */
    private void lightoff() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.3f;
        getWindow().setAttributes(lp);
    }

    /**
     * 设置手机屏幕亮度显示正常
     */
    private void lighton() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 1f;
        getWindow().setAttributes(lp);
    }

    /**
     * post请求线程
     */
    Runnable postRun = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            //String baseUrl = "http://172.20.10.5:8080/task/addTaskTwoitems";
            String baseUrl = Constant.twoitemsaddtaskUrl;
            List<File> files = new ArrayList<File>();
            //上传文件
            for (int i = 0; i < picturePaths.size(); i++) {
                File file = new File(picturePaths.get(i));
                files.add(file);
            }
            String result = HttpUtil.postWithFilesTwoitems(baseUrl,fileparams,picturePaths);
            //String result = HttpUtil.postWithFiles2(baseUrl,fileparams,files);
            Log.e("tag", "addtaskresultnew-->>" + result);

            JSONObject jsonObject= (JSONObject) JSON.parse(result);
            Integer data = (Integer) jsonObject.getInteger("status");
            if(data.toString().equals("0")){
                // 给btnLogin添加点击响应事件
                Intent intent = new Intent(AddListTwoitemsActivity.this, MainActivity.class);
                //启动
                startActivity(intent);
                String loginres = jsonObject.getString("msg");
                Looper.prepare();
                Toast.makeText(AddListTwoitemsActivity.this,loginres,Toast.LENGTH_LONG).show();
                Looper.loop();
            }else{
                //上传的文件不符合要求
                //2002,"msg":".test文件格式不正确"
                String loginres = jsonObject.getString("msg");
                Looper.prepare();
                Toast.makeText(AddListTwoitemsActivity.this,loginres,Toast.LENGTH_LONG).show();
                Looper.loop();
            }

            /*Intent intent=new Intent(AddListTwoitemsActivity.this,EditTextListActivity.class);
            startActivity(intent);
            AddListTwoitemsActivity.this.finish();*/
        }
    };

    private void isSave(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        //获取当前时间
        Date curDate = new Date(System.currentTimeMillis());
        String times = formatter.format(curDate);
        String tasktitle=title.getText().toString();
        String tasktype = type.getText().toString();
        String instancenum = instancelabelnum.getText().toString();
        String item1num = item1labelnum.getText().toString();
        String item2num = item2labelnum.getText().toString();
        String taskdeadline = deadline.getText().toString();
        String taskcontent=content.getText().toString();

        /*StringBuffer instancesb = new StringBuffer();
        for(int i=0;i<listinstance.size()-1;i++){
            instancesb.append(listinstance.get(i)+"#");
        }
        instancesb.append(listinstance.get(listinstance.size()-1));

        StringBuffer item1sb = new StringBuffer();
        for(int i=0;i<listitem1.size()-1;i++){
            item1sb.append(listitem1.get(i)+"#");
        }
        item1sb.append(listitem1.get(listitem1.size()-1));

        StringBuffer item2sb = new StringBuffer();
        for(int i=0;i<listitem2.size()-1;i++){
            item2sb.append(listitem2.get(i)+"#");
        }
        item2sb.append(listitem2.get(listitem2.size()-1));*/

        StringBuffer instancesb = new StringBuffer();
        for(int i=0;i<listinstance.size()-1;i++){
            instancesb.append(listinstance.get(i)+",");
        }
        instancesb.append(listinstance.get(listinstance.size()-1));

        StringBuffer item1sb = new StringBuffer();
        for(int i=0;i<listitem1.size()-1;i++){
            item1sb.append(listitem1.get(i)+",");
        }
        item1sb.append(listitem1.get(listitem1.size()-1));

        StringBuffer item2sb = new StringBuffer();
        for(int i=0;i<listitem2.size()-1;i++){
            item2sb.append(listitem2.get(i)+",");
        }
        item2sb.append(listitem2.get(listitem2.size()-1));

        if(instancenum==null||instancenum.equals("")||item1num==null||item1num.equals("")||item2num==null||item2num.equals("")){
            Toast.makeText(AddListTwoitemsActivity.this,"请填写标签个数。",Toast.LENGTH_LONG).show();
        }else{
            fileparams.put("title",tasktitle);
            fileparams.put("typeName",tasktype);
            fileparams.put("description",taskcontent);
            fileparams.put("createtime",times);
            fileparams.put("taskcompstatus","进行中");
            fileparams.put("deadline",taskdeadline);
            fileparams.put("viewnum",0);
            fileparams.put("attendnum",0);

            fileparams.put("instLabel",instancesb.toString());
            fileparams.put("item1Label",item1sb.toString());
            fileparams.put("item2Label",item2sb.toString());

            fileparams.put("labelnum",Integer.valueOf(instancenum));
            fileparams.put("labelnum1",Integer.valueOf(item1num));
            fileparams.put("labelnum2",Integer.valueOf(item2num));
            fileparams.put("userId",userId);

            new Thread(postRun).start();
        }
    }

    //设置和instance标签相关的
    public void initInstanceList() {
        for(int i=0;i<mTextStrinstance.length;i++){
            listinstance.add(mTextStrinstance[i]);
        }
    }

    public void initInstanceBtnListener() {
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
    public void initInstanceLayout(final ArrayList<String> arr) {

        instancelabel.removeAllViewsInLayout();
        /*** 创建 textView数组*/
        final TextView[] textViews = new TextView[arr.size()];
        final ImageView[] icons = new ImageView[arr.size()];
        for (int i = 0; i < arr.size(); i++) {
            final int pos = i;
            final View view = (View) LayoutInflater.from(AddListTwoitemsActivity.this).inflate(R.layout.text_view, instancelabel, false);
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
                    //遍历图标删除当前被点击项
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
    public int idxInstanceTextTag(String text) {
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
    public boolean doInstanceAddText(final String str, boolean bCustom, int idx) {
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
    public void inititem1List() {
        for(int i=0;i<mTextStritem1.length;i++){
            listitem1.add(mTextStritem1[i]);
        }
    }
    public void inititem1BtnListener() {
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
    public void inititem1Layout(final ArrayList<String> arr) {
        item1label.removeAllViewsInLayout();
        /**创建textView数组*/
        final TextView[] textViews = new TextView[arr.size()];
        final ImageView[] icons = new ImageView[arr.size()];
        for (int i = 0; i < arr.size(); i++) {
            final int pos = i;
            final View view = (View) LayoutInflater.from(AddListTwoitemsActivity.this).inflate(R.layout.text_view,item1label,false);
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
    public int idxitem1TextTag(String text) {
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
    public boolean doitem1AddText(final String str,boolean bCustom,int idx) {
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
    private void inititem2List() {
        for(int i=0;i<mTextStritem2.length;i++){
            listitem2.add(mTextStritem2[i]);
        }
    }

    public void inititem2BtnListener() {
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
    public void inititem2Layout(final ArrayList<String> arr) {
        item2label.removeAllViewsInLayout();
        /**创建textView数组*/
        final TextView[] textViews = new TextView[arr.size()];
        final ImageView[] icons = new ImageView[arr.size()];
        for (int i = 0; i < arr.size(); i++) {
            final int pos = i;
            final View view = (View) LayoutInflater.from(AddListTwoitemsActivity.this).inflate(R.layout.text_view, item2label,false);
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
    public int idxitem2TextTag(String text) {
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
    public boolean doitem2AddText(final String str,boolean bCustom,int idx) {
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
