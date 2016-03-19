package com.wootalk.inject;

import android.util.Log;

import model.JavascriptHelper;

/**
 * Created by Chang on 2016/3/18.
 */
public class RobotActionPlayManager implements PlayContext {


    private final JavascriptHelper mJavascriptHelper;
    private final int RESTART_DELAY = 1000;
    private final Settings mSettings;
    private BaseHandler mPrevHandler;
    private BaseHandler mStartHandler;

    private boolean mAskForStop;
    private boolean mTruelyStop;
    private OnStateChangeListener mOnStateChangeListener;

    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener){
        mOnStateChangeListener = onStateChangeListener;
    }


    public interface OnStateChangeListener{
        void onStateChanged(String handlerName, String stateName);
    }
    public RobotActionPlayManager(JavascriptHelper javascriptHelper, Settings settings) {
        mJavascriptHelper = javascriptHelper;
        mSettings = settings;
    }

    public BaseHandler add(BaseHandler handler){
        if (mPrevHandler != null){
            mPrevHandler.add(handler);
        }else{
            mPrevHandler = handler;
            mStartHandler = handler;
        }
        return mPrevHandler;
    }

    public void init(){


        BaseHandler talkOrQuitdecision =
                this.add(new StartChatHandler(this)).add(new CheckIfExitHandler(this, false));

        talkOrQuitdecision.fail(new ChangePersonHandler(this, null));


        //talkOrQuitdecision.fail(new ChangePersonHandler(null, this));

        BaseHandler checkIfQuit = talkOrQuitdecision.add(new WaitForResponseHandler(this))
                                                    .fail(new ChangePersonHandler(this, null))
                                                    .add(new SendTextHandler(this, ""))
                                                    .add(new CheckIfExitHandler(this));
        checkIfQuit.fail(new ChangePersonHandler(this, null));

    }
    public void play() {
        mTruelyStop = false;
        mAskForStop = false;
        if (mOnStateChangeListener != null){
            mOnStateChangeListener.onStateChanged(getClass().getName(), PlayContext.STATE_START);
        }

        mStartHandler.next(mJavascriptHelper);
    }

    @Override
    public void replay() {
        mJavascriptHelper.postDelayed(new Runnable() {
            @Override
            public void run() {
                mStartHandler = null;
                mPrevHandler = null;

                mJavascriptHelper.reload();

                init();

                play();
            }
        }, RESTART_DELAY);

    }

    @Override
    public Settings getSettings() {
        return mSettings;
    }

    @Override
    public void reportState(String handlerName, String stateName) {
        mTruelyStop = (stateName .equals(PlayContext.STATE_STOP));
        if (mOnStateChangeListener != null){
            Log.d("reportState", handlerName + ":"+ stateName);
            mOnStateChangeListener.onStateChanged(handlerName, stateName);
        }


    }

    @Override
    public boolean canPlayNext() {
        return !mAskForStop;
    }

    public void start(boolean isStart) {
        if (!isStart){
            stop();
        }else{
            replay();
        }
    }


    private void stop(){
        mAskForStop = true;
    }

    public boolean isRunning() {
        return !mTruelyStop;
    }
}
