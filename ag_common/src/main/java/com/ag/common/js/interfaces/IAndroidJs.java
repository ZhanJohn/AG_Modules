package com.ag.common.js.interfaces;

import android.content.Context;
import android.webkit.WebView;

import com.ag.common.createview.CustomerBarResult;
import com.ag.common.js.bean.MapNaviResult;
import com.ag.common.js.bean.NativeJumpInfo;
import com.ag.common.js.bean.PopLevel;
import com.ag.common.js.bean.ShareParam;
import com.ag.common.js.bean.WXPayParams;


public interface IAndroidJs {

    /**
     * 打开一个原生界面
     * @param context
     * @param barResult
     */
    void YtNativeOpenUrl(Context context, CustomerBarResult barResult);

    /**
     * 显示扫描二维码界面，返回扫描到的原生信息
     * @param context
     */
    void NativeScan(Context context);

    /**
     * 调用原生分享
     * @param context
     * @param shareParam
     */
    void NativeShare(Context context, ShareParam shareParam);

    /**
     * 共生登录
     * @param context
     */
    void NativeGetLoginToken(Context context);

    /**
     * 关掉栈Activity
     * @param context
     * @param popLevel
     */
    void YtNativePop2(Context context, PopLevel popLevel);

    /**
     * 跳转到哪个Tab页面
     * @param context
     * @param jumpInfo
     */
    void YtNativeJump2(Context context, NativeJumpInfo jumpInfo);

    /**
     * 调用百度地图导航
     * @param context
     * @param naviResult
     */
    void NativeMapNavi(Context context, MapNaviResult naviResult);

    /**
     * webview上传文件
     * 4.4.2版本作特殊处理
     * @param webView
     * @param imgUrl
     * @param callbackMethod
     */
    void NativeUploadFile(WebView webView, String imgUrl, String callbackMethod);

    /**
     * webview调用app获取当前定位
     * @param webView
     * @param callbackMethod
     */
    void NativeGetLocation(WebView webView, String callbackMethod);

    /**
     * WebView调用App进行微信支付
     * @param webView
     * @param wxPayParams  微信支付参数
     */
    void NativeWXPay(WebView webView, WXPayParams wxPayParams);

}
