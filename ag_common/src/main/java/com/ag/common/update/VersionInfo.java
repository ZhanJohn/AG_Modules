package com.ag.common.update;

/**
 * APP版本说明信息
 * @author JohnZhan
 *
 */
public class VersionInfo implements java.io.Serializable{
	
	private String version;
	private String type;
	private String remark;
	private String url;
	private String compulsory;
	private String versionname;
	private String htmlremark;
	
	
	public String getHtmlremark() {
		return htmlremark;
	}
	public void setHtmlremark(String htmlremark) {
		this.htmlremark = htmlremark;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getCompulsory() {
		return compulsory;
	}
	public void setCompulsory(String compulsory) {
		this.compulsory = compulsory;
	}
	public String getVersionname() {
		return versionname;
	}
	public void setVersionname(String versionname) {
		this.versionname = versionname;
	}
}
