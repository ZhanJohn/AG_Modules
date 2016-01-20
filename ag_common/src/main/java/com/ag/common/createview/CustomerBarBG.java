package com.ag.common.createview;

import java.io.Serializable;

/**
 * 背景色
 *
 */
public class CustomerBarBG implements Serializable{

//	"normal": "",              //正常状态背景图片
//    "highLight": ""           //高亮状态背景图片

	private String normal;
	private String highLight;

	public CustomerBarBG(){

	}

	public CustomerBarBG(String normal,String highLight){
		this.normal=normal;
		this.highLight=highLight;
	}

	/**
	 * 正常状态背景色
	 * @return
	 */
	public String getNormal() {
		return normal;
	}
	public void setNormal(String normal) {
		this.normal = normal;
	}

	/**
	 * 高亮状态背景色
	 * @return
	 */
	public String getHighLight() {
		return highLight;
	}
	public void setHighLight(String highLight) {
		this.highLight = highLight;
	}


}
