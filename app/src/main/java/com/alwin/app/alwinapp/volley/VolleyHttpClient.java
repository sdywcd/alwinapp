package com.alwin.app.alwinapp.volley;

import android.app.Dialog;
import android.content.Context;

import com.alwin.app.alwinapp.utils.NetWorkUtil;
import com.alwin.app.alwinapp.utils.ToastUtil;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * VolleyHttpClient
 * 
 * @author renpeng.ben
 * @date 2013-12-13s
 */
public class VolleyHttpClient {

	public static final String TAG = "VolleyHttpClient";

	private static Context mContext;

	private static RequestQueue mRequestQueue;

	private static VolleyHttpClient sInstance = null;

	private VolleyHttpClient(Context context) {
		mContext = context;
		this.init();
	}

	/**
	 * 获取 VolleyUtils
	 * 
	 * @param context
	 * @return
	 */
	public static VolleyHttpClient getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new VolleyHttpClient(context);
		} else {
			mContext = context;
		}
		return sInstance;
	}

	/**
	 * 初始化 RequestQueue
	 * 
	 *
	 */
	private void init() {
		mRequestQueue = Volley.newRequestQueue(mContext);
	}

	/**
	 * 获取 RequestQueue
	 * 
	 * @return RequestQueue
	 */
	private RequestQueue getRequestQueue() {
		if (mRequestQueue != null) {
			this.init();
		}
		return mRequestQueue;
	}

	@SuppressWarnings("rawtypes")
	public Request postJson(String url, String requestBody,
			Map<String, String> heads, Dialog dialog,
			Listener<String> listener, ErrorListener errorListener) {

		if (NetWorkUtil.isNetworkAvailable(mContext)) {

			PostJsonRequest request = new PostJsonRequest(url, heads,
					requestBody, dialog, listener, errorListener);
			setRequestRetryPolicy(request);
			return getRequestQueue().add(request);
		} else {

			ToastUtil.showSortToast(mContext, "网络不可用,请检测网络连接!");
			return null;
		}

	}

	@SuppressWarnings("rawtypes")
	public Request post(String url, Map<String, String> params,
			Map<String, String> heads, Dialog dialog,
			Listener<String> listener, ErrorListener errorListener) {
		if (NetWorkUtil.isNetworkAvailable(mContext)) {
			PostRequest postRequest = new PostRequest(url, params, heads,
					dialog, listener, errorListener);
			setRequestRetryPolicy(postRequest);
			return getRequestQueue().add(postRequest);
		} else {
			ToastUtil.showSortToast(mContext, "网络不可用,请检测网络连接!");
			return null;
		}
	}

	@SuppressWarnings("rawtypes")
	public Request post(String url, Map<String, String> params, Dialog dialog,
			Listener<String> listener, ErrorListener errorListener) {
		if (NetWorkUtil.isNetworkAvailable(mContext)) {
			PostRequest postRequest = new PostRequest(url, params, null,
					dialog, listener, errorListener);
			setRequestRetryPolicy(postRequest);
			return getRequestQueue().add(postRequest);
		} else {
			ToastUtil.showSortToast(mContext, "网络不可用,请检测网络连接!");
			return null;
		}
	}

	@SuppressWarnings("rawtypes")
	public Request getJson(String url, Map<String, String> params,
			Map<String, String> mHeaders, Dialog mDialog,
			Listener<String> mListener, ErrorListener mErrorListener) {

		if (NetWorkUtil.isNetworkAvailable(mContext)) {

			StringBuffer urlBuffer = new StringBuffer();
			urlBuffer.append(url).append("?")
					.append(encodeParameters(params));
			GetJsonRequest jsonRequest = new GetJsonRequest(
					urlBuffer.toString(), mHeaders, mDialog, mListener,
					mErrorListener);
			setRequestRetryPolicy(jsonRequest);
			return getRequestQueue().add(jsonRequest);
		} else {
			ToastUtil.showSortToast(mContext, "网络不可用,请检测网络连接!");
			return null;
		}

	}

	@SuppressWarnings("rawtypes")
	public Request getJson(String url, Map<String, String> params,
			Dialog mDialog, Listener<String> mListener,
			ErrorListener mErrorListener) {

		if (NetWorkUtil.isNetworkAvailable(mContext)) {

			StringBuffer urlBuffer = new StringBuffer();
			urlBuffer.append(url).append("?")
					.append(encodeParameters(params));
			GetJsonRequest jsonRequest = new GetJsonRequest(
					urlBuffer.toString(), mDialog, mListener,
					mErrorListener);
			setRequestRetryPolicy(jsonRequest);
			return getRequestQueue().add(jsonRequest);
		} else {
			ToastUtil.showSortToast(mContext, "网络不可用,请检测网络连接!");
			return null;
		}

	}

	/**
	 * Map to url params
	 * 
	 *
	 * @return
	 */
	public String encodeParameters(Map<String, String> params) {
		if (params != null) {
			StringBuilder encodedParams = new StringBuilder();
			try {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					encodedParams.append(URLEncoder.encode(entry.getKey(),
							"UTF-8"));
					encodedParams.append('=');
					encodedParams.append(URLEncoder.encode(entry.getValue(),
							"UTF-8"));
					encodedParams.append('&');
				}
				return encodedParams.toString();
			} catch (UnsupportedEncodingException uee) {
				throw new RuntimeException(
						"Encoding not supported: " + "UTF-8", uee);
			}
		} else {
			return "";
		}
	}

	private void setRequestRetryPolicy(Request<?> request) {
		request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
	}


//	private boolean isLogin() {
//		User user = SharePreferenceUtil.readUser(mContext);
//		return user != null ? true : false;
//	}
//
//	private void goToLoginActivity() {
//		Intent intent = new Intent(mContext, AccountFragmentActivity.class);
//		mContext.startActivity(intent);
//	}
}
