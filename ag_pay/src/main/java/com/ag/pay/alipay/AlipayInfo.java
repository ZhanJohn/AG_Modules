package com.ag.pay.alipay;

import android.content.Context;

public class AlipayInfo {

	private String account;
	private String partner;
	private String publickey;
	private String privatekey;

	private Context context;
	private String payCode;
	private String showTitle;
	private String body;
	private double money;

	private String notifyUrl;

	/**
	 * 服务器异步通知页面路径
	 * @return
	 */
	public String getNotifyUrl() {
		return notifyUrl;
	}
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	public Context getContext() {
		return context;
	}
	public void setContext(Context context) {
		this.context = context;
	}

	/**
	 * 支付记录Code
	 * @return
	 */
	public String getPayCode() {
		return payCode;
	}
	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}

	/**
	 * 显示标题
	 * @return
	 */
	public String getShowTitle() {
		return showTitle;
	}
	public void setShowTitle(String showTitle) {
		this.showTitle = showTitle;
	}

	/**
	 * 订单金额
	 * @return
	 */
	public double getMoney() {
		return money;
	}
	public void setMoney(double money) {
		this.money = money;
	}

	/**
	 * 商家支付宝key
	 * @return
	 */
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}

	/**
	 * 商家帐号
	 * @return
	 */
	public String getPartner() {
		return partner;
	}
	public void setPartner(String partner) {
		this.partner = partner;
	}

	/**
	 * 支付宝公钥
	 * @return
	 */
	public String getPublickey() {
		return publickey;
	}
	public void setPublickey(String publickey) {
		this.publickey = publickey;
	}

	/**
	 * 支付宝私钥
	 * @return
	 */
	public String getPrivatekey() {
		return privatekey;
	}
	public void setPrivatekey(String privatekey) {
		this.privatekey = privatekey;
	}

	/**
	 * 商品详情
	 * @return
	 */
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}


}
