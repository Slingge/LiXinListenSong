package com.lixin.listen.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * 点击阴影InageView
 */

public class ClickImageView extends ImageView {

    public interface ClickImageViewCallBack {
        void clickImage();
    }

    public ClickImageViewCallBack clickImageViewCallBack;

    public void setClickImageViewCallBack(ClickImageViewCallBack clickImageViewCallBack) {
        this.clickImageViewCallBack = clickImageViewCallBack;
    }

    public ClickImageView(Context context) {
        super(context);
    }

    public ClickImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClickImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.setColorFilter(0x55000000);
                clickImageViewCallBack.clickImage();
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                this.setColorFilter(null);
                break;
        }
        return super.onTouchEvent(event);
    }
}
