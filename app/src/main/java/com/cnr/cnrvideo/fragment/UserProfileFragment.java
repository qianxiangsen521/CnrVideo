package com.cnr.cnrvideo.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cnr.basemodule.base.BaseFragment;
import com.cnr.basemodule.di.component.AppComponent;
import com.cnr.cnrvideo.R;
import com.cnr.cnrvideo.fragment.response.VideoListResponse;

import java.util.HashMap;
import java.util.Map;


public class UserProfileFragment extends BaseFragment {

    private static final String UID_KEY = "uid";
    private UserProfileViewModel userProfileViewModel;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        userProfileViewModel = ViewModelProviders.of(this).get(UserProfileViewModel.class);
    }


    protected void requestData() {
        Map<String,String> map = new HashMap<>();
        userProfileViewModel.init(map);
        userProfileViewModel.getListResponseLiveData().observe(this, new Observer<VideoListResponse>() {
            @Override
            public void onChanged(@Nullable VideoListResponse videoListResponse) {

            }
        });
    }


    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return null;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void setData(@Nullable Object data) {

    }
}
