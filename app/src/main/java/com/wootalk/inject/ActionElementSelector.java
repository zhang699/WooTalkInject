package com.wootalk.inject;

/**
 * Created by Chang on 2016/3/20.
 */
public class ActionElementSelector {

    public static final String SELECTOR_STRANGER_MESSAGES = "#messages .stranger.text[mid=\"%s\"] ";
    public static final String SELECTOR_SYSTEM_TEXT = ".system.text:contains(%s)";
    public static final String SELECTOR_CHANGE_PERSON = ".mfp-container .mfp-content .white-popup #popup-yes";
    public static final String SELECTOR_MESSAGES_ME_TEXT = "#messages .me.text";
    public static final String SELECTOR_SYSTEM_TEXT_CONTAINS_S = ".system.text:contains(%s)";

    public static final long DEFAULT_SELECTOR_CHECKING_TIMEOUT = 200;

    public static final String UI_ACTION_$_MFP_CONTENT_CLICK = "$('.mfp-content').click()";
    public static final String UI_ACTION_$_POPUP_YES_CLICK = "$('#popup-yes').click()";
    public static final String UI_ACTION_$_MESSAGE_INPUT_VAL_S = "$('#messageInput').val('%s')";

    public static final String METHOD_CHANGE_PERSON = "changePerson();";
    public static final String METHOD_SEND_MESSAGE = "sendMessage()";
    public static final String METHOD_CLICK_START_CHAT = "clickStartChat";

    static final String TEXT_EXIT_STRING = "對方離開了，請按離開按鈕回到首頁";
    static final String TEXT_WAIT_FOR_CONNECTION = "加密連線完成，開始聊天囉!";

}
