package com.ag.common.createview;

import java.io.Serializable;

/**
 * 自定义控件
 *
 */
public class CustomerBarController implements Serializable{

	private String type;
	private String text;
	private String textColor;
	private String textFont;
	private CustomerBarRect rect;
	private CustomerBarBG bgImg;
	private CustomerBarAction action;


	/**
	 * 控件类型：button，按钮；label，文本；searchbar，搜索栏
	 * @return
	 */
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 控件显示的文本，如button的标题，label的内容，searchbar的默认提示
	 * @return
	 */
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * 字体颜色
	 * @return
	 */
	public String getTextColor() {
		return textColor;
	}
	public void setTextColor(String textColor) {
		this.textColor = textColor;
	}

	/**
	 * 字体大小
	 * @return
	 */
	public String getTextFont() {
		return textFont;
	}
	public void setTextFont(String textFont) {
		this.textFont = textFont;
	}

	/**
	 * 控件位置信息
	 * @return
	 */
	public CustomerBarRect getRect() {
		return rect;
	}
	public void setRect(CustomerBarRect rect) {
		this.rect = rect;
	}

	/**
	 * 控件背景色
	 * @return
	 */
	public CustomerBarBG getBgImg() {
		return bgImg;
	}
	public void setBgImg(CustomerBarBG bgImg) {
		this.bgImg = bgImg;
	}

	/**
	 * 响应函数
	 * @return
	 */
	public CustomerBarAction getAction() {
		return action;
	}
	public void setAction(CustomerBarAction action) {
		this.action = action;
	}


}
