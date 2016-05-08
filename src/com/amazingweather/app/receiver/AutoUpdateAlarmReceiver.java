package com.amazingweather.app.receiver;

import com.amazingweather.app.service.AutoUpdateService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AutoUpdateAlarmReceiver extends BroadcastReceiver{
	
	@Override
	public void onReceive(Context context,Intent intent){
		Intent autoupdateIntent=new Intent(context,AutoUpdateService.class);
		context.startService(autoupdateIntent);
	}

}
