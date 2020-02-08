package com.example.kongmin.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.EditText;
import android.widget.ScrollView;

import com.example.kongmin.myapplication.R;
import com.example.kongmin.util.KeyBoardLayout;
import com.example.kongmin.util.CommonUtils;
import com.example.kongmin.util.KeyboardUtils;
public class KeyBoardActivity extends AppCompatActivity implements KeyBoardLayout.KeyBoardListener {
    private static final String TAG = "KeyBoardActivity";

    private KeyBoardLayout keyBoardLayout;
    private HintPopupWindow hintPopupWindow;
    private EditText editText;
    private ScrollView scrollView;
    private boolean isSoftActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_board);
        keyBoardLayout = (KeyBoardLayout) findViewById(R.id.key_board_layout);
        keyBoardLayout.setOnKeyBoardShow(this);
        editText = (EditText) findViewById(R.id.comment_edit_text);
        hintPopupWindow = new HintPopupWindow(editText);
        scrollView = (ScrollView) findViewById(R.id.content_scroll_view);

        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN && !isSoftActive) {
                    scrollView.scrollTo(0, editText.getBottom());
                }
                return false;
            }
        });

        scrollView.setOnTouchListener(new View.OnTouchListener() {
            private int mLastX;
            private int mLastY;
            private int mTouchSlop = ViewConfiguration.get(getApplicationContext()).getScaledTouchSlop();
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int) event.getRawX(), y = (int) event.getRawY();
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        mLastX = x;
                        mLastY = y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (Math.abs(mLastY - y) > mTouchSlop && isSoftActive) {
                            KeyboardUtils.hideSoftInputFromWindow(editText);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return false;
            }
        });
    }

    public void onKeyBoardShow(final int keyBoardHeight) {
        Log.d(TAG, "onKeyBoardShow(): keyBoardHeight = " + keyBoardHeight);
        final int hintTop = CommonUtils.getScreenHeight() - keyBoardHeight - hintPopupWindow.getHeight();
        hintPopupWindow.setKeyboardHeight(keyBoardHeight);
        hintPopupWindow.show(getWindow().getDecorView(), 0, hintTop);
    }

    @Override
    public void onKeyBoardEvent(boolean active, int keyBoardHeight) {
        isSoftActive = active;
        if (active) {
            onKeyBoardShow(keyBoardHeight);
        } else {
            onKeyBoardHide(keyBoardHeight);
        }
    }

    private void onKeyBoardHide(int keyBoardHeight) {
        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                hintPopupWindow.hide();
            }
        }, 100);
    }

    @Override
    public void onBackPressed() {
        if (hintPopupWindow.isVisible()) {
            hintPopupWindow.hide();
            if (!isSoftActive) return;
        }
        super.onBackPressed();
    }
}