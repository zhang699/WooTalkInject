package com.wootalk.inject;

import android.util.Log;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.wootalk.model.JavascriptHelper;

/**
 * Created by Chang on 2016/3/20.
 */
public class WaitForTargetAnswerHandler extends BaseHandler {

    public WaitForTargetAnswerHandler(PlayContext playContext) {
        super(playContext);
    }
    private int CHECKING_INTERVAL = 500;
    private long mCheckTargetResponseInitTime = 0;
    @Override
    public void next(final JavascriptHelper instructor) {
        super.next(instructor);
        final int afterOpeningDelayMS = mPContext.getSettings().getWaitForResponseDelayAfterOpening() * 1000;

        final OnCheckResult onCheckResult = new OnCheckResult(){
            @Override
            public void onResult(Object result, Runnable task) {
                boolean isReachTimeout = (System.currentTimeMillis() - mCheckTargetResponseInitTime) >= afterOpeningDelayMS;
                Log.d("startCheckSpecificTask", "isReachTimeout:"+isReachTimeout);
                if (result == null && isReachTimeout){
                    nextToFailHandler();
                }else if (result != null) {
                    Log.d("startCheckSpecificTask", result.toString());
                    nextHandler();
                }else{
                    if (!isFinished()){
                        instructor.postDelayed(task, CHECKING_INTERVAL);
                    }
                }

            }
        };
        JavascriptHelper.FinishCallback findMeTextCallback = new JavascriptHelper.FinishCallback() {
            @Override
            public void onFinish(Object result) {
               // Log.d("findMeTextCallback", "result:"+result);
                if (result != null && result instanceof Elements) {

                    Elements elements = (Elements) result;
                    Element element = elements.last();
                    String mId = element.attributes().get("mid");
                    //Log.d("startCheckSpecificTask", "WaitForAnswerTargetHandler:"+mId);
                    int targetMsgId = 0;
                    try{
                        targetMsgId = Integer.parseInt(mId) + 1;
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    String selector = String.format(ActionElementSelector.SELECTOR_STRANGER_MESSAGES, targetMsgId);

                    mCheckTargetResponseInitTime = System.currentTimeMillis();
                    startCheckSpecificTask("", selector, CHECKING_INTERVAL, onCheckResult);


                } else {
                   mPContext.replay();
                }
            }

        };
        callWithCallback("", "#messages .me.text", findMeTextCallback);
    }


}
