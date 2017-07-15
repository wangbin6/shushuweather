package com.example.shushuweather.db;
/*
 * 数据库工具类
 * 
 * @author WangBin
 * @time 2017-07-13
 * */

import java.util.ArrayList;
import java.util.List;

import com.example.shushuweather.models.City;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ShushuWeatherDB {
	public static final String DB_NAME = "shushu_weather";//数据库名
	public static final String TB_CITY = "city";//城市列表表名
	public static final int DB_VERSION = 1;//数据库版本号
	private static ShushuWeatherDB shushuWeatherDB;
	private SQLiteDatabase db;
	
	/*将构造方法私有化*/
	private ShushuWeatherDB(Context context) {
		// TODO Auto-generated constructor stub
		ShushuWeatherOpenHelper dbHelper = new ShushuWeatherOpenHelper(context, DB_NAME, null, DB_VERSION);
		Log.d("ShushuWeatherDB", "将构造方法私有化");
		db = dbHelper.getWritableDatabase();
	}
	
	/*获取shushuWeatherDB的实例*/
	public synchronized static ShushuWeatherDB getinstance(Context context)
	{
		if(shushuWeatherDB==null)
		{
			shushuWeatherDB = new ShushuWeatherDB(context);
		}
		
		return shushuWeatherDB;
	}
	
	/*
	 * 检测城市是否存在
	 * 存在返回true，不存在返回false
	 * */
	public boolean checkCityExist(String cityid)
	{
		Cursor cursor = db.query(TB_CITY, new String[]{"id"},"cityid=?", new String[]{cityid}, null, null, null);
		
		if(cursor.getCount()>0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/*将城市信息存入city表中*/
	public void saveCity(City city)
	{
		if(city != null)
		{
			String cityid = city.getCityid();
			boolean isExist = checkCityExist(cityid);
			
			if(!isExist)
			{
				ContentValues values = new ContentValues();
				
				values.put("weaid", city.getWeaid());
				values.put("citynm", city.getCitynm());
				values.put("cityno", city.getCityno());
				values.put("cityid", city.getCityid());
				values.put("province", city.getProvince());
				values.put("municipality", city.getMunicipality());
				values.put("county", city.getCounty());
				
				db.insert(TB_CITY, null, values);
			}
		}
	}
	
	/*读取所有的省份*/
	public List<City> getAllProvinces()
	{
		List<City> list = new ArrayList<City>();
		
		Cursor cursor = db.query(TB_CITY, null, null, null, "province", null, null);
		
		if(cursor.moveToFirst())
		{
			do{
				City city = new City();
				city.setProvince(cursor.getString(cursor.getColumnIndex("province")));
				
				list.add(city);
				
			}while(cursor.moveToNext());
		}
		
		return list;
	}
	
	/*
	 * 根据省份名称获取省份下面的市
	 * 
	 * @param String provincenm 省份名称
	 * @return List list 所有属于该省份的市
	 * */
	public List<City> getAllMunicipality(String provincenm)
	{
		List<City> list = new ArrayList<City>();
		
		Cursor cursor = db.query(TB_CITY, null, "province=?", new String[]{provincenm}, "municipality", null, null);
		
		if(cursor.moveToFirst())
		{
			do{
				City city = new City();
				
				city.setMunicipality(cursor.getString(cursor.getColumnIndex("municipality")));
				
				list.add(city);
			}while(cursor.moveToNext());
		}
		
		return list;
	}
	
	/*
	 * 根据市名称获取市下面的区县
	 * 
	 * @param String municipality 市名称
	 * @return List list 所有属于该市的区县
	 * */
	public List<City> getAllCounty(String municipality)
	{
		List<City> list = new ArrayList<City>();
		
		Cursor cursor = db.query(TB_CITY, null, "municipality=?", new String[]{municipality}, null, null, null);
		
		if(cursor.moveToFirst())
		{
			do{
				City city = new City();
				city.setCounty(cursor.getString(cursor.getColumnIndex("county")));
				
				list.add(city);
			}while(cursor.moveToNext());
		}
		
		return list;
	}
	
	/**
	 * 根据城市名获取天气ID
	 * */
	public String getWeatherId(String county)
	{
		Cursor cursor = db.query(TB_CITY, new String[]{"weaid"}, "county=?", new String[]{county}, null, null, null);
		
		if(cursor.moveToFirst())
		{
			String weaid = cursor.getString(cursor.getColumnIndex("weaid"));
			
			if(weaid!=null)
			{
				return weaid;
			}
			else
			{
				return "";
			}
		}
		else
		{
			return "";
		}
	}
}
