package com.cnr.basemodule.utils;

import io.reactivex.disposables.Disposable;

/**
 * 事件回调接口
 */

public interface HttpDataListener<T> {

    void onSubscribe(Disposable d);

    void onNext(T t);

    void onError(Throwable t);

    void onCompleted();
}
