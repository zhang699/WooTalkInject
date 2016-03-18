package com.wootalk.inject;

import android.util.Log;

import model.JavascriptHelper;

/**
 * Created by JimmyJhang on 2016/3/18.
 */
public class ClickPassPhaseHandler extends BaseHandler {
    public ClickPassPhaseHandler(BaseHandler next) {
        super(next);
    }

    @Override
    public void next(JavascriptHelper instructor) {
        Log.d("ClickPassPhaseHandler", "next!!");
        super.next(instructor);
        call("$('.snap-drawer ul li a')[0].click();", "#keyInput");
        //helper.callAndWaitForSpecifiedSelector("$('#open-left').click()", "#keyInput", mFinishCallback);
    }
}
