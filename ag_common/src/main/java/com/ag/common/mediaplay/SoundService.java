package com.ag.common.mediaplay;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.text.TextUtils;

import java.io.IOException;

public class SoundService extends Service {

	private MediaPlayer player;
	public final static String Play_Key="playing";
    public final static String Sound_Path_Key="soundPath";
    public final static String Num_Loop_Key="numLoop";
    private String preLoadPath;
	 
    @Override
    public void onCreate() {
        super.onCreate();
        player = new MediaPlayer();
    }
 
    @Override
    public void onDestroy() {
        super.onDestroy();
        player.release();
        stopSelf();
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int playing = intent.getIntExtra(Play_Key, SoundEnum.Play.GetSoundEnum());
        String soundPath=intent.getStringExtra(Sound_Path_Key);
        int numLoop=intent.getIntExtra(Num_Loop_Key, 0);
        
        if(playing == SoundEnum.Pause.GetSoundEnum()){
        	player.pause();
        }else if(playing == SoundEnum.Stop.GetSoundEnum()){
        	player.pause();
        	player.seekTo(0);
        }else{
        	//之前有播放过音频，并且当前音频跟之前不同时重置播放器
        	if(!TextUtils.isEmpty(preLoadPath) && !soundPath.equals(preLoadPath)){
        		player.reset();
        	}else if(soundPath.equals(preLoadPath)){
        		if(!player.isPlaying()){
        			player.start();
        		}
        		//如果当前要播放音频跟之前播放的音频是相同的，并且播放器处于播放状态中
        		return super.onStartCommand(intent, flags, startId);
        	}
        	
        	try {
        		if(soundPath.indexOf("http")==0 || soundPath.indexOf("file:///")==0){
        			player.setDataSource(soundPath);
        		}else{
        			AssetManager asm=getResources().getAssets();
                	AssetFileDescriptor fileDescriptor=asm.openFd(soundPath);
                	
                	player.setDataSource(fileDescriptor.getFileDescriptor(),
                			fileDescriptor.getStartOffset(),
                			fileDescriptor.getLength());
        		}
            	
    			player.prepare();
    		} catch (IllegalStateException e) {
    			e.printStackTrace();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
            
            if(numLoop==0){
            	player.setLooping(false);
            }else{
            	player.setLooping(true);
            }
            
            this.preLoadPath=soundPath;
            player.start();
        }
        
        return super.onStartCommand(intent, flags, startId);
    }
 
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
