package com.example.kongmin.util;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Administrator on 2018/1/17.
 */

public class KeyBoardLayout extends View {
    private static final String TAG = "KeyBoardLayout";
    private KeyBoardListener keyBoardListener;

    public interface KeyBoardListener {
        void onKeyBoardEvent(boolean active, int keyBoardHeight);
    }

    public KeyBoardLayout(Context context) {
        super(context);
    }

    public KeyBoardLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public KeyBoardLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.d(TAG, "onSizeChanged(): w = " + w + ", h = " + h + ", oldw = " + oldw + ", oldh = " + oldh);
        super.onSizeChanged(w, h, oldw, oldh);
        int threshold = CommonUtils.getScreenHeight() / 5;
        // 如果旧高度和新高度都是有效值
        if (oldh > 0 && h > 0) {
            // 旧高度比新高度要大，表明键盘弹起
            int keyboardHeight = oldh - h;
            if (keyboardHeight > threshold) {
                notifyKeyBoardShow(keyboardHeight);
            } else if (keyboardHeight < 0) {
                notifyKeyBoardHide(-keyboardHeight);
            }
        }
    }

    private void notifyKeyBoardHide(int keyboardHeight) {
        if (keyBoardListener != null) {
            keyBoardListener.onKeyBoardEvent(false, keyboardHeight);
        }
    }

    private void notifyKeyBoardShow(int keyboardHeight) {
        if (keyBoardListener != null) {
            keyBoardListener.onKeyBoardEvent(true, keyboardHeight);
        }
    }

    public void setOnKeyBoardShow(KeyBoardListener keyBoardListener) {
        this.keyBoardListener = keyBoardListener;
    }
}