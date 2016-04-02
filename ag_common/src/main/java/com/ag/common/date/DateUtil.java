package com.ag.common.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {


	public static String getNowDatePath(){
		long time=System.currentTimeMillis();
		SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS");
		if (0 == time) {
			return "";
		}
		return mDateFormat.format(new Date(time));
	}

	public static String formatNowDateTime() {
		long time=System.currentTimeMillis();
		SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (0 == time) {
			return "";
		}
		return mDateFormat.format(new Date(time));
	}
	
	public static int Type = -1;
	public static String newGetCount(String dateStart, String dateEnd){
		
		Calendar startday = null;
		String[] startArray = dateStart.split("-");
		if (startArray.length == 3) {
			startday = Calendar.getInstance();
			startday.set(Integer.parseInt(startArray[0]), 
					Integer.parseInt(startArray[1])-1, 
					Integer.parseInt(startArray[2]));
			System.out.println("开始时间 " + dateStart);
		}else {
			System.err.println("开始时间格式错误");
			return null;
		}
		
		Calendar endday = null;
		String[] endArray = dateEnd.split("-");
		if (endArray.length == 3) {
			endday = Calendar.getInstance();
			endday.set(Integer.parseInt(endArray[0]), 
					Integer.parseInt(endArray[1])-1, 
					Integer.parseInt(endArray[2]));
			System.out.println("结束时间 " + dateEnd);
		}else {
			System.err.println("结束时间格式错误");
			return null;
		}
		
		// 开始日期在结束日期之后，表示已过期
		if(startday.after(endday)){ 
			return "text";
		}
		
		int[] result = getNeturalAge(startday, endday);
		int disYear = result[0];
		int disMonth = result[1];
		int disDay = result[2];
		
		if (disYear > 0) {
			// 年
			Type = 3;
			return String.valueOf(disYear);
		}else if (disMonth >= 3) {
			// 月
			Type = 2;
			return String.valueOf(disMonth);
		}else {
			// 日
			Type = 1;
			int dayCount = computeDateDifference(dateStart, dateEnd);
			if (dayCount <= 0) {
				return "text";
			}else {
				return String.valueOf(dayCount);
			}
		}
	}
	
	/**
	 * 
	 * @param calendarBirth 早（开始）
	 * @param calendarNow 晚（结束）
	 * @return
	 */
	public static int[] getNeturalAge(Calendar calendarBirth,Calendar calendarNow) { 
		int diffYears = 0, diffMonths, diffDays; 
		int dayOfBirth = calendarBirth.get(Calendar.DAY_OF_MONTH); 
		int dayOfNow = calendarNow.get(Calendar.DAY_OF_MONTH); 
		if (dayOfBirth <= dayOfNow) { 
			diffMonths = getMonthsOfAge(calendarBirth, calendarNow); 
			diffDays = dayOfNow - dayOfBirth; 
			if (diffMonths == 0) 
				diffDays++; 
		} else { 
			if (isEndOfMonth(calendarBirth)) { 
				if (isEndOfMonth(calendarNow)) { 
					diffMonths = getMonthsOfAge(calendarBirth, calendarNow); 
					diffDays = 0; 
				} else { 
					calendarNow.add(Calendar.MONTH, -1); 
					diffMonths = getMonthsOfAge(calendarBirth, calendarNow); 
					diffDays = dayOfNow + 1; 
				} 
			} else { 
				if (isEndOfMonth(calendarNow)) { 
					diffMonths = getMonthsOfAge(calendarBirth, calendarNow); 
					diffDays = 0; 
				} else { 
					calendarNow.add(Calendar.MONTH, -1);// 上个月 
					diffMonths = getMonthsOfAge(calendarBirth, calendarNow); 
					// 获取上个月最大的一天 
					int maxDayOfLastMonth = calendarNow.getActualMaximum(Calendar.DAY_OF_MONTH); 
					if (maxDayOfLastMonth > dayOfBirth) { 
						diffDays = maxDayOfLastMonth - dayOfBirth + dayOfNow; 
					} else { 
						diffDays = dayOfNow; 
					} 
				} 
			} 
		} 
		// 计算月份时，没有考虑年 
		diffYears = diffMonths / 12; 
		diffMonths = diffMonths % 12; 
		
		System.out.println("两个日期相差 " + diffYears + "年" + diffMonths + "个月" + diffDays + "日");
		
		return new int[] { diffYears, diffMonths, diffDays }; 
	} 


	/**  
	 * 获取两个日历的月份之差  
	 *   
	 * @param calendarBirth  
	 * @param calendarNow  
	 * @return  
	 */ 
	public static int getMonthsOfAge(Calendar calendarBirth, Calendar calendarNow) {  
		return (calendarNow.get(Calendar.YEAR) - calendarBirth  
				.get(Calendar.YEAR))* 12+ calendarNow.get(Calendar.MONTH)  
				- calendarBirth.get(Calendar.MONTH);  
	} 

	public static boolean isEndOfMonth(Calendar calendar) {  
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);  
		if (dayOfMonth == calendar.getActualMaximum(Calendar.DAY_OF_MONTH))  
			return true;  
		return false;  
	} 
	
	/**
	 * 判断第一个时间比第二个大
	 * @param DATE1
	 * @param DATE2
	 * @return
	 */
	public static boolean compare_date(String DATE1, String DATE2) {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date dt1 = df.parse(DATE1);
			Date dt2 = df.parse(DATE2);
			if (dt1.getTime() > dt2.getTime()) {
				return true;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 判断两个日期相等
	 * @param DATE1
	 * @param DATE2
	 * @return
	 */
	public static boolean compare_equal_date(String DATE1, String DATE2) {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date dt1 = df.parse(DATE1);
			Date dt2 = df.parse(DATE2);
			if (dt1.getTime() == dt2.getTime()) {
				return true;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 判断第一个时间比第二个大小或等于
	 * @param DATE1
	 * @param DATE2
	 * @return
	 */
	public static int compare_date_time(String DATE1, String DATE2) {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			System.out.println("compare_date_time:DATE1="+DATE1+";DATE2="+DATE2);
			Date dt1 = df.parse(DATE1);
			Date dt2 = df.parse(DATE2);
			if (dt1.getTime() > dt2.getTime()) {
				System.out.println("compare_date_time = 1");
				return 1;
			}
			if (dt1.getTime() == dt2.getTime()) {
				System.out.println("compare_date_time = 2");
				return 2;
			}
		} catch (Exception exception) {
			System.out.println("compare_date_time = exception");
			exception.printStackTrace();
		}
		System.out.println("compare_date_time = 0");
		return 0;
	}

	/**
	 * 计算日期差值（该方法不止汽车模块使用，请注意）
	 * @return
	 */
	public static int computeDateDifference(String nearDate, String farDate){

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		try{
			Date d1 = df.parse(farDate);
			Date d2 = df.parse(nearDate);
			long diff = d1.getTime() - d2.getTime();//这样得到的差值是微秒级别

			long days = diff / (1000 * 60 * 60 * 24);
			long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
			long minutes = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);

			System.out.println(""+days+"天"+hours+"小时"+minutes+"分");

			return (int)days;
		}
		catch (Exception e){
		}
		return -1;
	}

	/**
	 * 获取当前日期
	 * @return
	 */
	public static String GetDateFormat(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String dateTime = sdf.format(new Date());
		return dateTime;
	}

	/**
	 * 比较时间先后(与当前时间相比)
	 * @return
	 * @throws Exception
	 */
	public static boolean DateCompare(String checkDate) throws Exception {
		// 设定时间的模板
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String nowDate = sdf.format(new Date());
		// 得到指定模范的时间
		Date d1 = sdf.parse(checkDate);
		Date d2 = sdf.parse(nowDate);
		// 比较
		if (d2.getTime() > d1.getTime()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 字符串转日期
	 * @param dateStr
	 * @return
	 */
	public static Date StringToDate(String dateStr){

		String separator = dateStr.substring(4,5);
		String format = "yyyy" + separator + "MM" + separator + "dd" + " " + "HH:mm:ss";

		DateFormat dd = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = dd.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 计算月份差值(这样计算是不完美的，如果far比near更早，那算出来就是负数，属于过期时间了，只是在前面过期的话不会调用该方法，所以没事)
	 * @param data1 后
	 * @param data2 前
	 * @return
	 */
//	public static int computeMMDifference(String farDate, String nearDate){
//
//		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//
//		try{
//			Date d1 = df.parse(farDate);
//			Date d2 = df.parse(nearDate);
//
//			int diff = (d1.getYear()-d2.getYear())*12 + d1.getMonth() - d2.getMonth();
//			return diff;
//
//		}
//		catch (Exception e){
//		}
//		return -1;
//	}

	/**
	 * 计算年份差值（跟计算月份的时候一样不完美）
	 * @param data1
	 * @param data2
	 * @return
	 */
//	public static int computeYearDifference(int day){
//		return day/365;
//	}
	
//	public static int computeYearDifference(String farDate,String nearDate){
//
//		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//
//		try{
//			Date d1 = df.parse(farDate);
//			Date d2 = df.parse(nearDate);
//			int diff = d1.getYear() - d2.getYear();
//			return diff;
//		}
//		catch (Exception e){
//		}
//		return -1;
//	}
}

