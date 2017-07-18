package com.example.shushuweather.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import com.example.shushuweather.utils.MyData;

/**
 * 监听网络状态，实时更新MyData中的网络标识，确保APP知道当前的网络状况
 * @author WangBin
 *
 */
public class NetworkChangedReceiver extends BroadcastReceiver {
	private MyData mydata;
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		mydata = (MyData)context.getApplicationContext();
		if(ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction()))
		{
			Log.d("NetWork", intent.getAction());
			
			ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkinfo = connectivityManager.getActiveNetworkInfo();
			
			if(networkinfo!=null)
			{
				//尝试去测试网络可畅通
				if(networkinfo.isConnected())
				{
					if(networkinfo.getType()==ConnectivityManager.TYPE_WIFI)
					{
						//wifi
						mydata.setNetworkIsOk(true);
						mydata.setNetworkType(2);
					}
					else if(networkinfo.getType()==ConnectivityManager.TYPE_MOBILE)
					{
						//GPRS
						mydata.setNetworkIsOk(true);
						mydata.setNetworkType(1);
					}
				}
				else
				{
					Log.d("Network", "网络不可用");
				}
			}
			else
			{
				Log.d("Network", "网络不可用");
			}
		}
	}

}
