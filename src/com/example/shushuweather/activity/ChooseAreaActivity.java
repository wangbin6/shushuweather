package com.example.shushuweather.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.shushuweather.db.ShushuWeatherDB;
import com.example.shushuweather.models.City;
import com.example.shushuweather.utils.HttpCallbackListener;
import com.example.shushuweather.utils.HttpUtil;
import com.example.shushuweather.utils.Utility;
import com.example.shushuweather.R;
import android.app.Activity;
import android.app.DownloadManager.Query;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseAreaActivity extends Activity {
	
	private TextView titleText;//标题
	private ListView listView;
	private ArrayAdapter<String> adapter;
	private ShushuWeatherDB shushuWeatherDB;
	private List<City> provinceList;//省列表
	private List<City> municipalityList;//市列表
	private List<City> countyList;//区县列表
	private List<String> dataList = new ArrayList<String>();
	private int currentLevel;//当前选择的市省，市或者县
	private static final int LEVEL_PROVINCE = 0;//省
	private static final int LEVEL_CITY = 1;//市
	private static final int LEVEL_MUNICIPALITY = 2;//区县
	private ProgressDialog progressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		
		titleText = (TextView)findViewById(R.id.title_text);
		listView = (ListView)findViewById(R.id.list_view);
		
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,dataList);
		
		listView.setAdapter(adapter);
		
		shushuWeatherDB = ShushuWeatherDB.getinstance(this);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				
			}
		});
		
		queryProvinces();//第一次加载省份数据
	}
	
	//获取所有的中国的省
	private void queryProvinces()
	{
		provinceList = shushuWeatherDB.getAllProvinces();
		
		if(provinceList.size()>0)
		{
			dataList.clear();
			
			for(City province:provinceList)
			{
				dataList.add(province.getProvince());
			}
			
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText("中国");
			currentLevel = LEVEL_PROVINCE;
		}
		else
		{
			//Toast.makeText(ChooseAreaActivity.this, "暂无省份信息", Toast.LENGTH_SHORT).show();
			queryProvincesfromServer();
		}
	}
	
	//从服务器上获取城市列表
	private void queryProvincesfromServer()
	{
		showProgressDialog();
		String address = "http://api.k780.com/?app=weather.city&cou=1&appkey=26776&sign=42d21df6df1c8068dbd225379b10ac98&format=json";
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
							queryProvinces();
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
}
