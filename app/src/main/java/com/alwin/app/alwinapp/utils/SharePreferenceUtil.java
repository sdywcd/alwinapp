package com.alwin.app.alwinapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * sharePreferenceUtils
* @ClassName: SharePreferenceUtil 
* @Description: TODO
* @author jcchen
* @date 2014-11-26 上午11:58:25 
*
 */
public class SharePreferenceUtil {

	private static String ALWIN="alwin";
	//会员信息
	public static String HOST = "host";

	public static String PORT="port";

	public static String COOKIES="cookies";

	public static String USERNAME="username";

	public static String PASSWORD="password";


	public static void saveUserName(Context context,String username){
		SharedPreferences sPreferences = context.getSharedPreferences(
				ALWIN, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = sPreferences.edit();
		editor.putString(USERNAME,username);
		editor.commit();
	}


	public static String getUserName(Context context){
		SharedPreferences sPreferences = context.getSharedPreferences(
				ALWIN, Activity.MODE_PRIVATE);
		return sPreferences.getString(USERNAME,"");

	}

	public static void savePassword(Context context,String password){
		SharedPreferences sPreferences = context.getSharedPreferences(
				ALWIN, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = sPreferences.edit();
		editor.putString(PASSWORD,password);
		editor.commit();
	}

	public static String getPassword(Context context){
		SharedPreferences sPreferences = context.getSharedPreferences(
				ALWIN, Activity.MODE_PRIVATE);
		return sPreferences.getString(PASSWORD,"");

	}
	public static void saveHost(Context context,String host){
		SharedPreferences sPreferences = context.getSharedPreferences(
				ALWIN, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = sPreferences.edit();
		editor.putString(HOST,host);
		editor.commit();
	}
	

	public static String getHost(Context context){
		SharedPreferences sPreferences = context.getSharedPreferences(
				ALWIN, Activity.MODE_PRIVATE);
		return sPreferences.getString(HOST,"");
		
	}

	public static void saveCookie(Context context,String cookies){
		SharedPreferences sPreferences = context.getSharedPreferences(
				ALWIN, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = sPreferences.edit();
		editor.putString(COOKIES,cookies);
		editor.commit();
	}


	public static String getCookies(Context context){
		SharedPreferences sPreferences = context.getSharedPreferences(
				ALWIN, Activity.MODE_PRIVATE);
		return sPreferences.getString(COOKIES,"");
	}

	public static void savePort(Context context,String port){
		SharedPreferences sPreferences = context.getSharedPreferences(
				ALWIN, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = sPreferences.edit();
		editor.putString(PORT,port);
		editor.commit();
	}


	public static String getPort(Context context){
		SharedPreferences sPreferences = context.getSharedPreferences(
				ALWIN, Activity.MODE_PRIVATE);
		return sPreferences.getString(PORT,"");

	}

	/**
	 * 清空share
	 * 
	 * @method: remove void
	 * @throws
	 */
	public static void remove(Context context) {
		SharedPreferences sPreferences = context.getSharedPreferences(
				ALWIN, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = sPreferences.edit();
		editor.clear();
		editor.commit();
	}
	/**
	 * 清楚share
	 * @param context
	 * @param key
	 */
	public static void remove(Context context, String key) {
		SharedPreferences sPreferences = context.getSharedPreferences(
				ALWIN, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = sPreferences.edit();
		editor.remove(key);
		editor.commit();
	}
}
