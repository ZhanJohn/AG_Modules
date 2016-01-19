package com.ag.common.date;

import android.text.format.Time;

import java.util.Calendar;

public class SystemTimeUtils {
	
	public static String GetYearMonthDay(String split)
	{
		Calendar cal = Calendar.getInstance();// 使用日历类
		int year = cal.get(Calendar.YEAR);// 得到年
		int month = cal.get(Calendar.MONTH) + 1;// 得到月，因为从0开始的，所以要加1
		int day = cal.get(Calendar.DAY_OF_MONTH);// 得到天
		// int hour=cal.get(Calendar.HOUR);//得到小时
		// int minute=cal.get(Calendar.MINUTE);//得到分钟
		// int second=cal.get(Calendar.SECOND);//得到秒
		return String.format("%d%s%d%s%d",year,split,month,split,day);
	}
	
	public static String getNowDate(){
		Time t = new Time(); 
		t.setToNow(); 

		String month = null;
		String monthDay = null;
		String hour = null;
		String minute = null;

		if(t.month+1 < 10)
			month = "0" + (t.month + 1);
		else
			month = "" + (t.month + 1);

		if(t.monthDay < 10)
			monthDay = "0" + t.monthDay;
		else
			monthDay = "" + t.monthDay;

		if(t.hour < 10)
			hour = "0" + t.hour;
		else
			hour = "" + t.hour;

		if(t.minute < 10)
			minute = "0" + t.minute;
		else
			minute = "" + t.minute;

		return "更新于：" + month + "-" + monthDay + "  " + hour + ":" + minute;
	}
}
