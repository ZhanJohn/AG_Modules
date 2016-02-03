package com.ag.common.dialog;

import java.io.Serializable;

/**
 * 自定义dialog数据载体
 * 
 */
public class DialogEntity implements Serializable {

	private String value;
	private String id;
	private boolean isChecked;// 是否为选中状态

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean checked) {
		isChecked = checked;
	}
}
