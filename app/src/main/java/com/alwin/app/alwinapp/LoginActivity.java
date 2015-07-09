package com.alwin.app.alwinapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.alwin.app.alwinapp.ui.activity.LoadingUrlActivity;
import com.alwin.app.alwinapp.utils.ApiUrl;
import com.alwin.app.alwinapp.utils.SharePreferenceUtil;
import com.alwin.app.alwinapp.utils.StartActivityUtil;
import com.alwin.app.alwinapp.utils.ToastUtil;
import com.android.volley.NetworkResponse;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * 登录页面
 *
 * @author sdywcd
 */
public class LoginActivity extends Activity {
    private static String TAG = "LoginActivity";

    private EditText mUsername;

    private EditText mPassword;

    private Button mLoginBt;

    private EditText mHost;

    private EditText mPort;

    private String JSESSIONID;

    private String cookies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alwin_dologin);

        initView();

    }

    private void initView() {
        mHost = (EditText) findViewById(R.id.host);
        mPort = (EditText) findViewById(R.id.port);
        mUsername = (EditText) findViewById(R.id.username_et);
        mPassword = (EditText) findViewById(R.id.password_et);
        mLoginBt = (Button) findViewById(R.id.login_bt);

        mLoginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getLoginParam()) {
                    saveIpInfo();
                    //删除缓存的用户名和密码
                    //SharePreferenceUtil.remove(getApplicationContext(), SharePreferenceUtil.USERNAME);
                    //SharePreferenceUtil.remove(getApplicationContext(), SharePreferenceUtil.PASSWORD);
                    doLogin();
                }
            }
        });
        setIpInfoToView();
        //判定是否直接登陆
        String username=SharePreferenceUtil.getUserName(getApplicationContext());
        String password=SharePreferenceUtil.getPassword(getApplicationContext());
        if(StringUtils.isNotBlank(username)&&StringUtils.isNotBlank(password)){
            doLogin();
        }
    }


    private void setIpInfoToView() {
        String host = SharePreferenceUtil.getHost(getApplicationContext());
        mHost.setText(host);
        String port = SharePreferenceUtil.getPort(getApplicationContext());
        mPort.setText(port);
    }


    private boolean getLoginParam() {
        String username = mUsername.getText().toString();
        if (TextUtils.isEmpty(username)) {
            ToastUtil.showLongToast(getApplicationContext(), "请输入用户名／邮箱");
            return false;
        }
        String password = mPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            ToastUtil.showLongToast(getApplicationContext(), "请输入密码");
            return false;
        }
        SharePreferenceUtil.saveUserName(getApplicationContext(),username);
        SharePreferenceUtil.savePassword(getApplicationContext(),password);
        return true;
    }

    private void saveIpInfo() {
        String host = mHost.getText().toString();
        SharePreferenceUtil.saveHost(getApplicationContext(), host);
        String port = mPort.getText().toString();
        SharePreferenceUtil.savePort(getApplicationContext(), port);
    }



    private void doLogin() {
        String username=SharePreferenceUtil.getUserName(getApplicationContext());
        String password=SharePreferenceUtil.getPassword(getApplicationContext());
        if(StringUtils.isBlank(username)){
            username=mUsername.getText().toString().trim();
        }
        if(StringUtils.isBlank(password)){
            password=mPassword.getText().toString().trim();
        }
        String url = "http://" + SharePreferenceUtil.getHost(getApplicationContext()) + ":" + SharePreferenceUtil.getPort(getApplicationContext()) + ApiUrl.LOGIN_URL;
        RequestQueue requestQueue = Volley
                .newRequestQueue(getApplicationContext());
        Map<String, String> map = new HashMap<String, String>();
        map.put("username", username);
        map.put("password", password);
        JSONObject jsonObject = new JSONObject(map);
        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(
                Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        ToastUtil.showLongToast(getApplicationContext(),
                               "登陆成功正在跳转");
                        // goUrl();
                        Bundle bundle = new Bundle();
                        bundle.putString("name", mUsername.getText().toString());
                        StartActivityUtil.start(LoginActivity.this,
                                LoadingUrlActivity.class, bundle);
                        finish();
                        Log.d(TAG, "response -> " + response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage(), error);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=UTF-8");
                return headers;
            }
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response){
                Response<JSONObject> superResponse = super.parseNetworkResponse(response);
                Map<String, String> responseHeaders = response.headers;
                String rawCookies = responseHeaders.get("Set-Cookie");
                String part1 = substring(rawCookies, "", ";");
                cookies = rawCookies;
                SharePreferenceUtil.saveCookie(getApplicationContext(),cookies);
                return superResponse;
            }
        };

        requestQueue.add(jsonRequest);
    }

    public static String substring(String src, String fromString,
                                   String toString) {
        int fromPos = 0;
        if (fromString != null && fromString.length() > 0) {
            fromPos = src.indexOf(fromString);
            fromPos += fromString.length();
        }
        int toPos = src.indexOf(toString, fromPos);
        return src.substring(fromPos, toPos);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (this.getCurrentFocus() != null) {
                if (this.getCurrentFocus().getWindowToken() != null) {
                    imm.hideSoftInputFromWindow(this.getCurrentFocus()
                                    .getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
        return super.onTouchEvent(event);
    }

}
