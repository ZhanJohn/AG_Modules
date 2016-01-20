package com.ag.common.createview;

import java.io.Serializable;
import java.util.List;

public class CustomerBarConfig implements Serializable{

	private String navType;
	private List<CustomerBarController> controller;

	/**
	 * 0:没有Navigation  1：正常状态  2：淡入效果
	 * @return
	 */
	public String getNavType() {
		return navType;
	}
	public void setNavType(String navType) {
		this.navType = navType;
	}

	/**
	 * 子控件列表
	 * @return
	 */
	public List<CustomerBarController> getController() {
		return controller;
	}
	public void setController(List<CustomerBarController> controller) {
		this.controller = controller;
	}

}