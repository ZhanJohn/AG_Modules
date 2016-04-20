package com.ag.common.ui;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

/**
 *
 */
public class AGViewUtil {

    /**
     * 让ScrollView优先获取focus，这样childview获取不到focus，就不会滑动
     * @param scrollView
     */
    public static void disableAutoScrollToBottom(ScrollView scrollView) {
        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        scrollView.setFocusable(true);
        scrollView.setFocusableInTouchMode(true);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                arg0.requestFocusFromTouch();
                return false;
            }
        });
    }

}
