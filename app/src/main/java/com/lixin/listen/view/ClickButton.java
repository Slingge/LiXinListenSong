package com.lixin.listen.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * 点击阴影InageView
 */

public class ClickButton extends ImageView {

    public interface ClickButtonCallBack {
        void clickButton();
    }

    public ClickButtonCallBack clickButtonCallBack;

    public void setClickButtonCallBack(ClickButtonCallBack clickButtonCallBack) {
        this.clickButtonCallBack = clickButtonCallBack;
    }

    public ClickButton(Context context) {
        super(context);
    }

    public ClickButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClickButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.setColorFilter(0x55000000);
                clickButtonCallBack.clickButton();
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                this.setColorFilter(null);
                break;
        }
        return super.onTouchEvent(event);
    }
}
