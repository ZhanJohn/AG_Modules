package com.ag.common.createview;

import java.io.Serializable;

/**
 * 控件位置信息
 *
 */
public class CustomerBarRect implements Serializable{

//	 "marginTop": "",          //上边距(相对于Navigationbar,marginTop和marginBottom必须有一个为0)
//     "marginLeft": "",          //左边距(相对于Navigationbar,marginLeft和marginRight必须有一个为0)
//     "marginRight": "",       //右边距(相对于Navigationbar,marginLeft和marginRight必须有一个为0)
//     "height": "",                 //高度
//     "weight": ""                 //宽度

	private String isCenter;
	private String marginTop;
	private String marginLeft;
	private String marginRight;
	private String height;
	private String weight;

	public CustomerBarRect(){

	}

	public CustomerBarRect(String isCenter,String marginTop,String marginLeft,String marginRight,String height,String weight){
		this.isCenter=isCenter;
		this.marginLeft=marginLeft;
		this.marginRight=marginRight;
		this.marginTop=marginTop;
		this.height=height;
		this.weight=weight;
	}

	/**
	 * 控件是否居中 1：居中  0：不居中
	 * @return
	 */
	public String getIsCenter() {
		return isCenter;
	}
	public void setIsCenter(String isCenter) {
		this.isCenter = isCenter;
	}

	/**
	 * 上边距
	 * @return
	 */
	public String getMarginTop() {
		return marginTop;
	}
	public void setMarginTop(String marginTop) {
		this.marginTop = marginTop;
	}

	/**
	 * 左边距
	 * @return
	 */
	public String getMarginLeft() {
		return marginLeft;
	}
	public void setMarginLeft(String marginLeft) {
		this.marginLeft = marginLeft;
	}

	/**
	 * 右边距
	 * @return
	 */
	public String getMarginRight() {
		return marginRight;
	}
	public void setMarginRight(String marginRight) {
		this.marginRight = marginRight;
	}

	/**
	 * 高度
	 * @return
	 */
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}

	/**
	 * 宽度
	 * @return
	 */
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}

}
