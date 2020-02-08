package com.example.kongmin.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.example.kongmin.myapplication.R;
import com.example.kongmin.util.CommonUtils;
import com.example.kongmin.util.KeyboardUtils;

/**
 * Created by Administrator on 2018/1/18.
 */

public class HintPopupWindow implements View.OnClickListener {
    private PopupWindow popupWindow;
    private int keyboardHeight;
    private ImageView imageView;
    private Button button;
    private EditText editText;
    private int originHeight;

    public HintPopupWindow(EditText editText) {
        this.editText = editText;
        Context context = editText.getContext();
        popupWindow = new PopupWindow(editText);
        popupWindow.setWidth(CommonUtils.getScreenWidth());
        originHeight = context.getResources().getDimensionPixelSize(R.dimen.comment_hint_height);
        popupWindow.setHeight(originHeight);
        LayoutInflater inflater = LayoutInflater.from(context);
        View contentView = inflater.inflate(R.layout.comment_hint_layout, null);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setContentView(contentView);
        imageView = (ImageView) contentView.findViewById(R.id.comment_custom_pic);
        button = (Button) contentView.findViewById(R.id.comment_hint_btn);
        button.setOnClickListener(this);
    }

    public void show(View parent, int x, int y) {
        popupWindow.showAtLocation(parent, Gravity.NO_GRAVITY, x, y);
    }

    public void hide() {
        popupWindow.update(CommonUtils.getScreenWidth(), originHeight);
        popupWindow.dismiss();
    }

    public int getHeight() {
        return popupWindow.getHeight();
    }

    public void setKeyboardHeight(int keyboardHeight) {
        this.keyboardHeight = keyboardHeight;
    }

    @Override
    public void onClick(View v) {
        if (popupWindow.getHeight() == originHeight) {
            if (keyboardHeight > 0) {
                button.setText(R.string.comment_open_keyboard);
                imageView.getLayoutParams().height = keyboardHeight;
                popupWindow.setHeight(v.getResources().getDimensionPixelSize(R.dimen.comment_hint_height)
                        + imageView.getLayoutParams().height);
                popupWindow.update(CommonUtils.getScreenWidth(), popupWindow.getHeight());
                KeyboardUtils.hideSoftInput(editText);
            }
        } else {
            button.setText(R.string.comment_hide_keyboard);
            popupWindow.setHeight(originHeight);
            popupWindow.update(CommonUtils.getScreenWidth(), popupWindow.getHeight());
            KeyboardUtils.showSoftInput(editText);
        }
    }


    public boolean isVisible() {
        return popupWindow.isShowing();
    }
}