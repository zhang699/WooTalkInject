package com.wootalk.inject;

import com.wootalk.model.JavascriptHelper;

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

        String inputMsg = String.format(ActionElementSelector.UI_ACTION_$_MESSAGE_INPUT_VAL_S, mTextMsg);
        //Log.d("SendTextHandler.next", inputMsg);
        callWithCallback(inputMsg, "", new JavascriptHelper.FinishCallback(){
            @Override
            public void onFinish(Object result) {
                call(ActionElementSelector.METHOD_SEND_MESSAGE, "");
            }
        });



    }
}
