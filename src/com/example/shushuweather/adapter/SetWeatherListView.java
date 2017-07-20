package com.example.shushuweather.adapter;

import java.util.HashMap;
import java.util.List;

import com.example.shushuweather.R;
import com.example.shushuweather.models.SetWeatherItem;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 自定义天气配置ListView
 * @author WangBin
 *
 */
public class SetWeatherListView extends BaseAdapter {
	
	private List dataList;//存储数据的集合
	private LayoutInflater mInflater;
	
	public SetWeatherListView(Context context, List<SetWeatherItem> list) {
		// TODO Auto-generated constructor stub
		this.dataList = list;
		this.mInflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		Log.d("getCount", "getCount");
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder vh=null;
		
		if(convertView==null)
		{
			convertView = mInflater.inflate(R.layout.set_weather_list_item, parent,false);//加载布局
			vh = new ViewHolder();
			
			vh.itemText = (TextView)convertView.findViewById(R.id.itemText);
			vh.itemImage= (ImageView)convertView.findViewById(R.id.itemImage);
			
			convertView.setTag(vh);
			
		}
		else
		{
			vh = (ViewHolder)convertView.getTag();
		}
		
		SetWeatherItem sItem = (SetWeatherItem) dataList.get(position);
		
		vh.itemText.setText(sItem.getItemText());
		vh.itemImage.setImageResource(sItem.getItemImage());

		return convertView;
	}
	
	public class ViewHolder
	{
		TextView itemText;
		ImageView itemImage;
	}

}
