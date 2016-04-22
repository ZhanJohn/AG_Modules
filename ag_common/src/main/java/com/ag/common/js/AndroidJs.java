package com.ag.common.js;

import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;

import com.ag.common.createview.CustomerBarResult;
import com.ag.common.js.bean.MapNaviResult;
import com.ag.common.js.bean.NativeJumpInfo;
import com.ag.common.js.bean.PopLevel;
import com.ag.common.js.bean.RegShakeInfo;
import com.ag.common.js.bean.ShareParam;
import com.ag.common.js.bean.WXPayParams;
import com.ag.common.js.interfaces.AGAndroidJsSingleton;
import com.ag.common.other.StringUtils;
import com.ag.common.shake.ShakeHelper;
import com.google.gson.Gson;

public class AndroidJs extends AGNativeJS {

	private static final String TAG=AndroidJs.class.getSimpleName();

	/**
	 * 打开一个原生界面
	 * @param webView
	 * @param result
	 */
	public static void YtNativeOpenUrl(WebView webView,String result){
		Log.d(TAG,"YtNativeOpenUrl=="+result);

		if(TextUtils.isEmpty(result))
			return;

		CustomerBarResult barResult=null;
		try{
			barResult=(new Gson()).fromJson(result, CustomerBarResult.class);
		}catch(Exception e){
			e.printStackTrace();
		}
		if(barResult==null || barResult.getUrl()==null)
			return;

		//判断是否需要打开新窗口
		int newWnd= StringUtils.SafeInt(barResult.getIsOpenNewWnd(), 1);
		if(newWnd==0){
			webView.loadUrl(barResult.getUrl());
			return;
		}

		//打开新窗口
		AGAndroidJsSingleton.getInstance().getIAndroidJs().YtNativeOpenUrl(webView.getContext(),barResult);
	}

	/**
	 * 显示扫描二维码界面，返回扫描到的原生信息
	 * @param webView
	 * @return
	 */
	public static String NativeScan(WebView webView,String jsCallBackMethod){
		System.out.println("jsCallBackMethod="+jsCallBackMethod);
		webView.setTag(jsCallBackMethod);
		AGAndroidJsSingleton.getInstance().getIAndroidJs().NativeScan(webView.getContext());
		return GetJsResult("1", "");
	}

	/**
	 * 原生弹出分享界面，用户可选择分享到QQ、微信等进行分享
	 * @param webView
	 * @param result 分享界面提供的第三方平台，该字段为保留字段，空值代表支持所有第三方平台
	 *  分享界面显示的样式；portrait-竖立；landscape-横躺；
	 *  分享的样式, 该字段为保留字段，空值代表 新闻 样式
	 *  分享的内容
	 * 分享相关联的连接
	 * 分享的图片链接
	 * @return
	 */
	public static String NativeShare(WebView webView,String result){
		if(TextUtils.isEmpty(result)){
			return GetJsResult("0", "");
		}
		ShareParam param= (new Gson()).fromJson(result,ShareParam.class);
		if(param==null){
			return GetJsResult("0", "");
		}
		AGAndroidJsSingleton.getInstance().getIAndroidJs().NativeShare(webView.getContext(),param);
		return GetJsResult("1", "");
	}


	/**
	 * 获取共生登录的token跟usercode，各个平台再通过token,usercode去获取平台用户资料
	 * @param webView
	 * @return
	 */
	public static String NativeGetLoginToken(WebView webView,String callBackMethod){
		System.out.println("WebView-CallBackMethod=="+callBackMethod);
		webView.setTag(callBackMethod);
		AGAndroidJsSingleton.getInstance().getIAndroidJs().NativeGetLoginToken(webView.getContext());
		return GetJsResult("1", "");
	}

	/**
	 * 关掉栈Activity
	 * @param webView
	 * @param level 关掉几个
	 */
	public static void YtNativePop2(WebView webView,String level){
		System.out.println("NativeLevel==" + level);
		PopLevel obj=null;
		try {
			obj = (new Gson()).fromJson(level, PopLevel.class);
		}catch(Exception e){e.printStackTrace();}
		if(obj==null)
			return;
		AGAndroidJsSingleton.getInstance().getIAndroidJs().YtNativePop2(webView.getContext(),obj);
	}

	public static void YtNativeJump2(WebView webView,String uiid){
		if(TextUtils.isEmpty(uiid))
			return;
		NativeJumpInfo info=(new Gson()).fromJson(uiid, NativeJumpInfo.class);
		if(info==null)
			return;
		AGAndroidJsSingleton.getInstance().getIAndroidJs().YtNativeJump2(webView.getContext(),info);
	}

	/**
	 * 百度导航
	 * @param webView
	 * @param pos
	 */
	public static void NativeMapNavi(WebView webView,String pos){
		MapNaviResult result=(new Gson()).fromJson(pos,MapNaviResult.class);
		AGAndroidJsSingleton.getInstance().getIAndroidJs().NativeMapNavi(webView.getContext(),result);
//		Activity activity=(Activity)webView.getContext();
		//116.736709,23.373952
//		LatLng startLng=new LatLng(116.736709,23.373952);
//		LatLng endLng=new LatLng(116.754064,23.368081);//116.754064,23.368081
//		(new BaiduMapUtil()).launchNavigator(activity,startLng,endLng);
	}

	/**
	 * 打开键盘
	 * @param webView
	 */
	public static void NativeOpenKeyboard(WebView webView,String params){
//		KeyboardUtils.showOrHidentKeyboard(webView.getContext());
//		InputMethodManager imm = (InputMethodManager) webView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/**
	 * 上传文件，4.4.2版本作特殊处理
	 * @param webView
	 * @param imgUrl
	 * @param callbackMethod
	 */
	public static void NativeUploadFile(WebView webView,String imgUrl,String callbackMethod){
		if(TextUtils.isEmpty(imgUrl) || TextUtils.isEmpty(callbackMethod))
			return;
		AGAndroidJsSingleton.getInstance().getIAndroidJs().NativeUploadFile(webView,imgUrl,callbackMethod);
	}

	/**
	 * 注册摇一摇事件
	 */
	public static void NativeRegShake(final WebView webView,String respFun){
		if(TextUtils.isEmpty(respFun))
			return;
		final RegShakeInfo info=(new Gson()).fromJson(respFun,RegShakeInfo.class);
		//注册摇一摇监听事件
		ShakeHelper.getInstance(webView.getContext()).startShake(new ShakeHelper.IShakeListener() {
			@Override
			public void onShakeSuccess() {
				webView.loadUrl(String.format("javascript:%s()", info.getRespFun()));
			}
		});
	}

	/**
	 * Js调用App临时保存键值对
	 * @param webView
	 * @param key
	 * @param value
     */
	public static void NativeAddKeyValue(WebView webView,String key,String value){
		AGAndroidJsSingleton.getInstance().addJsValue(key,value);
	}

	/**
	 * Js调用App获取临时键值
	 * @param webView
	 * @param key
	 * @param callbackMethod js回调方法
     */
	public static void NativeGetKeyValue(WebView webView,String key,String callbackMethod){
		String value=AGAndroidJsSingleton.getInstance().getJsValue(key);
		String ret="0";
		if(TextUtils.isEmpty(value)) {
			ret="-1";
		}
		webView.loadUrl(String.format("javascript:%s('%s')", callbackMethod,GetJsResult(ret,value) ));
	}

	/**
	 * Js调用App删除某个临时键值
	 * @param webView
	 * @param key
     */
	public static void NativeRemoveKeyValue(WebView webView,String key){
		AGAndroidJsSingleton.getInstance().removeJsValue(key);
	}

	/**
	 * Js调用App删除所有临时键值
	 * @param webView
     */
	public static void NativeRemoveAllKeyValue(WebView webView){
		AGAndroidJsSingleton.getInstance().clearJsValue();
	}

	/**
	 * Js调用App获取当前定位
	 * @param webView
	 * @param callbackMethod
     */
	public static void NativeMapLocation(WebView webView,String callbackMethod){
		AGAndroidJsSingleton.getInstance().getIAndroidJs().NativeGetLocation(webView,callbackMethod);
//		Location location=getLocation(webView.getContext());
//		webView.loadUrl(String.format("javascript:%s(%s,%s)", callbackMethod, location.getLongitude(),location.getLatitude()));
//		webView.loadUrl(String.format("javascript:%s(%s,%s)", callbackMethod, "116.72865029","23.38390845"));
	}

	/**
	 * JS调用App进行微信支付
	 * @param webView
	 * @param jsonParams JSON参数
	 * @param jsCallBackMethod JS回调方法
     */
	public static void NativeWXPay(WebView webView,String jsonParams,String jsCallBackMethod){
		if(TextUtils.isEmpty(jsonParams) || TextUtils.isEmpty(jsCallBackMethod))
			return;
		//
		WXPayParams payParams=null;
		payParams=(new Gson()).fromJson(jsonParams,WXPayParams.class);
		if(payParams==null)
			return;
		payParams.setJsCallBackMethod(jsCallBackMethod);
		AGAndroidJsSingleton.getInstance().getIAndroidJs().NativeWXPay(webView,payParams);
	}



}
