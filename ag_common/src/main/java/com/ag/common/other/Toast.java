package com.ag.common.other;

import android.content.Context;
import android.os.Handler;

public class Toast {

	public static int LENGTH_LONG = 1;
	public static int LENGTH_SHORT = 0;
	
	private static android.widget.Toast mToast;
	private static Handler mhandler = new Handler();
	
	private static Runnable r = new Runnable(){
		public void run() {
			mToast.cancel();
		};
	};

	/**
	 * 兼容旧代码的调用
	 * @param context
	 * @param text
	 * @param duration
	 * @return
	 */
	public static android.widget.Toast makeText (Context context, String text, int duration) {
		mhandler.removeCallbacks(r);
		if (null != mToast) {
			mToast.setText(text);
		} else {
			mToast = android.widget.Toast.makeText(context, text, duration);
		}
		mhandler.postDelayed(r, 5000);
		return mToast;
	}
	
	/**
	 * 更少参数，使用更方便
	 * @param context
	 * @param text
	 * @return
	 */
	public static void ToastMessage (Context context, String text) {
		makeText(context, text, LENGTH_LONG).show();
	}
	
	/**
	 * 从资源文件获取信息
	 * @param context
	 * @param strId
	 * @param duration
	 * @return
	 */
	public static android.widget.Toast makeText (Context context, int strId, int duration) {
		return makeText (context, context.getString(strId), duration);
	}
	
	/**
	 * 设置Toast位置
	 * @param gravity
	 * @param xOffset
	 * @param yOffset
	 */
	public static void setGravity(int gravity,int xOffset,int yOffset){
		mToast.setGravity(gravity, xOffset, yOffset);
	}
}

