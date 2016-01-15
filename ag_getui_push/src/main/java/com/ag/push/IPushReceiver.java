package com.ag.push;

/**
 * Getui push's result
 */
public interface IPushReceiver {

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
