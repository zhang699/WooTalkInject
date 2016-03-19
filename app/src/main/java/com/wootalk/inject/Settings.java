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
    public Settings(Context context){
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public int getWaitForResponseDelay(){
        return Integer.parseInt(mPrefs.getString(KEY_SKIPPING_WAITING_TIME, "0"));
    }

    public String getOpeningSentence(){
        return mPrefs.getString(KEY_OPENING_SENTENCE, "");
    }
}
