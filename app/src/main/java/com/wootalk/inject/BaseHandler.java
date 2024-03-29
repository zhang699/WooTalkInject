package com.wootalk.inject;

import android.util.Log;

import com.wootalk.model.JavascriptHelper;

/**
 * Created by JimmyJhang on 2016/3/18.
 */
public class BaseHandler {

    protected final PlayContext mPContext;
    private BaseHandler mNextHandler;
    private BaseHandler mFailHandler;
    private boolean mIsFinished = false;
    private JavascriptHelper mHelper;
    protected JavascriptHelper.FinishCallback mFinishCallback = new JavascriptHelper.FinishCallback() {
        @Override
        public void onFinish(Object result) {
            Log.d("JavascriptHelper", "the command is finished:" + result);

            doNext(mNextHandler, mHelper);
        }
    };

    interface OnCheckResult {
        void onResult(Object result,Runnable task);
    }
    public BaseHandler(PlayContext pContext, BaseHandler next){
        this(pContext);
        mNextHandler = next;
    }

    public BaseHandler(PlayContext playContext){
        mPContext = playContext;
    }

    protected void reportState(String stateName){
        mPContext.reportState(getClass().getSimpleName(), stateName);
    }
    private void doNext(BaseHandler handler, JavascriptHelper helper){

        if (handler != null && mPContext.canPlayNext()){
            reportState(PlayContext.STATE_LOGGED);
            handler.next(helper);
        }else if (!mPContext.canPlayNext()){
            reportState(PlayContext.STATE_STOP);
        }
    }

    protected void nextHandler(){
        doNext(mNextHandler, mHelper);
    }

    protected void call(String javascriptFunc, String waitForSelector){
        callWithCallback(javascriptFunc, waitForSelector, mFinishCallback);
    }

    protected void setFinished(){
        mIsFinished = true;
    }

    protected boolean isFinished(){
        return mIsFinished;
    }
    protected void callWithCallback(String javascriptFunc, String waitForSelector, JavascriptHelper.FinishCallback finishCallback){

        if (mPContext.canPlayNext()){
            //Log.d("callWithCallback", "callWithCallback "+this.getClass().getName());
            mHelper.callAndWaitForSpecifiedSelector(javascriptFunc, waitForSelector, finishCallback);
        }else{
            reportState(PlayContext.STATE_STOP);
        }


    }

    protected void nextToFailHandler(){

        mIsFinished = true;
        doNext(mFailHandler, mHelper);


       // mHelper.callAndWaitForSpecifiedSelector(javascriptFunc, waitForSelector, mFailCallback);
    }


    public void next(JavascriptHelper instructor){
        mHelper = instructor;
        reportState(PlayContext.STATE_HANDLER_STARTING);
    }

    BaseHandler add(BaseHandler handler) {
        mNextHandler = handler;
        return mNextHandler;
    }

    BaseHandler fail(BaseHandler failHandler) {
        mFailHandler = failHandler;
        return this;
    }


    protected void startCheckSpecificTask(final String javascriptFunc, final String selector, long interval,
                                           final OnCheckResult callback ){
        mHelper.postDelayed(new Runnable() {
            @Override
            public void run() {
                final Runnable task = this;
                BaseHandler.this.callWithCallback(javascriptFunc, selector, new JavascriptHelper.FinishCallback() {
                    @Override
                    public void onFinish(Object result) {
                        callback.onResult(result, task);
                    }
                });
            }
        }, interval);
    }


    protected void startCheckSpecificTaskIfContainResult(final String javascriptFunc, final String selector, final long interval,
                                                         final OnCheckResult callback ){
      startCheckSpecificTask(javascriptFunc, selector, interval, new OnCheckResult() {
          @Override
          public void onResult(Object result, Runnable task) {
              if (result == null){
                  mHelper.postDelayed(task, interval);
              }else{
                  callback.onResult(result, task);
              }
          }
      });
    }






    /*void callWithCheckCallback (String javascriptFunc, String waitForSelector){
        Runnable task = this;

        callWithCallback(javascriptFunc, waitForSelector, new JavascriptHelper.FinishCallback() {
            @Override
            public void onFinish(Object result) {

            }
        });
    }*/
}
