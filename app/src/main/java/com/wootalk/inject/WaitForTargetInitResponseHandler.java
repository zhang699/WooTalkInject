package com.wootalk.inject;

import android.util.Log;

import model.JavascriptHelper;

/**
 * Created by Chang on 2016/3/18.
 */

public class WaitForTargetInitResponseHandler extends BaseHandler {


    public WaitForTargetInitResponseHandler(PlayContext context) {
        super(context);
    }

    private static final int CHECKING_INTERVAL = 500;
    @Override
    public void next(final JavascriptHelper instructor) {
        super.next(instructor);



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
