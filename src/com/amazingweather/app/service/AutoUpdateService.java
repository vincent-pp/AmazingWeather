package com.amazingweather.app.service;

import java.util.Date;
import com.amazingweather.app.activity.WeatherActivity;
import com.amazingweather.app.receiver.AutoUpdateAlarmReceiver;
import com.amazingweather.app.util.HttpCallbackListener;
import com.amazingweather.app.util.HttpUtil;
import com.amazingweather.app.util.Utility;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

public class AutoUpdateService extends Service{
	
	@Override 
	public IBinder onBind(Intent intent){
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent,int flags,int startId){
		
		new Thread(new Runnable(){
			@Override
			public void run(){
				updateWeather();
			}
		}).start();
		AlarmManager manager=(AlarmManager)getSystemService(ALARM_SERVICE);
		int timegap=60*60*1000;
		long triggerAtTime=SystemClock.elapsedRealtime()+timegap;
		Intent alarmintent=new Intent(this,AutoUpdateAlarmReceiver.class);
		PendingIntent pi=PendingIntent.getBroadcast(this, 0, alarmintent, 0);
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
		return super.onStartCommand(intent, flags, startId);
	}

	public void updateWeather(){
		SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
		String weatherCode=prefs.getString("weather_code", "");
		if(!TextUtils.isEmpty(weatherCode)){
			String address="http://www.weather.com.cn/data/cityinfo/"+weatherCode+".html";
			HttpUtil.sendHttpRequest(address,new HttpCallbackListener(){
				@Override
				public void onFinish(String response){
						Utility.handleWeatherResponse(AutoUpdateService.this, response);
					}	
					
				@Override 
				public void onError(Exception e){
					e.printStackTrace();
				}
			});
		}
	}
}
