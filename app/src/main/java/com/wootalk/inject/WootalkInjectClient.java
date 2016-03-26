package com.wootalk.inject;

import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import model.JavascriptHelper;


/**
 * Created by JimmyJhang on 2016/3/18.
 */
public class WootalkInjectClient extends WebViewClient{
    private final WebView mWebView;
    private static final String URL_WOOTALK = "https://wootalk.today/key/%E6%88%90%E4%BA%BA%E6%A8%A1%E5%BC%8F";
    private final JavascriptHelper mJavascriptHelper;
    private final RobotActionPlayManager mPM;
    private final Settings mSettings;
    //  private Activity mActivity;
    private Runnable mLoadMainTask = new Runnable() {
        @Override
        public void run() {
            mWebView.loadUrl(URL_WOOTALK);

        }
    };
    public WootalkInjectClient(WebView view, Settings settings) {
        mWebView = view;
        mSettings = settings;

        mJavascriptHelper = new JavascriptHelper(view);
        mWebView.postDelayed(mLoadMainTask, 0);

        mPM = new RobotActionPlayManager(mJavascriptHelper, settings);
        mPM.init();

    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Log.d("shouldOverrideUrl", url);
        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        Log.d("onPageFinished ", ""+url);
        if (mSettings.isSystemStarted()){
            mPM.play();
        }

    }

    public boolean isRunning(){
        return mPM.isRunning();
    }



    public void setOnStateChangeListener(RobotActionPlayManager.OnStateChangeListener stateChangeListener){
        mPM.setOnStateChangeListener(stateChangeListener);
    }


    public void start(boolean b) {
        mPM.start(b);
    }

    public void reloadWebView() {
        mJavascriptHelper.reload();
    }
}
