package org.gofirefly.firefly_android.log;

import android.util.Log;

public class DLog {
	private static final boolean DEBUG = true;
	private static final String TAG = "Firefly";
	
	public static void d(String msg){
		if (DEBUG) {
			Log.d(TAG, ">>> " + msg);
		}
	}
	
}
