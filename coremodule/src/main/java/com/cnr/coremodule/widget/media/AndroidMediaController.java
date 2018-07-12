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
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
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

import java.util.Formatter;
import java.util.Locale;

import javax.inject.Inject;

import static android.view.GestureDetector.*;

public class AndroidMediaController  implements IMediaController,View.OnTouchListener{

    private AppCompatActivity activity;
    private ConstraintLayout player_contorl_top_layout;
    private ConstraintLayout player_contorl_bottom_layout;
    private ConstraintLayout root_controller;
    private MediaPlayerControl mPlayer;
    private boolean mShowing;
    private boolean mDragging;
    private static final int sDefaultTimeout = 3000;
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


    public AndroidMediaController(Context mContext) {
        initView(mContext);
    }

    private void initView(Context mContext) {
        activity = CommonUtil.getAppCompActivity(mContext);
        player_contorl_top_layout = activity.findViewById(R.id.player_contorl_top_layout);
        player_contorl_bottom_layout = activity.findViewById(R.id.player_contorl_bottom_layout);
        main_player_layout = activity.findViewById(R.id.main_player_layout);
        mScreenImageView = activity.findViewById(R.id.is_full_screen);
        mMainLayout = activity.findViewById(R.id.main_player_layout);
        ijkVideoView = activity.findViewById(R.id.ijkVideoView);
        mEndTime = activity.findViewById(R.id.time);
        mCurrentTime = activity.findViewById(R.id.time_current);
        mPauseButton = activity.findViewById(R.id.player_play_img);
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
        if (mScreenImageView != null){
            mScreenImageView.setOnClickListener(onClickListener);
        }
        mLockImg =   activity.findViewById(R.id.player_lock);
        root_controller = activity.findViewById(R.id.root_controller);
        mLockImg.setOnClickListener(lockClickListener);

        topAnimation = AnimationUtils.loadAnimation(activity,
                R.anim.player_translate_top);
        topEnterAnimation = AnimationUtils.loadAnimation(activity,
                R.anim.player_translate_top_enter);
        bottomAnimation = AnimationUtils.loadAnimation(activity,
                R.anim.player_translate_bottom);
        bottomEnterAnimation = AnimationUtils.loadAnimation(activity,
                R.anim.player_translate_bottom_enter);
        main_player_layout.setOnTouchListener(this);

    }

    private View.OnClickListener lockClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            lockAndUnlock();
        }
    };

    private void lockAndUnlock() {
        if (isLocked){
            //锁
            isLocked = false;
            mLockImg.setImageResource(R.mipmap.player_unlock);
            hide();
            mHandler.sendEmptyMessageDelayed(LOCKIMG_FADE_OUT_INFO,
                    sDefaultTimeout);
        }else {
            //解
            isLocked = true;
            mLockImg.setImageResource(R.mipmap.player_locked);
            show();
            mHandler.removeMessages(LOCKIMG_FADE_OUT_INFO);
        }
    }
    @Override
    public void hide() {
        if (mShowing) {
            mHandler.removeMessages(SHOW_PROGRESS);

//            setSlide(player_contorl_top_layout, View.GONE, Gravity.TOP);
//            setSlide(player_contorl_bottom_layout, View.GONE, Gravity.BOTTOM);


            startAnimation(player_contorl_top_layout,topAnimation,View.GONE);
            startAnimation(player_contorl_bottom_layout,bottomAnimation,View.GONE);

            if (orientation()) {
                if (isLocked){
                    setSlide(root_controller, View.GONE, Gravity.LEFT);
                }
            }

            mShowing = false;
        }
    }
    private void startAnimation(ConstraintLayout linearLayout, Animation animation,int finalState){
        linearLayout.startAnimation(animation);
        linearLayout.setVisibility(finalState);
    }
    @Override
    public void show(int timeout) {
        if (!mShowing) {

            setProgress();
            if (mPauseButton != null) {
                mPauseButton.requestFocus();
            }
            disableUnsupportedButtons();
//            setSlide(player_contorl_top_layout, View.VISIBLE, Gravity.TOP);
//            setSlide(player_contorl_bottom_layout, View.VISIBLE, Gravity.BOTTOM);
            startAnimation(player_contorl_bottom_layout,bottomEnterAnimation,View.VISIBLE);
            startAnimation(player_contorl_top_layout,topEnterAnimation,View.VISIBLE);

            //锁屏幕
            if (orientation()) {
                if (mLockImg != null){
                    setSlide(root_controller, View.VISIBLE, Gravity.LEFT);
                }
            }

            mShowing = true;
        }
        updatePausePlay();
        mHandler.sendEmptyMessage(SHOW_PROGRESS);

        Message msg = mHandler.obtainMessage(FADE_OUT);
        if (timeout != 0) {
            mHandler.removeMessages(FADE_OUT);
            mHandler.sendMessageDelayed(msg, timeout);
        }
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
    public void showLockImgVs(){
        if (root_controller.getVisibility() != View.VISIBLE){
            setSlide(root_controller, View.VISIBLE, Gravity.LEFT);
            mHandler.sendEmptyMessageDelayed(LOCKIMG_FADE_OUT_INFO,
                    sDefaultTimeout);
        }else {
            setSlide(root_controller, View.GONE, Gravity.LEFT);
            mHandler.removeMessages(LOCKIMG_FADE_OUT_INFO);
        }
    }


    private void setSlide(ConstraintLayout linearLayout, int finalState, int type) {
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
        if (mProgress != null) {
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
            isPause =true;
            mPlayer.start();
        }
        updatePausePlay();
    }

    private void disableUnsupportedButtons() {
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
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
        attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
        activity.getWindow().setAttributes(attrs);
        activity.getWindow().getDecorView().setSystemUiVisibility(View.VISIBLE);
        mMainLayout.getLayoutParams().height = activity.getWindowManager().getDefaultDisplay().getWidth() * 9 / 16;
        mMainLayout.getLayoutParams().width = FrameLayout.LayoutParams.MATCH_PARENT;
        ijkVideoView.toggleAspectRatio(IRenderView.AR_16_9_FIT_PARENT);
    }

    /**
     * 设置全屏
     */
    public void setFullScreen() {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        activity.getWindow().setAttributes(attrs);
        activity.getWindow().getDecorView().setSystemUiVisibility(View.INVISIBLE);
        mMainLayout.getLayoutParams().height = FrameLayout.LayoutParams.MATCH_PARENT;
        mMainLayout.getLayoutParams().width = FrameLayout.LayoutParams.MATCH_PARENT;
        ijkVideoView.toggleAspectRatio(IRenderView.AR_ASPECT_FILL_PARENT);
    }
    public boolean orientation(){
        return activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (!isLocked()){
            showLockImgVs();
            return isLocked();
        }
        ijkVideoView.toggleMediaControlsVisiblity();
        return false;
    }



}
