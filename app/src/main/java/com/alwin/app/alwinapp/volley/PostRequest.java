package com.alwin.app.alwinapp.volley;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import android.app.Dialog;
import android.content.DialogInterface;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

/**
 * Form Post
 * 
 * @author renpengben
 * @date 2013-12-6
 */
public class PostRequest extends Request<String> implements
		DialogInterface.OnCancelListener {
	private final static String TAG = "PostRequest";
	private Listener<String> mListener;
	private ErrorListener mErrorListener;
	private Map<String, String> mParams;
	private Map<String, String> mHeaders;

	private Dialog mDialog;

	public PostRequest(String url, Map<String, String> params,
			Map<String, String> headers, Dialog dialog,
			Listener<String> listener, ErrorListener errorListener) {
		super(Method.POST, url, errorListener);
		this.mParams = params;
		this.mHeaders = headers;
		this.mDialog = dialog;
		this.mListener = listener;
		this.mErrorListener = errorListener;
		if (mDialog != null) {
			mDialog.show();
			mDialog.setOnCancelListener(this);
		}
	}

	public PostRequest(String url, Map<String, String> params, Dialog dialog,
			Listener<String> listener, ErrorListener errorListener) {
		this(url, params, null, dialog, listener, errorListener);
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		try {

			String jsonString = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));

			return Response.success(jsonString,
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		}
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		return mHeaders != null ? mHeaders : super.getHeaders();
	}

	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		return mParams != null ? mParams : super.getParams();
	}

	@Override
	public void deliverError(VolleyError error) {
		if (mDialog != null)
			mDialog.dismiss();
		mErrorListener.onErrorResponse(error);
	}

	@Override
	protected void deliverResponse(String response) {
		if (mDialog != null)
			mDialog.dismiss();
		mListener.onResponse(response);
	}

	@Override
	public void onCancel(DialogInterface arg0) {
		this.cancel();
	}

}
