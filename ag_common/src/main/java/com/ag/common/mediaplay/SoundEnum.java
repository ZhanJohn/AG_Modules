package com.ag.common.mediaplay;

public enum SoundEnum {

	/**
	 * 播放 
	 */
	Play(0),
	
	/**
	 * 暂停
	 */
	Pause(1),
	
	/**
	 * 停止
	 */
	Stop(2);
	
	private final int sound;
	
	private SoundEnum(int value){
		this.sound=value;
	}
	
	public int GetSoundEnum(){
		return sound;
	}
	
}
