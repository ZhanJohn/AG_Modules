package com.ag.common.webview.sqlite;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName="agcacheinfo")
public class AGCacheInfo implements Serializable {
	
	@DatabaseField(generatedId=true)
	private int id;
	
	@DatabaseField(columnName="starturl")
	private String starturl;
	
	@DatabaseField(columnName="redirecturl")
	private String redirecturl;

	public String getStartUrl() {
		return starturl;
	}

	public void setStartUrl(String startUrl) {
		this.starturl = startUrl;
	}

	public String getRedirectUrl() {
		return redirecturl;
	}
	
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirecturl = redirectUrl;
	}
	
	public AGCacheInfo(String startUrl, String redirectUrl){
		this.starturl=startUrl;
		this.redirecturl=redirectUrl;
	}

	public AGCacheInfo(){

	}
	
}
