package com.ag.umeng_push;

/**
 * Created by ZXR on 2016/1/15.
 */
public interface IUMengPushReceiver {

    /**
     * push receive data
     * @param result
     */
    void onReceive(String result);

    /**
     * push onBind
     */
    void onBindResult(boolean isSuccess);

    /**
     * push onUnBind
     */
    void onUnBindResult(boolean isSuccess);

}
