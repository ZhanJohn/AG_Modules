package com.ag.common.webview.utils;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;

import com.ag.common.webview.bean.WebUser;

import org.json.JSONObject;


public class AGCookies {

    public static String User_Login_Url="http://www.woshare.com/appssoinfo.aspx";

    public static void setUserCookie(Context context){
        CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(context);
        CookieManager cookieManager= CookieManager.getInstance();
        if(!cookieManager.hasCookies()) {
            return;
        }
        String cookie=cookieManager.getCookie(User_Login_Url);
        if(TextUtils.isEmpty(cookie))
            return;
        cookieManager.setCookie(User_Login_Url,cookie);
        cookieSyncMngr.sync();
    }

    /**
     * 获取用户Cookie
     */
    public static WebUser getUserCookie(){
        CookieManager cookieManager= CookieManager.getInstance();
        cookieManager.removeExpiredCookie();
        if(!cookieManager.hasCookies()) {
            return null;
        }
        String cookie=cookieManager.getCookie(User_Login_Url);
        if(TextUtils.isEmpty(cookie)){
            return null;
        }

        int start=cookie.lastIndexOf("{");
        int end=cookie.lastIndexOf("}");
        String value="";
        if(start!=-1 && end!=-1){
            value=cookie.substring(start,end+1);
        }else{
            return null;
        }

        Log.d("user-cookie","cookie="+cookie);
        Log.d("user-cookie","cookieObj=" + value);

        try {
            JSONObject jsonObject = new JSONObject(value);
            WebUser user=new WebUser();
            user.setSessionid(jsonObject.getString("sessionid"));
            user.setToken(jsonObject.getString("token"));
            user.setUsername(jsonObject.getString("username"));
            return user;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void clearCookies(Context context) {
        // CookieSyncManager cookieSyncMngr =
        // CookieSyncManager.createInstance(this);
        CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(context);
        cookieSyncManager.startSync();
        CookieManager cookieManager = CookieManager.getInstance();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            cookieManager.removeAllCookies(new ValueCallback<Boolean>() {
                @Override
                public void onReceiveValue(Boolean value) {
                    Log.d("CookieManager", "onReceiveValue " + value);
                }
            });
        }else{
            cookieManager.removeSessionCookie();
            cookieManager.removeAllCookie();
        }
        cookieSyncManager.sync();
    }

}
