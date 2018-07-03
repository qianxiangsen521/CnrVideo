package com.cnr.basemodule.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.cnr.basemodule.R;
import com.cnr.basemodule.fragment.BaseFragment;
import com.cnr.basemodule.http.HttpDataListener;
import com.cnr.basemodule.response.BaseResponse;


import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;


public abstract class BaseActivity extends AppCompatActivity implements HttpDataListener<BaseResponse> {

    protected Activity mContext;
    private Unbinder mUnBinder;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        mContext = this;
        mUnBinder = ButterKnife.bind(this);
        initEventAndData(savedInstanceState);
        requestData();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnBinder.unbind();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * 获取 layout
     */
    @LayoutRes
    protected abstract int getLayout();

    /**
     * 请求网络数据
     */
    protected abstract void requestData();

    /**
     * 初始化数据和事件
     */
    protected abstract void initEventAndData(Bundle savedInstanceState);
    @Override
    public void onSubscribe(Disposable d) {
    }

    @Override
    public void onError(Throwable t) {
    }

    @Override
    public void onCompleted() {
    }

    @Override
    public void onNext(BaseResponse baseResponse) {

    }

    public void addFragment(int fragment_full, BaseFragment fragments) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.setCustomAnimations(R.anim.a_to_b_of_in,
                R.anim.a_back_b_of_out, R.anim.a_to_b_of_in,
                R.anim.a_back_b_of_out);
        ft.replace(fragment_full, fragments);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null)
                .commit();
    }
    public void removeFragment(BaseFragment welcomeFragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.remove(welcomeFragment);
        transaction.commit();
    }
    protected void removeFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() >= 1) {
            getSupportFragmentManager().popBackStack();
        }
    }
    @Override
    protected void onStop() {


        super.onStop();
    }

    protected final void startActivity(@NonNull Class<?> targetActivity) {

        Intent intent = new Intent(this, targetActivity);
        startActivity(intent);
    }
    protected final void startActivity(Bundle mBundle, @NonNull Class<?> targetActivity) {

        Intent intent = new Intent(this, targetActivity);
        if (null != mBundle) {
            intent.putExtras(mBundle);
        }
        startActivity(intent);
    }

    /**
     * 跳转到指定的Activity
     *
     * @param extraName      要传递的值的键名称
     * @param extraValue     要传递的String类型值
     * @param targetActivity 要跳转的目标Activity
     */
    public final void startActivity(@NonNull String extraName, @NonNull String extraValue, @NonNull Class<?> targetActivity) {
        if (!TextUtils.isEmpty(extraName)) {

            final Intent intent = new Intent(getApplicationContext(), targetActivity);
            intent.putExtra(extraName, extraValue);

            startActivity(intent);
        } else {
            throw new NullPointerException("传递的值的键名称为null或空");
        }
    }
    public final void startActivity(@NonNull String extraName,int extraValue,@NonNull Class<?> targetActivity) {
        if (!TextUtils.isEmpty(extraName)) {
            final Intent intent = new Intent(this, targetActivity);
            intent.putExtra(extraName, extraValue);
            startActivity(intent);
        } else {
            throw new NullPointerException("传递的值的键名称为null或空");
        }
    }

}
