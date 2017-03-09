package com.lixin.listen.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class CommonScrollView extends ScrollView {
    private OnScrollListener onScrollListener;

    private int lastScrollY;

    public CommonScrollView(Context context) {
        this(context, null);
    }

    public CommonScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }
    @Override
    public int computeVerticalScrollRange() {
        return super.computeVerticalScrollRange();
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(onScrollListener != null){
            onScrollListener.onScroll(t);
        }
    }

//    private Handler handler = new Handler() {
//        public void handleMessage(android.os.Message msg) {
//            int scrollY = CommonScrollView.this.getScrollY();
//            //此时的距离和记录下的距离不相等，在隔5毫秒给handler发送消息
//            if (lastScrollY != scrollY) {
//                lastScrollY = scrollY;
//                handler.sendMessageDelayed(handler.obtainMessage(), 5);
//            }
//            if (onScrollListener != null) {
//                onScrollListener.onScroll(scrollY);
//            }
//        }
//    };
//
//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        if (onScrollListener != null) {
//            onScrollListener.onScroll(lastScrollY = this.getScrollY());
//        }
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_UP:
//                handler.sendMessageDelayed(handler.obtainMessage(), 20);
//                break;
//        }
//        return super.onTouchEvent(ev);
//    }

    public interface OnScrollListener {
        void onScroll(int scrollY);
    }
}
