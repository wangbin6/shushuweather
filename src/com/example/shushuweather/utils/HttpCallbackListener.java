package com.example.shushuweather.utils;
/*
 * 网络访问回调接口
 * */
public interface HttpCallbackListener {
	
	void onFinish(String response);
	
	void onError(Exception e);
}
