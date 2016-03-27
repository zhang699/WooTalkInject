package com.wootalk.inject;

import android.util.Log;

import com.wootalk.model.JavascriptHelper;

/**
 * Created by JimmyJhang on 2016/3/22.
 */
public class FinishHandler extends BaseHandler{
    public FinishHandler(PlayContext playContext) {
        super(playContext);
    }

    @Override
    public void next(JavascriptHelper instructor) {
        super.next(instructor);
        Log.d("FinishHandler", "next ");
        reportState(PlayContext.STATE_FINISHED);
    }
}
