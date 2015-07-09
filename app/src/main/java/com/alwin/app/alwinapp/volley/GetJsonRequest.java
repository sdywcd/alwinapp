package com.alwin.app.alwinapp.volley;

import android.app.Dialog;
import android.content.DialogInterface;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.util.Map;


/**
 * Get JSON 请求
 * 
 * @author renpeng.ben
 * @date 2013-12-6
 * @param <T>
 */
public class GetJsonRequest extends Request<String> implements
		DialogInterface.OnCancelListener {

	/**
	 * 请求头
	 */
	private Map<String, String> mHeaders;

	/**
	 * 加载dialog
	 */
	private Dialog mDialog;
	/**
	 * Response Listener
	 */
	protected final Listener<String> mListener;
	protected final ErrorListener mErrorListener;

	public GetJsonRequest(String url, Map<String, String> mHeaders,
			Dialog mDialog, Listener<String> mListener,
			ErrorListener mErrorListener) {
		super(Method.GET, url, mErrorListener);
		this.mDialog = mDialog;
		this.mHeaders = mHeaders;
		this.mErrorListener = mErrorListener;
		this.mListener = mListener;

		if (mDialog != null) {
			mDialog.show();
			mDialog.setOnCancelListener(this);
		}
	}

	public GetJsonRequest(String url, Dialog mDialog,
			Listener<String> mListener, ErrorListener mErrorListener) {
		this(url, null, mDialog, mListener, mErrorListener);
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		return mHeaders != null ? mHeaders : super.getHeaders();
	}

	/**
	 * 成功时的回调方法
	 */
	@Override
	protected void deliverResponse(String response) {
		if (mDialog != null)
			mDialog.dismiss();

		mListener.onResponse(response);
		Log.d(VolleyHttpClient.TAG, String.format("GET-response:%s", response));
	}

	@Override
	public void deliverError(VolleyError error) {
		if (mDialog != null)
			mDialog.dismiss();
		mErrorListener.onErrorResponse(error);

	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {

		try {
			String jsonString =

			new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
			return Response.success(jsonString,
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		}
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		this.cancel();
	}

}
