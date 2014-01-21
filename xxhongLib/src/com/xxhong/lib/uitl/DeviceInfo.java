package com.xxhong.lib.uitl;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
/**
 * 
 * @author xxhong
 *
 */
public class DeviceInfo {
	private static Context context;
	private DeviceInfo(Context context) {
		this.context = context;
	}
	private DeviceInfo(){}
	private static DeviceInfo deviceInfo;
	public static DeviceInfo getInstance(Context ct){
		if(deviceInfo==null){
			return new DeviceInfo(ct);
		}
		return deviceInfo;
	}
	/**
	 * 
	 * @return 当前连接的网络是WIFI,GPRS...
	 */
	public String getNetWorkName() {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		String typeName = info.getTypeName();
		return typeName;
	}
	/**
	 *  获取当前网络状态
	 * @return true 有网
	 */
	public boolean getNetWorkStates() {
		ConnectivityManager manager = (ConnectivityManager)context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		return (info != null && info.isConnected());
	}
	
}
