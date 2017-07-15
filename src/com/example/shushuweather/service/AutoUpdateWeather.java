package com.example.shushuweather.service;

import com.example.shushuweather.receiver.AutoUpdateReceiver;
import com.example.shushuweather.utils.HttpCallbackListener;
import com.example.shushuweather.utils.HttpUtil;
import com.example.shushuweather.utils.Utility;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

public class AutoUpdateWeather extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("AutoUpdateWeather", "天气已更新");
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				updateWeather();
			}}).start();
			
			AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
			int time = 60*60*8*1000;//10*1000;//60*60*8*1000;
			long triggerAtTime = SystemClock.elapsedRealtime()+time;
			Intent i = new Intent(this,AutoUpdateReceiver.class);
			PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
			manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
			return super.onStartCommand(intent, flags, startId);
	}
	
	private void updateWeather()
	{
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		
		String weaid = sp.getString("weaid", "");
		
		String address = "http://api.k780.com/?app=weather.today&weaid="+weaid+"&appkey=26776&sign=42d21df6df1c8068dbd225379b10ac98&format=json";
		
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				// TODO Auto-generated method stub
				Utility.handleWeatherResponse(AutoUpdateWeather.this, response);
				
			}
			
			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				e.printStackTrace();
			}
		});
	}
}
