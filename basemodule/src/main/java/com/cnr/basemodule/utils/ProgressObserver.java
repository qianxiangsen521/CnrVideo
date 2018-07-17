package com.cnr.basemodule.utils;

import android.content.Context;

import com.cnr.basemodule.base.BaseApplication;

import java.io.IOException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.adapter.rxjava2.HttpException;

public class ProgressObserver<T> implements Observer<T> {


    private HttpDataListener httpDataListner;
    protected Disposable disposable;
    protected String errMsg = "";
    public ProgressObserver(HttpDataListener httpDataListner) {
        this.httpDataListner = httpDataListner;
    }


    @Override
    public void onSubscribe(Disposable d) {
        disposable = d;
        httpDataListner.onSubscribe(d);
    }


    /**
     * 完成，隐藏ProgressDialog
     */
    @Override
    public void onComplete() {
        httpDataListner.onCompleted();
        disposeIt();
    }

    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     *
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        httpDataListner.onError(e);
        if (!NetWorkUtil.isNetworkConnected()) {
            errMsg = "网络连接出错,";
        } else if (e instanceof HttpException) {
            errMsg = "网络请求出错,";
        } else if (e instanceof IOException) {
            errMsg = "网络出错,";
        }

        disposeIt();

    }

    /**
     * 将onNext方法中的返回结果交给Activity或Fragment自己处理
     *
     * @param t 创建Subscriber时的泛型类型
     */
    @Override
    public void onNext(T t) {
        if (!NetWorkUtil.isNetworkConnected()) {//断开网络,请求缓存时判断
            ToastUtil.show(BaseApplication.getInstance(), "网络异常,请查看你的网络连接");
        }
        httpDataListner.onNext(t);
    }
    /**
     * 销毁disposable
     */
    private void disposeIt() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
            disposable = null;
        }
    }
}