package com.wootalk.inject;

import android.util.Log;

import org.jsoup.Connection;

import model.JavascriptHelper;

/**
 * Created by JimmyJhang on 2016/3/18.
 */
public class BaseHandler {

    private final BaseHandler mNextHandler;
    private JavascriptHelper mHelper;
    protected JavascriptHelper.FinishCallback mFinishCallback = new JavascriptHelper.FinishCallback() {
        @Override
        public void onFinish(Object result) {
            Log.d("onPageFinished", "the command is finished:" + result);
            doNext(mHelper);
        }
    };

    public BaseHandler(BaseHandler next){
        mNextHandler = next;
    }

    void doNext(JavascriptHelper helper){
        if (mNextHandler != null){
            mNextHandler.next(helper);
        }
    }

    protected void call(String javascriptFunc, String waitForSelector){
        mHelper.callAndWaitForSpecifiedSelector(javascriptFunc, waitForSelector, mFinishCallback);
    }
    public void next(JavascriptHelper instructor){
        mHelper = instructor;
    }

}
