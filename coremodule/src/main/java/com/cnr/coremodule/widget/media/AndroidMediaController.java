/*
 * Copyright (C) 2015 Bilibili
 * Copyright (C) 2015 Zhang Rui <bbcallen@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cnr.coremodule.widget.media;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cnr.coremodule.R;
import com.cnr.coremodule.widget.util.CommonUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

import static android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

public class AndroidMediaController  implements IMediaController,View.OnTouchListener {

    private AppCompatActivity activity;
    private ConstraintLayout player_contorl_top_layout;
    private ConstraintLayout player_contorl_bottom_layout;
    private ConstraintLayout player_control_backlayout_ConstraintLayout;
    private ConstraintLayout root_controller;
    private MediaPlayerControl mPlayer;
    private boolean mShowing = true;
    private boolean mDragging;
    private static final int sDefaultTimeout = 5000;
    private static final int FADE_OUT = 1;
    private static final int SHOW_PROGRESS = 2;
    private static final int LOCKIMG_FADE_OUT_INFO = 3;
    private ProgressBar mProgress;
    private TextView mEndTime, mCurrentTime;
    StringBuilder mFormatBuilder;
    Formatter mFormatter;
    private boolean isPause = true;
    private ImageView mPauseButton;
    private ImageView mScreenImageView;
    private FrameLayout mMainLayout;
    private IjkVideoView ijkVideoView;
    protected ImageView mLockImg;
    private FrameLayout main_player_layout;
    private boolean isLocked = true;

    private Animation topAnimation;

    private Animation topEnterAnimation;

    private Animation bottomAnimation;

    private Animation bottomEnterAnimation;

    private Animation rightToLeftAnimation;

    private Animation rightEnterToLeftAnimation;

    protected float mDownX;
    protected float mDownY;
    protected boolean mChangeVolume;
    protected boolean mChangeBrightness;
    protected int mGestureDownVolume;
    protected float mGestureDownBrightness;
    protected boolean mTouchingProgressBar;
    protected boolean mChangePosition;
    public static final int THRESHOLD = 80;
    protected long mGestureDownPosition;
    protected AudioManager mAudioManager;
    protected int mScreenWidth;
    protected int mScreenHeight;
    protected int mSeekTimePosition;
    protected Dialog mBrightnessDialog;
    protected Dialog mProgressDialog;
    protected Dialog mVolumeDialog;

    protected ProgressBar mDialogVolumeProgressBar;
    protected TextView mDialogVolumeTextView;
    protected ImageView mDialogVolumeImageView;
    protected ProgressBar mDialogBrightnessProgressBar;
    protected TextView mDialogBrightnessTextView;

    protected TextView mDialogSeekTime;
    protected TextView mDialogTotalTime;
    protected ProgressBar mDialogProgressBar;
    protected ImageView mDialogIcon;
    private boolean brocasting = false;
    protected TextView videoCurrentTime;
    protected ImageView batteryLevel;
    private ImageView player_next_img;

    private ConstraintLayout root_player_contorl;
    private ConstraintLayout player_contorl_right_bt;
    private ImageView player_contorl_dlna;

    private ImageView player_source;
    private ImageView player_share_img;
    private ImageView player_scale_img;

    private SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm");

    private int playType;
    public AndroidMediaController(Context mContext,int playType) {
        initView(mContext,playType);

    }

    private void initView(Context mContext,int playType) {
        activity = CommonUtil.getAppCompActivity(mContext);
        this.playType = playType;
        player_contorl_top_layout = activity.findViewById(R.id.player_contorl_top_layout);
        player_contorl_bottom_layout = activity.findViewById(R.id.player_contorl_bottom_layout);
        player_control_backlayout_ConstraintLayout = activity.findViewById(R.id.player_control_backlayout_ConstraintLayout);
        main_player_layout = activity.findViewById(R.id.main_player_layout);
        mScreenImageView = activity.findViewById(R.id.is_full_screen);
        mMainLayout = activity.findViewById(R.id.main_player_layout);
        ijkVideoView = activity.findViewById(R.id.ijkVideoView);
        mEndTime = activity.findViewById(R.id.time);
        mCurrentTime = activity.findViewById(R.id.time_current);
        mPauseButton = activity.findViewById(R.id.player_play_img);
        root_player_contorl = activity.findViewById(R.id.root_player_contorl);
        player_contorl_right_bt = activity.findViewById(R.id.player_contorl_right_bt);
        player_contorl_dlna = activity.findViewById(R.id.player_contorl_dlna);
        player_next_img = activity.findViewById(R.id.player_next_img);
        player_share_img =activity.findViewById(R.id.player_share_img);
        player_scale_img =activity.findViewById(R.id.player_scale_img);
        if (mPauseButton != null) {
            mPauseButton.requestFocus();
            mPauseButton.setOnClickListener(mPauseListener);
        }



        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());

        mProgress = activity.findViewById(R.id.mediacontroller_progress);

        if (mProgress != null) {
            if (mProgress instanceof SeekBar) {
                SeekBar seeker = (SeekBar) mProgress;

                seeker.setOnSeekBarChangeListener(mSeekListener);
            }
            mProgress.setMax(1000);
        }
        if (mScreenImageView != null) {
            mScreenImageView.setOnClickListener(onClickListener);
        }
        mLockImg = activity.findViewById(R.id.player_lock);
        root_controller = activity.findViewById(R.id.root_controller);
        mLockImg.setOnClickListener(lockClickListener);
        if (player_control_backlayout_ConstraintLayout != null) {
            player_control_backlayout_ConstraintLayout.setOnClickListener(OrientationOnClickListener);
        }

        topAnimation = AnimationUtils.loadAnimation(activity,
                R.anim.player_translate_top);
        topEnterAnimation = AnimationUtils.loadAnimation(activity,
                R.anim.player_translate_top_enter);
        bottomAnimation = AnimationUtils.loadAnimation(activity,
                R.anim.player_translate_bottom);
        bottomEnterAnimation = AnimationUtils.loadAnimation(activity,
                R.anim.player_translate_bottom_enter);
        rightToLeftAnimation = AnimationUtils.loadAnimation(activity,
                R.anim.player_translate_right_to_left);

        rightEnterToLeftAnimation = AnimationUtils.loadAnimation(activity,
                R.anim.player_translate_right_to_left_enter);

        main_player_layout.setOnTouchListener(this);

        mScreenWidth = activity.getResources().getDisplayMetrics().widthPixels;
        mScreenHeight = activity.getResources().getDisplayMetrics().heightPixels;
        mAudioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        videoCurrentTime = activity.findViewById(R.id.video_current_time);
        batteryLevel = activity.findViewById(R.id.battery_level);
        player_source = activity.findViewById(R.id.player_source);
        setSystemTimeAndBattery();
        setSmallScreen();
    }

    public void showFragment(){
        hide();
        startAnimation(root_player_contorl,rightToLeftAnimation,View.VISIBLE);
    }



    private BroadcastReceiver battertReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
                int level = intent.getIntExtra("level", 0);
                int scale = intent.getIntExtra("scale", 100);
                int percent = level * 100 / scale;
                if (percent < 15) {
                    batteryLevel.setBackgroundResource(R.mipmap.jz_battery_level_10);
                } else if (percent >= 15 && percent < 40) {
                    batteryLevel.setBackgroundResource(R.mipmap.jz_battery_level_30);
                } else if (percent >= 40 && percent < 60) {
                    batteryLevel.setBackgroundResource(R.mipmap.jz_battery_level_50);
                } else if (percent >= 60 && percent < 80) {
                    batteryLevel.setBackgroundResource(R.mipmap.jz_battery_level_70);
                } else if (percent >= 80 && percent < 95) {
                    batteryLevel.setBackgroundResource(R.mipmap.jz_battery_level_90);
                } else if (percent >= 95 && percent <= 100) {
                    batteryLevel.setBackgroundResource(R.mipmap.jz_battery_level_100);
                }
                activity.unregisterReceiver(battertReceiver);
                brocasting = false;
            }
        }
    };

    private View.OnClickListener OrientationOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setOrientation();
        }
    };

    private View.OnClickListener lockClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            lockAndUnlock();
        }
    };

    private void lockAndUnlock() {
        if (isLocked) {
            //锁
            unlock();
        } else {
            //解
            locked();
        }
    }
    public void unlock(){
        isLocked = false;
        mLockImg.setImageResource(R.mipmap.player_unlock);
        hide();
        mHandler.sendEmptyMessageDelayed(LOCKIMG_FADE_OUT_INFO,
                sDefaultTimeout);
    }
    public void locked(){
        isLocked = true;
        mLockImg.setImageResource(R.mipmap.player_locked);
        show();
        mHandler.removeMessages(LOCKIMG_FADE_OUT_INFO);
    }

    @Override
    public void hide() {
        if (mShowing) {
            mHandler.removeMessages(SHOW_PROGRESS);
            startAnimation(player_contorl_top_layout, topAnimation, View.GONE);
            startAnimation(player_contorl_bottom_layout, bottomAnimation, View.GONE);

            if (orientation()) {
                startAnimation(player_contorl_right_bt,rightEnterToLeftAnimation,View.GONE);
                if (isLocked) {
                    setSlide(root_controller, View.GONE, Gravity.LEFT);
                }
            }

            mShowing = false;
        }
    }

    public void startAnimation(ConstraintLayout linearLayout, Animation animation, int finalState) {
//        int isVis = linearLayout.getVisibility() == finalState ? View.GONE : View.VISIBLE;
        linearLayout.startAnimation(animation);
        linearLayout.setVisibility(finalState);
    }

    @Override
    public void show(int timeout) {
        if (!mShowing) {
            if (root_player_contorl.getVisibility() != View.GONE){
                startAnimation(root_player_contorl,rightEnterToLeftAnimation,View.GONE);
            }
//            if (!isPlayTypeLive()){
                setProgress();
//            }
            if (mPauseButton != null) {
                mPauseButton.requestFocus();
            }
            disableUnsupportedButtons();

            startAnimation(player_contorl_bottom_layout, bottomEnterAnimation, View.VISIBLE);
            startAnimation(player_contorl_top_layout, topEnterAnimation, View.VISIBLE);

            //锁屏幕
            if (orientation()) {
                setSlide(player_contorl_right_bt,View.VISIBLE, Gravity.RIGHT);
                if (mLockImg != null) {
                    setSlide(root_controller, View.VISIBLE, Gravity.LEFT);
                }
            }

            mShowing = true;
        }
        updatePausePlay();
//        if (!isPlayTypeLive()){
            mHandler.sendEmptyMessage(SHOW_PROGRESS);
//        }
        Message msg = mHandler.obtainMessage(FADE_OUT);
        if (timeout != 0) {
            mHandler.removeMessages(FADE_OUT);
            mHandler.sendMessageDelayed(msg, timeout);
        }
        setSystemTimeAndBattery();
    }


    private void hideNavigationBar() {
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        |View. SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        |View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }
    private boolean isPlayTypeLive(){
        if (playType == com.cnr.coremodule.widget.util.Configuration.PLAY_LIVE){
            return true;
        }
        return false;
    }

    private void showNavigationBar() {
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View. SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }

    @Override
    public boolean isShowing() {
        return mShowing;
    }

    @Override
    public void setAnchorView(View view) {

    }

    @Override
    public void setEnabled(boolean enabled) {
        if (mPauseButton != null) {
            mPauseButton.setEnabled(enabled);
        }
        if (mProgress != null) {
            mProgress.setEnabled(enabled);
        }

        disableUnsupportedButtons();
    }

    @Override
    public void setMediaPlayer(com.cnr.coremodule.widget.media.MediaPlayerControl player) {
        mPlayer = player;
        updatePausePlay();
    }


    @Override
    public void show() {
        show(sDefaultTimeout);
    }

    @Override
    public void showOnce(View view) {

    }

    @Override
    public boolean isLocked() {
        return isLocked;
    }

    @Override
    public void showLockImgVs() {
        if (root_controller.getVisibility() != View.VISIBLE) {
            setSlide(root_controller, View.VISIBLE, Gravity.LEFT);
            mHandler.sendEmptyMessageDelayed(LOCKIMG_FADE_OUT_INFO,
                    sDefaultTimeout);
        } else {
            setSlide(root_controller, View.GONE, Gravity.LEFT);
            mHandler.removeMessages(LOCKIMG_FADE_OUT_INFO);
        }
    }


    public void setSlide(ConstraintLayout linearLayout, int finalState, int type) {
        if (Build.VERSION.SDK_INT < 21) {
            linearLayout.setVisibility(finalState);
        } else {
            Slide slide = new Slide();
            slide.setSlideEdge(type);
//            slide.setDuration(500);
            TransitionManager.beginDelayedTransition(linearLayout, slide);

            linearLayout.setVisibility(finalState);
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int pos;
            switch (msg.what) {
                case FADE_OUT:
                    hide();
                    break;
                case SHOW_PROGRESS:
                    pos = setProgress();
                    if (!mDragging && mShowing && mPlayer.isPlaying()) {
                        msg = obtainMessage(SHOW_PROGRESS);
                        sendMessageDelayed(msg, 1000 - (pos % 1000));
                    }
                    break;
                case LOCKIMG_FADE_OUT_INFO:
                    setSlide(root_controller, View.GONE, Gravity.LEFT);
                    break;
                default:
                    break;
            }
        }
    };

    public int setProgress() {

        if (mPlayer == null || mDragging) {
            return 0;
        }
        int position = mPlayer.getCurrentPosition();
        int duration = mPlayer.getDuration();
        if (mProgress != null ) {
            if (duration > 0) {
                // use long to avoid overflow
                long pos = 1000L * position / duration;
                mProgress.setProgress((int) pos);
            }
            int percent = mPlayer.getBufferPercentage();
            mProgress.setSecondaryProgress(percent * 10);
        }

        if (mEndTime != null)
            mEndTime.setText(stringForTime(duration));
        if (mCurrentTime != null)
            mCurrentTime.setText(stringForTime(position));

        return position;
    }

    // There are two scenarios that can trigger the seekbar listener to trigger:
    //
    // The first is the user using the touchpad to adjust the posititon of the
    // seekbar's thumb. In this case onStartTrackingTouch is called followed by
    // a number of onProgressChanged notifications, concluded by onStopTrackingTouch.
    // We're setting the field "mDragging" to true for the duration of the dragging
    // session to avoid jumps in the position in case of ongoing playback.
    //
    // The second scenario involves the user operating the scroll ball, in this
    // case there WON'T BE onStartTrackingTouch/onStopTrackingTouch notifications,
    // we will simply apply the updated position without suspending regular updates.
    private SeekBar.OnSeekBarChangeListener mSeekListener = new SeekBar.OnSeekBarChangeListener() {
        public void onStartTrackingTouch(SeekBar bar) {
            show(3600000);

            mDragging = true;

            // By removing these pending progress messages we make sure
            // that a) we won't update the progress while the user adjusts
            // the seekbar and b) once the user is done dragging the thumb
            // we will post one of these messages to the queue again and
            // this ensures that there will be exactly one message queued up.
            mHandler.removeMessages(SHOW_PROGRESS);
        }

        public void onProgressChanged(SeekBar bar, int progress, boolean fromuser) {
            if (!fromuser) {
                // We're not interested in programmatically generated changes to
                // the progress bar's position.
                return;
            }

            long duration = mPlayer.getDuration();
            long newposition = (duration * progress) / 1000L;
            mPlayer.seekTo((int) newposition);
            if (mCurrentTime != null)
                mCurrentTime.setText(stringForTime((int) newposition));
        }

        public void onStopTrackingTouch(SeekBar bar) {
            mDragging = false;
            setProgress();
            updatePausePlay();
            show(sDefaultTimeout);

            // Ensure that progress is properly updated in the future,
            // the call to show() does not guarantee this because it is a
            // no-op if we are already showing.
            mHandler.sendEmptyMessage(SHOW_PROGRESS);
        }
    };

    public String stringForTime(int timeMs) {
        if (timeMs <= 0 || timeMs >= 24 * 60 * 60 * 1000) {
            return "00:00";
        }
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    private void updatePausePlay() {
        if (activity != null && mPauseButton != null)
            updatePausePlay(isPause, mPauseButton);
    }

    protected void updatePausePlay(boolean isPlaying, ImageView pauseButton) {
        if (isPlaying) {
            pauseButton.setImageResource(R.drawable.selector_player_play_normal);
        } else {
            pauseButton.setImageResource(R.drawable.selector_player_play);
        }
    }

    private View.OnClickListener mPauseListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            doPauseResume();
            show(sDefaultTimeout);
        }
    };

    private void doPauseResume() {
        if (mPlayer.isPlaying()) {
            isPause = false;
            mPlayer.pause();
        } else {
            isPause = true;
            mPlayer.start();
        }
        updatePausePlay();
    }

    private void disableUnsupportedButtons() {
        if (mPlayer == null){
            return;
        }
        try {
            if (mPauseButton != null && !mPlayer.canPause()) {
                mPauseButton.setEnabled(false);
            }
            if (mProgress != null && !mPlayer.canSeekBackward() && !mPlayer.canSeekForward()) {
                mProgress.setEnabled(false);
            }
        } catch (IncompatibleClassChangeError ex) {
            // We were given an old version of the interface, that doesn't have
            // the canPause/canSeekXYZ methods. This is OK, it just means we
            // assume the media can be paused and seeked, and so we don't disable
            // the buttons.
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

                setFullScreen();
            } else {
                setSmallScreen();
            }
        }
    };

    public void setSmallScreen() {
        activity.getWindow().getDecorView().setSystemUiVisibility(View.VISIBLE);
        setVisible(View.GONE);
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
        attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
        activity.getWindow().setAttributes(attrs);
        ijkVideoView.toggleAspectRatio(IRenderView.AR_MATCH_PARENT);

    }


    /**
     * 设置全屏
     */
    public void setFullScreen() {
        hideNavigationBar();
        setVisible(View.VISIBLE);
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        activity.getWindow().setAttributes(attrs);

        ijkVideoView.toggleAspectRatio(IRenderView.AR_MATCH_PARENT);



    }
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setSmallScreen();
        } else {
            setFullScreen();
        }

    }


    private void setVisible(int isVis){
        isVisible(player_source,isVis);
        isVisible(mLockImg,isVis);
        isVisible(player_contorl_right_bt,isVis);
        isVisible(player_next_img,isVis);
        isVisible(player_contorl_dlna,isVis);
        isVisible(player_share_img,isVis);
        isVisible(player_scale_img,isVis);
    }

    private void isVisible(View view,int isVis){
//        int isVis = view.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
        view.setVisibility(isVis);
    }

    public boolean orientation() {
        return activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public void setOrientation() {
        if (orientation()) {
            setSmallScreen();
        } else {
            activity.finish();
        }
    }

    public void stopPlay(Boolean mBackPressed) {
        if (mBackPressed) {
            ijkVideoView.stopPlayback();
            ijkVideoView.release(true);
        }
        IjkMediaPlayer.native_profileEnd();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (!isLocked()) {
            showLockImgVs();
            return isLocked();
        }
        float x = event.getX();
        float y = event.getY();
        int id = v.getId();
        if (id == R.id.main_player_layout) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mTouchingProgressBar = true;
                    mDownX = x;
                    mDownY = y;
                    mChangeVolume = false;
                    mChangePosition = false;
                    mChangeBrightness = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    float deltaX = x - mDownX;
                    float deltaY = y - mDownY;
                    float absDeltaX = Math.abs(deltaX);
                    float absDeltaY = Math.abs(deltaY);
                    if (orientation()) {
                        if (!mChangePosition && !mChangeVolume && !mChangeBrightness) {
                            if (absDeltaX > THRESHOLD || absDeltaY > THRESHOLD) {

                                if (absDeltaX >= THRESHOLD) {
                                    mChangePosition = true;
                                    if (mPlayer != null) {
                                        mGestureDownPosition = mPlayer.getCurrentPosition();
                                    }
                                } else {
                                    //如果y轴滑动距离超过设置的处理范围，那么进行滑动事件处理
                                    if (mDownX < mScreenWidth * 0.5f) {//左侧改变亮度
                                        mChangeBrightness = true;
                                        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                                        if (lp.screenBrightness < 0) {
                                            try {
                                                mGestureDownBrightness = android.provider.Settings.System.getInt(activity.getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS);
                                            } catch (Settings.SettingNotFoundException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            mGestureDownBrightness = lp.screenBrightness * 255;
                                        }
                                    } else {//右侧改变声音
                                        mChangeVolume = true;
                                        mGestureDownVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                                    }
                                }
                            }
                        }
                    }
                    if (mChangePosition) {
                        if (mPlayer != null) {
                            int totalTimeDuration = mPlayer.getDuration();
                            mSeekTimePosition = (int) (mGestureDownPosition + deltaX * totalTimeDuration / mScreenWidth);
                            if (mSeekTimePosition > totalTimeDuration)
                                mSeekTimePosition = totalTimeDuration;
                            String seekTime = stringForTime(mSeekTimePosition);
                            String totalTime = stringForTime(totalTimeDuration);
                            showProgressDialog(deltaX, seekTime, mSeekTimePosition, totalTime, totalTimeDuration);
                        }
                    }
                    if (mChangeVolume) {
                        deltaY = -deltaY;
                        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                        int deltaV = (int) (max * deltaY * 3 / mScreenHeight);
                        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mGestureDownVolume + deltaV, 0);
                        //dialog中显示百分比
                        int volumePercent = (int) (mGestureDownVolume * 100 / max + deltaY * 3 * 100 / mScreenHeight);
                        showVolumeDialog(-deltaY, volumePercent);
                    }

                    if (mChangeBrightness) {
                        deltaY = -deltaY;
                        int deltaV = (int) (255 * deltaY * 3 / mScreenHeight);
                        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
                        if (((mGestureDownBrightness + deltaV) / 255) >= 1) {//这和声音有区别，必须自己过滤一下负值
                            params.screenBrightness = 1;
                        } else if (((mGestureDownBrightness + deltaV) / 255) <= 0) {
                            params.screenBrightness = 0.01f;
                        } else {
                            params.screenBrightness = (mGestureDownBrightness + deltaV) / 255;
                        }
                        activity.getWindow().setAttributes(params);
                        //dialog中显示百分比
                        int brightnessPercent = (int) (mGestureDownBrightness * 100 / 255 + deltaY * 3 * 100 / mScreenHeight);
                        showBrightnessDialog(brightnessPercent);
                    }

                    break;
                case MotionEvent.ACTION_UP:
                    dismissVolumeDialog();
                    dismissBrightnessDialog();
                    dismissProgressDialog();
                    if (mChangePosition) {
                        if (mPlayer != null) {
                            mPlayer.seekTo(mSeekTimePosition);
                            setProgress();
                        }
                    }
                    break;
            }
            gestureDetector.onTouchEvent(event);
            return isLocked;

        }
        return false;
    }
    /**
     * 双击
     */
    protected GestureDetector gestureDetector = new GestureDetector(activity,
            new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    doPauseResume();
                    return super.onDoubleTap(e);
                }

                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {
                    ijkVideoView.toggleMediaControlsVisiblity();
                    return super.onSingleTapConfirmed(e);
                }
            });



    @SuppressLint("DefaultLocale")
    public void showBrightnessDialog(int brightnessPercent) {
        if (mBrightnessDialog == null) {
            View localView = LayoutInflater.from(activity).inflate(R.layout.jz_dialog_brightness, null);
            mDialogBrightnessTextView = localView.findViewById(R.id.tv_brightness);
            mDialogBrightnessProgressBar = localView.findViewById(R.id.brightness_progressbar);
            mBrightnessDialog = createDialogWithView(localView);
        }
        if (!mBrightnessDialog.isShowing()) {
            mBrightnessDialog.show();
        }
        if (brightnessPercent > 100) {
            brightnessPercent = 100;
        } else if (brightnessPercent < 0) {
            brightnessPercent = 0;
        }
        mDialogBrightnessTextView.setText(String.format("%d%%", brightnessPercent));
        mDialogBrightnessProgressBar.setProgress(brightnessPercent);
    }

    public void dismissVolumeDialog() {
        if (mVolumeDialog != null) {
            mVolumeDialog.dismiss();
        }
    }
    public void dismissBrightnessDialog() {
        if (mBrightnessDialog != null) {
            mBrightnessDialog.dismiss();
        }
    }
    public void showProgressDialog(float deltaX, String seekTime, long seekTimePosition, String totalTime, long totalTimeDuration) {
        if (mProgressDialog == null) {
            View localView = LayoutInflater.from(activity).inflate(R.layout.jz_dialog_progress, null);
            mDialogProgressBar = localView.findViewById(R.id.duration_progressbar);
            mDialogSeekTime = localView.findViewById(R.id.tv_current);
            mDialogTotalTime = localView.findViewById(R.id.tv_duration);
            mDialogIcon = localView.findViewById(R.id.duration_image_tip);
            mProgressDialog = createDialogWithView(localView);
        }
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }

        mDialogSeekTime.setText(seekTime);
        mDialogTotalTime.setText(String.format(" / %s", totalTime));

        mDialogProgressBar.setProgress(totalTimeDuration <= 0 ? 0 : (int) (seekTimePosition * 100 / totalTimeDuration));
        if (deltaX > 0) {
            mDialogIcon.setBackgroundResource(R.mipmap.jz_forward_icon);
        } else {
            mDialogIcon.setBackgroundResource(R.mipmap.jz_backward_icon);
        }
    }
    public void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }
    @SuppressLint("DefaultLocale")
    public void showVolumeDialog(float deltaY, int volumePercent) {
        if (mVolumeDialog == null) {
            @SuppressLint("InflateParams") View localView = LayoutInflater.from(activity).inflate(R.layout.jz_dialog_volume, null);
            mDialogVolumeImageView = localView.findViewById(R.id.volume_image_tip);
            mDialogVolumeTextView = localView.findViewById(R.id.tv_volume);
            mDialogVolumeProgressBar = localView.findViewById(R.id.volume_progressbar);
            mVolumeDialog = createDialogWithView(localView);
        }
        if (!mVolumeDialog.isShowing()) {
            mVolumeDialog.show();
        }
        if (volumePercent <= 0) {
            mDialogVolumeImageView.setBackgroundResource(R.mipmap.jz_close_volume);
        } else {
            mDialogVolumeImageView.setBackgroundResource(R.mipmap.jz_add_volume);
        }
        if (volumePercent > 100) {
            volumePercent = 100;
        } else if (volumePercent < 0) {
            volumePercent = 0;
        }
        mDialogVolumeTextView.setText(String.format("%d%%", volumePercent));
        mDialogVolumeProgressBar.setProgress(volumePercent);
    }
    public Dialog createDialogWithView(View localView) {
        Dialog dialog = new Dialog(activity, R.style.jz_style_dialog_progress);
        dialog.setContentView(localView);
        Window window = dialog.getWindow();
        window.addFlags(Window.FEATURE_ACTION_BAR);
        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        window.setLayout(-2, -2);
        WindowManager.LayoutParams localLayoutParams = window.getAttributes();
        localLayoutParams.gravity = Gravity.CENTER;
        window.setAttributes(localLayoutParams);
        return dialog;
    }

    public void setSystemTimeAndBattery() {
        if (videoCurrentTime != null){
            videoCurrentTime.setText(dateFormatter.format(new Date()));
        }
        if (!brocasting) {
            activity.registerReceiver(
                    battertReceiver,
                    new IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            );
        }
    }

}
