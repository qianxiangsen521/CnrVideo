package com.cnr.cnrvideo.di.module;

import android.app.Activity;

import com.cnr.basemodule.di.scope.ActivityScope;
import com.cnr.cnrvideo.mvp.contract.HttpContract;
import com.cnr.cnrvideo.mvp.model.HttpModel;

import dagger.Module;
import dagger.Provides;

@Module
public class HttpModule {

    private HttpContract.View view;

    /**
     * 构建 UserModule 时,将 View 的实现类传进来,这样就可以提供 View 的实现类给 Presenter
     *
     */
    private Activity activity;
    public HttpModule(HttpContract.View view, Activity activity) {
        this.view = view;
        this.activity =activity;
    }

    @ActivityScope
    @Provides
    Activity provideActivity(){
        return activity;
    }

    @ActivityScope
    @Provides
    HttpContract.View provideUserView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    HttpContract.Model provideUserModel(HttpModel model) {
        return model;
    }

}
