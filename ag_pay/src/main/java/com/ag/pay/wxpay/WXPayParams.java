package com.ag.pay.wxpay;

import java.io.Serializable;

/**
 * Created by ZXR on 2016/1/13.
 */
public class WXPayParams implements Serializable {

    /**
     *微信分配的公众账号ID
     */
    private String appId;

    /**
     *微信支付分配的商户号
     */
    private String partnerId;

    /**
     *微信返回的支付交易会话ID
     */
    private String prepayId;

    /**
     *微信返回的随机字符串
     */
    private String nonceStr;

    /**
     *时间戳
     */
    private String timeStamp;

    /**
     *扩展字段,暂填写固定值Sign=WXPay
     */
    private String packageValue="Sign=WXPay";

    /**
     *微信返回的签名
     */
    private String sign;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getPackageValue() {
        return packageValue;
    }

    public void setPackageValue(String packageValue) {
        this.packageValue = packageValue;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

}
