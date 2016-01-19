package com.ag.common.res;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class AGAssets {

	/**
	 * 读取Assets中的文件
	 * @param context
	 * @param fileName
	 * @return String
	 */
	public static String getFromAssetsFoString(Context context, String fileName){ 
		try { 
			InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName) ); 
			BufferedReader bufReader = new BufferedReader(inputReader);
			String line="";
			String Result="";
			while((line = bufReader.readLine()) != null)
				Result += line;
			return Result;
		} catch (Exception e) { 
			e.printStackTrace(); 
		}
		return null;
	}
	
}
