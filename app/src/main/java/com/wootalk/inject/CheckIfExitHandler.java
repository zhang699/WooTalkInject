package com.wootalk.inject;

import com.wootalk.model.JavascriptHelper;

/**
 * Created by Chang on 2016/3/18.
 */
public class CheckIfExitHandler extends BaseHandler {
    private static final int CHECKING_DELAY = 500;
    private final boolean mIsCheckInfinitely;

    public CheckIfExitHandler(PlayContext context, boolean isCheckInfinitely) {
        super(context);
        mIsCheckInfinitely = isCheckInfinitely;
    }

    public CheckIfExitHandler(PlayContext context) {
        this(context, true);
    }

    @Override
    public void next(final JavascriptHelper instructor) {
        super.next(instructor);
        String checkSelector = String.format(ActionElementSelector.SELECTOR_SYSTEM_TEXT_CONTAINS_S, ActionElementSelector.TEXT_EXIT_STRING);

        startCheckSpecificTask("", checkSelector, CHECKING_DELAY, new OnCheckResult(){
            @Override
            public void onResult(Object result, Runnable task) {
                if (result != null) {
                    nextToFailHandler();
                    setFinished();
                } else {

                    if (!isFinished() && mIsCheckInfinitely) {
                        instructor.postDelayed(task, CHECKING_DELAY);
                    }

                    if (!mIsCheckInfinitely) {
                        nextHandler();
                        setFinished();
                    }
                }
            }
        });

    }
}
