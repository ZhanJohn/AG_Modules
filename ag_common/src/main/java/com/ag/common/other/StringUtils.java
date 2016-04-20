package com.ag.common.other;

import android.content.Context;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 *
 * 
 */
public final class StringUtils {

	/**
	 * 将double转换成2位小数
	 * 
	 * @param number
	 * @return
	 */
	public static String Number2Decimal(double number) {
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMaximumFractionDigits(2);
		return nf.format(number);
	}

	/**
	 * 是否为允许的字符
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isDigitsNumber(String str) {
		String reg = "(^[1-9]\\d*$|^[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*$)";
		return str.matches(reg);
	}

	/**
	 * 判断是否为纯数字(整数)
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str) {
		String reg = "^\\d+$";
		return str.matches(reg);
	}

	/**
	 * 默认的空值
	 */
	public static final String EMPTY = "";

	/**
	 * 检查字符串是否为空
	 * 
	 * @param str
	 *            字符串
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (str == null) {
			return true;
		} else if (str.trim().length() == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 返回字符串的非空值
	 * 
	 * @param str
	 * @return
	 */
	public static String GetNullEmptyValue(String str, String defaultStr) {
		if (isEmpty(str))
			return defaultStr;
		return str;
	}

	/**
	 * 检查字符串是否为空
	 * 
	 * @param str
	 *            字符串
	 * @return
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	/**
	 * 截取并保留标志位之前的字符串
	 * 
	 * @param str
	 *            字符串
	 * @param expr
	 *            分隔符
	 * @return
	 */
	public static String substringBefore(String str, String expr) {
		if (isEmpty(str) || expr == null) {
			return str;
		}
		if (expr.length() == 0) {
			return EMPTY;
		}
		int pos = str.indexOf(expr);
		if (pos == -1) {
			return str;
		}
		return str.substring(0, pos);
	}

	/**
	 * 截取并保留标志位之后的字符串
	 * 
	 * @param str
	 *            字符串
	 * @param expr
	 *            分隔符
	 * @return
	 */
	public static String substringAfter(String str, String expr) {
		if (isEmpty(str)) {
			return str;
		}
		if (expr == null) {
			return EMPTY;
		}
		int pos = str.indexOf(expr);
		if (pos == -1) {
			return EMPTY;
		}
		return str.substring(pos + expr.length());
	}

	/**
	 * 截取并保留最后一个标志位之前的字符串
	 * 
	 * @param str
	 *            字符串
	 * @param expr
	 *            分隔符
	 * @return
	 */
	public static String substringBeforeLast(String str, String expr) {
		if (isEmpty(str) || isEmpty(expr)) {
			return str;
		}
		int pos = str.lastIndexOf(expr);
		if (pos == -1) {
			return str;
		}
		return str.substring(0, pos);
	}

	/**
	 * 截取并保留最后一个标志位之后的字符串
	 * 
	 * @param str
	 * @param expr
	 *            分隔符
	 * @return
	 */
	public static String substringAfterLast(String str, String expr) {
		if (isEmpty(str)) {
			return str;
		}
		if (isEmpty(expr)) {
			return EMPTY;
		}
		int pos = str.lastIndexOf(expr);
		if (pos == -1 || pos == (str.length() - expr.length())) {
			return EMPTY;
		}
		return str.substring(pos + expr.length());
	}

	/**
	 * 把字符串按分隔符转换为数组
	 * 
	 * @param string
	 *            字符串
	 * @param expr
	 *            分隔符
	 * @return
	 */
	public static String[] stringToArray(String string, String expr) {
		return string.split(expr);
	}

	/**
	 * 去除字符串中的空格
	 * 
	 * @param str
	 * @return
	 */
	public static String noSpace(String str) {
		str = str.trim();
		str = str.replace(" ", "_");
		return str;
	}

	/**
	 * 替换字符串
	 * 
	 * @param from
	 *            String 原始字符串
	 * @param to
	 *            String 目标字符串
	 * @param source
	 *            String 母字符串
	 * @return String 替换后的字符串
	 */
	public static String replace(String from, String to, String source) {
		if (source == null || from == null || to == null)
			return null;
		StringBuffer bf = new StringBuffer("");
		int index = -1;
		while ((index = source.indexOf(from)) != -1) {
			bf.append(source.substring(0, index) + to);
			source = source.substring(index + from.length());
			index = source.indexOf(from);
		}
		bf.append(source);
		return bf.toString();
	}

	/**
	 * 替换字符串，能能够在HTML页面上直接显示(替换双引号和小于号)
	 * 
	 * @param str
	 *            String 原始字符串
	 * @return String 替换后的字符串
	 */
	public static String htmlencode(String str) {
		if (str == null) {
			return null;
		}

		return replace("\"", "&quot;", replace("<", "&lt;", str));
	}

	/**
	 * 替换字符串，将被编码的转换成原始码（替换成双引号和小于号）
	 * 
	 * @param str
	 *            String
	 * @return String
	 */
	public static String htmldecode(String str) {
		if (str == null) {
			return null;
		}

		return replace("&quot;", "\"", replace("&lt;", "<", str));
	}

	private static final String _BR = "<br/>";

	/**
	 * 在页面上直接显示文本内容，替换小于号，空格，回车，TAB
	 * 
	 * @param str
	 *            String 原始字符串
	 * @return String 替换后的字符串
	 */
	public static String htmlshow(String str) {
		if (str == null) {
			return null;
		}

		str = replace("<", "&lt;", str);
		str = replace(" ", "&nbsp;", str);
		str = replace("\r\n", _BR, str);
		str = replace("\n", _BR, str);
		str = replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;", str);
		return str;
	}

	/**
	 * 返回指定字节长度的字符串
	 * 
	 * @param str
	 *            String 字符串
	 * @param length
	 *            int 指定长度
	 * @return String 返回的字符串
	 */
	public static String toLength(String str, int length) {
		if (str == null) {
			return null;
		}
		if (length <= 0) {
			return "";
		}
		try {
			if (str.getBytes("GBK").length <= length) {
				return str;
			}
		} catch (Exception ex) {
		}
		StringBuffer buff = new StringBuffer();

		int index = 0;
		char c;
		length -= 3;
		while (length > 0) {
			c = str.charAt(index);
			if (c < 128) {
				length--;
			} else {
				length--;
				length--;
			}
			buff.append(c);
			index++;
		}
		buff.append("...");
		return buff.toString();
	}

	/**
	 * 返回指定长度的字符串
	 * 
	 * @param str
	 * @param length
	 * @return
	 */
	public static String subStr(String str, int length) {
		if (str == null) {
			return null;
		}
		if (length <= 0) {
			return "";
		}
		if (str.length() <= length) {
			return str;
		} else {
			return str.substring(0, length - 1) + "...";
		}
	}

	/**
	 * 判断是否为整数
	 * 
	 * @param str
	 *            传入的字符串
	 * @return 是整数返回true,否则返回false
	 */
	public static boolean isInteger(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		return pattern.matcher(str).matches();
	}

	/**
	 * 判断是否为浮点数，包括double和float
	 * 
	 * @param str
	 *            传入的字符串
	 * @return 是浮点数返回true,否则返回false
	 */
	public static boolean isDouble(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?\\d+\\.\\d*$");
		return pattern.matcher(str).matches();
	}

	/**
	 * 判断输入的字符串是否符合Email样式.
	 * 
	 * @param str
	 *            传入的字符串
	 * @return 是Email样式返回true,否则返回false
	 */
	public static boolean isEmail(String str) {
		Pattern pattern = Pattern
				.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
		return pattern.matcher(str).matches();
	}

	/**
	 * 判断输入的字符串是否为纯汉字
	 * 
	 * @param str
	 *            传入的字符窜
	 * @return 如果是纯汉字返回true,否则返回false
	 */
	public static boolean isChinese(String str) {
		Pattern pattern = Pattern.compile("[\u0391-\uFFE5]+$");
		return pattern.matcher(str).matches();
	}

	/**
	 * 是否为空白,包括null和""
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str) {
		return str == null || str.trim().length() == 0;
	}

	/**
	 * 判断是不是合法手机 handset 手机号码
	 */
	public static boolean isHandset(String handset) {
		try {
			if (handset == null || handset.length() != 11) {
				return false;
			}
			if (!handset.substring(0, 1).equals("1")) {
				return false;
			}
			String check = "^[0123456789]+$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(handset);
			boolean isMatched = matcher.matches();
			if (isMatched) {
				return true;
			} else {
				return false;
			}
		} catch (RuntimeException e) {
			return false;
		}
	}

	/**
	 * 格式化字符串
	 * 
	 * @param oBody
	 * @param len
	 *            截取长度
	 * @param isReplaceBR
	 *            是否替换换行字符
	 * @param isAddDot
	 *            是否在字符串后面加上省略号
	 * @return
	 */
	public static String CutOrFormatBody(String oBody, int len,
			Boolean isReplaceBR, Boolean isAddDot) {
		if (isEmpty(oBody))
			return "";
		else {
			String str = (len > 0) ? ((oBody.length() > len) ? (oBody
					.substring(0, len) + (isAddDot ? "…" : "")) : oBody)
					: oBody;
			if (isReplaceBR)
				return str.replace("\n", "<br />");
			else
				return str;
		}
	}

	/**
	 * 检查用户名是否合法
	 * 
	 * @param name
	 * @return
	 */
	public static boolean checkUserName(String name) {
		Pattern pattern = Pattern
				.compile("^([\u4E00-\uFA29]|[\uE7C7-\uE7F3]|[a-zA-Z0-9]){3,20}$");
		return pattern.matcher(name).matches();
	}

	/**
	 * 检查email格式
	 */
	public static boolean checkEmail(String email) {

		Pattern pattern = Pattern
				.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
		Matcher matcher = pattern.matcher(email);
		if (matcher.matches()) {
			return true;
		}
		return false;
	}

	/**
	 * 将字符串字面值转换为整数
	 * 
	 * @param value
	 *            字符串字面值
	 * @param defaultValue
	 *            默认值
	 * @return
	 */
	public static int SafeInt(String value, int defaultValue) {
		int result = 0;
		try {
			if (value.contains(",")) {
				value = value.replaceAll(",", "");
			}
			result = Integer.parseInt(value);
		} catch (Exception e) {
			return defaultValue;
		}
		return result;
	}

	public static Long safeLong(String value,Long defaultValue){
		long result = 0;
		try {
			if (value.contains(",")) {
				value = value.replaceAll(",", "");
			}
			result = Long.parseLong(value);
		} catch (Exception e) {
			return defaultValue;
		}
		return result;
	}

	/**
	 * 将字符串字面值转换为浮点数
	 * 
	 * @param value
	 * @param defaultValue
	 * @return
	 */
	public static double SafeDouble(String value, double defaultValue) {
		double result = 0;
		try {
			if (value.contains(",")) {
				value = value.replaceAll(",", "");
			}

			result = Double.parseDouble(value);

		} catch (Exception e) {
			return defaultValue;
		}
		return result;
	}

	
	public static float SafeFloat(String value,float defaultValue){
		float result=0;
		try{
			if (value.contains(",")) {
				value = value.replaceAll(",", "");
			}

			result=Float.parseFloat(value);
			
		}catch(Exception e){
			return defaultValue;
		}
		return result;
	}

	/**
	 * 将对象的字符串表示转换成整数
	 * 
	 * @param value
	 * @param defaultValue
	 * @return
	 */
	public static int SafeInt(Object value, int defaultValue) {
		int result = 0;
		try {
			if (value.toString().contains(",")) {
				value = value.toString().replaceAll(",", "");
			}
			result = Integer.parseInt(value.toString());
		} catch (Exception e) {
			return defaultValue;
		}
		return result;
	}

	/**
	 * 将对象转换成字符串表示
	 * 
	 * @param value
	 * @return
	 */
	public static String SafeString(Object value) {
		if (value == null)
			return "";
		return value.toString();
	}

	/**
	 * 把尾部的0删除
	 * 
	 * @param title
	 * @return
	 */
	public static String subZeroString(String title) {
		if (title == null) {
			return null;
		}
		if (title.endsWith(".00")) {
			return title.replace(".00", "");
		}
		if (title.endsWith(".0")) {
			return title.replace(".0", "");
		}
		while (title.contains(".") && title.endsWith("0")) {
			title = title.substring(0, title.length() - 1);
		}
		return title;
	}

	/**
	 * 设置文字突出显示(只设置第一次出现的关键字)
	 * 
	 * @param context
	 * @param focus
	 *            要突出显示的文字
	 * @param content
	 *            完整内容
	 * @param focusColor
	 *            突出颜色
	 * @param unColor
	 *            非突出颜色
	 * @return
	 */
	public static Spannable getProminentText(Context context, String focus,
			String content, int focusColor, int unColor) {

		if (StringUtils.isEmpty(focus) || StringUtils.isEmpty(content)) {
			return null;
		}
		Spannable WordtoSpan = new SpannableString(content);
		if (content.contains(focus)) {
			int begin = content.indexOf(focus);
			int end = begin + focus.length();
			WordtoSpan.setSpan(new ForegroundColorSpan(context.getResources()
					.getColor(focusColor)), begin, end,
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		} else {
			WordtoSpan.setSpan(new ForegroundColorSpan(context.getResources()
					.getColor(unColor)), 0, content.length(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return WordtoSpan;
	}

	/**
	 * 设置文字突出显示(可设置所有关键字)
	 * 
	 * @param focus
	 * @param content
	 * @return
	 */
	public static Spanned getPromlinentText(String focus, String content) {
		if (StringUtils.isEmpty(focus) || StringUtils.isEmpty(content)) {
			return null;
		}
		return Html.fromHtml(content.replaceAll(focus, "<font color='#ff0000'>"
				+ focus + "</font>"));
	}


}
