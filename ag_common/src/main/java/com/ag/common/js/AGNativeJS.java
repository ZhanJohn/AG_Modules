package com.ag.common.js;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import com.ag.common.file.AGFile;
import com.ag.common.mediaplay.SoundEnum;
import com.ag.common.mediaplay.SoundListService;
import com.ag.common.mediaplay.SoundService;
import com.ag.common.other.AGAppControl;
import com.ag.common.other.AGLog;
import com.google.gson.Gson;

import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Calendar;
import java.util.Locale;

/**
 * 需要被JS调用的函数，必须定义成public static，且必须包含WebView这个参数
 *
 */
public class AGNativeJS {
	
	protected static String GetJsResult(String ret,String msg){
		JsResultInfo info=new JsResultInfo();
		info.setMsg(msg);
		info.setRet(ret);
		Gson gson=new Gson();
		return gson.toJson(info);
	}

	/**
	 * 调用原生打印Debug日志
	 * @param webView 
	 * @param outputinfo 要输入的相关信息
	 * @param type 0,控制台输出;1,弹出窗口
	 */
	public static String NativeLog(WebView webView,String outputinfo,int type){
		if(TextUtils.isEmpty(outputinfo))
			return GetJsResult("0", "");
		if(type==0){
			AGLog.Print(outputinfo);
		}else{
			NativeAlert(webView, outputinfo);
		}
		return GetJsResult("1", "");
	}
	
	/**
	 * 调用原生新开一个窗口，打开一个Url
	 * @param webView
	 * @param url
	 */
	public static String NativeOpenUrl(WebView webView,String url){
		if(TextUtils.isEmpty(url))
			return GetJsResult("0", "");
		Log.i("NativeOpenUrl", url);
		webView.loadUrl(url);
		return GetJsResult("1", "");
	}
	
	/**
	 * 读取文件
	 * @param webView
	 * @param document
	 * @param filepath
	 * @return
	 */
	public static String NativeReadFile(WebView webView,String document,String filepath){
		String res = "";
		try {
			FileInputStream fin = webView.getContext().openFileInput(filepath);
			int length = fin.available();
			byte[] buffer = new byte[length];
			fin.read(buffer);
			res = EncodingUtils.getString(buffer, "UTF-8");
			fin.close();
		} catch (Exception e) {
			e.printStackTrace();
			return GetJsResult("0", "读取失败");
		}
		return GetJsResult("1", res);
	}
	
	/**
	 * 写入文件
	 * @param webView
	 * @return
	 */
	public static String NativeWriteFile(WebView webView,String document,String filepath,String data){
		try {
			FileOutputStream fout = webView.getContext().openFileOutput(filepath,
					Context.MODE_PRIVATE);

			byte[] bytes = data.getBytes();
			fout.write(bytes);
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
			return GetJsResult("0", "写入失败");
		} 
		return GetJsResult("1", "写入成功");
	}
	
	/**
	 * 删除文件
	 * @param webView
	 * @param document
	 * @param filepath
	 * @return
	 */
	public static String NativeDeleteFile(WebView webView,String document,String filepath){
		File file=new File("./data/data/"+webView.getContext().getPackageName()+"/files/"+filepath);
		boolean flag=false;
		if(file.exists() && file.isFile()){
			flag=file.delete();
		}
		return GetJsResult(flag?"1":"0", flag?"删除成功":"删除失败");
	}
	
	/**
	 * 下载文件
	 * @param webView
	 * @return
	 */
	@SuppressWarnings({ "static-access"})
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static String NativeDownloadFile(WebView webView,String fileUri,String savePath,String document){
		if(TextUtils.isEmpty(fileUri)){
			return GetJsResult("0", "下载失败");
		}
		
		DownloadManager downloadManager=(DownloadManager)webView.getContext().getSystemService(webView.getContext().DOWNLOAD_SERVICE);
		//开始下载   
        Uri resource = Uri.parse(fileUri);   
        Request request = new Request(resource);
        request.setAllowedNetworkTypes(Request.NETWORK_MOBILE | Request.NETWORK_WIFI);   
        request.allowScanningByMediaScanner();
//        request.setAllowedOverRoaming(false);   
//        //设置文件类型  
//        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();  
//        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(fileUri));  
//        request.setMimeType(mimeString);  
        //在通知栏中显示   
//        request.setShowRunningNotification(false);  
//        request.setVisibleInDownloadsUi(false);  
        //sdcard的目录下的download文件夹
        AGFile.creatSDDir(document);
        request.setDestinationInExternalPublicDir("/"+document+"/", savePath);  
//        request.setTitle("移动G3广告");   
        downloadManager.enqueue(request);
        
		return GetJsResult("1", "下载开始");
	}
	
	/**
	 * 播放音频
	 * @param webView
	 * @param document
	 * @param numofloops -1,循环播放； 0,播放一次； n(>0),播放n次；
	 * @return
	 */
	public static String NativePlayAudio(WebView webView,String document,String filename,int numofloops){
		Intent intent = new Intent(webView.getContext(),SoundService.class);
        intent.putExtra(SoundService.Play_Key, SoundEnum.Play.GetSoundEnum());
        intent.putExtra(SoundService.Num_Loop_Key, numofloops);
        intent.putExtra(SoundService.Sound_Path_Key, document+"/" + filename);
        webView.getContext().startService(intent);
		return GetJsResult("1", "");
	}
	
	public static String NativePlayAudio(WebView webView,String internetPath,int numofloops){
		Intent intent = new Intent(webView.getContext(),SoundService.class);
        intent.putExtra(SoundService.Play_Key, SoundEnum.Play.GetSoundEnum());
        intent.putExtra(SoundService.Num_Loop_Key, numofloops);
        intent.putExtra(SoundService.Sound_Path_Key, internetPath);
        webView.getContext().startService(intent);
		return GetJsResult("1", "");
	}

	/**
	 * 暂停音频播放
	 * @param webView
	 * @return
	 */
	public static String NativePauseAudio(WebView webView){
		Intent intent = new Intent(webView.getContext(),SoundService.class);
        intent.putExtra(SoundService.Play_Key, SoundEnum.Pause.GetSoundEnum());
        webView.getContext().startService(intent);
		return GetJsResult("1", "");
	}
	
	/**
	 * 停止音频播放
	 * @param webView
	 * @return
	 */
	public static String NativeStopAudio(WebView webView){
		Intent intent = new Intent(webView.getContext(),SoundService.class);
        intent.putExtra(SoundService.Play_Key, SoundEnum.Stop.GetSoundEnum());
        webView.getContext().startService(intent);
		return GetJsResult("1", "");
	}
	
	public static String NativePlayListAudio(WebView webView,String document,String filename,int numofloops){
		Intent intent = new Intent(webView.getContext(),SoundListService.class);
        intent.putExtra(SoundService.Num_Loop_Key, numofloops);
        intent.putExtra(SoundService.Sound_Path_Key, document+"/" + filename);
        webView.getContext().startService(intent);
		return GetJsResult("1", "");
	}
	
	public static String NativePlayListAudio(WebView webView,String internetPath,int numofloops){
		Intent intent = new Intent(webView.getContext(),SoundListService.class);
        intent.putExtra(SoundService.Num_Loop_Key, numofloops);
        intent.putExtra(SoundService.Sound_Path_Key, internetPath);
        webView.getContext().startService(intent);
		return GetJsResult("1", "");
	}
	
	/**
	 * 向原生注册网络状态监听
	 * @param webView
	 * @param jsCallbackMethod js回调方法
	 * @param pingUrl ping网络连接
	 * @return
	 */
	public static String NativeRegNetworkListen(final WebView webView,final String pingUrl,final String jsCallbackMethod){
		// 无网络：-1
		// 其他未知网络：-2
		// Mobile网络：0
		// Wifi网络:1
		ConnectivityManager mConnectivityManager = (ConnectivityManager) webView.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		int type = -1;
		final NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
		if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
			type = mNetworkInfo.getType();
		}
		if (type == ConnectivityManager.TYPE_WIFI
				|| type == ConnectivityManager.TYPE_MOBILE) {
			new AsyncTask<Void, Integer, Boolean>() {

				@Override
				protected Boolean doInBackground(Void... params) {
					return NativeConnectState(pingUrl);
				}

				@Override
				protected void onPostExecute(Boolean result) {
					super.onPostExecute(result);
					int type=-1;
					if(result){
						if(mNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI)
							type=1;
						else{
							type=0;
						}
					}else{
						if(mNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI ||
								mNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
							type=-1;
						else{
							type=-2;
						}
					}
					webView.loadUrl("javascript:"+jsCallbackMethod+"(" + type + ")");
				}
				
			}.execute();
			
		} else {
			type = -1;
			webView.loadUrl("javascript:"+jsCallbackMethod+"('" + type + "')");
		}
		return GetJsResult("1", "");
	}
	
	/**
	 * 获取网络状态
	 * @param pingUrl ping网络连接
	 * @return
	 */
	public static boolean NativeConnectState(String pingUrl){
		try {
			URL url = new URL(pingUrl);
			HttpURLConnection conn = (HttpURLConnection) url
					.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5 * 1000);
			conn.setRequestProperty("Charsert", "utf-8");
			if (conn.getResponseCode() == 200) {
				return true;
			}
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 向原生注销网络状态监听
	 * @param webView
	 * @return
	 */
	public static String NativeUnRegNetworkListen(WebView webView){
		
		return GetJsResult("1", "");
	}
	
	/**
	 * 调用原生的拍照功能，拍照后照片保存到手机相册中
	 * @param webView
	 * @return
	 */
	public static String NativeTakePicture(WebView webView){
		
		String name = DateFormat.format("yyyyMMdd_hhmmss",
				Calendar.getInstance(Locale.CHINA))
				+ ".jpg";
		File file = new File("/sdcard/DCIM/");
		if (!file.exists())
			file.mkdirs();// 创建文件夹
		String photoPath = "/sdcard/DCIM/" + name;
		
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		File out = new File(photoPath);
		Uri uri = Uri.fromFile(out);
		intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION,ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		
		webView.setTag(photoPath);
		
		((Activity)webView.getContext()).startActivityForResult(intent,1);
		return GetJsResult("1", "");
	}
	
	/**
	 * 从相册中获取一张或多张照片
	 * @param webView
	 * @return
	 */
	public static String NativeSelectPicture(WebView webView,String jsCallbackMethod){
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				"image/*");
		webView.setTag(jsCallbackMethod);
		((Activity)webView.getContext()).startActivityForResult(intent, 3);
		return GetJsResult("1", "");
	}

	
	/**
	 * 在地图上标示某个地理位置
	 * @param webView
	 * @param latitude 纬度，范围为90 ~ -90
	 * @param longitude 经度，范围为180 ~ -180
	 * @param name 位置名
	 * @param address 地址详情说明
	 * @param scale 地图缩放级别，范围为1 ~ 28，默认为28
	 * @param infoUrl 在查看位置界面显示的超链接，可点击跳转
	 * @return
	 */
	public static String NativeShowLocation(WebView webView,String latitude,String longitude,String name,String address,String scale,String infoUrl){
		
		return GetJsResult("1", "");
	}
	
	/**
	 * 获取当前所在的地理位置信息
	 * @param webView
	 * @return
	 */
	public static String NativeGetLocation(WebView webView){
		
		return GetJsResult("1", "");
	}
	
	/**
     * 结束当前窗口
     * @param view 浏览器
     * */
    public static void NativeGoBack(WebView view) {
        if (view.getContext() instanceof Activity) {
            ((Activity)view.getContext()).finish();
        }
    }
    
    /**
     * 获取用户系统版本大小
     * @param webView 浏览器
     * @return 安卓SDK版本
     * */
    public static int NativeGetOsSdk(WebView webView) {
        return Build.VERSION.SDK_INT;
    }
    
    public static String NativeGetOsSdkVersion(WebView webView) {
        return Build.VERSION.CODENAME;
    }
    
    /**
     * 系统弹出提示框
     * @param webView 浏览器
     * @param message 提示信息
     * */
    public static void NativeAlert(WebView webView, String message) {
        // 构建一个Builder来显示网页中的alert对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(webView.getContext());
        builder.setTitle("系统消息");
        builder.setMessage(message);
        builder.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.create();
        builder.show();
    }
    
    /**
     * 短暂气泡提醒
     * @param webView 浏览器
     * @param message 提示信息
     * */
    public static void NativeToast(WebView webView, String message) {
        Toast.makeText(webView.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 可选择时间长短的气泡提醒
     * @param webView 浏览器
     * @param message 提示信息
     * @param isShowLong 提醒时间方式
     * */
    public static void NativeToast(WebView webView, String message, int isShowLong) {
        Toast.makeText(webView.getContext(), message, isShowLong).show();
    }
    
    /**
	 * Call Phone
	 * @param phone the phone's number
	 */
	public static void NativeCall(WebView webView,String phone) {
		Intent intent = new Intent(Intent.ACTION_VIEW,
				Uri.parse("tel:" + phone));
		webView.getContext().startActivity(intent);
	}
	
	/**
	 * 刷新webview
	 * @param webView
	 */
	public static void Refresh(WebView webView){
		webView.reload();
	}
	
	/**
	 * 异步回调
	 * @param view
	 * @param ms
	 * @param backMsg
	 * @param jsCallback
	 */
	public static void NativeDelayJsCallBack(WebView view, int ms, final String backMsg, final JsCallback jsCallback) {
        TaskExecutor.scheduleTaskOnUiThread(ms * 1000, new Runnable() {
            @Override
            public void run() {
                try {
                    jsCallback.apply(backMsg);
                } catch (JsCallback.JsCallbackException je) {
                    je.printStackTrace();
                }
            }
        });
    }
	
	/**
	 * 获取设备信息
	 * @param webView
	 * @return
	 */
	public static String NativeDeviceInfo(WebView webView){
		TelephonyManager phoneMgr=(TelephonyManager)webView.getContext().getSystemService(Context.TELEPHONY_SERVICE);
		
		return GetJsResult("1", "devideId:"+phoneMgr.getDeviceId()//设备ID
//				+";sofeVersion:"+phoneMgr.getDeviceSoftwareVersion()//
//				+";number:"+phoneMgr.getLine1Number()//手机号码
//				+";phoneType:"+phoneMgr.getPhoneType()//手机类型
//				+";contryIso:"+phoneMgr.getSimCountryIso()//国家码
//				+";simName:"+phoneMgr.getSimOperatorName()//服务商名称
//				+";simSerialNumber:"+phoneMgr.getSimSerialNumber()//sim序列号
				+";model:"+ Build.MODEL
				+";version:"+ Build.VERSION.RELEASE
				+";sdkVersion:"+ Build.VERSION.SDK_INT
				);
	}
	
	/**
	 * 屏幕宽高
	 * @param webView
	 * @return
	 */
	public static String NativeScreen(WebView webView) {
		return GetJsResult(
				"1",
				"width:" + AGAppControl.GetScreenWidth(webView.getContext())
						+ ";height:"
						+ AGAppControl.GetScreenHeight(webView.getContext()));
	}

	
}
