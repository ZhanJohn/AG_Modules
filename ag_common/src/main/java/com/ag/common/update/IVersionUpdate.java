package com.ag.common.update;

public interface IVersionUpdate {

	/**
	 * 必须强制更新
	 */
	void onMustUpdate(VersionInfo info);
	
	/**
	 * 有版本更新
	 */
	void onHasUpdate(VersionInfo info);
	
	/**
	 * 当前已是最新版本
	 */
	void onLastVersion();
	
	/**
	 * 更新失败
	 */
	void onUpdateError();

	/**
	 * 准备更新，可在这时显示Dialog
	 */
	void onUpdatePre();

}
