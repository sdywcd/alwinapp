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
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

/**
 * JSON Post
 * 
 * @author renpengben
 * @date 2013-12-6
 */
public class PostJsonRequest extends Request<String> implements
		DialogInterface.OnCancelListener {
	private static final String PROTOCOL_CHARSET = "utf-8";

	private static final String PROTOCOL_CONTENT_TYPE = String.format(
			"application/json; charset=%s", PROTOCOL_CHARSET);

	private Listener<String> mListener;
	private ErrorListener mErrorListener;
	private String mRequestBody;
	private Map<String, String> mHeaders;

	private Dialog mDialog;

	public PostJsonRequest(String url, Map<String, String> mHeaders,
			String requestBody, Dialog mDialog, Listener<String> mListener,
			ErrorListener mErrorListener) {
		super(Method.POST, url, mErrorListener);
		this.mListener = mListener;
		this.mErrorListener = mErrorListener;
		this.mRequestBody = requestBody;
		this.mHeaders = mHeaders;
		this.mDialog = mDialog;

		if (mDialog != null) {
			mDialog.show();
			mDialog.setOnCancelListener(this);
		}
	}

	public PostJsonRequest(String url, String requestBody, Dialog mDialog,
			Listener<String> mListener, ErrorListener mErrorListener) {
		this(url, null, requestBody, mDialog, mListener, mErrorListener);
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
	public String getBodyContentType() {
		return PROTOCOL_CONTENT_TYPE;
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		return mHeaders != null ? mHeaders : super.getHeaders();
	}

	@Override
	public byte[] getBody() {
		try {
			Log.v("RequestBody", mRequestBody);
			return mRequestBody == null ? null : mRequestBody
					.getBytes(PROTOCOL_CHARSET);
		} catch (UnsupportedEncodingException uee) {
			VolleyLog
					.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
							mRequestBody, PROTOCOL_CHARSET);
			return null;
		}
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
	public void onCancel(DialogInterface dialog) {
		this.cancel();
	}

}
