package com.ag.common.js;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * 下载完成后的提示
 *
 */
public class DownloadCompleteReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)){
//			long downId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,-1);
			Toast.makeText(context,"下载完成",Toast.LENGTH_SHORT).show();
		}
	}

}
