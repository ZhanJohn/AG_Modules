package com.ag.common.dialog;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 自定义dialog数据载体
 * 
 */
public class DialogEntity implements Parcelable {

	private String value;
	private String id;
	private boolean isChecked;// 是否为选中状态
	private String productid;
	private double dispatchPrice;

	private String usetype;
	private String frequency;
	private String money;
	private String rebate;

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

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public String getProductid() {
		return productid;
	}

	public void setProductid(String productid) {
		this.productid = productid;
	}

	public double getDispatchPrice() {
		return dispatchPrice;
	}

	public void setDispatchPrice(double dispatchPrice) {
		this.dispatchPrice = dispatchPrice;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getRebate() {
		return rebate;
	}

	public void setRebate(String rebate) {
		this.rebate = rebate;
	}

	public String getUsetype() {
		return usetype;
	}

	public void setUsetype(String usetype) {
		this.usetype = usetype;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(usetype);
		dest.writeString(frequency);
		dest.writeString(money);
		dest.writeString(rebate);
	}

	public static final Creator<DialogEntity> CREATOR = new Creator<DialogEntity>() {
		public DialogEntity createFromParcel(Parcel in) {
			return new DialogEntity(in);
		}

		public DialogEntity[] newArray(int size) {
			return new DialogEntity[size];
		}
	};
	

	public DialogEntity() {}
	
	private DialogEntity(Parcel dest) 
    {
		id=dest.readString();
		usetype=dest.readString();
		frequency=dest.readString();
		money=dest.readString();
		rebate=dest.readString();
    }
}
