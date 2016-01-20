package com.ag.common.webview.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;

import com.ag.common.webview.sqlite.AGCacheInfo;
import com.ag.common.webview.sqlite.CacheDao;

import java.io.File;


public class WebviewCacheUtil {

	private String TAG="WebviewCacheUtil";
	private static WebviewCacheUtil cacheUtil=new WebviewCacheUtil();
	public static WebviewCacheUtil Instance(){
		return cacheUtil;
	}

	public void initCacheView(WebView webView,Context context) {
		WebSettings settings = webView.getSettings();
		settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);

		// 开启javascript设置
		settings.setJavaScriptEnabled(true);
		// 设置可以使用localStorage
		settings.setDomStorageEnabled(true);
//		// 应用可以有数据库
		settings.setDatabaseEnabled(true);
		String cacheDirPath = context.getFilesDir().getAbsolutePath()
				+ "/webcache";
		Log.i("cachePath", cacheDirPath);
//        settings.setDatabasePath(cacheDirPath);
		settings.setAppCachePath(cacheDirPath);
		settings.setAppCacheEnabled(true);

		// LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
		// LOAD_DEFAULT: 根据cache-control决定是否从网络上取数据。
		// LOAD_CACHE_NORMAL: API level 17中已经废弃, 从API level
		// 11开始作用同LOAD_DEFAULT模式
		// LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
		// LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。

		// 如果有网络，则用WebSettings.LOAD_DEFAULT
		// 如果无网络，则启用缓存WebSettings.LOAD_CACHE_ELSE_NETWORK
		if (CheckNetworkState(context)) {
			// clearWebViewCache();
			System.out.println("网络状态:有");
			settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		} else {
			System.out.println("网络状态:无");
			settings.setCacheMode(WebSettings.LOAD_CACHE_ONLY);
		}

	}

	public String loadUrl(Context context,String webUrl) {
		return getRedirectUrl(context,getTrueUrl(context,webUrl));
	}

	public String getRedirectUrl(Context context,String url) {
		System.out.println("getRedirectUrl==" + url);
		if (!CheckNetworkState(context) && !TextUtils.isEmpty(url)) {
			// 获取重定向的网址
//			List<AGCacheInfo> list = (new DbHelper<AGCacheInfo>(context)).queryForEq(AGCacheInfo.class, "starturl", url);
//			AGCacheInfo info=(list!=null && list.size()>0)?list.get(0):null;
			AGCacheInfo info=new CacheDao(context).GetModel(url);
			if (info != null) {
				// System.out.println("重定向的网址=" + info.getRedirectUrl() +
				// ";starturl=" + info.getStartUrl());
				return info.getRedirectUrl();
			} else {
				// System.out.println("重定向的网址=null");
			}
		}
		return url;
	}

	public String getTrueUrl(Context context,String url) {
		if (CheckNetworkState(context))
			return url;
		String tmpUrl = url;
		if (url.lastIndexOf(".com") + 4 == url.length()) {
			tmpUrl = url + "/";
		}
		return tmpUrl;
	}

	public boolean CheckNetworkState(Context context) {

		ConnectivityManager cManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cManager.getActiveNetworkInfo();
		if (info == null) {
			return false;
		}
		if (info.isConnected()) {
			return true;
		}
		return false;
	}

	/**
	 * 清除WebView缓存
	 */
	public void clearWebViewCache(Context context) {

		// 清理Webview缓存数据库
		try {
			context.deleteDatabase("webview.db");
			context.deleteDatabase("webviewCache.db");
		} catch (Exception e) {
			e.printStackTrace();
		}

		// WebView 缓存文件
		File appCacheDir = new File(context.getDir("cache",
				Context.MODE_PRIVATE).getPath());

		File webviewCacheDir = new File(context.getCacheDir().getAbsolutePath()
				+ "/webviewCache");

		// 删除webview 缓存目录
		if (webviewCacheDir.exists()) {
			deleteFile(webviewCacheDir);
		}
		// 删除webview 缓存 缓存目录
		if (appCacheDir.exists()) {
			deleteFile(appCacheDir);
		}
	}

	/**
	 * 递归删除 文件/文件夹
	 *
	 * @param file
	 */
	public void deleteFile(File file) {

		Log.i(TAG, "delete file path=" + file.getAbsolutePath());

		if (file.exists()) {
			if (file.isFile()) {
				file.delete();
			} else if (file.isDirectory()) {
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					deleteFile(files[i]);
				}
			}
			file.delete();
		} else {
			Log.e(TAG, "delete file no exists " + file.getAbsolutePath());
		}
	}

}
