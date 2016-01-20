package com.ag.common.createview;

import java.io.Serializable;

/**
 * 响应函数
 *
 */
public class CustomerBarAction implements Serializable{

	private String funName;
	private String param;
	
	/**
	 * 函数名称
	 * @return
	 */
	public String getFunName() {
		return funName;
	}
	public void setFunName(String funName) {
		this.funName = funName;
	}
	
	/**
	 * 参数
	 * @return
	 */
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	
}
