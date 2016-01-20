package com.ag.common.webview.sqlite;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName="agheaderinfo")
public class AGHeaderInfo implements Serializable {
	
	@DatabaseField(generatedId=true)
	private int id;
	
	@DatabaseField(columnName="requesturl")
	private String requesturl;
	
	@DatabaseField(columnName="cachecontrol")
	private String cachecontrol;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRequesturl() {
		return requesturl;
	}

	public void setRequesturl(String requesturl) {
		this.requesturl = requesturl;
	}

	public String getCachecontrol() {
		return cachecontrol;
	}

	public void setCachecontrol(String cachecontrol) {
		this.cachecontrol = cachecontrol;
	}

	public AGHeaderInfo(){
		
	}
	
	public AGHeaderInfo(String requesturl, String cachecontrol){
		this.requesturl=requesturl;
		this.cachecontrol=cachecontrol;
	}

}
