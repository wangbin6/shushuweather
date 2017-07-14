package com.example.shushuweather.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.shushuweather.db.ShushuWeatherDB;
import com.example.shushuweather.models.City;

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
			
			//判断是否成功
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
}
