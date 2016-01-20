package com.ag.common.js.bean;

import java.io.Serializable;


public class WXPayParams implements Serializable {

    private String appid;
    private String partnerid;
    private String prepayid;
    private String Package;
    private String noncestr;
    private String timestamp;
    private String sign;
    private String jsCallBackMethod;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getPackage() {
        return Package;
    }

    public void setPackage(String aPackage) {
        Package = aPackage;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getJsCallBackMethod() {
        return jsCallBackMethod;
    }

    public void setJsCallBackMethod(String jsCallBackMethod) {
        this.jsCallBackMethod = jsCallBackMethod;
    }
}
