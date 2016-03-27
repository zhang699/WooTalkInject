package com.wootalk.inject;

import com.wootalk.model.JavascriptHelper;

/**
 * Created by Chang on 2016/3/20.
 */
public class CheckTargetIdleHandler extends BaseHandler {

    public CheckTargetIdleHandler(PlayContext playContext) {
        super(playContext);
    }

    @Override
    public void next(final JavascriptHelper instructor) {
        super.next(instructor);
        int delay = mPContext.getSettings().getWaitForResponseDelayAfterOpening();
        startCheckSpecificTask("", "", delay, new OnCheckResult() {
            @Override
            public void onResult(Object result, Runnable task) {
                if (result == null){
                    nextToFailHandler();
                }else{
                    next(instructor);
                }
            }
        });
    }
}
