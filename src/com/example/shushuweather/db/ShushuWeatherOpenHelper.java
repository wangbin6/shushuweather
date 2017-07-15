package com.example.shushuweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
/*
 * 舒舒天气数据库类
 * 
 * @author WangBin
 * @time 2017-07-13 13:51
 * 
 * */
public class ShushuWeatherOpenHelper extends SQLiteOpenHelper {
	
	//创建城市列表表cities的SQL语句
	public static final String CREATE_CITY = "CREATE TABLE city (id integer primary key autoincrement,weaid varchar(20),cityid varchar(25),cityno varchar(50),citynm varchar(20),province varchar(20),municipality varchar(20),county varchar(25))"; 
	
	public ShushuWeatherOpenHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		try {
			db.execSQL(CREATE_CITY);
			//db.execSQL(".import file:///android_asset/city.sql city");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
