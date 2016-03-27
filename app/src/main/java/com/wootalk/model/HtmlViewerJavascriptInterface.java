package com.wootalk.model;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;

import java.util.HashMap;

/**
 * Created by JimmyJhang on 2016/3/18.
 */
public class HtmlViewerJavascriptInterface {

    interface Callback {
        void onResult (String result);
    }
    private class CallbackInfo{
        boolean isRemoveImmediately;
        Callback callback;
    }

    private HashMap<String, CallbackInfo> mHtmlResults = new HashMap<>();


    @JavascriptInterface
    public void showHTML(String id, String html){
        //Log.d("showHTMLId", id);
        //Log.d("showHTML", html);
        if (mHtmlResults.containsKey(id)){
            CallbackInfo callbackInfo = mHtmlResults.get(id);

            callbackInfo.callback.onResult(html);
            if (callbackInfo.isRemoveImmediately){
                mHtmlResults.remove(id);
            }

        }
    }

    public void addWaitCallback(String id, boolean isRemoveImmediately, Callback callback){
        CallbackInfo info = new CallbackInfo();
        info.isRemoveImmediately = isRemoveImmediately;
        info.callback = callback;
        mHtmlResults.put(id, info);
    }

    public void removeCallback(String id){
        mHtmlResults.remove(id);
    }


    public String waitForHtmlResult(String id) {
        return "";
    }
}
