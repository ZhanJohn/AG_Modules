package com.ag.share.customer;

import android.content.Context;
import android.content.pm.ActivityInfo;

import com.ag.share.OnekeyShareTheme;

import java.io.Serializable;
import java.util.List;

/**
 * 分享内容实体类
 * @author JohnZhan
 *
 */
public class ShareInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Context context;
	private String platform;
	private boolean silent;
	private int iconId;
	private String content;
	private String imgPath;
	private String imgUrl;
	private String url;
	private String code;
	private String shareTitle;
	private String appName;
	private String webUrl;
	private OnekeyShareTheme shareTheme=OnekeyShareTheme.CLASSIC;
	private int orientation=ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
	private List<CustomerPlatform> platforms;
	private boolean hasButton;
	private boolean hasTopTitle;
	private boolean isWechatTitleToContent=true;
	
	public boolean isHasTopTitle() {
		return hasTopTitle;
	}
	public void setHasTopTitle(boolean hasTopTitle) {
		this.hasTopTitle = hasTopTitle;
	}
	public boolean isHasButton() {
		return hasButton;
	}
	public void setHasButton(boolean hasButton) {
		this.hasButton = hasButton;
	}
	public int getOrientation() {
		return orientation;
	}
	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}
	public List<CustomerPlatform> getPlatforms() {
		return platforms;
	}
	public void setPlatforms(List<CustomerPlatform> platforms) {
		this.platforms = platforms;
	}
	public OnekeyShareTheme getShareTheme() {
		return shareTheme;
	}
	public void setShareTheme(OnekeyShareTheme shareTheme) {
		this.shareTheme = shareTheme;
	}
	public String getWebUrl() {
		return webUrl;
	}
	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}
	public String getShareTitle() {
		return shareTitle;
	}
	public void setShareTitle(String shareTitle) {
		this.shareTitle = shareTitle;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public Context getContext() {
		return context;
	}
	public void setContext(Context context) {
		this.context = context;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public boolean isSilent() {
		return silent;
	}
	public void setSilent(boolean silent) {
		this.silent = silent;
	}
	public int getIconId() {
		return iconId;
	}
	public void setIconId(int iconId) {
		this.iconId = iconId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public boolean isWechatTitleToContent() {
		return isWechatTitleToContent;
	}
	public void setWechatTitleToContent(boolean isWechatTitleToContent) {
		this.isWechatTitleToContent = isWechatTitleToContent;
	}	
	
}
