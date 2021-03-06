package com.ag.common.other;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;

import com.ag.common.res.AGResource;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AGActivity {

	public static String getQQNumber(String url){
		System.out.println("getQQNumber=="+url);
		//tencent://message/?uin=120285926&Site=qq&Menu=yes
		String patternUrl="^tencent://message/\\?uin=([0-9]+)&Site=qq&Menu=yes$";
		Pattern pattern = Pattern.compile(patternUrl);
		Matcher matcher = pattern.matcher(url);
		boolean flag=matcher.find();
		if(flag){
			String group=matcher.group(1);
			System.out.println("flag==" + group);
			return group;
		}
		return null;
	}
	
	public static void openQQ(Activity activity,String qq){
		String url="mqqwpa://im/chat?chat_type=wpa&uin="+qq;  
		activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));  
	}

	public static void callPhone(Activity activity, String phone){
		Intent in2 = new Intent();
	    in2.setAction(Intent.ACTION_VIEW);//指定意图动作
		in2.setData(Uri.parse("tel:"+phone));//指定电话号码
		activity.startActivity(in2);
	}
	
	public static void leftIn(Activity activity){
		activity.overridePendingTransition(AGResource.getValueIntByName(activity, "push_right_in", "anim"), AGResource.getValueIntByName(activity, "push_left_out", "anim"));
	}
	
	public static void rightOut(Activity activity){
		activity.overridePendingTransition(AGResource.getValueIntByName(activity, "push_left_in", "anim"), AGResource.getValueIntByName(activity, "push_right_out", "anim"));
	}
	
	public static void showActivity(Activity activity,Class<?> cls){
		Intent intent = new Intent(activity,cls);
		activity.startActivity(intent);
		leftIn(activity);
	}
	
	public static void showActivity(Activity activity,Class<?> cls, String className){
		Intent intent = new Intent(activity,cls);
		intent.putExtra("className", className);
		activity.startActivity(intent);
		leftIn(activity);
	}
	
	public static void showActivityAndFinish(Activity activity,Class<?> cls){
		showActivity(activity, cls);
		activity.finish();
	}
	
	public static void showActivityForResult(Activity activity,Class<?> cls,int request){
		Intent intent = new Intent(activity,cls);
		activity.startActivityForResult(intent,request);
		leftIn(activity);
	}

	public static void showActivityForResult(Activity activity, Class<?> cls, int request, String key, Bundle bundle){
		Intent intent = new Intent(activity,cls);
		intent.putExtra(key,bundle);
		activity.startActivityForResult(intent,request);
		leftIn(activity);
	}

	public static void showActivityForResult(Fragment fragment, Class<?> cls, int request){
		Intent intent = new Intent(fragment.getActivity(),cls);
		fragment.startActivityForResult(intent,request);
		leftIn(fragment.getActivity());
	}

	public static void showActivityForResult(Fragment fragment, Class<?> cls, int request,String key, Bundle bundle){
		Intent intent = new Intent(fragment.getActivity(),cls);
		intent.putExtra(key,bundle);
		fragment.startActivityForResult(intent,request);
		leftIn(fragment.getActivity());
	}

	public static void showImageActivity(Activity activity,int request){
		Intent intent;
		if (Build.VERSION.SDK_INT < 19) {
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");

		} else {
			intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		}
		activity.startActivityForResult(intent, request);
	}

	public static void showImageActivity(Fragment fragment,int request){
		Intent intent;
		if (Build.VERSION.SDK_INT < 19) {
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");

		} else {
			intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		}
		fragment.startActivityForResult(intent, request);
	}

	public static void showSystemSetting(Context context){
		Intent intent=new Intent(Settings.ACTION_SETTINGS);
		context.startActivity(intent);
	}

	public static void showSystemWifiSetting(Context context){
		Intent intent=new Intent(Settings.ACTION_WIFI_SETTINGS);
		context.startActivity(intent);
	}

	public static void showSystemApnSetting(Context context){
		Intent intent=new Intent(Settings.ACTION_APN_SETTINGS);
		context.startActivity(intent);
	}
	
}
