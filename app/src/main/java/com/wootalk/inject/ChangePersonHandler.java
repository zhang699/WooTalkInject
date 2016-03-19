package com.wootalk.inject;

import model.JavascriptHelper;

/**
 * Created by Chang on 2016/3/18.
 */
public class ChangePersonHandler extends BaseHandler {
    private final PlayContext mRestarter;

    public ChangePersonHandler(PlayContext playContext, BaseHandler next) {
        super(playContext);
        mRestarter = playContext;
    }

    @Override
    public void next(JavascriptHelper instructor) {
        super.next(instructor);
        callWithCallback("changePerson();", "", new JavascriptHelper.FinishCallback(){
            @Override
            public void onFinish(Object result) {
                callWithCallback("$('#popup-yes').click()", "", new JavascriptHelper.FinishCallback(){
                    @Override
                    public void onFinish(Object result) {
                        mRestarter.replay();
                    }
                });
            }
        });
    }
}
