package com.ag.common.pattern;

import android.text.TextUtils;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则工具类
 *
 */
public class PatternUtil {
	
	/**
	 * 检查车牌号码
	 * @param value
	 * @return
	 */
	public static boolean CheckCarNumber(String value){
		Log.i("车牌号码", value);
		String regEx = "^[\u4e00-\u9fa5a-zA-Z0-9]{1,10}";
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(value);
		return mat.find();
	}

	/**
	 * 检查中英文和数字，不含特殊字符
	 * @param value
	 * @return
	 */
	public static boolean CheckNormalWord(String value){
		String regEx = "^[\u4e00-\u9fa5a-zA-Z0-9]+$";
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(value);
		return mat.find();
	}
	
	/**
	 * 检查中英文，不含特殊字符
	 * @param value
	 * @return
	 */
	public static boolean CheckChinaAndEnlishWord(String value){
		String regEx = "^[\u4e00-\u9fa5a-zA-Z]+$";
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(value);
		return mat.find();
	}
	
	/**
	 * 中文、英文、数字
	 * @param value
	 * @param maxLeng
	 * @return
	 */
	public static boolean CheckChinaAndEnlish(String value,int maxLeng){
		String regEx = "^[\u4e00-\u9fa5a-zA-Z0-9]{1,"+maxLeng+"}";
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(value);
		return mat.find();
	}
	
	public static boolean CheckChinaAndEnlish(String value,int minLeng,int maxLeng){
		String regEx = "^[\u4e00-\u9fa5a-zA-Z0-9]{"+minLeng+","+maxLeng+"}";
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(value);
		return mat.find();
	}
	
	/**
	 * 字母、数字
	 * @param value
	 * @param maxLeng
	 * @return
	 */
	public static boolean CheckEnlishWord(String value,int maxLeng){
		String regEx = "^[a-zA-Z0-9]{1,"+maxLeng+"}";
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(value);
		return mat.find();
	}
	
	/**
	 * 字母、数字、英文符号
	 * @param value
	 * @param maxLeng
	 * @return
	 */
	public static boolean CheckEnlishSignWord(String value,int minLeng,int maxLeng){//\\$
		String regEx = "^[a-zA-Z0-9,.?!:/@\";'~()<>*$&%-+_=`#\\{}|\\^\\[\\]]{"+minLeng+","+maxLeng+"}";//,.?!:/@\";'~()<>*$&[]\\`#$%^-+_={}|
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(value);
		return mat.find();
	}

	public static String checkMobile(String mobile){
		if(TextUtils.isEmpty(mobile) || mobile.length()<11)
			return null;

		mobile=mobile.replace("-","");
		if(mobile.length()<11)
			return null;

		mobile=mobile.substring(mobile.length()-11);

		String regEx = "^1(\\d){10}$";
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(mobile);
		if(mat.find()){
			return mobile;
		}
		return null;
	}
	
}
