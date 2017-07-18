package com.example.shushuweather.activity;

import com.example.shushuweather.R;
import com.example.shushuweather.db.ShushuWeatherDB;
import com.example.shushuweather.receiver.NetworkChangedReceiver;
import com.example.shushuweather.service.AutoUpdateWeather;
import com.example.shushuweather.utils.HttpCallbackListener;
import com.example.shushuweather.utils.HttpUtil;
import com.example.shushuweather.utils.MyData;
import com.example.shushuweather.utils.Utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
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
	private Button infobtn;//软件相关
	private ImageView wicon;//天气图标
	private String county;
	private boolean networkavilable=false;//网络是否可用,默认不可用
	private MyData mydata;
	private NetworkChangedReceiver mNetworkChangedReceiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mydata = (MyData)getApplication();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		
		//初始化各控件
		weatherInfoLayout = (LinearLayout)findViewById(R.id.weather_info_layout);
		citynm = (TextView)findViewById(R.id.city_name);
		publishText = (TextView)findViewById(R.id.publish_text);
		weatherDespText = (TextView)findViewById(R.id.weather_desp);
		tempLow = (TextView)findViewById(R.id.templ1);
		tempHigh = (TextView)findViewById(R.id.templ2);
		currentDate = (TextView)findViewById(R.id.current_date);
		switchCityBtn = (Button)findViewById(R.id.switch_city);
		refreshWeather = (Button)findViewById(R.id.refresh_weather);
		infobtn = (Button)findViewById(R.id.infobtn);
		wicon = (ImageView)findViewById(R.id.wicon);
		
		//监听网络
		mNetworkChangedReceiver = new NetworkChangedReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
		filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
		filter.addAction("android.net.wifi.STATE_CHANGE");
		registerReceiver(mNetworkChangedReceiver, filter);
		
		//检测网络
		//networkavilable = Utility.checkNetworkAvailable(WeatherActivity.this);
		networkavilable = mydata.getNetworkIsOk();
		
		//绑定点击事件
		switchCityBtn.setOnClickListener(this);
		refreshWeather.setOnClickListener(this);
		infobtn.setOnClickListener(this);
		
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
		unregisterReceiver(mNetworkChangedReceiver);
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
		case R.id.infobtn:
			showAppInfoDialog();
		default:
			break;
		}
	}
	
	//显示软件相关弹出框
	private void showAppInfoDialog()
	{
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("舒舒天气");
		//builder.setIcon(R.drawable.info);
		builder.setMessage("《舒舒天气》"+" 版本号"+R.string.version);
		builder.show();
	}
	
	//通过县级城市名获取天气ID，在通过ID区获取天气
	private void queryWeatherCounty(String county)
	{
		if(!networkavilable)
		{
			Toast.makeText(this, "小主~网络不可用哟~", Toast.LENGTH_SHORT).show();
			publishText.setText("网络不可用...");
			return;
		}
		
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
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(WeatherActivity.this, "获取天气信息失败！", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}
	
	//显示本地天气
	private void showWeather()
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String txt = prefs.getString("txt", "");//多云，晴，etc
		county = prefs.getString("county", "");
		citynm.setText(county);
		tempLow.setText(prefs.getString("tmp", "")+"℃");
		tempHigh.setText(prefs.getString("dir", ""));
		currentDate.setText(prefs.getString("date", "")+" "+prefs.getString("week", ""));
		weatherDespText.setText(txt);
		publishText.setText("今天"+prefs.getString("publishtime", "")+"发布");
		weatherInfoLayout.setVisibility(View.VISIBLE);
		citynm.setVisibility(View.VISIBLE);
		Log.d("txt", txt);
		if(txt.indexOf("多云")!=-1)
		{
			wicon.setImageDrawable(getResources().getDrawable(R.drawable.duoyun));
		}
		else if(txt.indexOf("雷阵雨")!=-1)
		{
			wicon.setImageDrawable(getResources().getDrawable(R.drawable.leizhenyu));
		}
		else if(txt.indexOf("晴")!=-1)
		{
			wicon.setImageDrawable(getResources().getDrawable(R.drawable.qing));
		}
		else if(txt.indexOf("雪")!=-1)
		{
			wicon.setImageDrawable(getResources().getDrawable(R.drawable.xue));
		}
		else if(txt.indexOf("阴")!=-1)
		{
			wicon.setImageDrawable(getResources().getDrawable(R.drawable.yin));
		}
		else if(txt.indexOf("雨")!=-1)
		{
			wicon.setImageDrawable(getResources().getDrawable(R.drawable.yu));
		}
		else
		{
			wicon.setImageDrawable(getResources().getDrawable(R.drawable.undefined));
		}
		
		Intent i = new Intent(WeatherActivity.this,AutoUpdateWeather.class);
		startService(i);
	}
}
