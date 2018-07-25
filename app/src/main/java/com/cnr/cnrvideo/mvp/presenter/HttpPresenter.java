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
package com.cnr.cnrvideo.mvp.presenter;

import android.Manifest;
import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.SupportActivity;
import android.util.Log;

import com.cnr.basemodule.di.scope.ActivityScope;
import com.cnr.basemodule.mvp.BasePresenter;
import com.cnr.basemodule.utils.HttpDataListener;
import com.cnr.basemodule.utils.ProgressObserver;
import com.cnr.cnrvideo.R;
import com.cnr.cnrvideo.fragment.entity.BaseResponse;
import com.cnr.cnrvideo.mvp.contract.HttpContract;
import com.cnr.cnrvideo.mvp.model.HttpModel;
import com.cnr.cnrvideo.response.PlayInfoResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * ================================================
 * 展示 Presenter 的用法
 *
 * @see <a href="https://github.com/JessYanCoding/MVPArms/wiki#2.4.4">Presenter wiki 官方文档</a>
 * Created by JessYan on 09/04/2016 10:59
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
@ActivityScope
public class HttpPresenter extends BasePresenter<HttpContract.Model, HttpContract.View>
        implements HttpDataListener<com.cnr.cnrvideo.response.BaseResponse>,
        EasyPermissions.PermissionCallbacks{

    private static final int RC_LOCATION_CONTACTS_PERM = 1001;

    private String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE};

    @Inject
    Activity activity;

    @Inject
    public HttpPresenter(HttpContract.Model model,HttpContract.View rootView) {
        super(model,rootView);
    }

    /**
     * 使用 2017 Google IO 发布的 Architecture Components 中的 Lifecycles 的新特性 (此特性已被加入 Support library)
     * 使 {@code Presenter} 可以与 {@link SupportActivity} 和 {@link Fragment} 的部分生命周期绑定
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        requestUsers();//打开 App 时自动加载列表
    }

    public void requestUsers() {
        requestPermisstionTask();
    }

    @AfterPermissionGranted(RC_LOCATION_CONTACTS_PERM)
    public void requestPermisstionTask() {
        //允许程序访问手机状态信息 Manifest.permission.READ_PHONE_STATE
        if (EasyPermissions.hasPermissions(activity, perms)) {

            Map<String,String> map = new HashMap<>();
            map.put("playType","0");
            map.put("playId","64");
            mModel.getPlayInfoResponse(map,new ProgressObserver(this));

        } else {
            EasyPermissions.requestPermissions(activity,
                    "SH",
                    RC_LOCATION_CONTACTS_PERM, perms);
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);

    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(activity, perms)) {
            new AppSettingsDialog.Builder(activity)
                    .setTitle("权限申请")
                    .setRationale("权限申请")
                    .setNegativeButton("取消").build().show();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSubscribe(Disposable d) {
        mRootView.startLoadMore();
    }

    @Override
    public void onNext(com.cnr.cnrvideo.response.BaseResponse baseResponse) {
        if (baseResponse instanceof PlayInfoResponse){
            PlayInfoResponse playInfoResponse = (PlayInfoResponse)baseResponse;
            Log.d("TAGTAG", "onNext: "+playInfoResponse.getPlaySources().get(0).getSourceUrl());
            mRootView.showMessage(playInfoResponse.getPlaySources().get(0).getSourceUrl());

        }
    }

    @Override
    public void onError(Throwable t) {

    }

    @Override
    public void onCompleted() {
        mRootView.endLoadMore();
    }
}
