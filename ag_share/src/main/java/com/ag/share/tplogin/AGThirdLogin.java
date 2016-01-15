package com.ag.share.tplogin;

import android.content.Context;
import android.util.Log;

import java.io.Serializable;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

public class AGThirdLogin {
    private static String TAG="TPLogin";

    private static AGThirdLogin agThirdLogin=new AGThirdLogin();
    public static AGThirdLogin getInstance(){
        return agThirdLogin;
    }

    private AGThirdLogin(){}

    /**
     * third part's login
     * @param context application
     * @param platName platform' name:QQ.Name,SinaWeibo.NAME,Wechat.NAME
     * @param iagtpLogin
     */
    public void tpLogin(Context context,String platName, final IAGTPLogin iagtpLogin) {
        if(iagtpLogin==null)
            return;

        ShareSDK.initSDK(context);

        Platform plat = ShareSDK.getPlatform(platName);
        if(plat==null || !plat.isClientValid()){
            iagtpLogin.onLoginFail("platform is not exist or client is not valid!");
            return;
        }

        if (plat.isAuthValid()) {
            plat.removeAccount(true);
        }

        //使用SSO授权，通过客户单授权
        plat.SSOSetting(false);

        plat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int action, HashMap<String, Object> hashMap) {
                Log.d(TAG,"weixin---onComplete");

                String headUrl=hashMap.get("headimgurl").toString();
                String nickname=hashMap.get("nickname").toString();
                String sex=hashMap.get("sex").toString();
                String unionid=hashMap.get("unionid").toString();

                tpLoginParams loginParams=new tpLoginParams(unionid,nickname,headUrl,sex);
                iagtpLogin.onLoginSuccess(loginParams);
            }

            @Override
            public void onError(Platform platform, int action, Throwable throwable) {
                Log.d(TAG,"weixin---onError");
                throwable.printStackTrace();
                iagtpLogin.onLoginFail(throwable.getMessage());
            }

            @Override
            public void onCancel(Platform platform, int action) {
                Log.d(TAG,"weixin---onCancel");
                iagtpLogin.onLoginFail("onCancel");
            }
        });

        plat.showUser(null);
    }

    public interface IAGTPLogin{

        /**
         * login success
         * @param loginParams return login's params
         */
        void onLoginSuccess(tpLoginParams loginParams);

        /**
         * login fail
         * @param errorMsg return error message
         */
        void onLoginFail(String errorMsg);
    }

    public class tpLoginParams implements Serializable{
        private String unionid;
        private String nickname;
        private String headimgurl;
        private String sex;

        public String getUnionid() {
            return unionid;
        }

        public void setUnionid(String unionid) {
            this.unionid = unionid;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getHeadimgurl() {
            return headimgurl;
        }

        public void setHeadimgurl(String headimgurl) {
            this.headimgurl = headimgurl;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public tpLoginParams(){}

        public tpLoginParams(String unionid,String nickname,String headimgurl,String sex){
            this.unionid=unionid;
            this.nickname=nickname;
            this.headimgurl=headimgurl;
            this.sex=sex;
        }

    }

}
