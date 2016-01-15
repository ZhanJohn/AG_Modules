package com.ag.share;

import android.text.TextUtils;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.sina.weibo.SinaWeibo;

/**
 * 此类在Onekeyshare中并无用途，只是在Socialization中考虑到耦合度，需要通过反射的方式操作Onekeyshare，
 *而原先的{@link ShareContentCustomizeCallback}无法完成此需求，故创建本类，以供外部设置操作回调。
 *
 * @author Brook
 */
public class ReflectableShareContentCustomizeCallback implements ShareContentCustomizeCallback {

	private boolean isWechatTitleToContent=true;

	public ReflectableShareContentCustomizeCallback(){

	}

	public ReflectableShareContentCustomizeCallback(boolean isWechatTitleToContent){
		this.isWechatTitleToContent=isWechatTitleToContent;
	}

	public void onShare(Platform platform, ShareParams paramsToShare) {

		System.out.println("ShareContentCustomizeDemo==="+platform.getName());
//		Toast.makeText(platform.getContext(), platform.getName()+";"+isWechatTitleToContent, Toast.LENGTH_LONG).show();

		// 如果是新浪微博加上地址
		if (platform.getName().equals(SinaWeibo.NAME) || platform.getName().equals("TencentWeibo")) {
			if(isWechatTitleToContent && !TextUtils.isEmpty(paramsToShare.getText().trim())){

				paramsToShare.setText(paramsToShare.getText() + " " + paramsToShare.getUrl());
			}else if(!TextUtils.isEmpty(paramsToShare.getTitle().trim())){
				//标题加链接
				paramsToShare.setText(paramsToShare.getTitle() + " " + paramsToShare.getUrl());
			}else{
				paramsToShare.setText(" "+ paramsToShare.getUrl());
			}

		}

		// 微信分享链接只有标题，将标题改为内容
		if (platform.getName().equals("WechatMoments") && isWechatTitleToContent) {
			paramsToShare.setTitle(paramsToShare.getText());
		}

		// 有本地就拿本地（即，本地与网络同时填的话，过滤掉网络地址）
		if (!TextUtils.isEmpty(paramsToShare.getImagePath())) {
			paramsToShare.setImageUrl(null);
		}

	}

}
