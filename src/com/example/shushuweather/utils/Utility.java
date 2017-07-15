package com.example.shushuweather.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.shushuweather.db.ShushuWeatherDB;
import com.example.shushuweather.models.City;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

public class Utility {
	
	/*
	 * 解析处理服务器返回的城市列表信息
	 * 
	 * */
	public synchronized static boolean handleCityResponse(ShushuWeatherDB shushuWeatherDB,String response){
		if(!TextUtils.isEmpty(response))
		{
			JSONObject jsonObject = JSON.parseObject(response);
			
			//判断json数据是否成功
			if(jsonObject.getInteger("success")==1)
			{
				JSONObject results = JSON.parseObject(jsonObject.getString("result"));

				for(int i=1;i<=results.size();i++)
				{
					JSONObject res = JSON.parseObject(results.getString(""+i));
					if(res!=null)
					{
						City city = new City();
						city.setCityid(res.getString("cityid"));
						city.setCitynm(res.getString("citynm"));
						city.setCityno(res.getString("cityno"));
						city.setWeaid(res.getString("weaid"));
						city.setProvince(res.getString("area_1"));
						city.setMunicipality(res.getString("area_2"));
						city.setCounty(res.getString("area_3"));
						
						shushuWeatherDB.saveCity(city);
						
						Log.d("saveCity", "cityid:"+res.getString("cityid"));
					}
				}
				
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * 解析天气信息json
	 * @author Administrator
	 */
	public static void handleWeatherResponse(Context context,String response)
	{
		try
		{
			JSONObject jsonObject = JSON.parseObject(response);
			
			//判断返回的json数据是否成功
			if(jsonObject.getInteger("success")==1)
			{
				JSONObject result = JSONObject.parseObject(jsonObject.getString("result"));
				if(result!=null)
				{
					String weaid = result.getString("weaid");
					String days = result.getString("days");
					String week = result.getString("week");
					String citynm = result.getString("citynm");
					String temperature = result.getString("temperature");
					String temperature_curr = result.getString("temperature_curr");
					String humidity = result.getString("humidity");
					String weather = result.getString("weather");
					String weather_curr = result.getString("weather_curr");
					String wind = result.getString("wind");
					String winp = result.getString("winp");
					String temp_high = result.getString("temp_high");
					String temp_low = result.getString("temp_low");
					String temp_curr = result.getString("temp_curr");
					
					saveWeatherInfo(context,weaid,days,week,citynm,temperature,temperature_curr,humidity,weather,weather_curr,wind,winp,temp_high,temp_low,temp_curr);
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public static void saveWeatherInfo(Context context,String weaid,String days,String week,String citynm,String temperature,String temperature_curr,String humidity,String weather,String weather_curr,String wind,String winp,String temp_high,String temp_low,String temp_curr)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("h时m分",Locale.CHINA);
		
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		
		editor.putBoolean("city_selected", true);
		editor.putString("weaid", weaid);
		editor.putString("days", days);
		editor.putString("week", week);
		editor.putString("citynm", citynm);
		editor.putString("temperature", temperature);
		editor.putString("temperature_curr", temperature_curr);
		editor.putString("humidity", humidity);
		editor.putString("weather", weather);
		editor.putString("weather_curr", weather_curr);
		editor.putString("wind", wind);
		editor.putString("winp", winp);
		editor.putString("temp_high", temp_high);
		editor.putString("temp_low", temp_low);
		editor.putString("temp_curr", temp_curr);
		editor.putString("publish_time", sdf.format(new Date()));
		
		editor.commit();
	}
}
