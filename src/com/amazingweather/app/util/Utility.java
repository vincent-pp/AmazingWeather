package com.amazingweather.app.util;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.amazingweather.app.db.AmazingWeatherDB;
import com.amazingweather.app.model.City;
import com.amazingweather.app.model.County;
import com.amazingweather.app.model.Province;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

public class Utility {

	public synchronized static boolean handleProvincesResponse(AmazingWeatherDB amazingWeatherDB,String response){
		
		if(!TextUtils.isEmpty(response)){
			String[] allProvinces=response.split(",");
			if(allProvinces!=null&&allProvinces.length>0){
				for(String p:allProvinces){
					String[] array=p.split("\\|");
					Province province=new Province();
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
					
					amazingWeatherDB.saveProvince(province);
				}
				return true;
			}
		}
		return false;
	}
	
    public synchronized static boolean handleCitiesResponse(AmazingWeatherDB amazingWeatherDB,String response,int provinceId){
		
		if(!TextUtils.isEmpty(response)){
			String[] allCities=response.split(",");
			if(allCities!=null&&allCities.length>0){
				for(String c:allCities){
					String[] array=c.split("\\|");
					City city=new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					
					amazingWeatherDB.saveCity(city);
				}
				return true;
			}
		}
		return false;
	}


    public synchronized static boolean handleCountiesResponse(AmazingWeatherDB amazingWeatherDB,String response,int cityId){
		
		if(!TextUtils.isEmpty(response)){
			String[] allCounties=response.split(",");
			if(allCounties!=null&&allCounties.length>0){
				for(String c:allCounties){
					String[] array=c.split("\\|");
					County county=new County();
					county.setCountyCode(array[0]);
					county.setCountyName(array[1]);
					county.setCityId(cityId);
					
					amazingWeatherDB.saveCounty(county);
				}
				return true;
			}
		}
		return false;
	}
	
    
    public static void handleWeatherResponse(Context context,String response){
    	
    	try{
    		JSONObject jsonObject=new JSONObject(response);
    		JSONObject weatherInfo=jsonObject.getJSONObject("weatherinfo");
    		String cityName=weatherInfo.getString("city");
    		String weatherCode=weatherInfo.getString("cityid");
    		String templow=weatherInfo.getString("temp1");
    		String temphigh=weatherInfo.getString("temp2");
    		String weatherDesp=weatherInfo.getString("weather");
    		String publishTime=weatherInfo.getString("ptime");
    		saveWeatherInfo(context,cityName,weatherCode,templow,temphigh,weatherDesp,publishTime);
    	}catch(JSONException e){
    		e.printStackTrace();
    	}
    }
    
    public static void saveWeatherInfo(Context context,String cityName,String weatherCode,String templow,String temphigh,String weatherDesp,String publishTime){
    	
    	SimpleDateFormat sdf=new SimpleDateFormat("yyyyƒÍM‘¬d»’",Locale.CHINA);
    	SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(context).edit();
    	editor.putBoolean("city_selected", true);
    	editor.putString("city_name", cityName);
    	editor.putString("weather_code",weatherCode);
    	editor.putString("temp_low", templow);
    	editor.putString("temp_high", temphigh);
    	editor.putString("weather_desp", weatherDesp);
    	editor.putString("publish_time", publishTime);
    	editor.putString("current_date", sdf.format(new Date()));
    	editor.commit();
    }
}

