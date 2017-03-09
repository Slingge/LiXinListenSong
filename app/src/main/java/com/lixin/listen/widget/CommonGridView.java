package com.lixin.listen.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.GridView;

/**
 * Created by LiPeng on 2015/5/14.
 */
public class CommonGridView extends GridView {


    public CommonGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommonGridView(Context context) {
        super(context);
    }

    public CommonGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(MEASURED_SIZE_MASK,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = getMeasuredHeight();

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            return true;
        }

        return super.dispatchTouchEvent(ev);
    }
}
