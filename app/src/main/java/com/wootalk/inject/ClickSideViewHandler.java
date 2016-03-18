package com.wootalk.inject;

import model.JavascriptHelper;

/**
 * Created by JimmyJhang on 2016/3/18.
 */
public class ClickSideViewHandler extends BaseHandler{

    public ClickSideViewHandler(BaseHandler next) {
        super(next);
    }

    @Override
    public void next(JavascriptHelper instructor) {
        super.next(instructor);

        call("$('#open-left').click();", ".snap-drawer ul li:first-child a");
        //helper.callAndWaitForSpecifiedSelector("$('#open-left').click()", "#keyInput", mFinishCallback);
    }
}
