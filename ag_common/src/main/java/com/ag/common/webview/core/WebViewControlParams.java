package com.ag.common.webview.core;

import android.view.View;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.Serializable;


public class WebViewControlParams implements Serializable {

    private WebView webView;
    private RelativeLayout layout_error;
    private View layout_img_loading;
    private TextView layout_tv_title;
    private String loadUrl;

    private long timeout = 30*1000;
    private String App_Request_Header_Key="agapp";
    private String Error_Url="file:///android_asset/error.htm";
    private String AuthLogin_Url="";
    private String Index_Url="";
    private String Index_Html_Url="file:///android_asset/index_html/app_index_local.html";
    private String str_no_internet="";
    private String str_error_load="";

    /**
     * 加载webview缓存，默认为true，加载
     */
    private boolean loadWebViewCache=true;

    /**
     * 添加请求头，默认为true，添加
     */
    private boolean addRequestHeader=true;

    /**
     * webivew获取定位
     */
    private boolean mGeolocationPermission=false;

    public boolean ismGeolocationPermission() {
        return mGeolocationPermission;
    }

    public void setmGeolocationPermission(boolean mGeolocationPermission) {
        this.mGeolocationPermission = mGeolocationPermission;
    }

    public WebView getWebView() {
        return webView;
    }

    public void setWebView(WebView webView) {
        this.webView = webView;
    }

    public RelativeLayout getLayout_error() {
        return layout_error;
    }

    public void setLayout_error(RelativeLayout layout_error) {
        this.layout_error = layout_error;
    }

    public View getLayout_img_loading() {
        return layout_img_loading;
    }

    public void setLayout_img_loading(View layout_img_loading) {
        this.layout_img_loading = layout_img_loading;
    }

    public TextView getLayout_tv_title() {
        return layout_tv_title;
    }

    public void setLayout_tv_title(TextView layout_tv_title) {
        this.layout_tv_title = layout_tv_title;
    }

    public String getLoadUrl() {
        return loadUrl;
    }

    public void setLoadUrl(String loadUrl) {
        this.loadUrl = loadUrl;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public String getApp_Request_Header_Key() {
        return App_Request_Header_Key;
    }

    public void setApp_Request_Header_Key(String app_Request_Header_Key) {
        App_Request_Header_Key = app_Request_Header_Key;
    }

    public String getError_Url() {
        return Error_Url;
    }

    public void setError_Url(String error_Url) {
        Error_Url = error_Url;
    }

    public String getAuthLogin_Url() {
        return AuthLogin_Url;
    }

    public void setAuthLogin_Url(String authLogin_Url) {
        AuthLogin_Url = authLogin_Url;
    }

    public String getIndex_Url() {
        return Index_Url;
    }

    public void setIndex_Url(String index_Url) {
        Index_Url = index_Url;
    }

    public String getIndex_Html_Url() {
        return Index_Html_Url;
    }

    public void setIndex_Html_Url(String index_Html_Url) {
        Index_Html_Url = index_Html_Url;
    }

    public String getStr_no_internet() {
        return str_no_internet;
    }

    public void setStr_no_internet(String str_no_internet) {
        this.str_no_internet = str_no_internet;
    }

    public String getStr_error_load() {
        return str_error_load;
    }

    public void setStr_error_load(String str_error_load) {
        this.str_error_load = str_error_load;
    }

    public boolean isLoadWebViewCache() {
        return loadWebViewCache;
    }

    public void setLoadWebViewCache(boolean loadWebViewCache) {
        this.loadWebViewCache = loadWebViewCache;
    }

    public boolean isAddRequestHeader() {
        return addRequestHeader;
    }

    public void setAddRequestHeader(boolean addRequestHeader) {
        this.addRequestHeader = addRequestHeader;
    }
}
