package com.ag.controls.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

public class MyGridView extends GridView {
    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGridView(Context context) {
        super(context);
    }

    public MyGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    // 该自定义控件只是重写了GridView的onMeasure方法，使其不会出现滚动条，ScrollView嵌套ListView也是同样的道理，不再赘述。
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 重写的onTouchEvent回调方法
        switch (event.getAction()) {
            // 按下
            case MotionEvent.ACTION_DOWN:

                return super.onTouchEvent(event);
            // 滑动
            case MotionEvent.ACTION_MOVE:

                break;
            // 离开
            case MotionEvent.ACTION_UP:

                return super.onTouchEvent(event);
        }
        // 注意：返回值是false
        return false;
    }

}
