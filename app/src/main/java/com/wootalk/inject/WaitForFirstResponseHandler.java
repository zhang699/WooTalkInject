package com.wootalk.inject;

import com.wootalk.model.JavascriptHelper;

/**
 * Created by Chang on 2016/3/22.
 */
public class WaitForFirstResponseHandler extends BaseHandler {
    private int mWaitSec = 12;

    public WaitForFirstResponseHandler(PlayContext playContext) {
        super(playContext);
    }

    @Override
    public void next(JavascriptHelper instructor) {
        super.next(instructor);
        mWaitSec = mPContext.getSettings().getWaitForResponseDelay();
        instructor.postDelayed(new Runnable() {
            @Override
            public void run(){
                setFinished();
                call("", "");

            }
        }, mWaitSec * 1000);

    }
}
