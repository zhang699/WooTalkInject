package com.wootalk.inject;

/**
 * Created by Chang on 2016/3/20.
 */
public class ActionElementSelector {

    public static final String SELECTOR_STRANGER_MESSAGES = "#messages .stranger.text[mid=\"%s\"] ";
    public static final String SELECTOR_SYSTEM_TEXT = ".system.text:contains(%s)";
    public static final String SELECTOR_CHANGE_PERSON = ".mfp-container .mfp-content .white-popup #popup-yes";

    public static final long DEFAULT_SELECTOR_CHECKING_TIMEOUT = 200;
}
