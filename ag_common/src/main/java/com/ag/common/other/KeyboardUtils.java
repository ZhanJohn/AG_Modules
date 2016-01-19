package com.ag.common.other;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class KeyboardUtils {

	/**
	 * 显示即隐藏 | 隐藏即显示
	 * @param context
	 */
	public static void showOrHidentKeyboard(Context context){
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	/**
	 * 强制显示软键盘
	 * @param context
	 * @param view
	 */
	public static void showKeyboard(Context context, View view){
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);  
		imm.showSoftInput(view,InputMethodManager.SHOW_FORCED); 
	}

	/**
	 * 强制隐藏软键盘
	 * @param context
	 * @param view
	 */
	public static void hidentKeyboard(Context context, View view){
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken() , 0); 
	}
}
