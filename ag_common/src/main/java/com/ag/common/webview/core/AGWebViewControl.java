package com.ag.common.webview.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ag.common.config.AGSharedPreferences;
import com.ag.common.js.AGChromeClient;
import com.ag.common.js.AndroidJs;
import com.ag.common.other.AGActivity;
import com.ag.common.other.NetworkStateUtils;
import com.ag.common.res.AGResource;
import com.ag.common.webview.BaseWebViewActivity;
import com.ag.common.webview.sqlite.AGCacheInfo;
import com.ag.common.webview.sqlite.CacheDao;
import com.ag.common.webview.sqlite.HeaderDao;
import com.ag.common.webview.sqlite.AGHeaderInfo;
import com.ag.common.webview.utils.WebviewCacheUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.HashMap;
import java.util.Map;

public class AGWebViewControl {

	private static final String TAG="AGWebViewControl";
	private Context context;
	private WebView webView;
	private View layout_img_loading;
	private TextView layout_tv_title;
//	private String url;
	public String reloadUrl;
	private boolean pullRefreshLoad=false;//下拉刷新
	private RelativeLayout layout_error;
	private boolean receiveError=false;//请求错误
	public AGChromeClient chromeClient;//方便处理upload file的回调

	private long timeout = 30*1000;
	private static final String App_Request_Header_Key="app";
	private static final String Error_Url="file:///android_asset/error.htm";
	private static String AuthLogin_Url="";
	private String Index_Url="";
	private String Index_Html_Url="file:///android_asset/index_html/app_index_local.html";
	private String str_no_internet="网络连接不可用，请检查网络后重试";
	private String str_error_load="页面加载失败，请点击刷新重试";

	private WebViewControlParams controlParams;
	private IWebviewRefresh iWebviewRefresh;

	public AGWebViewControl(Context context, WebViewControlParams controlParams, IWebviewRefresh iWebviewRefresh){
		this.controlParams=controlParams;
		this.iWebviewRefresh = iWebviewRefresh;
		this.context=context;
		this.layout_error=controlParams.getLayout_error();
		this.webView=controlParams.getWebView();
		this.layout_img_loading=controlParams.getLayout_img_loading();
		this.layout_tv_title=controlParams.getLayout_tv_title();
		this.Index_Url=controlParams.getIndex_Url();
		this.AuthLogin_Url=controlParams.getAuthLogin_Url();
		if(controlParams.getTimeout()>30*1000){
			this.timeout=controlParams.getTimeout();
		}

		if(context instanceof Activity){
			//if come from scancode,it's not add request header
			boolean isScan=((Activity)context).getIntent().getBooleanExtra(BaseWebViewActivity.Add_Request_Header_Key,true);
			this.controlParams.setAddRequestHeader(isScan);
		}

		if(layout_error!=null){
			layout_error_internet=(RelativeLayout)layout_error.findViewById(AGResource.getIdByName(context,"layout_error_internet"));
			layout_img_error=(ImageView)layout_error.findViewById(AGResource.getIdByName(context,"layout_img_error"));
			layout_tv_error=(TextView)layout_error.findViewById(AGResource.getIdByName(context,"layout_tv_error"));
			layout_btn_refresh=(Button)layout_error.findViewById(AGResource.getIdByName(context,"layout_btn_refresh"));
		}
	}

	/**
	 * 获取WebView请求头
	 * @return
	 */
	private Map<String, String> getWebViewHeader(){
		Map<String, String> map=new HashMap<String, String>();
		if(isAddRequestHeader())
			map.put(App_Request_Header_Key, "2");
		return map;
	}

	public void refreshWebView(boolean isRefresh){
		pullRefreshLoad=isRefresh;
		loadWebViewUrl(reloadUrl);
	}

	public void setWebViewAndLoading(){
		//Android5.0以上系统取不到第三方cookie
		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
			android.webkit.CookieManager cookieManager= android.webkit.CookieManager.getInstance();
			cookieManager.setAcceptCookie(true);
			cookieManager.setAcceptThirdPartyCookies(webView,true);
		}

		webView.setOverScrollMode(View.OVER_SCROLL_NEVER);
		WebSettings settings=webView.getSettings();
		settings.setJavaScriptEnabled(true);
		setGeoLocation();
//		settings.setGeolocationEnabled(controlParams.ismGeolocationPermission());
//		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		if(isAddRequestHeader())
			settings.setUserAgentString(settings.getUserAgentString()+" APP/2");
		Log.d(TAG,"userAgent==" + webView.getSettings().getUserAgentString());

		//初始化缓存配置
		if(isReadCache()) {
			//可读缓存
			WebviewCacheUtil.Instance().initCacheView(webView, context);
		}else{
			//设置不读缓存
			settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		}

		webView.setWebViewClient(new WebViewClient() {

			private String startUrl;
			private boolean isRedirect = false;

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				isRedirect = true;
				System.out.println("myWebView-重定向网址=" + url);

				if(url.indexOf("tel:")>=0){
					AGActivity.CallPhone((Activity)context, url.substring(4));
					return true;
				}
				else if(url.indexOf("tencent://")>=0){
					AGActivity.OpenQQ((Activity)context, AGActivity.getQQNumber(url));
					return true;
				}

				view.loadUrl(url,getWebViewHeader());
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				if(!pullRefreshLoad && !layout_img_loading.isShown())
					layout_img_loading.setVisibility(View.VISIBLE);
				receiveError = false;

				mHandler.postDelayed(runnable,timeout);
//				setWebViewTimeout();

				//过滤掉一些奇葩的跳转
				if(url.startsWith(AuthLogin_Url) || url.contains("about:blank")){
					//SSO链接过滤，不读缓存
					super.onPageStarted(view, url, favicon);
					return;
				}

				if (TextUtils.isEmpty(startUrl))
					startUrl = url;

				if(!url.contains(Error_Url)){
					reloadUrl=url;
				}else{
					receiveError=true;
				}

				Log.d(TAG,"myWebView-onPageStarted=" + url);
				if(!isReadCache()){
					//不读缓存
					super.onPageStarted(view, url, favicon);
					return;
				}

				// setWebViewCache(view);
				// 如果有网络，则用WebSettings.LOAD_DEFAULT
				// 如果无网络，则启用缓存WebSettings.LOAD_CACHE_ELSE_NETWORK
				WebSettings settings = webView.getSettings();
				if (NetworkStateUtils.isNetworkConnected(context)) {
					// clearWebViewCache();
					System.out.println("网络状态:有");
					settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
				} else {
					System.out.println("网络状态:无");
					settings.setCacheMode(WebSettings.LOAD_CACHE_ONLY);
					//错误页面链接时跳过
					if(!url.contains(Error_Url)){
						//读取缓存头，值为no-cache时强调跳转为错误页面

						AGHeaderInfo info= new HeaderDao(context).GetWebViewModel(url);
						if(info!=null && !TextUtils.isEmpty(info.getCachecontrol())
								&& info.getCachecontrol().equalsIgnoreCase("no-cache")){
							System.out.println("no-cache时应该显示错误界面");
							setInternetErrorView();
							return;
						}
					}

				}
				super.onPageStarted(view, WebviewCacheUtil.Instance().getRedirectUrl(context, url), favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				layout_img_loading.setVisibility(View.GONE);
				pullRefreshLoad=false;
				mHandler.removeCallbacks(runnable);//http response headers
//				timerCancel();
				System.out.println("myWebView-onPageFinished=" + url
						+ ";start=" + startUrl + ";redirect=" + isRedirect);

				//首次加载本地资源，并且网络可用，请求线上网站
				if(NetworkStateUtils.isNetworkConnected(context) && AGSharedPreferences.isIndexHtmlEnter(context)){
					AGSharedPreferences.setFirstIndexHtmlEnter(context);
					loadWebViewUrl(Index_Url);
					return;
				}

				if(iWebviewRefresh!=null){
					iWebviewRefresh.refreshSuccess();
				}

//				pullToRefreshWebView.setLastUpdatedLabel("最后刷新:" + DateUtil.formatNowDateTime());
//				pullToRefreshWebView.onPullDownRefreshComplete();

				//过滤特殊的链接
				if(url.startsWith(AuthLogin_Url) || url.contains("about:blank")){
					super.onPageFinished(view, url);
					return;
				}

				//无网络或是请求地址有误时不继续执行下面的操作
				if(receiveError)
					return;

				//隐藏无网络连接显示
//				if(layout_gif.isShown()){
//					layout_gif.setVisibility(View.GONE);
//					layout_error.setVisibility(View.GONE);
//				}

				if(layout_tv_title!=null && TextUtils.isEmpty(layout_tv_title.getText().toString())){
					layout_tv_title.setText(view.getTitle());
				}

				//刷新的可用url
				reloadUrl=url;

				//请求缓存头
				if(isReadCache())
					loadWebViewHeader(context,url);

				//保存请求的url和重定向后的url
				if (isRedirect && !TextUtils.isEmpty(startUrl)
						&& !TextUtils.isEmpty(url) && !startUrl.equals(url)) {
					if(isReadCache()){
						new CacheDao(context).Add(new AGCacheInfo(startUrl, url));
					}

					isRedirect = false;
					startUrl = null;
				} else if (!isRedirect) {
					startUrl = null;
				}
				super.onPageFinished(view, url);
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
										String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
				receiveError=true;
				System.out.println("onReceivedError-errorCode=="+errorCode+";desc=="+description);
				System.out.println("onReceivedError=="+failingUrl);
				setInternetErrorView();
//				view.loadUrl(UrlConfig.Error_Url);
			}

			@Override
			public void onLoadResource(WebView view, String url) {
				super.onLoadResource(view, url);
				System.out.println("onLoadResource-url="+url);
			}

		});

		//开启Js调用原生
		//webview 超时处理
		//
		chromeClient=new AGChromeClient("AndroidJs", AndroidJs.class,controlParams.ismGeolocationPermission());
		chromeClient.setActivity((Activity)context);
		webView.setWebChromeClient(chromeClient);

		//屏蔽掉长按事件 因为webview长按时将会调用系统的复制控件
		webView.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				return true;
			}
		});

		webView.setDownloadListener(new DownloadListener() {

			@Override
			public void onDownloadStart(String url, String userAgent,
										String contentDisposition, String mimetype, long contentLength) {
				if (url != null && url.startsWith("http://"))
					context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
			}
		});

		//首次先加载本地html
		String tmpUrl=controlParams.getLoadUrl();

		if(AGSharedPreferences.isIndexHtmlEnter(context)){
			if(!TextUtils.isEmpty(Index_Html_Url)){
				//首次加载并且本地链接不为空，则先加载本地网页
				webView.loadUrl(Index_Html_Url);
			}else{
				//首次加载并且本地链接为空，则直接读取线上网页
				AGSharedPreferences.setFirstIndexHtmlEnter(context);
			}
		}
		//如果需要读取缓存，则从缓存中读取真实的加载链接，即重定向最后的链接
		if(isReadCache()){
			tmpUrl=WebviewCacheUtil.Instance().loadUrl(context, controlParams.getLoadUrl());
		}
		loadWebViewUrl(tmpUrl);

	}

	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what==1){
				//webview超时处理
				System.out.println("webview is timeout!!!"+webView.getProgress());
				webView.stopLoading();
				receiveError=true;
				setInternetErrorView();
			}
		}
	};

	private Runnable runnable=new Runnable() {
		@Override
		public void run() {
			//webview加载监听，超时且未完成加载时，停止加载，并弹出超时提示
			if (webView.getProgress() < 80) {
				Log.d("testTimeout", "timeout...........");
				Message msg = new Message();
				msg.what = 1;
				mHandler.sendMessage(msg);
			}
		}
	};

	public void loadWebViewUrl(String url){
		webView.loadUrl(url,getWebViewHeader());
	}

	private void setGeoLocation(){
		if(!controlParams.ismGeolocationPermission())
			return;
		WebSettings webSettings = webView.getSettings();

		//启用地理定位
		webSettings.setGeolocationEnabled(true);

		//最重要的方法，一定要设置，这就是出不来的主要原因
		webSettings.setDomStorageEnabled(true);
	}

	/**
	 * 是否读缓存
	 * @return
     */
	private boolean isReadCache(){
		if(controlParams!=null && controlParams.isLoadWebViewCache())
			return true;
		return false;
	}

	/**
	 * 是否添加请求头
	 * @return
     */
	private boolean isAddRequestHeader(){
		if(controlParams!=null && controlParams.isAddRequestHeader())
			return true;
		return false;
	}

	public void setIndex_Html_Url(String index_Html_Url){
		this.Index_Html_Url=index_Html_Url;
	}

	/**
	 * 请求Http Headers并保存
	 * @param url
	 */
	public static void loadWebViewHeader(final Context context,final String url){
		if(!NetworkStateUtils.isNetworkConnected(context))
			return;

//		requestUrl=url;

		//1.如果当前缓存头存在当前url，则跳过
		if(new HeaderDao(context).GetWebViewModel(url)!=null){
			return;
		}
		//2.不存在时异步请求并写入数据库
		AsyncHttpClient client=new AsyncHttpClient();
		client.setTimeout(30 * 1000);

		try {
			client.get(url, new TextHttpResponseHandler() {
				@Override
				public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
					throwable.printStackTrace();
				}

				@Override
				public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
					//获取缓存头信息
					String cacheControl = null;
					for (cz.msebera.android.httpclient.Header obj : headers) {
						System.out.println("key=" + obj.getName() + ";value=" + obj.getValue());
						if (obj.getName().equalsIgnoreCase("cache-control")) {
							cacheControl = obj.getValue();
							break;
						}
					}
					//写入数据库
					new HeaderDao(context).AddWebViewCache(new AGHeaderInfo(url, cacheControl));
				}
			});
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	private RelativeLayout layout_error_internet;
	//	private GifImageView layout_gif;
	private ImageView layout_img_error;
	private TextView layout_tv_error;
	private Button layout_btn_refresh;
	/**
	 * 网络连接断开或页面加载出错的提示
	 */
	public void setInternetErrorView(){
		if(layout_error==null)
			return;

//		layout_gif.setVisibility(View.GONE);
		layout_error.setVisibility(View.VISIBLE);
		layout_error_internet.setVisibility(View.VISIBLE);

		//网络连接断开
		if (!NetworkStateUtils.isNetworkConnected(context)){
			layout_img_error.setImageResource(AGResource.getMipMapByName(context,"wifi"));
			layout_tv_error.setText(str_no_internet);
			layout_btn_refresh.setVisibility(View.GONE);
//			layout_gif.setVisibility(View.GONE);
			return;
		}

		if(!receiveError)
			return;

		//页面加载出错
		layout_img_error.setImageResource(AGResource.getMipMapByName(context,"gantanhao"));
		layout_tv_error.setText(str_error_load);
		layout_btn_refresh.setVisibility(View.VISIBLE);
		layout_btn_refresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				layout_error_internet.setVisibility(View.GONE);
//				layout_gif.setVisibility(View.VISIBLE);
				refreshWebView(true);
			}
		});

	}

}
