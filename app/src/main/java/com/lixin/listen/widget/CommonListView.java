package com.lixin.listen.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

/**
 * Created by LiPeng on 2015/7/2.
 */
public class CommonListView extends ListView {

    public CommonListView(Context context) {
        super(context);
    }

    public CommonListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommonListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = getMeasuredHeight();

    }
}
