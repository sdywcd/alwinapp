package com.alwin.app.alwinapp.ui.activity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.alwin.app.alwinapp.R;

/**
 * Created by sdywcd on 15/6/12.
 */

public class MyWebView extends WebView {
    boolean mIgnoreTouchCancel;

    private ProgressBar progressbar;

    public ProgressBar getProgressbar() {
        return progressbar;
    }

    public void setProgressbar(ProgressBar progressbar) {
        this.progressbar = progressbar;
    }

    public void ignoreTouchCancel(boolean val) {
        mIgnoreTouchCancel = val;
    }

    public MyWebView(Context context) {
        super(context);
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        progressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,15, 0, 0));
        progressbar.setProgressDrawable(getResources().getDrawable(R.drawable.progress_bar_states));
        addView(progressbar);
        //        setWebViewClient(new WebViewClient(){});
        //setWebChromeClient(new WebChromeClient());
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean mIgnoreTouchCancel=false;


        int action = ev.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            onScrollChanged(getScrollX(), getScrollY(), getScrollX(), getScrollY());
        }
        // 第一种不完美解决方案

        boolean ret = super.onTouchEvent(ev);
        if (mIgnoreTouchCancel) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    requestDisallowInterceptTouchEvent(true);
                    ret = true;
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    requestDisallowInterceptTouchEvent(false);
                    mIgnoreTouchCancel = false;
                    break;
            }
        }
        return ret;

        // 第二种完美解决方案
        //return action == MotionEvent.ACTION_CANCEL && mIgnoreTouchCancel || super.onTouchEvent(ev);
    }




    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressbar.setVisibility(GONE);
            } else {
                if (progressbar.getVisibility() == GONE)
                    progressbar.setVisibility(VISIBLE);
                progressbar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }

    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        progressbar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }
}

