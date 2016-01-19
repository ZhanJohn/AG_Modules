package com.ag.common.tool;

import android.content.Context;
import android.text.ClipboardManager;

public class Clipboard {
	
	/**
	 * 实现文本复制功能
	 */
	@SuppressWarnings("deprecation")
	public static void copy(String str, Context context){
		// 得到剪贴板管理器
		ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
//		ClipData clip = ClipData.newPlainText("simple text","Hello, World!");
//		cmb.setPrimaryClip(clip);
		cmb.setText(str);
	}
	
	/**
	 * 实现粘贴功能
	 * @param context
	 * @return
	 */
	public static String paste(Context context){
		// 得到剪贴板管理器
		ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
		return cmb.getText().toString().trim();
	}
}
