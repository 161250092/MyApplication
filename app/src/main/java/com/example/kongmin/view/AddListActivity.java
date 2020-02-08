package com.example.kongmin.view;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.EditText;

import com.example.kongmin.myapplication.R;
import com.example.kongmin.pojo.MarkCategory1;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import com.example.kongmin.util.*;
import android.widget.TextView;
import android.view.ViewGroup;
import android.graphics.Color;
import android.widget.Toast;
import android.widget.Button;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import java.net.URLEncoder;
import java.net.URL;
import java.net.HttpURLConnection;

import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.PopupWindow;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.content.SharedPreferences;
import android.content.Context;
import android.os.AsyncTask;

import com.alibaba.fastjson.JSONObject;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ScrollView;
import android.view.animation.TranslateAnimation;
import android.graphics.drawable.BitmapDrawable;
import android.view.animation.Animation;
import android.view.animation.AccelerateInterpolator;
import android.widget.RelativeLayout;

public class AddListActivity extends BaseActivity implements OnClickListener {

    EditText ed1,ed2;
    //ImageButton imageButton;
    //MyDataBase myDatabase;
    MarkCategory1 cun;
    int ids;



    /*ArrayList<String> names = new ArrayList<String>();
    FlowGroupView view;*/

    //和设置标签相关的
    Flowlayout mTagLayout;
    private ArrayList<TagItem> mAddTags = new ArrayList<TagItem>();
    private EditText inputLabel;
    private Button btnSure;
    // 存放标签数据的数组
    //String[] mTextStr = { "A渠道", "B渠道", "TCL空调部", "TCL家电", "天猫", "京东", "淘宝" };
    String[] mTextStr = { "AA", "BB", "TCL", "TCL2"};
    ArrayList<String>  list = new ArrayList<String>();


    private ArrayList<String> names = new ArrayList<String>();
    private FlowGroupView view;

    private String paramUrl ="";

    private Map<String,String> fileparams = new HashMap<String,String>();

    //和上传文件相关的
    protected static final int SUCCESS = 2;
    protected static final int FAILD = 3;
    protected static int RESULT_LOAD_FILE = 1;
    private TextView cancel;
    private TextView upload;
    //private EditText pathView;
    //private Button buttonLoadImage;
    private String picturePath;
    private List<String> picturePaths = new ArrayList<String>();
    private View show;



    //弹出框显示附件
    private LinearLayout filelinearlayout;


    //和上传文件按钮相关的

    //弹出选择附件
    ImageView add_file;
    LinearLayout poplayout;
    LinearLayout fujianlayout;
    PopupWindow window;

    //网络相关的
    //String TAG = MainActivity.class.getCanonicalName();
    //private EditText et_data_title;
    //private EditText et_data_content;
    private HashMap<String, String> stringHashMap;


    private String docid;


    //和弹出界面相关的
    private SlideBottomLayout slideBottomLayout;
    private TextView textView;
    private ImageView pop_fujian;

    private TextView label;

    private TagTextView tv_main;
    private List tags;

    private RelativeLayout mMessageRoot;



    // 声明PopupWindow
    private PopupWindow popupWindow;
    // 声明PopupWindow对应的视图
    private View popupView;

    // 声明平移动画
    private TranslateAnimation animation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.add_text_main);
        initKeyBoardListener((ScrollView) findViewById(R.id.scrollView));
        ed1=(EditText) findViewById(R.id.editText1);
        ed2=(EditText) findViewById(R.id.editText2);

        stringHashMap = new HashMap<>();
        //imageButton=(ImageButton) findViewById(R.id.saveButton);
        //myDatabase=new MyDataBase(this);

        Intent intent=this.getIntent();
        ids=intent.getIntExtra("ids", 0);




        /*tv_main = (TagTextView) findViewById(R.id.ttv_main);
        tags = new ArrayList<>();
        tags.add("NEW");
        //tags.add("HOT");
        //tags.add("软文版");
        //tags.add("我是打酱油的");
        tv_main.setContentAndTag("一二三四一二三四一二三四一二三四一二三四一二三四NEW一二三四一二三","New",24);*/



        /*view =  findViewById(R.id.flowgroupview);
        setData();
        for (int i = 0; i < names.size(); i++) {
            addTextView(names.get(i));
        }*/



        //和弹出界面相关的
        slideBottomLayout = (SlideBottomLayout)findViewById(R.id.slideLayout);
        pop_fujian = (ImageView)findViewById(R.id.sc_fujianimg);
        /*textView = (TextView)findViewById(R.id.handle);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               slideBottomLayout.switchVisible();
            }
        });*/

        /*setData();
        view = (FlowGroupView) findViewById(R.id.flowgroupview);
        for (int i = 0; i < names.size(); i++) {
            addTextView(names.get(i));
        }*/



        //和设置标签相关的
        inputLabel = (EditText) LayoutInflater.from(this).inflate(R.layout.my_edit_text, null);
        //btnSure = (Button) findViewById(R.id.btn_sure);
        mTagLayout = (Flowlayout) findViewById(R.id.tag_layout);
        initList();
        initLayout(list);
        initBtnListener();

        //上传文件按钮
        fujianlayout = (LinearLayout)findViewById(R.id.llybuttom);
        add_file = (ImageView) fujianlayout.findViewById(R.id.img_float);



        filelinearlayout = (LinearLayout)findViewById(R.id.filelinearlayout);

        //和上传文件相关的
        initView();
        initData();





        //默认为0，不为0,则为修改数据时跳转过来的
        if(ids!=0){
            //cun=myDatabase.getTiandCon(ids);
            ed1.setText(cun.getTitle());
            ed2.setText(cun.getContent());
        }
        //保存按钮的点击事件，他和返回按钮是一样的功能，所以都调用isSave()方法；
        /*imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                isSave();
            }
        });*/
    }


    /**
     * 动态添加布局
     * @param str
     */
    /*private void addTextView(String str) {
        TextView child = new TextView(this);
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
        params.setMargins(15, 15, 15, 15);
        child.setLayoutParams(params);
        child.setBackgroundResource(R.drawable.list_tag_view);
        child.setText(str);
        child.setTextColor(Color.WHITE);
        initEvents(child);//监听
        view.addView(child);
    }*/
    /**
     * 为每个view 添加点击事件
     */
    /*private void initEvents(final TextView tv){
        tv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(AddListActivity.this, tv.getText().toString(), 0).show();
            }
        });
    }*/

    /**
     * 动态添加布局
     * @param str
     */
    private void addTextView(String str) {
        TextView child = new TextView(this);
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
        params.setMargins(15, 15, 15, 15);
        child.setLayoutParams(params);
        child.setBackgroundResource(R.drawable.red_sold_round_sel);
        child.setText(str);
        child.setTextColor(Color.WHITE);
        initEvents(child);//监听
        view.addView(child);
    }
    /**
     * 为每个view 添加点击事件
     */
    private void initEvents(final TextView tv){
        tv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(AddListActivity.this, tv.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setData(){
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
    }


   //和上传文件相关的
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    show.setVisibility(View.INVISIBLE);
                    picturePath = "";
                    //pathView.setText(picturePath);
                    Toast.makeText(getApplicationContext(), "上传成功！", Toast.LENGTH_LONG).show();
                    break;
                case FAILD:
                    show.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "上传失败！", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
            return false;
        }
    });
    private void initView() {

        cancel = (TextView) findViewById(R.id.cancel);

        upload = (TextView) findViewById(R.id.upload);
        //buttonLoadImage = (Button) findViewById(R.id.buttonLoadPicture);
        cancel.setOnClickListener(this);
        upload.setOnClickListener(this);
        //buttonLoadImage.setOnClickListener(this);
        show = findViewById(R.id.show);
        //pathView = (EditText) findViewById(R.id.file_path);
        //pathView.setKeyListener(null);
        add_file.setOnClickListener(this);

        pop_fujian.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "提示：点击窗口外部关闭窗口！",
                //               Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), FileSelectActivity.class);
                startActivityForResult(intent, RESULT_LOAD_FILE);
            }
        });

        //上传文件按钮相关的
        add_file.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                //imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);

                //Toast.makeText(getApplicationContext(), "提示：点击窗口外部关闭窗口！",
                 //       Toast.LENGTH_SHORT).show();
                //bottomPopupWindowView.showPopouView();
                /*View view = (View)LayoutInflater.from(getApplicationContext()).inflate(R.layout.fujian_pop, null,false);
                poplayout = (LinearLayout)view.findViewById(R.id.pop_layout);
                // 创建PopupWindow对象，指定宽度和高度
                PopupWindow window = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                window.setWidth(view.getWidth());
                // 设置动画
                //window.setAnimationStyle(R.style.popup_window_anim);
                // 设置背景颜色
                window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
                // 设置可以获取焦点
                window.setFocusable(true);
                // 设置可以触摸弹出框以外的区域
                window.setOutsideTouchable(true);
                // 更新popupwindow的状态
                window.update();
                // 以下拉的方式显示，并且可以设置显示的位置
                //window.showAsDropDown(tvProduct, 0, 20);
                window.showAtLocation(view, Gravity.CENTER | Gravity.BOTTOM, 0, 50);*/

                //slideBottomLayout.switchVisible();
                if (slideBottomLayout.arriveTop()){
                    slideBottomLayout.hide();
                } else{
                    slideBottomLayout.show();
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0); //强制隐藏键盘
                }
            }
        });
    }


    private void initData() {
        picturePath = "";
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                finish();
                break;
            /*case R.id.buttonLoadPicture:
                Intent intent = new Intent(getApplicationContext(), FileSelectActivity.class);
                startActivityForResult(intent, RESULT_LOAD_FILE);
                break;*/
            case R.id.upload:
                //uploadFile();
                isSave();
                break;
            /*case R.id.ic_cancel:
                bottomPopupWindowView.disMissPopupView();
                break;*/
            default:
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_FILE && resultCode == RESULT_LOAD_FILE && data != null) {
            picturePath = data.getStringExtra("path");
            Log.e("tag", "picturePath-->>" + picturePath);
            picturePaths.add(picturePath);
            //pathView.setText(picturePath);
            int num = picturePath.lastIndexOf("/");
            String filename = picturePath.substring(num+1);

            TextView child = new TextView(getApplicationContext());
            ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
            params.setMargins(15, 15, 15, 15);
            child.setLayoutParams(params);
            //child.setBackgroundResource(R.drawable.line_rect_huise);
            child.setText(filename);
            child.setPadding(20,5,20,5);
            child.setTextColor(Color.BLACK);
            child.setTextSize(18);
            initEvents(child);//监听
            filelinearlayout.addView(child);
            initTextviewClick(child,picturePath);
        }

    }
    public void initTextviewClick(final TextView child,final String picturePath){

        //设置单击事件：
        child.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                // TODO 弹出popupwind选择拍照或者从相册选择
                changeIcon(v,picturePath);
                lightoff();

            }
        });

    }

    /**
     * 弹出popupWindow更改头像
     */
    private void changeIcon(final View view,final String picturePath) {
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

            // 设置背景图片， 必须设置，不然动画没作用
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
                    filelinearlayout.removeView(view);
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
                    String popstr = readFile(picturePath);
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
        popupWindow.showAtLocation(AddListActivity.this.findViewById(R.id.editText2), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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

    private String readFile(String picturePath) {
        //show.setVisibility(View.VISIBLE);
        /*final String uploadUrl = "http://10.0.2.2:8080/file/getDoccontent";
        final File file = new File(picturePath);
        Log.d("picturePath--->>>", picturePath);
        if(file!=null)
        {
            new AsyncTask<Void, Void, String>(){
                @Override
                protected String doInBackground(Void... arg0) {
                    // TODO Auto-generated method stub
                    request = UploadUtil.readFile( file, uploadUrl);
                    *//*JSONObject jsonObject= (JSONObject) JSON.parse(request);
                    String data = (String) jsonObject.get("data");
                    JSONObject jsonObject2= (JSONObject) JSON.parse(data);
                    String doc = (String) jsonObject.get("docId");*//*
                    JSONObject jsonObject = JSONObject.parseObject(request);
                    Log.d("request--->>>", request);
                    return request;
                }
                protected void onPostExecute(String result) {
                    //   uploadImage.setText(result);
                };
            }.execute();

        }*/
        String request = "";
        try {
            request = UploadUtil.readSDFile(picturePath);
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.d("request--->>>", request);
        return request;
    }
















    /******************************************************/
    private String fun(String msg){
        int i = msg.length();
        int j = msg.lastIndexOf("/") + 1;
        String a = msg.substring(j, i) ;
        System.out.println(a);
        return a;
    }
    /******************************************************/
    private void uploadFile() {
        //show.setVisibility(View.VISIBLE);
        //final String uploadUrl = "http://10.0.2.2:8080/file/addsinglefile";
        final String uploadUrl = "http://172.20.10.5:8080/file/addsinglefile";
        //final String uploadUrl = "http://10.0.2.2:8080/file/addmultifile";
       /* new Thread() {

            @Override

            public void run() {
                Message msg = Message.obtain();
                // 服务器的访问路径
                String uploadUrl = "http://10.0.2.2:8080/file/addsinglefile";
                Map<String, File> files = new HashMap<String, File>();
                String name = fun(picturePath);
                files.put(name, new File(picturePath));
                //files.put("test.jpg", new File(picturePath));
                Log.d("str--->>>", picturePath);
                try {
                    String str = HttpPost.post(uploadUrl, new HashMap<String,String>(), files);
                    System.out.println("str--->>>" + str);
                    msg.what = SUCCESS;
                } catch (Exception e) {
                    msg.what = FAILD;
                }
                mHandler.sendMessage(msg);
            }

        }.start();*/

        final File file = new File(picturePath);
        if(file!=null)
        {
            //final String request = UploadUtil.uploadFile( file, requestURL);
            new AsyncTask<Void, Void, String>(){
                @Override
                protected String doInBackground(Void... arg0) {
                    // TODO Auto-generated method stub
                    final String request = UploadUtil.uploadFile( file, uploadUrl);
                    /*JSONObject jsonObject= (JSONObject) JSON.parse(request);
                    String data = (String) jsonObject.get("data");
                    JSONObject jsonObject2= (JSONObject) JSON.parse(data);
                    String doc = (String) jsonObject.get("docId");*/
                    JSONObject jsonObject = JSONObject.parseObject(request);
                    Log.d("request--->>>", request);
                    Integer data = (Integer)jsonObject.getJSONObject("data").getInteger("docId");
                    docid = data.toString();
                    return request;
                }
                protected void onPostExecute(String result) {
                 //   uploadImage.setText(result);
                };
            }.execute();

        }

        Log.d("str--->>>", picturePath);

    }
    @Override
    protected void onDestroy() {
        //show.setVisibility(View.INVISIBLE);
        super.onDestroy();
    }


   //和设置标签相关的
    private void initList() {
        for(int i=0;i<mTextStr.length;i++){
            list.add(mTextStr[i]);
        }
    }

    private void initBtnListener() {
        /**
         * 初始化  单击事件：
         */
        //点击添加标签按钮
        /*btnSure.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                String label = inputLabel.getText().toString().trim();

                String[] newStr = new String[mTagLayout.getChildCount()];

                *//**
                 * 获取  子view的数量   并添加进去
                 *//*
                if(label!=null&&!label.equals("")){
                    for(int m = 0;m < mTagLayout.getChildCount()-1;m++){
                        newStr[m] =((TextView)mTagLayout.getChildAt(m).
                                findViewById(R.id.text)).getText().toString();
                                //根据当前位置查找到当前textView中标签内容
                    }
                    list.add(label);
                    initLayout(list);
                    inputLabel.setText("");
                }
            }
        });*/

        inputLabel.setImeOptions(EditorInfo.IME_ACTION_DONE);
        inputLabel.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    public boolean onEditorAction(TextView v, int actionId,
                                                  KeyEvent event) {if (actionId == EditorInfo.IME_ACTION_SEND
                            || actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT
                            || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                        //让mPasswordEdit获取输入焦点
                        //mPasswordEdit.requestFocus();


                        String label = inputLabel.getText().toString().trim();

                        String[] newStr = new String[mTagLayout.getChildCount()];

                        /**
                         * 获取  子view的数量   并添加进去
                         */
                        if(label!=null&&!label.equals("")){
                            for(int m = 0;m < mTagLayout.getChildCount()-1;m++){
                                newStr[m] =((TextView)mTagLayout.getChildAt(m).
                                        findViewById(R.id.text)).getText().toString();
                                //根据当前位置查找到当前textView中标签内容
                            }
                            list.add(label);
                            initLayout(list);
                            inputLabel.setText("");
                            inputLabel.requestFocus();
                        }



                        return true;
                    }
                        return false;
                    }
                });
    }


    private void initLayout(final ArrayList<String> arr) {

        mTagLayout.removeAllViewsInLayout();
        /**
         * 创建 textView数组
         */
        final TextView[] textViews = new TextView[arr.size()];
        final ImageView[] icons = new ImageView[arr.size()];

        for (int i = 0; i < arr.size(); i++) {

            final int pos = i;

            final View view = (View) LayoutInflater.from(AddListActivity.this).inflate(R.layout.text_view, mTagLayout, false);

            final TextView text = (TextView) view.findViewById(R.id.text);  //查找到当前textView
            final ImageView icon = (ImageView) view.findViewById(R.id.delete_icon);  //查找到当前删除小图标

            // 将已有标签设置成可选标签
            text.setText(list.get(i));
            /**
             * 将当前textView赋值给textView数组
             */
            textViews[i] = text;
            icons[i] = icon;

            //设置单击事件：
            icon.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    //遍历  图标  删除 当前  被点击项
                    for(int j = 0; j < icons.length;j++){
                        if(icon.equals(icons[j])){  //获取   当前  点击删除图标的位置：
                            mTagLayout.removeViewAt(j);
                            list.remove(j);
                            initLayout(list);
                        }
                    }
                }
            });

            text.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    text.setActivated(!text.isActivated()); // true是激活的

                    if (text.isActivated()) {
                        boolean bResult = doAddText(list.get(pos), false, pos);
                        text.setActivated(bResult);
                        //遍历   数据    将图标设置为可见：
                        for(int j = 0;j< textViews.length;j++){
                            if(text.equals(textViews[j])){//非当前  textView
                                icons[j].setVisibility(View.VISIBLE);
                            }
                        }
                    }else{
                        for(int j = 0;j< textViews.length;j++){
                            icons[j].setVisibility(View.GONE);
                        }
                    }

                    /**
                     * 遍历  textView  满足   已经被选中     并且不是   当前对象的textView   则置为  不选
                     */
                    for(int j = 0;j< textViews.length;j++){
                        if(!text.equals(textViews[j])){//非当前  textView
                            textViews[j].setActivated(false); // true是激活的
                            icons[j].setVisibility(View.GONE);
                        }
                    }
                }
            });

            mTagLayout.addView(view);
        }

        mTagLayout.addView(inputLabel);
    }

    // 标签索引文本
    protected int idxTextTag(String text) {
        int mTagCnt = mAddTags.size(); // 添加标签的条数
        for (int i = 0; i < mTagCnt; i++) {
            TagItem item = mAddTags.get(i);
            if (text.equals(item.tagText)) {
                return i;
            }
        }
        return -1;
    }

    // 标签添加文本状态
    private boolean doAddText(final String str, boolean bCustom, int idx) {
        int tempIdx = idxTextTag(str);
        if (tempIdx >= 0) {
            TagItem item = mAddTags.get(tempIdx);
            item.tagCustomEdit = false;
            item.idx = tempIdx;
            return true;
        }
        int tagCnt = mAddTags.size(); // 添加标签的条数
        TagItem item = new TagItem();
        item.tagText = str;
        item.tagCustomEdit = bCustom;
        item.idx = idx;
        mAddTags.add(item);
        tagCnt++;
        return true;
    }











    /*
     * 返回按钮调用的方法。
     */
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd  HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String times = formatter.format(curDate);
        String title=ed1.getText().toString();
        String content=ed2.getText().toString();
        //是要修改数据
        if(ids!=0){
            //cun=new Cuns(title,ids, content, times);
            //myDatabase.toUpdate(cun);
            Intent intent=new Intent(AddListActivity.this,EditTextListActivity.class);
            startActivity(intent);
            AddListActivity.this.finish();
        }
        //新建日记
        else{
            if(title.equals("")&&content.equals("")){
                Intent intent=new Intent(AddListActivity.this,EditTextListActivity.class);
                startActivity(intent);
                AddListActivity.this.finish();
            }
            else{
                //cun=new Cuns(title,content,times);
                //myDatabase.toInsert(cun);
                Intent intent=new Intent(AddListActivity.this,EditTextListActivity.class);
                startActivity(intent);
                AddListActivity.this.finish();
            }

        }
    }


    /**
     * post请求线程
     */
    Runnable postRun = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            //requestPost(stringHashMap);
            //String baseUrl = "http://10.0.2.2:8080/task/addTask";
            String baseUrl = "http://172.20.10.5:8080/task/addTask";
            List<File> files = new ArrayList<File>();
            //上传文件
            for (int i = 0; i < picturePaths.size(); i++) {
                File file = new File(picturePaths.get(i));
                files.add(file);
            }
            //String result = HttpUtil.requestPostandUploadfiles(baseUrl,paramUrl,files);
            String result = HttpUtil.postWithFiles(baseUrl, fileparams, picturePaths);
            Log.e("tag", "addtaskparamUrl-->>" + paramUrl);
            Log.e("tag", "addtaskresultnew-->>" + result);

        }
    };

    /**
     * post提交数据
     *
     * @param paramsMap
     */
    private void requestPost(HashMap<String, String> paramsMap) {

            try{
            String baseUrl = "http://10.0.2.2:8080/task/addTask";
            //合成参数
            StringBuilder tempParams = new StringBuilder();
            int pos = 0;
            for (String key : paramsMap.keySet()) {
                if (pos > 0) {
                    tempParams.append("&");
                }
                tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key), "utf-8")));
                pos++;
            }
            String params = tempParams.toString();
            Log.e("tag", "params--post-->>" + params);

            /*List<File> files = new ArrayList<File>();
            //上传文件
            for (int i = 0; i < picturePaths.size(); i++) {
                File file = new File(picturePaths.get(i));
                files.add(file);

            }
            String result = null;
            final String BOUNDARY = UUID.randomUUID().toString();  //边界标识 随机生成
            final String PREFIX = "--", LINE_END = "\r\n";
            final String CONTENT_TYPE = "multipart/form-data";   //内容类型
            try {
                URL url = new URL(baseUrl);
                // 打开一个HttpURLConnection连接
                HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                // 设置连接超时时间
                urlConn.setConnectTimeout(5 * 1000);
                //设置从主机读取数据超时
                urlConn.setReadTimeout(5 * 1000);
                // Post请求必须设置允许输出 默认false
                urlConn.setDoOutput(true);
                //设置请求允许输入 默认是true
                urlConn.setDoInput(true);
                // Post请求不能使用缓存
                urlConn.setUseCaches(false);
                // 设置为Post请求
                urlConn.setRequestMethod("POST");
                urlConn.setRequestProperty("Charset", "UTF-8");
                //设置编码
                urlConn.setRequestProperty("connection", "keep-alive");
                urlConn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
                if (files.size()!= 0) {
                    *//**当文件不为空，把文件包装并且上传*//*
                    final DataOutputStream dos = new DataOutputStream(urlConn.getOutputStream());
                    *//**
                     * 这里重点注意：
                     * name里面的值为服务器端需要key   只有这个key才可以得到对应的文件,在这里这个key就是对应的files
                     * filename是文件的名字，包含后缀名的   比如:abc.png
                     *//*
                    for(int i = 0; i < files.size(); i++){
                    StringBuffer sb = new StringBuffer();
                    sb.append(PREFIX);
                    sb.append(BOUNDARY);
                    sb.append(LINE_END);







                        sb.append("Content-Disposition: form-data; name=\"img" + i + "\"; filename=\"" + files.get(i).getName()+"\""+LINE_END);
                        //sb.append("Content-Type: application/octet-stream; charset="+ "UTF-8" +LINE_END);
                        sb.append(LINE_END);
                        dos.write(sb.toString().getBytes());
                        InputStream is = new FileInputStream(files.get(i));
                        byte[] bytes = new byte[1024];
                        int len = -1;
                        while((len=is.read(bytes))!=-1) {
                            dos.write(bytes, 0, len);
                        }

                        dos.write(LINE_END.getBytes());
                        is.close();

                    }
                    byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
                    dos.write(end_data);
                    dos.flush();
                    *//**
                     * 获取响应码 200=成功
                     * 当响应成功，获取响应的流
                     *//*
                    int res = conn.getResponseCode();
                    //  Log.e(TAG, "response code:"+res);
                    if(res==200){
                        listener.onFinish(res);
                    }


                    sb.append("Content-Disposition: form-data; name=\"files[]\"" + LINE_END);
                    sb.append("Content-Type: application/octet-stream; charset=utf-8" + LINE_END);
                    sb.append(LINE_END);
                    dos.write(sb.toString().getBytes());
                    final InputStream is = new FileInputStream(files);
                    final byte[] bytes = new byte[1024];
                    int len = 0;
                    while ((len = is.read(bytes)) != -1) {
                        dos.write(bytes, 0, len);
                    }
                    is.close();
                    dos.write(LINE_END.getBytes());
                    dos.write(params.getBytes());
                    final byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
                    dos.write(end_data);
                    dos.flush();
                    *//**
                     * 获取响应码  200=成功
                     * 当响应成功，获取响应的流
                     *//*
                    final int res = urlConn.getResponseCode();
                    Log.e("addtask", "response code:" + res);
                    if (res == 200) {
                        Log.e("addtask", "request success");
                        final InputStream input = urlConn.getInputStream();
                        final StringBuffer sb1 = new StringBuffer();
                        int ss;
                        while ((ss = input.read()) != -1) {
                            sb1.append((char) ss);
                        }
                        result = sb1.toString();
                        Log.e("addtask", "result : " + result);
                    } else {
                        Log.e("addtask", "request error");
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }*/


            // 请求的参数转换为byte数组
//            byte[] postData = params.getBytes();
            // 新建一个URL对象
            URL url = new URL(baseUrl);
            // 打开一个HttpURLConnection连接
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            // 设置连接超时时间
            urlConn.setConnectTimeout(5 * 1000);
            //设置从主机读取数据超时
            urlConn.setReadTimeout(5 * 1000);
            // Post请求必须设置允许输出 默认false
            urlConn.setDoOutput(true);
            //设置请求允许输入 默认是true
            urlConn.setDoInput(true);
            // Post请求不能使用缓存
            urlConn.setUseCaches(false);
            // 设置为Post请求
            urlConn.setRequestMethod("POST");
            urlConn.disconnect();

            SharedPreferences sharedPreferences = getSharedPreferences("ceshi",Context.MODE_PRIVATE);
            String sessionid=sharedPreferences.getString("jsessionid","");
            //sessionid值格式：JSESSIONID=AD5F5C9EEB16C71EC3725DBF209F6178，是键值对，不是单指值
            if(sessionid != null) {
                //urlConn.setDoOutput(true);
                urlConn.setRequestProperty("Cookie", sessionid);
            }

            //设置本次连接是否自动处理重定向
            urlConn.setInstanceFollowRedirects(true);
            //配置请求Content-Type
            //urlConn.setRequestProperty("Content-Type", "application/json");//post请求不能设置这个
            // 开始连接
            urlConn.connect();

            // 发送请求参数
            PrintWriter dos = new PrintWriter(urlConn.getOutputStream());
            dos.write(params);
            dos.flush();
            dos.close();
            // 判断请求是否成功
            if (urlConn.getResponseCode() == 200) {
                // 获取返回的数据
                String result = streamToString(urlConn.getInputStream());
                Log.e("tag", "Post方式请求成功，result--->" + result);
            } else {
                Log.e("tag", "Post方式请求失败");
            }
            // 关闭连接
            urlConn.disconnect();
        } catch (Exception e) {
            Log.e("tag", e.toString());
        }

    }

    /**
     * 将输入流转换成字符串
     *
     * @param is 从网络获取的输入流
     * @return
     */
    public String streamToString(InputStream is) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.close();
            is.close();
            byte[] byteArray = baos.toByteArray();
            return new String(byteArray);
        } catch (Exception e) {
            Log.e("tag", e.toString());
            return null;
        }
    }


    private void isSave(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd  HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String times = formatter.format(curDate);
        String title=ed1.getText().toString();
        String content=ed2.getText().toString();

       /* stringHashMap.put("title", title);
        stringHashMap.put("description", content);
        stringHashMap.put("docid", docid);
        stringHashMap.put("label",sb.toString());
        stringHashMap.put("createtime",times);*/
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<list.size()-1;i++){
            sb.append(list.get(i)+"#");
        }
        sb.append(list.get(list.size()-1));
        // todo 设置用户名
        paramUrl = "?title="+title+"&description="+content+"&label="+sb.toString()+"&createtime="+times+"&userid=1";
        //paramUrl = "?title=测试&description=描述&type=1&userid=1&taskcompstatus=未完成&otherinfo=无&label=zzzz";
        fileparams.put("title",title);
        fileparams.put("description",content);
        fileparams.put("label",sb.toString());
        fileparams.put("createtime",times);
        fileparams.put("userid","1");
        new Thread(postRun).start();
        try {
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }


        //是要修改数据
        if(ids!=0){
            //cun=new Cuns(title,ids, content, times);
            //myDatabase.toUpdate(cun);
            Intent intent=new Intent(AddListActivity.this,EditTextListActivity.class);
            startActivity(intent);
            AddListActivity.this.finish();
        }
        //新建日记
        else{
            //cun=new Cuns(title,content,times);
            //myDatabase.toInsert(cun);
            Intent intent=new Intent(AddListActivity.this,EditTextListActivity.class);
            startActivity(intent);
            AddListActivity.this.finish();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.add_text_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,
                        "标题："+ed1.getText().toString()+"    " +
                                "内容："+ed2.getText().toString());
                startActivity(intent);
                break;

            default:
                break;
        }
        return false;
    }

}
