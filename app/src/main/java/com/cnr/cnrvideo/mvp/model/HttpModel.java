/*
 * Copyright 2017 JessYan
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
package com.cnr.cnrvideo.mvp.model;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;


import com.cnr.basemodule.integration.IRepositoryManager;
import com.cnr.basemodule.mvp.BaseModel;
import com.cnr.cnrvideo.fragment.Webservice;
import com.cnr.cnrvideo.fragment.entity.BaseResponse;
import com.cnr.cnrvideo.fragment.entity.User;
import com.cnr.cnrvideo.fragment.response.VideoListResponse;
import com.cnr.cnrvideo.mvp.contract.HttpContract;

import java.util.List;
import java.util.Map;


import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import timber.log.Timber;


public class HttpModel extends BaseModel implements HttpContract.Model{


    @Inject
    public HttpModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }


//    public void getVideoListResponse(Map<String, String> reqMap, Observer<? extends BaseResponse> observer) {
//        Observable<VideoListResponse> videoListResponse = mRepositoryManager.
//                obtainRetrofitService(Webservice.class).getVideoListResponse(reqMap);
//        doRequest(videoListResponse,observer);
//    }
//
//    private void doRequest(Observable<? extends BaseResponse> observable, Observer subscriber) {
//        Observable<? extends BaseResponse> observable1 =
//                observable
//                        .subscribeOn(Schedulers.io())
//                        .unsubscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread());
//        observable1.subscribe(subscriber);
//    }
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void onPause() {
        Timber.d("Release Resource");
    }

    @Override
    public Observable<List<User>> getUsers(int lastIdQueried, boolean update) {
        return null;
    }
}
