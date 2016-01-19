package com.ag.common.pattern;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpecialSignUtils {
	
	/**
	 * 一个中文算2个长度，截取不超过指定长度的字符
	 * @param string
	 * @param appointLen
	 * @return
	 */
	public static String subLenToString(String string, int appointLen){
		
		try {
			String result;
			String TAG = "subLenToString";
			int realLen = string.length();
			int virtualLen = string.getBytes("gbk").length;

			System.out.println(TAG + "string 实际长度 " + realLen);
			System.out.println(TAG + "string 虚拟长度 " + virtualLen);

			// 当这个大于等于虚拟长度，就到了
			int subLen = 0;
			// 实际长度的截取下标
			int subIndex = 0;

			if (virtualLen > appointLen) {

				for (int i = 0; i < string.length(); i++) {
					System.out.println(TAG + "循环 " + string.substring(i, i+1));
					if (isChinesePattern(string.substring(i, i+1))) {
						subLen += 2;
					}else {
						subLen += 1;
					}
					if (subLen >= appointLen) {
						subIndex = i + 1;
						break;
					}
				}
				
				if (subIndex > 0 && subIndex <= string.length()) {
					
					System.out.println(TAG + "截取结果 " + string.substring(0, subIndex));

					result = string.substring(0, subIndex);
					
					if (result.getBytes("gbk").length > appointLen) {
						result = result.substring(0, result.length()-1);
					}
					
					return result;
				}
				
				return "";
			}else {
				System.out.println(TAG + "无需截取  " + string);
				return string;
			}

			

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * 判断真实姓名格式是否正确(支持小写字母、大写字母、中文、空格、·) 长度2-50
	 * @param mobiles  /^[a-z|A-Z|\u4E00-\u9FA5|\s|·]{2,50}$/
	 * @return
	 */
	public static boolean isNamePattern(String mobiles) {
		Pattern p = Pattern.compile("^[a-z|A-Z|\u4E00-\u9FA5|\\s|·]{2,50}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}
	

	/**
	 * 判断昵称格式是否正确
	 * @param mobiles
	 * @return
	 */
	public static boolean isNickName(String mobiles) {
		Pattern p = Pattern.compile("^[a-z|A-Z|\u4E00-\u9FA5|0-9|_]{2,20}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}
	
	/**
	 * 汽车排量格式(支持小写字母、大写字母、数字、小数点、空格)
	 * @param mobiles
	 * @return
	 */
	public static boolean isOutVolumePattern(String mobiles) {
		Pattern p = Pattern.compile("^[0-9|a-z|A-Z|\\s|.]{1,6}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}
	
	/**
	 * 车型
	 * @param mobiles
	 * @return
	 */
	public static boolean isCarTypePattern(String mobiles) {
		Pattern p = Pattern.compile("^[0-9|a-z|A-Z|\\s|.|\u4E00-\u9FA5]{1,50}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}
	
	/**
	 * 中文
	 * @param mobiles
	 * @return
	 */
	public static boolean isChinesePattern(String mobiles){
		Pattern p = Pattern.compile("^[\u4E00-\u9FA5]$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}
}
