package com.wootalk.inject;

import com.wootalk.model.JavascriptHelper;

/**
 * Created by Chang on 2016/3/26.
 */
public class CancelActionBlockingHandler extends BaseHandler {
    private static final long TIMEOUT_CHECKING = 2000;

    public CancelActionBlockingHandler(PlayContext playContext) {
        super(playContext);
    }

    @Override
    public void next(final JavascriptHelper instructor) {
        super.next(instructor);
        final long startCheckingTime = System.currentTimeMillis();
        startCheckSpecificTask("", ActionElementSelector.SELECTOR_CHANGE_PERSON,

                ActionElementSelector.DEFAULT_SELECTOR_CHECKING_TIMEOUT, new OnCheckResult() {
                    @Override
                    public void onResult(Object result, Runnable task) {
                        boolean isReachTimeout = System.currentTimeMillis() - startCheckingTime >TIMEOUT_CHECKING ;
                        if (isReachTimeout && result != null && mPContext.isCanCancelBlocking()){
                            call("$('.mfp-content').click()", "");
                        }
                        instructor.postDelayed(task, ActionElementSelector.DEFAULT_SELECTOR_CHECKING_TIMEOUT);

                    }
                }
        );

    }
}
