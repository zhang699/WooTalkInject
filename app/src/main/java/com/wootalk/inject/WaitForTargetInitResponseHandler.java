package com.wootalk.inject;

import android.util.Log;

import model.JavascriptHelper;

/**
 * Created by Chang on 2016/3/18.
 */

public class WaitForTargetInitResponseHandler extends BaseHandler {
    private int mWaitSec = 12;

    public WaitForTargetInitResponseHandler(PlayContext context) {
        super(context);
    }

    private static final int CHECKING_INTERVAL = 1000;
    @Override
    public void next(final JavascriptHelper instructor) {
        super.next(instructor);
        mWaitSec = mPContext.getSettings().getWaitForResponseDelay();
        instructor.postDelayed(new Runnable() {
            @Override
            public void run(){
                setFinished();
                call("", "");

            }
        }, mWaitSec * 1000);


        OnCheckResult callback = new OnCheckResult(){
            @Override
            public void onResult(Object result, Runnable task) {
                if (result != null){
                    Log.d("instructor.postDelay", "goFailHandler");

                    setFinished();
                    nextToFailHandler();

                }else{
                    //next(instructor);
                    Log.d("instructor.postDelay", "keepChecking");
                    if (!isFinished()){
                        instructor.postDelayed(task, CHECKING_INTERVAL);
                    }

                }
            }
        };
        String strangerMsgSelector = String.format(ActionElementSelector.SELECTOR_STRANGER_MESSAGES, 0);
        startCheckSpecificTask("", strangerMsgSelector, 0, callback);

    }
}
