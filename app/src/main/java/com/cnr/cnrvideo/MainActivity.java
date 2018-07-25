package com.cnr.cnrvideo;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.cnr.basemodule.base.BaseActivity;
import com.cnr.basemodule.di.component.AppComponent;
import com.cnr.cnrvideo.bean.PlaySource;
import com.cnr.cnrvideo.cnrfragment.PlaySourceFragment;
import com.cnr.cnrvideo.di.component.DaggerUserComponent;
import com.cnr.cnrvideo.di.module.HttpModule;
import com.cnr.cnrvideo.mvp.contract.HttpContract;
import com.cnr.cnrvideo.mvp.presenter.HttpPresenter;
import com.cnr.cnrvideo.response.PlayInfoResponse;
import com.cnr.coremodule.widget.media.AndroidMediaController;
import com.cnr.coremodule.widget.media.IjkVideoView;


import butterknife.BindView;
import butterknife.OnClick;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;


public class MainActivity extends BaseActivity<HttpPresenter> implements
        HttpContract.View,PlaySourceFragment.OnSelectSourceListener {
    private boolean mBackPressed;
    AndroidMediaController mMediaController;
    @BindView(R.id.ijkVideoView)
    IjkVideoView mVideoView;
    @BindView(R.id.player_source)
    ImageView player_source;
    private Fragment playSourceFragment;
    private FragmentManager fragmentManager;

    private int mFragCurrentIndex;

    public static final int FRAGMENT_PLAY_SOURCE = 1;

    private int playType = 1;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerUserComponent.builder().appComponent(appComponent)
                .httpModule(new HttpModule(this,this))
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
        mMediaController = new AndroidMediaController(this,playType);
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        mVideoView.setMediaController(mMediaController);


    }

    @OnClick(R.id.player_source)
    public void sOnClick(View view){
        mMediaController.showFragment();
        switchFragment(FRAGMENT_PLAY_SOURCE);
    }

    public void onBackPressed() {
        mBackPressed = true;
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMediaController.stopPlay(mBackPressed);
    }

    @Override
    public void startLoadMore() {

    }

    @Override
    public void endLoadMore() {

    }
    @Override
    public void showMessage(@NonNull String message) {
        mVideoView.setVideoPath("http://live.bctv.52ytv.cn/tv/2/133c96b/5a151dd0/bc4448b79bec4bf133840ec41d50e597.m3u8");
        mVideoView.start();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

       mMediaController.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mMediaController.isLocked()){
                mMediaController.setOrientation();
            }else{
                mMediaController.locked();
                mMediaController.setOrientation();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * fragment切换
     *
     * @param index
     */
    private void switchFragment(int index) {
        if (fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
        }
        Fragment fragmentNow = fragmentManager.findFragmentByTag("player"
                + mFragCurrentIndex);
        if (fragmentNow != null) {
            fragmentManager.beginTransaction().hide(fragmentNow).commit();
        }
        Fragment fragment = fragmentManager.findFragmentByTag("player" + index);
        if (fragment == null) {
            fragment = createMainFragment(index);
            fragmentManager
                    .beginTransaction()
                    .add(R.id.player_contorl_source_prog, fragment,
                            "player" + index).commit();
        } else {
            fragmentManager.beginTransaction().show(fragment).commit();
        }
        mFragCurrentIndex = index;
    }


    /**
     * @param index
     */
    public Fragment createMainFragment(int index) {
        Fragment fragment = null;
        switch (index) {

            case FRAGMENT_PLAY_SOURCE:
                playSourceFragment = new PlaySourceFragment();
                ((PlaySourceFragment) playSourceFragment)
                        .setOnSelectSourceListener(this);
                fragment = playSourceFragment;
                break;
//            case FRAGMENT_CHANNEL:
//                channelListFragment = new ChannelListFragment();
//                ((ChannelListFragment) channelListFragment)
//                        .setSelectedListener(this);
//                fragment = channelListFragment;
//
//                break;
//            case FRAGMENT_DLNA:
//                dlnaFragment = new DLNAFragment();
//                ((DLNAFragment) dlnaFragment).setOnDlnaSelectedListener(this);
//                fragment = dlnaFragment;
//
//                break;
//            case FRAGMENT_FEEDBACK:
//                feedBackFragment = new FeedBackFragment();
//                ((FeedBackFragment) feedBackFragment)
//                        .setSubmitFeedbackListener(this);
//                fragment = feedBackFragment;
//                break;
//            case FRAGMENT_PROGAM:
//                progamListFragment = new WeekProgamListFragment();
//                ((WeekProgamListFragment) progamListFragment)
//                        .setDayProgamSelectedListener(this);
//                fragment = progamListFragment;
//                break;
//            case FRAGMENT_SHOT_SHARE:
//                screenShotFragment = new ScreenShotFragment();
//                fragment = screenShotFragment;
//                break;
            default:
                break;
        }
        return fragment;
    }

    @Override
    public void onSelectSource(PlaySource playSource, int selectedIndex) {

    }
}
