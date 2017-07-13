package com.example.shushuweather.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.shushuweather.db.ShushuWeatherDB;
import com.example.shushuweather.models.City;

import com.example.shushuweather.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ChooseAreaActivity extends Activity {
	
	private TextView titleText;//标题
	private ListView listView;
	private ArrayAdapter<String> adapter;
	private ShushuWeatherDB shushuWeatherDB;
	private List<City> provinceList;//省列表
	private List<City> municipalityList;//市列表
	private List<City> countyList;//区县列表
	private List<String> dataList = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		
		titleText = (TextView)findViewById(R.id.title_text);
		listView = (ListView)findViewById(R.id.list_view);
		
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,dataList);
	}
}
