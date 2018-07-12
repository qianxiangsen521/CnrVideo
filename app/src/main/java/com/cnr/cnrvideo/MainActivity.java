package com.cnr.cnrvideo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;

import com.cnr.basemodule.base.BaseActivity;
import com.cnr.basemodule.di.component.AppComponent;
import com.cnr.cnrvideo.di.component.DaggerUserComponent;
import com.cnr.cnrvideo.di.module.HttpModule;
import com.cnr.cnrvideo.mvp.contract.HttpContract;
import com.cnr.cnrvideo.mvp.presenter.HttpPresenter;
import com.cnr.coremodule.widget.media.AndroidMediaController;
import com.cnr.coremodule.widget.media.IjkVideoView;


import butterknife.BindView;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class MainActivity extends BaseActivity<HttpPresenter> implements HttpContract.View{
    private boolean mBackPressed;
    private String mVideoPath = "http://117.139.20.20:6410/28000001/00000000000700000000000011176389";
    AndroidMediaController mMediaController;
    @BindView(R.id.ijkVideoView)
    IjkVideoView mVideoView;
    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerUserComponent.builder().appComponent(appComponent)
                .httpModule(new HttpModule(this))
                .build().inject(this);

    }



    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        getSupportActionBar().hide();
        // init player
        mMediaController = new AndroidMediaController(this);
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        mVideoView.setMediaController(mMediaController);
        mVideoView.setVideoPath(mVideoPath);
        mVideoView.start();
    }

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

    @Override
    public void startLoadMore() {

    }

    @Override
    public void endLoadMore() {

    }

    @Override
    public void showMessage(@NonNull String message) {

    }
}
