package com.ag.common.createview;

import java.io.Serializable;

public class CustomerBarResult implements Serializable{

	private String interfaceVersion;
	private String isOpenNewWnd;
	private String url;
	private CustomerBarConfig wndCfg;
	private String isCanPullToRefresh;//0不能下拉刷新， 1可以下拉刷新
	private String specialPageType;//payment：支付界面
	private String isNeed2FreshParentUI;//0返回不刷新，返回要刷新

	/**
	 * 接口兼容考虑
	 * @return
	 */
	public String getInterfaceVersion() {
		return interfaceVersion;
	}
	public void setInterfaceVersion(String interfaceVersion) {
		this.interfaceVersion = interfaceVersion;
	}

	/**
	 * 是否打开新窗口 1表示打开新窗口
	 * @return
	 */
	public String getIsOpenNewWnd() {
		return isOpenNewWnd;
	}
	public void setIsOpenNewWnd(String isOpenNewWnd) {
		this.isOpenNewWnd = isOpenNewWnd;
	}

	/**
	 * 需要加载的url
	 * @return
	 */
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 视图样式配置
	 * @return
	 */
	public CustomerBarConfig getWndCfg() {
		return wndCfg;
	}
	public void setWndCfg(CustomerBarConfig wndCfg) {
		this.wndCfg = wndCfg;
	}

	public String getIsCanPullToRefresh() {
		return isCanPullToRefresh;
	}

	public void setIsCanPullToRefresh(String isCanPullToRefresh) {
		this.isCanPullToRefresh = isCanPullToRefresh;
	}

	public String getSpecialPageType() {
		return specialPageType;
	}

	public String getIsNeed2FreshParentUI() {
		return isNeed2FreshParentUI;
	}

	public void setIsNeed2FreshParentUI(String isNeed2FreshParentUI) {
		this.isNeed2FreshParentUI = isNeed2FreshParentUI;
	}

	public void setSpecialPageType(String specialPageType) {
		this.specialPageType = specialPageType;
	}
}