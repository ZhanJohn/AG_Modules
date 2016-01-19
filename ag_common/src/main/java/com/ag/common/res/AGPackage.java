package com.ag.common.res;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PermissionInfo;

import java.util.List;

public class AGPackage {

	/**
	 * 获取当前程序版本名
	 * 
	 * @return
	 */
	public static String getPackageVersion(Context context) {
		String version = "";
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = null;
			pi = pm.getPackageInfo(context.getPackageName(), 0);
			version = pi.versionName;
		} catch (Exception e) {
			version = ""; // failed, ignored
		}
		return version;
	}

	/**
	 * 获取当前程序包名
	 * 
	 * @return
	 */
	public static String getPackageName(Context context) {
		String version = "";
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = null;
			pi = pm.getPackageInfo(context.getPackageName(), 0);
			version = pi.packageName;
		} catch (Exception e) {
			version = ""; // failed, ignored
		}
		return version;
	}

	/**
	 * 获取当前程序版本code
	 * 
	 * @return
	 */
	public static int getPackageCode(Context context) {
		int code = 1;
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = null;
			pi = pm.getPackageInfo(context.getPackageName(), 0);
			code = pi.versionCode;
		} catch (Exception e) {
			code = 1; // failed, ignored
		}
		return code;
	}

	// 检查某个应用是否安装
	public static boolean checkAPP(Context context, String packageName) {
		if (packageName == null || "".equals(packageName))
			return false;
		try {
			ApplicationInfo info = context.getPackageManager()
					.getApplicationInfo(packageName,
							PackageManager.GET_UNINSTALLED_PACKAGES);
			System.out.println("应用包名=" + info.packageName);
			return true;
		} catch (NameNotFoundException e) {
			return false;
		}
	}

	/**
	 * 获取所有应用的名称，包名，以及权限 有了包名就可以判断是否有某个应用了
	 */
	public static String GetPackages(final Context context) {
		List<PackageInfo> list = context.getPackageManager()
				.getInstalledPackages(PackageManager.GET_PERMISSIONS);

		StringBuilder stringBuilder = new StringBuilder();

		for (PackageInfo packageInfo : list) {
			stringBuilder.append("package name:" + packageInfo.packageName
					+ "\n");

			ApplicationInfo applicationInfo = packageInfo.applicationInfo;
			stringBuilder.append("应用名称:"
					+ applicationInfo.loadLabel(context.getPackageManager())
					+ "\n");
			if (packageInfo.permissions != null) {

				for (PermissionInfo p : packageInfo.permissions) {
					stringBuilder.append("权限包括:" + p.name + "\n");
				}
			}
			stringBuilder.append("\n");
		}
		return stringBuilder.toString();
	}

}
