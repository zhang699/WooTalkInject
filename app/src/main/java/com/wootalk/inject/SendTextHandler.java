package com.wootalk.inject;

import android.util.Log;

import model.JavascriptHelper;

/**
 * Created by Chang on 2016/3/18.
 */
public class SendTextHandler extends BaseHandler {
    private String mTextMsg;

    public SendTextHandler(PlayContext context, String textMsg) {
        super(context);
        mTextMsg = textMsg;
    }


    @Override
    public void next(final JavascriptHelper instructor) {
        super.next(instructor);
        //'stranger typing'"#contents .stranger.typing"
        mTextMsg = mPContext.getSettings().getOpeningSentence();
        String inputMsg = String.format("$('#messageInput').val('%s')", mTextMsg);
        Log.d("SendTextHandler.next", inputMsg);
        callWithCallback(inputMsg, "", new JavascriptHelper.FinishCallback(){
            @Override
            public void onFinish(Object result) {
                call("sendMessage()", "");
            }
        });



    }
}
