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
import com.example.shushuweather.models.County;
import com.example.shushuweather.models.Province;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ShushuWeatherDB {
	public static final String DB_NAME = "shushu_weather";//数据库名
	public static final String TB_CITY = "city";//城市列表表名
	public static final String TB_CITY_NAME = "acity";//市列表表名
	public static final String TB_PROVINCE_NAME = "aprovince";//省列表表名
	public static final String TB_COUNTY_NAME = "acounty";//区县列表表名
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
	
	/*将省信息存入province表中*/
	public void saveProvince(Province province)
	{
		if(province!=null)
		{
			ContentValues values = new ContentValues();
			
			values.put("provincenm", province.getProvincenm());
			
			db.insert(TB_PROVINCE_NAME, null, values);
		}
	}
	
	/*将市信息存入city表中*/
	public void saveCity(City city)
	{
		if(city!=null)
		{
			try
			{
				ContentValues values = new ContentValues();
				
				values.put("provincenm", city.getProvincenm());
				values.put("citynm", city.getCitynm());
				
				db.insert(TB_CITY_NAME, null, values);
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}
	}
	
	/*将县信息存入county表中*/
	public void saveCounty(County county)
	{
		if(county!=null)
		{
			ContentValues values = new ContentValues();
			
			values.put("citynm", county.getCitynm());
			values.put("countynm", county.getCountynm());
			
			db.insert(TB_COUNTY_NAME, null, values);
		}
	}	
	
	/*读取所有的省份*/
	public List<Province> getAllProvinces()
	{
		List<Province> list = new ArrayList<Province>();
		
		Cursor cursor = db.query(TB_PROVINCE_NAME, new String[]{"provincenm"}, null, null, null, null, null);
		
		if(cursor.moveToFirst())
		{
			do{
				Province province = new Province();
				province.setProvincenm(cursor.getString(cursor.getColumnIndex("provincenm")));
				
				list.add(province);
				
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
	public List<City> getAllCity(String provincenm)
	{
		List<City> list = new ArrayList<City>();
		
		Cursor cursor = db.query(TB_CITY_NAME, null, "provincenm=?", new String[]{provincenm}, "citynm", null, null);
		
		if(cursor.moveToFirst())
		{
			do{
				City city = new City();
				
				city.setCitynm(cursor.getString(cursor.getColumnIndex("citynm")));
				
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
	public List<County> getAllCounty(String citynm)
	{
		List<County> list = new ArrayList<County>();
		
		Cursor cursor = db.query(TB_COUNTY_NAME, null, "citynm=?", new String[]{citynm}, null, null, null);

		if(cursor.moveToFirst())
		{
			do{
				County county = new County();
				county.setCountynm(cursor.getString(cursor.getColumnIndex("countynm")));
				
				list.add(county);
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
