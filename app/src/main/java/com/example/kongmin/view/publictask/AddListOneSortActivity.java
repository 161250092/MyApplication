package com.example.kongmin.view.publictask;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.kongmin.Constant.Constant;
import com.example.kongmin.keyboard.EmojiKeyboard;
import com.example.kongmin.pojo.ShowFile;
import com.example.kongmin.util.HttpUtil;
import com.example.kongmin.myapplication.R;
import com.example.kongmin.util.MyApplication;
import com.example.kongmin.view.BaseActivity;
import com.example.kongmin.view.EditTextListActivity;
import com.example.kongmin.view.FileSelectActivity;
import com.example.kongmin.view.mainPage.MainActivity;
import com.example.kongmin.view.ShowFileAdapter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by kongmin on 2019/02/11.
 * 文本排序标注发任务界面
 */
public class AddListOneSortActivity extends BaseActivity {

    //选择文件的弹出框
    private EmojiKeyboard emojiKeyboard;

    private final String TAG = "文本排序标注发任务";

    public static final String matchtype = "文本排序标注";
    public static String tasktypedetail = "";

    private EditText title;
    private TextView type;
    private RadioGroup typedetail;
    private EditText deadline;
    private EditText content;
    //返回按钮
    private TextView cancel;
    //保存按钮
    private TextView save;
    //选择文件按钮
    private ImageView add_file;
    //选中的排序类型
    private RadioButton rb;

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
        setContentView(R.layout.activity_add_osmain);

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
        title = (EditText) findViewById(R.id.sorttitle);
        type = (TextView) findViewById(R.id.sorttype);
        typedetail = (RadioGroup) findViewById(R.id.sorttypedetail);
        deadline = (EditText) findViewById(R.id.sortdeadline);
        content = (EditText) findViewById(R.id.sortcontent);

        //返回和保存按钮
        cancel = (TextView) findViewById(R.id.matchcancel);
        save = (TextView) findViewById(R.id.matchsave);
        //上传文件列表
        listfile = (ListView) findViewById(R.id.filelist);
        inflater=getLayoutInflater();
        type.setText(matchtype);
        rb = findViewById(typedetail.getCheckedRadioButtonId());
        tasktypedetail = rb.getText().toString();
        typedetail.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group,int checkedId){
                rb= (RadioButton) findViewById(checkedId);
                //Toast.makeText(AddListOneSortActivity.this, rb.getText().toString(), Toast.LENGTH_LONG).show();
                tasktypedetail = rb.getText().toString();
            }
        });
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
                Intent intent = new Intent(getApplicationContext(),EditTextListActivity.class);
                startActivity(intent);
            }
        });

    }

    private void initView() {
        ScrollView rv_messageList = (ScrollView)findViewById(R.id.scrollView);
        //任务内容编辑框
        EditText et_inputMessage = (EditText) findViewById(R.id.sortcontent);
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
        popupWindow.showAtLocation(AddListOneSortActivity.this.findViewById(R.id.twoitemscontent), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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
            String baseUrl = Constant.onesortaddtaskUrl;
            List<File> files = new ArrayList<File>();
            //上传文件
            for (int i = 0; i < picturePaths.size(); i++) {
                File file = new File(picturePaths.get(i));
                files.add(file);
            }
            String result = HttpUtil.postWithFilesTwoitems(baseUrl,fileparams,picturePaths);
            Log.e("tag", "addtaskresultnew-->>" + result);

            JSONObject jsonObject= (JSONObject) JSON.parse(result);
            Integer data = (Integer) jsonObject.getInteger("status");
            if(data.toString().equals("0")){
                // 给btnLogin添加点击响应事件
                Intent intent = new Intent(AddListOneSortActivity.this, MainActivity.class);
                //启动
                startActivity(intent);
                String loginres = jsonObject.getString("msg");
                Looper.prepare();
                Toast.makeText(AddListOneSortActivity.this,loginres,Toast.LENGTH_LONG).show();
                Looper.loop();
            }else{
                //上传的文件不符合要求
                //2002,"msg":".test文件格式不正确"
                String loginres = jsonObject.getString("msg");
                Looper.prepare();
                Toast.makeText(AddListOneSortActivity.this,loginres,Toast.LENGTH_LONG).show();
                Looper.loop();
            }
            /*String result = HttpUtil.postWithFiles(baseUrl,fileparams,picturePaths);
            Log.e("tag", "addtaskresultnew-->>" + result);
            Intent intent=new Intent(AddListOneSortActivity.this,EditTextListActivity.class);
            startActivity(intent);
            AddListOneSortActivity.this.finish();*/
        }
    };

    private void isSave(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        //获取当前时间
        Date curDate = new Date(System.currentTimeMillis());
        String times = formatter.format(curDate);
        String tasktitle=title.getText().toString();
        //标注类别
        String tasktype = type.getText().toString();
        String taskdeadline = deadline.getText().toString();
        String taskcontent=content.getText().toString();

        fileparams.put("title",tasktitle);
        fileparams.put("typeName",tasktypedetail);
        fileparams.put("description",taskcontent);
        fileparams.put("createtime",times);
        fileparams.put("taskcompstatus","进行中");
        fileparams.put("deadline",taskdeadline);
        fileparams.put("viewnum",0);
        fileparams.put("attendnum",0);
        fileparams.put("userId",userId);

        if(tasktypedetail.equals("文本排序")){
            fileparams.put("typeId",5);

        }else{
            fileparams.put("typeId",6);
        }

        new Thread(postRun).start();
    }
}
