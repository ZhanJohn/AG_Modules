package com.ag.controls.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ZoomButtonsController;

import com.ag.controls.common.util.WebviewUtils;

import java.lang.reflect.Field;

public class SuperWebView extends WebView {

    private Context context;
    private SuperWebParam webParam;
    public boolean isZoomImg = true;

    public SuperWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        this.setWebViewClient(webViewClient);
    }

    public SuperWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.setWebViewClient(webViewClient);
    }

    public SuperWebView(Context context) {
        super(context);
        this.context = context;
        this.setWebViewClient(webViewClient);
    }

    @SuppressLint("NewApi")
    public void setSupportZoom(boolean zoom) {
        WebSettings setting = getSettings();
        setting.setSupportZoom(true);
        setting.setBuiltInZoomControls(true);
        setting.setUseWideViewPort(true);
        setting.setLoadWithOverviewMode(true);

        //若要考虑兼容3.0以下版本则：

        if (android.os.Build.VERSION.SDK_INT >= 11) {
            this.getSettings().setDisplayZoomControls(false);
        } else {
            this.setZoomControlGone(this);
        }

    }

    //Android 3.0(11) 以下使用以下方法:
    //利用java的反射机制
    public void setZoomControlGone(View view) {
        Class classType;
        Field field;
        try {
            classType = WebView.class;
            field = classType.getDeclaredField("mZoomButtonsController");
            field.setAccessible(true);
            ZoomButtonsController mZoomButtonsController = new ZoomButtonsController(
                    view);
            mZoomButtonsController.getZoomControls().setVisibility(View.GONE);
            try {
                field.set(view, mZoomButtonsController);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    public void InitSetting(SuperWebParam param) {
        if (param == null)
            return;

        this.webParam = param;
        WebViewSetting();
        setFocusable(false);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void WebViewSetting() {
        WebSettings setting = getSettings();
        setting.setJavaScriptEnabled(true);
        setting.setUserAgentString(setting.getUserAgentString() + " " + webParam.getUserAgent());

//        setting.setSupportZoom(true);
//        setting.setBuiltInZoomControls(true);
//        if (Build.VERSION.SDK_INT >= 11) {
//            setting.setDisplayZoomControls(false);
//        }
//        setting.setUseWideViewPort(true);
//        setting.setLoadWithOverviewMode(true);

        setting.setDomStorageEnabled(true);
        setting.setDatabaseEnabled(true);
    }

    private WebViewClient webViewClient = new WebViewClient() {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //loadUrl(url);
            return true;
        }
    };

    public String replaceTag(String tmpPath, String content) {

        if (TextUtils.isEmpty(content)) {
            return "";
        }

        return WebviewUtils.ReplaceHtml(tmpPath, content);
    }

    public String replaceTag(String content) {

        if (TextUtils.isEmpty(content)) {
            return "";
        }

        return WebviewUtils.ReplaceHtml(webParam.getRootUrl(), content);
    }
}