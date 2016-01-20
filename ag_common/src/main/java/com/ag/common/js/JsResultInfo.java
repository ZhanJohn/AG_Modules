package com.ag.common.js;

import java.io.Serializable;

/**
 * 返回给Js的结果
 */
public class JsResultInfo implements Serializable{

	private String ret;
	private String msg;
	public String getRet() {
		return ret;
	}
	public void setRet(String ret) {
		this.ret = ret;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
