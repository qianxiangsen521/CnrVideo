package com.cnr.cnrvideo.fragment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.cnr.cnrvideo.fragment.response.VideoListResponse;

import java.util.Map;

import javax.inject.Inject;

public class UserProfileViewModel extends ViewModel{


    private LiveData<VideoListResponse> listResponseLiveData;
    private UserRepository userRepository;

//    @Inject
    public UserProfileViewModel(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public LiveData<VideoListResponse> getListResponseLiveData() {
        return listResponseLiveData;
    }

    public void setListResponseLiveData(LiveData<VideoListResponse> listResponseLiveData) {
        this.listResponseLiveData = listResponseLiveData;
    }

    public void init(Map<String,String> map){
        if (this.listResponseLiveData != null){
            return;
        }
        listResponseLiveData = userRepository.getUser(map);
    }
}
