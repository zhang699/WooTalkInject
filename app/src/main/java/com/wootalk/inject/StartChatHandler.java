package com.wootalk.inject;

import model.JavascriptHelper;

/**
 * Created by Chang on 2016/3/18.
 */
public class StartChatHandler extends BaseHandler {
    public StartChatHandler(PlayContext playContext) {
        super(playContext);
    }

    @Override
    public void next(JavascriptHelper instructor) {
        super.next(instructor);
        call("clickStartChat()", "");
    }
}
