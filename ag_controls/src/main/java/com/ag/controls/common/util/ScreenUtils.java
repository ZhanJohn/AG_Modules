package com.ag.controls.common.util;


import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Field;

public class ScreenUtils {

    /**
     * 屏幕宽高比
     *
     * @param c
     * @return
     */
    public static double getScreenRatio(Activity c) {

        DisplayMetrics dm = new DisplayMetrics();
        c.getWindowManager().getDefaultDisplay().getMetrics(dm);
        System.out.println("分辨率：" + dm.widthPixels + " x " + dm.heightPixels);
        System.out.println("宽高比：" + dm.heightPixels / (dm.widthPixels + 0.0));
        System.out.println("density：" + dm.density);
        System.out.println("densityDpi：" + dm.densityDpi);

        return dm.heightPixels / (dm.widthPixels + 0.0);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 字体大小px单位，返回高度
     *
     * @param fontSize
     * @return
     */
    public static int getFontHeight(float fontSize) {
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        FontMetrics fm = paint.getFontMetrics();
//	    return (int) Math.ceil(fm.descent - fm.top) + 2;
        // 根据控件高度设置字体大小
        return (int) ((Math.ceil(fm.descent - fm.top) + 2) / 5);
    }

    //遍历设置字体
    public static void changeViewSize(ViewGroup viewGroup, int screenWidth, int screenHeight) {//传入Activity顶层Layout,屏幕宽,屏幕高
        int adjustFontSize = adjustFontSize(screenWidth, screenHeight);
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View v = viewGroup.getChildAt(i);
            if (v instanceof ViewGroup) {
                changeViewSize((ViewGroup) v, screenWidth, screenHeight);
            } else if (v instanceof Button) {//按钮加大这个一定要放在TextView上面，因为Button也继承了TextView
                ((Button) v).setTextSize(adjustFontSize + 2);
            } else if (v instanceof TextView) {
                /*if(v.getId()== R.id.item_tv_name){//顶部标题
                    ( (TextView)v ).setTextSize(adjustFontSize+4);
				}else{
					( (TextView)v ).setTextSize(adjustFontSize);
				} */
            }
        }
    }

    //获取字体大小
    public static int adjustFontSize(int screenWidth, int screenHeight) {
        screenWidth = screenWidth > screenHeight ? screenWidth : screenHeight;
        /**
         * 1. 在视图的 onsizechanged里获取视图宽度，一般情况下默认宽度是320，所以计算一个缩放比率
         rate = (float) w/320   w是实际宽度
         2.然后在设置字体尺寸时 paint.setTextSize((int)(8*rate));   8是在分辨率宽为320 下需要设置的字体大小
         实际字体大小 = 默认字体大小 x  rate
         */
        int rate = (int) (5 * (float) screenWidth / 320); //我自己测试这个倍数比较适合，当然你可以测试后再修改
        return rate < 15 ? 15 : rate; //字体太小也不好看的
    }

    /**
     * 获取屏幕宽高像素
     *
     * @param context
     * @return screenRect[0]:宽 | screenRect[1]:高
     */
    public static int[] getScreenRect(Context context) {

        int[] screenRect = new int[2];

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        screenRect[0] = dm.widthPixels;
        screenRect[1] = dm.heightPixels - getStatusBarHeight(context);

        return screenRect;
    }

    /**
     * 获取手机状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }
}
