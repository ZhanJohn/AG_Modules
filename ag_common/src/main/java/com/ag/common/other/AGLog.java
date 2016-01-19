package com.ag.common.other;

import android.content.Context;
import android.util.Log;

public class AGLog {

	private static boolean DEBUG = false;

	private static AGLog zqLog = new AGLog();

	private AGLog() {
	}

	public static void Init(Context context) {
		DEBUG = AGAppControl.isApkDebugable(context);
	}

	public static void Print(String msg) {
		if (DEBUG)
			return;
		Log.i(AGLog.class.getSimpleName(), msg);
	}

	public static void Print(String tag, String msg) {
		if (DEBUG)
			return;
		Log.i(tag, msg);
	}

}
