package com.wootalk.inject;

/**
 * Created by Chang on 2016/3/18.
 */
public interface PlayContext {
    static final String STATE_STOP = "stop";
    static final String STATE_NEXT = "next";
    static final String STATE_START = "start";
    void replay();
    Settings getSettings();
    void reportState(String handlerName, String stateName);
    boolean canPlayNext();
}
