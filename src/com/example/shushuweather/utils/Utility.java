package com.example.shushuweather.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.shushuweather.db.ShushuWeatherDB;
import com.example.shushuweather.models.City;
import com.example.shushuweather.models.County;
import com.example.shushuweather.models.Province;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

public class Utility {

	/*
	 * 解析处理服务器返回的县列表信息
	 * 
	 * */
	public synchronized static boolean handleCountyResponse(ShushuWeatherDB shushuWeatherDB,String response){
		if(!TextUtils.isEmpty(response))
		{
			JSONObject jsonObject = JSON.parseObject(response);
			
			//判断json数据是否成功
			if(jsonObject.getInteger("status")==1)
			{
				JSONArray results = JSON.parseArray(jsonObject.getString("districts"));
				
				JSONObject result2 = JSON.parseObject((results.get(0)).toString());
				
				String citynm = result2.getString("name");//县名称
				
				JSONArray citys = JSON.parseArray(result2.getString("districts"));

				for(Object city:citys)
				{
					if(citys!=null)
					{
						JSONObject p = (JSONObject)city;
						County countyObj = new County();
						countyObj.setCountynm(p.getString("name"));
						countyObj.setCitynm(citynm);
						shushuWeatherDB.saveCounty(countyObj);
						
						Log.d("saveCounty", "县:"+p.getString("name"));
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
	
	/*
	 * 解析处理服务器返回的市列表信息
	 * 
	 * */
	public synchronized static boolean handleCityResponse(ShushuWeatherDB shushuWeatherDB,String response){
		if(!TextUtils.isEmpty(response))
		{
			JSONObject jsonObject = JSON.parseObject(response);
			
			//判断json数据是否成功
			if(jsonObject.getInteger("status")==1)
			{
				JSONArray results = JSON.parseArray(jsonObject.getString("districts"));
				
				JSONObject result2 = JSON.parseObject((results.get(0)).toString());
				
				String provincenm = result2.getString("name");//省名称
				
				JSONArray citys = JSON.parseArray(result2.getString("districts"));

				for(Object city:citys)
				{
					if(city!=null)
					{
						JSONObject c = (JSONObject)city;
						City cityObj = new City();
						cityObj.setCitynm(c.getString("name"));
						cityObj.setProvincenm(provincenm);
						shushuWeatherDB.saveCity(cityObj);
						
						Log.d("saveCity", "市:"+c.getString("name"));
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
	/*
	 * 解析处理服务器返回的城市列表信息-新
	 * 
	 * */
	public synchronized static boolean handleProvinceResponse(ShushuWeatherDB shushuWeatherDB,String response){
		if(!TextUtils.isEmpty(response))
		{
			JSONObject jsonObject = JSON.parseObject(response);
			
			//判断json数据是否成功
			if(jsonObject.getInteger("status")==1)
			{
				JSONArray results = JSON.parseArray(jsonObject.getString("districts"));
				
				JSONObject result2 = JSON.parseObject((results.get(0)).toString());
				
				JSONArray provinces = JSON.parseArray(result2.getString("districts"));

				for(Object province:provinces)
				{
					if(province!=null)
					{
						JSONObject p = (JSONObject)province;
						Province provinceObj = new Province();
						provinceObj.setProvincenm(p.getString("name"));
						
						shushuWeatherDB.saveProvince(provinceObj);
						
						Log.d("saveProvince", "省:"+p.getString("name"));
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
	
	//链接里有中文需转码
	public static String UrlTranslateToUTF(String address)
	{
		try
		{
			address = URLEncoder.encode(address, "utf-8");
			
			return address;
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			return "";
			
		}
	}	
}
