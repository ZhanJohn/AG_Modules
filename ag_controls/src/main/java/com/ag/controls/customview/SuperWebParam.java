package com.ag.controls.customview;

import java.io.Serializable;

/**
 * SuperWebView
 */
public class SuperWebParam implements Serializable{

    private String userAgent;
    private String rootUrl;

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getRootUrl() {
        return rootUrl;
    }

    public void setRootUrl(String rootUrl) {
        this.rootUrl = rootUrl;
    }

    public SuperWebParam() {

    }

    public SuperWebParam(String userAgent, String rootUrl) {
        this.userAgent = userAgent;
        this.rootUrl = rootUrl;
    }

}
