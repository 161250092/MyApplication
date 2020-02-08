package com.example.kongmin.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kongmin.myapplication.R;


public class PopupActivity extends AppCompatActivity implements View.OnClickListener, AnimatorListener {

    private BottomPopupWindowView bottomPopupWindowView;

    private View contentView;
    private View bottomView;
    private LinearLayout mainView;

    private SlideBottomLayout slideBottomLayout;
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_popup);
        setContentView(R.layout.fujian_pop);
        slideBottomLayout = (SlideBottomLayout)findViewById(R.id.slideLayout);
        textView = (TextView)findViewById(R.id.handle);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                slideBottomLayout.switchVisible();
            }
        });
        /*mainView=(LinearLayout)findViewById(R.id.main_view);

        bottomView=LayoutInflater.from(this).inflate(R.layout.layout_bottom_view,null);
        (bottomView.findViewById(R.id.promptly_buy)).setOnClickListener(this);
        (findViewById(R.id.guige)).setOnClickListener(this);
        bottomPopupWindowView=(BottomPopupWindowView)findViewById(R.id.bottom_popup);
        bottomPopupWindowView.setOnClickListener(this);
        bottomPopupWindowView.setBaseView(bottomView);
        contentView=LayoutInflater.from(this).inflate(R.layout.layout_content_view,null);
        bottomPopupWindowView.setContextView(contentView);
        (contentView.findViewById(R.id.ic_cancel)).setOnClickListener(this);
        bottomPopupWindowView.setAnimatorListener(this);*/
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.promptly_buy:
            case R.id.ic_cancel:
                bottomPopupWindowView.disMissPopupView();
                break;
            case R.id.guige:
                bottomPopupWindowView.showPopouView();
                break;
        }
    }

    @Override
    public void startValue(int value) {
        setMargins (mainView,value-10,value,value-10,value);
    }

    @Override
    public void endValue(int value) {
        setMargins (mainView,value,value,value,value);
    }

    public static void setMargins (View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }


}