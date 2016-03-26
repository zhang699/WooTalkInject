package inject.wootalk.com.wootalkinjectapplication;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.content.Intent;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.wootalk.inject.PlayContext;
import com.wootalk.inject.RobotActionPlayManager;
import com.wootalk.inject.Settings;
import com.wootalk.inject.WootalkInjectClient;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;

    private ImageButton mSettingsButtons;
    private final Handler mHideHandler = new Handler();
   // private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
           /* mWooTalkWebView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);*/
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    private WebView mWooTalkWebView;
    private Switch mEnableSwitch;
    private WootalkInjectClient mWootalkClient;
    private ImageButton mRefreshButton;
    private ProgressBar mStateProgressBar;
    private TextView mStateTextView;
    private NotificationManager mNotifyMgr;
    private Settings mSettings;
    private CompoundButton.OnCheckedChangeListener mOnControlChangeLstener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mSettings = new Settings(this);


        initWooTalkWebView();

        mSettingsButtons = (ImageButton) findViewById(R.id.button_settings);
        mEnableSwitch = (Switch) findViewById(R.id.enable_control_switch);
        mStateProgressBar = (ProgressBar) findViewById(R.id.state_progressbar);
        mStateTextView = (TextView) findViewById(R.id.textview_status);
        mRefreshButton = (ImageButton) findViewById(R.id.button_refresh);

        mStateProgressBar.setVisibility(View.INVISIBLE);
        //mEnableSwitch.setEnabled(false);
        mSettingsButtons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FullscreenActivity.this, SettingsActivity.class));
            }
        });

        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWootalkClient.reloadWebView();
                mWooTalkWebView.reload();
            }
        });

        mOnControlChangeLstener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mEnableSwitch.setEnabled(false);
                mWootalkClient.start(b);
            }
        };

        mEnableSwitch.setOnCheckedChangeListener(mOnControlChangeLstener);

        mWootalkClient.setOnStateChangeListener(new RobotActionPlayManager.OnStateChangeListener() {
            @Override
            public void onStateChanged(String handlerName, String stateName) {
                if (stateName.equals(PlayContext.STATE_INITIALED)) {
                    mEnableSwitch.setEnabled(true);
                }else if (!stateName.equals(PlayContext.STATE_FINISHED)){
                    mEnableSwitch.setEnabled(true);

                    boolean isRunning = mWootalkClient.isRunning();
                    setState(isRunning);
                    mSettings.setSystemStarted(isRunning);

                }else{
                    sendNotficiation(getString(R.string.string_find_girl), getString(R.string.string_click_to_show));
                }

            }
        });

        setState(false);

    }
    private void setState(boolean isRunning){
        mEnableSwitch.setOnCheckedChangeListener(null);
        mEnableSwitch.setChecked(isRunning);
        mStateProgressBar.setVisibility(isRunning ? View.VISIBLE: View.INVISIBLE);
        mStateTextView.setText(isRunning ? R.string.state_running : R.string.state_stop);
        mEnableSwitch.setOnCheckedChangeListener(mOnControlChangeLstener);
    }

    private void initWooTalkWebView(){
        mWooTalkWebView = (WebView) findViewById(R.id.wootalk_webview);
        mWootalkClient = new WootalkInjectClient(mWooTalkWebView, mSettings);
        mWooTalkWebView.setWebViewClient(mWootalkClient);

        WebSettings webSettings = mWooTalkWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        mWooTalkWebView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }


    private void sendNotficiation(String title, String text){

       // Log.d("testForNotification", "testForNotification");


        int notificationId = 001;
        Intent resultIntent = new Intent(this, FullscreenActivity.class);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setContentTitle(title)
                        .setContentText(text)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentIntent(resultPendingIntent);

        if (mSettings.getNotificationVibrateEnabled()){
            mBuilder.setVibrate(new long[] { 1000, 1000});
        }

        mBuilder.setLights(Color.CYAN, 1000, 1000);
        mNotifyMgr.notify(notificationId, mBuilder.build());
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        //mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
       /* mWooTalkWebView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);*/
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
