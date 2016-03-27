package com.wootalk.inject;

import com.wootalk.model.JavascriptHelper;

/**
 * Created by JimmyJhang on 2016/3/18.
 */
public class ClickSideViewHandler extends BaseHandler{

    public ClickSideViewHandler(PlayContext context, BaseHandler next) {
        super(context, next);
    }

    @Override
    public void next(JavascriptHelper instructor) {
        super.next(instructor);
        /*傳送
        * changePerson
        * #popup-yes
        * #sendButton
        * #messageInput*/
        call("$('#open-left').click();", ".snap-drawer ul li:first-child a");
        //helper.callAndWaitForSpecifiedSelector("$('#open-left').click()", "#keyInput", mFinishCallback);
    }
}
