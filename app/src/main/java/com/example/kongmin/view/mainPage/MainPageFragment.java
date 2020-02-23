package com.example.kongmin.view.mainPage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.kongmin.managetask.dotask.MyTaskListActivity;
import com.example.kongmin.myapplication.R;
import com.example.kongmin.util.MyImageLoader;
import com.example.kongmin.view.taskList.TaskListActivity;
import com.hb.dialog.dialog.LoadingDialog;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

public class MainPageFragment extends Fragment implements OnBannerListener {

    private Banner mBanner;
    private MyImageLoader mMyImageLoader;
    private ArrayList<Integer> imagePath;
    private ArrayList<String> imageTitle;

    private static  ArrayList<Class> selections;

    static {
        selections = new ArrayList<>();
        selections.add(MyTaskListActivity.class);
        selections.add(TaskListActivity.class);
    }

    LoadingDialog loadingDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mRootView;
        mRootView = inflater.inflate(R.layout.fragment_main_page, container, false);

        loadingDialog = new LoadingDialog(getActivity());

        initData();
        initView(mRootView);

        GridView gridView = mRootView.findViewById(R.id.gridview);
        final List<String> list = new ArrayList<>();
        list.add("我的任务");
        list.add("任务列表");
        for(int i = 3;i <= 6;i++){
            list.add("待开发");
        }
        gridView.setAdapter(new MainPageGridViewAdapter(list,getActivity()));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if ( position <= 1)
                {
                    loadingDialog = new LoadingDialog(getActivity());
                    loadingDialog.setMessage("loading");
                    loadingDialog.show();
                    Intent intent = new Intent(getActivity(),
                            selections.get(position));
                    startActivity(intent);
                }



            }
        });
        return mRootView;
    }
    private void initData() {
        imagePath = new ArrayList<>();
        imageTitle = new ArrayList<>();
        imagePath.add(R.drawable.view_00);
        imagePath.add(R.drawable.view_01);
        imagePath.add(R.drawable.view_02);
        imageTitle.add("风景1");
        imageTitle.add("风景2");
        imageTitle.add("风景3");
    }

    private void initView(View mRootView) {
        mMyImageLoader = new MyImageLoader();
        mBanner = mRootView.findViewById(R.id.banner);
        //设置样式，里面有很多种样式可以自己都看看效果
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        //设置图片加载器
        mBanner.setImageLoader(mMyImageLoader);
        //设置轮播的动画效果,里面有很多种特效,可以都看看效果。
        mBanner.setBannerAnimation(Transformer.ZoomOutSlide);
        //轮播图片的文字
        mBanner.setBannerTitles(imageTitle);
        //设置轮播间隔时间
        mBanner.setDelayTime(3000);
        //设置是否为自动轮播，默认是true
        mBanner.isAutoPlay(true);
        //设置指示器的位置，小点点，居中显示
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        //设置图片加载地址
        mBanner.setImages(imagePath)
                //轮播图的监听
                .setOnBannerListener(this)
                //开始调用的方法，启动轮播图。
                .start();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (loadingDialog != null)
            loadingDialog.dismiss();
    }

    @Override
    public void OnBannerClick(int position) {
        Toast.makeText(getActivity(), "你点了第" + (position + 1) + "张轮播图", Toast.LENGTH_SHORT).show();
    }



}
