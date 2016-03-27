package com.wootalk.inject;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.wootalk.model.JavascriptHelper;

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
    private List<BaseHandler> mExceptionHandlers = new ArrayList<BaseHandler>();
    private FinishHandler mFinishHandler;

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


        mExceptionHandlers.clear();
        BaseHandler checkIfExit = new CheckIfExitHandler(this);
        checkIfExit.fail(new ChangePersonHandler(this, null));

        BaseHandler checkTargetFirstMessage = new WaitForTargetInitResponseHandler(this);
        checkTargetFirstMessage.fail(new ChangePersonHandler(this, null));

        BaseHandler checkActionBlocking = new CheckIfActionBlockingHandler(this);

        mExceptionHandlers.add(checkIfExit);
        mExceptionHandlers.add(checkTargetFirstMessage);
        mExceptionHandlers.add(checkActionBlocking);
        //mExceptionHandlers.add(mFinishHandler);

        BaseHandler mChangePeronHandler = new ChangePersonHandler(this, null);
        mFinishHandler = new FinishHandler(this);

        BaseHandler talkOrQuitdecision =
                this.add(new StartChatHandler(this))
                    .add(new WaitForConnectionCompletedHandler(this))
                    .fail(mChangePeronHandler);


        String openingSentence = mSettings.getOpeningSentence();
        String personalitySentence = mSettings.getPersonalityOpeningSentence();



        //talkOrQuitdecision.fail(new ChangePersonHandler(null, this));

        BaseHandler checkIfQuit = talkOrQuitdecision.add(new WaitForFirstResponseHandler(this))
                                                    .fail(mChangePeronHandler)
                                                    .add(new SendTextHandler(this, openingSentence))
                                                    .add(new WaitForTargetAnswerHandler(this))
                                                    .fail(mChangePeronHandler)
                                                    .add(new SendTextHandler(this, personalitySentence))
                                                    .add(new WaitForTargetAnswerHandler(this))
                                                    .fail(mChangePeronHandler)
                                                    .add(mFinishHandler);

        executeCallback(getClass().getName(), PlayContext.STATE_INITIALED);
    }

    private void executeCallback(String stateName, String state){
        if (mOnStateChangeListener != null){
            mOnStateChangeListener.onStateChanged(stateName, state);
        }
    }
    public void play() {
        mTruelyStop = false;
        mAskForStop = false;

        executeCallback(getClass().getName(), PlayContext.STATE_START);

        mStartHandler.next(mJavascriptHelper);

        for (BaseHandler h : mExceptionHandlers){
            h.next(mJavascriptHelper);
        }
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
