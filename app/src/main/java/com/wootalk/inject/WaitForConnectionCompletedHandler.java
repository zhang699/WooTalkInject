package com.wootalk.inject;

import model.JavascriptHelper;

/**
 * Created by Chang on 2016/3/23.
 */
public class WaitForConnectionCompletedHandler extends BaseHandler {
    static final String TEXT_WAIT_FOR_CONNECTION = "加密連線完成，開始聊天囉!";

    static final int CHECKING_INTERVAL = 500;
    static final int TIMEOUT_CONNECTION = 8000;
    public WaitForConnectionCompletedHandler(PlayContext playContext) {
        super(playContext);
    }

    @Override
    public void next(final JavascriptHelper instructor) {
        super.next(instructor);

        String checkSelector = String.format(ActionElementSelector.SELECTOR_SYSTEM_TEXT, TEXT_WAIT_FOR_CONNECTION);
        final long startTime = System.currentTimeMillis();
        startCheckSpecificTask("", checkSelector, CHECKING_INTERVAL, new OnCheckResult(){
            @Override
            public void onResult(Object result, Runnable task) {
                if (result != null) {
                    nextHandler();
                    setFinished();
                } else {

                    boolean isReachTimeout = (System.currentTimeMillis() - startTime) > TIMEOUT_CONNECTION;
                    if (!isFinished() && !isReachTimeout){
                        instructor.postDelayed(task, CHECKING_INTERVAL);
                    }else if (isReachTimeout){
                        nextToFailHandler();
                    }
                }
            }
        });


    }
}
