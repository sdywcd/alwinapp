package com.alwin.app.alwinapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 检测网络工具类
 * 
 * @ClassName:NetWorkUtil
 * @Description: 包含检测网络的相关方法
 * @Author:James
 * @Date:2013-12-17 上午11:00:15
 * @Version V1.0
 */
public class NetWorkUtil {
	/**
	 * 检测当的网络（WLAN、3G/2G）状态
	 * 
	 * @method: isNetworkAvailable
	 * @param context
	 * @return boolean
	 * @throws
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager != null) {
			NetworkInfo networkInfo = connectivityManager
					.getActiveNetworkInfo();
			if (networkInfo != null && networkInfo.isConnected()) {
				if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}
		return false;

	}
}
