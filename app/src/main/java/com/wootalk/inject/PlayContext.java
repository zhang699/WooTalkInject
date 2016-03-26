package com.wootalk.inject;

/**
 * Created by Chang on 2016/3/18.
 */
public interface PlayContext {
    String STATE_STOP = "stop";
    String STATE_NEXT = "next";
    String STATE_START = "start";
    String STATE_FINISHED = "finished";
    String STATE_INITIALED = "initialed";

    void replay();
    Settings getSettings();
    void reportState(String handlerName, String stateName);
    boolean canPlayNext();
}
