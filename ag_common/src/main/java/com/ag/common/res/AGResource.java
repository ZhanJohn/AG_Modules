package com.ag.common.res;

import android.content.Context;

public class AGResource {

	public static int getMipMapByName(Context context,String name){
		return getValueIntByName(context, name, "mipmap");
	}

	/**
	 * 获取Drawble-xx目录下的静态图片文件
	 * @param context
	 * @param name
	 * @return
	 */
	public static int getDrawbleByName(Context context,String name){
		return getValueIntByName(context, name, "drawable");
	}
	
	/**
	 * 获取Layout-xx目录下的静态布局
	 * @param context
	 * @param name
	 * @return
	 */
	public static int getLayoutByName(Context context,String name){
		return getValueIntByName(context, name, "layout");
	}
	
	/**
	 * 获取ID目录下的控件或值
	 * @param context
	 * @param name
	 * @return
	 */
	public static int getIdByName(Context context,String name){
		return getValueIntByName(context, name, "id");
	}

	/**
	 * 获取String ID
	 * @param context
	 * @param name
     * @return
     */
	public static int getStringByName(Context context,String name){
		return getValueIntByName(context, name, "string");
	}

	/**
	 * 获取Style值
	 * @param context
	 * @param name
     * @return
     */
	public static int getStyleByName(Context context,String name){
		return getValueIntByName(context, name, "style");
	}
	
	/**
	 * 获取项目的静态文件或控件
	 * @param context
	 * @param name
	 * @param type drawble/layout/id
	 * @return
	 */
	public static int getValueIntByName(Context context,String name,String type){
		return context.getResources().getIdentifier(name, type, context.getPackageName());
	}
	
}
