package com.ag.share.customer;

import android.view.View.OnClickListener;

/**
 * 自定义平台
 *
 */
public class CustomerPlatform {
	private String customerLogo;
	private String customerName;
	private OnClickListener customerListener;
	public String getCustomerLogo() {
		return customerLogo;
	}
	public void setCustomerLogo(String customerLogo) {
		this.customerLogo = customerLogo;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public OnClickListener getCustomerListener() {
		return customerListener;
	}
	public void setCustomerListener(OnClickListener customerListener) {
		this.customerListener = customerListener;
	}
	
}
