package model;

import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.wootalk.inject.BaseHandler;
import com.wootalk.inject.ClickPassPhaseHandler;
import com.wootalk.inject.ClickSideViewHandler;


/**
 * Created by JimmyJhang on 2016/3/18.
 */
public class WootalkInjectClient extends WebViewClient{
    private final WebView mWebView;
    private static final String URL_WOOTALK = "https://wootalk.today/";
    private final JavascriptHelper mJavascriptHelper;
    private final BaseHandler mHandler;
    //  private Activity mActivity;
    private Runnable mLoadMainTask = new Runnable() {
        @Override
        public void run() {
            mWebView.loadUrl(URL_WOOTALK);

        }
    };
    public WootalkInjectClient(WebView view) {
        mWebView = view;
        mJavascriptHelper = new JavascriptHelper(view);
        mWebView.postDelayed(mLoadMainTask, 0);

        mHandler = new ClickSideViewHandler(new ClickPassPhaseHandler(null));
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
        //Log.d("onPageFinished ", );
        //view.loadUrl("javascript:$('#open-left').click()");

        mHandler.next(mJavascriptHelper);

        //mJavascriptHelper.changeWebView(view);
        //DeferredManager m = new AndroidDeferredManager();



        /*mJavascriptHelper.callAndWaitForSpecifiedSelector("$('.snap-drawer ul li:first-child a')[0].click()", "", finishCallback);
        mJavascriptHelper.callAndWaitForSpecifiedSelector("$('#keyInput').val('成人模式')", "", finishCallback);*/





    }


}
