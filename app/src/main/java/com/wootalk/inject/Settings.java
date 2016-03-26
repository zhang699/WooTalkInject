package com.wootalk.inject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Chang on 2016/3/19.
 */
public class Settings {

    private final SharedPreferences mPrefs;
    public static final String KEY_OPENING_SENTENCE = "opening_sentence";
    public static final String KEY_SKIPPING_WAITING_TIME = "skipping_waitingtime";
    public static final String KEY_AFTER_OPENING_WAITING_TIME = "afteropening_waitingtime";

    public static final String KEY_PERSONALITY_OPENING = "personality_opening_sentence";

    public static final String KEY_NOTIFICATION_NEW_MESSAGES = "notifications_new_message_vibrate";
    private static final String KEY_IS_STARTED = "is_started";


    public Settings(Context context){
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);

    }

    public int getWaitForResponseDelay(){
        return Integer.parseInt(mPrefs.getString(KEY_SKIPPING_WAITING_TIME, "0"));
    }

    public int getWaitForResponseDelayAfterOpening(){
        return Integer.parseInt(mPrefs.getString(KEY_AFTER_OPENING_WAITING_TIME, "0"));
    }

    public String getOpeningSentence(){
        return mPrefs.getString(KEY_OPENING_SENTENCE, "");
    }

    public String getPersonalityOpeningSentence(){
        return mPrefs.getString(KEY_PERSONALITY_OPENING, "");
    }

    public boolean getNotificationVibrateEnabled(){
        return mPrefs.getBoolean(KEY_NOTIFICATION_NEW_MESSAGES, true);
    }

    public boolean isSystemStarted(){
        return mPrefs.getBoolean(KEY_IS_STARTED, true);
    }

    public void setSystemStarted(boolean isStarted){
        mPrefs.edit().putBoolean(KEY_IS_STARTED, isStarted).apply();
    }

}
