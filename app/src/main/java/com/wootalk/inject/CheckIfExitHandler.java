package com.wootalk.inject;

import model.JavascriptHelper;

/**
 * Created by Chang on 2016/3/18.
 */
public class CheckIfExitHandler extends BaseHandler {
    private final String EXIT_STRING = "對方離開了，請按離開按鈕回到首頁";
    private static final int CHECK_DELAY = 500;
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
        String checkSelector = String.format(".system.text:contains(%s)", EXIT_STRING);

        startCheckSpecificTask("", checkSelector, CHECK_DELAY, new OnCheckResult(){
            @Override
            public void onResult(Object result, Runnable task) {
                if (result != null) {
                    nextToFailHandler();
                    setFinished();
                } else {

                    if (!isFinished() && mIsCheckInfinitely) {
                        instructor.postDelayed(task, CHECK_DELAY);
                    }

                    if (!mIsCheckInfinitely) {
                        nextHandler();
                        setFinished();
                    }
                }
            }
        });
        /*instructor.postDelayed(new Runnable() {
            @Override
            public void run() {
                String checkSelector = String.format(".system.text:contains(%s)", EXIT_STRING);
                final Runnable task = this;

                callWithCallback("", checkSelector, new JavascriptHelper.FinishCallback() {
                    @Override
                    public void onFinish(Object result) {

                    }
                });
            }
        }, CHECK_DELAY);*/
    }
}
