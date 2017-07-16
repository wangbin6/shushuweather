package com.example.shushuweather.activity;

import com.example.shushuweather.R;
import com.example.shushuweather.db.ShushuWeatherDB;
import com.example.shushuweather.service.AutoUpdateWeather;
import com.example.shushuweather.utils.HttpCallbackListener;
import com.example.shushuweather.utils.HttpUtil;
import com.example.shushuweather.utils.Utility;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WeatherActivity extends Activity implements OnClickListener{
	
	private LinearLayout weatherInfoLayout;
	private TextView citynm;//显示城市名
	private TextView publishText;//显示发布时间
	private TextView weatherDespText;//显示天气
	private TextView tempLow;//显示最低气温
	private TextView tempHigh;//显示最高气温
	private TextView currentDate;//显示当前日期
	private Button switchCityBtn;//切换城市按钮
	private Button refreshWeather;//更新天气按钮
	private String county;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		
		weatherInfoLayout = (LinearLayout)findViewById(R.id.weather_info_layout);
		citynm = (TextView)findViewById(R.id.city_name);
		publishText = (TextView)findViewById(R.id.publish_text);
		weatherDespText = (TextView)findViewById(R.id.weather_desp);
		tempLow = (TextView)findViewById(R.id.templ1);
		tempHigh = (TextView)findViewById(R.id.templ2);
		currentDate = (TextView)findViewById(R.id.current_date);
		switchCityBtn = (Button)findViewById(R.id.switch_city);
		refreshWeather = (Button)findViewById(R.id.refresh_weather);
		
		switchCityBtn.setOnClickListener(this);
		refreshWeather.setOnClickListener(this);
		
		county = getIntent().getStringExtra("county");

		if(!TextUtils.isEmpty(county))
		{
			//有县级名称就去查询天气
			publishText.setText("同步中...");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			citynm.setVisibility(View.INVISIBLE);
			queryWeatherCounty(county);
		}
		else
		{
			//没有则直接显示本地天气
			showWeather();
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Intent i = new Intent(WeatherActivity.this,AutoUpdateWeather.class);
		stopService(i);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.switch_city:
			Intent intent = new Intent(this,ChooseAreaActivity.class);
			intent.putExtra("from_weather_activity", true);
			startActivity(intent);
			finish();
			break;
		case R.id.refresh_weather:
			publishText.setText("同步中...");
			queryWeatherCounty(county);
			break;
		default:
			break;
		}
	}
	
	//通过县级城市名获取天气ID，在通过ID区获取天气
	private void queryWeatherCounty(String county)
	{
		if(county!="")
		{
			county = Utility.UrlTranslateToUTF(county);
			
			//京东Api
			String address = "https://way.jd.com/he/freeweather?city="+county+"&appkey=3258aaecb16449111594ef945633cdf6";
			
			queryFromServer(address);
		}
	}
	
	//从服务器上查询天气
	private void queryFromServer(String address)
	{
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				// TODO Auto-generated method stub
				Utility.handleJDWeatherResponse(WeatherActivity.this, response);
				
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						showWeather();
					}
				});
			}
			
			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				Toast.makeText(WeatherActivity.this, "获取天气信息失败！", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	//显示本地天气
	private void showWeather()
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		county = prefs.getString("county", "");
		citynm.setText(county);
		tempLow.setText(prefs.getString("tmp", "")+"℃");
		tempHigh.setText(prefs.getString("dir", ""));
		currentDate.setText(prefs.getString("date", "")+" "+prefs.getString("week", ""));
		weatherDespText.setText(prefs.getString("txt", ""));
		publishText.setText("今天"+prefs.getString("publishtime", "")+"发布");
		weatherInfoLayout.setVisibility(View.VISIBLE);
		citynm.setVisibility(View.VISIBLE);
		
		Intent i = new Intent(WeatherActivity.this,AutoUpdateWeather.class);
		startService(i);
	}
}
