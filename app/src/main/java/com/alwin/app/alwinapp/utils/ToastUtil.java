package com.alwin.app.alwinapp.utils;


import android.content.Context;
import android.widget.Toast;

/**
 * Toast工具类
 * 
 * @author renpeng.ben
 * @date 2013-12-13
 */
public class ToastUtil {

	/**
	 * 显示短时间的Toast
	 * @param context
	 * @param msg
	 */
	public static void showSortToast(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
	
	
	/**
	 * 显示长时间的Toast
	 * @param context
	 * @param msg
	 */
	public static void showLongToast(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}
}
