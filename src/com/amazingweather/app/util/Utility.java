package com.amazingweather.app.util;

import com.amazingweather.app.db.AmazingWeatherDB;
import com.amazingweather.app.model.City;
import com.amazingweather.app.model.County;
import com.amazingweather.app.model.Province;

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
	
}

