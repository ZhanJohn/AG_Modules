package com.ag.common.webview;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.webkit.WebView;

import com.ag.common.webview.bean.WebUser;
import com.ag.common.webview.core.AGWebViewControl;
import com.ag.common.webview.utils.AGCookies;


public abstract class BaseWebViewFragment extends Fragment {

    protected enum ViewTypeEnum{

        /**
         * 购物车
         */
        Cart,

        /**
         *用户中心
         */
        UserCenter,

        /**
         * 其它
         */
        Other

    }

    protected WebUser currentUser;
    private WebView webView;
    private AGWebViewControl viewControl;

    /**
     * view resume 时需要重新刷新
     */
    private ViewTypeEnum typeEnum;
    private boolean isPause=false;
    private String userCenterUrl="";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        currentUser= AGCookies.getUserCookie();
    }

    protected void refreshWebView(){
        if(viewControl!=null)
            viewControl.refreshWebView(true);
    }

    public void refreshUrl(String mUrl){
        if (webView!=null &&
                !TextUtils.isEmpty(webView.getUrl()) &&
                !webView.getUrl().equals(mUrl) &&
                viewControl!=null) {
            viewControl.loadWebViewUrl(mUrl);
        }
    }

    public void refreshWebView(String mUrl){
        if(viewControl!=null){
            viewControl.loadWebViewUrl(mUrl);
        }
    }

    protected void setWebViewParams(WebView webView,AGWebViewControl webViewControl,ViewTypeEnum typeEnum,String userCenterUrl){
        this.typeEnum=typeEnum;
        this.userCenterUrl=userCenterUrl;
        this.webView=webView;
        this.viewControl=webViewControl;
        if(viewControl!=null){
            viewControl.setWebViewAndLoading();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        isPause=true;
        if(webView==null || viewControl==null)
            return;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB)
            webView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(webView==null || viewControl==null)
            return;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB)
            webView.onResume();
        //检测登录状态是否有改变，有就刷新
        WebUser mUser= AGCookies.getUserCookie();
        if ((currentUser == null && mUser != null) || (currentUser != null && mUser == null)
                || (currentUser != null && mUser != null && !currentUser.getToken().equals(mUser.getToken()))) {
            currentUser=mUser;
            if(typeEnum == ViewTypeEnum.UserCenter){
                viewControl.loadWebViewUrl(userCenterUrl);
                return;
            }
            viewControl.refreshWebView(false);
            return;
        }
        //刷新购物车
        if(isPause && typeEnum == ViewTypeEnum.Cart){
            viewControl.refreshWebView(false);
            return;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(webView==null)
            return;
        webView.destroy();
        webView=null;
    }

    public void setNetworkView(){
        if(webView==null || viewControl==null)
            return;
        viewControl.setInternetErrorView();
    }


}
