package com.amazingweather.app.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;

public class HttpUtil {

	public static void sendHttpRequest(final String address,final HttpCallbackListener listener){
		if(!isNetworkAvailable()){
			Toast.makeText(MyApplication.getContext(), "network is unavailabe", Toast.LENGTH_SHORT).show();
			return;
		}
		new Thread(new Runnable(){
			@Override
			public void run(){
				HttpURLConnection connection=null;
				try{
					URL url=new URL(address);
					connection=(HttpURLConnection)url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					InputStream in=connection.getInputStream();
					BufferedReader reader=new BufferedReader(new InputStreamReader(in));
					StringBuilder response=new StringBuilder();
					String line;
					while((line=reader.readLine())!=null){
						response.append(line);
					}
					if(listener!=null){
						listener.onFinish(response.toString());
					}
				}catch(Exception e){
					if(listener!=null){
						listener.onError(e);
					}
				}finally{
					if(connection!=null){
						connection.disconnect();
					}
				}
			}
		}).start();
	}

	private static boolean isNetworkAvailable() {
		ConnectivityManager manager=(ConnectivityManager) MyApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		if(manager.getActiveNetworkInfo()!=null){
			return manager.getActiveNetworkInfo().isAvailable();
		}
		return false;
	}
}

