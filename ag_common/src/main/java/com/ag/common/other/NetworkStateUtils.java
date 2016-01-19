package com.ag.common.other;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkStateUtils{
	
	public static boolean isConnected(Context context){
		return NetworkStateUtils.isMobileConnected(context) || NetworkStateUtils.isWifiConnected(context);
	}
	
	// 判断WIFI网络是否可用
	public static boolean isNetworkConnected(Context context) {  
		if (context != null) {  
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context  
					.getSystemService(Context.CONNECTIVITY_SERVICE);  
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();  
			if (mNetworkInfo != null) {  
				return mNetworkInfo.isConnected();  
			}  
		}  
		return false;  
	}

	// 判断WIFI网络是否可用
	public static boolean isWifiConnected(Context context) {  
		if (context != null) {  
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context  
					.getSystemService(Context.CONNECTIVITY_SERVICE);  
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager  
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);  
			if (mWiFiNetworkInfo != null) {  
				return mWiFiNetworkInfo.isConnected();  
			}  
		}  
		return false;  
	}

	//	 判断MOBILE网络是否可用
	public static boolean isMobileConnected(Context context) {  
		if (context != null) {  
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context  
					.getSystemService(Context.CONNECTIVITY_SERVICE);  
			NetworkInfo mMobileNetworkInfo = mConnectivityManager  
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);  
			if (mMobileNetworkInfo != null) {  
				return mMobileNetworkInfo.isConnected();  
			}  
		}  
		return false;  
	}

	// 获取当前网络连接的类型信息
	public static int getConnectedType(Context context) {  
		if (context != null) {  
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context  
					.getSystemService(Context.CONNECTIVITY_SERVICE);  
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();  
			if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {  
				return mNetworkInfo.getType();  
			}  
		}  
		return -1;  
	}
}
