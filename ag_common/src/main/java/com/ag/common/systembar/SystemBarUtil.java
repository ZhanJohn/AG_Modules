package com.ag.common.systembar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class SystemBarUtil {
	
	/**
	 * 如果界面中出现EditText是设置layout_alignParentBottom=true，弹出键盘后edittext不会弹起
	 * @param activity
	 * @param viewId layout top bar's id
	 * @param color set statusbar color
	 */
	public static void InitSystemBar(Activity activity,int viewId,int color){
		View barView=null;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			barView=activity.findViewById(viewId);
			setTranslucentStatus(activity,true);
		}

		SystemBarTintManager tintManager = new SystemBarTintManager(activity);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setNavigationBarTintEnabled(true);
		tintManager.setStatusBarTintResource(color);
		
		//set top bar's marginTop
		if(barView!=null){
			SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();

			if(barView.getLayoutParams().getClass().equals(LinearLayout.LayoutParams.class)){
				LinearLayout.LayoutParams params=(LinearLayout.LayoutParams)(barView.getLayoutParams());
				params.setMargins(0, config.getPixelInsetTop(false), 0, 0);
				barView.setLayoutParams(params);
			}else if(barView.getLayoutParams().getClass().equals(RelativeLayout.LayoutParams.class)){
				RelativeLayout.LayoutParams params=(RelativeLayout.LayoutParams)(barView.getLayoutParams());
				params.setMargins(0, config.getPixelInsetTop(false), 0, 0);
				barView.setLayoutParams(params);
			}else if(barView.getLayoutParams().getClass().equals(FrameLayout.LayoutParams.class)){
				FrameLayout.LayoutParams params=(FrameLayout.LayoutParams)(barView.getLayoutParams());
				params.setMargins(0, config.getPixelInsetTop(false), 0, 0);
				barView.setLayoutParams(params);
			}
		}
		
	}
	
	@TargetApi(19) 
	private static void setTranslucentStatus(Activity activity,boolean on) {
		Window win = activity.getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}
	
//	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
//	public static void SetSystemBar(Activity activity,boolean hasFocus){
//		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
//			
//			if (hasFocus) {
//				activity.getWindow().getDecorView().setSystemUiVisibility(
//						View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//						| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//						| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//						| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//						| View.SYSTEM_UI_FLAG_FULLSCREEN
//						| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//						);
//			}
//		}
//	}

	/**
	 * 设置沉浸栏，Activity设置之前，Layout布局要设置Style="@style/immersion_status"，系统的沉浸栏颜色可以通过Layout的background修改
	 * Android 4.4版本以上才有效
	 * @param context
     */
	public static void setImmesionStatus(Activity context){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			// 透明状态栏
			context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// 透明导航栏
			context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
	}
	
}
