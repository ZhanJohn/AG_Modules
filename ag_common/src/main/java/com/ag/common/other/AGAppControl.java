package com.ag.common.other;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PermissionInfo;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;

import com.ag.common.update.UpdateManager;
import com.ag.common.update.VersionInfo;

import java.util.List;

public class AGAppControl {

	private static final String SHAREDPREFERENCES_NAME = "my_pref";
	private static final String KEY_GUIDE_ACTIVITY = "guide_activity";

	/**
	 * 判断当前在前台运行的app是否当前app
	 * 
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean GetTopApp(Context context, String packageName) {
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);

		List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
		if (tasksInfo.size() > 0) {
			ComponentName topConponent = tasksInfo.get(0).topActivity;
			Log.i("packageName",
					packageName + "--" + topConponent.getPackageName());
			if (packageName.equals(topConponent.getPackageName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断版本是否初次加载，读取SharedPreferences中的guide_activity字段
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isFirstEnter(Context context) {
		if (context == null) {
			return false;
		}
		String mResultStr = context.getSharedPreferences(
				SHAREDPREFERENCES_NAME, Context.MODE_WORLD_READABLE).getString(
				KEY_GUIDE_ACTIVITY + GetVersionName(context), "");
		System.out.println("首次进入界面=" + mResultStr);
		if (mResultStr.equalsIgnoreCase("false"))
			return false;
		return true;
	}

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

	/**
	 * 获取本机号码
	 * 
	 * @param context
	 * @return
	 */
	public static String GetPhoneNumber(Context context) {
		TelephonyManager phoneMgr = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return phoneMgr.getLine1Number();
	}

	/**
	 * 获得软件名称
	 * 
	 * @param context
	 * @return
	 */
	public static String GetVersionName(Context context) {
		String versionName = "1.0.0";
		PackageManager packageManager = context.getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			versionName = packageInfo.versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return versionName;
	}

	/**
	 * 获得软件版本号
	 */
	public static int GetVersionCode(Context con) {
		int version = 1;
		PackageManager packageManager = con.getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(
					con.getPackageName(), 0);
			version = packageInfo.versionCode;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return version;
	}

	/**
	 * 获取Drawble-xx目录下的静态图片文件
	 * 
	 * @param context
	 * @param name
	 * @return
	 */
	public static int getDrawbleByName(Context context, String name) {
		return getValueIntByName(context, name, "drawable");
	}

	/**
	 * 获取Layout-xx目录下的静态布局
	 * 
	 * @param context
	 * @param name
	 * @return
	 */
	public static int getLayoutByName(Context context, String name) {
		return getValueIntByName(context, name, "layout");
	}

	/**
	 * 获取ID目录下的控件或值
	 * 
	 * @param context
	 * @param name
	 * @return
	 */
	public static int getIdByName(Context context, String name) {
		return getValueIntByName(context, name, "id");
	}

	/**
	 * 获取项目的静态文件或控件
	 * 
	 * @param context
	 * @param name
	 * @param type
	 *            drawble/layout/id
	 * @return
	 */
	public static int getValueIntByName(Context context, String name,
			String type) {
		return context.getResources().getIdentifier(name, type,
				context.getPackageName());
	}

	public static int GetScreenWidth(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		return dm.widthPixels;
	}

	public static int GetScreenHeight(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		return dm.heightPixels;
	}

	public static DisplayMetrics GetScreenDisplay(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		return dm;
	}

	/**
	 * 显示更新Dialog
	 * 
	 * @param activity
	 * @param version
	 * @param laterUpdate
	 */
	public static void ShowUpdateManagerDialog(Activity activity,
											   VersionInfo version, UpdateManager.ILaterUpdate laterUpdate) {
		UpdateManager manager = new UpdateManager(activity);

		manager.newVersionName = version.getVersion();
		manager.downApkUrl = version.getUrl();
		manager.IsMustUpdate = (version.getCompulsory().equals("1") ? false
				: false);
		manager.versionExplain = version.getHtmlremark();
		manager.setILaterUpdate(laterUpdate);
		manager.showNoticeDialog();
	}

	/**
	 * 设置首次启动引导
	 * 
	 * @param context
	 */
	public static void setFirstGuide(Context context) {
		SharedPreferences settings = context.getSharedPreferences("my_pref", 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("guide_activity" + GetVersionName(context), "false");
		editor.commit();
	}

	/**
	 * 判断当前应用是否处于DEBUG状态
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isApkDebugable(Context context) {
		try {
			ApplicationInfo info = context.getApplicationInfo();
			return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
		} catch (Exception e) {

		}
		return false;
	}

}
