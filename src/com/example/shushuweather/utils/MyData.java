package com.example.shushuweather.utils;

import android.app.Application;

/**
 * 定义全局变量类
 * @author WangBin
 * @time 2017-07-18
 *
 */
public class MyData extends Application{
	private boolean networkIsOk;
	private int networkType;//1:GPRS;2:wifi
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		networkIsOk=false;//网络状态默认不可用
		networkType=1;
	}
	
	public boolean getNetworkIsOk() {
		return networkIsOk;
	}
	
	public void setNetworkIsOk(boolean networkIsOk) {
		this.networkIsOk = networkIsOk;
	}
	
	public int getNetworkType() {
		return networkType;
	}
	public void setNetworkType(int networkType) {
		this.networkType = networkType;
	}
	
	
}
