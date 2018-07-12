package com.cnr.cnrvideo.di.module;

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
    public HttpModule(HttpContract.View view) {
        this.view = view;
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
