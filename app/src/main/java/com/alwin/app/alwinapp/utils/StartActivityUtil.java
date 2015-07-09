package com.alwin.app.alwinapp.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Activity启动util
 * 
 * @author renpeng.ben
 * @date 2013-12-16
 */
public class StartActivityUtil {

	public static void start(Activity context, Class<?> cls) {
		Intent intent = new Intent();
		intent.setClass(context, cls);
		context.startActivity(intent);
	//	ActivityTransitionUtil.startActivityTransition(context);
	}

	public static void start(Activity context, Class<?> cls, Bundle bundle) {
		Intent intent = new Intent();
		intent.putExtras(bundle);
		intent.setClass(context, cls);

		context.startActivity(intent);
	//	ActivityTransitionUtil.startActivityTransition(context);
	}

	public static void goToHomeActivity(Activity context, Class<?> cls) {
		context.finish();
		Intent intent = new Intent();
		intent.setClass(context, cls);
		context.startActivity(intent);
	//	ActivityTransitionUtil.startActivityTransition(context);
	}

	public static void goToHomeActivity(Activity context, Class<?> cls,Bundle bundle) {
		context.finish();
		Intent intent = new Intent();
		intent.setClass(context, cls);
		context.startActivity(intent);
	//	ActivityTransitionUtil.startActivityTransition(context);
	}
}
