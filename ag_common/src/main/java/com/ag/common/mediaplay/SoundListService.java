package com.ag.common.mediaplay;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.text.TextUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 支持同时播放多个音频
 *
 */
public class SoundListService extends Service {

	
	private Map<String, MediaPlayer> mapPlayers=new HashMap<String, MediaPlayer>();
	private List<String> pausePlayers=new ArrayList<String>();
	public final static String Play_Key="playing";
    public final static String Sound_Path_Key="soundPath";
    public final static String Num_Loop_Key="numLoop";
	 
    @Override
    public void onCreate() {
        super.onCreate();
    }
 
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mapPlayers!=null && mapPlayers.size()>0){
        	Iterator<String> iterator= mapPlayers.keySet().iterator();
        	while(iterator.hasNext()){
        		MediaPlayer player=mapPlayers.get(iterator.next());
        		player.release();
        	}
        }
        stopSelf();
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int playing = intent.getIntExtra(Play_Key, SoundEnum.Play.GetSoundEnum());
        String soundPath=intent.getStringExtra(Sound_Path_Key);
        int numLoop=intent.getIntExtra(Num_Loop_Key, 0);
        
        //
        if(playing == SoundEnum.Pause.GetSoundEnum() && TextUtils.isEmpty(soundPath)){
        	onPause();
        	return super.onStartCommand(intent, flags, startId);
        }else if(playing == SoundEnum.Play.GetSoundEnum() && TextUtils.isEmpty(soundPath)){
        	onResume();
        	return super.onStartCommand(intent, flags, startId);
        }
        
        MediaPlayer player=null;
        if(mapPlayers.containsKey(soundPath)){
        	player=mapPlayers.get(soundPath);
        }
        
        if(player!=null){
        	if(player.isPlaying()){
        		player.pause();
        	}else{
        		player.start();
        	}
        	return super.onStartCommand(intent, flags, startId);
        }
        
        player=new MediaPlayer();
    	mapPlayers.put(soundPath, player);
        
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

        player.start();
        
        return super.onStartCommand(intent, flags, startId);
    }
    
    private void onPause(){
    	if(mapPlayers!=null && mapPlayers.size()>0){
        	Iterator<String> iterator= mapPlayers.keySet().iterator();
        	while(iterator.hasNext()){
        		String key=iterator.next();
        		MediaPlayer player=mapPlayers.get(key);
        		if(player.isPlaying()){
        			player.pause();
        			pausePlayers.add(key);
        		}
        	}
        }
    }
    
    private void onResume(){
    	if(pausePlayers!=null && pausePlayers.size()>0){
    		for (String key : pausePlayers) {
    			MediaPlayer player=mapPlayers.get(key);
    			if(!player.isPlaying())
    				player.start();
			}
    		pausePlayers.clear();
    	}
    }
 
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
