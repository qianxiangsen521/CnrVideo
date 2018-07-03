package com.cnr.cnrvideo;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.cnr.basemodule.activity.BaseActivity;
import com.cnr.coremodule.widget.media.AndroidMediaController;
import com.cnr.coremodule.widget.media.IjkVideoView;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class MainActivity extends BaseActivity {
    private boolean mBackPressed;
    private IjkVideoView mVideoView;
    private String mVideoPath = "http://117.139.20.20:6410/28000001/00000000000700000000000011176389";
    private AndroidMediaController mMediaController;
    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initEventAndData(Bundle savedInstanceState) {
        initView();
    }

    private void initView() {
        getSupportActionBar().hide();
        mMediaController = new AndroidMediaController(this);
        // init player
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        mVideoView = findViewById(R.id.ijkVideoView);
        mVideoView.setMediaController(mMediaController);
        mVideoView.setVideoPath(mVideoPath);
        mVideoView.start();
    }

    @Override
    protected void requestData() {

    }

    @Override
    public void onBackPressed() {
        mBackPressed = true;

        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBackPressed ) {
            mVideoView.stopPlayback();
            mVideoView.release(true);
        }
        IjkMediaPlayer.native_profileEnd();
    }




}
