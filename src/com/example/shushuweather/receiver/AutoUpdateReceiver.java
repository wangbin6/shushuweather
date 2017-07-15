package com.example.shushuweather.receiver;

import com.example.shushuweather.service.AutoUpdateWeather;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AutoUpdateReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Intent i = new Intent(context,AutoUpdateWeather.class);
		context.startService(i);
	}

}
