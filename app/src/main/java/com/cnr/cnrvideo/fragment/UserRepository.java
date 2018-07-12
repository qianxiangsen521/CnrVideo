package com.cnr.cnrvideo.fragment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.cnr.cnrvideo.fragment.response.VideoListResponse;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@Singleton
public class UserRepository {


    private final Webservice webservice;

//    @Inject
    public UserRepository(Webservice webservice) {
        this.webservice = webservice;
    }

    public LiveData<VideoListResponse> getUser(Map<String,String> map){
        final MutableLiveData<VideoListResponse> data = new MutableLiveData<>();

//        webservice.getVideoListResponse(map).enqueue(new Callback<VideoListResponse>() {
//            @Override
//            public void onResponse(Call<VideoListResponse> call,
//                                   Response<VideoListResponse> response) {
//                data.setValue(response.body());
//            }
//
//            @Override
//            public void onFailure(Call<VideoListResponse> call, Throwable t) {
//
//            }
//        });
        return data;
    }
}


