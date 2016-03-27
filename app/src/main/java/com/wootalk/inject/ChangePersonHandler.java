package com.wootalk.inject;

import com.wootalk.model.JavascriptHelper;

/**
 * Created by Chang on 2016/3/18.
 */
public class ChangePersonHandler extends BaseHandler {
    private final PlayContext mRestarter;
    private static final long TIMEOUT_CHECKING = 1000;
    public ChangePersonHandler(PlayContext playContext, BaseHandler next) {
        super(playContext);
        mRestarter = playContext;
    }

    @Override
    public void next(final JavascriptHelper instructor) {
        super.next(instructor);
        //.mfp-container .mfp-content .white-popup
        callWithCallback("changePerson();", "", new JavascriptHelper.FinishCallback(){
            @Override
            public void onFinish(Object result) {
                final long startCheckingTime = System.currentTimeMillis();
                startCheckSpecificTask("", ActionElementSelector.SELECTOR_CHANGE_PERSON,
                        ActionElementSelector.DEFAULT_SELECTOR_CHECKING_TIMEOUT, new OnCheckResult() {
                    @Override
                    public void onResult(Object result, Runnable task) {
                        boolean isReachTimeout = System.currentTimeMillis() - startCheckingTime >TIMEOUT_CHECKING ;
                        if (result != null){
                            callWithCallback("$('#popup-yes').click()", "", new JavascriptHelper.FinishCallback(){
                                @Override
                                public void onFinish(Object result) {
                                    mRestarter.replay();
                                }
                            });
                        }else if (isReachTimeout){
                            mRestarter.replay();
                        }else {
                            instructor.postDelayed(task, ActionElementSelector.DEFAULT_SELECTOR_CHECKING_TIMEOUT);
                        }

                    }
                });

            }
        });
    }
}
