package com.example.kongmin.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kongmin.myapplication.R;
import com.example.kongmin.pojo.Answer;
import com.example.kongmin.pojo.Page;
import com.example.kongmin.pojo.Question;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class QuestionActivity extends AppCompatActivity {

    // 数据源 假数据
    //private String resultJosn = "{\"result\":\"1\",\"type\":\"1\",\"data\":[{\"type\":\"3\",\"eid\":\"11\",\"problem\":\"1、 什么样的团队更容易获得成功？\",\"trueanswer\":\"B\",\"optionData\":[{\"option\":\"团结\",\"isChecked\":\"0\"},{\"option\":\"精英\",\"isChecked\":\"0\"},{\"option\":\"聪明\",\"isChecked\":\"0\"},{\"option\":\"领导\",\"isChecked\":\"0\"}]},{\"type\":\"3\",\"eid\":\"12\",\"problem\":\"2、 学习态度\",\"trueanswer\":\"C\",\"optionData\":[{\"option\":\"好\",\"isChecked\":\"0\"},{\"option\":\"一般\",\"isChecked\":\"0\"},{\"option\":\"很好\",\"isChecked\":\"0\"},{\"option\":\"差\",\"isChecked\":\"0\"}]},{\"type\":\"2\",\"eid\":\"13\",\"problem\":\"3、 这件事真的是对的吗\",\"trueanswer\":\"A\",\"optionData\":[{\"option\":\"正确\",\"isChecked\":\"0\"},{\"option\":\"错误\",\"isChecked\":\"0\"}]},{\"type\":\"2\",\"eid\":\"14\",\"problem\":\"4、 这个道理真的对吗\",\"trueanswer\":\"B\",\"optionData\":[{\"option\":\"正确\",\"isChecked\":\"0\"},{\"option\":\"错误\",\"isChecked\":\"0\"}]},{\"type\":\"1\",\"eid\":\"15\",\"problem\":\"5、 老师姓什么？\",\"trueanswer\":\"D\",\"optionData\":[{\"option\":\"赵\",\"isChecked\":\"0\"},{\"option\":\"钱\",\"isChecked\":\"0\"},{\"option\":\"孙\",\"isChecked\":\"0\"},{\"option\":\"李\",\"isChecked\":\"0\"}]},{\"type\":\"1\",\"eid\":\"16\",\"problem\":\"6、 这个要多上钱\",\"trueanswer\":\"C\",\"optionData\":[{\"option\":\"100\",\"isChecked\":\"0\"},{\"option\":\"200\",\"isChecked\":\"0\"},{\"option\":\"300\",\"isChecked\":\"0\"},{\"option\":\"400\",\"isChecked\":\"0\"}]},{\"type\":\"2\",\"eid\":\"17\",\"problem\":\"7、 我不用学这个课程也能成功\",\"trueanswer\":\"B\",\"optionData\":[{\"option\":\"正确\",\"isChecked\":\"0\"},{\"option\":\"错误\",\"isChecked\":\"0\"}]},{\"type\":\"2\",\"eid\":\"18\",\"problem\":\"8、 学习人数已经由300人了\",\"trueanswer\":\"A\",\"optionData\":[{\"option\":\"正确\",\"isChecked\":\"0\"},{\"option\":\"错误\",\"isChecked\":\"0\"}]},{\"type\":\"1\",\"eid\":\"19\",\"problem\":\"9、 该课程要上多久\",\"trueanswer\":\"A\",\"optionData\":[{\"option\":\"10分钟\",\"isChecked\":\"0\"},{\"option\":\"20分钟\",\"isChecked\":\"0\"},{\"option\":\"30分钟\",\"isChecked\":\"0\"},{\"option\":\"40分钟\",\"isChecked\":\"0\"}]},{\"type\":\"2\",\"eid\":\"20\",\"problem\":\"10、 学过这个课程的都成功了\",\"trueanswer\":\"B\",\"optionData\":[{\"option\":\"正确\",\"isChecked\":\"0\"},{\"option\":\"错误\",\"isChecked\":\"0\"}]}]}\n";
    //    private String resultJosn = "{\"result\":\"1\",\"type\":\"1\",\"data\":[{\"type\":\"2\",\"eid\":\"6\",\"problem\":\"1、 真不拿\",\"trueanswer\":\"A\",\"optionData\":[{\"option\":\"正确\",\"isChecked\":\"0\"},{\"option\":\"错误\",\"isChecked\":\"0\"}]},{\"type\":\"2\",\"eid\":\"7\",\"problem\":\"2、 这个怎么做\",\"trueanswer\":\"A\",\"optionData\":[{\"option\":\"1\",\"isChecked\":\"1\"},{\"option\":\"2\",\"isChecked\":\"0\"},{\"option\":\"3\",\"isChecked\":\"0\"},{\"option\":\"4\",\"isChecked\":\"0\"}]},{\"type\":\"2\",\"eid\":\"8\",\"problem\":\"3、 wgergwe\",\"trueanswer\":\"B\",\"optionData\":[{\"option\":\"正确\",\"isChecked\":\"0\"},{\"option\":\"错误\",\"isChecked\":\"0\"}]},{\"type\":\"3\",\"eid\":\"9\",\"problem\":\"4、 szdfasdf\",\"trueanswer\":\"C\",\"optionData\":[{\"option\":\"112\",\"isChecked\":\"0\"},{\"option\":\"22\",\"isChecked\":\"0\"},{\"option\":\"23\",\"isChecked\":\"0\"},{\"option\":\"43\",\"isChecked\":\"1\"}]},{\"type\":\"2\",\"eid\":\"10\",\"problem\":\"5、 rqwerqw\",\"trueanswer\":\"B\",\"optionData\":[{\"option\":\"正确\",\"isChecked\":\"0\"},{\"option\":\"错误\",\"isChecked\":\"0\"}]}]}";
    private String resultJosn = "{\"result\":\"1\",\"type\":\"1\",\"data\":[{\"type\":\"3\",\"eid\":\"11\",\"problem\":" +
            "\"1、经审理查明：2015年8月20日7时20分左右，被告人傅某驾驶皖M14826重型半挂牵引车（赣K×××××挂）沿滁州市创业路由南向北行驶到会峰路交叉口右转弯时，刮碰后碾压骑行电动车被害人宋某，致宋某当场死亡。……4、122受理单证明：电话151××××3905（傅某）于2015年8月20日7时27分32秒、2015年8月20日7时23分17秒打电话报警。 ……2015年8月20日早上在会峰路与创业路发生的交通事故，他是事后知道的。\n" +
            "该段文字中事故时间是哪个？\"," +
            "\"trueanswer\":\"B\",\"optionData\":[{\"option\":\"2015年8月20日7时20分左右\",\"isChecked\":\"0\"},{\"option\":\"2015年8月20日7时27分32秒\",\"isChecked\":\"0\"},{\"option\":\"2015年8月20日7时23分17秒\",\"isChecked\":\"0\"},{\"option\":\"2015年8月20日\",\"isChecked\":\"0\"}]},{\"type\":\"3\",\"eid\":\"12\",\"problem\":\"2、 学习态度\",\"trueanswer\":\"C\",\"optionData\":[{\"option\":\"好\",\"isChecked\":\"0\"},{\"option\":\"一般\",\"isChecked\":\"0\"},{\"option\":\"很好\",\"isChecked\":\"0\"},{\"option\":\"差\",\"isChecked\":\"0\"}]},{\"type\":\"2\",\"eid\":\"13\",\"problem\":\"3、 这件事真的是对的吗\",\"trueanswer\":\"A\",\"optionData\":[{\"option\":\"正确\",\"isChecked\":\"0\"},{\"option\":\"错误\",\"isChecked\":\"0\"}]},{\"type\":\"2\",\"eid\":\"14\",\"problem\":\"4、 这个道理真的对吗\",\"trueanswer\":\"B\",\"optionData\":[{\"option\":\"正确\",\"isChecked\":\"0\"},{\"option\":\"错误\",\"isChecked\":\"0\"}]},{\"type\":\"1\",\"eid\":\"15\",\"problem\":\"5、 老师姓什么？\",\"trueanswer\":\"D\",\"optionData\":[{\"option\":\"赵\",\"isChecked\":\"0\"},{\"option\":\"钱\",\"isChecked\":\"0\"},{\"option\":\"孙\",\"isChecked\":\"0\"},{\"option\":\"李\",\"isChecked\":\"0\"}]},{\"type\":\"1\",\"eid\":\"16\",\"problem\":\"6、 这个要多上钱\",\"trueanswer\":\"C\",\"optionData\":[{\"option\":\"100\",\"isChecked\":\"0\"},{\"option\":\"200\",\"isChecked\":\"0\"},{\"option\":\"300\",\"isChecked\":\"0\"},{\"option\":\"400\",\"isChecked\":\"0\"}]},{\"type\":\"2\",\"eid\":\"17\",\"problem\":\"7、 我不用学这个课程也能成功\",\"trueanswer\":\"B\",\"optionData\":[{\"option\":\"正确\",\"isChecked\":\"0\"},{\"option\":\"错误\",\"isChecked\":\"0\"}]},{\"type\":\"2\",\"eid\":\"18\",\"problem\":\"8、 学习人数已经由300人了\",\"trueanswer\":\"A\",\"optionData\":[{\"option\":\"正确\",\"isChecked\":\"0\"},{\"option\":\"错误\",\"isChecked\":\"0\"}]},{\"type\":\"1\",\"eid\":\"19\",\"problem\":\"9、 该课程要上多久\",\"trueanswer\":\"A\",\"optionData\":[{\"option\":\"10分钟\",\"isChecked\":\"0\"},{\"option\":\"20分钟\",\"isChecked\":\"0\"},{\"option\":\"30分钟\",\"isChecked\":\"0\"},{\"option\":\"40分钟\",\"isChecked\":\"0\"}]},{\"type\":\"2\",\"eid\":\"20\",\"problem\":\"" +
            "10、2015年7月16日14时40分许，孟某某驾驶冀A356KM厢式货车沿302线由东向西行驶到25公里＋500米处时，与同向行驶刘士贤驮着杨宇航的电动自行车追尾相撞，造成刘士贤受伤，杨宇航受伤经抢救无效死亡，车辆损坏的重大交通事故。孟某某负此事故的全部责任。\n本段案件中的受害人有？\",\"trueanswer\":\"B\",\"optionData\":[{\"option\":\"刘士贤\",\"isChecked\":\"0\"},{\"option\":\"杨宇航\",\"isChecked\":\"0\"},{\"option\":\"刘士贤、杨宇航\",\"isChecked\":\"0\"},{\"option\":\"孟某某\",\"isChecked\":\"0\"}]}]} \n";






    private LinearLayout test_layout;
    private Page the_page;
    // 答案列表
    private ArrayList<Answer> the_answer_list;
    // 问题列表
    private ArrayList<Question> the_quesition_list;
    // 问题所在的View
    private View que_view;
    // 答案所在的View
    private View ans_view;
    private LayoutInflater xInflater;
    private Page page;
    // 下面这两个list是为了实现点击的时候改变图片，因为单选多选时情况不一样，为了方便控制
    // 存每个问题下的TextView
    private ArrayList<ArrayList<TextView>> textlist = new ArrayList<ArrayList<TextView>>();
    // 存每个答案的TextView
    private ArrayList<TextView> textlist2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        xInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // 初始化数据
        initDate();
        // 提交按钮
        TextView button = (TextView) findViewById(R.id.tv_commit);
        button.setOnClickListener(new submitOnClickListener(page));
    }


    private void initDate() {
        ArrayList<Question> quesitionsList = null;
        try {
            quesitionsList = new ArrayList<>();//问题列表

            JSONObject resultJson = new JSONObject(resultJosn);
            JSONArray arrayJson = resultJson.optJSONArray("data");

            for (int i=0;i<arrayJson.length();i++){
                JSONObject subObject = arrayJson.getJSONObject(i);

                ArrayList<Answer> answers = new ArrayList<>();

                JSONArray arrayAnswerJson = subObject.optJSONArray("optionData");//问题的答案
                for (int j=0; j< arrayAnswerJson.length(); j++) {
                    JSONObject answerObject = arrayAnswerJson.getJSONObject(j);
                    Answer a_answer = new Answer();
                    a_answer.setAnswerId(""+j);
                    a_answer.setAnswer_content(answerObject.getString("option"));
                    a_answer.setAns_state(Integer.parseInt(answerObject.getString("isChecked")));

                    answers.add(a_answer);
                }

                Question q_quesition = new Question();
                q_quesition.setQuestionId(subObject.getString("eid"));//问题的id
                q_quesition.setType(subObject.getString("type"));//类型，1判断 2单选 3不定项
                q_quesition.setContent(subObject.getString("problem"));//问题
                q_quesition.setAnswers(answers);
                q_quesition.setQue_state(0);

                quesitionsList.add(q_quesition);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        page = new Page();
        page.setPageId("1");
        page.setStatus("0");
        page.setTitle("信息抽取标注测试题");
        page.setQuesitions(quesitionsList);
        // 加载布局
        initView(page);
    }

    private void initView(Page page) {
        // TODO Auto-generated method stub
        // 这是要把问题的动态布局加入的布局
        test_layout = (LinearLayout) findViewById(R.id.lly_test);
        TextView page_txt = (TextView) findViewById(R.id.txt_title);
        page_txt.setText(page.getTitle());
        // 获得问题即第二层的数据
        the_quesition_list = page.getQuesitions();
        // 根据第二层问题的多少，来动态加载布局
        for (int i = 0; i < the_quesition_list.size(); i++) {
            que_view = xInflater.inflate(R.layout.question_layout, null);
            //ImageView iv_type = (ImageView) que_view.findViewById(R.id.iv_type);
            TextView txt_que = (TextView) que_view.findViewById(R.id.txt_question_item);
            // 这是第三层布局要加入的地方
            LinearLayout add_layout = (LinearLayout) que_view.findViewById(R.id.lly_answer);

            View v_line = (View) que_view.findViewById(R.id.v_line);
            if(i == 0){//第一道题目的分隔条不用显示
                v_line.setVisibility(View.GONE);
            }

            //类型：1选择题；2判断题 3不定项
            if (the_quesition_list.get(i).getType().equals("1")) {
                //iv_type.setImageResource(R.mipmap.single_menu);
            } else if (the_quesition_list.get(i).getType().equals("2")){
                //iv_type.setImageResource(R.mipmap.judge_menu);
            } else if (the_quesition_list.get(i).getType().equals("3")){
                //iv_type.setImageResource(R.mipmap.more_menu);
            }

            txt_que.setText(the_quesition_list.get(i).getContent());//设置问题题目
            // 获得答案即第三层数据
            the_answer_list = the_quesition_list.get(i).getAnswers();
            textlist2 = new ArrayList<>();
            for (int j = 0; j < the_answer_list.size(); j++) {

                ans_view = xInflater.inflate(R.layout.answer_layout, null);
                TextView txt_ans = (TextView) ans_view.findViewById(R.id.txt_answer_item);
                TextView tv_menu = (TextView) ans_view.findViewById(R.id.tv_menu);

                //自己手动为每个问题的选项加上相应的ABCD...
                if (j==0){
                    tv_menu.setText("A");
                }else if (j==1){
                    tv_menu.setText("B");
                }else if (j==2){
                    tv_menu.setText("C");
                }else if (j==3){
                    tv_menu.setText("D");
                }else if (j==4){
                    tv_menu.setText("E");
                }else if (j==5){
                    tv_menu.setText("F");
                }else if (j==6){
                    tv_menu.setText("G");
                }
                // 判断哪个答案已选
                if (the_answer_list.get(j).getAns_state() == 1) {
//                    Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_SHORT).show();
                    the_quesition_list.get(i).setQue_state(1);
                    tv_menu.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_red));
                    tv_menu.setTextColor(getResources().getColor(R.color.colorWhite));
                } else {
//                    Toast.makeText(getApplicationContext(),"0",Toast.LENGTH_SHORT).show();
                    tv_menu.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_white));
                    tv_menu.setTextColor(getResources().getColor(R.color.colorblack));
                }

                textlist2.add(tv_menu);
                txt_ans.setText(the_answer_list.get(j).getAnswer_content());
                LinearLayout lly_answer_size = (LinearLayout) ans_view.findViewById(R.id.lly_answer_size);

                if (j%2!=0){//为了美观了，将答案的背景隔开一下
                    lly_answer_size.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                }
                //设置点击事件
                lly_answer_size .setOnClickListener(new answerItemOnClickListener(i, j, the_answer_list, txt_ans));
                add_layout.addView(ans_view);
            }
            textlist.add(textlist2);

            test_layout.addView(que_view);//将生成的问题都添加到一个布局文件中
        }
    }

    /**
     * 试卷各个选项的点击事件
     */
    class answerItemOnClickListener implements View.OnClickListener {
        private int i;
        private int j;
        private TextView txt;
        private ArrayList<Answer> the_answer_lists;

        public answerItemOnClickListener(int i, int j, ArrayList<Answer> the_answer_list, TextView text) {
            this.i = i;
            this.j = j;
            this.the_answer_lists = the_answer_list;
            this.txt = text;
        }

        // 实现点击选项后改变选中状态以及对应图片
        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            // 判断当前问题是单选还是多选
            if (the_quesition_list.get(i).getType().equals("3")) {//1选择题；2判断题 3不定项

                for(int z=0;z<the_answer_list.size();z++){
                    if(the_answer_list.get(z).getAns_state()==1){
                        the_quesition_list.get(i).setQue_state(1);
//                        return;
                    }else{
                        the_quesition_list.get(i).setQue_state(0);
                    }
                }
                if (the_answer_lists.get(j).getAns_state() == 0) {
//                    Toast.makeText(getApplication(), "0", Toast.LENGTH_SHORT).show();
                    textlist.get(i).get(j).setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_red));
                    textlist.get(i).get(j).setTextColor(getResources().getColor(R.color.colorWhite));
                    the_answer_lists.get(j).setAns_state(1);//注意一下这些相应的角标是用i 、j还是z。
                    the_quesition_list.get(i).setQue_state(1);
                } else {
//                    Toast.makeText(getApplication(), "1", Toast.LENGTH_SHORT).show();
                    textlist.get(i).get(j).setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_white));
                    textlist.get(i).get(j).setTextColor(getResources().getColor(R.color.colorblack));
                    the_answer_lists.get(j).setAns_state(0);
                    the_quesition_list.get(i).setQue_state(0);
                }
            } else {// 单选
                for (int z = 0; z < the_answer_lists.size(); z++) {
                    if (z == j) {
//                        Toast.makeText(getApplication(), "3", Toast.LENGTH_SHORT).show();
                        // 如果当前未被选中
                        textlist.get(i).get(j).setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_red));
                        textlist.get(i).get(j).setTextColor(getResources().getColor(R.color.colorWhite));
                        the_answer_lists.get(z).setAns_state(1);
                        the_quesition_list.get(i).setQue_state(1);
                    } else {
//                        Toast.makeText(getApplication(), "4", Toast.LENGTH_SHORT).show();
                        the_answer_lists.get(z).setAns_state(0);
                        the_quesition_list.get(i).setQue_state(1);
                        textlist.get(i).get(z).setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_white));
                        textlist.get(i).get(z).setTextColor(getResources().getColor(R.color.colorblack));
                    }
                }
            }
        }
    }

    /**
     * 提交按钮事件处理
     */
    class submitOnClickListener implements View.OnClickListener {

        private Page page;

        public submitOnClickListener(Page page) {
            this.page = page;
        }

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            // 判断是否答完题
            boolean isState = true;
            // 最终要的json数组
            JSONArray jsonArray = new JSONArray();
            // 点击提交的时候，先判断状态，如果有未答完的就提示，如果没有再把每条答案提交（包含问卷ID 问题ID 及答案ID）
            // 注：不用管是否是一个问题的答案，就以答案的个数为准来提交上述格式的数据
            for (int i = 0; i < the_quesition_list.size(); i++) {
                the_answer_list = the_quesition_list.get(i).getAnswers();
                // 判断是否有题没答完
                if (the_quesition_list.get(i).getQue_state() == 0) {
                    Toast.makeText(getApplicationContext(), "您第" + (i + 1) + "题没有答完", Toast.LENGTH_LONG).show();
                    jsonArray = null;
                    isState = false;
                    return;
                } else {
                    JSONObject json = new JSONObject();
                    String answers2 = "";
                    String answers = "";
                    for (int j = 0; j < the_answer_list.size(); j++) {
                        if (the_answer_list.get(j).getAns_state() == 1) {
                            try {
                                answers2 = the_quesition_list.get(i).getQuestionId();
                                if (answers.length()==0){
                                    answers = answers +j;
                                }else {
                                    answers = answers +"-"+ j;
                                }

                                //===为不定项拼接答案================================
                                if (answers.contains("0")) {
                                    answers = answers.replace("0", "A");
                                }
                                if (answers.contains("1")) {
                                    answers = answers.replace("1", "B");
                                }
                                if (answers.contains("2")) {
                                    answers = answers.replace("2", "C");
                                }
                                if (answers.contains("3")) {
                                    answers = answers.replace("3", "D");
                                }
                                if (answers.contains("4")) {
                                    answers = answers.replace("4", "E");
                                }
                                if (answers.contains("5")) {
                                    answers = answers.replace("5", "F");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    try {
                        json.put("answer", answers);
                        json.put("eid", answers2);
//                        Toast.makeText(getApplicationContext(), json + "", Toast.LENGTH_SHORT).show();
                        jsonArray.put(json);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            Log.d("jsonArray-->", "" + jsonArray);
            Toast.makeText(getApplicationContext(), "提交的数据：" + jsonArray, Toast.LENGTH_SHORT).show();
        }
    }




}