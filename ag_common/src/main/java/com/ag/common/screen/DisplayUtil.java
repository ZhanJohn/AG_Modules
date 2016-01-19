package com.ag.common.screen;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * dp、sp 转换为 px 的工具类
 *
 *
 */
public class DisplayUtil {
	
	public static void ToTruePx(Context context, View view,int planPx,int basePx){
		ViewGroup.LayoutParams params = view.getLayoutParams();
		params.height = DisplayUtil.ToTruePx(context, planPx, basePx);
		view.setLayoutParams(params);
	}
	
	/**
	 * 根据设计稿标记的Px值转换为当前的真实px值
	 * @param planPx 原来标记的px值
	 * @param basePx 基数值：480/640/720……
	 * @return
	 */
	public static int ToTruePx(Context context, int planPx,int basePx){
//		return (int)(planPx*(CommonConfig.ScreenWidth/(basePx*1.0)));
		return (int)(planPx*(ScreenUtils.getScreenRect(context)[0]/(basePx*1.0)));
	}
	
	/**
	 * 将px值转换为dip或dp值，保证尺寸大小不变
	 * 
	 * @param pxValue
	 *            （DisplayMetrics类中属性density）
	 * @return
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 将dip或dp值转换为px值，保证尺寸大小不变
	 * 
	 * @param dipValue
	 *            （DisplayMetrics类中属性density）
	 * @return
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 * 
	 * @param pxValue
	 *            （DisplayMetrics类中属性scaledDensity）
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
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}
	
	
	public static void relationLayoutMargin(Context context, View v, float top, float bottom, float left, float right)
	{
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams
				(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		if(top>0)
		{
			lp.topMargin = px2dip(context,top);
		}
		if(bottom > 0)
		{
			lp.bottomMargin = px2dip(context,bottom);
		}
		if(left > 0)
		{
			lp.leftMargin = px2dip(context,left);
		}
		if(right > 0)
		{
			lp.rightMargin = px2dip(context, right);
		}
		
		v.setLayoutParams(lp);
	}
	
	public static void linearLayoutMargin(Context context, View v, float top, float bottom, float left, float right)
	{
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams
				(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		if(top>0)
		{
			lp.topMargin = px2dip(context,top);
		}
		if(bottom > 0)
		{
			lp.bottomMargin = px2dip(context,bottom);
		}
		if(left > 0)
		{
			lp.leftMargin = px2dip(context,left);
		}
		if(right > 0)
		{
			lp.rightMargin = px2dip(context, right);
		}
		
		v.setLayoutParams(lp);
	}
	
	public static void changeTextSize(Context context, float size, TextView t)
	{
		t.setTextScaleX(px2sp(context,size));
	}
	
	
}
