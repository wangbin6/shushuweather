package com.example.shushuweather.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.example.shushuweather.db.ShushuWeatherDB;
import com.example.shushuweather.models.City;
import com.example.shushuweather.models.County;
import com.example.shushuweather.models.Province;
import com.example.shushuweather.utils.HttpCallbackListener;
import com.example.shushuweather.utils.HttpUtil;
import com.example.shushuweather.utils.Utility;
import com.example.shushuweather.R;
import android.app.Activity;
import android.app.DownloadManager.Query;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseAreaActivity extends Activity implements OnClickListener{
	
	private TextView titleText;//标题
	private ListView listView;//城市列表
	//private Button update_city_btn;//更新城市列表按钮
	private ArrayAdapter<String> adapter;
	private ShushuWeatherDB shushuWeatherDB;
	private List<Province> provinceList;//省列表
	private List<City> cityList;//市列表
	private List<County> countyList;//区县列表
	private List<String> dataList = new ArrayList<String>();
	private int currentLevel;//当前选择的市省，市或者县
	private static final int LEVEL_PROVINCE = 0;//省
	private static final int LEVEL_MUNICIPALITY = 1;//市
	private static final int LEVEL_COUNTY = 2;//区县
	private ProgressDialog progressDialog;//加载
	private Province provinceSeletced;//选中的省
	private City citySelected;//选中的市
	private County countySelected;//选中的区县
	private boolean networkavilable = false;//网络不可用

	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		//查看网络状态
		networkavilable = Utility.checkNetworkAvailable(ChooseAreaActivity.this);
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		//是否从WeatherActivity活动中过来;true表示是
		boolean from_weather_activity = getIntent().getBooleanExtra("from_weather_activity", false);
		if(!from_weather_activity)
		{
			//如果之前获取过天气信息则直接跳转天气
			if(prefs.getBoolean("city_selected", false))
			{
				Intent intent = new Intent(this,WeatherActivity.class);
				startActivity(intent);
				finish();
				return;
			}
		}

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		
		titleText = (TextView)findViewById(R.id.title_text);
		listView = (ListView)findViewById(R.id.list_view);
		//update_city_btn = (Button)findViewById(R.id.update_city_btn);
		
		//update_city_btn.setOnClickListener(this);//更新城市列表按钮绑定点击事件
		
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,dataList);
		
		listView.setAdapter(adapter);
		
		shushuWeatherDB = ShushuWeatherDB.getinstance(this);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				if(currentLevel==LEVEL_PROVINCE)
				{
					provinceSeletced = provinceList.get(position);
					queryCity();//获取该省下属的所有市
				}
				else if(currentLevel ==LEVEL_MUNICIPALITY)
				{
					citySelected = cityList.get(position);
					queryCounty();//获取该市下属的所有区县
				}
				else if(currentLevel ==LEVEL_COUNTY)
				{
					String county = countyList.get(position).getCountynm();
					
					Intent intent = new Intent(ChooseAreaActivity.this,WeatherActivity.class);
					intent.putExtra("county", county);
					startActivity(intent);
					finish();
				}
				
				
			}
		});
		
		queryProvinces();//第一次加载省份数据
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		/*case R.id.update_city_btn:
			queryProvincesfromServer();
			break;*/

		default:
			break;
		}
	}
	
	//获取选中市下属的所有区县
	public void queryCounty()
	{
		countyList = shushuWeatherDB.getAllCounty(citySelected.getCitynm());
		if(countyList.size()>0)
		{
			dataList.clear();
			
			for(County county:countyList)
			{
				dataList.add(county.getCountynm());
			}
			
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(citySelected.getCitynm());
			currentLevel =LEVEL_COUNTY;
		}
		else
		{
			if(networkavilable)
			{
				queryCountyFromServer();//从服务器获取区县信息
			}
			else
			{
				Toast.makeText(this, "小主~网络不可用哟~", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	//获取选中省份下属的所有市
	public void queryCity()
	{
		cityList = shushuWeatherDB.getAllCity(provinceSeletced.getProvincenm());

		if(cityList.size()>0)
		{
			dataList.clear();
			
			for(City city:cityList)
			{
				dataList.add(city.getCitynm());
			}
			
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(provinceSeletced.getProvincenm());
			currentLevel = LEVEL_MUNICIPALITY;
		}
		else
		{
			if(networkavilable)
			{
				queryCityFromServer();
			}
			else
			{
				Toast.makeText(this, "小主~网络不可用哟~", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	//获取所有的中国的省
	private void queryProvinces()
	{
		provinceList = shushuWeatherDB.getAllProvinces();
		
		if(provinceList.size()>0)
		{
			dataList.clear();
			
			for(Province province:provinceList)
			{
				dataList.add(province.getProvincenm());
			}
			
			adapter.notifyDataSetChanged();
			listView.setSelection(0);//列表移动到指定的Position处
			titleText.setText("中国");
			currentLevel = LEVEL_PROVINCE;
		}
		else
		{
			if(networkavilable)
			{
				queryProvincesfromServer();
			}
			else
			{
				Toast.makeText(this, "小主~网络不可用哟~", Toast.LENGTH_SHORT).show();
			}
		}
	}

	//从服务器上获取某省下属县
	private void queryCountyFromServer()
	{
		showProgressDialog();
		String citynm = citySelected.getCitynm();
		citynm = Utility.UrlTranslateToUTF(citynm);
		String address = "http://restapi.amap.com/v3/config/district?keywords="+citynm+"&subdistrict=1&key=8eee051d08bc42cfc33dbd065a87d60e";
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				// TODO Auto-generated method stub
				boolean result = false;

				result = Utility.handleCountyResponse(shushuWeatherDB, response);
				
				if(result)
				{
					//通过runOnUiThread回到主线程处理逻辑
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							closeProgressDialog();
							queryCounty();
						}
					});
				}
				else
				{
					//通过runOnUiThread回到主线程处理逻辑
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							closeProgressDialog();
							Toast.makeText(ChooseAreaActivity.this, "获取区县信息失败！", Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
			
			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	//从服务器上获取某省下属市
	private void queryCityFromServer()
	{
		showProgressDialog();
		String provincenm = provinceSeletced.getProvincenm();
		provincenm = Utility.UrlTranslateToUTF(provincenm);
		String address = "http://restapi.amap.com/v3/config/district?keywords="+provincenm+"&subdistrict=1&key=8eee051d08bc42cfc33dbd065a87d60e";
		
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				// TODO Auto-generated method stub
				boolean result = false;
				result = Utility.handleCityResponse(shushuWeatherDB, response);
				
				if(result)
				{
					//通过runOnUiThread回到主线程处理逻辑
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							closeProgressDialog();
							queryCity();
						}
					});
				}
				else
				{
					//通过runOnUiThread回到主线程处理逻辑
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							closeProgressDialog();
							Toast.makeText(ChooseAreaActivity.this, "获取市信息失败！", Toast.LENGTH_SHORT).show();
						}
					});
				}				
			}
			
			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	//从服务器上获取所有省
	private void queryProvincesfromServer()
	{
		showProgressDialog();
		String address = " http://restapi.amap.com/v3/config/district?subdistrict=1&key=8eee051d08bc42cfc33dbd065a87d60e";
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				// TODO Auto-generated method stub
				boolean result = false;
				
				result = Utility.handleProvinceResponse(shushuWeatherDB, response);
				
				if(result)
				{
					//通过runOnUiThread回到主线程处理逻辑
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							closeProgressDialog();
							queryProvinces();
						}
					});
				}
				else
				{
					//通过runOnUiThread回到主线程处理逻辑
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							closeProgressDialog();
							Toast.makeText(ChooseAreaActivity.this, "获取省信息失败！", Toast.LENGTH_SHORT).show();
						}
					});
				}				
			}
			
			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	//显示进度对话框
	private void showProgressDialog()
	{
		if(progressDialog ==null)
		{
			progressDialog = new ProgressDialog(this);
			
			progressDialog.setMessage("正在加载中...");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		
		progressDialog.show();
	}
	
	//关闭进度对话框
	private void closeProgressDialog()
	{
		if(progressDialog!=null)
		{
			progressDialog.dismiss();
		}
	}
	
	//重写Back按键事件，根据当前的级别判断，此时应该返回省，市，区县列表还是退出
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(currentLevel==LEVEL_MUNICIPALITY)
		{
			queryProvinces();
		}
		else if(currentLevel==LEVEL_COUNTY)
		{
			queryCity();
		}
		else
		{
			finish();
		}
	}
}
