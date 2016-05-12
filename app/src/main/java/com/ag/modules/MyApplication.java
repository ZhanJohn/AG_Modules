package com.ag.modules;

import android.app.Application;

/**
 * Created by ZXR on 2016/1/18.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
//        AGPushUMeng.getInstance().initialize(this, new IUMengPushReceiver() {
//            @Override
//            public void onReceive(String result) {
//                Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onBindResult(boolean isSuccess) {
//
//            }
//
//            @Override
//            public void onUnBindResult(boolean isSuccess) {
//
//            }
//
//            @Override
//            public void onRegisterDeviceToken(String deviceToken) {
//                Toast.makeText(getApplicationContext(),deviceToken,Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}
