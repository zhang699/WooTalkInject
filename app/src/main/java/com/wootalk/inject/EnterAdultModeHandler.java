package com.wootalk.inject;

import com.wootalk.model.JavascriptHelper;

/**
 * Created by Chang on 2016/3/18.
 */
public class EnterAdultModeHandler extends ClickSideViewHandler {
    public EnterAdultModeHandler(PlayContext context, BaseHandler next) {
        super(context, next);
    }

    @Override
    public void next(JavascriptHelper instructor) {
        super.next(instructor);
        call("$('#keyInput').val('成人模式')", "");
    }
}
