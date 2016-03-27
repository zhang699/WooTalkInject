package com.wootalk.inject;

import com.wootalk.model.JavascriptHelper;

/**
 * Created by Chang on 2016/3/18.
 */
public class StartChatHandler extends BaseHandler {
    public StartChatHandler(PlayContext playContext) {
        super(playContext);
    }

    @Override
    public void next(final JavascriptHelper instructor) {
        super.next(instructor);
        startCheckSpecificTask("", ActionElementSelector.SELECTOR_CHANGE_PERSON,
                ActionElementSelector.DEFAULT_SELECTOR_CHECKING_TIMEOUT, new OnCheckResult() {
            @Override
            public void onResult(Object result, Runnable task) {
                if (result == null){
                    call("clickStartChat()", "");
                    //startCheckSpecificTask("", Ac);
                }else{
                    instructor.postDelayed(task, ActionElementSelector.DEFAULT_SELECTOR_CHECKING_TIMEOUT);
                }
            }
        });

    }
}
